package by.kipind.game.olympicgames.sprite;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import by.kipind.game.olympicgames.ResourcesManager;

public class Pike extends AnimatedSprite {
    final String LOG_TAG = "myLogs";

    private int status = 0; // 0- ; -1 -

    // ---------------------------------------------
    // VARIABLES
    // ---------------------------------------------

    public Body pBody;
    public int frameDuration = 100;

    private final int[] animFrame = new int[] { 0, 1, 2 };

    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------

    public Pike(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld) {
	super(pX, pY, (ITiledTextureRegion) ResourcesManager.getInstance().gameGraf.get("pike_region"), vbo);
	createPhysics(camera, physicsWorld);
	//camera.setChaseEntity(this);

    }

    // ---------------------------------------------
    // CLASS LOGIC
    // ---------------------------------------------

    private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
	pBody = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

	pBody.setUserData("pike");
	pBody.getFixtureList().get(0).setFriction(0.25f);
	pBody.setFixedRotation(true);

	physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, pBody, true, false) {
	    @Override
	    public void onUpdate(float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed);
		camera.onUpdate(0.1f);
		if (getPkStatus() != 0) {
		    if (pBody.getLinearVelocity().y > 6) {
			Pike.this.setCurrentTileIndex(0);

		    } else if (pBody.getLinearVelocity().y < -6) {
			Pike.this.setCurrentTileIndex(2);

		    }else if (pBody.getLinearVelocity().x > 0) {
			Pike.this.setCurrentTileIndex(1);

		    } 

		}
	    }
	});
    }

    public boolean toThrow(int i, float throwSpeed) {
	int deg = i;
	pBody.setLinearVelocity(0f, 0f);
	pBody.applyLinearImpulse((float) (throwSpeed * Math.cos(Math.toRadians(deg))), (float) (throwSpeed * Math.sin(Math.toRadians(deg))), pBody.getPosition().x, pBody.getPosition().y);
	setPkStatus(1);
	return true;
    }

    public void onFall() {
	// :TODO
	ResourcesManager.getInstance().playSoundFromStack("pike_foll");

	pBody.setLinearVelocity(0f, 0f);
	setPkStatus(-1);
	
    }

    public int getPkStatus() {
	return status;
    }

    public void setPkStatus(int pkStatus) {
	this.status = pkStatus;
    }

    public void reSet() {
	this.status = 0;
	this.frameDuration = 100;
	this.pBody.setActive(false);

    }

}
