package com.romain.mathieu.go4lunch.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import static com.romain.mathieu.go4lunch.controller.activity.DetailsRestaurantActivity.hashMapLike;

/**
 * Created by Romain on 06/02/2018.
 */

public class SharedPreferencesUtils {

    static final String MY_FILE = "MySharedPreference.xml";
    //    static final String KEY_MOOD = "KEY_INT";
    static final String KEY_LIST = "KEY_HASHMAP_LIKE";

    // ------------------------------
    // - SAVE AN PRIMITIVE VARIABLE -
    // ------------------------------


//    public static void saveMood(Context context, int MyVariableInt) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_FILE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt(KEY_INT, MyVariableInt);
//        editor.apply();
//    }
//
//    public static int getMood(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_FILE, Context.MODE_PRIVATE);
//        return sharedPreferences.getInt(KEY_INT, 0);
//    }
//
//    static void removeMood(Context context, String prefsName, String key) {
//        SharedPreferences preferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.remove(key);
//        editor.apply();
//    }
//
//    public static boolean containsMood(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_FILE, Context.MODE_PRIVATE);
//        return sharedPreferences.contains(KEY_INT);
//    }


    // ----------------------------
    // - SAVE AN OBJECT WITH GSON -
    // ----------------------------


    public static void saveHashMap(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(hashMapLike);
        editor.putString(KEY_LIST, json);
        editor.apply();
    }

    public static HashMap<String, Boolean> getHashMap(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_LIST, null);
        Type type = new TypeToken<HashMap<String, Boolean>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static boolean containsHashMap(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.contains(KEY_LIST);
    }


}
