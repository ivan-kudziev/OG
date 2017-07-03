package by.kipind.game.olympicgames.scenes.gameScene;

import android.graphics.Color;
import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

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
import org.andengine.extension.physics.box2d.PhysicsConnector;
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

import java.io.IOException;

import by.kipind.game.olympicgames.GameSettings;
import by.kipind.game.olympicgames.R;
import by.kipind.game.olympicgames.ResourcesManager;
import by.kipind.game.olympicgames.SceneManager;
import by.kipind.game.olympicgames.SceneManager.SceneType;
import by.kipind.game.olympicgames.sceneElements.ChatWin;
import by.kipind.game.olympicgames.sceneElements.ShortRecordsTable;
import by.kipind.game.olympicgames.sceneElements.UfoSteck;
import by.kipind.game.olympicgames.scenes.BaseScene;
import by.kipind.game.olympicgames.sprite.Svetofor;
import by.kipind.game.olympicgames.sprite.Ufo;
import by.kipind.game.olympicgames.sprite.buttons.AnimBtn;
import by.kipind.game.olympicgames.sprite.buttons.BtnGoLeft;
import by.kipind.game.olympicgames.sprite.buttons.BtnGoRight;

public class ShootingGS extends BaseScene implements IOnSceneTouchListener {
    private static final String GAME_TYPE = "SHOOTING";
    private static final String GAME_LVL_FILE_PATH = "level/shooting.lvl";

    private static int STEPS_PER_SECOND = 60;
    // ---------
    private static final String TAG_ENTITY = "entity";
    private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
    private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
    private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";

    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_UFO_L = "ufo_l";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_UFO_L_SH = "ufo_l_sh";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_UFO_R = "ufo_r";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_UFO_R_SH = "ufo_r_sh";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SVETOFOR = "svetofor";
    private static final Object TAG_GAME_ELEMENT_FROM_FILE_SHOOT_AREA_L = "area_l";
    private static final Object TAG_GAME_ELEMENT_FROM_FILE_SHOOT_AREA_R = "area_r";
    private static final Object TAG_GAME_ELEMENT_FROM_FILE_SHOOT_RED_AREA_L = "areaR_l";
    private static final Object TAG_GAME_ELEMENT_FROM_FILE_SHOOT_RED_AREA_R = "areaR_r";

    // ----------
    private final int lvlWidth = 800;
    private final int lvlHeight = 450;

    private HUD gameHUD;
    protected Body shBodyL, shBodyR;
    private Ufo ufoL, ufoR;
    private Sprite ufoShL, ufoShR;
    private Sprite areaL, areaR, redAreaL, redAreaR;
    private Sprite ufoIco;
    private Svetofor svetofor;

    private ShortRecordsTable rt;
    private ChatWin cw;
    private UfoSteck ufoCount;

    private BtnGoRight hudShootRight;
    private BtnGoLeft hudShootLeft;

    private Sprite hudAreaBordersBl;
    private Sprite hudAreaBlackAlpha;
    private Sprite hudAreaBlackAlphaUp;

    /* private Sprite hudAreaBordersRd; private Sprite hudAreaBordersYl; private Sprite hudAreaBordersOr; */
    private Sprite gameMarkDown;
    // private Sprite ufoсounterEl;
    private Sprite hudBtnReplay;
    private Sprite hudBtnBack;
    private Sprite raundResFon;

    private PhysicsWorld physicsWorld;

    private AnimatedSprite player;

    private Text scoreText;
    private Text ufoLeftText;
    private Text worldRecLabel;
    private Text roundResLabel;

    private String trImage;

    private Float wPersonalRecord;
    private Integer tCounter = 0;
    private Integer ufoCounter;
    private float xValL, xValR;
    Vector2 ufoStartLeftXY, ufoStartRightXY;

