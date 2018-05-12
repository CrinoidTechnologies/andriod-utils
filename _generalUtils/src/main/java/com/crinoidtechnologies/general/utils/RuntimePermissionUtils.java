package com.crinoidtechnologies.general.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.crinoidtechnologies.general.R;
import com.crinoidtechnologies.general.template.dialogs.DialogBuilderUtils;
import com.crinoidtechnologies.general.template.dialogs.DialogPositiveButtonListener;

/**
 * Created by ${Vivek} on 11/18/2015 for MessageSaver.Be careful
 */
public class RuntimePermissionUtils {
    public static int checkPermissionStatus(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
    //Manifest.permission.READ_CONTACTS

    public static void requestPermission(final Activity context, final String permission, int explanation, final int requestCode) {

        if (checkPermissionStatus(context, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                SharedPrefUtils.i().writeBoolean(permission.hashCode(), true);

                DialogBuilderUtils.getDialog(context, new DialogPositiveButtonListener() {
                    @Override
                    public void onPositiveButtonClick(DialogInterface dialog, int which) {
                        requestForPermission(context, permission, requestCode);
                    }
                }, R.string.need_permission, explanation, R.string.ok, 0).show();

            } else {

                // No explanation needed, we can request the permission.

                requestForPermission(context, permission, requestCode);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    public static void requestPermissions(final Activity context, final int requestCode, final String... permissions) {

        if (!hasPermissions(context, permissions)) {
            requestForPermission(context, permissions, requestCode);
        }

    }

    private static void requestForPermission(Activity context, String permission, int requestCode) {
        ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);
    }

    private static void requestForPermission(Activity context, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(context, permissions, requestCode);
    }
}

