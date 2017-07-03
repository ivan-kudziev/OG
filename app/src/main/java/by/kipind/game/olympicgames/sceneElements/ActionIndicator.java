package by.kipind.game.olympicgames.sceneElements;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import by.kipind.game.olympicgames.ResourcesManager;

public class ActionIndicator extends HUD {
    // ===========================================================
    // Constants
    // ===========================================================

    private final float RUNNER_FPS = 60f;
    public static float RUNNER_RED_WIGHT;

    // ===========================================================
    // Fields
    // ===========================================================

    private Sprite mIndicFon;
    private Sprite mIndicRedArea;
    private Sprite mIndRunnerOn;
    private Sprite mIndRunnerOff;

    private float mIndRedAreaHalf;
    private final int mIndRedAreaAligh;
    private int mIndBorderReachFlag;

    private final float mIndStartX, mIndEndX, mIndHalfWight;

    private Float mIndicVal = 0f, mIndicShag = 0f;
    private boolean runnerSwitcher;

    // ===========================================================
    // Constructors
    // ===========================================================
    public ActionIndicator(final float pX, final float pY, final float iWKaf, final float iHKaf, int iType, final Camera pCamera, VertexBufferObjectManager vbo) {
	this.setCamera(pCamera);

	mIndRedAreaAligh = iType;
	mIndBorderReachFlag = 1;

	this.mIndicFon = new Sprite(pX, pY, ResourcesManager.getInstance().gameGraf.get("ge_ai_fon"), vbo);
	this.mIndicRedArea = new Sprite(pX, pY, ResourcesManager.getInstance().gameGraf.get("ge_ai_red"), vbo);
	this.mIndRunnerOn = new Sprite(pX, pY, ResourcesManager.getInstance().gameGraf.get("ge_ai_runner_on"), vbo);
	this.mIndRunnerOff = new Sprite(pX, pY, ResourcesManager.getInstance().gameGraf.get("ge_ai_runner_off"), vbo);

	this.registerUpdateHandler(new TimerHandler(1 / RUNNER_FPS, true, new ITimerCallback() {
	    @Override
	    public void onTimePassed(final TimerHandler pTimerHandler) {
		ActionIndicator.this.onMoveAction();
	    }
	}));

	this.attachChild(this.mIndicFon);
	this.attachChild(this.mIndicRedArea);
	this.attachChild(this.mIndRunnerOn);
	this.attachChild(this.mIndRunnerOff);

	setStartIndWH(iWKaf, iHKaf);

	mIndHalfWight = this.mIndicFon.getWidth() / 2;
	mIndStartX = this.mIndicFon.getX() - mIndHalfWight;
	mIndEndX = this.mIndicFon.getX() + mIndHalfWight;

	// mIndicShag = (2 * mIndHalfWight) / (RUNNER_FPS * 3);

	mIndicVal = this.mIndicFon.getX();

	setStartRedWH(iWKaf, iHKaf);
	// setRedAreaWH(0.5f, 1f);

	ActionIndicator.RUNNER_RED_WIGHT = this.mIndicRedArea.getWidth();
	this.switchRunner(1);
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

    private void onMoveAction() {
	mIndicVal = mIndicVal + mIndicShag;
	if (mIndicVal > mIndEndX) {
	    mIndicVal = mIndEndX;
	    mIndBorderReachFlag = 1;
	    this.switchRunner(1);
	    mIndicShag *= -1;
	} else if (mIndicVal < mIndStartX) {
	    mIndicVal = mIndStartX;
	    mIndBorderReachFlag = -1;
	    this.switchRunner(1);
	    mIndicShag *= -1;
	}
	this.mIndRunnerOn.setPosition(mIndicVal, mIndRunnerOn.getY());
	this.mIndRunnerOff.setPosition(mIndicVal, mIndRunnerOff.getY());

    }

    // ---public

    public void inverMoveDerection() {
	mIndicShag *= -1;
    }

    public int getResult() {
	int res = -2;
	if (mIndicVal >= this.mIndicRedArea.getX() - mIndRedAreaHalf && mIndicVal <= this.mIndicRedArea.getX() + mIndRedAreaHalf) {
	    res = 0;
	} else if (mIndicVal > this.mIndicRedArea.getX() + mIndRedAreaHalf) {
	    res = 1;
	} else if (mIndicVal < this.mIndicRedArea.getX() - mIndRedAreaHalf) {
	    res = -1;
	}
	return res;
    }

    public void setRedAreaWH(float iWigthKaf, float iHeightKaf) {
	this.mIndicRedArea.setWidth(ActionIndicator.RUNNER_RED_WIGHT * iWigthKaf);
	this.mIndicRedArea.setHeight(this.mIndicRedArea.getHeight() * iHeightKaf);
	mIndRedAreaHalf = this.mIndicRedArea.getWidth() / 2;
	setRedAreaAlign();

    }

    public void switchRunner(int swFlag) {
	switch (swFlag) {
	case 1:
	    runnerSwitcher = true;
	    break;
	case 0:
	    runnerSwitcher = !runnerSwitcher;
	    break;
	case -1:
	    runnerSwitcher = false;
	    break;

	default:
	    runnerSwitcher = !runnerSwitcher;
	    break;
	}

	this.mIndRunnerOn.setVisible(runnerSwitcher);
	this.mIndRunnerOff.setVisible(!runnerSwitcher);

    }

    public void setShag(float kaf) {
	if (kaf != 0) {
	    mIndicShag = (2 * mIndHalfWight) / (RUNNER_FPS * kaf);
	} else {
	    mIndicShag = 0f;
	}
    }

    // ===========================================================
    // Getters & Setters
    // ===========================================================

    public Float getmIndicShag() {
	return mIndicShag;
    }

    public void setmIndicShag(Float mIndicShag) {
	this.mIndicShag = mIndicShag;
    }

    public int getBorderReachFlag() {
	return mIndBorderReachFlag;
    }

    public void setBorderReachFlag(int mIndBorderReachFlag) {
	this.mIndBorderReachFlag = mIndBorderReachFlag;
    }

}
