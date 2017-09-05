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
import by.kipind.game.olympicgames.sceneElements.PowerInidcator;
import by.kipind.game.olympicgames.sceneElements.ShortRecordsTable;
import by.kipind.game.olympicgames.scenes.BaseScene;
import by.kipind.game.olympicgames.sprite.Barier;
import by.kipind.game.olympicgames.sprite.Player;
import by.kipind.game.olympicgames.sprite.Svetofor;
import by.kipind.game.olympicgames.sprite.buttons.AnimBtn;
import by.kipind.game.olympicgames.sprite.buttons.BtnRun;
import by.kipind.game.utils.FallowWebLink;

public class RunBarGS extends BaseScene implements IOnSceneTouchListener {
    private static final String GAME_TYPE = "RUN_BARIER";

    private static int STEPS_PER_SECOND = 60;
    // ---------
    private static final String TAG_ENTITY = "entity";
    private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
    private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
    private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";

    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 = "platform2";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_STOP = "stop";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_START = "start";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GROUND = "ground";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BARIER = "barier";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BARIER_DUB = "barier_dub";

    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SVETOFOR = "svetofor";

    private static final Object TAG_GAME_ELEMENT_FROM_FILE_METR_20 = "metr_20";
    private static final Object TAG_GAME_ELEMENT_FROM_FILE_METR_40 = "metr_40";
    private static final Object TAG_GAME_ELEMENT_FROM_FILE_METR_60 = "metr_60";
    private static final Object TAG_GAME_ELEMENT_FROM_FILE_METR_80 = "metr_80";
    private static final Object TAG_GAME_ELEMENT_FROM_FILE_METR_LINE = "metr_line";

    private static final String LVL_FILE_PATH = "level/run100bar.lvl";
    // ----------
    private final int lvlWidth = 3200;
    private final int lvlHeight = 450;

    private HUD gameHUD;
    private Body groundBody;
    private ShortRecordsTable rt;
    private ChatWin cw;

    private Sprite hudAreaBordersBl;
    private Sprite hudAreaBlackAlpha;
    private Sprite hudAreaBlackAlphaUp;

    /* private Sprite hudAreaBordersRd; private Sprite hudAreaBordersYl; private Sprite hudAreaBordersOr; */
    private Sprite hudBtnReplay;
    private Sprite hudBtnBack;
    private Sprite raundResFon;

    private Sprite hudAdClock;
    private Sprite hudAdHost;
    private Sprite hudAdBras;

    private BtnRun hudRunRight;
    private BtnRun hudRunJump;

    private PowerInidcator hudPowerInidcator;

    private PhysicsWorld physicsWorld;

    private Barier barier;
    private Player player;
    private Svetofor svetofor;

    private Text falsStart;
    private Text scoreText;
    private Text worldRecLabel;
    private Text roundResLabel;

    private boolean falseStartDisplayed = false;
    private boolean firstStep = true;
    private boolean rstBarFlag = false;

    private Float wPersonalRecord;
    private Integer tCounter = 0;

    private Long sysTimeStart = 0l, sysTimeFinish = 0l;
    private boolean drag;

    @Override
    public void createScene() {
	this.wPersonalRecord = GameSettings.W_RECORD_RUN_BARIER;

	createBackground();
	createHUD();
	createPhysics();

	loadLevel();
	ResourcesManager.getInstance().musicPlay(2);

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
	ResourcesManager.getInstance().musicPause(2);

	camera.setHUD(null);
	camera.setCenter(SCENE_WIDTH / 2, SCENE_HEIGHT / 2);
	camera.setChaseEntity(null);
	resourcesManager.gameGraf.clear();

    }

