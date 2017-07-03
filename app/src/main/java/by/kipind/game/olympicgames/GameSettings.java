package by.kipind.game.olympicgames;

import android.app.Application;
import android.content.Context;
import android.widget.FrameLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

import by.kipind.game.line.CRUDObject;
import by.kipind.game.utils.LocalDataLoader;

public class GameSettings extends Application {
    final static String LOG_TAG = "myLogs";


    public static final int GAME_CODE = 4;
    public static final String GAME_ONLINE_SERVER = "http://androidgameleaderbords-mrkip.rhcloud.com";

    public static final int ACTIVITY_ID_SETTINGS_INFO = 0;
    public static final int ACTIVITY_ID_RUN100 = 1;
    public static final int ACTIVITY_ID_RUN_BARIER = 2;
    public static final int ACTIVITY_ID_LONG_JUMP = 3;
    public static final int ACTIVITY_ID_PIKE_THROW = 4;
    public static final int ACTIVITY_ID_ARCHERY = 5;
    public static final int ACTIVITY_ID_SHOOTING = 6;
    public static final int ACTIVITY_ID_RAFTING = 9;
    public static final int ACTIVITY_ID_EXIT = 7;
    public static final int ACTIVITY_ID_LB = 8;

    private static final String TAG_GAME_PLAYER_NICK = "Nick";
    private static final String TAG_GAME_PLAYER_NICK_ID = "NickLoc";
    private static final String TAG_AD_VIDEO_SHOWN_DATE = "videoShowDate";
    private static final String TAG_GOOGLE_PLAY_VISIT = "GPVisitStatus";
    private static final String TAG_WEEK_OF_YEAR = "kv";
    private static final String TAG_GAME_DATA_SYNC = "sync";

    public static boolean TAG_AD_BUNNER_SHOW = false;

    public final static String PREFERENCE = "olimpGameLocalData";

    public final static long SECONDS_MILLIS = 1000;
    public final static long MINUTE_MILLIS = SECONDS_MILLIS * 60;
    public final static long HOURS_MILLIS = MINUTE_MILLIS * 60;
    public final static long DAY_MILLIS = HOURS_MILLIS * 24;

    public static Calendar calendar;
    public static FrameLayout mFrameLayout;

    // public static int googlePlayFlag = 0, soundValue = 1, musicValue = 1;

    private static Context GAME_CONTEXT;
    private static CRUDObject netAction = new CRUDObject();

    public static int GOOGLE_PLAY_VISIT;
    public static int WEEK_OF_YEAR;
    public static int AD_VIDEO_SHOWN; // -1 -not shown
    public static int SOUND_VAL = 1; // 0-1
    public static int MUSIC_VAL = 1;

    public static Float RECORD_RUN100;
    public static Float RECORD_RUN_BARIER;
    public static Float RECORD_LONG_JUMP;
    public static Float RECORD_ARCHERY;
    public static Float RECORD_SHOOTING;
    public static Float RECORD_PIKE_THROW;
    public static Float RECORD_RAFTING;
    public static Float RECORD_BOW_SPEAR;

    public static Float W_RECORD_RUN100;
    public static Float W_RECORD_RUN_BARIER;
    public static Float W_RECORD_LONG_JUMP;
    public static Float W_RECORD_ARCHERY;
    public static Float W_RECORD_SHOOTING;
    public static Float W_RECORD_PIKE_THROW;
    public static Float W_RECORD_RAFTING;
    public static Float W_RECORD_BOW_SPEAR;

    public static String GAME_PLAYER_NICK = "-1";
    public static String GAME_PLAYER_NICK_ID = "-1";
    private static String GAME_DATA_SYNC = "0";
    public static LocalDataLoader LOAD_MANAGER;

    private static final int GAME_TYPE_NUM = 6;

    public void onCreate() {
        super.onCreate();

        GameSettings.GAME_CONTEXT = getApplicationContext();
        LOAD_MANAGER = new LocalDataLoader(PREFERENCE, getAppContext());

        if (Long.valueOf(LOAD_MANAGER.getStringParam(TAG_AD_VIDEO_SHOWN_DATE)) - System.currentTimeMillis() > 86000000) {
            AD_VIDEO_SHOWN = (int) Math.round(1 + Math.random() * 5);
        }
        // TODO: add vodeo flag
        // AD_VIDEO_SHOWN = -1;
        int j = (int) Math.round(1 + Math.random() * (GAME_TYPE_NUM - 1));
        int i = j + 1;
        while (i != j) {
            if (getBlockByAdGame(i) == 0 || getBlockByAdGame(i) == -1) {
                setBlockByAdGame(i, "1");
                i = j;
                break;
            }
            if (i != GAME_TYPE_NUM) {
                i++;
            } else {
                i = 1;
            }
        }
        initYearWeek();
        LoadUserData();
        LoadPersonalRecords();

    }

