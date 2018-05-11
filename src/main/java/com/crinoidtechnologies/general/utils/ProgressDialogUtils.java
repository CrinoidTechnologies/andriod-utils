package com.crinoidtechnologies.general.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.crinoidtechnologies.general.template.BaseApplication;

/**
 * Created by ${Vivek} on 5/5/2016 for Avante.Be careful
 */
public class ProgressDialogUtils {

    /**
     * @param context
     * @param message
     * @param progressStyle   {@link ProgressDialog.STYLE_HORIZONTAL}
     * @param isIndeterminate
     * @return
     */
    public static ProgressDialog getProgressDialog(Context context, String message, int progressStyle, boolean isIndeterminate) {
        ProgressDialog progress = new ProgressDialog(context);
        if (null != message && !message.isEmpty()) {
            progress.setMessage(message);
        }
        progress.setProgressStyle(progressStyle);
        progress.setIndeterminate(isIndeterminate);
        return progress;
    }

    public static ProgressDialog getProgressDialog(String message) {
        return getProgressDialog(BaseApplication.getAppContext(), message);
    }

    public static ProgressDialog getProgressDialog(Context context) {
        return getProgressDialog(context, "");
    }

    public static ProgressDialog getProgressDialog(Context context, String message) {
        return getProgressDialog(context, message, ProgressDialog.STYLE_SPINNER, true);
    }

    public static ProgressDialog getForceProgressDialog(Context context, String message) {
        ProgressDialog dialog = getProgressDialog(context, message, ProgressDialog.STYLE_SPINNER, true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }

}

