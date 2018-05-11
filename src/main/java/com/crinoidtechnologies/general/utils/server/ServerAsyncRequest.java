package com.crinoidtechnologies.general.utils.server;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by ${Vivek} on 3/3/2016 for RadioTesting.Be careful
 */

public class ServerAsyncRequest extends AsyncTask<String, String, String> {

    private iRequestCompleteHandler completeHandler;

    public ServerAsyncRequest(iRequestCompleteHandler completeHandler) {
        Log.d("TAG", "ServerRequest: constructor");
        this.completeHandler = completeHandler;
    }

    @Override
    protected String doInBackground(String... uri) {
        String response = null;
        try {
            URL url = new URL(uri[0]);
            // URLConnection conn = url.openConnection();
            InputStream is = url.openStream();//.getInputStream();

            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            response = sb.toString();
            inputStreamReader.close();
            is.close();
            //  Log.d("TAG", "getAppData() called with: " + response);
        } catch (IOException e) {
            //TODO Handle problems..
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Do anything with response..
        if (completeHandler != null) {
            completeHandler.onRequestComplete(result);
        }
    }

    public interface iRequestCompleteHandler {
        void onRequestComplete(String response);
    }
}