package by.kipind.game.line;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import by.kipind.game.olympicgames.GameSettings;


public class JSONParser {
	final String LOG_TAG = "myLogs";

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	// constructor
	public JSONParser() {

	}

	// function get json from url
	// by making HTTP POST or GET mehtod
	public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {
		// Making HTTP request
		jObj = new JSONObject();

		if (checkConnetionToServer()) {

			try {
				// check for request method
				if (method == "POST") {
					// request method is POST
					// defaultHttpClient
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(url);
					httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

					HttpResponse httpResponse = httpClient.execute(httpPost);
					HttpEntity httpEntity = httpResponse.getEntity();
					is = httpEntity.getContent();

				}
				if (method == "GET") {
					// request method is GET
					DefaultHttpClient httpClient = new DefaultHttpClient();
					String paramString = URLEncodedUtils.format(params, "UTF-8");
					url += "?" + paramString;
					HttpGet httpGet = new HttpGet(url);

					HttpResponse httpResponse = httpClient.execute(httpGet);
					HttpEntity httpEntity = httpResponse.getEntity();
					is = httpEntity.getContent();
				}


			} catch (Exception e) {
				// return null;
			}

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
			} catch (Exception e) {

				Log.e("Buffer Error", "Error converting result " + e.toString());
				// return null;
			}

			// try parse the string to a JSON object
			try {
				jObj = new JSONObject(json);
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
				// return null;
			}
		}
		// return JSON String
		return jObj;

	}

	private boolean checkConnetionToServer() {

		try {
			URL urlCC = new URL(GameSettings.GAME_ONLINE_SERVER + "/");
			HttpURLConnection urlc = (HttpURLConnection) urlCC.openConnection();
			urlc.setRequestProperty("User-Agent", "test");
			urlc.setRequestProperty("Connection", "close");
			urlc.setConnectTimeout(1000);
			urlc.connect();


			if (urlc.getResponseCode() == 200) {
				return true;

			} else {
				GameSettings.syncAction("-1");
				return false;
			}

		} catch (IOException e) {
			GameSettings.syncAction("-1");
			return false;
		}

	}
}
