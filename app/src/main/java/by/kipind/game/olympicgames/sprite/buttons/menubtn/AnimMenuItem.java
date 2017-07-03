package by.kipind.game.olympicgames.sprite.buttons.menubtn;

import org.andengine.entity.scene.menu.item.AnimatedSpriteMenuItem;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class AnimMenuItem extends AnimatedSpriteMenuItem {

    public AnimMenuItem(int pID, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
	super(pID, pTiledTextureRegion, pVertexBufferObjectManager);

    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onSelected() {
	this.setCurrentTileIndex(1);
    }

    @Override
    public void onUnselected() {
	this.setCurrentTileIndex(0);
    }
}
