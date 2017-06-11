package com.easy.player.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.easy.player.R;
import com.easy.player.controller.EasyMediaController;

import java.io.File;
import java.lang.ref.WeakReference;

import io.vov.vitamio.widget.VideoView;

/**
 * Created by malijie on 2017/6/9.
 */

public class FullScreenPlayActivity extends BaseActivity{
    private static final int MSG_UPDATE_CURRENT_TIME = 1;
    private static final int MSG_UPDATE_BATTERY = 2;
    private static final long UPDATE_TIME_FREQUENCE = 60 * 1000;


    private String FILE_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "apk" + File.separator + "test.mkv";


    private VideoView mVideoView = null;
    private MediaControllerHandler mHandler = new MediaControllerHandler(this);
    private EasyMediaController mEasyMediaController = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_play_layout);
        mVideoView = (VideoView) findViewById(R.id.id_video_view);

        mEasyMediaController = new EasyMediaController(this,this,mVideoView);
        mVideoView.setMediaController(mEasyMediaController);
        mVideoView.setVideoPath(FILE_PATH);


        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_CURRENT_TIME,1000);

        registerBroadcastReceiver();
    }

    private  class MediaControllerHandler extends Handler{
        private WeakReference<Context> mContext = null;
        MediaControllerHandler(Context context){
            mContext = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
             switch (msg.what){
                 case MSG_UPDATE_CURRENT_TIME:
                     mEasyMediaController.updateCurrentTime();
                     Message timeMsg = Message.obtain();
                     timeMsg.what = MSG_UPDATE_CURRENT_TIME;
                     sendMessageDelayed(timeMsg,UPDATE_TIME_FREQUENCE);

                     break;

                 case MSG_UPDATE_BATTERY:
                     mEasyMediaController.updateBatteryUI(msg.arg1);
                     break;

             }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        unregisterReceiver(batteryReceiver);

    }


    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){

                Message msg = Message.obtain();
                msg.what = MSG_UPDATE_BATTERY;
                msg.arg1 = getBattery(intent);
                mHandler.sendMessage(msg);
            }
        }
    };

    /**
     * 获取电池电量
     * @param i
     * @return
     */
    private int getBattery(Intent i){
        int level = i.getIntExtra("level",0);
        int scale = i.getIntExtra("scale",0);
        return level * 100 /scale;
    }

    private void registerBroadcastReceiver(){
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver,filter);
    }




}
