package by.kipind.game.olympicgames.resourceObjects;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

public class ShootingGraf extends BaseResConteiner {
    // Game Texture

    public ShootingGraf(BaseGameActivity activity) {

	super(activity);
	loadGraphics();
    }

    private void loadGraphics() {

	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

	textureRegionRes.put("game_borders_bl_region", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "game/borders_bl.png"));
	textureRegionRes.put("game_panel_region", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "game/hud_panel_fon.png"));
	textureRegionRes.put("hud_element_chat_fon", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "game/chat_bg.png"));
	textureRegionRes.put("game_borders_hud_fon", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "game/info_bg.png"));
	textureRegionRes.put("game_borders_hud_fon_up", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "game/info_bg_up.png"));
	textureRegionRes.put("bt_replay", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "game/ico_bt_replay.png"));
	textureRegionRes.put("bt_back", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "game/ico_bt_exit.png"));
	textureRegionRes.put("bt_shoot_left", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(hudTextureAtlas, activity, "btn/atlas_shoot_left.png", 3, 1));
	textureRegionRes.put("bt_shoot_right", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(hudTextureAtlas, activity, "btn/atlas_shoot_right.png", 3, 1));
	textureRegionRes.put("svetofor_region", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(hudTextureAtlas, activity, "sprites/atlas_svetfor.jpg", 3, 2));

	textureRegionRes.put("game_background_region", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/shooting/shoot_fon.png"));
	textureRegionRes.put("tb_left", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/shooting/shoot_tb_left.png"));
	textureRegionRes.put("tb_right", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/shooting/shoot_tb_right.png"));
	textureRegionRes.put("shoot_tree", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/shooting/shoot_tree.png"));
	textureRegionRes.put("shoot_ufo_sh", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/shooting/shoot_ufo_sh.png"));
	textureRegionRes.put("shoot_area", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/shooting/shoot_area.png"));
	textureRegionRes.put("shoot_area_red", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/shooting/shoot_area_red.png"));
	textureRegionRes.put("shoot_ufo_ico", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/shooting/shoot_ufo.png"));
	textureRegionRes.put("ufo", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/shooting/ufo.png"));
	textureRegionRes.put("ufo_counter", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/shooting/ufo_counter.png"));
	textureRegionRes.put("small_ufo", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/shooting/small_ufo.png"));
	
	// --Tiled
	textureRegionRes.put("player_region", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "sprites/atlas_shoot_pl.png", 3, 1));
	textureRegionRes.put("shoot_ufo", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "sprites/atlas_ufo.png", 5, 1));

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