    private void createBackground() {
	attachChild(new Sprite(334, this.lvlHeight / 2, resourcesManager.gameGraf.get("game_background_region"), vbom));
	attachChild(new Sprite(1001, this.lvlHeight / 2, resourcesManager.gameGraf.get("game_background_region"), vbom));
	attachChild(new Sprite(1668, this.lvlHeight / 2, resourcesManager.gameGraf.get("game_background_region"), vbom));
	attachChild(new Sprite(2335, this.lvlHeight / 2, resourcesManager.gameGraf.get("game_background_region"), vbom));
	attachChild(new Sprite(3002, this.lvlHeight / 2, resourcesManager.gameGraf.get("game_background_region"), vbom));

	/*hudAdClock = new Sprite(3050, 310, resourcesManager.gameGraf.get("clock_small"), vbom) {
	    @Override
	    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
		    return SceneObjectTouch(this);
		}
		return false;
	    }
	};
	hudAdClock.setTag(1);

	hudAdBras = new Sprite(350, 310, resourcesManager.gameGraf.get("bras_small"), vbom) {
	    @Override
	    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
		    return SceneObjectTouch(this);
		}
		return false;
	    }
	};
	hudAdBras.setTag(2);

	hudAdHost = new Sprite(1910, 310, resourcesManager.gameGraf.get("host_small"), vbom) {
	    @Override
	    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
		    return SceneObjectTouch(this);
		}
		return false;
	    }
	};
	hudAdHost.setTag(3);

	registerTouchArea(hudAdHost);
	registerTouchArea(hudAdBras);
	registerTouchArea(hudAdClock);

	attachChild(hudAdHost);
	attachChild(hudAdBras);
	attachChild(hudAdClock);*/

    }

