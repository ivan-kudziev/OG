package by.kipind.game.olympicgames.sceneElements;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.List;

import by.kipind.game.olympicgames.ResourcesManager;

public class UfoSteck extends Entity {

    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================

    protected final ResourcesManager resourcesManager = ResourcesManager.getInstance();
    protected VertexBufferObjectManager vbo;

    private Sprite fon, ufo;
    private List<Sprite> ufos = new ArrayList<Sprite>();

    protected int objTotal, objNow;

    // ===========================================================
    // ConstructorsIEntity entity,
    // ===========================================================
    public UfoSteck(final int kol, final float pX, final float pY, VertexBufferObjectManager vbo) {

	this.vbo = vbo;
	this.objTotal = kol;
	this.objNow = kol;

	fon = new Sprite(pX, pY, resourcesManager.gameGraf.get("ufo_counter"), vbo);
	this.attachChild(fon);

	// float startYForDarts = fon.getY() - fon.getHeight() * 0.5f+;

	ufo = new Sprite(0, 0, resourcesManager.gameGraf.get("small_ufo"), vbo);
	// this.attachChild(ufos.get(0));


	float ufoHeight = ufo.getHeight() / 2;
	float ufoWidth = ufo.getWidth() / 2;
	float startYForUfos = fon.getY() - fon.getHeight() * 0.5f - ufoHeight;
	float startXForUfos = fon.getX() + fon.getWidth() / 2 - (ufoWidth + 4.1f);

	int j = 1;
	for (int i = 1; i <= kol; i++) {
	    if (j > 3) {// 2 * j * ufoHeight - fon.getHeight() > 0
		j = 1;
	    }
	    ufos.add(new Sprite(startXForUfos - (((i - 1) / 3) * (2 * ufoWidth + 4.1f) + 1), startYForUfos + 2 * j * (ufoHeight + 1.7f) + 1.5f, resourcesManager.gameGraf
		    .get("small_ufo"), vbo));
	    this.attachChild(ufos.get(i - 1));
	    j++;
	}

    }

    // ===========================================================
    // Methods
    // ===========================================================

    public int minusObj() {
	if (objNow > 0) {
	    ufos.get(objNow - 1).setVisible(false);
	    // darts.get(objNow-1).setIgnoreUpdate(true);
	    objNow--;
	}
	return objNow;
    }

    public int plusObj() {
	if (objNow <= objTotal) {

	    ufos.get(objNow).setVisible(true);
	    // darts.get(objNow-1).setIgnoreUpdate(true);
	    objNow++;
	}
	return objNow;
    }

    public void reSet() {
	for (int i = 0; i < ufos.size(); i++) {
	    ufos.get(i).setVisible(true);
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
