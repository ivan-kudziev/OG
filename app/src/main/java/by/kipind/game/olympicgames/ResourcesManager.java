package by.kipind.game.olympicgames;

import android.graphics.Color;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import by.kipind.game.olympicgames.activitys.GameActivity;
import by.kipind.game.olympicgames.resourceObjects.ArcheryGraf;
import by.kipind.game.olympicgames.resourceObjects.LongJumpGraf;
import by.kipind.game.olympicgames.resourceObjects.MMPlayerInfoGraf;
import by.kipind.game.olympicgames.resourceObjects.PikeThrowGraf;
import by.kipind.game.olympicgames.resourceObjects.RaftingGraf;
import by.kipind.game.olympicgames.resourceObjects.Run100Graf;
import by.kipind.game.olympicgames.resourceObjects.RunBarGraf;
import by.kipind.game.olympicgames.resourceObjects.ShootingGraf;

public class ResourcesManager   {
    // ---------------------------------------------
    // VARIABLES
    // ---------------------------------------------

    private static final ResourcesManager INSTANCE = new ResourcesManager();

    public GoogleApiClient googleApiClient;
    public Engine engine;
    public GameActivity activity;
    public BoundCamera camera;
    public VertexBufferObjectManager vbom;

    public Font font_pix_kir;
    private XmlPullParser lvlInfParser;

    // ---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    // ---------------------------------------------
    public ITextureRegion splash_region;
    private BitmapTextureAtlas splashTextureAtlas;

    public BuildableBitmapTextureAtlas flagTextureAtlas;
    public Map<String, ITextureRegion> flagGraf;

    public ITextureRegion menu_background_region;
    public ITiledTextureRegion play_sett_inf;
    public ITiledTextureRegion play_run100;
    public ITiledTextureRegion play_run_barier;
    public ITiledTextureRegion play_long_jump;
    public ITiledTextureRegion play_pike_throw;
    public ITiledTextureRegion play_archery;
    public ITiledTextureRegion play_shooting;
    public ITiledTextureRegion play_rafting;
    public ITiledTextureRegion play_exit;
    public ITiledTextureRegion play_lb;
    public ITextureRegion ico_run;
    public ITextureRegion ico_barier;
    public ITextureRegion ico_pike;
    public ITextureRegion ico_long_jump;
    public ITextureRegion info_bg;
    public ITextureRegion ico_shoot;
    public ITextureRegion ico_arch;
    public ITextureRegion ico_raf;

    public ITextureRegion rate_fon;
    public ITextureRegion rate_black_star;
    public ITextureRegion rate_gold_star;
    public ITextureRegion rate_mail;

    public ITextureRegion ad_status;

    private Music music;
    private Music music1;
    private Map<String, Sound> soundStack = new HashMap<String, Sound>();

    private BuildableBitmapTextureAtlas menuTextureAtlas;
    public Map<String, ITextureRegion> gameGraf;

    // ---------------------------------------------
    // CLASS LOGIC
    // ---------------------------------------------

    public void loadMenuResources() {
        loadMenuGraphics();
        loadMenuAudio();
        loadMenuFonts();
    }

    public void loadGameResources(int gameId) {
        loadGameResources(gameId, this.activity);
    }

    public void loadGameResources(int gameId, BaseGameActivity activity) {

        switch (gameId) {
            case GameSettings.ACTIVITY_ID_SETTINGS_INFO:

                gameGraf = new MMPlayerInfoGraf(activity).getTextureRegionMap();
                loadRun100Audio();
                break;
            case GameSettings.ACTIVITY_ID_RUN100:
                gameGraf = new Run100Graf(activity).getTextureRegionMap();
                loadRun100Audio();
                break;
            case GameSettings.ACTIVITY_ID_RUN_BARIER:
                gameGraf = new RunBarGraf(activity).getTextureRegionMap();
                loadRun100Audio();
                break;
            case GameSettings.ACTIVITY_ID_LONG_JUMP:
                gameGraf = new LongJumpGraf(activity).getTextureRegionMap();
                loadRun100Audio();

                break;
            case GameSettings.ACTIVITY_ID_PIKE_THROW:
                gameGraf = new PikeThrowGraf(activity).getTextureRegionMap();
                loadRun100Audio();

                break;
            case GameSettings.ACTIVITY_ID_ARCHERY:
                gameGraf = new ArcheryGraf(activity).getTextureRegionMap();
                loadRun100Audio();

                break;
            case GameSettings.ACTIVITY_ID_SHOOTING:
                gameGraf = new ShootingGraf(activity).getTextureRegionMap();
                loadRun100Audio();
                break;
            case GameSettings.ACTIVITY_ID_RAFTING:
                gameGraf = new RaftingGraf(activity).getTextureRegionMap();
                loadRun100Audio();

                break;

            default:
                break;
        }
        loadGameFonts();

    }

