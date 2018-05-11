package com.crinoidtechnologies.general.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by ${Vivek} on 2/29/2016 for RadioTesting.Be careful
 */
public class ServiceUtils {
    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
