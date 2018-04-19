package by.kipind.game.olympicgames.sprite;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import by.kipind.game.olympicgames.ResourcesManager;

/**
 * Created by kip on 03.09.2017.
 */

public class Sensor extends Sprite {
	final String LOG_TAG = "myLogs";
	private final int[] animFrame = new int[]{0, 1, 2};

	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------
	public Body body;
	private int status = 0; // 0-netral ; 1 - got contact ; -1 - lost contact
	private PhysicsConnector myPhysicsConnector;

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public Sensor(float pX, float pY, float pWMlt, float pHMlp, VertexBufferObjectManager vbo, PhysicsWorld physicsWorld, String userData, String graficName) {
		super(pX, pY, (ITiledTextureRegion) ResourcesManager.getInstance().gameGraf.get(graficName), vbo);
		this.setWidth(this.getWidth() * pWMlt);
		this.setHeight(this.getHeight() * pHMlp);

		createPhysics(physicsWorld, userData);

	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public void setBodyPos(float x, float y, float rotate) {
		this.body.setTransform(x / 32, y / 32, rotate);
	}

	private void createPhysics(PhysicsWorld physicsWorld, String identifier) {
		switch (identifier) {
			case "stone":
				body = PhysicsFactory.createCircleBody(physicsWorld, this, BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(1000, 1f, 0f, false));
				break;
			default:
				body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(0, 0, 0, true));

		}

		body.setUserData(identifier);
		myPhysicsConnector = new PhysicsConnector(this, body, true, false);
		physicsWorld.registerPhysicsConnector(myPhysicsConnector);
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
