package com.crinoidtechnologies.general.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.crinoidtechnologies.general.template.BaseApplication;
import com.crinoidtechnologies.general.R;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ${Vivek} on 9/18/2015 for Chat.Be careful
 */
public class GeneralUtils {
    public static final String COPY_MESSAGE_LABEL = "copy_label";
    private static final String TAG = GeneralUtils.class.getSimpleName();
    private static final int PICTURE_SELECT_REQUEST_CODE = 123;

    public static void makeActivityFullScreen(Activity context) {
        context.requestWindowFeature(Window.FEATURE_NO_TITLE);
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static boolean supportsGooglePlayServices(Activity context) {
//        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) ==
//                ConnectionResult.SUCCESS;
        return false;
    }

    public static int getAppVersionName(Context context) {
        int version = 1;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (info != null) {
                version = info.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static long getTimeInSeconds() {
        return Calendar.getInstance().getTime().getTime() / 1000;
    }

    public static boolean hasNavBar(Context context) {
        Resources resources = context.getResources();
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            return resources.getBoolean(id);
        } else {    // Check for keys
            boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !hasMenuKey && !hasBackKey;
        }
    }

    public static Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    public static int getStatusBarHeight(Context c) {
        int result = 0;
        int resourceId = c.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = c.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            }
        }

        return size;
    }

    private static boolean isTablet(Context c) {
        return (c.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * @param context
     * @return // Configuration.ORIENTATION_PORTRAIT
     */
    public static int getCurrentOrientation(Context context) {
        return context.getResources().getConfiguration().orientation;

    }

    public static void copyTextToClipBoard(Context context, String text) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(ClipData.newPlainText(COPY_MESSAGE_LABEL, text));
    }

    public static boolean isPlayServicePresent(Context context) {
//        int resultCode = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(context);
//        return resultCode == ConnectionResult.SUCCESS;
        return false;
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static float getDensityFactor(Context context) {
        if (context == null) {
            return 1;
        }

        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return metrics.density;

    }

    public static String getForegroundApp(Context context) {
        String name = "";
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
//            Log.d(TAG, "getForegroundApp() called with: " + "context = [" + am + "]");
//             for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
//                 Log.d(TAG, "active process is " + processInfo.pkgList[0]);
//             }
            if (runningProcesses.get(0).importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return runningProcesses.get(0).pkgList[0];
            }
            // }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            return (componentInfo.getPackageName());
        }
        return name;
    }

    public static boolean isAccessibilitySettingsOn(Context mContext, Class classType) {
        String path = classType.getCanonicalName();
        return isAccessibilitySettingsOn(mContext, path);
    }

    /**
     * @param mContext
     * @param servicePath "com.goonj.goonjcare.services.MyAccessibilityService";
     * @return
     */
    public static boolean isAccessibilitySettingsOn(Context mContext, String servicePath) {
        int accessibilityEnabled = 0;
        final String service = mContext.getPackageName() + "/" + servicePath;
        // final String service = "com.curlymustachestudios.texa/com.curlymustachestudios.texa.service.MyAccessibilityService";
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            //  Log.v(TAG, "***ACCESSIBILIY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();

                    //  Log.v(TAG, "-------------- > accessabilityService :: " + accessabilityService);
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        //  Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILIY IS DISABLED***");
        }

        return accessibilityFound;
    }

    public static void openUrl(Context context, String url) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.no_browser, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static void openUrlFromService(Context context, String url) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.no_browser, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static void openImageInGallery(Context context, String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (!path.contains("file://")) {
            path = "file://" + path;
        }
        intent.setDataAndType(Uri.parse(path), "image/*");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.no_app, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static boolean internetAvailable(Context appContext) {
        ConnectivityManager cm =
                (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String getAppNameFromPackageName(Context context, String pckName) {
        final PackageManager pm = context.getApplicationContext().getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(pckName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai) : pckName);
    }

    public static boolean showTouchEffectLikeButtonOnImage(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageView view = (ImageView) v;
                //overlay is black with transparency of 0x77 (119)
                view.getDrawable().setColorFilter(0xaa000000, PorterDuff.Mode.SRC_ATOP);
                view.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                ImageView view = (ImageView) v;
                //clear the overlay
                view.getDrawable().clearColorFilter();
                view.invalidate();
                break;
            }
        }
        return false;
    }

