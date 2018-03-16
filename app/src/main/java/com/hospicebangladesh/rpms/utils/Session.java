package com.hospicebangladesh.rpms.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Tushar on 1/27/2018.
 */

public class Session {
    private static final String PREFS_NAME = "pms";


    public static final String KEY_INIT = "init";

    public static final String user_id = "user_id";
    public static final String mobile = "mobile";
    public static final String password = "password";
    public static final String status = "status";
    public static final String message = "message";

    public static final String name = "name";


    public static void savePreference(Context context,SessionManager sessionManager) {
        savePreference(context, user_id, "" + sessionManager.getUserId());
        savePreference(context, mobile, sessionManager.getMobile());
        savePreference(context, password, sessionManager.getPassword());
        savePreference(context, status, sessionManager.getStatus());
        savePreference(context, message, sessionManager.getMessage());

    }

    public static void savePreference(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getPreference(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(key, null);

    }


    public static void clearPreference(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();

    }

    public static String getPrefsName() {
        return PREFS_NAME;
    }

}
