package by.kipind.game.olympicgames.sceneElements;

import android.content.Context;
import android.graphics.PointF;
import android.os.AsyncTask;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import by.kipind.game.UtilObjects.LeaderboardItem;
import by.kipind.game.line.JSONParser;
import by.kipind.game.olympicgames.GameSettings;
import by.kipind.game.olympicgames.R;
import by.kipind.game.olympicgames.ResourcesManager;

public class PersonalRecordTable extends HUD {

    private static String url_user_records = GameSettings.GAME_ONLINE_SERVER + "/a_get_user_records.php";

    private static final String ATTRIBUTE_NAME_SUCCESS = "success";
    private static final String ATTRIBUTE_NAME_RECORDS_TAG = "records";
    private static final String ATTRIBUTE_NAME_GID = "game_id";
    private static final String ATTRIBUTE_NAME_NICK = "name";
    private static final String ATTRIBUTE_NAME_RECORD_VL = "rv";
    private static final String ATTRIBUTE_NAME_PLACE = "place";

    // ===========================================================
    // Fields
    // ===========================================================

    private List<Sprite> lGIcon;
    private List<Text> lText;
    private List<Text> lPlace;
    private Text txtTablTitle;
    private Text txtPlName;
    private AnimatedSprite loadProc;
    ArrayList<LeaderboardItem> recordsList;

    private final Context context = GameSettings.getAppContext();
    protected final ResourcesManager resourcesManager = ResourcesManager.getInstance();
    protected VertexBufferObjectManager vbo;
    private final TextOptions tOpt = new TextOptions(HorizontalAlign.LEFT);

    private PointF startXY;
    private String plName;
    private String plNameID;

