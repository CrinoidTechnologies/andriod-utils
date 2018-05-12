package com.crinoidtechnologies.general.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.crinoidtechnologies.general.template.BaseApplication;

/**
 * Created by ${Vivek} on 12/13/2015 for MessageSaver.Be careful
 */
public class ToastUtils {

    public static void showToast(Context context, int rID, int length) {
        Toast.makeText(context, rID, length).show();
    }

    public static void showToast(int rID, int length) {
        Toast.makeText(BaseApplication.getAppContext(), rID, length).show();
    }

    public static void showLongToast(int rID) {
        showToast(rID, Toast.LENGTH_LONG);
    }

    public static void showShortToast(int rID) {
        showToast(rID, Toast.LENGTH_SHORT);
    }

    // for strings..

    public static void showToast(Context context, String rID, int length) {
        Toast.makeText(context, rID, length).show();
    }

    public static void showToast(String rID, int length) {

        Toast.makeText(BaseApplication.getAppContext(), rID, length).show();
    }

    public static void showLongToast(String rID) {
        showToast(rID, Toast.LENGTH_LONG);
    }

    public static void showShortToast(String rID) {
        showToast(rID, Toast.LENGTH_SHORT);
    }

    /**
     * @param context
     * @param rID
     * @param length
     * @param offsetY + in case above center
     */

    public static void showToastWithVerticalOffset(Context context, int rID, int length, int offsetY) {
        Toast toast = Toast.makeText(context, rID, length);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, -(int) GeneralUtils.convertDpToPixel(offsetY, context));
        toast.show();
    }

}


