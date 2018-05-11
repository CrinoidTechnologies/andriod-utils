package com.crinoidtechnologies.general.template;

import android.util.Log;

import com.crinoidtechnologies.general.utils.FileUtils;
import com.google.gson.Gson;

/**
 * this class is used to get all data related applists, it also store data to file.
 * in order to save data to local file put parameter in StaticData and initialise it in constructor in case of first time class
 * then manager will handle everything.
 */
public abstract class BaseStaticDataManager<T> {

    protected static final String TAG = BaseStaticDataManager.class.getSimpleName();
    protected static final String FILE_NAME = "sData.txt";

    public T sData;
    public Class<T> classType;

    protected BaseStaticDataManager() {

        setDataAndClass();

        if (FileUtils.isFileExistsInInternalStorage(FILE_NAME)) {// load data from file
            Gson gson = new Gson();
            sData = gson.fromJson(FileUtils.readFileFromInternalStorage(FILE_NAME), classType);
        }
    }

    /**
     * set class type and default sData value
     */
    protected abstract void setDataAndClass();

    public void saveDataOnFile() {
        Gson gson = new Gson();
        String data = gson.toJson(sData, classType);
        Log.e(TAG, "saveDataOnFile " + data);
        FileUtils.writeDataToInternalStorage(FILE_NAME, data);
    }

    public static class DataSyncRequest {
        public int requestType;
        public int requestSerialNumber;
        public String data;

        public DataSyncRequest(int requestType, int requestSerialNumber, String data) {
            this.requestType = requestType;
            this.requestSerialNumber = requestSerialNumber;
            this.data = data;
        }
    }
}
