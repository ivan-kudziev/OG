package by.kipind.game.olympicgames.sprite.buttons;

import org.andengine.entity.scene.ITouchArea;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class BtnRun extends AnimBtn {

    public BtnRun(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager vbo) {
	super(pX, pY, pTiledTextureRegion, vbo);
	// TODO Auto-generated constructor stub
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, ITouchArea pTouchArea, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	return false;
    }

}