    public static Context getAppContext() {
        return GameSettings.GAME_CONTEXT;
    }

    public static void LoadPersonalRecords() {

        RECORD_RUN100 = Float.valueOf(LOAD_MANAGER.getStringParam("RUN100"));
        RECORD_RUN_BARIER = Float.valueOf(LOAD_MANAGER.getStringParam("RUN_BARIER"));
        RECORD_LONG_JUMP = Float.valueOf(LOAD_MANAGER.getStringParam("LONG_JUMP"));
        RECORD_ARCHERY = Float.valueOf(LOAD_MANAGER.getStringParam("ARCHERY"));
        RECORD_SHOOTING = Float.valueOf(LOAD_MANAGER.getStringParam("SHOOTING"));
        RECORD_PIKE_THROW = Float.valueOf(LOAD_MANAGER.getStringParam("PIKE_THROW"));
        RECORD_RAFTING = Float.valueOf(LOAD_MANAGER.getStringParam("RAFTING"));
        RECORD_BOW_SPEAR = Float.valueOf(LOAD_MANAGER.getStringParam("BOW_SPEAR"));

        W_RECORD_RUN100 = Float.valueOf(LOAD_MANAGER.getStringParam("W_RUN100"));
        W_RECORD_RUN_BARIER = Float.valueOf(LOAD_MANAGER.getStringParam("W_RUN_BARIER"));
        W_RECORD_LONG_JUMP = Float.valueOf(LOAD_MANAGER.getStringParam("W_LONG_JUMP"));
        W_RECORD_ARCHERY = Float.valueOf(LOAD_MANAGER.getStringParam("W_ARCHERY"));
        W_RECORD_SHOOTING = Float.valueOf(LOAD_MANAGER.getStringParam("W_SHOOTING"));
        W_RECORD_PIKE_THROW = Float.valueOf(LOAD_MANAGER.getStringParam("W_PIKE_THROW"));
        W_RECORD_RAFTING = Float.valueOf(LOAD_MANAGER.getStringParam("W_RAFTING"));
        W_RECORD_BOW_SPEAR = Float.valueOf(LOAD_MANAGER.getStringParam("W_BOW_SPEAR"));

    }

    public static void resetWeekRecords() {

        LOAD_MANAGER.saveParam("W_RUN100", "-1");
        LOAD_MANAGER.saveParam("W_RUN_BARIER", "-1");
        LOAD_MANAGER.saveParam("W_LONG_JUMP", "-1");
        LOAD_MANAGER.saveParam("W_ARCHERY", "-1");
        LOAD_MANAGER.saveParam("W_SHOOTING", "-1");
        LOAD_MANAGER.saveParam("W_PIKE_THROW", "-1");
        LOAD_MANAGER.saveParam("W_RAFTING", "-1");
        LOAD_MANAGER.saveParam("W_BOW_SPEAR", "-1");
    }

    public static void resetPersRecords() {

        LOAD_MANAGER.saveParam("RUN100", "-1");
        LOAD_MANAGER.saveParam("RUN_BARIER", "-1");
        LOAD_MANAGER.saveParam("LONG_JUMP", "-1");
        LOAD_MANAGER.saveParam("ARCHERY", "-1");
        LOAD_MANAGER.saveParam("SHOOTING", "-1");
        LOAD_MANAGER.saveParam("PIKE_THROW", "-1");
        LOAD_MANAGER.saveParam("BOW_SPEAR", "-1");
        LOAD_MANAGER.saveParam("RAFTING", "-1");
    }

    public static String[] LoadShadowTrace(String tag) {
        Pattern pattern = Pattern.compile(":");
        String strVal = LOAD_MANAGER.getStringParam(tag + "_s");
        if (strVal != "-1") {
            return pattern.split(strVal);
        } else {
            return null;
        }

    }

