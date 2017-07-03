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
import by.kipind.game.olympicgames.ResourcesManager;

public class ShortRecordsTable extends HUD {
    // ===========================================================
    // Constants
    // ===========================================================

    private final float RUNNER_FPS = 60f;
    public static float RUNNER_RED_WIGHT;

    private static String url_all_records = GameSettings.GAME_ONLINE_SERVER + "/a_get_game_top_rec.php";

    private static final String ATTRIBUTE_NAME_SUCCESS = "success";
    private static final String ATTRIBUTE_NAME_RECORDS_TAG = "records";
    private static final String ATTRIBUTE_NAME_UID = "name_id";
    private static final String ATTRIBUTE_NAME_NAME = "name";
    private static final String ATTRIBUTE_NAME_COUNTRY = "country";
    private static final String ATTRIBUTE_NAME_RECORD_VL = "recordvalue";
    private static final String ATTRIBUTE_NAME_DATE = "created_at";
    private static final String ATTRIBUTE_NAME_PLACE = "place";

    // ===========================================================
    // Fields
    // ===========================================================

    private List<Sprite> lFlag;
    private List<Text> lText;
    private List<Text> lPlace;
    private Text lDeLine;
    private AnimatedSprite loadProc;
    ArrayList<LeaderboardItem> recordsList;

    private final Context context = GameSettings.getAppContext();
    protected final ResourcesManager resourcesManager = ResourcesManager.getInstance();
    protected VertexBufferObjectManager vbo;
    private final TextOptions tOpt = new TextOptions(HorizontalAlign.LEFT);

    private PointF startXY;
    private int recKol;
    private String plNameId;
    private int yearKW;
    private String gameID;
    private boolean reloadFlag;

