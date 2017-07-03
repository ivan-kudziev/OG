package by.kipind.game.olympicgames.sprite.buttons;

import org.andengine.entity.scene.ITouchArea;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.view.MotionEvent;

public class BtnLongJump extends AnimBtn {
    private long btnDownTime;
    private float ress;
    private int jumpDeg=45;
    private float maxJumpDeg;
    private float timeIntervalMinMax;

    public BtnLongJump(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager vbo) {
	super(pX, pY, pTiledTextureRegion, vbo);
	maxJumpDeg = 80;
	timeIntervalMinMax = 300;
    }

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
	super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);

	if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {

	    btnDownTime = System.currentTimeMillis();

	}
	if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
	    btnDownTime = System.currentTimeMillis() - btnDownTime;
	    if (btnDownTime > timeIntervalMinMax) {
		jumpDeg =Math.round( maxJumpDeg);
	    } else {

		ress = maxJumpDeg * btnDownTime / timeIntervalMinMax;
		jumpDeg = Math.round(maxJumpDeg * btnDownTime / timeIntervalMinMax);
	    }
	    actionJump();

	}

	return false;
    }

    public void actionJump() {

    };

    public int getJumpDeg() {
	return jumpDeg;
    }

    public void setJumpDeg(int jumpDeg) {
	this.jumpDeg = jumpDeg;
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, ITouchArea pTouchArea, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	// TODO Auto-generated method stub
	return false;
    }

}
