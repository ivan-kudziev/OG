package by.kipind.game.olympicgames.resourceObjects;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

public class PikeThrowGraf  extends BaseResConteiner{
    // Game Texture
   
    public PikeThrowGraf(BaseGameActivity activity) {

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

	textureRegionRes.put("ge_pi_fon", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "sprites/power_identific/pi_fon.png"));
	textureRegionRes.put("ge_pi_red", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "sprites/power_identific/pi_red.png"));
	textureRegionRes.put("ge_pi_skin", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "sprites/power_identific/pi_form.png"));
	
	textureRegionRes.put("svetofor_region", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(hudTextureAtlas, activity, "sprites/atlas_svetfor.jpg", 3, 2));
	textureRegionRes.put("bt_run_region", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(hudTextureAtlas, activity, "btn/atlas_bt_run.png", 3, 1));
	textureRegionRes.put("bt_pike_throw_region", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(hudTextureAtlas, activity, "btn/atlas_bt_pike_throw.png", 3, 1));
	textureRegionRes.put("game_pike_metraj", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "game/pike_throw/pike_metraj.jpg"));
	textureRegionRes.put("grass", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "game/pike_throw/grass.png"));
	
	textureRegionRes.put("char_0", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "charset/number_0.png"));
	textureRegionRes.put("char_1", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "charset/number_1.png"));
	textureRegionRes.put("char_2", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "charset/number_2.png"));
	textureRegionRes.put("char_3", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "charset/number_3.png"));
	textureRegionRes.put("char_4", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "charset/number_4.png"));
	textureRegionRes.put("char_5", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "charset/number_5.png"));
	textureRegionRes.put("char_6", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "charset/number_6.png"));
	textureRegionRes.put("char_7", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "charset/number_7.png"));
	textureRegionRes.put("char_8", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "charset/number_8.png"));
	textureRegionRes.put("char_9", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "charset/number_9.png"));
	
	
	
	textureRegionRes.put("game_background_region", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/running/fon_beg.jpg"));
	
	textureRegionRes.put("player_region", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "sprites/atlas_beg_pike.png", 5, 2));
	textureRegionRes.put("pike_region", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "sprites/atlas_pike.png", 3, 1));

	textureRegionRes.put("game_background1_region", BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "game/long_jump/sport_yuard.jpg"));
	textureRegionRes.put("game_ground_line", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/ground_line2.bmp"));
	textureRegionRes.put("jump_yuard", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/long_jump/long_jamp_yuard.png"));
	textureRegionRes.put("stop_line", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/finish_line1.png"));
	textureRegionRes.put("down_mark", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/long_jump/down_mark.png"));
	textureRegionRes.put("up_mark", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/long_jump/up_mark.png"));

	textureRegionRes.put("metraj_20", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/running/metraj_20.png"));
	textureRegionRes.put("metraj_40", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/running/metraj_40.png"));
	textureRegionRes.put("metraj_line", BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game/running/metraj_line.png"));

	// ------adv
	textureRegionRes.put("bras_small", BitmapTextureAtlasTextureRegionFactory.createFromAsset(adTextureAtlas, activity, "adv/bras_small.png"));
	textureRegionRes.put("clock_small", BitmapTextureAtlasTextureRegionFactory.createFromAsset(adTextureAtlas, activity, "adv/clock_small.png"));
	textureRegionRes.put("host_small", BitmapTextureAtlasTextureRegionFactory.createFromAsset(adTextureAtlas, activity, "adv/host_small.png"));

	try {
	    BaseResConteiner.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
	    BaseResConteiner.gameTextureAtlas.load();
	    BaseResConteiner.hudTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
	    BaseResConteiner.hudTextureAtlas.load();
	    BaseResConteiner.adTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
	    BaseResConteiner.adTextureAtlas.load();
	} catch (final TextureAtlasBuilderException e) {
	    Debug.e(e);
	}
    }
}
