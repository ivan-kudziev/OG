package by.kipind.game.olympicgames.sceneElements;

import android.content.Context;
import android.graphics.PointF;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;

import by.kipind.game.olympicgames.GameSettings;
import by.kipind.game.olympicgames.ResourcesManager;

public class ShootAngleSelector extends Entity {
    // ===========================================================
    // Constants
    // ===========================================================

    private final float RUNNER_FPS = 60f;
    // ===========================================================

    private final Context context = GameSettings.getAppContext(); //context.getString(R.string.net_game_lb_total_id)
    protected final ResourcesManager resourcesManager = ResourcesManager.getInstance();
    protected VertexBufferObjectManager vbo;
    private final TextOptions tOpt = new TextOptions(HorizontalAlign.LEFT);

    private Sprite cell;
    private Sprite direction;

    protected PointF startXY;
    protected int lineNum;
    private float angleDegVal, maxDegVal, minDegVal;
    private boolean isStarted = false;
    private float rtShag, rtVal;
    // ===========================================================
    // ConstructorsIEntity entity,
    // ===========================================================
    public ShootAngleSelector(final float pX, final float pY, VertexBufferObjectManager vbo) {

	this.vbo = vbo;
	this.startXY = new PointF(pX, pY);
	this.rtShag = 1;
	this.rtVal = 0;

	cell = new Sprite(pX, pY, resourcesManager.gameGraf.get("cell_profil"), vbo);
	// cell.setScale(1f, 1f);
	// cell.setPosition(pX, pY);
	this.attachChild(cell);

	direction = new Sprite(pX, pY, resourcesManager.gameGraf.get("hilow_strelka"), vbo);
	// direction.setScale(1f, 1f);
	direction.setPosition(cell.getX() - direction.getWidth() / 1.8f, cell.getY());
	this.attachChild(direction);

	// this.attachChild(txtDivider);

	this.registerUpdateHandler(new TimerHandler(1 / RUNNER_FPS, true, new ITimerCallback() {
	    @Override
	    public void onTimePassed(final TimerHandler pTimerHandler) {
		if (isStarted) {
		    if (rtVal >= maxDegVal || rtVal <= minDegVal) {
			rtShag = rtShag * -1;
		    }
		    rtVal = rtVal + rtShag;
		    dirMoove(rtVal);

		}
	    }
	}));

    }

    // ===========================================================
    // Methods
    // ===========================================================

    public boolean Start() {

	maxDegVal = 20;
	minDegVal = -20;

	direction.setRotationCenter(0, 0.5f);

	isStarted = true;

	return false;
    }

    public Float Stop() {
	isStarted = false;
	return -Math.round(37.32 * Math.tan(Math.toRadians(direction.getRotation()))) / 10f; // returns value in [1;-1] from degriz value [15;-15]

    }

    private void dirMoove(float rtv) {
	direction.setRotation(rtv);
    }


    /*
     * public float getWight() { return this.mWight; } */
    // ===========================================================
    // Getters & Setters
    // ===========================================================

    public PointF getStartXY() {
	return startXY;
    }

    public void setStartXY(PointF startXY) {
	this.startXY = startXY;
    }

    public void setStartXY(float x, float y) {
	this.startXY.x = x;
	this.startXY.y = y;
    }

    public int getLineNum() {
	return lineNum;
    }

    public void setLineNum(int lineNum) {
	this.lineNum = lineNum;
    }

}
