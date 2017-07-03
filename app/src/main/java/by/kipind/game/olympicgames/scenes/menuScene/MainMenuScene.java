package by.kipind.game.olympicgames.scenes.menuScene;

import android.view.MotionEvent;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.util.GLState;

import java.util.ArrayList;
import java.util.List;

import by.kipind.game.olympicgames.GameSettings;
import by.kipind.game.olympicgames.ResourcesManager;
import by.kipind.game.olympicgames.SceneManager;
import by.kipind.game.olympicgames.SceneManager.SceneType;
import by.kipind.game.olympicgames.sceneElements.PersonalRecordTable;
import by.kipind.game.olympicgames.sceneElements.RatePanel;
import by.kipind.game.olympicgames.scenes.BaseScene;
import by.kipind.game.olympicgames.sprite.buttons.menubtn.AnimMenuItem;
import by.kipind.game.reklama.BtnAdStat;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener, IOnSceneTouchListener {

    private MenuScene menuChildScene;
    private HUD gameHUD;

    private Sprite hudAreaBlackAlpha;
    private Sprite rateMailArea;
    private Sprite rateGPArea;
    private PersonalRecordTable rt;
    private RatePanel mRatePanel;

    private int testTimer = 0, v = 1;

    private List<BtnAdStat> adBtnList;

    @Override
    public void createScene() {
        if (!GameSettings.GAME_PLAYER_NICK_ID.equals("-1")) {
            GameSettings.onSync();
        } else {
            GameSettings.GAME_PLAYER_NICK = "";
        }
        createBackground();
        setHUD();
        createMenuChildScene();

    }

    @Override
    public void onBackKeyPressed() {
        System.exit(0);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_MENU;
    }

    @Override
    public void disposeScene() {
        camera.setHUD(null);
        GameSettings.TAG_AD_BUNNER_SHOW = false;
    }

    @Override
    public void setHUD() {

        if (gameHUD == null) {
            gameHUD = new HUD();

            hudAreaBlackAlpha = new Sprite(0, 0, resourcesManager.info_bg, vbom) {

                @Override
                public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                    super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                    if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        return SceneObjectTouch(this);
                    }
                    return false;
                }
            };

            hudAreaBlackAlpha.setScale(0.85f, 1.2f);
            hudAreaBlackAlpha.setPosition((SCENE_WIDTH + hudAreaBlackAlpha.getWidth() * 0.85f) / 2, (SCENE_HEIGHT) / 2f + hudAreaBlackAlpha.getHeight() * 0.3f);

            adBtnList = new ArrayList<BtnAdStat>();
            int[] adAr = new int[7];
            adAr = GameSettings.getBlockByAd();
            for (int i = 0; i < adAr.length; i++) {
                if (adAr[i] == 1) {
                    adBtnList.add(new BtnAdStat(i, 330, 330, (ITiledTextureRegion) ResourcesManager.getInstance().ad_status, vbom));
                }
            }

            // --------------

            rt = new PersonalRecordTable(GameSettings.GAME_PLAYER_NICK, GameSettings.GAME_PLAYER_NICK_ID, GameSettings.WEEK_OF_YEAR, hudAreaBlackAlpha.getX(),
                    hudAreaBlackAlpha.getY(),
                    hudAreaBlackAlpha.getWidth() * 0.85f,
                    hudAreaBlackAlpha.getHeight(), camera, vbom);
            // TODO:REM
        /* rt.registerUpdateHandler(new TimerHandler(1f, true, new ITimerCallback() {
	     * 
	     * @Override public void onTimePassed(final TimerHandler pTimerHandler) { testTimer++; if (testTimer > 10 && v == 1) { SceneManager.getInstance().loadGameScene(engine, 1); v = -1; } } })); */

            // if (adBtnList != null) {
            for (BtnAdStat iter : adBtnList) {
                gameHUD.registerTouchArea(iter);
                gameHUD.attachChild(iter);

            }
            // }

            gameHUD.registerTouchArea(hudAreaBlackAlpha);

            gameHUD.attachChild(hudAreaBlackAlpha);
            gameHUD.attachChild(rt);

            mRatePanel = new RatePanel(0, 0, vbom);
            mRatePanel.setPosition(hudAreaBlackAlpha.getX(), hudAreaBlackAlpha.getY() - hudAreaBlackAlpha.getHeight() / 1.5f - mRatePanel.getHeight());
            rateMailArea = new Sprite(0, 0, resourcesManager.rate_black_star, vbom) {
                @Override
                public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                    super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                    if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        return SceneObjectTouch(this);
                    }
                    return false;
                }
            };
            rateGPArea = new Sprite(0, 0, resourcesManager.rate_gold_star, vbom) {
                @Override
                public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                    super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                    if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        return SceneObjectTouch(this);
                    }
                    return false;
                }
            };

            rateMailArea.setScale(3, 1);
            rateGPArea.setScale(3, 1);
            rateMailArea.setPosition(mRatePanel.getX() - mRatePanel.getWidth() / 4, mRatePanel.getY());
            rateGPArea.setPosition(mRatePanel.getX() + mRatePanel.getWidth() / 4, mRatePanel.getY());

            gameHUD.registerTouchArea(rateMailArea);
            gameHUD.registerTouchArea(rateGPArea);
            gameHUD.attachChild(rateGPArea);
            gameHUD.attachChild(rateMailArea);
            if (GameSettings.GOOGLE_PLAY_VISIT > 0) {// GameSettings.GOOGLE_PLAY_VISIT > 0 show rate link
                mRatePanel.changeToInviteMail();
            }
            gameHUD.attachChild(mRatePanel);

        }

        camera.setHUD(gameHUD);

        if (ResourcesManager.getInstance().musicIsPlaying(1)) {
            ResourcesManager.getInstance().musicResume(1);

        } else {
            ResourcesManager.getInstance().musicPlay(1);
        }
    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        // :TODO change buton click sound
        ResourcesManager.getInstance().playSoundFromStack("arch_vistrel");

        if (pMenuItem.getID() != 7 && (GameSettings.GAME_PLAYER_NICK_ID == "-2" || GameSettings.GAME_PLAYER_NICK == "" || GameSettings.GAME_PLAYER_NICK_ID == "-1")) {
            SceneManager.getInstance().loadGameScene(engine, 0);// menuItemPlInf
        } else {
            if (pMenuItem.getID() >= 1 && pMenuItem.getID() <= 6) {
                ResourcesManager.getInstance().musicPause(1);
            }
            SceneManager.getInstance().loadGameScene(engine, pMenuItem.getID());
        }

        return true;
    }

    private void createBackground() {

        attachChild(new Sprite(SCENE_WIDTH / 2, SCENE_HEIGHT / 2, resourcesManager.menu_background_region, vbom) {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera) {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
        });
    }

    private void createMenuChildScene() {
        menuChildScene = new MenuScene(camera);
        menuChildScene.setPosition(SCENE_WIDTH / 2, SCENE_HEIGHT / 2);

        final IMenuItem menuItemPlInf = new AnimMenuItem(GameSettings.ACTIVITY_ID_SETTINGS_INFO, resourcesManager.play_sett_inf, vbom);
        final IMenuItem menuItemRun100 = new AnimMenuItem(GameSettings.ACTIVITY_ID_RUN100, resourcesManager.play_run100, vbom);
        final IMenuItem menuItemRunBar = new AnimMenuItem(GameSettings.ACTIVITY_ID_RUN_BARIER, resourcesManager.play_run_barier, vbom);
        final IMenuItem menuItemLongJump = new AnimMenuItem(GameSettings.ACTIVITY_ID_LONG_JUMP, resourcesManager.play_long_jump, vbom);
        final IMenuItem menuItemPikeThrow = new AnimMenuItem(GameSettings.ACTIVITY_ID_PIKE_THROW, resourcesManager.play_pike_throw, vbom);
        final IMenuItem menuItemArchery = new AnimMenuItem(GameSettings.ACTIVITY_ID_ARCHERY, resourcesManager.play_archery, vbom);
        final IMenuItem menuItemShooting = new AnimMenuItem(GameSettings.ACTIVITY_ID_SHOOTING, resourcesManager.play_shooting, vbom);
        final IMenuItem menuItemExit = new AnimMenuItem(GameSettings.ACTIVITY_ID_EXIT, resourcesManager.play_exit, vbom);
        final IMenuItem menuItemLeaders = new AnimMenuItem(GameSettings.ACTIVITY_ID_LB, resourcesManager.play_lb, vbom);
        final IMenuItem menuItemRafting = new AnimMenuItem(GameSettings.ACTIVITY_ID_RAFTING, resourcesManager.play_rafting, vbom);
        // ScaleMenuItemDecorator(new AnimatedSpritem(GameSettings.ACTIVITY_ID_LB, resourcesManager.play_lb, vbom), 1f, 0.7f);

        menuChildScene.addMenuItem(menuItemPlInf); // 0
        menuChildScene.addMenuItem(menuItemRun100); // 1
        menuChildScene.addMenuItem(menuItemRunBar);// 2
        menuChildScene.addMenuItem(menuItemLongJump);// 3
        menuChildScene.addMenuItem(menuItemPikeThrow);// 4
        menuChildScene.addMenuItem(menuItemArchery);// 5
        menuChildScene.addMenuItem(menuItemShooting);// 6
        menuChildScene.addMenuItem(menuItemExit);// 7
        menuChildScene.addMenuItem(menuItemLeaders);// 8
        menuChildScene.addMenuItem(menuItemRafting);// 9

        for (IMenuItem itm : menuChildScene.getMenuItems()) {
            itm.setScale(0.8f);
        }
        // menuChildScene.buildAnimations();
        menuChildScene.setBackgroundEnabled(false);

        final float mElWgh = menuItemRun100.getWidth() * 0.85f;
        final float mElHgh = menuItemRun100.getHeight() * 0.5f;
        final float mElCrnt = hudAreaBlackAlpha.getY() - SCENE_HEIGHT / 2;

        menuItemExit.setPosition(0 - mElWgh, -SCENE_HEIGHT / 2 + mElHgh);
        menuItemLeaders.setPosition(0 - 2 * mElWgh, -SCENE_HEIGHT / 2 + mElHgh);
        menuItemPlInf.setPosition(0 - 3 * mElWgh, -SCENE_HEIGHT / 2 + mElHgh);

        menuItemRun100.setPosition(0 - (3 * mElWgh), mElCrnt + mElHgh);
        menuItemRunBar.setPosition(0 - (2 * mElWgh), mElCrnt + mElHgh);
        menuItemLongJump.setPosition(0 - (mElWgh), mElCrnt + mElHgh);
        menuItemPikeThrow.setPosition(0 - (3 * mElWgh), mElCrnt - mElHgh);
        menuItemArchery.setPosition(0 - (2 * mElWgh), mElCrnt - mElHgh);
        menuItemShooting.setPosition(0 - (mElWgh), mElCrnt - mElHgh);
        menuItemRafting.setPosition(0 -4 * mElWgh, -SCENE_HEIGHT / 2 + mElHgh);

        upateAdBtnPos();

        menuChildScene.setOnMenuItemClickListener(this);

        setChildScene(menuChildScene);
    }

    private boolean SceneObjectTouch(Object touchedObj) {
        boolean res = false;
        if (touchedObj.equals(hudAreaBlackAlpha)) {
            rt.reload();
            res = true;
        } else if (touchedObj.equals(rateGPArea)) {
            if (mRatePanel.getState() == 1) {

                mRatePanel.gotoGP();
                mRatePanel.changeToInviteMail();
            } else {
                mRatePanel.gotoInviteMail();
            }
            res = true;
        } else if (touchedObj.equals(rateMailArea)) {
            if (mRatePanel.getState() == 1) {

                mRatePanel.gotoMail();
                mRatePanel.changeToInviteMail();
            } else {
                mRatePanel.gotoInviteMail();
            }
            res = true;
        }
        return res;
    }

    private void upateAdBtnPos() {
        for (BtnAdStat iter : adBtnList) {
            iter.setPosition(SCENE_WIDTH / 2 + menuChildScene.getMenuItem(iter.gameId).getX(), SCENE_HEIGHT / 2 + menuChildScene.getMenuItem(iter.gameId).getY() - iter.getHeight()
                    * 0.2f);

        }
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        return false;
    }

}