    @Override
    public void createScene() {
	this.wPersonalRecord = GameSettings.W_RECORD_SHOOTING;
	this.ufoCounter = 18;

	createBackground();
	createHUD();
	createPhysics();

	loadLevel();
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

	attachChild(new Sprite(135, this.lvlHeight / 1.8f, resourcesManager.gameGraf.get("game_background_region"), vbom));
	attachChild(new Sprite(405, this.lvlHeight / 1.8f, resourcesManager.gameGraf.get("game_background_region"), vbom));
	attachChild(new Sprite(675, this.lvlHeight / 1.8f, resourcesManager.gameGraf.get("game_background_region"), vbom));
	attachChild(new Sprite(135, 0, resourcesManager.gameGraf.get("game_panel_region"), vbom));
	attachChild(new Sprite(405, 0, resourcesManager.gameGraf.get("game_panel_region"), vbom));
	attachChild(new Sprite(675, 0, resourcesManager.gameGraf.get("game_panel_region"), vbom));

	attachChild(new Sprite(this.lvlWidth / 4, this.lvlHeight / 2f, resourcesManager.gameGraf.get("shoot_tree"), vbom));
	attachChild(new Sprite(3 * this.lvlWidth / 4, this.lvlHeight / 2, resourcesManager.gameGraf.get("shoot_tree"), vbom));

	attachChild(new Sprite(55, this.lvlHeight / 3.5f, resourcesManager.gameGraf.get("tb_left"), vbom));
	attachChild(new Sprite(this.lvlWidth - 55, this.lvlHeight / 3.5f, resourcesManager.gameGraf.get("tb_right"), vbom));

    }

