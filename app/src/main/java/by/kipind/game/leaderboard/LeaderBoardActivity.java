package by.kipind.game.leaderboard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import by.kipind.game.line.JSONParser;
import by.kipind.game.olympicgames.GameSettings;
import by.kipind.game.olympicgames.R;

public class LeaderBoardActivity extends Activity implements OnClickListener, OnFocusChangeListener {
	private static String url_all_records = GameSettings.GAME_ONLINE_SERVER + "/a_get_all_products.php";

	private static final String ATTRIBUTE_NAME_SUCCESS = "success";
	private static final String ATTRIBUTE_NAME_RECORDS_TAG = "records";
	private static final String ATTRIBUTE_NAME_UID = "id";
	private static final String ATTRIBUTE_NAME_NAME = "name";
	// private static final String ATTRIBUTE_NAME_GAME = "game_id";
	private static final String ATTRIBUTE_NAME_COUNTRY = "country";
	private static final String ATTRIBUTE_NAME_RECORD_VL = "recordvalue";
	private static final String ATTRIBUTE_NAME_DATE = "created_at";

	private ProgressDialog pDialog;
	private boolean outOfAppFlag = true;

	private String paramGodKw;
	// Sound MTrack;
	// SharedPreferences sPref;
	private InputMethodManager imm;

	Button lNikPointInfo;
	Button lFilterSet;
	Button lFilterSetCW;
	EditText tvGod;
	EditText tvKw;

	List<Button> gameType = new ArrayList<Button>();

