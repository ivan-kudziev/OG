package by.kipind.game.olympicgames.sceneElements;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import by.kipind.game.olympicgames.ResourcesManager;

public class PowerInidcator extends HUD {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private Sprite mIndicFon;
    private Sprite mIndicRedArea;
    private Sprite mIndicSkin;

    private float mIndRedAreaHalf;
    private final int mIndRedAreaAligh;
    private final float mIndMaxVal;

    private final float mIndStartX, mIndEndX, mIndHalfWight;

    private Float mIndicVal = 0f;

    // ===========================================================
    // Constructors
    // ===========================================================
    public PowerInidcator(final float pX, final float pY, float maxValue, final Camera pCamera, VertexBufferObjectManager vbo) {
	this.setCamera(pCamera);

	mIndRedAreaAligh = -1;
	mIndMaxVal = maxValue;

	this.mIndicFon = new Sprite(pX, pY, ResourcesManager.getInstance().gameGraf.get("ge_pi_fon"), vbo);
	this.mIndicRedArea = new Sprite(pX, pY, ResourcesManager.getInstance().gameGraf.get("ge_pi_red"), vbo);
	this.mIndicSkin = new Sprite(pX, pY, ResourcesManager.getInstance().gameGraf.get("ge_pi_skin"), vbo);

	this.mIndicFon.setScale(1f, 0.9f);
	this.mIndicRedArea.setScale(1f, 0.9f);
	this.mIndicSkin.setScale(1f, 0.9f);

	this.attachChild(this.mIndicFon);
	this.attachChild(this.mIndicRedArea);
	this.attachChild(this.mIndicSkin);

	setStartIndWH(1, 1);

	mIndHalfWight = this.mIndicFon.getWidth() / 2;
	mIndStartX = this.mIndicFon.getX() - mIndHalfWight;
	mIndEndX = this.mIndicFon.getX() + mIndHalfWight;

	setStartRedWH(0, 1);

    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void setStartIndWH(float iWigthKaf, float iHeightKaf) {
	this.mIndicFon.setWidth(this.mIndicFon.getWidth() * iWigthKaf);
	this.mIndicFon.setHeight(this.mIndicFon.getHeight() * iHeightKaf);
    }

    private void setStartRedWH(float iWigthKaf, float iHeightKaf) {
	this.mIndicRedArea.setWidth(this.mIndicRedArea.getWidth() * iWigthKaf);
	this.mIndicRedArea.setHeight(this.mIndicRedArea.getHeight() * iHeightKaf);
	mIndRedAreaHalf = this.mIndicRedArea.getWidth() / 2;
	setRedAreaAlign();

    }

    private void setRedAreaAlign() {
	switch (mIndRedAreaAligh) {
	case -1:
	    this.mIndicRedArea.setX(mIndStartX + mIndRedAreaHalf);
	    break;// left
	case 0:
	    break;// center
	case 1:
	    this.mIndicRedArea.setX(mIndEndX - mIndRedAreaHalf);
	    break;// right

	default:
	    break;
	}

    }

    // ---public methods

    public void changeValue(float newVal) {
	mIndicVal = 2 * mIndHalfWight * newVal / mIndMaxVal;
	if (mIndicVal > 2 * mIndHalfWight) {
	    mIndicVal = 2 * mIndHalfWight;
	} else if (mIndicVal < 0) {
	    mIndicVal = 0f;
	}
	
	this.mIndicRedArea.setWidth(mIndicVal);
	mIndRedAreaHalf = this.mIndicRedArea.getWidth() / 2;
	setRedAreaAlign();
    }

    // ===========================================================
    // Getters & Setters
    // ===========================================================

    @Override
    public float getHeight() {
	return this.mIndicFon.getHeight() * 2;
    }

    @Override
    public float getWidth() {
	return this.mIndicFon.getWidth() * 1;
    }
}
