package com.crinoidtechnologies.general.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import com.crinoidtechnologies.general.R;

/**
 * Created by ${Vivek} on 11/6/2015 for MessageSaver.Be careful
 */
public class AppRater {
    private final static String APP_RATE_TITLE = "Rate";//Min number of days
    private final static String APP_RATE_MESSAGE = "Rate";//Min number of days
    private final static String APP_REMIN_ME_LATER = "Rate";//Min number of days

    private final static int DAYS_UNTIL_PROMPT = 2;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 4;//Min number of launches
    private final static int REMIND_DAYS_UNTIL_PROMPT = 0;//Min number of launches
    private final static int REMIND_LAUNCHES_UNTIL_PROMPT = 4;//Min number of launches
    private static String APP_TITLE;// App Name
    private static String APP_PNAME;// Package Name

    public static void app_launched(Context mContext) {
        APP_TITLE = mContext.getString(R.string.app_name);
        APP_PNAME = mContext.getPackageName();
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        boolean isREmindMe = prefs.getBoolean("remind_me", false);
        // Wait at least n days before opening
        if (launch_count >= (isREmindMe ? REMIND_LAUNCHES_UNTIL_PROMPT : LAUNCHES_UNTIL_PROMPT)) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                editor.putLong("launch_count", 0);
                showRateDialog(mContext, editor);
            }
        }

        editor.apply();
    }

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle(APP_RATE_TITLE + mContext.getString(R.string.app_name))
                .setMessage(R.string.app_rate_message)
                .setPositiveButton(APP_RATE_TITLE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ThirdPartyAppOpenerUtils.openRateUsOnPlayStore(mContext);
                        editor.putBoolean("dontshowagain", true);
                        editor.apply();
                        // Answers.getAppContext().logRating(new RatingEvent());

                    }
                })

                .setNegativeButton(R.string.remind_me_later, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putBoolean("remind_me", true);
                        editor.apply();
                        //Answers.getAppContext().logCustom(new CustomEvent("Rating Cancel"));

                    }
                });
        builder.create().show();
    }
}