    // ===========================================================
    // Constructors
    // ===========================================================
    public ShortRecordsTable(String gameID, final int yearKW, final String plName, final float pX, final float pY, final float iW, final float iH, int iRecKol,
	    final Camera pCamera,
	    VertexBufferObjectManager vbo) {
	this.setCamera(pCamera);

	this.vbo = vbo;
	lDeLine = new Text(0, 0, resourcesManager.font_pix_kir, ".  .  .  .  .  .  .  .  .  .  .  .  . ", tOpt, vbo);// "<><><><><><><><><><><><>"
	lDeLine.setColor(Color.CYAN);
	lDeLine.setScale(0.35f);
	lDeLine.setAnchorCenter(0, 0);

	this.startXY = new PointF(pX - iW / 2, pY - 8 + (iH - lDeLine.getHeight()) / 2);
	this.recKol = iRecKol;
	this.plNameId = plName;
	this.gameID = gameID;
	this.yearKW = yearKW;
	loadProc = new AnimatedSprite(pX, pY, (ITiledTextureRegion) ResourcesManager.getInstance().flagGraf.get("region_loading"), vbo);
	loadProc.setScale(0.5f);
	loadProc.animate(300);
	loadProc.setVisible(false);

	this.attachChild(loadProc);

	new MyTask().execute(this.gameID, String.valueOf(this.plNameId), String.valueOf(this.recKol), String.valueOf(this.yearKW));

    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void fillRT() {
	int i = 0;
	float[] txColorRGB = { 1, 1, 1 };

	if (!recordsList.isEmpty()) {
	    int plLen = 2;
	    PointF posXY = new PointF(startXY.x, startXY.y);
	    float flagW;

	    lFlag.add(new Sprite(startXY.x, startXY.y, resourcesManager.flagGraf.get("flag_wr"), vbo));
	    lText.add(new Text(startXY.x, startXY.y, resourcesManager.font_pix_kir, "-1", vbo));
	    lPlace.add(new Text(startXY.x, startXY.y, resourcesManager.font_pix_kir, "-1", vbo));
	    i++;
	    flagW = lFlag.get(i - 1).getWidth() / 2;

	    for (LeaderboardItem rec : recordsList) {
		if (i == recordsList.size() && plNameId.equals(String.valueOf(rec.getUid())) && recordsList.size() <= recKol) {// i == recordsList.size() && plName.equals(rec.getNick()) && recordsList.size() < recKol
		    break;
		}
		if (i <= recKol) {
		    rec.setLeadPlase(i);

		    if (i > 3) {
			txColorRGB[0] = 0.8f;
			txColorRGB[1] = 0.99f;
			txColorRGB[2] = 0.8f;
		    } else if (i == 1) {
			txColorRGB[0] = 1;
			txColorRGB[1] = 1;
			txColorRGB[2] = 0.19f;
		    } else if (i == 2) {
			txColorRGB[0] = 0.72f;
			txColorRGB[1] = 0.8f;
			txColorRGB[2] = 0.86f;
		    } else if (i == 3) {
			txColorRGB[0] = 0.95f;
			txColorRGB[1] = 0.55f;
			txColorRGB[2] = 0.16f;
		    }
		} else {
		    txColorRGB[0] = 0.9f;
		    txColorRGB[1] = 0.99f;
		    txColorRGB[2] = 1f;
		    lDeLine.setPosition(posXY.x, posXY.y);
		    this.attachChild(lDeLine);
		    posXY.set(startXY.x, lFlag.get(i - 1).getY() - lFlag.get(i - 1).getHeight() - 2);
		    plLen = String.valueOf(rec.getLeadPlase()).length();

		}

		lPlace.add(new Text(0, 0, resourcesManager.font_pix_kir, setStrLen(String.valueOf(rec.getLeadPlase()), plLen, "  "), tOpt, vbo));
		lPlace.get(i).setScale(0.35f);
		lPlace.get(i).setAnchorCenter(0, 0);
		lPlace.get(i).setPosition(posXY.x, posXY.y + 2);
		lPlace.get(i).setColor(txColorRGB[0], txColorRGB[1], txColorRGB[2]);

		lFlag.add(new Sprite(0, 0, resourcesManager.flagGraf.get("flag_" + rec.getCountry()), vbo));
		lFlag.get(i).setScale(0.5f);
		lFlag.get(i).setAnchorCenter(0, 0);
		lFlag.get(i).setPosition(posXY.x + lPlace.get(i).getWidth() * 0.35f + 1, posXY.y);

		lText.add(new Text(0, 0, resourcesManager.font_pix_kir, setStrLen(rec.getNick(), 11, "  ") + "" + String.valueOf(rec.getProgVal()), tOpt, vbo));
		lText.get(i).setScale(0.33f);
		lText.get(i).setAnchorCenter(0, 0);
		lText.get(i).setPosition(lFlag.get(i).getX() + flagW + 1, posXY.y + 2);
		lText.get(i).setColor(txColorRGB[0], txColorRGB[1], txColorRGB[2]);// 0.97f,
										   // 0.97f,
										   // 0.01f

		this.attachChild(lPlace.get(i));
		this.attachChild(lFlag.get(i));
		this.attachChild(lText.get(i));

		posXY.set(startXY.x, lFlag.get(i).getY() - lFlag.get(i).getHeight() * 0.45f);
		i++;
	    }

	} else {
	    i = 0;
	    lFlag.add(new Sprite(0, 0, resourcesManager.flagGraf.get("conn_prob"), vbo));
	    lFlag.get(i).setScale(1.5f);
	    lFlag.get(i).setPosition(loadProc);

	    this.attachChild(lFlag.get(i));

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

    class MyTask extends AsyncTask<String, Integer, Void> {

	@Override
	protected void onPreExecute() {
	    super.onPreExecute();
	    loadProc.setVisible(true);
	    if (lFlag == null) {
		lFlag = new ArrayList<Sprite>();
	    } else {
		lFlag.clear();
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
	protected Void doInBackground(String... args) {
	    loadLb(args[0], args[1], args[2], args[3]);
	    return null;
	}

	@Override
	protected void onPostExecute(Void result) {
	    super.onPostExecute(result);

	    fillRT();
	    loadProc.setVisible(false);

	}

	@SuppressWarnings("unused")
	private String loadLb(String serverGameLBID, String plName, String topLen, String kw) {
	    JSONParser jParser = new JSONParser();
	    JSONArray records = null;
	    // Integer listResID;

	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("game", serverGameLBID));
	    params.add(new BasicNameValuePair("pl_name_id", plName));
	    params.add(new BasicNameValuePair("god_kv", kw));
	    params.add(new BasicNameValuePair("top_kol", topLen));
	    params.add(new BasicNameValuePair("order", "desc"));

	    try {
		// getting JSON string from URL
		JSONObject json = jParser.makeHttpRequest(url_all_records, "GET", params);

		// Check your log cat for JSON reponse
		// Log.d("All Products: ", json.toString());
		if (json == null) {
		} else
		    try {
			// Checking for SUCCESS TAG
			int success = json.getInt(ATTRIBUTE_NAME_SUCCESS);

			if (success == 1) {
			    // game id found
			    // Getting Array of game records
			    records = json.getJSONArray(ATTRIBUTE_NAME_RECORDS_TAG);
			    LeaderboardItem item;
			    // looping through All Products
			    for (int i = 0; i < records.length(); i++) {
				JSONObject c = records.getJSONObject(i);

				item = new LeaderboardItem();

				item.setUid(Integer.valueOf(c.getString(ATTRIBUTE_NAME_UID)));
				item.setLeadPlase(Integer.valueOf(c.getString(ATTRIBUTE_NAME_PLACE)));
				item.setCountry(c.getString(ATTRIBUTE_NAME_COUNTRY).toLowerCase());
				item.setNick(c.getString(ATTRIBUTE_NAME_NAME));

				item.setProgVal(Math.abs(Float.valueOf(c.getString(ATTRIBUTE_NAME_RECORD_VL))));

				item.setProgUpDate(c.getString(ATTRIBUTE_NAME_DATE));

				recordsList.add(item);
			    }

			} else {
			    return null;
			}

			return "ok";

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

	    // new MyTask().execute(this.gameID, String.valueOf(this.plNameId), String.valueOf(this.recKol));
	    new MyTask().execute(this.gameID, String.valueOf(this.plNameId), String.valueOf(this.recKol), String.valueOf(this.yearKW));
	}
    }

    // ===========================================================
    // Getters & Setters
    // ===========================================================

}
