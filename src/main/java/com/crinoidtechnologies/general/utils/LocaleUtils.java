package com.crinoidtechnologies.general.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by shubham on 12/15/2016.
 */

public class LocaleUtils {
    public static boolean setLocale(String lang, Context context, boolean doRefresh) {
        Locale myLocale = null;
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!lang.equals(conf.getLocales().get(0).getLanguage())) {
                myLocale = new Locale(lang);
            }
        } else {
            if (!lang.equals(conf.locale.getLanguage())) {
                myLocale = new Locale(lang);
            }
        }
        if (myLocale != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                conf.setLocale(myLocale);
            } else {
                conf.locale = myLocale;
            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                context = context.createConfigurationContext(conf);
//            } else {
//                context.getResources().updateConfiguration(conf, dm);
//            }
            res.updateConfiguration(conf, dm);
        }
        if (doRefresh) {
            if (context instanceof Activity) {
                ((Activity) context).recreate();
                // ((Activity) activity).getActionBar().setTitle(((Activity) activity).getActionBar().getTitle());
            }
            return true;
        }

        return false;
    }

    public static boolean setLocaleAndRefresh(String lang, Context activity) {
        return setLocale(lang, activity, true);

    }
}
