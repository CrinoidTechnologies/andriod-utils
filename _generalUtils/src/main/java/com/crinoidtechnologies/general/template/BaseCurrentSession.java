package com.crinoidtechnologies.general.template;

import android.util.Log;

import com.crinoidtechnologies.general.utils.DateUtils;
import com.crinoidtechnologies.general.utils.SharedPrefUtils;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * This is singleton class used to keep or provides a medium to access all the data in App. Data can be user profile, any shared preference, temporary items in list.
 */
public abstract class BaseCurrentSession<T> {

    public static SimpleDateFormat dateFormatter;
    public static DecimalFormat priceFormatter = new DecimalFormat("#.0");
    protected static BaseCurrentSession i = null;
    protected T localData;
    protected Class<T> tClass;

    protected BaseCurrentSession(T localData, Class<T> tClass) {
        i = this;
        this.localData = localData;
        this.tClass = tClass;

        String data = SharedPrefUtils.i().readString(SharedPrefUtils.PREF_SAVED_DATA);

        Log.d("TAG", "BaseCurrentSession: " + data);

        if (data.length() > 1) {

            try {
                this.localData = (new Gson().fromJson(data, tClass));
            } catch (Exception e) {
                this.localData = localData;
            }

        }
        dateFormatter = DateUtils.defaultDateFormat;
    }

    public static BaseCurrentSession getI() {
        if (i == null) {
            throw new AssertionError("this i");
        }
        return i;
    }

    public void saveDataLocally() {
        String data = new Gson().toJson(localData, tClass);
        Log.d("TAG", "saveData: " + data);
        SharedPrefUtils.i().writeString(SharedPrefUtils.PREF_SAVED_DATA, data);
    }

    public T getLocalData() {
        return localData;
    }

}
