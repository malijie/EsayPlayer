package com.easy.player.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.easy.player.config.Profile;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.vov.vitamio.utils.Log;

/**
 * Created by malijie on 2017/7/10.
 */

public class MediaScanService extends Service implements Runnable{
    private String mScanDirectory;
    private ConcurrentHashMap<String,String> mScanMap = new ConcurrentHashMap<String, String>();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mScanDirectory = intent.getStringExtra(Profile.getScanDirectoryKey());
Log.mlj("mScanDirectory==" + mScanDirectory);
        new Thread().start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void run() {
        scan();
    }

    private void scan(){
        if (!mScanMap.containsKey(mScanDirectory)){
            mScanMap.put(mScanDirectory, "");
        }
    }
}
