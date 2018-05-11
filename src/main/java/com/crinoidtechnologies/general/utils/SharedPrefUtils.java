package com.crinoidtechnologies.general.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ${Vivek} on 9/19/2015 for Chat.Be careful
 */
public class SharedPrefUtils {

    // keys....
    public static final int PREF_IS_FIRST_TIME = 0;
    public static final int PREF_TOUR_COMPLETE = 1;
    public static final int PREF_SAVED_DATA = 99;
    public static final int PREF_USER_SIGN_UP = 6;

    //public static final String PREF_ = "";
    //public static final String PREF_ = "";

    private static SharedPrefUtils i;

    protected String fileName;
    protected SharedPreferences preferences;

    protected SharedPrefUtils(Context context) {
        i = this;
        fileName = context.getPackageName() + ".prefFile";
        preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public static SharedPrefUtils i() {
        if (i == null) {
            throw new AssertionError("Before using it initialise it by creating object with context");
        }

        return i;
    }

    public static void initiate(Context context) {
        if (i == null) {
            new SharedPrefUtils(context);
        }
    }

    public void writeBoolean(Integer key, boolean value) {
        preferences.edit().putBoolean(key.toString(), value).apply();
    }

    public void writeInteger(Integer key, int value) {
        preferences.edit().putInt(key.toString(), value).apply();
    }

    public void writeString(Integer key, String value) {
        preferences.edit().putString(key.toString(), value).apply();
    }

    public boolean readBoolean(Integer key) {

        switch (key) {
            case PREF_IS_FIRST_TIME:
                return preferences.getBoolean(key.toString(), true);
        }
        return preferences.getBoolean(key.toString(), false);
    }

    public int readInteger(Integer key) {
        switch (key) {

            case -1: {
                return preferences.getInt(key.toString(), 2);
            }

        }
        return preferences.getInt(key.toString(), 0);

    }

    public long readLong(Integer key) {
        switch (key) {

            case -1: {
                return preferences.getLong(key.toString(), 2);
            }

        }
        return preferences.getLong(key.toString(), 0);

    }

    public void writeLong(Integer key, long value) {
        preferences.edit().putLong(key.toString(), value).apply();
    }

    public String readString(Integer key) {
        switch (key) {
            case PREF_SAVED_DATA:
                return preferences.getString(key.toString(), "");

        }
        return preferences.getString(key.toString(), "");
    }

}
