package by.kipind.game.olympicgames.sceneElements;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import by.kipind.game.olympicgames.ResourcesManager;

public class DartSteck extends Entity {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================

    protected final ResourcesManager resourcesManager = ResourcesManager.getInstance();
    protected VertexBufferObjectManager vbo;

    private Sprite fon;
    private List<Sprite> darts = new ArrayList<Sprite>();

    protected int objTotal, objNow;

    // ===========================================================
    // ConstructorsIEntity entity,
    // ===========================================================
    public DartSteck(final int kol, final float pX, final float pY, VertexBufferObjectManager vbo) {

	this.vbo = vbo;
	this.objTotal = kol;
	this.objNow = kol;

	fon = new Sprite(pX, pY, resourcesManager.gameGraf.get("streli_ramka"), vbo);
	this.attachChild(fon);

	float startYForDarts = fon.getY() - fon.getHeight() * 0.4f;

	darts.add(new Sprite(fon.getX(), startYForDarts, resourcesManager.gameGraf.get("strela"), vbo));
	this.attachChild(darts.get(0));

	float dartHeight = darts.get(0).getHeight();

	for (int i = 1; i < kol; i++) {
	    darts.add(new Sprite(fon.getX(), startYForDarts + i * dartHeight, resourcesManager.gameGraf.get("strela"), vbo));
	    this.attachChild(darts.get(i));
	}

    }

    // ===========================================================
    // Methods
    // ===========================================================

    public int minusObj() {
	if (objNow > 0) {
	    darts.get(objNow - 1).setVisible(false);
	    // darts.get(objNow-1).setIgnoreUpdate(true);
	    objNow--;
	}
	return objNow;
    }

    public int plusObj() {
	if (objNow < objTotal) {

	    darts.get(objNow).setVisible(true);
	    // darts.get(objNow-1).setIgnoreUpdate(true);
	    objNow++;
	}
	return objNow;
    }

    public void reSet() {
	for (int i = 0; i < darts.size(); i++) {
	    darts.get(i).setVisible(true);
	}
	objNow = objTotal;
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

    public int getObjNow() {
	return objNow;
    }

}
