package by.kipind.game.olympicgames.sprite;

import android.graphics.Color;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.Arrays;

import by.kipind.game.olympicgames.ResourcesManager;

public class Svetofor extends AnimatedSprite {
    final String LOG_TAG = "myLogs";

    int[] ligtStatus = new int[] { -1, Color.RED, Color.YELLOW, Color.GREEN };

    // ---------------------------------------------
    // VARIABLES
    // ---------------------------------------------

    public Body body;
    private boolean start = false;
    private int status = 0;
    private boolean removeFlag = false;

    public int frameDuration = 700;

    private final int[] animFrame = new int[] { 0, 1, 2, 3, 4 };

    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------

    public Svetofor(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld) {
	super(pX, pY, (ITiledTextureRegion) ResourcesManager.getInstance().gameGraf.get("svetofor_region"), vbo);
	createPhysics(camera, physicsWorld);

	// this.setWidth(this.getWidth()*4);
	// this.setHeight(this.getHeight()*4);

    }

    // ---------------------------------------------
    // CLASS LOGIC
    // ---------------------------------------------

    private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
	body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.StaticBody, PhysicsFactory.createFixtureDef(0, 0, 0));
	body.setActive(false);

	body.setUserData("svetofor");

	physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false) {

	    @Override
	    public void onUpdate(float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed);
		camera.onUpdate(0.1f);
		if (getStatus() == Color.GREEN && removeFlag) {
		    // body.setTransform(-1, -1, 0);
		    setVisible(false);
		}
		if (getCurrentTileIndex() != status) {
		    status = getCurrentTileIndex();
		    if (status != 4) {
			// :TODO
			ResourcesManager.getInstance().playSoundFromStack("svetofor_nastart");
		    } else {
			// :TODO
			ResourcesManager.getInstance().playSoundFromStack("svetofor_start");
		    }
		}

	    }
	});
	stopAnimation(0);
    }

    public void Start() {
	if (!start) {
	    ResourcesManager.getInstance().playSoundFromStack("stadion_priv");
	    this.setVisible(true);
	    this.start = true;
	    long[] spriteFameDuration = new long[animFrame.length];
	    Arrays.fill(spriteFameDuration, frameDuration);
	    animate(spriteFameDuration, animFrame, false);
	}
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

    @Override
    public void setVisible(boolean flag) {
	super.setVisible(flag);
	// body.setActive(false);

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
