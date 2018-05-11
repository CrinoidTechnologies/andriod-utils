package com.crinoidtechnologies.general.utils;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by ${Vivek} on 3/3/2016 for RadioTesting.Be careful
 */

public abstract class AsyncTaskWithListener<T, V> extends AsyncTask<T, Integer, V> {

    private TaskCompleteHandler<V> completeHandler;

    public AsyncTaskWithListener(TaskCompleteHandler<V> completeHandler) {
        Log.d("TAG", "ServerRequest: constructor");
        this.completeHandler = completeHandler;
    }

    @Override
    protected void onPostExecute(V result) {
        super.onPostExecute(result);
        if (completeHandler != null) {
            completeHandler.onRequestComplete(result);
        }
    }

    public interface TaskCompleteHandler<V> {
        void onRequestComplete(V response);
    }
}