    public static void getInstalledPackageList(final Context context, final RequiredListListener listListener) {

        new AsyncTask<Void, Integer, List<ApplicationInfo>>() {

            @Override
            protected List<ApplicationInfo> doInBackground(Void... params) {
                final PackageManager pm = context.getPackageManager();
                //get a list of installed apps.
                List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

//                for (ApplicationInfo packageInfo : packages) {
//                    Log.d(TAG, "Installed package :" + packageInfo.packageName);
//                    Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
//                    Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
//                }
                return packages;
            }

            @Override
            protected void onPostExecute(List<ApplicationInfo> objects) {
                if (listListener != null) {
                    listListener.onResultFound(objects);
                }
            }
        }.execute();

    }

    public static boolean isAppPresentOnDevice(String packageName, List<ApplicationInfo> list) {
        for (ApplicationInfo packageInfo : list) {
            Log.d(TAG, "Installed package :" + packageInfo.packageName);
            if (packageName.equals(packageInfo.packageName)) {
                return true;
            }
        }
        return false;
        //  Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
        //  Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));

    }

    public static void startService(Context context, Class<?> service) {
        if (!isServiceRunning(context, service)) {
            Intent intent = new Intent(context, service);
            context.startService(intent);
        }
    }

    public static void addClipboardChangeListener(Context context, ClipboardManager.OnPrimaryClipChangedListener onPrimaryClipChangedListener) {
        final ClipboardManager mClipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboard.addPrimaryClipChangedListener(onPrimaryClipChangedListener);

    }

    public static void removeClipChangeListner(Context context, ClipboardManager.OnPrimaryClipChangedListener listener) {
        final ClipboardManager mClipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboard.removePrimaryClipChangedListener(listener);
    }

    public static void startMessagingApp(Context context, String data) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        sendIntent.putExtra("sms_body", data);
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sendIntent);
    }

    public static void changeColorOfMenuItems(Menu menu, int colorCode) {
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(BaseApplication.getAppContext().getResources().getColor(colorCode), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public static void changeColorOfButtonBackground(View button, int colorCode) {
        button.getBackground().setColorFilter(BaseApplication.getAppContext().getResources().getColor(colorCode), PorterDuff.Mode.MULTIPLY);

    }

    public static void makeIntentAsClearHistory(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    public static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    public static void makeIntentAsSingleActivity(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public static ArrayAdapter<String> getAutoCompleteAdapter(Context context, List<String> items) {
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, items);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        return dataAdapter;
    }

    public static ArrayAdapter<String> getSpinnerAdapter(Context context, String[] items) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return dataAdapter;
    }

    public static void hideAppIcon(Class mainActivityClass, Context mainActivity, boolean unhide) {
        PackageManager p = mainActivity.getPackageManager();
        ComponentName componentName = new ComponentName(mainActivity, mainActivityClass); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
        if (unhide)
            p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        else
            p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

    }

    public static boolean isEmailValid(String email) {
        return email != null && email.length() > 0 && isValidEmail(email);
    }

    public static void setViewHeight(View view, float v) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int) v;
        view.setLayoutParams(params);
    }

    public static void setViewHeightToMatchParent(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(params);
    }

    public static String formatDoubleToPlace(double number) {
        return new DecimalFormat("#0.00").format(number);
    }

    public static String getFirstLetterCatiliseString(String str) {
        if (!TextUtils.isEmpty(str)) {
            return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        }
        return str;
    }

    public interface RequiredListListener {
        void onResultFound(List<ApplicationInfo> list);
    }

}
