package com.crinoidtechnologies.general.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.crinoidtechnologies.general.template.BaseApplication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ${Vivek} on 8/21/2015 for MyStore.Be careful
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    public static void writeDataToInternalStorage(String fileName, String data) {

        if (!isFileExistsInInternalStorage(fileName)) {
            createNewFileInInternalStorage(fileName);
        }
        FileOutputStream outputStream;
        try {
            outputStream = BaseApplication.getAppContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean createNewFileInInternalStorage(String fileName) {
        try {
            if (new File(BaseApplication.getAppContext().getFilesDir(), fileName).createNewFile()) {
                Log.d(TAG, " file is no t created ");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return false;
    }

    public static boolean isFileExistsInInternalStorage(String fileName) {
        String directorName = BaseApplication.getAppContext().getFilesDir().getPath();
        Log.d(TAG, "directory name " + directorName);
        return new File(BaseApplication.getAppContext().getFilesDir(), fileName).exists();
    }

    public static String readFileFromInternalStorage(String fileName) {
        String ret = null;
        //File fl = new File(filePath);
        FileInputStream fin = null;
        try {

            fin = BaseApplication.getAppContext().openFileInput(fileName);
            ret = convertStreamToString(fin);
            //Make sure you close all streams.
            Log.e(TAG, "file data is " + ret);
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static long bytesAvailable(boolean isInternal) {
        if (!isInternal && !isExternalStorageWritable()) {
            return 0;
        }
        String path = isInternal ? Environment.getDataDirectory().getPath() : Environment.getExternalStorageDirectory().getPath();
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = 0;
        if (Build.VERSION.SDK_INT >= 18)
            bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        else {
            bytesAvailable = stat.getBlockSize() * stat.getBlockCount();
        }
        return bytesAvailable;
    }

    public static boolean isFileExist(String path) {
        File file = new File(path);
        return file.isFile();
    }

    public static byte[] getByteDataFromFile(String path) {
        File file = new File(path);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static boolean createNewFileInInternalStrorage(String fileName) {
        try {
            if (new File(BaseApplication.getAppContext().getFilesDir(), fileName).createNewFile()) {
                Log.d(TAG, " file is no t created ");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return false;
    }

    public Uri saveImageToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(BaseApplication.getAppContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("temp", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "image_" + System.currentTimeMillis() + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Uri.fromFile(mypath);
    }

}