    public static void LoadUserData() {
        // int[] arr = { 1, 2, 5, 6, 7, 8, 10, 17, 29 };
        // TODO:REM
        // GAME_PLAYER_NICK = "Plpo";// LOAD_MANAGER.getStringParam(TAG_GAME_PLAYER_NICK);//
        // GAME_PLAYER_NICK_ID = "47"; // LOAD_MANAGER.getStringParam(TAG_GAME_PLAYER_NICK_ID);
        // GAME_DATA_SYNC = "1";// LOAD_MANAGER.getStringParam(TAG_GAME_DATA_SYNC);
        // GOOGLE_PLAY_VISIT = 1;// Integer.valueOf(LOAD_MANAGER.getStringParam(TAG_GOOGLE_PLAY_VISIT));
        // UpdateShadowTrace(1, "RUN100", arr);

        if (LOAD_MANAGER.getStringParam("appVersion") == "-1") {
            LOAD_MANAGER.saveParam("appVersion", "5.xxx");
            LOAD_MANAGER.saveParam(TAG_GAME_PLAYER_NICK, "-1");
            LOAD_MANAGER.saveParam(TAG_GAME_PLAYER_NICK_ID, "-1");
            resetPersRecords();
            resetWeekRecords();
        }
        GAME_PLAYER_NICK = LOAD_MANAGER.getStringParam(TAG_GAME_PLAYER_NICK);//
        GAME_PLAYER_NICK_ID = LOAD_MANAGER.getStringParam(TAG_GAME_PLAYER_NICK_ID);
        GAME_DATA_SYNC = LOAD_MANAGER.getStringParam(TAG_GAME_DATA_SYNC);
        //GOOGLE_PLAY_VISIT = Integer.valueOf(LOAD_MANAGER.getStringParam(TAG_GOOGLE_PLAY_VISIT));
        GOOGLE_PLAY_VISIT = 1;
    }

    public static void UpdateRecord(int key, String tag, String value) {
        LOAD_MANAGER.saveParam("W_" + tag, value);

        float fValue = Float.valueOf(value);

        switch (key) {
            case ACTIVITY_ID_RUN100:
                W_RECORD_RUN100 = fValue;
                if (fValue < RECORD_RUN100 || RECORD_RUN100 == -1) {
                    LOAD_MANAGER.saveParam(tag, value);
                    RECORD_RUN100 = fValue;
                }
                value = "-" + value;
                break;
            case ACTIVITY_ID_RUN_BARIER:
                W_RECORD_RUN_BARIER = fValue;
                if (fValue < RECORD_RUN_BARIER || RECORD_RUN_BARIER == -1) {
                    LOAD_MANAGER.saveParam(tag, value);
                    RECORD_RUN_BARIER = fValue;
                }
                value = "-" + value;
                break;
            case ACTIVITY_ID_LONG_JUMP:
                W_RECORD_LONG_JUMP = fValue;
                if (fValue > RECORD_LONG_JUMP) {
                    LOAD_MANAGER.saveParam(tag, value);
                    RECORD_LONG_JUMP = fValue;
                }
                break;
            case ACTIVITY_ID_ARCHERY:
                W_RECORD_ARCHERY = fValue;
                if (fValue > RECORD_ARCHERY) {
                    LOAD_MANAGER.saveParam(tag, value);
                    RECORD_ARCHERY = fValue;
                }
                break;
            case ACTIVITY_ID_SHOOTING:
                W_RECORD_SHOOTING = fValue;
                if (fValue < RECORD_SHOOTING || RECORD_SHOOTING == -1) {
                    LOAD_MANAGER.saveParam(tag, value);
                    RECORD_SHOOTING = fValue;
                }
                value = "-" + value;
                break;
            case ACTIVITY_ID_RAFTING:
                W_RECORD_RAFTING = fValue;
                if (fValue < RECORD_RAFTING || RECORD_RAFTING == -1) {
                    LOAD_MANAGER.saveParam(tag, value);
                    RECORD_RAFTING = fValue;
                }
                value = "-" + value;
                break;
            case ACTIVITY_ID_PIKE_THROW:
                W_RECORD_PIKE_THROW = fValue;
                if (fValue > RECORD_PIKE_THROW) {
                    LOAD_MANAGER.saveParam(tag, value);
                    RECORD_PIKE_THROW = fValue;
                }
                break;

            default:
                break;
        }


        netAction.sendRecord(GAME_PLAYER_NICK, value, GAME_CODE + "" + key, GAME_CONTEXT, GAME_PLAYER_NICK_ID);

    }

