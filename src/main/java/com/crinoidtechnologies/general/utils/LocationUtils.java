package com.crinoidtechnologies.general.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.crinoidtechnologies.general.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by ${Vivek} on 8/4/2016 for WifiWeb.Be careful
 */

public class LocationUtils {

    public static void registerForNetworkLocationUpdate(Context context, LocationListener listener) {

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, .1f, listener);
        }

    }

    public static LocationManager getLocationManager(Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public static boolean isGpsEnabled(final Activity context, boolean showDialog) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (showDialog) {

            if (!gps_enabled && !network_enabled) {
                // notify user
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
                dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(myIntent);
                        //get gps
                    }
                });
                dialog.show();
            }
        }
        return gps_enabled || network_enabled;
    }

    public static boolean isBetterLocation(Location location, Location currentBestLocation) {
//        return true;
        //TODO: temporary commenting
//
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > 100;
        boolean isSignificantlyOlder = timeDelta < -100;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public static void getAddressFromLocation(Activity activity, final AddressFetchListner completeHandler, Location lastKnownLocation) {
        final Location location = lastKnownLocation;
        if (lastKnownLocation == null) {
            return;
        }

        new GeoAddressTask(activity, new AsyncTaskWithListener.TaskCompleteHandler<List<Address>>() {
            @Override
            public void onRequestComplete(List<Address> response) {
                if (response != null && response.size() > 0) {
                    completeHandler.onAddressFetch(location, response.get(0));
                } else {
                    completeHandler.onAddressFetch(location, null);
                }
            }
        }).execute(lastKnownLocation);
    }

    public static String getAddresForInString(Address address) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            sb.append(address.getAddressLine(i)).append("\n");
        }
        sb.append(address.getLocality()).append("\n");
        sb.append(address.getPostalCode()).append("\n");
        sb.append(address.getCountryName());
        return sb.toString();
    }

    public static String getHalfAddresForInString(Address address) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < address.getMaxAddressLineIndex() - 1; i++) {
            sb.append(address.getAddressLine(i)).append("\n");
        }
        // sb.append(address.getLocality()).append("\n");
        // sb.append(address.getPostalCode()).append("\n");
        //  sb.append(address.getCountryName());
        return sb.toString();
    }

    public static int runningTimeBetweenTwoLocation(Location source, Location destination) {

        float distanceInMeters = source.distanceTo(destination);

        int walkSpeedPerMinute = 220;
        return (int) distanceInMeters / walkSpeedPerMinute;
    }

    public static Location getLocationFromCoordinate(double lat, double lng) {
        Location location1 = new Location("");
        location1.setLatitude(lat);
        location1.setLongitude(lng);
        return location1;
    }

    public static Location getLocationFromAddress(Address address) {
        if (address != null) {
            return getLocationFromCoordinate(address.getLatitude(), address.getLongitude());
        }
        return new Location("");
    }

    public interface AddressFetchListner {
        void onAddressFetch(Location location, Address address);
    }

    public static class GeoAddressTask extends AsyncTaskWithListener<Location, List<Address>> {

        private Context context;

        public GeoAddressTask(Context context, TaskCompleteHandler<List<Address>> completeHandler) {
            super(completeHandler);
            this.context = context;
        }

        @Override
        protected List<Address> doInBackground(Location... params) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            if (params.length > 0) {
                try {
                    return geocoder.getFromLocation(params[0].getLatitude(), params[0].getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

}
