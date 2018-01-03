package by.kipind.game.line;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import by.kipind.game.olympicgames.GameSettings;

public class CRUDObject {
    // Progress Dialog
    private ProgressDialog pDialog;

    SharedPreferences sPref;

    JSONParser jsonParser = new JSONParser();

    private String inputName = "", inputRecord = "", inputGame = "", uid = "", inputNickID = "";
    private String inputStatType = "", inputStatValue = "";
    Context cntx;
    // url to create new product
    private static final String url_create_update_product = GameSettings.GAME_ONLINE_SERVER + "/a_create_update_record.php";
    // url to create new product
    private static final String url_insert_some_stat = GameSettings.GAME_ONLINE_SERVER + "/stat_in.php";
    // single product url
    private static final String url_product_detials = GameSettings.GAME_ONLINE_SERVER + "/a_get_product_details.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_RECORD = "record";
    private static final String TAG_UID = "user_id";

    private static final String TAG_NAME = "name";
    private static final String TAG_GAME = "game";
    private static final String TAG_RECORD_VL = "recordvalue";

    final String LOG_TAG = "myLogs";

    public CRUDObject() {
	// TODO Auto-generated constructor stub

    }

    public boolean isNetCon(Context c) {
	ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	if (activeNetwork != null && activeNetwork.isConnected()) {
	    return true;
	} else {
	    return false;
	}
    }

    public void sendRecord(String name, String record, String gameId, Context c, String NickID) {
	this.inputName = name;
	this.inputRecord = record;
	this.inputGame = gameId;
	this.inputNickID = NickID;
	this.cntx = c;

	ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	if (activeNetwork != null && activeNetwork.isConnected()) {
	    new SendRecordInfo().execute();
	} else {
	    GameSettings.syncAction("-1");
	}
    }

    public void sendStatsInfo(String name, String statVal, String statId, Context c, int NickLoc) {
	this.inputName = name;
	this.inputStatValue = statVal;
	this.inputStatType = statId;
	this.inputNickID = String.valueOf(NickLoc);
	this.cntx = c;

	ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	if (activeNetwork != null && activeNetwork.isConnected()) {
	    new SendStatsInfo().execute();

	}
    }

    /**
     * Background Async Task to upate \create new record
     * */
    class SendRecordInfo extends AsyncTask<String, String, Integer> {
	String fgi;
	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	@Override
	protected void onPreExecute() {
	    super.onPreExecute();

	}

	/**
	 * Creating product
	 * */
	protected Integer doInBackground(String... args) {
	    // Building Parameters
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("name", inputName));
	    params.add(new BasicNameValuePair("game", inputGame));
	    params.add(new BasicNameValuePair("record", inputRecord));
	    params.add(new BasicNameValuePair("name_id", inputNickID));
	    fgi = String.valueOf(System.currentTimeMillis()).substring(6);
	    // fgi = fgi + String.valueOf(Integer.valueOf(inputNickID) + Integer.valueOf(inputGame));
	    params.add(new BasicNameValuePair("f_game_id", fgi));

	    // Log.d(LOG_TAG, "param---->" + inputName + " " + inputGame + " " + inputRecord + " " + inputNickID);

	    try {
		JSONObject json = jsonParser.makeHttpRequest(url_create_update_product, "POST", params);


		if (json.getInt(TAG_SUCCESS) > 0) {
		    return json.getInt(TAG_UID);
		} else {
		    return json.getInt(TAG_SUCCESS);
		}

		// return json.getInt(TAG_SUCCESS);

	    } catch (JSONException e) {
		// Log.d(LOG_TAG, "err---->" + e.getMessage());
		GameSettings.syncAction("-1");
	    }
	    GameSettings.syncAction("-1");
	    return -404;
	}

	/**
	 * After completing background task
	 * **/
	protected void onPostExecute(Integer rslt) {
	    // sPref = cntx.getSharedPreferences("Records", Context.MODE_PRIVATE);
	    if (rslt > 0) {
		GameSettings.UpdateUser(inputName, String.valueOf(rslt), "1");
	    } else {
		GameSettings.UpdateUser(inputName, inputNickID, String.valueOf(rslt));
	    }

	    /*
	     * if ((rslt != 0) && (rslt != -3) && (rslt != -4)) { GameSettings.UpdateUser(inputName, String.valueOf(rslt)); } if (rslt == -3) { GameSettings.UpdateUser("-1", "-1"); } */
	}
    }

    class SendStatsInfo extends AsyncTask<String, String, Integer> {

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	@Override
	protected void onPreExecute() {
	    super.onPreExecute();

	}

	/**
	 * Creating product
	 * */
	protected Integer doInBackground(String... args) {

	    // Building Parameters
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("name", inputName));
	    params.add(new BasicNameValuePair("sType", inputStatType));
	    params.add(new BasicNameValuePair("sValue", inputStatValue));
	    params.add(new BasicNameValuePair("nickLoc", inputNickID));

	    try {
		JSONObject json = jsonParser.makeHttpRequest(url_insert_some_stat, "POST", params);
		return json.getInt(TAG_SUCCESS);

	    } catch (JSONException e) {
	    }

	    return -404;
	}

	protected void onPostExecute(Integer rslt) {

	}

    }

    /**
     * Background Async Task to Get complete product details
     * */
    class GetProductDetails extends AsyncTask<String, String, String> {

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	@Override
	protected void onPreExecute() {
	    super.onPreExecute();

	}

	/**
	 * Getting product details in background thread
	 * */
	protected String doInBackground(String... args) {

	    int success;
	    try {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("name", inputName));
		params.add(new BasicNameValuePair("game", inputGame));

		JSONObject json = jsonParser.makeHttpRequest(url_product_detials, "GET", params);

		// Log.d("Single user record", json.toString());

		success = json.getInt(TAG_SUCCESS);
		if (success == 1) {
		    JSONArray productObj = json.getJSONArray(TAG_RECORD); // JSON//
									  // Array

		    JSONObject product = productObj.getJSONObject(0);

		    String rName = product.getString(TAG_NAME);
		    String rGame = product.getString(TAG_GAME);
		    String rRecordVl = product.getString(TAG_RECORD_VL);

		} else {
		    // product with pid not found
		}
	    } catch (JSONException e) {
		e.printStackTrace();
	    }

	    return null;
	}

	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	protected void onPostExecute(String file_url) {
	    // dismiss the dialog once got all details
	    pDialog.dismiss();
	}
    }

}