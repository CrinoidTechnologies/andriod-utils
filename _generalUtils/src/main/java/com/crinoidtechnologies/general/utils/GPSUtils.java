package com.crinoidtechnologies.general.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

/**
 * Created by ${Vivek} on 2/20/2016 for MissApp.Be careful
 */
public class GPSUtils {
    public static String gpsDialogTitle = "Enable Location";
    public static String gpsDialogMessage = "GPS is not enabled. Do you want to go to settings menu?";

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public static void showGPSSettingsAlert(final Context context, boolean isShowCancel) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(gpsDialogTitle);

        // Setting Dialog Message
        alertDialog.setMessage(gpsDialogMessage);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        if (isShowCancel) {
            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        // Showing Alert Message
        alertDialog.show();
    }

    public static boolean isGpsEnabled(Context context) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
