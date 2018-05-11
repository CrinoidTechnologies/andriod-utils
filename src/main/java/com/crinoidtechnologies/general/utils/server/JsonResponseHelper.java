package com.crinoidtechnologies.general.utils.server;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Vivek} on 5/5/2016 for Avante.Be careful
 */
public class JsonResponseHelper {

    public static JsonObject getJsonObject(String data) {
        return new JsonParser().parse(data).getAsJsonObject();
    }

    public static <T> List<T> getJsonObjectArray(String data, Class<T> tClass) {
        List<T> items = new ArrayList<>();
        if (data.length() > 0) {
            JsonArray array = new JsonParser().parse(data).getAsJsonArray();
            Gson gson = new Gson();
            for (int i = 0; i < array.size(); i++) {
                items.add(gson.fromJson(array.get(i), tClass));
            }
        }
        return items;
    }

    public static <T> List<T> getJsonObjectArray(JsonElement data, Class<T> tClass) {
        if (data == null) {
            return new ArrayList<>();
        }
        JsonArray array = data.getAsJsonArray();
        List<T> items = new ArrayList<>(array.size());
        Gson gson = new Gson();
        for (int i = 0; i < array.size(); i++) {
            Log.d("TAG", "getJsonObjectArray: " + tClass + "   " + array.get(i));
            items.add(gson.fromJson(array.get(i), tClass));
        }
        return items;
    }

    public static <T> List<T> getJsonObjectArray(JsonArray array, Class<T> tClass) {
        List<T> items = new ArrayList<>(array.size());
        Gson gson = new Gson();
        for (int i = 0; i < array.size(); i++) {
            Log.d("TAG", "getJsonObjectArray: " + tClass + "   " + array.get(i));
            items.add(gson.fromJson(array.get(i), tClass));
        }
        return items;
    }

    public static boolean hasKey(JsonObject object, String key) {
        return object.get(key) != null;
    }

    public static boolean hasKey(String data, String key) {

        return getJsonObject(data).get(key) != null;
    }

    public static boolean isError(String data) {
        return hasKey(data, "error");
    }

    public static <T> T getObjectFrom(String json, Class<T> classOfT) {
        try {
            T obj = new Gson().fromJson(json, classOfT);
            return obj;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getResponseObject(Object data, Class<T> tClass) {
        return new Gson().fromJson(getJsonObject((String) data).get("responseData"), tClass);
    }
}
