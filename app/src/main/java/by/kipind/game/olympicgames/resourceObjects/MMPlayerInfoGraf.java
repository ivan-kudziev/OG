package by.kipind.game.olympicgames.resourceObjects;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

public class MMPlayerInfoGraf  extends BaseResConteiner{
    //  Texture
   
    public MMPlayerInfoGraf(BaseGameActivity activity) {

	super(activity);
	MMBaseInfoGraphics();
    }

    private void MMBaseInfoGraphics() {

	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	// gameTextureAtlas = new
	// BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,
	// TextureOptions.BILINEAR);

	textureRegionRes.put("activity_background_region", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "menu/baseInfo/reg_background.jpg"));
	textureRegionRes.put("nick_form_background_region", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "menu/baseInfo/nick_name_panel.png"));
	// textureRegionRes.put("bt_yes_region", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "menu/baseInfo/panel_yes.png"));
	// textureRegionRes.put("bt_no_region", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "menu/baseInfo/panel_no.png"));
	
	
	// --Tiled
	textureRegionRes.put("bt_exit_region", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(hudTextureAtlas, activity, "btn/anm_exit.png", 2, 1));
	textureRegionRes.put("bt_ok_region", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(hudTextureAtlas, activity, "btn/anm_ok.png", 2, 1));
	textureRegionRes.put("ms_btn_region", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(hudTextureAtlas, activity, "btn/atlas_ms_btn.png", 4, 1));
	textureRegionRes.put("loading_region", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(hudTextureAtlas, activity, "sprites/loading.png", 4, 3));
	
	
	try {
	    this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
	    this.gameTextureAtlas.load();
	    this.hudTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
	    this.hudTextureAtlas.load();
	} catch (final TextureAtlasBuilderException e) {
	    Debug.e(e);
	}

    }
}