    public static void UpdateShadowTrace(int key, String tag, List<Integer> shadowTrace) {
        String value = "";

        for (Integer tracePoint : shadowTrace) {
            value = value + String.valueOf(tracePoint) + ":";
        }

        LOAD_MANAGER.saveParam(tag + "_s", value);

        // netAction.sendRecord(GAME_PLAYER_NICK, value, GAME_CODE + "" + key, GAME_CONTEXT, GAME_PLAYER_NICK_STATUS);

    }

    public static int UpdateUser(String etNick, String lock, String dataSncStatus) {

        GameSettings.LOAD_MANAGER.saveParam(TAG_GAME_PLAYER_NICK, etNick);
        GameSettings.LOAD_MANAGER.saveParam(TAG_GAME_PLAYER_NICK_ID, lock);
        GameSettings.LOAD_MANAGER.saveParam(TAG_GAME_DATA_SYNC, dataSncStatus);

        LoadUserData();

        return 0;

    }

    public static void onSync() {
        // Log.d(LOG_TAG, "sync---->" + GAME_PLAYER_NICK + " " + GAME_PLAYER_NICK_ID + " " + GAME_DATA_SYNC);

        if (Integer.valueOf(GAME_DATA_SYNC) < 0 && netAction.isNetCon(GAME_CONTEXT)) {
            // if (GAME_PLAYER_NICK_ID == "1") {
            if (W_RECORD_RUN_BARIER != -1)
                netAction.sendRecord(GAME_PLAYER_NICK, String.valueOf(-1 * W_RECORD_RUN_BARIER), GAME_CODE + "" + ACTIVITY_ID_RUN_BARIER, GAME_CONTEXT, GAME_PLAYER_NICK_ID);
            if (W_RECORD_LONG_JUMP != -1)
                netAction.sendRecord(GAME_PLAYER_NICK, String.valueOf(W_RECORD_LONG_JUMP), GAME_CODE + "" + ACTIVITY_ID_LONG_JUMP, GAME_CONTEXT, GAME_PLAYER_NICK_ID);
            if (W_RECORD_PIKE_THROW != -1)
                netAction.sendRecord(GAME_PLAYER_NICK, String.valueOf(W_RECORD_PIKE_THROW), GAME_CODE + "" + ACTIVITY_ID_PIKE_THROW, GAME_CONTEXT, GAME_PLAYER_NICK_ID);
            if (W_RECORD_SHOOTING != -1)
                netAction.sendRecord(GAME_PLAYER_NICK, String.valueOf(-1 * W_RECORD_SHOOTING), GAME_CODE + "" + ACTIVITY_ID_SHOOTING, GAME_CONTEXT, GAME_PLAYER_NICK_ID);
            if (W_RECORD_ARCHERY != -1)
                netAction.sendRecord(GAME_PLAYER_NICK, String.valueOf(W_RECORD_ARCHERY), GAME_CODE + "" + ACTIVITY_ID_ARCHERY, GAME_CONTEXT, GAME_PLAYER_NICK_ID);
            if (W_RECORD_RUN100 != -1)
                netAction.sendRecord(GAME_PLAYER_NICK, String.valueOf(-1 * W_RECORD_RUN100), GAME_CODE + "" + ACTIVITY_ID_RUN100, GAME_CONTEXT, GAME_PLAYER_NICK_ID);
            if (W_RECORD_RAFTING != -1)
                netAction.sendRecord(GAME_PLAYER_NICK, String.valueOf(-1 * W_RECORD_RAFTING), GAME_CODE + "" + ACTIVITY_ID_RAFTING, GAME_CONTEXT, GAME_PLAYER_NICK_ID);

            GameSettings.LOAD_MANAGER.saveParam(GAME_DATA_SYNC, "1");

            GAME_DATA_SYNC = "1";
        /* } else { if (RECORD_RUN100 != -1) netAction.sendRecord(GAME_PLAYER_NICK, String.valueOf(-1 * RECORD_RUN100), GAME_CODE + "" + ACTIVITY_ID_RUN100, GAME_CONTEXT, GAME_PLAYER_NICK_ID); else if (RECORD_RUN_BARIER != -1)
	     * netAction.sendRecord(GAME_PLAYER_NICK, String.valueOf(-1 * RECORD_RUN_BARIER), GAME_CODE + "" + ACTIVITY_ID_RUN_BARIER, GAME_CONTEXT, GAME_PLAYER_NICK_ID); else if (RECORD_LONG_JUMP != -1) netAction.sendRecord(GAME_PLAYER_NICK,
	     * String.valueOf(RECORD_LONG_JUMP), GAME_CODE + "" + ACTIVITY_ID_LONG_JUMP, GAME_CONTEXT, GAME_PLAYER_NICK_ID); else if (RECORD_PIKE_THROW != -1) netAction.sendRecord(GAME_PLAYER_NICK, String.valueOf(RECORD_PIKE_THROW), GAME_CODE + "" +
	     * ACTIVITY_ID_PIKE_THROW, GAME_CONTEXT, GAME_PLAYER_NICK_ID); else if (RECORD_SHOOTING != -1) netAction.sendRecord(GAME_PLAYER_NICK, String.valueOf(-1 * RECORD_SHOOTING), GAME_CODE + "" + ACTIVITY_ID_SHOOTING, GAME_CONTEXT,
	     * GAME_PLAYER_NICK_ID); else if (RECORD_ARCHERY != -1) netAction.sendRecord(GAME_PLAYER_NICK, String.valueOf(RECORD_ARCHERY), GAME_CODE + "" + ACTIVITY_ID_ARCHERY, GAME_CONTEXT, GAME_PLAYER_NICK_ID);
	     * 
	     * GAME_DATA_SYNC = 0; } */
        }

    }