    private void loadMenuGraphics() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
        menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);

        menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.jpg");
        play_sett_inf = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity, "mm_si.png", 2, 1);
        play_run100 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity, "mm_r.png", 2, 1);
        play_run_barier = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity, "mm_rb.png", 2, 1);
        play_long_jump = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity, "mm_lj.png", 2, 1);
        play_pike_throw = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity, "mm_mk.png", 2, 1);
        play_archery = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity, "mm_arch.png", 2, 1);
        play_shooting = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity, "mm_shoot.png", 2, 1);
        play_rafting = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity, "mm_shoot.png", 2, 1);
        play_exit = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity, "mm_exit.png", 2, 1);
        play_lb = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity, "mm_record.png", 2, 1);

        ico_run = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "gameTypeIco/ico_run.png");
        ico_barier = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "gameTypeIco/ico_barier.png");
        ico_long_jump = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "gameTypeIco/ico_jump.png");
        ico_pike = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "gameTypeIco/ico_pike.png");
        ico_shoot = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "gameTypeIco/ico_shoot.png");
        ico_arch = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "gameTypeIco/ico_arch.png");
        ico_raf = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "gameTypeIco/ico_shoot.png");

        rate_fon = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "rate/rate_fon4.png");
        rate_black_star = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "rate/rate_star_off.png");
        rate_gold_star = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "rate/rate_star_on.png");
        rate_mail = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "rate/rate_mail.png");

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        info_bg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "game/info_bg.png");
        ad_status = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity, "atlas_video_ad.png", 3, 1);

        try {
            this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.menuTextureAtlas.load();
        } catch (final TextureAtlasBuilderException e) {
            Debug.e(e);
        }
    }

    private void loadMenuFonts() {
        FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        font_pix_kir = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "chat_noir.ttf", 50, true, Color.WHITE, 2, Color.WHITE);
        font_pix_kir.load();

    }

    private void loadMenuAudio() {
        try {
            soundStack.put("arch_v_meshen", SoundFactory.createSoundFromAsset(engine.getSoundManager(), getInstance().activity, "audio/sound/arch_v_meshen.ogg"));
            soundStack.put("arch_vistrel", SoundFactory.createSoundFromAsset(engine.getSoundManager(), getInstance().activity, "audio/sound/arch_vistrel.ogg"));
            soundStack.put("shooting_target_thow", SoundFactory.createSoundFromAsset(engine.getSoundManager(), getInstance().activity, "audio/sound/shooting_target_thow.ogg"));
            soundStack.put("shooting_shoot", SoundFactory.createSoundFromAsset(engine.getSoundManager(), getInstance().activity, "audio/sound/shooting_shoot.ogg"));
            soundStack.put("barier_upal", SoundFactory.createSoundFromAsset(engine.getSoundManager(), getInstance().activity, "audio/sound/barier_upal.ogg"));
            soundStack.put("barier_jump", SoundFactory.createSoundFromAsset(engine.getSoundManager(), getInstance().activity, "audio/sound/barier_jump.ogg"));

            soundStack.put("pike_throw", SoundFactory.createSoundFromAsset(engine.getSoundManager(), getInstance().activity, "audio/sound/pike_throw.ogg"));
            soundStack.put("pike_foll", SoundFactory.createSoundFromAsset(engine.getSoundManager(), getInstance().activity, "audio/sound/pike_down.ogg"));

            soundStack.put("jump_down", SoundFactory.createSoundFromAsset(engine.getSoundManager(), getInstance().activity, "audio/sound/jump_down.ogg"));

            soundStack.put("footstep", SoundFactory.createSoundFromAsset(engine.getSoundManager(), getInstance().activity, "audio/sound/footstep.ogg"));

            soundStack.put("svetofor_nastart", SoundFactory.createSoundFromAsset(engine.getSoundManager(), getInstance().activity, "audio/sound/svetofor_nastart.ogg"));
            soundStack.put("svetofor_start", SoundFactory.createSoundFromAsset(engine.getSoundManager(), getInstance().activity, "audio/sound/svetofor_start.ogg"));
            soundStack.put("false_start", SoundFactory.createSoundFromAsset(engine.getSoundManager(), getInstance().activity, "audio/sound/false_start.ogg"));

            soundStack.put("finish_aplodismenti", SoundFactory.createSoundFromAsset(engine.getSoundManager(), getInstance().activity, "audio/sound/finish_aplodismenti.ogg"));

            soundStack.put("stadion_priv", SoundFactory.createSoundFromAsset(engine.getSoundManager(), getInstance().activity, "audio/sound/stadion_priv.ogg"));

            music = MusicFactory.createMusicFromAsset(engine.getMusicManager(), getInstance().activity, "audio/mm_bg_music.ogg");
            music.setLooping(true);

            music1 = MusicFactory.createMusicFromAsset(engine.getMusicManager(), getInstance().activity, "audio/stadion_shum.ogg");
            music1.setLooping(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadGameGraphics() {
    }

    private void loadGameFonts() {

    }

    private void loadRun100Audio() {

    }

    public void loadSplashScreen() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.NEAREST_PREMULTIPLYALPHA);

        splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "com_logo1.png", 0, 0);
        splashTextureAtlas.load();
        loadFlagImgAtlas();

        // GameSettings.LoadPersonalRecords();
    }

    public void unloadSplashScreen() {
        splashTextureAtlas.unload();
        splash_region = null;
    }

    public void unloadMenuTextures() {
        menuTextureAtlas.unload();
    }

    public void loadMenuTextures() {
        menuTextureAtlas.load();
    }

    public void unloadGameTextures() {

        // TODO (Since we did not create any textures for game scene yet)

    }

    public void unloadAll() {
        music.release();
        flagGraf.clear();
        flagTextureAtlas.unload();

    }

    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom     <br>
     *                 <br>
     *                 We use this method at beginning of game loading, to prepare Resources Manager properly, setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     * @param googleApiClient
     */
    public static void prepareManager(Engine engine, GameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom, GoogleApiClient googleApiClient) {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
        getInstance().googleApiClient= googleApiClient;

    }

    private void loadFlagImgAtlas() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        flagTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
        flagGraf = new HashMap<String, ITextureRegion>();
        String sr;
        try {
            lvlInfParser = activity.getResources().getXml(R.xml.game_status);

            while (lvlInfParser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (lvlInfParser.getEventType() == XmlPullParser.START_TAG && lvlInfParser.getName().equals("f")) {
                    sr = lvlInfParser.getAttributeValue(0);
                    flagGraf.put(sr, BitmapTextureAtlasTextureRegionFactory.createFromAsset(flagTextureAtlas, activity, "flags/" + sr + ".png"));
                }
                lvlInfParser.next();

            }
            flagGraf.put("conn_prob", BitmapTextureAtlasTextureRegionFactory.createFromAsset(flagTextureAtlas, activity, "conn_prob.png"));

        } catch (Throwable t) {
            Toast.makeText(activity, "Error while get data from XML: " + t.toString(), Toast.LENGTH_LONG).show();
        }
        flagGraf.put("region_loading", BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(flagTextureAtlas, activity, "sprites/loading.png", 4, 3));

        try {
            this.flagTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.flagTextureAtlas.load();
        } catch (final TextureAtlasBuilderException e) {
            Debug.e(e);
        }

    }

    // ---------------------------------------------
    // GETTERS AND SETTERS
    // ---------------------------------------------

    public static ResourcesManager getInstance() {
        return INSTANCE;
    }

    // -----------------------------------------------
    public boolean musicIsPlaying(int i) {
        switch (i) {
            case 1:
                return this.music.isPlaying();
            case 2:
                return this.music1.isPlaying();
            default:
                return this.music.isPlaying();

        }

    }

    public void musicResume(int i) {
        if (GameSettings.MUSIC_VAL != 0 && this.music != null && !musicIsPlaying(i)) {
            switch (i) {
                case 1:
                    this.music.resume();
                case 2:
                    this.music1.resume();
                default:
                    this.music.resume();
                    break;
            }

        }
    }

    public void musicPlay(int i) {
        if (GameSettings.MUSIC_VAL != 0) {
            switch (i) {
                case 1:
                    this.music.play();
                    break;
                case 2:
                    this.music1.play();
                    break;

                default:
                    break;
            }

        }
    }

    public void musicPause(int i) {
        if (musicIsPlaying(i)) {
            switch (i) {
                case 1:
                    this.music.pause();
                    break;
                case 2:
                    this.music1.pause();
                    break;

                default:
                    break;
            }

        }
    }

    public void playSoundFromStack(String soundTag) {
        if (GameSettings.SOUND_VAL != 0) {
            ResourcesManager.getInstance().soundStack.get(soundTag).play();
        }
    }



}
