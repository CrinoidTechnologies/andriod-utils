package com.crinoidtechnologies.general.template.activities;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.crinoidtechnologies.general.template.BaseApplication;
import com.crinoidtechnologies.general.R;
import com.crinoidtechnologies.general.utils.RuntimePermissionUtils;
import com.crinoidtechnologies.general.template.dialogs.DialogBuilderUtils;
import com.crinoidtechnologies.general.template.dialogs.DialogPositiveButtonListener;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BasePermissionActivity extends BaseActivity {

    protected int currentRequestCode = 0;

    public BasePermissionActivity() {
        // Required empty public constructor
    }

    /**
     * @param permissions Manifest.permission.RECORD_AUDIO
     * @return
     */
    protected boolean hasPermission(String... permissions) {

        for (String permission : permissions) {
            if (!RuntimePermissionUtils.hasPermission(BaseApplication.getAppContext(), permission)) {
                return false;
            }
        }

        return true;
    }

    protected boolean askForPermission(int requestCode, String... permissions) {
        if (!hasPermission(permissions)) {
            if (currentRequestCode != requestCode) {
                RuntimePermissionUtils.requestPermissions(this, requestCode, permissions);
                currentRequestCode = requestCode;
            }
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d("TAG", "onRequestPermissionsResult() called with: " + "requestCode = [" + requestCode + "], permissions = [" + permissions.length + "], grantResults = [" + grantResults.length + "]");

        currentRequestCode = 0;

        int grantPermissionLength = 0;

        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_GRANTED) {
                grantPermissionLength++;
            }
        }

        if (permissions.length == grantPermissionLength) {
            onPermissionGranted(requestCode);
        } else {
            onPermissionDenied(requestCode);
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
        }

        // other 'case' lines to check for other
        // permissions this app might request
    }

    protected void onPermissionDenied(int requestCode) {

    }

    protected void onPermissionGranted(int requestCode) {

    }

    protected void showPermissionExplainationExample(final int requestCode, int explanation, final String permission) {
        DialogBuilderUtils.getDialog(this, new DialogPositiveButtonListener() {
            @Override
            public void onPositiveButtonClick(DialogInterface dialog, int which) {
                Log.d("TAG", "onPositiveButtonClick() called with: " + "dialog = [" + dialog + "], which = [" + which + "]");
                askForPermission(requestCode, permission);
            }
        }, R.string.need_permission, explanation, R.string.ok, 0).show();
    }

}
