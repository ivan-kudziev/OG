package by.kipind.game.olympicgames.sprite;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.graphics.Color;
import by.kipind.game.olympicgames.ResourcesManager;

import com.badlogic.gdx.physics.box2d.Body;

public class AdVideoState extends AnimatedSprite {
    final String LOG_TAG = "myLogs";

    int[] ligtStatus = new int[] { -1, Color.RED, Color.YELLOW, Color.GREEN };

    // ---------------------------------------------
    // VARIABLES
    // ---------------------------------------------

    public Body body;
    private boolean start = false;
    private int status = 0;
    private boolean removeFlag = false;

    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------

    public AdVideoState(float pX, float pY, VertexBufferObjectManager vbo, Camera camera) {
	super(pX, pY, (ITiledTextureRegion) ResourcesManager.getInstance().ad_status, vbo);

	this.setWidth(this.getWidth() * 0.7f);
	this.setHeight(this.getHeight() * 0.7f);

    }

    // ---------------------------------------------
    // CLASS LOGIC
    // ---------------------------------------------


    public void Start() {

    }

    public int getStatus() {
	int res = -1;
	switch (getCurrentTileIndex()) {
	case 0:
	    res = -1;
	    break;
	case 1:
	    res = Color.RED;
	    break;
	case 2:
	    res = Color.RED;
	    break;
	case 3:
	    res = Color.YELLOW;
	    break;
	case 4:
	    res = Color.GREEN;

	    break;

	default:
	    break;
	}
	return res;
    }


    public void reSet() {
	this.start = false;
	this.status = 0;
	this.setVisible(true);
	// this.frameDuration = 700;
	stopAnimation(0);
    }

    public void setRemoveFlag(boolean removeFlag) {
	this.removeFlag = removeFlag;
    }

}