	public String tg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leaderboard_main);

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		pDialog = new ProgressDialog(LeaderBoardActivity.this);
		pDialog.setMessage(getString(R.string.connection));
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);

		// MTrack = Sound.getInstansSound(this, true);

		String dataBaseGameLBId = getString(R.string.net_game_lb_total_id);
		ImageView btn_exit = (ImageView) findViewById(R.id.mlb_btnBack);
		btn_exit.setOnClickListener(this);

		tvGod = (EditText) findViewById(R.id.mlb_etGod);
		tvGod.setText(String.valueOf(GameSettings.WEEK_OF_YEAR).substring(0, 4));
		tvGod.setOnFocusChangeListener(this);
		tvKw = (EditText) findViewById(R.id.mlb_etKw);
		tvKw.setText(String.valueOf(GameSettings.WEEK_OF_YEAR).substring(4, 6));
		tvKw.setOnFocusChangeListener(this);
		paramGodKw = String.valueOf(GameSettings.WEEK_OF_YEAR);


		lNikPointInfo = (Button) findViewById(R.id.mlb_l_nick_point);
		lFilterSet = (Button) findViewById(R.id.mlb_set_current_date);
		lFilterSet.setOnClickListener(this);
		lFilterSetCW = (Button) findViewById(R.id.mlb_set_current_week);
		lFilterSetCW.setOnClickListener(this);

		gameType.add((Button) findViewById(R.id.mlb_game_type_run100));
		gameType.add((Button) findViewById(R.id.mlb_game_type_barier));
		gameType.add((Button) findViewById(R.id.mlb_game_type_jump));
		gameType.add((Button) findViewById(R.id.mlb_game_type_pike));
		gameType.add((Button) findViewById(R.id.mlb_game_type_arch));
		gameType.add((Button) findViewById(R.id.mlb_game_type_shoot));
		for (Button bt : gameType) {
			bt.setOnClickListener(this);
		}
		tg = "41";

		loadRecords(dataBaseGameLBId);
	}

	private void loadRecords(String gameID) {
		tvGod.setText(paramGodKw.substring(0, 4));
		tvKw.setText(paramGodKw.substring(4, 6));
		pDialog.show();

		String nm;
		if (GameSettings.GAME_PLAYER_NICK_ID != "0" && GameSettings.WEEK_OF_YEAR == Integer.valueOf(paramGodKw)) {
			switch (Integer.valueOf(gameID)) {
				case 41:
					if (GameSettings.RECORD_RUN100 > 0) {
						nm = GameSettings.GAME_PLAYER_NICK + ": " + GameSettings.RECORD_RUN100 + getString(R.string.game_hud_ed_secs);
					} else {
						nm = GameSettings.GAME_PLAYER_NICK + ": -- " + getString(R.string.game_hud_ed_secs);
					}
					break;
				case 42:
					if (GameSettings.RECORD_RUN_BARIER > 0) {
						nm = GameSettings.GAME_PLAYER_NICK + ": " + GameSettings.RECORD_RUN_BARIER + getString(R.string.game_hud_ed_secs);
					} else {
						nm = GameSettings.GAME_PLAYER_NICK + ": -- " + getString(R.string.game_hud_ed_secs);
					}
					break;
				case 43:
					if (GameSettings.RECORD_LONG_JUMP > 0) {
						nm = GameSettings.GAME_PLAYER_NICK + ": " + GameSettings.RECORD_LONG_JUMP + getString(R.string.game_hud_ed_metr);
					} else {
						nm = GameSettings.GAME_PLAYER_NICK + ": -- " + getString(R.string.game_hud_ed_metr);
					}
					break;
				case 44:
					if (GameSettings.RECORD_PIKE_THROW > 0) {
						nm = GameSettings.GAME_PLAYER_NICK + ": " + GameSettings.RECORD_PIKE_THROW + getString(R.string.game_hud_ed_metr);
					} else {
						nm = GameSettings.GAME_PLAYER_NICK + ": -- " + getString(R.string.game_hud_ed_metr);
					}
					break;
				case 45:
					if (GameSettings.RECORD_ARCHERY > 0) {
						nm = GameSettings.GAME_PLAYER_NICK + ": " + GameSettings.RECORD_ARCHERY + getString(R.string.game_hud_ed_points);
					} else {
						nm = GameSettings.GAME_PLAYER_NICK + ": -- " + getString(R.string.game_hud_ed_points);
					}
					break;
				case 46:
					if (GameSettings.RECORD_SHOOTING > 0) {
						nm = GameSettings.GAME_PLAYER_NICK + ": " + GameSettings.RECORD_SHOOTING + getString(R.string.game_hud_ed_secs);
					} else {
						nm = GameSettings.GAME_PLAYER_NICK + ": -- " + getString(R.string.game_hud_ed_secs);
					}
					break;

				default:
					nm = "-";
					break;
			}

		} else {
			nm = "-";
		}
		this.lNikPointInfo.setText(nm);
		new MyTask().execute(gameID);

	}

	private void TEst() {
		ArrayList<LeaderboardItem> recordsList = new ArrayList<LeaderboardItem>();
		LeaderboardItem item;

		item = new LeaderboardItem();

		item.setUid(Integer.valueOf("1"));
		item.setLeadPlase(1);
		item.setCountry("BY");
		item.setNick("ivsn");
		item.setProgVal(Float.valueOf("100"));
		item.setProgUpDate("10.12.13");

		recordsList.add(item);

		final ListView lv_mlb = (ListView) findViewById(R.id.mlb_totalList);
		lv_mlb.setAdapter(new CastomItemListAdapter(this, recordsList));

	}

	@Override
	public void onFocusChange(View v, boolean focusFlag) {
	/* if ((v.getId() == R.id.mlb_etGod) && (!focusFlag)) { hideKeyBoard(v); paramGodKw = "" + tvGod.getText() + tvKw.getText(); // loadRecords(tg); } if ((v.getId() == R.id.mlb_etKw) && (!focusFlag)) { hideKeyBoard(v); paramGodKw = "" +
	 * tvGod.getText() + tvKw.getText(); // loadRecords(tg); } */
	}

	@Override
	public void onClick(View v) {
		hideKeyBoard(v);
		paramGodKw = "" + tvGod.getText() + tvKw.getText();

		if (v.getId() == R.id.mlb_btnBack) {
			Intent resIntent = new Intent();
			setResult(RESULT_OK, resIntent);
			outOfAppFlag = false;
			this.finish();
		} else if (v.getId() == R.id.mlb_set_current_date) {
			loadRecords(tg);
		} else if (v.getId() == R.id.mlb_set_current_week) {
			tvGod.setText(String.valueOf(GameSettings.WEEK_OF_YEAR).substring(0, 4));
			tvKw.setText(String.valueOf(GameSettings.WEEK_OF_YEAR).substring(4, 6));
			paramGodKw = String.valueOf(GameSettings.WEEK_OF_YEAR);
			loadRecords(tg);
		} else {
			tg = v.getTag().toString();
			loadRecords(v.getTag().toString());
		}

	}

	public void onBackPressed() {
		outOfAppFlag = false;
		super.onBackPressed();
	}

	protected void onPause() {
		if (outOfAppFlag) {
			// MTrack.pause();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MTrack.resume();
		outOfAppFlag = true;
		super.onResume();
	}

	class MyTask extends AsyncTask<String, Integer, Void> {
		ArrayList<LeaderboardItem> recordsList;
		ListView lv_mlb;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(String... args) {
			try {
				for (String arg : args) {

					recordsList = new ArrayList<LeaderboardItem>();

					downloadLb(arg);

					if (recordsList.size() != 0) {
						publishProgress(1);
					} else {
						publishProgress(0);
					}
					TimeUnit.SECONDS.sleep(1); //
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... vd) {
			super.onProgressUpdate(vd);
			if (!vd[0].equals(0)) {

				lv_mlb = (ListView) findViewById(R.id.mlb_totalList);
				lv_mlb.setAdapter(new CastomItemListAdapter(LeaderBoardActivity.this, recordsList));

			} else {
				Toast.makeText(LeaderBoardActivity.this, (CharSequence) getString(R.string.connection_fail), Toast.LENGTH_SHORT).show();

			}
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			setFocusToButton();

		}

		private void setFocusToButton() {
			for (Button bt : gameType) {
				if (bt.getTag().toString().equals(tg)) {
					bt.setSelected(true);
				} else {
					bt.setSelected(false);
				}

			}
		}

		private String downloadLb(String serverGameLBID) {
			JSONParser jParser = new JSONParser();
			JSONArray records = null;
			// Integer listResID;

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("game", serverGameLBID));
			params.add(new BasicNameValuePair("god_kv", paramGodKw));

			try {
				// getting JSON string from URL
				JSONObject json = new JSONObject();
				json = jParser.makeHttpRequest(url_all_records, "GET", params);

				// Check your log cat for JSON reponse
				// Log.d("All Products: ", json.toString());
				if (json.equals(null)) {
				} else
					try {
						// Checking for SUCCESS TAG
						int success = json.getInt(ATTRIBUTE_NAME_SUCCESS);

						if (success == 1) {
							// game id found
							// Getting Array of game records
							records = json.getJSONArray(ATTRIBUTE_NAME_RECORDS_TAG);
							// looping through All Products
							for (int i = 0; i < records.length(); i++) {
								JSONObject c = records.getJSONObject(i);

								LeaderboardItem item = new LeaderboardItem();

								item.setUid(Integer.valueOf(c.getString(ATTRIBUTE_NAME_UID)));
								item.setLeadPlase(i + 1);
								item.setCountry(c.getString(ATTRIBUTE_NAME_COUNTRY));
								item.setNick(c.getString(ATTRIBUTE_NAME_NAME));
								item.setProgVal(Math.abs(Float.valueOf(c.getString(ATTRIBUTE_NAME_RECORD_VL))));
								item.setProgUpDate(c.getString(ATTRIBUTE_NAME_DATE));

								recordsList.add(item);
							}
						} else {
							LeaderboardItem item2 = new LeaderboardItem();

							item2.setUid(0);
							item2.setLeadPlase(1);
							item2.setCountry("");
							item2.setNick("");
							item2.setProgVal(0);
							item2.setProgUpDate("");

							recordsList.add(item2);
							return "ok";
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

	protected void hideKeyBoard(View v) {
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
}
