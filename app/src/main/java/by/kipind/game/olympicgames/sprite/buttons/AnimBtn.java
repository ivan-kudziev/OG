package by.kipind.game.olympicgames.sprite.buttons;

import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.view.MotionEvent;

public abstract class AnimBtn extends AnimatedSprite implements IOnAreaTouchListener {
   public static int BTN_STATE_FREE = 0;
   public static int BTN_STATE_PRESSED = 1;
   public static int BTN_STATE_UNACTIVE = 2;

    public AnimBtn(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager vbo) {
	super(pX, pY, pTiledTextureRegion, vbo);
	// TODO Auto-generated constructor stub

    }

    public void reSize(float kX, float kY) {
	this.setWidth(this.getWidth() * kX);
	this.setHeight(this.getHeight() * kY);
    }

    public int setCurrentState(int stateIndex) {
	int res = -1;
	try {
	    this.setCurrentTileIndex(stateIndex);
	    res = stateIndex;
	} catch (ArrayIndexOutOfBoundsException e) {
	    res = -1;
	}
	return res;

    }

    
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
	if (this.getCurrentTileIndex() != AnimBtn.BTN_STATE_UNACTIVE) {
	    switch (pSceneTouchEvent.getAction()) {
	    case MotionEvent.ACTION_DOWN:
		this.setCurrentTileIndex(AnimBtn.BTN_STATE_PRESSED);
		break;
	    case MotionEvent.ACTION_OUTSIDE:
		this.setCurrentTileIndex(AnimBtn.BTN_STATE_FREE);
		break;
	    case MotionEvent.ACTION_UP:
		this.setCurrentTileIndex(AnimBtn.BTN_STATE_FREE);
		break;
	    case MotionEvent.ACTION_CANCEL:
		this.setCurrentTileIndex(AnimBtn.BTN_STATE_FREE);
		break;
	    default:
		break;
	    }
	}
	return false;

    }

}
