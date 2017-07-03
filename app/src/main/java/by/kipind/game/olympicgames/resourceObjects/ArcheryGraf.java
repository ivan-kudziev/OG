package by.kipind.game.olympicgames.resourceObjects;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

public class ArcheryGraf  extends BaseResConteiner{
    // Game Texture
   
    public ArcheryGraf(BaseGameActivity activity) {

	super(activity);
	loadGraphics();
    }

    private void loadGraphics() {

	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

	textureRegionRes.put("game_borders_bl_region", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "game/borders_bl.png"));
	textureRegionRes.put("hud_element_chat_fon", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "game/chat_bg.png"));
	textureRegionRes.put("game_borders_hud_fon", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "game/info_bg.png"));
	textureRegionRes.put("game_borders_hud_fon_up", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "game/info_bg_up.png"));
	textureRegionRes.put("bt_replay", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "game/ico_bt_replay.png"));
	textureRegionRes.put("bt_back", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "game/ico_bt_exit.png"));
	textureRegionRes.put("bt_arch_shoot", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "btn/atlas_arch_shoot.png", 3, 1));
	textureRegionRes.put("bt_arch_angle", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "btn/atlas_arch_angle.png", 3, 1));

	
	textureRegionRes.put("game_background_region", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/arch/arch_fon.jpg"));
	textureRegionRes.put("cell_profil", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/arch/arch_cell_prof.png"));
	textureRegionRes.put("cell", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/arch/arch_cell.png"));
	textureRegionRes.put("hilow_strelka", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/arch/arch_hilow_strelka.png"));
	textureRegionRes.put("cell_metka", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/arch/arch_cell_metka.png"));
	textureRegionRes.put("shoot_line", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/arch/arch_shoot_line.png"));
	textureRegionRes.put("streli_ramka", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/arch/arch_strela_fon.png"));
	textureRegionRes.put("strela", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/arch/arch_strela.png"));
	
	// --Tiled
	textureRegionRes.put("player_region", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "sprites/arch_pl.png", 1, 1));
	
	try {
	    BaseResConteiner.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
	    BaseResConteiner.gameTextureAtlas.load();
	    BaseResConteiner.hudTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
	    BaseResConteiner.hudTextureAtlas.load();
	} catch (final TextureAtlasBuilderException e) {
	    Debug.e(e);
	}

    }
}
