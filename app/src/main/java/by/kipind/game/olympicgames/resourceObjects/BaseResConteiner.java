package by.kipind.game.olympicgames.resourceObjects;

import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseResConteiner {

    public static Map<String, ITextureRegion> textureRegionRes;

    public BaseGameActivity activity;
    public static BuildableBitmapTextureAtlas gameTextureAtlas;
    public static BuildableBitmapTextureAtlas hudTextureAtlas;
    public static BuildableBitmapTextureAtlas adTextureAtlas;

    public BaseResConteiner(BaseGameActivity activity) {
	this.activity = activity;
	unloadGraf();

    }

    public Map<String, ITextureRegion> getTextureRegionMap() {
	return textureRegionRes;
    }

    public void unloadGraf() {
	try {
	    if (textureRegionRes != null) {
		gameTextureAtlas.unload();
		hudTextureAtlas.unload();
		adTextureAtlas.unload();
		textureRegionRes.clear();
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
		hudTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
		adTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);

	    } else {
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
		hudTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
		adTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
		textureRegionRes = new HashMap<String, ITextureRegion>();

	    }

	} catch (NullPointerException e) {
	    // TODO: handle exception
	}

    }

}
