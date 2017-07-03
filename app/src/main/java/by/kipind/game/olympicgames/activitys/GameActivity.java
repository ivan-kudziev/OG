package by.kipind.game.olympicgames.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.appodeal.ads.Appodeal;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;

import java.io.IOException;

import by.kipind.game.olympicgames.GameSettings;
import by.kipind.game.olympicgames.ResourcesManager;
import by.kipind.game.olympicgames.SceneManager;
import by.kipind.game.reklama.AdModule;

public class GameActivity extends BaseGameActivity {
    protected static Integer SCENE_WIDTH = 800;
    protected static Integer SCENE_HEIGHT = 450;
    // private static TimeZone timeZone = TimeZone.getTimeZone("UTC");
    AdView adView;

    private BoundCamera camera;

    // private ResourcesManager resourcesManager;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    protected void onSetContentView() {

		String appKey = "647a0f43cb668742d7034c39a9e8b69527c3e6741c2ab189";
		Appodeal.disableLocationPermissionCheck();
		//Appodeal.setTesting(true);
		Appodeal.initialize(this, appKey, Appodeal.INTERSTITIAL);

	// ads set
	if (adView != null) {
	    return;
	}



	final FrameLayout frameLayout = new FrameLayout(this);
	final FrameLayout.LayoutParams frameLayoutLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

	final FrameLayout.LayoutParams adViewLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT,
		Gravity.RIGHT | Gravity.TOP);

	adView = new AdView(this);
	adView.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
	adView.setAdUnitId("ca-app-pub-3924626110211690/7913659768");
	// adView.setAdListener(null);
	adView.setBackgroundColor(android.graphics.Color.TRANSPARENT);
	AdRequest adrequest = new AdRequest.Builder().build();
	adView.loadAd(adrequest);// only if u test the ads
	// adView.loadAd(new AdRequest.Builder().build()); When u dont test the ads on ur device
	this.mRenderSurfaceView = new RenderSurfaceView(this);
	mRenderSurfaceView.setRenderer(mEngine, this);

	final android.widget.FrameLayout.LayoutParams surfaceViewLayoutParams = new android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
		android.view.ViewGroup.LayoutParams.MATCH_PARENT);
	surfaceViewLayoutParams.gravity = Gravity.CENTER;

	frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
	frameLayout.addView(adView, adViewLayoutParams);

	this.setContentView(frameLayout, frameLayoutLayoutParams);
	mEngine.registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback() {
	    public void onTimePassed(final TimerHandler pTimerHandler) {
		ads();
		pTimerHandler.reset();
	    }
	}));
    }

    private void ads() {
	this.runOnUiThread(new Runnable() {
	    @Override
	    public void run() {
		if (GameSettings.TAG_AD_BUNNER_SHOW) {
		    adView.setVisibility(AdView.VISIBLE);
		} else {
		    adView.setVisibility(AdView.INVISIBLE);
		}
	    }
	});
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
	camera = new BoundCamera(0, 0, SCENE_WIDTH, SCENE_HEIGHT);
	EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(SCENE_WIDTH, SCENE_HEIGHT), this.camera);
	engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
	engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
	return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
	ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
	// resourcesManager = ResourcesManager.getInstance();
	pOnCreateResourcesCallback.onCreateResourcesFinished();

    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
	SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
	mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {
	    public void onTimePassed(final TimerHandler pTimerHandler) {
		mEngine.unregisterUpdateHandler(pTimerHandler);
		SceneManager.getInstance().createMenuScene();

	    }
	}));
	pOnPopulateSceneCallback.onPopulateSceneFinished();

    }

    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) {
	return new LimitedFPSEngine(pEngineOptions, 35);
    }

    @Override
    protected void onDestroy() {
	//
	ResourcesManager.getInstance().musicPause(1);
	android.os.Process.killProcess(android.os.Process.myPid());
    super.onDestroy();
	// System.exit(0);
    }

    @Override
    protected void onPause() {
	super.onPause();
	ResourcesManager.getInstance().musicPause(1);
	ResourcesManager.getInstance().musicPause(2);
    }

    @Override
    protected void onResume() {
	super.onResume();
	ResourcesManager.getInstance().musicResume(1);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    SceneManager.getInstance().getCurrentScene().onBackKeyPressed();

	}
	return false;
    }

    public void onLeave() {
	this.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);

	if (requestCode == 1) {

	}
	if (requestCode == 2) {
	    switch (data.getExtras().getInt("adShowRes")) {
	    case -777:
		AdModule.getInstance().AdShown();

		break;

	    default:
		AdModule.getInstance().AdFail();
		break;
	    }

	}
    }

}
