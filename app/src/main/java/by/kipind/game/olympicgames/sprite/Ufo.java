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

public class Ufo extends AnimatedSprite {
    final String LOG_TAG = "myLogs";

    private int status = 0; // 0- ; -1 -

    // ---------------------------------------------
    // VARIABLES
    // ---------------------------------------------

    public Body ufoBody;
    public Body ufoShBody;
    public int frameDuration = 100;

    private float scaleMax, scaleMin;

    private final int[] animFrame = new int[] { 0, 1, 2 };

    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------

    public Ufo(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld) {
	super(pX, pY, (ITiledTextureRegion) ResourcesManager.getInstance().gameGraf.get("shoot_ufo"), vbo);
	createPhysics(camera, physicsWorld);
	// camera.setChaseEntity(this);

    }

    // ---------------------------------------------
    // CLASS LOGIC
    // ---------------------------------------------

    private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
	ufoBody = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0.1f));

	ufoBody.setUserData("ufo");
	ufoBody.getFixtureList().get(0).setFriction(0.1f);
	ufoBody.setFixedRotation(true);

	physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, ufoBody, true, false) {
	    @Override
	    public void onUpdate(float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed);
		camera.onUpdate(0.1f);
		if (getPkStatus() == 1 && getCurrentTileIndex() == 4) {
		    setVisible(false);
		    setCurrentTileIndex(0);
		    setPkStatus(-2);
		}
	    }
	});
    }

    public boolean toThrow(int i, float throwSpeed) {
	int deg = i;
	ufoBody.setLinearVelocity(0f, 0f);
	ufoBody.applyLinearImpulse((float) (throwSpeed * Math.cos(Math.toRadians(deg))), (float) (throwSpeed * Math.sin(Math.toRadians(deg))), ufoBody.getPosition().x,
		ufoBody.getPosition().y);
	setPkStatus(1);
	return true;
    }

    public void onFall() {
	ufoBody.setLinearVelocity(0f, 0f);
	this.setVisible(false);
	setPkStatus(-1);

    }

    public void onShoot() {
	ufoBody.setLinearVelocity(0f, 0f);

	animate(this.frameDuration, false);



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
	this.ufoBody.setActive(false);

    }

}
