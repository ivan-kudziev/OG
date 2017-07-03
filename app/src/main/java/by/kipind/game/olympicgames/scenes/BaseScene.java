package by.kipind.game.olympicgames.scenes;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;
import android.content.Context;
import by.kipind.game.olympicgames.GameSettings;
import by.kipind.game.olympicgames.ResourcesManager;
import by.kipind.game.olympicgames.SceneManager.SceneType;

public abstract class BaseScene extends Scene {
    protected static Integer SCENE_WIDTH = 800;
    protected static Integer SCENE_HEIGHT = 450;

   
    // ---------------------------------------------
    // VARIABLES
    // ---------------------------------------------

    protected Engine engine;
    protected Activity activity;
    protected ResourcesManager resourcesManager;
    protected VertexBufferObjectManager vbom;
    protected BoundCamera camera;
    protected Context context;

    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------

    public BaseScene() {
	this.resourcesManager = ResourcesManager.getInstance();
	this.engine = resourcesManager.engine;
	this.activity = resourcesManager.activity;
	this.vbom = resourcesManager.vbom;
	this.camera = resourcesManager.camera;
	context = GameSettings.getAppContext();
	
	createScene();
    }

    // ---------------------------------------------
    // ABSTRACTION
    // ---------------------------------------------

    public abstract void createScene();

    public abstract void setHUD();

    public abstract void onBackKeyPressed();

    public abstract SceneType getSceneType();

    public abstract void disposeScene();

}