    private void createHUD() {
	gameHUD = new HUD();

	cw = new ChatWin(String.valueOf(GameSettings.W_RECORD_RUN_BARIER), 10, SCENE_HEIGHT, 5, vbom);

	raundResFon = new Sprite(SCENE_WIDTH / 2f, SCENE_HEIGHT * 0.7f, resourcesManager.gameGraf.get("game_borders_hud_fon"), vbom);
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

	hudAreaBordersBl.setScale(0.7f, 1.3f);
	hudAreaBordersBl.setPosition(SCENE_WIDTH - hudAreaBordersBl.getWidth() / 2, SCENE_HEIGHT / 8 * 5f);

	hudAreaBlackAlpha.setScale(1f, 1f);
	hudAreaBlackAlpha.setPosition(SCENE_WIDTH * 0.16f + (hudAreaBlackAlpha.getWidth() * 1f) / 2, SCENE_HEIGHT - (hudAreaBlackAlpha.getHeight() / 2f));
	// hudAreaBlackAlpha.setPosition((hudAreaBlackAlpha.getWidth() * 1f) / 2, SCENE_HEIGHT - (hudAreaBlackAlpha.getHeight() / 2f));
	hudAreaBlackAlpha.setVisible(false);

	hudAreaBlackAlphaUp = new Sprite(0, 0, resourcesManager.gameGraf.get("game_borders_hud_fon_up"), vbom) {
	    @Override
	    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
		    return SceneObjectTouch(this);
		}
		return false;
	    }
	};
	hudAreaBlackAlpha.setPosition(SCENE_WIDTH * 0.16f + (hudAreaBlackAlpha.getWidth() * 1f) / 2, SCENE_HEIGHT - (hudAreaBlackAlpha.getHeight() / 2f));
	hudAreaBlackAlphaUp.setPosition(hudAreaBlackAlpha.getX(),
		hudAreaBlackAlpha.getY() + (hudAreaBlackAlpha.getHeight() * hudAreaBlackAlpha.getScaleY() - hudAreaBlackAlphaUp.getHeight()) / 2);

	hudRunRight = new BtnRun(0, 0, (ITiledTextureRegion) ResourcesManager.getInstance().gameGraf.get("bt_run_region"), vbom) {
	    @Override
	    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
		    return SceneObjectTouch(this);
		}
		return false;

	    }
	};
	hudRunJump = new BtnRun(0, 0, (ITiledTextureRegion) ResourcesManager.getInstance().gameGraf.get("bt_run_jump_region"), vbom) {
	    @Override
	    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		return SceneObjectTouch(this);
	    }
	};

	hudRunJump.setPosition(cw.getX() + cw.getWidth() / 2f - hudRunJump.getHeight() / 2, hudRunJump.getWidth() / 2 + 8);
	hudRunRight.setPosition(SCENE_WIDTH - hudRunJump.getX(), hudRunRight.getWidth() / 2 + 8);

	hudPowerInidcator = new PowerInidcator(0, 0, 13f, camera, vbom);
	hudPowerInidcator.setPosition(SCENE_WIDTH / 2, hudRunRight.getY() + (hudRunRight.getHeight() - hudPowerInidcator.getHeight()) / 2);
	hudPowerInidcator.setVisible(false);

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
	falsStart = new Text(SCENE_WIDTH / 2, SCENE_HEIGHT / 2, resourcesManager.font_pix_kir, context.getString(R.string.game_false_start),
		new TextOptions(HorizontalAlign.CENTER), vbom);
	falsStart.setVisible(false);

	scoreText = new Text(0, 0, resourcesManager.font_pix_kir, "Time: 0.1234567890 Null" + context.getString(R.string.game_dq), new TextOptions(HorizontalAlign.LEFT), vbom);
	scoreText.setScale(0.5f);
	scoreText.setPosition(hudAreaBordersBl.getX() - (hudAreaBordersBl.getWidth() / 4), hudAreaBordersBl.getY() - (hudAreaBordersBl.getHeight() / 2));
	scoreText.setAnchorCenter(0, 0);
	scoreText.setText("0.000");
	scoreText.registerUpdateHandler(new TimerHandler(1 / 100f, true, new ITimerCallback() {

	    @Override
	    public void onTimePassed(final TimerHandler pTimerHandler) {
		groundBody.setTransform(player.body.getPosition().x, groundBody.getPosition().y, 0);
		if (!player.isFinish() && svetofor.getStatus() == Color.GREEN) {
		    tCounter++;
		    scoreText.setText(String.valueOf((double) tCounter / 1000));

		}
	    }
	}));
	roundResLabel = new Text(0, 0, resourcesManager.font_pix_kir, context.getString(R.string.game_hud_rs_lb), new TextOptions(HorizontalAlign.CENTER), vbom);
	roundResLabel.setText(context.getString(R.string.game_hud_rs_lb));
	roundResLabel.setScale(0.5f);
	roundResLabel.setPosition(raundResFon.getX(), raundResFon.getY() + (raundResFon.getHeight() / 2f) - (roundResLabel.getHeight() / 5));
	roundResLabel.setVisible(false);


	worldRecLabel = new Text(0, 0, resourcesManager.font_pix_kir, context.getString(R.string.game_hud_wd_lb), new TextOptions(HorizontalAlign.CENTER), vbom);
	worldRecLabel.setText(context.getString(R.string.game_hud_wd_lb));
	worldRecLabel.setScale(0.5f);
	worldRecLabel.setPosition(hudAreaBlackAlpha.getX(), hudAreaBlackAlpha.getY() + (hudAreaBlackAlpha.getHeight() / 2f) - (worldRecLabel.getHeight() / 5));

	rt = new ShortRecordsTable("" + GameSettings.GAME_CODE + GameSettings.ACTIVITY_ID_RUN_BARIER, GameSettings.WEEK_OF_YEAR, GameSettings.GAME_PLAYER_NICK_ID,
		hudAreaBlackAlpha.getX(),
		hudAreaBlackAlpha.getY(), hudAreaBlackAlpha.getWidth() - 10, hudAreaBlackAlpha.getHeight() - (worldRecLabel.getHeight() / 2), 5, camera, vbom);
	rt.setVisible(false);

	// --------------
	gameHUD.registerTouchArea(hudRunRight);
	gameHUD.registerTouchArea(hudRunJump);
	gameHUD.registerTouchArea(hudBtnReplay);
	gameHUD.registerTouchArea(hudBtnBack);
	gameHUD.registerTouchArea(hudAreaBlackAlphaUp);
	gameHUD.registerTouchArea(hudAreaBlackAlpha);
	gameHUD.setTouchAreaBindingOnActionDownEnabled(true);

	gameHUD.attachChild(raundResFon);
	gameHUD.attachChild(roundResLabel);
	gameHUD.attachChild(hudAreaBordersBl);
	gameHUD.attachChild(hudAreaBlackAlpha);
	gameHUD.attachChild(hudAreaBlackAlphaUp);
	gameHUD.attachChild(hudBtnReplay);
	gameHUD.attachChild(hudBtnBack);

	gameHUD.attachChild(hudRunRight);
	gameHUD.attachChild(hudRunJump);
	gameHUD.attachChild(hudPowerInidcator);
	gameHUD.attachChild(rt);
	gameHUD.attachChild(cw);
	gameHUD.attachChild(scoreText);
	gameHUD.attachChild(worldRecLabel);
	gameHUD.attachChild(falsStart);

	gameHUD.registerUpdateHandler(new TimerHandler(1 / 60f, true, new ITimerCallback() {
	    @Override
	    public void onTimePassed(final TimerHandler pTimerHandler) {
		hudPowerInidcator.changeValue(player.getSpeed() - 1);
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

		return RunBarGS.this;
	    }
	});

	levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY) {

	    public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes,
		    final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException {
		final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
		final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
		final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);

		final Sprite levelObject;
		if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2)) {
		    levelObject = new Sprite(x, y, resourcesManager.gameGraf.get("ge_ai_fon"), vbom);
		    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
		    body.setUserData("platform2");
		    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
		} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GROUND)) {
		    levelObject = new Sprite(x, y, resourcesManager.gameGraf.get("game_ground_line"), vbom);
		    levelObject.setVisible(false);
		    groundBody = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
		    groundBody.setUserData("ground");
		    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, groundBody, false, false));
		} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_START)) {
		    levelObject = new Sprite(x, y, resourcesManager.gameGraf.get("start_line"), vbom);

		} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_STOP)) {
		    levelObject = new Sprite(x, y, resourcesManager.gameGraf.get("stop_line"), vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
			    super.onManagedUpdate(pSecondsElapsed);

			    if (!player.isFinish() && player.collidesWith(new Sprite(x + this.getWidth() / 3f, y, resourcesManager.gameGraf.get("run_bar_dub"), vbom))) {
				ResourcesManager.getInstance().playSoundFromStack("finish_aplodismenti");
				hudRunRight.setCurrentState(AnimBtn.BTN_STATE_UNACTIVE);
				hudRunJump.setCurrentState(AnimBtn.BTN_STATE_UNACTIVE);
				player.onFinish();
				player.body.setLinearVelocity(0f, 0f);
				player.body.applyLinearImpulse(2f, 0, player.body.getPosition().x, player.body.getPosition().y);
				if (!showRes()) {
				    if (wPersonalRecord == -1 || Float.valueOf((String) scoreText.getText()) < wPersonalRecord) {
				    GameSettings.UpdateRecord(GameSettings.ACTIVITY_ID_RUN_BARIER, GAME_TYPE, (String) scoreText.getText());
				    }
				    cw.addLine(Float.valueOf((String) scoreText.getText()), false);
				} else {

				}
			    }
			}
		    };
		} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BARIER)) {
		    barier = new Barier(x, y, vbom, camera, physicsWorld) {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
			    super.onManagedUpdate(pSecondsElapsed);
			    if (this.getBarStatus() == -1 && rstBarFlag) {
				this.toUp();

			    }
			    if (this.getBarStatus() != -1 && player.collidesWith(new Sprite(x - barier.getWidth() / 8, y, resourcesManager.gameGraf.get("run_bar_dub"), vbom))) {
				// :TODO
				ResourcesManager.getInstance().playSoundFromStack("barier_upal");
				this.toFall();
				if (player.getFootContacts() != 1) {
				    player.setLinearSpeed(player.getSpeed() * 0.6f, player.body.getLinearVelocity().y);
				    player.setSpeedBeforJump(player.getSpeedBeforJump() * 0.7f);

				} else {
				    player.setLinearSpeed(1f, 0f);
				}

			    }

			}
		    };
		    levelObject = barier;

		} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BARIER_DUB)) {
		    levelObject = new Sprite(x, y, resourcesManager.gameGraf.get("run_bar_dub"), vbom);
		} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER)) {
		    player = new Player(x, y, vbom, camera, physicsWorld) {
			public void onFinish() {
			    super.onFinish();
			    stopAnimation(0);
			    body.setLinearVelocity(0, 0);
			    body.applyLinearImpulse(4, 0, body.getPosition().x, body.getPosition().y);

			}
		    };
		    camera.setChaseEntity(player);
		    player.setMaxSpeed(10);
		    levelObject = player;
		} else if (type.equals(TAG_GAME_ELEMENT_FROM_FILE_METR_20)) {
		    levelObject = new Sprite(x, y, resourcesManager.gameGraf.get("metraj_20"), vbom);

		} else if (type.equals(TAG_GAME_ELEMENT_FROM_FILE_METR_40)) {
		    levelObject = new Sprite(x, y, resourcesManager.gameGraf.get("metraj_40"), vbom);

		} else if (type.equals(TAG_GAME_ELEMENT_FROM_FILE_METR_60)) {
		    levelObject = new Sprite(x, y, resourcesManager.gameGraf.get("metraj_60"), vbom);

		} else if (type.equals(TAG_GAME_ELEMENT_FROM_FILE_METR_80)) {
		    levelObject = new Sprite(x, y, resourcesManager.gameGraf.get("metraj_80"), vbom);

		} else if (type.equals(TAG_GAME_ELEMENT_FROM_FILE_METR_LINE)) {
		    levelObject = new Sprite(x, y, resourcesManager.gameGraf.get("metraj_line"), vbom);

		} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SVETOFOR)) {
		    svetofor = new Svetofor(SCENE_WIDTH / 2, SCENE_HEIGHT / 5, vbom, camera, physicsWorld);
		    levelObject = svetofor;
		    svetofor.Start();

		}

		else {
		    throw new IllegalArgumentException();
		}

		levelObject.setCullingEnabled(true);

		return levelObject;
	    }
	});

	levelLoader.loadLevelFromAsset(activity.getAssets(), LVL_FILE_PATH);
    }

    private boolean SceneObjectTouch(Object touchedObj) {
	boolean res = false;

	if (firstStep && touchedObj.equals(hudRunRight)) {
	    sysTimeStart = System.currentTimeMillis();

	    if (!falseStartDisplayed && svetofor.getStatus() != Color.GREEN) {
		svetofor.stopAnimation();
		// :TODO
		ResourcesManager.getInstance().playSoundFromStack("false_start");
		falsStart.setVisible(true);
		hudRunRight.setCurrentState(AnimBtn.BTN_STATE_UNACTIVE);
		hudRunJump.setCurrentState(AnimBtn.BTN_STATE_UNACTIVE);
		showRes();

	    } else {
		svetofor.setVisible(false);
		hudPowerInidcator.setVisible(true);
		hudBtnReplay.setVisible(true);

	    }
	    firstStep = false;
	    rstBarFlag = false;
	}

	if (touchedObj.equals(hudRunRight) && (hudRunRight.getCurrentTileIndex() != AnimBtn.BTN_STATE_UNACTIVE) && !falseStartDisplayed) {
	    player.powFunctionRun();
	    firstStep = false;
	    res = true;
	} else if (touchedObj.equals(hudRunJump) && (hudRunJump.getCurrentTileIndex() != AnimBtn.BTN_STATE_UNACTIVE) && !firstStep) {
	    player.jumpUp();
	    res = true;
	} else if (touchedObj.equals(hudBtnReplay) && hudBtnReplay.isVisible()) {
	    restartGameLvl();
	    res = true;
	} else if (touchedObj.equals(hudBtnBack) && hudBtnBack.isVisible()) {
	    onBackKeyPressed();
	    res = true;
	} else if (touchedObj.equals(hudAreaBlackAlphaUp)) {
	    hudAreaBlackAlpha.setVisible(!hudAreaBlackAlpha.isVisible());
	    rt.setVisible(hudAreaBlackAlpha.isVisible());
	    if (rt.isVisible()) {
		rt.reload();
	    }
	    res = true;
	} else if (touchedObj.equals(hudAreaBlackAlpha) && hudAreaBlackAlpha.isVisible()) {
	    rt.reload();
	    res = true;
	} else if (touchedObj.equals(hudAdClock)) {
	    FallowWebLink.openWebLink(((Sprite) touchedObj).getTag());
	    res = true;
	} else if (touchedObj.equals(hudAdBras)) {
	    FallowWebLink.openWebLink(((Sprite) touchedObj).getTag());
	    res = true;
	} else if (touchedObj.equals(hudAdHost)) {
	    FallowWebLink.openWebLink(((Sprite) touchedObj).getTag());
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
		    if (x2.getBody().getUserData().equals("player") && x1.getBody().getUserData().equals("ground")) {
			player.increaseFootContacts();
		    }
		}
	    }

	    public void endContact(Contact contact) {
		final Fixture x1 = contact.getFixtureA();
		final Fixture x2 = contact.getFixtureB();

		if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null) {
		    if (x2.getBody().getUserData().equals("player") && x1.getBody().getUserData().equals("ground")) {
			player.decreaseFootContacts();
		    }
		}
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
	// TODO Auto-generated method stub
	return false;
    }

    private boolean showRes() {
	hudAreaBordersBl.setPosition(raundResFon.getX(), raundResFon.getY() + raundResFon.getHeight() * 0.15f);
	sysTimeFinish = System.currentTimeMillis();
	// Log.d("myLogs", "b " + Math.round(Float.valueOf((String) scoreText.getText())) + " " + ((sysTimeFinish - sysTimeStart) / 1000));

	if (Math.round(Float.valueOf((String) scoreText.getText()) * 1.3f) < ((sysTimeFinish - sysTimeStart) / 1000)) {
	    drag = true;
	    scoreText.setText(context.getString(R.string.game_dq));
	    hudAreaBordersBl.setScale(1.8f, 1.3f);

	    // hudAreaBordersBl.setWidth(raundResFon.getWidth());

	    scoreText.setScale(0.4f);
	    scoreText.setPosition(hudAreaBordersBl.getX() - (hudAreaBordersBl.getWidth() / 1.25f), hudAreaBordersBl.getY() - (hudAreaBordersBl.getHeight() / 2));

	} else {
	    // scoreText.setScale(0.5f);
	    // hudAreaBordersBl.setScale(0.7f, 1.3f);

	    scoreText.setPosition(hudAreaBordersBl.getX() - (hudAreaBordersBl.getWidth() / 4), hudAreaBordersBl.getY() - (hudAreaBordersBl.getHeight() / 2));
	}

	raundResFon.setVisible(true);
	roundResLabel.setVisible(true);

	hudBtnBack.setScale(0.7f);
	hudBtnReplay.setScale(0.7f);

	hudBtnBack.setPosition(raundResFon.getX() - hudBtnBack.getWidth() * 0.5f, raundResFon.getY() - raundResFon.getHeight() * 0.25f);
	hudBtnReplay.setPosition(raundResFon.getX() + hudBtnBack.getWidth() * 0.5f, raundResFon.getY() - raundResFon.getHeight() * 0.25f);

	return drag;
    }

    private void hideRes() {
	if (drag) {
	    drag = false;
	    scoreText.setScale(0.5f);
	    hudAreaBordersBl.setScale(0.7f, 1.3f);

	}
	raundResFon.setVisible(false);
	roundResLabel.setVisible(false);
	hudAreaBordersBl.setPosition(SCENE_WIDTH - hudAreaBordersBl.getWidth() / 2, SCENE_HEIGHT / 8 * 5f);
	scoreText.setPosition(hudAreaBordersBl.getX() - (hudAreaBordersBl.getWidth() / 4), hudAreaBordersBl.getY() - (hudAreaBordersBl.getHeight() / 2));

	hudBtnBack.setScale(0.5f);
	hudBtnReplay.setScale(0.5f);

	hudBtnBack.setPosition(SCENE_WIDTH - hudBtnBack.getWidth() * 0.3f, SCENE_HEIGHT - hudBtnBack.getHeight() * 0.25f);
	hudBtnReplay.setPosition(SCENE_WIDTH - hudBtnReplay.getWidth() * 0.3f, hudBtnBack.getY() - hudBtnBack.getHeight() * 0.25f - hudBtnReplay.getHeight() * 0.25f - 3);

    }

    private void restartGameLvl() {
	hideRes();
	falseStartDisplayed = false;
	firstStep = true;
	rstBarFlag = true;
	hudRunRight.setCurrentState(AnimBtn.BTN_STATE_FREE);
	hudRunJump.setCurrentState(AnimBtn.BTN_STATE_FREE);

	this.wPersonalRecord = GameSettings.W_RECORD_RUN_BARIER;

	hudPowerInidcator.setVisible(false);
	falsStart.setVisible(false);

	scoreText.setText("0.000");
	tCounter = 0;
	player.reSet();
	// player.setPosition(130, 200);
	camera.setChaseEntity(player);

	svetofor.reSet();
	svetofor.Start();

    }

    @Override
    public void setHUD() {
	// TODO Auto-generated method stub

    }

}
