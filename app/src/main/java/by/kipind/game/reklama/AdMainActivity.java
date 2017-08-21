package by.kipind.game.reklama;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import by.kipind.game.olympicgames.R;

public class AdMainActivity extends Activity implements OnClickListener {

    private final String unLockLvlAdBlock = "ca-app-pub-3924626110211690/1734845360";

    private InterstitialAd mInterstitialAd;

    private ImageView ivLeaderBord;
    private int loadAdErrorCode=0;
    private boolean showAdFlag=false;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.ad_main);

	ivLeaderBord = (ImageView) findViewById(R.id.imageView1);
	ivLeaderBord.setOnClickListener(this);

	mInterstitialAd = new InterstitialAd(this);
	mInterstitialAd.setAdUnitId(unLockLvlAdBlock);

	mInterstitialAd.setAdListener(new AdListener() {
	    @Override
	    public void onAdLoaded() {

		showRevVideo();
	    }

	    @Override
	    public void onAdFailedToLoad(int errorCode) {

		if (errorCode != 2) { // if there is no ad for this user in %ad_net_work% open lvl
		    backToGame(-777);
		} else {
		ivLeaderBord.setVisibility(View.VISIBLE);
		    loadAdErrorCode = errorCode;
		}
	    }

	    @Override
	    public void onAdLeftApplication() {
	    }

	    @Override
	    public void onAdClosed() {
		// RequestNewInterstatial();
		backToGame(-777);
	    }

	});


RequestInterstatial();

        if(!showAdFlag){
            showAdFlag=showRevVideo();
        }


    }

    protected void backToGame(int extraBackParam) {
	Intent resIntent = new Intent();
	resIntent.putExtra("adShowRes", extraBackParam);
	setResult(RESULT_OK, resIntent);
	this.finish();
    }

    protected void RequestInterstatial() {
	AdRequest adRequest = new AdRequest.Builder().addTestDevice("D1BD3EB7210C5E0FBE523551EA523168").build();

	mInterstitialAd.loadAd(adRequest);
    }

    public boolean showRevVideo() {
	boolean res = false;



	if (mInterstitialAd.isLoaded()) {
	    mInterstitialAd.show();

	} else {
	}

	return res;
    }

    @Override
    public void onClick(View v) {
	if (v.equals(this.ivLeaderBord)) {
	    backToGame(loadAdErrorCode);
	}

    }

    @Override
    public void onBackPressed() {
	backToGame(loadAdErrorCode);
    }

}
