package com.example.android_ex2.Utillities;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesManager {
    private static volatile SharePreferencesManager instance = null;
    private SharedPreferences sharedPref;
    private static final String SP_FILE = "SP_FILE";


    private SharePreferencesManager(Context context) {
        sharedPref = context.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }

    public static SharePreferencesManager getInstance() {
        return instance;
    }

    public static SharePreferencesManager init(Context context) {
        if (instance == null) {
            synchronized (SharePreferencesManager.class) {
                if (instance == null) {
                    instance = new SharePreferencesManager(context);
                }
            }
        }
        return getInstance();
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defValue) {
        return sharedPref.getString(key, defValue);
    }
}
