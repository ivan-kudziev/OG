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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import by.kipind.game.olympicgames.ResourcesManager;

public abstract class Kaiak extends AnimatedSprite {
	// final String LOG_TAG = "myLogs";

	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private final int[] animFrame = new int[]{1, 2, 3, 4, 5, 6};
	public Body body;
	public List<Integer> shadowTrace = new ArrayList<Integer>();
	private float startXPos;
	private float startYPos;
	private int sStatus = -1;
	private int plState = 0; //
	private boolean isFinish = false;
	private int contacts = 0;
	private float speed = 0;
	private float maxSpeed = 10000;
	private float speedBeforJump = 0;
	private Long frameDuration = 0l;
	private int longJumpDeg = 90;

	// private long[] spriteFameDuration = new long[animFrame.length];

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public Kaiak(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, (ITiledTextureRegion) ResourcesManager.getInstance().gameGraf.get("player_region"), vbo);
		// this.setScale(0.45f);

		this.setHeight(this.getHeight() * 1f);
		this.setWidth(this.getWidth() * 1f);

		createPhysics(camera, physicsWorld);

		// Arrays.fill(spriteFameDuration, 1000);
	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public float getSpeed() {
		return speed;

	}

	public void setSpeed(float speed) {
		if (speed > 1) {
			this.speed = speed;
		} else {
			this.speed = 1;
		}
	}

	public void setXSpeed(float speed) {
		if (speed > 1) {
			body.setLinearVelocity(speed, 0f);
			// body.applyLinearImpulse(speed, 0, body.getPosition().x,
			// body.getPosition().y);
			this.speed = speed;
		} else {
			this.speed = 1;
		}
	}

	public void onFinish() {
		this.isFinish = true;

	}

	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		body.setUserData("bout");
		body.getFixtureList().get(0).setFriction(0.25f);
		body.setFixedRotation(true);

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);
				setSpeed(body.getLinearVelocity().x);

			}

		});

		setStartXYPos(body.getPosition().x, body.getPosition().y);
	}

	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

	public void onThrow() {
		body.setLinearVelocity(0f, 0f);
		this.setCurrentTileIndex(1);

	}

	public void onStop() {
		body.setLinearVelocity(0f, 0f);
		plState = 6;
	}

	public void onFailStop() {
		body.setLinearVelocity(0f, 0f);
		plState = 5;
	}


	public void powFunctionRun() {
		if (isFinish || plState == 2 || plState == 3 || speed > maxSpeed) {
			return;
		}
		if (getCurrentTileIndex() == 0) {
			this.setCurrentTileIndex(1);
			this.frameDuration = 0l;
			body.applyLinearImpulse(1f, 0, body.getPosition().x, body.getPosition().y);
		} else {
			body.applyLinearImpulse((float) Math.pow(0.1, this.getSpeed() / 50), 0, body.getPosition().x, body.getPosition().y);
		}
		plState = 1;
	}

	public void run() {
		if (isFinish || plState == 2 || plState == 3) {
			return;
		}
		if (getCurrentTileIndex() == 0) {
			this.setCurrentTileIndex(1);
			this.frameDuration = 0l;
			body.applyLinearImpulse(5f, 0, body.getPosition().x, body.getPosition().y);
		} else {
			body.applyLinearImpulse(3f, 0, body.getPosition().x, body.getPosition().y);
		}
		plState = 1;
	}


	public boolean jumpLong(Integer jumpDeg) {
		int deg = this.longJumpDeg;
		// :TODO
		ResourcesManager.getInstance().playSoundFromStack("barier_jump");
		if (plState == 1) {
			if (!jumpDeg.equals(null)) {
				deg = jumpDeg;
			}
			stopAnimation(8);
			speedBeforJump = this.speed * 0.8f;
			body.setLinearVelocity(0f, 0f);
			body.applyLinearImpulse((float) (speedBeforJump * Math.cos(Math.toRadians(deg))), (float) (speedBeforJump * Math.sin(Math.toRadians(deg))), body.getPosition().x,
					body.getPosition().y);
			speedBeforJump = 4;

		} else {
			return false;
		}
		plState = 3;
		return true;
	}

	public void setLinearSpeed(float newXSpeed, float newYSpeed) {
		body.setLinearVelocity(newXSpeed, newYSpeed);
	}

	public void changeFrameDuration(Long frameDuration, int[] spriteFrames, int currentFrame) {

		if (currentFrame > spriteFrames.length - 1) {
			currentFrame = spriteFrames.length - 1;
		}
		int iter = currentFrame, i = 0;

		int[] spriteFramesMod = new int[spriteFrames.length];
		long[] spriteFameDuration = new long[spriteFrames.length];

		Arrays.fill(spriteFameDuration, frameDuration);

		do {
			spriteFramesMod[i] = spriteFrames[iter];
			if (iter == spriteFrames.length - 1) {
				iter = 0;
			} else {
				iter++;
			}
			i++;

		} while (iter != currentFrame);

		animate(spriteFameDuration, spriteFramesMod, true);

	}

	public int getContacts() {
		return contacts;
	}

	public void setContacts(int contType) {
		contacts = contType;

	}

	public float getSpeedBeforJump() {
		return speedBeforJump;
	}

	public void setSpeedBeforJump(float speedBeforJump) {
		this.speedBeforJump = speedBeforJump;
	}

	public int getLongJumpDeg() {
		return longJumpDeg;
	}

	public void setLongJumpDeg(int longJumpDeg) {
		this.longJumpDeg = longJumpDeg;
	}

	public int getPlState() {
		return plState;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float getStartXPos() {
		return startXPos;
	}

	public void setStartXPos(float startXPos) {
		this.startXPos = startXPos;
	}

	public float getStartYPos() {
		return startYPos;
	}

	public void setStartYPos(float startYPos) {
		this.startYPos = startYPos;
	}

	public void setStartXYPos(float startXPos, float startYPos) {
		this.startXPos = startXPos;
		this.startYPos = startYPos;
	}

	public void reSet() {
		this.body.setTransform(this.startXPos, this.startYPos, 0);
		this.body.setLinearVelocity(0, 0);
		this.body.setActive(true);
		this.stopAnimation(0);

		plState = 0;
		isFinish = false;
		speed = 0;

	}

	public void setShadowTrace(List<Integer> shadowTrace) {
		this.shadowTrace.clear();
		this.shadowTrace.addAll(shadowTrace);
	}

}
