package com.example.android_ex2;

import android.app.Application;

import com.example.android_ex2.Utillities.SharePreferencesManager;
import com.example.android_ex2.Utillities.SignalManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharePreferencesManager.init(this);
        SignalManager.init(this);
    }
}
