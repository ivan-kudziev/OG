package by.kipind.game.olympicgames.sprite;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import by.kipind.game.olympicgames.ResourcesManager;

/**
 * Created by kip on 03.09.2017.
 */

public class Sensor extends AnimatedSprite {
	final String LOG_TAG = "myLogs";
	private final int[] animFrame = new int[]{0, 1, 2};

	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------
	public Body body;
	private int status = 0; // 0- ; -1 -

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public Sensor(float pX, float pY, VertexBufferObjectManager vbo,  PhysicsWorld physicsWorld, String userData,String graficName) {
		super(pX, pY, (ITiledTextureRegion) ResourcesManager.getInstance().gameGraf.get(graficName), vbo);
		createPhysics( physicsWorld, userData);

	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	private void createPhysics( PhysicsWorld physicsWorld, String identifier) {
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(0, 0, 0,true));
		body.setUserData(identifier);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, false, false));
	}


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void reSet() {
		this.status = 0;
		this.body.setActive(false);

	}

}
