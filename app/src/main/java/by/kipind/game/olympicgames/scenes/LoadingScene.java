package by.kipind.game.olympicgames.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import by.kipind.game.olympicgames.SceneManager.SceneType;

public class LoadingScene extends BaseScene {
   
    
    @Override
    public void createScene() {
	setBackground(new Background(Color.WHITE));
	attachChild(new Text(SCENE_WIDTH / 2, SCENE_HEIGHT / 2, resourcesManager.font_pix_kir, "...", vbom));
    }

    @Override
    public void onBackKeyPressed() {
	return;
    }

    @Override
    public SceneType getSceneType() {
	return SceneType.SCENE_LOADING;
    }

    @Override
    public void disposeScene() {
	// TODO Auto-generated method stub

    }

    @Override
    public void setHUD() {
	// TODO Auto-generated method stub

    }

}
