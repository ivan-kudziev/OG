package by.kipind.game.reklama;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import by.kipind.game.olympicgames.GameSettings;
import by.kipind.game.olympicgames.ResourcesManager;

/*import com.appodeal.ads.Appodeal;
 import com.appodeal.ads.RewardedVideoCallbacks;
 */
public class AdModule {
    private static final AdModule INSTANCE = new AdModule();


    private boolean initFlag_video = false;

    private List<BtnAdStat> lis = new ArrayList<BtnAdStat>();
    private int activeBtnId = -1;
    public static AdModule getInstance() {
	return INSTANCE;
    }

    public AdModule() {

    }

    public void initAd() {
	if (!initFlag_video) {

	    initFlag_video = true;
	}
    }


    public boolean showRevVideo(int gameId) {
	boolean res = true;
	this.activeBtnId = gameId;
	Intent intent = new Intent(ResourcesManager.getInstance().activity, AdMainActivity.class);
	ResourcesManager.getInstance().activity.startActivityForResult(intent, 2);

	return res;
    }

    public void AdShown() {
	GameSettings.setBlockByAdGame(this.activeBtnId, "0");

	for (BtnAdStat btn : lis) {
	    if (btn.gameId == this.activeBtnId) {
		btn.setCurrentState(BtnAdStat.BTN_STATE_LOADING);
		btn.reMove();
	    }
	}
	this.activeBtnId = -1;
    }

    public void AdFail() {
	this.activeBtnId = -1;
    }

    public void addListener(int gameBtnType, BtnAdStat btnAdStat) {
	lis.add(btnAdStat);
    }


}