    // ===========================================================
    // Constructors
    // ===========================================================
    public PersonalRecordTable(final String plName, final String plNameID, final int turnirID, final float pX, final float pY, final float iW, final float iH,
	    final Camera pCamera,
	    VertexBufferObjectManager vbo) {
	this.setCamera(pCamera);

	this.vbo = vbo;
	this.startXY = new PointF(pX - iW / 2 + 10, pY + iH / 2 - 10);

	if (plName.equals("-1")) {
	    this.plName = context.getString(R.string.ge_personal_record_noinfo);
	    this.plNameID = plNameID;
	} else {
	    this.plName = plName;
	    this.plNameID = plNameID;

	}

	String recTbTitle = setStrLen(context.getString(R.string.ge_personal_record_game), 5, "  ") + setStrLen(context.getString(R.string.ge_personal_record_rank), 7, "  ")
		+ setStrLen(context.getString(R.string.ge_personal_record_record), 7, "  ");

	txtTablTitle = new Text(0, 0, resourcesManager.font_pix_kir, recTbTitle, tOpt, vbo);// "<><><><><><><><><><><><>"
	txtTablTitle.setColor(Color.CYAN);
	txtTablTitle.setScale(0.4f);
	txtTablTitle.setAnchorCenter(0, 0);

	txtPlName = new Text(0, 0, resourcesManager.font_pix_kir, "try this soft french bread with ligt oil", tOpt, vbo);// "<><><><><><><><><><><><>"
	txtPlName.setScale(0.4f);
	txtPlName.setAnchorCenter(0, 0);

	txtPlName.setPosition(startXY.x, startXY.y);// - txtPlName.getHeight() * 0.4f
	txtPlName.setColor(1, 1, 0);
	// txtPlName.setText(GameSettings.GAME_PLAYER_NICK + "(" + context.getString(R.string.ge_personal_record_wr) + ")");
	// txtPlName.setText(GameSettings.GAME_PLAYER_NICK + "(" + context.getString(R.string.ge_personal_record_pr) + ")");
	txtPlName.setText(GameSettings.GAME_PLAYER_NICK);

	this.attachChild(txtPlName);

	loadProc = new AnimatedSprite(pX, pY, (ITiledTextureRegion) ResourcesManager.getInstance().flagGraf.get("region_loading"), vbo);
	loadProc.setScale(0.5f);
	loadProc.animate(300);
	loadProc.setVisible(false);
	this.attachChild(loadProc);

	new MyTask().execute(context.getString(R.string.net_game_lb_total_id), String.valueOf(this.plName), plNameID, String.valueOf(GameSettings.WEEK_OF_YEAR));// turnirID

    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void fillRT() {

	if (!recordsList.isEmpty()) {

	    int i = 0;
	    float flagW;
	    float[] txColorRGB = { 1, 1, 0 };
	    String recVal;
	    lGIcon.add(new Sprite(startXY.x, startXY.y, resourcesManager.ico_run, vbo));
	    lPlace.add(new Text(startXY.x, startXY.y, resourcesManager.font_pix_kir, "-1", vbo));
	    lText.add(new Text(startXY.x, startXY.y, resourcesManager.font_pix_kir, setStrLen(recordsList.get(0).getNick(), 11, "  "), vbo));

	    PointF posXY = new PointF(startXY.x, startXY.y - txtPlName.getHeight() * txtPlName.getScaleY());
	    txtTablTitle.setPosition(posXY.x, posXY.y);
	    this.attachChild(txtTablTitle);
	    posXY.set(posXY.x, posXY.y - txtTablTitle.getHeight() * 0.5f);

	    i++;
	    flagW = lGIcon.get(i - 1).getWidth() / 2;
	    txColorRGB[0] = 0.9f;
	    txColorRGB[1] = 0.99f;
	    txColorRGB[2] = 1f;

	    for (LeaderboardItem rec : recordsList) {

		if (rec.getLeadPlase() > 9999 || rec.getLeadPlase() < 1) {
		    recVal = "----";
		} else {
		    recVal = String.valueOf(rec.getLeadPlase());
		}
		switch (rec.getUid() - GameSettings.GAME_CODE * 10) {
		case GameSettings.ACTIVITY_ID_RUN100:
		    lGIcon.add(new Sprite(0, 0, resourcesManager.ico_run, vbo));
		    break;
		case GameSettings.ACTIVITY_ID_LONG_JUMP:
		    lGIcon.add(new Sprite(0, 0, resourcesManager.ico_long_jump, vbo));
		    break;
		case GameSettings.ACTIVITY_ID_PIKE_THROW:
		    lGIcon.add(new Sprite(0, 0, resourcesManager.ico_pike, vbo));
		    break;
		case GameSettings.ACTIVITY_ID_RUN_BARIER:
		    lGIcon.add(new Sprite(0, 0, resourcesManager.ico_barier, vbo));
		    break;
		case GameSettings.ACTIVITY_ID_SHOOTING:
		    lGIcon.add(new Sprite(0, 0, resourcesManager.ico_shoot, vbo));
		    break;
		case GameSettings.ACTIVITY_ID_ARCHERY:
		    lGIcon.add(new Sprite(0, 0, resourcesManager.ico_arch, vbo));
		    break;
		default:
		    lGIcon.add(new Sprite(0, 0, resourcesManager.flagGraf.get("flag_wr"), vbo));
		    break;
		}
		lGIcon.get(i).setScale(0.6f);
		lGIcon.get(i).setAnchorCenter(0, 0);
		lGIcon.get(i).setPosition(posXY.x + flagW * 0.5f, posXY.y);

		lPlace.add(new Text(0, 0, resourcesManager.font_pix_kir, setStrLen(recVal, 5, "   "), tOpt, vbo));
		lPlace.get(i).setScale(0.35f);
		lPlace.get(i).setAnchorCenter(0, 0);
		lPlace.get(i).setPosition(lGIcon.get(i).getX() + 3 * flagW + 1, posXY.y + 2);
		lPlace.get(i).setColor(0.8f, 0.99f, 0.8f);

		lText.add(new Text(0, 0, resourcesManager.font_pix_kir, String.valueOf(rec.getProgVal()), tOpt, vbo));
		lText.get(i).setScale(0.33f);
		lText.get(i).setAnchorCenter(0, 0);
		lText.get(i).setPosition(lGIcon.get(i).getX() + 3 * flagW + lPlace.get(i).getWidth() * 0.35f + 2, posXY.y + 2);
		lText.get(i).setColor(txColorRGB[0], txColorRGB[1], txColorRGB[2]);// 0.97f,
										   // 0.97f,
										   // 0.01f

		this.attachChild(lPlace.get(i));
		this.attachChild(lGIcon.get(i));
		this.attachChild(lText.get(i));

		posXY.set(startXY.x, lGIcon.get(i).getY() - lGIcon.get(i).getHeight() * 0.55f);
		i++;
	    }

	}

    }

    private String setStrLen(String str, int len, String conc) {
	if (len <= str.length()) {
	    str = str.substring(0, len);
	} else {
	    for (int i = str.length(); i <= len; i++) {
		str += conc;
	    }
	}
	return str;
    }

    class MyTask extends AsyncTask<String, Integer, String> {

	@Override
	protected void onPreExecute() {
	    super.onPreExecute();
	    loadProc.setVisible(true);
	    if (lGIcon == null) {
		lGIcon = new ArrayList<Sprite>();
	    } else {
		lGIcon.clear();
	    }
	    if (lText == null) {
		lText = new ArrayList<Text>();
	    } else {
		lText.clear();
	    }
	    if (lPlace == null) {
		lPlace = new ArrayList<Text>();
	    } else {
		lPlace.clear();
	    }
	    if (recordsList == null) {
		recordsList = new ArrayList<LeaderboardItem>();
	    } else {
		recordsList.clear();
	    }
	}

	@Override
	protected String doInBackground(String... args) {
	    // Log.d("myLogs", "prt_param_bg---->" + args[0] + " " + args[1] + " " + args[2] + " " + args[3]);

	    if (loadFromNetDB(args[0], args[1], args[2], args[3]) != "ok") {
		txtPlName.setText(">>" + context.getString(R.string.ge_personal_record_pr) + "<<");
		return loadFromLocalDB();
	    } else {
		txtPlName.setText(">>" + context.getString(R.string.ge_personal_record_wr) + "<<");

		return "ok";
	    }
	}

	@Override
	protected void onPostExecute(String result) {
	    super.onPostExecute(result);
	    if (result != null && result == "ok") {
		fillRT();
	    }

	    loadProc.setVisible(false);
	}

	private String loadFromLocalDB() {
	    LeaderboardItem item;

	    for (int i = 1; i <= 6; i++) {
		item = new LeaderboardItem();

		item.setUid(i + GameSettings.GAME_CODE * 10);
		item.setLeadPlase(-1);
		item.setNick("");

		switch (i) {
		case GameSettings.ACTIVITY_ID_RUN100:
		    item.setProgVal(GameSettings.RECORD_RUN100);
		    break;
		case GameSettings.ACTIVITY_ID_RUN_BARIER:
		    item.setProgVal(GameSettings.RECORD_RUN_BARIER);
		    break;
		case GameSettings.ACTIVITY_ID_LONG_JUMP:
		    item.setProgVal(GameSettings.RECORD_LONG_JUMP);
		    break;
		case GameSettings.ACTIVITY_ID_PIKE_THROW:
		    item.setProgVal(GameSettings.RECORD_PIKE_THROW);
		    break;
		case GameSettings.ACTIVITY_ID_SHOOTING:
		    item.setProgVal(GameSettings.RECORD_SHOOTING);
		    break;
		case GameSettings.ACTIVITY_ID_ARCHERY:
		    item.setProgVal(GameSettings.RECORD_ARCHERY);
		    break;

		default:
		    item.setProgVal(-1f);
		    break;
		}

		item.setCountry("wr");

		if (item.getProgVal() != -1) {
		    recordsList.add(item);
		}
	    }

	    if (!recordsList.isEmpty()) {
		return "ok";
	    } else {
		return "empty";
	    }

	}

	private String loadFromNetDB(String serverGameLBID, String plName, String plNameID, String godKv) {
	    JSONParser jParser = new JSONParser();
	    JSONArray records = null;
	    // Integer listResID;

	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("name", plName));
	    params.add(new BasicNameValuePair("game", serverGameLBID));
	    params.add(new BasicNameValuePair("name_id", plNameID));
	    params.add(new BasicNameValuePair("god_kv", godKv));

	    try {
		// getting JSON string from URL
		JSONObject json = jParser.makeHttpRequest(url_user_records, "GET", params);

		// Check your log cat for JSON reponse
		// Log.d("All Products: ", json.toString());
		if (json.equals(null)) {
		} else
		    try {
			// Checking for SUCCESS TAG
			int success = json.getInt(ATTRIBUTE_NAME_SUCCESS);
			// Log.d("myLogs", "prt---->" + success);

			if (success == 1) {
			    // game id found
			    // Getting Array of game records
			    records = json.getJSONArray(ATTRIBUTE_NAME_RECORDS_TAG);
			    LeaderboardItem item;
			    // looping through All Products
			    for (int i = 0; i < records.length(); i++) {
				JSONObject c = records.getJSONObject(i);

				item = new LeaderboardItem();

				item.setUid(Integer.valueOf(c.getString(ATTRIBUTE_NAME_GID)));
				item.setLeadPlase(Integer.valueOf(c.getString(ATTRIBUTE_NAME_PLACE)));
				item.setNick(c.getString(ATTRIBUTE_NAME_NICK));
				item.setProgVal(Math.abs(Float.valueOf(c.getString(ATTRIBUTE_NAME_RECORD_VL))));
				item.setCountry("wr");

				recordsList.add(item);

			    }
			    return "ok";
			} else if (success == 2) {
			    return "empty";
			} else {
			    return null;
			}
		    } catch (JSONException e) {
			return null;
		    }
	    } catch (Exception e) {
		return null;
	    }
	    return null;
	}

    }

    // ---public

    public void reload() {
	if (!loadProc.isVisible()) {
	    GameSettings.onSync();

	    this.detachChildren();
	    this.attachChild(loadProc);
	    txtPlName.setText(GameSettings.GAME_PLAYER_NICK);
	    this.attachChild(txtPlName);

	    // new MyTask().execute(context.getString(R.string.net_game_lb_total_id), String.valueOf(GameSettings.GAME_PLAYER_NICK));
	    new MyTask().execute(context.getString(R.string.net_game_lb_total_id), GameSettings.GAME_PLAYER_NICK, GameSettings.GAME_PLAYER_NICK_ID,
		    String.valueOf(GameSettings.WEEK_OF_YEAR));

	}
    }

    // ===========================================================
    // Getters & Setters
    // ===========================================================

}