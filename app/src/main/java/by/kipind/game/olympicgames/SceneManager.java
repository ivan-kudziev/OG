package by.kipind.game.olympicgames;

import android.content.Intent;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;
import org.andengine.ui.activity.BaseGameActivity;

import by.kipind.game.leaderboard.LeaderBoardActivity;
import by.kipind.game.olympicgames.activitys.PlayerInfoActivity;
import by.kipind.game.olympicgames.scenes.BaseScene;
import by.kipind.game.olympicgames.scenes.LoadingScene;
import by.kipind.game.olympicgames.scenes.SplashScene;
import by.kipind.game.olympicgames.scenes.gameScene.ArcheryGS;
import by.kipind.game.olympicgames.scenes.gameScene.LongJumpGS;
import by.kipind.game.olympicgames.scenes.gameScene.PikeThrowGS;
import by.kipind.game.olympicgames.scenes.gameScene.RaftingGS;
import by.kipind.game.olympicgames.scenes.gameScene.Run100GS;
import by.kipind.game.olympicgames.scenes.gameScene.RunBarGS;
import by.kipind.game.olympicgames.scenes.gameScene.ShootingGS;
import by.kipind.game.olympicgames.scenes.menuScene.MainMenuScene;

//import com.appodeal.ads.Appodeal;

public class SceneManager {
    // ---------------------------------------------
    // SCENES
    // ---------------------------------------------

    private BaseScene splashScene;
    private BaseScene menuScene;
    private BaseScene loadingScene;
    private BaseScene gameScene;

    // ---------------------------------------------
    // VARIABLES
    // ---------------------------------------------

    private static final SceneManager INSTANCE = new SceneManager();

    private SceneType currentSceneType = SceneType.SCENE_SPLASH;

    private BaseScene currentScene;

    private Engine engine = ResourcesManager.getInstance().engine;

    public enum SceneType {
        SCENE_SPLASH, SCENE_MENU, SCENE_GAME, SCENE_LOADING,
    }

    // ---------------------------------------------
    // CLASS LOGIC
    // ---------------------------------------------

    public void setScene(BaseScene scene) {
        engine.setScene(scene);
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }

    public void setScene(SceneType sceneType) {
        switch (sceneType) {
            case SCENE_MENU:
                setScene(menuScene);
                break;
            case SCENE_GAME:
                setScene(gameScene);
                break;
            case SCENE_SPLASH:
                setScene(splashScene);
                break;
            case SCENE_LOADING:
                setScene(loadingScene);
                break;
            default:
                break;
        }
    }

    // ---------------------------------------------
    // GETTERS AND SETTERS
    // ---------------------------------------------

    public static SceneManager getInstance() {
        return INSTANCE;
    }

    public SceneType getCurrentSceneType() {
        return currentSceneType;
    }

    public BaseScene getCurrentScene() {
        return currentScene;
    }

    public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
        ResourcesManager.getInstance().loadSplashScreen();
        splashScene = new SplashScene();
        currentScene = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }

    private void disposeSplashScene() {
        ResourcesManager.getInstance().unloadSplashScreen();
        splashScene.disposeScene();
        splashScene = null;
    }

    public void createMenuScene() {
        ResourcesManager.getInstance().loadMenuResources();
        menuScene = new MainMenuScene();
        loadingScene = new LoadingScene();
        SceneManager.getInstance().setScene(menuScene);

        disposeSplashScene();
    }

    public void loadGameScene(final BaseGameActivity activity, final int gameNum) {

    }

    public void loadGameScene(final Engine mEngine, final int gameNum) {

        if (gameNum == GameSettings.ACTIVITY_ID_EXIT) {
            ResourcesManager.getInstance().unloadMenuTextures();
            ResourcesManager.getInstance().activity.onLeave();

        } else if (gameNum == GameSettings.ACTIVITY_ID_SETTINGS_INFO) {
            Intent intent = new Intent(ResourcesManager.getInstance().activity, PlayerInfoActivity.class);
            intent.putExtra("callerId", 1);
            ResourcesManager.getInstance().activity.startActivity(intent);

        } else if (gameNum == GameSettings.ACTIVITY_ID_LB) {
            Intent intent = new Intent(ResourcesManager.getInstance().activity, LeaderBoardActivity.class);
            intent.putExtra("callerId", 1);
            ResourcesManager.getInstance().activity.startActivity(intent);

        } else {
            setScene(loadingScene);
            menuScene.disposeScene();
            ResourcesManager.getInstance().unloadMenuTextures();
            mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
                public void onTimePassed(final TimerHandler pTimerHandler) {
                    mEngine.unregisterUpdateHandler(pTimerHandler);

                    ResourcesManager rm = ResourcesManager.getInstance();
                    rm.loadGameResources(gameNum);

                    switch (gameNum) {
                        case GameSettings.ACTIVITY_ID_SETTINGS_INFO:
                            Intent intent = new Intent(ResourcesManager.getInstance().activity, PlayerInfoActivity.class);
                            intent.putExtra("callerId", 1);
                            ResourcesManager.getInstance().activity.startActivity(intent);
                            break;
                        case GameSettings.ACTIVITY_ID_RUN100:
                            gameScene = new Run100GS();
                            break;
                        case GameSettings.ACTIVITY_ID_RUN_BARIER:
                            gameScene = new RunBarGS();
                            break;
                        case GameSettings.ACTIVITY_ID_LONG_JUMP:
                            gameScene = new LongJumpGS();
                            break;
                        case GameSettings.ACTIVITY_ID_PIKE_THROW:
                            gameScene = new PikeThrowGS();
                            break;
                        case GameSettings.ACTIVITY_ID_ARCHERY:
                            gameScene = new ArcheryGS();
                            break;
                        case GameSettings.ACTIVITY_ID_SHOOTING:
                            gameScene = new ShootingGS();
                            break;
                        case GameSettings.ACTIVITY_ID_RAFTING:
                            gameScene = new RaftingGS();
                            break;

                        default:
                            break;
                    }

                    if (gameScene != null) {
                        setScene(gameScene);
                    } else {
                    }
                }
            }));
        }

    }

    public void loadMenuScene(final Engine mEngine) {

        setScene(loadingScene);
        gameScene.disposeScene();
        ResourcesManager.getInstance().unloadGameTextures();
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadMenuTextures();
                menuScene.setHUD();
                setScene(menuScene);
            }
        }));
        GameSettings.TAG_AD_BUNNER_SHOW = true;
    }

}