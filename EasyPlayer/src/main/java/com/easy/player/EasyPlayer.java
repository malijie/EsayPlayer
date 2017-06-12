package com.easy.player;

import android.app.Application;
import android.content.Context;

import io.vov.vitamio.Vitamio;

/**
 * Created by malijie on 2017/6/7.
 */

public class EasyPlayer extends Application{
    public static Context sContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        Vitamio.isInitialized(sContext);
    }
}
