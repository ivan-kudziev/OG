package by.kipind.game.olympicgames.sprite.buttons;

import android.view.MotionEvent;

import org.andengine.entity.scene.ITouchArea;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class BtnGoRight extends AnimBtn {

    private long btnDownTime;
    private float ress;
    private int startDeg = 45;
    private float maxStartDeg;
    private float timeIntervalMinMax;

    public BtnGoRight(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager vbo) {
	super(pX, pY, pTiledTextureRegion, vbo);
	maxStartDeg = 80;
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
		startDeg = Math.round(maxStartDeg);
	    } else {

		ress = maxStartDeg * btnDownTime / timeIntervalMinMax;
		startDeg = Math.round(maxStartDeg * btnDownTime / timeIntervalMinMax);
	    }
	    actionThrow();

	}

	return false;
    }

    public void actionThrow() {

    };

    public int getStartDeg() {
	return startDeg;
    }

    public void setStartDeg(int startDeg) {
	this.startDeg = startDeg;
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, ITouchArea pTouchArea, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	// TODO Auto-generated method stub
	return false;
    }

}