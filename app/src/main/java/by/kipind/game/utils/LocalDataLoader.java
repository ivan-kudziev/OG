package by.kipind.game.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class LocalDataLoader {

    private SharedPreferences sharedPref;
    private Context appContext;

    public LocalDataLoader(String prefFileName, Context appContext) {
	sharedPref = appContext.getApplicationContext().getSharedPreferences(prefFileName, appContext.MODE_PRIVATE);
	this.appContext = appContext;
    }

    public boolean saveParam(String tag, String val) {

	try {
	    sharedPref.edit().putString(tag, val).commit();

	} catch (Exception e) {
	    Toast.makeText(this.appContext, "Error while try to save in sharedPreference : " + e.toString(), Toast.LENGTH_LONG).show();
	    return false;
	}

	return true;
    }

    public String getStringParam(String tag) {
	String res = null;
	try {
	    res = sharedPref.getString(tag, (String) "-1");

	} catch (Exception e) {
	    Toast.makeText(this.appContext, "Error while try to get from sharedPreference: " + e.toString(), Toast.LENGTH_LONG).show();
	    return null;
	}
	return res;
    }

}
