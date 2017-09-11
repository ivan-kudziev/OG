package by.kipind.game.olympicgames.activitys;


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleLayoutGameActivity;
import org.andengine.util.adt.align.HorizontalAlign;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import by.kipind.game.line.CRUDObject;
import by.kipind.game.line.JSONParser;
import by.kipind.game.olympicgames.GameSettings;
import by.kipind.game.olympicgames.R;
import by.kipind.game.olympicgames.ResourcesManager;

import static by.kipind.game.olympicgames.GameSettings.setMusicSound;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class PlayerInfoActivity extends SimpleLayoutGameActivity implements TextWatcher, IOnSceneTouchListener {
    // ===========================================================
    // Constants
    // ===========================================================

    private final int SCENE_WIDTH = 800;
    private final int SCENE_HEIGHT = 450;

    private static final float AUTOWRAP_WIDTH = 720 - 50 - 50;

    // ===========================================================
    // Fields
    // ===========================================================
    final String LOG_TAG = "myLogs";

    private EditText mEditText;

    private Font mFont;

    private InputMethodManager imm;
    private HUD gameHUD;

    private Sprite hudNickFormBG;
    private Sprite hudBtYes;

    private AnimatedSprite exitBtn;
    private AnimatedSprite musicBtn;
    private AnimatedSprite soundBtn;
    private AnimatedSprite loadingAnim;

    private Text PanelLbNameEnter;
    private Text PanelLbExDate;
    private Text PanelLbIDNum;
    private Text edTxt;
    // -----------------
    private JSONParser jParser = new JSONParser();
    // private static String url_get_user =
    // "http://androidgameleaderbords-mrkip.rhcloud.com/get_user.php";
    private static String url_get_user = GameSettings.GAME_ONLINE_SERVER + "/a_get_user.php";
    CRUDObject netActionSendStat = new CRUDObject();

    private static final String TAG_SUCCESS = "success";
    public int spId;

    private String carTime;
    private BoundCamera camera;
    VertexBufferObjectManager vbom = new VertexBufferObjectManager();
    private ResourcesManager resourcesManager;
    private Scene scene;
    // -----------------

    private Context context;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected int getLayoutID() {
        return R.layout.textbreakexample;
    }

    @Override
    protected int getRenderSurfaceViewID() {
        return R.id.textbreakexample_rendersurfaceview;
    }

    @Override
    protected void onSetContentView() {
        super.onSetContentView();

        this.carTime = String.valueOf(System.currentTimeMillis());
        this.mEditText = (EditText) this.findViewById(R.id.textbreakexample_text);
        if (GameSettings.GAME_PLAYER_NICK_ID == "-2" || GameSettings.GAME_PLAYER_NICK_ID == "0" || GameSettings.GAME_PLAYER_NICK == "") {
            this.mEditText.setText("Plr" + carTime.substring(carTime.length() - 4, carTime.length()));
            this.mEditText.setSelection(mEditText.getText().length());
        } else {
            mEditText.setText(GameSettings.GAME_PLAYER_NICK);
            this.mEditText.setInputType(EditorInfo.TYPE_NULL);
        }

        this.mEditText.addTextChangedListener(this);
    }

    @Override
    public EngineOptions onCreateEngineOptions() {

        camera = new BoundCamera(0, 0, SCENE_WIDTH, SCENE_HEIGHT);

        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(SCENE_WIDTH, SCENE_HEIGHT), camera);
    }

    @Override
    public void onCreateResources() {

        resourcesManager = ResourcesManager.getInstance();
        resourcesManager.loadGameResources(GameSettings.ACTIVITY_ID_SETTINGS_INFO, this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        this.mFont = FontFactory.createStrokeFromAsset(this.getFontManager(), mainFontTexture, this.getAssets(), "chat_noir.ttf", 50, true, Color.WHITE, 2, Color.WHITE);
        this.mFont.load();

    }

    @Override
    public Scene onCreateScene() {
        context = GameSettings.getAppContext();
        vbom = this.getVertexBufferObjectManager();

        this.mEngine.registerUpdateHandler(new FPSLogger());

        scene = new Scene();
        // scene.getBackground().setColor(0.09804f, 0.6274f, 0.8784f);

        createBackground(scene);
        createHUD(scene);

        this.updateText();
        scene.setOnSceneTouchListener(this);

        return scene;
    }

    @Override
    public void afterTextChanged(final Editable pEditable) {
        this.updateText();
    }

    private void updateText() {
        final String string = this.mEditText.getText().toString();
        this.edTxt.setText(string);
    }

    @Override
    public void beforeTextChanged(final CharSequence pCharSequence, final int pStart, final int pCount, final int pAfter) {
    /* Nothing. */
    }

    @Override
    public void onTextChanged(final CharSequence pCharSequence, final int pStart, final int pBefore, final int pCount) {
    /* Nothing. */
    }

    private void createBackground(Scene scene) {
        scene.attachChild(new Sprite(SCENE_WIDTH / 2, SCENE_HEIGHT / 2, resourcesManager.gameGraf.get("activity_background_region"), vbom));
    }

    private void createHUD(Scene scene) {
        gameHUD = new HUD();

        exitBtn = new AnimatedSprite(0, 0, (ITiledTextureRegion) resourcesManager.gameGraf.get("bt_exit_region"), vbom) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    return SceneObjectTouch(this);
                }
                return false;
            }
        };
        exitBtn.setPosition(SCENE_WIDTH - exitBtn.getWidth(), SCENE_HEIGHT - exitBtn.getHeight());

        musicBtn = new AnimatedSprite(0, 0, (ITiledTextureRegion) resourcesManager.gameGraf.get("ms_btn_region"), vbom) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    return SceneObjectTouch(this);
                }
                return false;
            }
        };
        musicBtn.setPosition(musicBtn.getWidth(), SCENE_HEIGHT - musicBtn.getHeight());
        musicBtn.setCurrentTileIndex(GameSettings.MUSIC_VAL);

        soundBtn = new AnimatedSprite(0, 0, (ITiledTextureRegion) resourcesManager.gameGraf.get("ms_btn_region"), vbom) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    return SceneObjectTouch(this);
                }
                return false;
            }
        };
        soundBtn.setPosition(soundBtn.getWidth(), SCENE_HEIGHT - soundBtn.getHeight() * 2f);
        soundBtn.setCurrentTileIndex(GameSettings.SOUND_VAL + 2);

        loadingAnim = new AnimatedSprite(SCENE_WIDTH / 2, SCENE_HEIGHT / 2, (ITiledTextureRegion) resourcesManager.gameGraf.get("loading_region"), vbom);
        loadingAnim.animate(300);
        loadingAnim.setVisible(false);

        hudNickFormBG = new Sprite(SCENE_WIDTH / 2f, SCENE_HEIGHT / 1.5f, resourcesManager.gameGraf.get("nick_form_background_region"), vbom) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    return SceneObjectTouch(this);
                }
                return false;
            }
        };
        // hudNickFormBG.setScale(2f, 2f);

        hudBtYes = new AnimatedSprite(0, 0, (ITiledTextureRegion) resourcesManager.gameGraf.get("bt_ok_region"), vbom) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    return SceneObjectTouch(this);
                }
                return false;
            }
        };

        hudBtYes.setPosition(SCENE_WIDTH - hudBtYes.getWidth(), SCENE_HEIGHT - hudBtYes.getHeight() * 2.5f);

        // CREATE TEXT info
        PanelLbNameEnter = new Text(0, 0, this.mFont, context.getString(R.string.mmf_nick_label1), new TextOptions(HorizontalAlign.LEFT), vbom);
        PanelLbNameEnter.setColor(Color.BLACK);
        PanelLbNameEnter.setScale(0.6f);
        PanelLbNameEnter.setPosition(hudNickFormBG.getX() + PanelLbNameEnter.getWidth() * PanelLbNameEnter.getScaleX() * 0.1f, hudNickFormBG.getY() + PanelLbNameEnter.getHeight()
                * 0.9f);

        PanelLbExDate = new Text(0, 0, this.mFont, context.getString(R.string.mmf_nick_label_exd), new TextOptions(HorizontalAlign.LEFT), vbom);
        PanelLbExDate.setColor(Color.BLACK);
        PanelLbExDate.setScale(0.35f);
        PanelLbExDate.setPosition(hudNickFormBG.getX() + hudNickFormBG.getWidth() / 2f - PanelLbExDate.getWidth() * PanelLbExDate.getScaleX() * 0.8f, hudNickFormBG.getY()
                - PanelLbExDate.getHeight() * 0.4f);

        PanelLbIDNum = new Text(0, 0, this.mFont, context.getString(R.string.mmf_nick_label_idn), new TextOptions(HorizontalAlign.CENTER), vbom);
        PanelLbIDNum.setColor(Color.BLACK);
        PanelLbIDNum.setScale(0.35f);
        PanelLbIDNum.setPosition(hudNickFormBG.getX() - PanelLbIDNum.getWidth() * PanelLbIDNum.getScaleX() * 0.2f, hudNickFormBG.getY() - PanelLbIDNum.getHeight() * 0.4f);

        edTxt = new Text(0, 0, this.mFont, context.getString(R.string.mmf_nick_label1), new TextOptions(HorizontalAlign.CENTER), vbom);

        if (GameSettings.GAME_PLAYER_NICK_ID == "-2" || GameSettings.GAME_PLAYER_NICK_ID == "0" || GameSettings.GAME_PLAYER_NICK == "") {
            PanelLbNameEnter.setText(context.getString(R.string.mmf_nick_label1));
            gameHUD.registerTouchArea(hudNickFormBG);
            gameHUD.registerTouchArea(hudBtYes);
            gameHUD.attachChild(hudBtYes);
        } else {
            PanelLbNameEnter.setText(context.getString(R.string.mmf_nick_label2));

        }

        edTxt.setScale(0.5f);
        edTxt.setPosition(hudNickFormBG.getX(), hudNickFormBG.getY() + edTxt.getHeight() * edTxt.getScaleX() * 0.1f);
        edTxt.setColor(Color.BLUE);
        // --------------

        gameHUD.registerTouchArea(exitBtn);
        gameHUD.registerTouchArea(musicBtn);
        gameHUD.registerTouchArea(soundBtn);
        gameHUD.setTouchAreaBindingOnActionDownEnabled(true);

        gameHUD.attachChild(hudNickFormBG);
        gameHUD.attachChild(PanelLbNameEnter);
      //  gameHUD.attachChild(PanelLbExDate);
      //  gameHUD.attachChild(PanelLbIDNum);
        gameHUD.attachChild(edTxt);
        gameHUD.attachChild(exitBtn);
        gameHUD.attachChild(musicBtn);
        gameHUD.attachChild(soundBtn);
        gameHUD.attachChild(loadingAnim);

        camera.setHUD(gameHUD);
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        // TODO Auto-generated method stub
        return false;
    }

    private boolean SceneObjectTouch(Object touchedObj) {
        boolean res = false;

        if (touchedObj.equals(hudBtYes)) {
            showKeyBoard();
            new CheckNick().execute((String) edTxt.getText(), context.getString(R.string.net_game_lb_total_id));
            res = true;
        } else if (touchedObj.equals(exitBtn)) {
            exitBtn.setCurrentTileIndex(1);
            onBack();
        } else if (touchedObj.equals(musicBtn)) {
            musicBtn.setCurrentTileIndex(musicBtn.getCurrentTileIndex() ^ 1);
           // GameSettings.MUSIC_VAL = GameSettings.MUSIC_VAL ^ 1;
            setMusicSound(GameSettings.MUSIC_VAL ^ 1,GameSettings.SOUND_VAL);
            if (GameSettings.MUSIC_VAL == 0) {
                ResourcesManager.getInstance().musicPause(1);
            } else {
                ResourcesManager.getInstance().musicPlay(1);
            }
        } else if (touchedObj.equals(soundBtn)) {
            if (soundBtn.getCurrentTileIndex() == 2) {
                soundBtn.setCurrentTileIndex(3);
            } else {
                soundBtn.setCurrentTileIndex(2);
            }
            //GameSettings.SOUND_VAL = GameSettings.SOUND_VAL ^ 1;
            setMusicSound(GameSettings.MUSIC_VAL ,GameSettings.SOUND_VAL^ 1);
        } else if (touchedObj.equals(hudNickFormBG)) {
            mEditText.requestFocus();
            showKeyBoard();
        }

        return res;
    }

    public void onBack() {
        resourcesManager.unloadGameTextures();
        this.finish();
    }

    public void fixName(int taskRes) {

        if (taskRes > 0) {
            // TODO: save in lan
            GameSettings.UpdateUser((String) edTxt.getText(), String.valueOf(taskRes), "1");
            Toast.makeText(context, (CharSequence) context.getString(R.string.nick_saved), Toast.LENGTH_LONG).show();
            hudBtYes.setVisible(false);
        } else {
            switch (taskRes) {
                case 0:
                    Toast.makeText(context, (CharSequence) context.getString(R.string.connection_fail) + ". " + context.getString(R.string.local_save_nick), Toast.LENGTH_LONG).show();
                    GameSettings.UpdateUser((String) edTxt.getText(), "0", "-1");
                    break;
                case -1:
                    Toast.makeText(context, (CharSequence) context.getString(R.string.nick_exist), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(context, (CharSequence) context.getString(R.string.connection_fail) + ". " + context.getString(R.string.local_save_nick), Toast.LENGTH_LONG).show();
                    GameSettings.UpdateUser((String) edTxt.getText(), "0", "0");
                    break;
            }
        }
    }

    class CheckNick extends AsyncTask<String, String, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingAnim.setVisible(true);

        }

        protected Integer doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nick", args[0]));
            params.add(new BasicNameValuePair("game", args[1]));

            try {

                JSONObject json = jParser.makeHttpRequest(url_get_user, "GET", params);


                if (!json.equals(null)) {
                    if (json.getInt(TAG_SUCCESS) > 0) {
                        return json.getInt("nameID");
                    } else {
                        return json.getInt(TAG_SUCCESS);
                    }
                }
            } catch (Exception e) {
                return 0;
            }
            return 0;
        }

        protected void onPostExecute(Integer taskRes) {
            loadingAnim.setVisible(false);
            fixName(taskRes);

        }

    }

    protected void showKeyBoard() {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }

}