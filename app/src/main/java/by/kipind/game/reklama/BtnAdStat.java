package by.kipind.game.reklama;

import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/*import com.appodeal.ads.Appodeal;
 import com.appodeal.ads.RewardedVideoCallbacks;
 */

public class BtnAdStat extends AnimatedSprite implements IOnAreaTouchListener {

    static final int BTN_STATE_SHOW = 0;
    static final int BTN_STATE_LOADING = 1;
    static final int BTN_STATE_FAIL = 2;

    private AdModule am = AdModule.getInstance();
    public final int gameId;


    public BtnAdStat(int gid, float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager vbo) {
	super(pX, pY, pTiledTextureRegion, vbo);
	this.setCurrentTileIndex(BTN_STATE_SHOW);
	reSize(0.7f, 0.7f);
	// setCurrentState(BTN_STATE_LOADING);
	gameId = gid;
	am.addListener(gameId, this);

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
	switch (this.getCurrentTileIndex()) {
	case BTN_STATE_SHOW:
	    am.showRevVideo(gameId);
	    break;
	case BTN_STATE_LOADING:
	    break;
	case BTN_STATE_FAIL:
	    break;
	default:
	    break;
	}

	return true;

    }


    public void reMove() {
	this.setPosition(-this.getWidth(), -this.getHeight());
	// this.setIgnoreUpdate(true);
	// detachSelf();

    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, ITouchArea pTouchArea, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	return true;
    }

}