    private void createHUD() {
	gameHUD = new HUD();

	cw = new ChatWin(String.valueOf(GameSettings.W_RECORD_SHOOTING), 10, SCENE_HEIGHT, 5, vbom);

	raundResFon = new Sprite(SCENE_WIDTH / 2f, SCENE_HEIGHT * 0.5f, resourcesManager.gameGraf.get("game_borders_hud_fon"), vbom);
	raundResFon.setVisible(false);

	hudAreaBordersBl = new Sprite(0, 0, resourcesManager.gameGraf.get("game_borders_bl_region"), vbom);
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

	hudAreaBordersBl.setScale(0.75f, 1.3f);
	hudAreaBordersBl.setPosition(SCENE_WIDTH * 0.65f, SCENE_HEIGHT * 0.2f); // SCENE_WIDTH - hudAreaBordersBl.getWidth() * 1.5f, SCENE_HEIGHT / 8 * 1.5f

	hudAreaBlackAlpha.setScale(0.85f, 1f);
	hudAreaBlackAlpha.setPosition(cw.getX() + cw.getWidth() / 2f + (hudAreaBlackAlpha.getWidth() * 0.85f) / 2 + 3, SCENE_HEIGHT - (hudAreaBlackAlpha.getHeight() / 2f));
	hudAreaBlackAlpha.setVisible(false);

	hudAreaBlackAlphaUp = new Sprite(0, 0, resourcesManager.gameGraf.get("game_borders_hud_fon_up"), vbom);
	hudAreaBlackAlphaUp.setScale(0.85f, 1f);
	hudAreaBlackAlphaUp.setPosition(hudAreaBlackAlpha.getX(),
		hudAreaBlackAlpha.getY() + (hudAreaBlackAlpha.getHeight() * hudAreaBlackAlpha.getScaleY() - hudAreaBlackAlphaUp.getHeight()) / 2);

	ufoCount = new UfoSteck(this.ufoCounter, 0, 0, vbom);
	ufoCount.setPosition(hudAreaBlackAlpha.getX() + (hudAreaBlackAlpha.getWidth() * 0.85f) / 2 + ufoCount.getWidth() / 2 + 3, SCENE_HEIGHT - ufoCount.getHeight() / 2);

	ufoIco = new Sprite(0, 0, resourcesManager.gameGraf.get("shoot_ufo_ico"), vbom);
	ufoIco.setScale(0.8f);
	ufoIco.setPosition(hudAreaBordersBl.getWidth() * 1.5f, SCENE_HEIGHT / 8 * 1.5f);

	hudShootRight = new BtnGoRight(SCENE_WIDTH / 2f, SCENE_HEIGHT / 2f, (ITiledTextureRegion) ResourcesManager.getInstance().gameGraf.get("bt_shoot_right"), vbom) {
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
	hudShootLeft = new BtnGoLeft(SCENE_WIDTH / 2f, SCENE_HEIGHT / 2f, (ITiledTextureRegion) ResourcesManager.getInstance().gameGraf.get("bt_shoot_left"), vbom) {
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

	hudShootLeft.setPosition(cw.getX() + cw.getWidth() / 2f - hudShootLeft.getHeight() / 2, hudShootLeft.getWidth() / 2 + 8);
	hudShootRight.setPosition(SCENE_WIDTH - hudShootLeft.getX(), hudShootRight.getWidth() / 2 + 8);

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
	hudBtnReplay.setPosition(SCENE_WIDTH / 2 - hudBtnReplay.getWidth() / 3, hudBtnReplay.getWidth() / 4);

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
	scoreText = new Text(0, 0, resourcesManager.font_pix_kir, "-fail: 0.1234567890 Null", new TextOptions(HorizontalAlign.LEFT), vbom);
	scoreText.setScale(0.5f);
	scoreText.setPosition(hudAreaBordersBl.getX() - (hudAreaBordersBl.getWidth() / 4), hudAreaBordersBl.getY() - (hudAreaBordersBl.getHeight() / 2));
	scoreText.setAnchorCenter(0, 0);
	scoreText.setText("0.000");

	ufoLeftText = new Text(0, 0, resourcesManager.font_pix_kir, "/ 0.1234567890 Null", new TextOptions(HorizontalAlign.LEFT), vbom);
	ufoLeftText.setScale(0.5f);
	ufoLeftText.setPosition(SCENE_WIDTH * 0.5f, SCENE_HEIGHT * 0.05f);// ufoIco.getX() + ufoIco.getWidth() / 2, SCENE_HEIGHT / 8 * 1.5f);
	// ufoLeftText.setAnchorCenter(0, 0);
	ufoLeftText.setText("15\15");

	roundResLabel = new Text(0, 0, resourcesManager.font_pix_kir, context.getString(R.string.game_hud_rs_lb), new TextOptions(HorizontalAlign.CENTER), vbom);
	roundResLabel.setText(context.getString(R.string.game_hud_rs_lb));
	roundResLabel.setScale(0.5f);
	roundResLabel.setPosition(raundResFon.getX(), raundResFon.getY() + (raundResFon.getHeight() / 2f) - (roundResLabel.getHeight() / 5));
	roundResLabel.setVisible(false);

	worldRecLabel = new Text(0, 0, resourcesManager.font_pix_kir, context.getString(R.string.game_hud_wd_lb), new TextOptions(HorizontalAlign.CENTER), vbom);
	worldRecLabel.setText(context.getString(R.string.game_hud_wd_lb));
	worldRecLabel.setScale(0.5f);
	worldRecLabel.setPosition(hudAreaBlackAlpha.getX(), hudAreaBlackAlpha.getY() + (hudAreaBlackAlpha.getHeight() / 2f) - (worldRecLabel.getHeight() / 5));


	rt = new ShortRecordsTable("" + GameSettings.GAME_CODE + GameSettings.ACTIVITY_ID_SHOOTING, GameSettings.WEEK_OF_YEAR, GameSettings.GAME_PLAYER_NICK_ID,
		hudAreaBlackAlpha.getX(),
		hudAreaBlackAlpha.getY(), hudAreaBlackAlpha.getWidth() * 0.85f - 10, hudAreaBlackAlpha.getHeight() - (worldRecLabel.getHeight() / 2), 5, camera, vbom);
	rt.setVisible(false);

	// --------------

	gameHUD.registerTouchArea(hudShootRight);
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
	gameHUD.attachChild(hudShootRight);
	gameHUD.attachChild(hudShootLeft);
	gameHUD.attachChild(hudBtnReplay);
	gameHUD.attachChild(hudBtnBack);
	gameHUD.attachChild(rt);
	gameHUD.attachChild(cw);
	gameHUD.attachChild(scoreText);
	// gameHUD.attachChild(ufoLeftText);
	gameHUD.attachChild(worldRecLabel);
	gameHUD.attachChild(ufoCount);

	gameHUD.registerUpdateHandler(new TimerHandler(1 / 60f, true, new ITimerCallback() {
	    @Override
	    public void onTimePassed(final TimerHandler pTimerHandler) {
		if (ufoCounter > 0 && svetofor.getStatus() == Color.GREEN) {
		    tCounter++;
		    scoreText.setText(String.valueOf((double) tCounter / 1000));

		    if (ufoL.getPkStatus() != 1 && ufoR.getPkStatus() != 1) {
			if (ufoL.getPkStatus() == 0) {
			    throwUfo(-1);
			    resetUfo(1);

			} else if (ufoR.getPkStatus() == 0) {
			    throwUfo(1);
			    resetUfo(-1);

			}
		    }

		    if (ufoL.getPkStatus() == 1) {
			shBodyL.setTransform(ufoL.ufoBody.getPosition().x, shBodyL.getPosition().y + (ufoL.ufoBody.getPosition().x - xValL) * 0.3f, 0);
			ufoL.setScale(1 - (shBodyL.getPosition().y - ufoStartLeftXY.y) * 0.15f);
			ufoShL.setScale(1 - (shBodyL.getPosition().y - ufoStartLeftXY.y) * 0.15f);
			xValL = ufoL.ufoBody.getPosition().x;
			areaL.setPosition(areaL.getX(), ufoL.getY());
			areaR.setPosition(areaR.getX(), ufoL.getY());
			redAreaL.setPosition(areaL.getX(), ufoL.getY());
			redAreaR.setPosition(areaR.getX(), ufoL.getY());
		    }
		    if (ufoR.getPkStatus() == 1) {
			shBodyR.setTransform(ufoR.ufoBody.getPosition().x, shBodyR.getPosition().y + (-ufoR.ufoBody.getPosition().x + xValR) * 0.3f, 0);
			ufoR.setScale(1 - (shBodyR.getPosition().y - ufoStartRightXY.y) * 0.15f);
			ufoShR.setScale(1 - (shBodyR.getPosition().y - ufoStartRightXY.y) * 0.15f);
			xValR = ufoR.ufoBody.getPosition().x;
			areaL.setPosition(areaL.getX(), ufoR.getY());
			areaR.setPosition(areaR.getX(), ufoR.getY());
			redAreaL.setPosition(areaL.getX(), ufoR.getY());
			redAreaR.setPosition(areaR.getX(), ufoR.getY());

		    }

		}
	    }
	}));

	camera.setHUD(gameHUD);
    }

    private void createPhysics() {
	physicsWorld = new FixedStepPhysicsWorld(STEPS_PER_SECOND, new Vector2(0, -15), false);
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

		return ShootingGS.this;
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
		    player = new AnimatedSprite(SCENE_WIDTH / 2f, SCENE_HEIGHT / 5f, (ITiledTextureRegion) ResourcesManager.getInstance().gameGraf.get("player_region"), vbom);
		    player.setCurrentTileIndex(1);
		    camera.setChaseEntity(player);
		    // player.setMaxSpeed(11);
		    levelObject = player;
		} else if (type.equals(TAG_GAME_ELEMENT_FROM_FILE_SHOOT_AREA_L)) {
		    areaL = new Sprite(x, y, resourcesManager.gameGraf.get("shoot_area"), vbom);
		    // areaL.setScale(0.5f);
		    // areaL.setVisible(false);
		    levelObject = areaL;
		} else if (type.equals(TAG_GAME_ELEMENT_FROM_FILE_SHOOT_AREA_R)) {
		    areaR = new Sprite(x, y, resourcesManager.gameGraf.get("shoot_area"), vbom);
		    // areaR.setScale(0.5f);
		    // areaR.setVisible(false);
		    levelObject = areaR;
		} else if (type.equals(TAG_GAME_ELEMENT_FROM_FILE_SHOOT_RED_AREA_L)) {
		    redAreaL = new Sprite(x, y, resourcesManager.gameGraf.get("shoot_area_red"), vbom);
		    // areaL.setScale(0.5f);
		    redAreaL.setVisible(false);
		    levelObject = redAreaL;
		} else if (type.equals(TAG_GAME_ELEMENT_FROM_FILE_SHOOT_RED_AREA_R)) {
		    redAreaR = new Sprite(x, y, resourcesManager.gameGraf.get("shoot_area_red"), vbom);
		    // areaR.setScale(0.5f);
		    redAreaR.setVisible(false);
		    levelObject = redAreaR;
		} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_UFO_L)) {
		    ufoL = new Ufo(x, y, vbom, camera, physicsWorld);
		    ufoL.ufoBody.setUserData("ufo_l");
		    ufoL.setVisible(false);
		    xValL = ufoL.ufoBody.getPosition().x;
		    ufoStartLeftXY = new Vector2(ufoL.ufoBody.getPosition().x, ufoL.ufoBody.getPosition().y);
		    levelObject = ufoL;
		} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_UFO_L_SH)) {
		    ufoShL = new Sprite(x, y, resourcesManager.gameGraf.get("shoot_ufo_sh"), vbom);
		    ufoShL.setVisible(false);
		    levelObject = ufoShL;
		    shBodyL = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
		    shBodyL.setUserData("ufo_sh_l");
		    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, shBodyL, true, false));
		} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_UFO_R)) {
		    ufoR = new Ufo(x, y, vbom, camera, physicsWorld);
		    ufoR.ufoBody.setUserData("ufo_r");
		    ufoR.setVisible(false);
		    xValR = ufoR.ufoBody.getPosition().x;
		    ufoStartRightXY = new Vector2(ufoR.ufoBody.getPosition().x, ufoR.ufoBody.getPosition().y);
		    levelObject = ufoR;
		} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_UFO_R_SH)) {
		    ufoShR = new Sprite(x, y, resourcesManager.gameGraf.get("shoot_ufo_sh"), vbom);
		    ufoShR.setVisible(false);
		    levelObject = ufoShR;
		    shBodyR = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
		    shBodyR.setUserData("ufo_sh_r");
		    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, shBodyR, true, false));
		} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SVETOFOR)) {
		    svetofor = new Svetofor(SCENE_WIDTH / 2, SCENE_HEIGHT / 2, vbom, camera, physicsWorld);
		    svetofor.setRemoveFlag(true);
		    levelObject = svetofor;
		    svetofor.Start();

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
	if (touchedObj.equals(hudShootRight) && !redAreaR.isVisible() && (hudShootRight.getCurrentTileIndex() != AnimBtn.BTN_STATE_UNACTIVE)) {
	    // :TODO
	    player.setCurrentTileIndex(2);
	    ResourcesManager.getInstance().playSoundFromStack("shooting_shoot");
	    redAreaR.setVisible(true);
	    if (ufoR.getPkStatus() == 1 && ufoR.contains(areaR.getX(), areaR.getY())) {
		ufoR.onShoot();
		scoreChange();
	    } else if (ufoL.getPkStatus() == 1 && ufoL.contains(areaR.getX(), areaR.getY())) {
		ufoL.onShoot();
		scoreChange();
	    }
	    res = true;

	} else if (touchedObj.equals(hudShootLeft) && !redAreaL.isVisible() && (hudShootLeft.getCurrentTileIndex() != AnimBtn.BTN_STATE_UNACTIVE)) {
	    // :TODO
	    player.setCurrentTileIndex(0);
	    ResourcesManager.getInstance().playSoundFromStack("shooting_shoot");
	    redAreaL.setVisible(true);
	    if (ufoL.getPkStatus() == 1 && ufoL.contains(areaL.getX(), areaL.getY())) {
		ufoL.onShoot();
		scoreChange();
	    } else if (ufoR.getPkStatus() == 1 && ufoR.contains(areaL.getX(), areaL.getY())) {
		ufoR.onShoot();
		scoreChange();
	    }
	    res = true;
	    //
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
			if (ufoL.getPkStatus() == 1 && ufoL.getCurrentTileIndex() == 0) {

			    ufoL.setVisible(false);
			    ufoShL.setVisible(false);
			    ufoL.onFall();
			}
		    } else if (x2.getBody().getUserData().equals("ufo_sh_r") && x1.getBody().getUserData().equals("ufo_r")) {
			if (ufoR.getPkStatus() == 1 && ufoR.getCurrentTileIndex() == 0) {
			    ufoR.setVisible(false);
			    ufoShR.setVisible(false);
			    ufoR.onFall();
			}

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

    private void throwUfo(int i) {
	player.setCurrentTileIndex(1);

	int rad;
	redAreaL.setVisible(false);
	redAreaR.setVisible(false);

	rad = (int) (32 + Math.random() * 20);
	switch (i) {
	case -1:
	    if (ufoL.toThrow(rad, 34f - 0.3f * rad)) {
		ufoL.setVisible(true);
		ufoShL.setVisible(true);
	    }
	    break;
	case 1:
	    if (ufoR.toThrow(90 + rad, 9f + 0.3f * rad)) {
		ufoR.setVisible(true);
		ufoShR.setVisible(true);
	    }
	    break;

	default:
	    break;
	}
	// :TODO
	ResourcesManager.getInstance().playSoundFromStack("shooting_target_thow");

    };

    private void resetUfo(int i) {
	switch (i) {
	case -1:
	    ufoL.onFall();
	    shBodyL.setTransform(ufoStartLeftXY.x, ufoStartLeftXY.y - 1, 0);
	    ufoL.setPkStatus(0);
	    ufoL.ufoBody.setTransform(ufoStartLeftXY, 0);
	    ufoL.setPosition(55, 112);
	    ufoL.setScale(1 - (shBodyL.getPosition().y - ufoStartLeftXY.y) * 0.15f);
	    ufoShL.setPosition(55, 100);
	    ufoShL.setScale(1 - (shBodyL.getPosition().y - ufoStartLeftXY.y) * 0.15f);
	    xValL = ufoL.ufoBody.getPosition().x;
	    break;
	case 1:
	    ufoR.onFall();
	    shBodyR.setTransform(ufoStartRightXY.x, ufoStartRightXY.y - 1, 0);
	    ufoR.setPkStatus(0);
	    ufoR.ufoBody.setTransform(ufoStartRightXY, 0);
	    ufoR.setPosition(55, 112);
	    ufoR.setScale(1 - (shBodyR.getPosition().y - ufoStartRightXY.y) * 0.15f);
	    ufoShR.setPosition(55, 100);
	    ufoShR.setScale(1 - (shBodyR.getPosition().y - ufoStartRightXY.y) * 0.15f);
	    xValR = ufoR.ufoBody.getPosition().x;
	    break;

	default:
	    break;
	}

    };

    private void scoreChange() {
	ufoCounter--;
	ufoLeftText.setText(String.valueOf(ufoCounter) + "/15");
	ufoCount.minusObj();
	if (ufoCounter == 0) {
	    redAreaL.setVisible(false);
	    redAreaR.setVisible(false);
	    resetUfo(1);
	    resetUfo(-1);

	    showRes();
	    if (wPersonalRecord == -1 || Float.valueOf((String) scoreText.getText()) < wPersonalRecord) {
		GameSettings.UpdateRecord(GameSettings.ACTIVITY_ID_SHOOTING, GAME_TYPE, (String) scoreText.getText());
	    }
	    cw.addLine(Float.valueOf((String) scoreText.getText()), false);
	}

    }

    private void showRes() {
	raundResFon.setVisible(true);
	roundResLabel.setVisible(true);
	hudAreaBordersBl.setPosition(raundResFon.getX(), raundResFon.getY() + raundResFon.getHeight() * 0.15f);
	scoreText.setPosition(hudAreaBordersBl.getX() - (hudAreaBordersBl.getWidth() / 4), hudAreaBordersBl.getY() - (hudAreaBordersBl.getHeight() / 2));

	hudBtnBack.setScale(0.7f);
	hudBtnReplay.setScale(0.7f);

	hudBtnBack.setPosition(raundResFon.getX() - hudBtnBack.getWidth() * 0.5f, raundResFon.getY() - raundResFon.getHeight() * 0.25f);
	hudBtnReplay.setPosition(raundResFon.getX() + hudBtnBack.getWidth() * 0.5f, raundResFon.getY() - raundResFon.getHeight() * 0.25f);

    }

    private void hideRes() {
	raundResFon.setVisible(false);
	roundResLabel.setVisible(false);
	hudAreaBordersBl.setPosition(SCENE_WIDTH * 0.65f, SCENE_HEIGHT * 0.2f); // SCENE_WIDTH - hudAreaBordersBl.getWidth() * 1.5f, SCENE_HEIGHT / 8 * 1.5f
	scoreText.setPosition(hudAreaBordersBl.getX() - (hudAreaBordersBl.getWidth() / 4), hudAreaBordersBl.getY() - (hudAreaBordersBl.getHeight() / 2));

	hudBtnBack.setScale(0.5f);
	hudBtnReplay.setScale(0.5f);

	hudBtnBack.setPosition(SCENE_WIDTH - hudBtnBack.getWidth() * 0.3f, SCENE_HEIGHT - hudBtnBack.getHeight() * 0.25f);
	hudBtnReplay.setPosition(SCENE_WIDTH - hudBtnReplay.getWidth() * 0.3f, hudBtnBack.getY() - hudBtnBack.getHeight() * 0.25f - hudBtnReplay.getHeight() * 0.25f - 3);

    }

    private void restartGameLvl() {
	hideRes();
	this.ufoCounter = 18;
	ufoCount.reSet();
	this.tCounter = 0;

	    resetUfo(1);
	    resetUfo(-1);


	hudShootRight.setCurrentState(AnimBtn.BTN_STATE_FREE);
	hudShootLeft.setCurrentState(AnimBtn.BTN_STATE_FREE);

	redAreaL.setVisible(false);
	redAreaR.setVisible(false);

	this.wPersonalRecord = GameSettings.W_RECORD_SHOOTING;

	scoreText.setText("0.000");
	ufoLeftText.setText("30/30");
	svetofor.reSet();
	svetofor.Start();

	camera.setChaseEntity(player);

    }

}