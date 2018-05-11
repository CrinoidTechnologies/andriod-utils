package com.crinoidtechnologies.general.utils;

import android.content.Context;

public class
DeviceInfoUtils {

    private static final String DEVICE = "Device: ";
    private static final String SDK_VERSION = "SDK version: ";
    private static final String MODEL = "Model: ";
    private static final String APP_VERSION = "Build version: ";
    private static final String NEW_LINE = "\n";
    private static final String DIVIDER_STRING = "----------";

    public static StringBuilder getDeviseInfoForFeedback(Context context) {
        StringBuilder infoStringBuilder = new StringBuilder();
//        infoStringBuilder.append(DEVICE).append(android.os.Build.DEVICE);
//        infoStringBuilder.append(NEW_LINE);
        infoStringBuilder.append(SDK_VERSION).append(android.os.Build.VERSION.SDK);
        infoStringBuilder.append(NEW_LINE);
        infoStringBuilder.append(MODEL).append(android.os.Build.MODEL);
        infoStringBuilder.append(NEW_LINE);
        infoStringBuilder.append(APP_VERSION).append(GeneralUtils.getAppVersionName(context));
        infoStringBuilder.append(NEW_LINE);
        infoStringBuilder.append(DIVIDER_STRING);
        infoStringBuilder.append(NEW_LINE);
        return infoStringBuilder;
    }
}