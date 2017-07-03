package by.kipind.game.olympicgames.scenes.gameScene;

import java.io.IOException;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import android.view.MotionEvent;
import by.kipind.game.olympicgames.GameSettings;
import by.kipind.game.olympicgames.R;
import by.kipind.game.olympicgames.ResourcesManager;
import by.kipind.game.olympicgames.SceneManager;
import by.kipind.game.olympicgames.SceneManager.SceneType;
import by.kipind.game.olympicgames.sceneElements.ChatWin;
import by.kipind.game.olympicgames.sceneElements.DartSteck;
import by.kipind.game.olympicgames.sceneElements.DartTarget;
import by.kipind.game.olympicgames.sceneElements.ShootAngleSelector;
import by.kipind.game.olympicgames.sceneElements.ShortRecordsTable;
import by.kipind.game.olympicgames.scenes.BaseScene;
import by.kipind.game.olympicgames.sprite.buttons.AnimBtn;
import by.kipind.game.olympicgames.sprite.buttons.BtnGoLeft;
import by.kipind.game.olympicgames.sprite.buttons.BtnGoRight;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ArcheryGS extends BaseScene implements IOnSceneTouchListener {
    private static final String GAME_TYPE = "ARCHERY";
    private static final String GAME_LVL_FILE_PATH = "level/archery.lvl";

    private static int STEPS_PER_SECOND = 60;
    // ---------
    private static final String TAG_ENTITY = "entity";
    private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
    private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
    private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";

    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TARGET = "target";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DART = "dart";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LINE = "line";

    private final int lvlWidth = 800;
    private final int lvlHeight = 450;

    // ----------GAME
    protected Body shBodyR;

    private PhysicsWorld physicsWorld;
    private AnimatedSprite player;
    private Sprite target;
    private Sprite dart;
    private Sprite line;
    private Sprite raundResFon;

    // -----------HUD---------
    private HUD gameHUD;
    private ShortRecordsTable rt;
    private ChatWin cw;
    private ShootAngleSelector sa;
    private DartSteck ds;
    private DartTarget dt;
    private BtnGoRight hudSetAngleRight;
    private BtnGoLeft hudShootLeft;

    private Sprite hudAreaBordersBl;
    private Sprite hudAreaBlackAlpha;
    private Sprite hudAreaBlackAlphaUp;

    private Sprite hudBtnReplay;
    private Sprite hudBtnBack;

    private Text txTotalScore;
    private Text txShootScore;
    private Text worldRecLabel;
    private Text roundResLabel;

    private int totalScore = 0, shootScore = 0;

    // -------
    private Float wPersonalRecord;
    private Integer tCounter = 0;
    Vector2 ufoStartLeftXY;

    private float upDownNormVal = 0, leftRightNormVal = 0;
    private float targetShag = 0, dartShag = 0;
    private boolean goFlag = false;

    @Override
    public void createScene() {
	this.wPersonalRecord = GameSettings.W_RECORD_ARCHERY;

	createBackground();
	createHUD();
	createPhysics();

	loadLevel();
	ResourcesManager.getInstance().playSoundFromStack("stadion_priv");

	setOnSceneTouchListener(this);

    }

    @Override
    public void onBackKeyPressed() {
	SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneType getSceneType() {
	return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene() {
	camera.setHUD(null);
	camera.setCenter(SCENE_WIDTH / 2, SCENE_HEIGHT / 2);
	camera.setChaseEntity(null);
	resourcesManager.gameGraf.clear();
    }

    private void createBackground() {

	attachChild(new Sprite(120, this.lvlHeight / 2f, resourcesManager.gameGraf.get("game_background_region"), vbom));
	attachChild(new Sprite(360, this.lvlHeight / 2f, resourcesManager.gameGraf.get("game_background_region"), vbom));
	attachChild(new Sprite(580, this.lvlHeight / 2f, resourcesManager.gameGraf.get("game_background_region"), vbom));
	attachChild(new Sprite(820, this.lvlHeight / 2f, resourcesManager.gameGraf.get("game_background_region"), vbom));

    }

    private void createHUD() {
	gameHUD = new HUD();

	cw = new ChatWin(String.valueOf(GameSettings.W_RECORD_ARCHERY), 10, SCENE_HEIGHT, 5, vbom);

	ds = new DartSteck(10, 0, 0, vbom);
	ds.setPosition(cw.getX() + cw.getWidth() / 2f + ds.getWidth() / 2 + 3, SCENE_HEIGHT * 1f - ds.getHeight() / 2);

	dt = new DartTarget(0, 0, vbom);
	dt.setPosition(ds.getX() + ds.getWidth() / 2f + dt.getWidth() / 2, SCENE_HEIGHT * 1f - dt.getHeight() / 2);

	raundResFon = new Sprite(SCENE_WIDTH / 2f, SCENE_HEIGHT / 2f, resourcesManager.gameGraf.get("game_borders_hud_fon"), vbom);
	raundResFon.setVisible(false);

	hudAreaBordersBl = new Sprite(0, 0, resourcesManager.gameGraf.get("game_borders_bl_region"), vbom);
	hudAreaBordersBl.setScale(0.7f, 1.3f);
	hudAreaBordersBl.setPosition(cw.getX() + cw.getWidth() / 2f + (hudAreaBordersBl.getWidth() * 0.69f) / 2 + 3,
		ds.getY() - ds.getHeight() / 2f - (hudAreaBordersBl.getHeight() * 1.3f) / 2 - 3);

	hudAreaBlackAlpha = new Sprite(0, 0, resourcesManager.gameGraf.get("game_borders_hud_fon"), vbom) {
	    @Override
	    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
		    return SceneObjectTouch(this);
		}
		return false;
	    }
	};
	// hudAreaBlackAlpha.setScale(1f, 1f);
	hudAreaBlackAlpha.setScale(0.85f, 1f);
	hudAreaBlackAlpha.setPosition(dt.getX() + dt.getWidth() / 2f + (hudAreaBlackAlpha.getWidth() * 0.85f) / 2 + 3, SCENE_HEIGHT - (hudAreaBlackAlpha.getHeight() / 2f));
	hudAreaBlackAlpha.setVisible(false);

	hudAreaBlackAlphaUp = new Sprite(0, 0, resourcesManager.gameGraf.get("game_borders_hud_fon_up"), vbom);
	hudAreaBlackAlphaUp.setScale(0.85f, 1f);
	hudAreaBlackAlphaUp.setPosition(hudAreaBlackAlpha.getX(),
		hudAreaBlackAlpha.getY() + (hudAreaBlackAlpha.getHeight() * hudAreaBlackAlpha.getScaleY() - hudAreaBlackAlphaUp.getHeight()) / 2);

	hudSetAngleRight = new BtnGoRight(SCENE_WIDTH / 2f, SCENE_HEIGHT / 2f, (ITiledTextureRegion) ResourcesManager.getInstance().gameGraf.get("bt_arch_angle"), vbom) {
	    @Override
	    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
		    return SceneObjectTouch(this);
		} else if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
		    // redAreaR.setVisible(false);
		}
		return false;
	    }
	};
	hudShootLeft = new BtnGoLeft(SCENE_WIDTH / 2f, SCENE_HEIGHT / 2f, (ITiledTextureRegion) ResourcesManager.getInstance().gameGraf.get("bt_arch_shoot"), vbom) {
	    @Override
	    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
		    return SceneObjectTouch(this);
		} else if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
		    // redAreaL.setVisible(false);
		}
		return false;
	    }
	};
	hudShootLeft.setCurrentState(AnimBtn.BTN_STATE_UNACTIVE);

	hudShootLeft.setPosition(cw.getX() + cw.getWidth() / 2f - hudShootLeft.getHeight() / 2, hudShootLeft.getWidth() / 2 + 8);
	hudSetAngleRight.setPosition(SCENE_WIDTH - hudShootLeft.getX(), hudSetAngleRight.getWidth() / 2 + 8);

	hudBtnReplay = new Sprite(SCENE_WIDTH / 2, 0, resourcesManager.gameGraf.get("bt_replay"), vbom) {
	    @Override
	    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
		    return SceneObjectTouch(this);
		}
		return false;
	    }
	};
	hudBtnReplay.setScale(0.5f);

	hudBtnBack = new Sprite(SCENE_WIDTH / 2, 0, resourcesManager.gameGraf.get("bt_back"), vbom) {
	    @Override
	    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
		    return SceneObjectTouch(this);
		}
		return false;
	    }
	};
	hudBtnBack.setScale(0.5f);

	hudBtnBack.setPosition(SCENE_WIDTH - hudBtnBack.getWidth() * 0.3f, SCENE_HEIGHT - hudBtnBack.getHeight() * 0.25f);
	hudBtnReplay.setPosition(SCENE_WIDTH - hudBtnReplay.getWidth() * 0.3f, hudBtnBack.getY() - hudBtnBack.getHeight() * 0.25f - hudBtnReplay.getHeight() * 0.25f - 3);

	// CREATE TEXT info
	txTotalScore = new Text(0, 0, resourcesManager.font_pix_kir, "-fail: 0.1234567890 Null", new TextOptions(HorizontalAlign.LEFT), vbom);
	txTotalScore.setScale(0.5f);
	txTotalScore.setPosition(hudAreaBordersBl.getX() - (hudAreaBordersBl.getWidth() / 4), hudAreaBordersBl.getY() - (hudAreaBordersBl.getHeight() / 2));
	txTotalScore.setAnchorCenter(0, 0);
	txTotalScore.setText("0.0");

	txShootScore = new Text(0, 0, resourcesManager.font_pix_kir, "+ 0.1234567890 Null", new TextOptions(HorizontalAlign.LEFT), vbom);
	txShootScore.setScale(0.5f);
	txShootScore.setPosition(hudAreaBordersBl.getX(), hudAreaBordersBl.getY() - (hudAreaBordersBl.getHeight() * 1.3f));// ufoIco.getX() + ufoIco.getWidth() / 2, SCENE_HEIGHT / 8 * 1.5f);
	// ufoLeftText.setAnchorCenter(0, 0);
	txShootScore.setText("+");

	roundResLabel = new Text(0, 0, resourcesManager.font_pix_kir, context.getString(R.string.game_hud_rs_lb), new TextOptions(HorizontalAlign.CENTER), vbom);
	roundResLabel.setText(context.getString(R.string.game_hud_rs_lb));
	roundResLabel.setScale(0.5f);
	roundResLabel.setPosition(raundResFon.getX(), raundResFon.getY() + (raundResFon.getHeight() / 2f) - (roundResLabel.getHeight() / 5));
	roundResLabel.setVisible(false);

	worldRecLabel = new Text(0, 0, resourcesManager.font_pix_kir, context.getString(R.string.game_hud_wd_lb), new TextOptions(HorizontalAlign.CENTER), vbom);
	worldRecLabel.setText(context.getString(R.string.game_hud_wd_lb));
	worldRecLabel.setScale(0.5f);
	worldRecLabel.setPosition(hudAreaBlackAlpha.getX(), hudAreaBlackAlpha.getY() + (hudAreaBlackAlpha.getHeight() / 2f) - (worldRecLabel.getHeight() / 5));

	rt = new ShortRecordsTable("" + GameSettings.GAME_CODE + GameSettings.ACTIVITY_ID_ARCHERY, GameSettings.WEEK_OF_YEAR, GameSettings.GAME_PLAYER_NICK_ID,
		hudAreaBlackAlpha.getX(),
		hudAreaBlackAlpha.getY(), hudAreaBlackAlpha.getWidth() * 0.85f - 10, hudAreaBlackAlpha.getHeight() - (worldRecLabel.getHeight() / 2), 5, camera, vbom);
	rt.setVisible(false);


	sa = new ShootAngleSelector(0, 0, vbom);
	sa.setPosition(hudSetAngleRight.getX() - hudSetAngleRight.getWidth() / 1.5f, hudSetAngleRight.getY());//
	sa.Start();
	// --------------SCENE_WIDTH / 2, SCENE_HEIGHT / 2

	gameHUD.registerTouchArea(hudSetAngleRight);
	gameHUD.registerTouchArea(hudShootLeft);
	gameHUD.registerTouchArea(hudBtnReplay);
	gameHUD.registerTouchArea(hudBtnBack);
	gameHUD.registerTouchArea(hudAreaBlackAlpha);
	gameHUD.setTouchAreaBindingOnActionDownEnabled(true);

	gameHUD.attachChild(raundResFon);
	gameHUD.attachChild(roundResLabel);
	gameHUD.attachChild(hudAreaBordersBl);
	gameHUD.attachChild(hudAreaBlackAlpha);
	gameHUD.attachChild(hudAreaBlackAlphaUp);
	gameHUD.attachChild(hudSetAngleRight);
	gameHUD.attachChild(hudShootLeft);
	gameHUD.attachChild(hudBtnReplay);
	gameHUD.attachChild(hudBtnBack);
	gameHUD.attachChild(rt);
	gameHUD.attachChild(cw);
	gameHUD.attachChild(sa);
	gameHUD.attachChild(ds);
	gameHUD.attachChild(dt);
	gameHUD.attachChild(txTotalScore);
	gameHUD.attachChild(txShootScore);
	gameHUD.attachChild(worldRecLabel);

	// final float colLine = target.getX() - dart.getWidth() / 2;

	gameHUD.registerUpdateHandler(new TimerHandler(1 / 60f, true, new ITimerCallback() {
	    @Override
	    public void onTimePassed(final TimerHandler pTimerHandler) {
		if (goFlag) {
		    if ((target.collidesWith(dart) && dart.getX() > target.getX() - dart.getWidth() / 2) || dart.getX() > lvlWidth) {
			// :TODO
			ResourcesManager.getInstance().playSoundFromStack("arch_v_meshen");

			leftRightNormVal = (target.getY() - dart.getY()) / (target.getHeight() / 2); // Math.round(((dart.getY() - target.getY()) / (target.getHeight() / 2)) * 10) / 10;
			txTotalScore.setText(String.valueOf(leftRightNormVal));

			nextShoot();
		    } else {
			if (target.getY() < lvlHeight / 3 || target.getY() > lvlHeight - target.getHeight() / 2) {
			    targetShag = targetShag * (-1);
			}
			target.setPosition(target.getX(), target.getY() - targetShag);

			dart.setPosition(dart.getX() + dartShag, dart.getY());

		    }
		}
	    }
	}));

	camera.setHUD(gameHUD);
    }

    private void createPhysics() {
	physicsWorld = new FixedStepPhysicsWorld(STEPS_PER_SECOND, new Vector2(0, -9), false);
	physicsWorld.setContactListener(contactListener());
	registerUpdateHandler(physicsWorld);
    }

    private void loadLevel() {
	final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);
	final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);

	levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL) {
	    public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes,
		    final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException {
		final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
		final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);

		camera.setBounds(0, 0, width, height);
		camera.setBoundsEnabled(true);

		return ArcheryGS.this;
	    }
	});

	levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY) {
	    public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes,
		    final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException {
		final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
		final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
		final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);

		final Sprite levelObject;
		// final String asa = type.substring(0, 5);

		if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER)) {
		    player = new AnimatedSprite(dart.getX() - dart.getWidth() / 2, dart.getY(), (ITiledTextureRegion) ResourcesManager.getInstance().gameGraf.get("player_region"),
			    vbom);
		    // camera.setChaseEntity(player);
		    // player.setMaxSpeed(11);
		    levelObject = player;
		} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TARGET)) {
		    target = new Sprite(0, 0, ResourcesManager.getInstance().gameGraf.get("cell_profil"), vbom);
		    target.setPosition(SCENE_WIDTH - (target.getWidth() + hudBtnReplay.getWidth()), SCENE_HEIGHT - target.getHeight() / 2f);
		    levelObject = target;
		} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DART)) {
		    dart = new Sprite(line.getX(), line.getY(), ResourcesManager.getInstance().gameGraf.get("strela"), vbom);

		    levelObject = dart;
		} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LINE)) {
		    line = new Sprite(SCENE_WIDTH * 0.25f, SCENE_HEIGHT * 0.4f, ResourcesManager.getInstance().gameGraf.get("shoot_line"), vbom);

		    levelObject = line;
		} else {
		    throw new IllegalArgumentException();
		}

		levelObject.setCullingEnabled(true);

		return levelObject;
	    }
	});

	levelLoader.loadLevelFromAsset(activity.getAssets(), GAME_LVL_FILE_PATH);
    }

    private boolean SceneObjectTouch(Object touchedObj) {
	boolean res = false;
	if (touchedObj.equals(hudSetAngleRight) && (hudSetAngleRight.getCurrentTileIndex() != AnimBtn.BTN_STATE_UNACTIVE)) {

	    upDownNormVal = sa.Stop();
	    hudShootLeft.setCurrentState(AnimBtn.BTN_STATE_FREE);
	    hudSetAngleRight.setCurrentState(AnimBtn.BTN_STATE_UNACTIVE);
	    targetShag = 1.3f;
	    goFlag = true;
	    res = true;
	} else if (touchedObj.equals(hudShootLeft) && (hudShootLeft.getCurrentTileIndex() != AnimBtn.BTN_STATE_UNACTIVE)) {
	    // :TODO
	    ResourcesManager.getInstance().playSoundFromStack("arch_vistrel");

	    dartShag = 7f;
	    ds.minusObj();
	    hudShootLeft.setCurrentState(AnimBtn.BTN_STATE_UNACTIVE);
	    res = true;
	} else if (touchedObj.equals(hudBtnReplay) && hudBtnReplay.isVisible()) {
	    restartGameLvl();
	    res = true;
	} else if (touchedObj.equals(hudBtnBack) && hudBtnBack.isVisible()) {
	    onBackKeyPressed();
	    res = true;
	} else if (touchedObj.equals(hudAreaBlackAlpha)) {
	    hudAreaBlackAlpha.setVisible(!hudAreaBlackAlpha.isVisible());
	    hudAreaBlackAlphaUp.setVisible(!hudAreaBlackAlpha.isVisible());
	    rt.setVisible(hudAreaBlackAlpha.isVisible());
	    if (rt.isVisible()) {
		rt.reload();
	    }
	    res = true;
	}

	return res;
    }

    private ContactListener contactListener() {
	ContactListener contactListener = new ContactListener() {
	    public void beginContact(Contact contact) {
		final Fixture x1 = contact.getFixtureA();
		final Fixture x2 = contact.getFixtureB();

		if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null) {
		    if (x2.getBody().getUserData().equals("ufo_sh_l") && x1.getBody().getUserData().equals("ufo_l")) {

		    } else if (x2.getBody().getUserData().equals("ufo_sh_r") && x1.getBody().getUserData().equals("ufo_r")) {

		    }
		}
	    }

	    public void endContact(Contact contact) {
		final Fixture x1 = contact.getFixtureA();
		final Fixture x2 = contact.getFixtureB();

	    }

	    public void preSolve(Contact contact, Manifold oldManifold) {

	    }

	    public void postSolve(Contact contact, ContactImpulse impulse) {

	    }
	};
	return contactListener;

    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

	return false;
    }

    @Override
    public void setHUD() {
	// TODO Auto-generated method stub

    }

    private void nextShoot() {

	if (ds.getObjNow() != 0) {

	    hudSetAngleRight.setCurrentState(AnimBtn.BTN_STATE_FREE);
	    hudShootLeft.setCurrentState(AnimBtn.BTN_STATE_UNACTIVE);

	    if (Math.abs(upDownNormVal) > 1) {
		upDownNormVal = (upDownNormVal / Math.abs(upDownNormVal)) * 1.1f;
	    }
	    if (Math.abs(leftRightNormVal) > 1) {
		leftRightNormVal = (leftRightNormVal / Math.abs(leftRightNormVal)) * 1.1f;
	    }

	    dt.addMark(0.45f * dt.getWidth() * leftRightNormVal, 0.45f * dt.getHeight() * upDownNormVal);

	    shootScore = (int) (Math.round((1 - Math.sqrt(upDownNormVal * upDownNormVal + leftRightNormVal * leftRightNormVal)) * 1000));
	    if (shootScore < 0) {
		shootScore = 0;
	    }

	    totalScore = totalScore + shootScore;
	    txTotalScore.setText(String.valueOf(totalScore / 100d));
	    txShootScore.setText("+" + String.valueOf(shootScore / 100d));

	    dart.setPosition(line);
	    target.setPosition(SCENE_WIDTH - (target.getWidth() + hudBtnReplay.getWidth()), SCENE_HEIGHT - target.getHeight() / 2f);

	    dartShag = 0;
	    targetShag = 0;
	    upDownNormVal = 0;
	    leftRightNormVal = 0;

	    sa.Start();
	} else {
	    // :TODO
	    ResourcesManager.getInstance().playSoundFromStack("finish_aplodismenti");

	    goFlag = false;

	    if (Math.abs(upDownNormVal) > 1) {
		upDownNormVal = (upDownNormVal / Math.abs(upDownNormVal)) * 1.1f;
	    }
	    if (Math.abs(leftRightNormVal) > 1) {
		leftRightNormVal = (leftRightNormVal / Math.abs(leftRightNormVal)) * 1.1f;
	    }

	    shootScore = (int) Math.round((1 - Math.sqrt(upDownNormVal * upDownNormVal + leftRightNormVal * leftRightNormVal)) * 1000);
	    if (shootScore < 0) {
		shootScore = 0;
	    }
	    
	    totalScore = totalScore + shootScore;
	    txTotalScore.setText(String.valueOf(totalScore / 100d));
	    txShootScore.setText("+" + String.valueOf(shootScore / 100d));

	    dart.setPosition(line);
	    dartShag = 0;
	    targetShag = 0;

	    showRes();
	    if (wPersonalRecord == -1 || Float.valueOf((String) txTotalScore.getText()) > wPersonalRecord) {
		GameSettings.UpdateRecord(GameSettings.ACTIVITY_ID_ARCHERY, GAME_TYPE, (String) txTotalScore.getText());
	    }
	    cw.addLine(Float.valueOf((String) txTotalScore.getText()), true);

	}
    }

    private void showRes() {
	raundResFon.setVisible(true);
	roundResLabel.setVisible(true);
	hudAreaBordersBl.setPosition(raundResFon.getX(), raundResFon.getY() + raundResFon.getHeight() * 0.25f);
	txTotalScore.setPosition(hudAreaBordersBl.getX() - (hudAreaBordersBl.getWidth() / 4), hudAreaBordersBl.getY() - (hudAreaBordersBl.getHeight() / 2));

	hudBtnBack.setScale(0.7f);
	hudBtnReplay.setScale(0.7f);

	hudBtnBack.setPosition(raundResFon.getX() - hudBtnBack.getWidth() * 0.5f, raundResFon.getY() - raundResFon.getHeight() * 0.25f);
	hudBtnReplay.setPosition(raundResFon.getX() + hudBtnBack.getWidth() * 0.5f, raundResFon.getY() - raundResFon.getHeight() * 0.25f);
    }

    private void hideRes() {
	raundResFon.setVisible(false);
	roundResLabel.setVisible(false);
	hudAreaBordersBl.setPosition(cw.getX() + cw.getWidth() / 2f + (hudAreaBordersBl.getWidth() * 0.69f) / 2 + 3,
		ds.getY() - ds.getHeight() / 2f - (hudAreaBordersBl.getHeight() * 1.3f) / 2 - 3);
	txTotalScore.setPosition(hudAreaBordersBl.getX() - (hudAreaBordersBl.getWidth() / 4), hudAreaBordersBl.getY() - (hudAreaBordersBl.getHeight() / 2));

	hudBtnBack.setScale(0.5f);
	hudBtnReplay.setScale(0.5f);

	hudBtnBack.setPosition(SCENE_WIDTH - hudBtnBack.getWidth() * 0.3f, SCENE_HEIGHT - hudBtnBack.getHeight() * 0.25f);
	hudBtnReplay.setPosition(SCENE_WIDTH - hudBtnReplay.getWidth() * 0.3f, hudBtnBack.getY() - hudBtnBack.getHeight() * 0.25f - hudBtnReplay.getHeight() * 0.25f - 3);

    }

    private void restartGameLvl() {
	hideRes();
	ds.reSet();
	dt.reSet();
	hudSetAngleRight.setCurrentState(AnimBtn.BTN_STATE_FREE);
	hudShootLeft.setCurrentState(AnimBtn.BTN_STATE_UNACTIVE);

	totalScore = 0;
	txTotalScore.setText(String.valueOf(totalScore));
	txShootScore.setText("----");

	dart.setPosition(line);
	target.setPosition(SCENE_WIDTH - (target.getWidth() + hudBtnReplay.getWidth()), SCENE_HEIGHT - target.getHeight() / 2f);

	dartShag = 0;
	targetShag = 0;
	upDownNormVal = 0;
	leftRightNormVal = 0;

	sa.Start();
	goFlag = true;

    }

}