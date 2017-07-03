package by.kipind.game.olympicgames.sceneElements;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.graphics.PointF;
import by.kipind.game.olympicgames.ResourcesManager;

public class DartTarget extends Entity {

    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================

    protected final ResourcesManager resourcesManager = ResourcesManager.getInstance();
    protected VertexBufferObjectManager vbo;

    private Sprite fon;
    private List<Sprite> marks = new ArrayList<Sprite>();

    protected PointF centerXY;

    // ===========================================================
    // ConstructorsIEntity entity,
    // ===========================================================
    public DartTarget(final float pX, final float pY, VertexBufferObjectManager vbo) {

	this.vbo = vbo;

	fon = new Sprite(pX, pY, resourcesManager.gameGraf.get("cell"), vbo);
	this.attachChild(fon);
	this.centerXY = new PointF(pX, pY);

    }

    // ===========================================================
    // Methods
    // ===========================================================

    public void addMark(float deltaX, float deltaY) {
	marks.add(new Sprite(centerXY.x + deltaX, centerXY.y + deltaY, resourcesManager.gameGraf.get("cell_metka"), vbo));
	this.attachChild(marks.get(marks.size() - 1));

    }

    public void reSet() {
	for (Sprite mark : marks) {
	    this.detachChild(mark);
	}
	marks.clear();
    }

    @Override
    public float getWidth() {
	return this.fon.getWidth();
    }

    @Override
    public float getHeight() {
	return this.fon.getHeight();
    }

    /* public float getWight() { return this.mWight; } */
    // ===========================================================
    // Getters & Setters
    // ===========================================================

}