    public static int[] getBlockByAd() {
        int[] res = {0, 0, 0, 0, 0, 0, 0};
        for (int i = 1; i <= GAME_TYPE_NUM; i++) {
            res[i] = Integer.valueOf(LOAD_MANAGER.getStringParam("game" + i));
        }
        return res;
    }

    public static void setAllBlockByAd(String value) {
        for (int i = 1; i <= GAME_TYPE_NUM; i++) {
            LOAD_MANAGER.saveParam("game" + i, value);
        }

    }

    public static void setBlockByAdGame(int game, String value) {
        LOAD_MANAGER.saveParam("game" + game, value);

    }

    public int getBlockByAdGame(int game) {
        return Integer.valueOf(LOAD_MANAGER.getStringParam("game" + game));

    }

    public static void rateAction(int i) {
        LOAD_MANAGER.saveParam(TAG_GOOGLE_PLAY_VISIT, String.valueOf(i));
        GOOGLE_PLAY_VISIT = i;

    }

    public static void syncAction(String i) {
        LOAD_MANAGER.saveParam(TAG_GAME_DATA_SYNC, i);
        GAME_DATA_SYNC = i;

    }

    public static void setWeek(int week_of_year) {
        LOAD_MANAGER.saveParam(TAG_WEEK_OF_YEAR, String.valueOf(week_of_year));
        WEEK_OF_YEAR = week_of_year;
    }

    public static int loadWeek() {
        WEEK_OF_YEAR = Integer.valueOf(LOAD_MANAGER.getStringParam(TAG_WEEK_OF_YEAR));
        return WEEK_OF_YEAR;
    }

    public static long[] getDateDiff(long d1, long d2) {
        long diff = d2 - d1;
        long[] res = new long[]{0, 0, 0, 0};
        res[0] = diff / DAY_MILLIS;
        res[1] = (diff % DAY_MILLIS) / HOURS_MILLIS;
        res[2] = ((diff % DAY_MILLIS) % HOURS_MILLIS) / MINUTE_MILLIS;
        res[3] = (((diff % DAY_MILLIS) % HOURS_MILLIS) % MINUTE_MILLIS) / SECONDS_MILLIS;
        return res;
    }

    public static void initYearWeek() {

        TimeZone.setDefault(TimeZone.getTimeZone("Etc/GMT+4"));
        calendar = new GregorianCalendar(TimeZone.getTimeZone("Etc/GMT+4"));
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        int yearWeek = calendar.get(Calendar.YEAR) * 100 + calendar.get(Calendar.WEEK_OF_YEAR) - 1;
        // System.out.println(yearWeek + ">" + GameSettings.WEEK_OF_YEAR);
        loadWeek();

        if (yearWeek > WEEK_OF_YEAR) {
            GameSettings.setWeek(yearWeek);
            resetWeekRecords();
        }
    }

    public static long[] weekChalengePeriodExpire() {
        calendar.setTime(new Date());
        long d1 = calendar.getTimeInMillis();

        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.WEEK_OF_YEAR, (int) (WEEK_OF_YEAR - (WEEK_OF_YEAR / 100) * 100) + 1);
        calendar.set(Calendar.YEAR, (int) WEEK_OF_YEAR / 100);

        // long[] didf = new long[] {};
        return GameSettings.getDateDiff(d1, calendar.getTimeInMillis());


    }

}
