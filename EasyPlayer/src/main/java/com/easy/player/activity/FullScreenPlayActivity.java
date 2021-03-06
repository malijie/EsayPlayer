package com.easy.player.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.easy.player.R;
import com.easy.player.controller.EasyMediaController;
import com.easy.player.entity.POMedia;
import com.easy.player.plugin.PluginBrightness;
import com.easy.player.plugin.PluginFastBack;
import com.easy.player.plugin.PluginFastForward;
import com.easy.player.plugin.PluginVolume;
import com.easy.player.utils.Utils;

import java.lang.ref.WeakReference;

import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.VideoView;

import static io.vov.vitamio.MediaPlayer.VIDEOQUALITY_HIGH;

/**
 * Created by malijie on 2017/6/9.
 */

public class FullScreenPlayActivity extends BaseActivity{
    private static final String TAG = FullScreenPlayActivity.class.getSimpleName();
    private static final int MSG_UPDATE_CURRENT_TIME = 1;
    private static final int MSG_UPDATE_BATTERY = 2;
    private static final int MSG_HIDE_PLUGIN_UI = 3;

    private static final long UPDATE_TIME_FREQUENCE = 60 * 1000;
    private GestureDetector mGestureDetector = null;

    private VideoView mVideoView = null;

    private MediaControllerHandler mHandler = new MediaControllerHandler(this);
    private EasyMediaController mEasyMediaController = null;
    private int mScreenWidth;

    private PluginBrightness mPluginBrightness = null;
    private PluginVolume mPluginVolume = null;
    private PluginFastBack mPluginBack = null;
    private PluginFastForward mPluginFastForward = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.mlj(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_play_layout);

        initViews();
        initData();

    }

    @Override
    public void initViews() {
        POMedia poMedia = (POMedia) getIntent().getSerializableExtra("po_media");

        mVideoView = (VideoView) findViewById(R.id.id_fullscreen_video_view);

        mEasyMediaController = new EasyMediaController(this,this,mVideoView);
        mVideoView.setVideoPath(poMedia.path);
        mVideoView.setMediaController(mEasyMediaController);
        mVideoView.setVideoQuality(VIDEOQUALITY_HIGH);
        mVideoView.requestFocus();
        initPlugins();


    }

    private void initPlugins(){
        mPluginBrightness = PluginBrightness.getInstance(this);
        mPluginVolume = PluginVolume.getInstance(this);
        mPluginFastForward = PluginFastForward.getInstance(this,mVideoView);
        mPluginBack =  PluginFastBack.getInstance(this,mVideoView);
    }

    @Override
    public void initData() {
        mScreenWidth = Utils.getWindowWidth();

        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_CURRENT_TIME,1000);
        registerBroadcastReceiver();
        mGestureDetector = new GestureDetector(this,new MyGestureListener());
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

                 case MSG_HIDE_PLUGIN_UI:
                     mPluginBrightness.hide();
                     mPluginVolume.hide();
                     mPluginFastForward.hide();
                     mPluginBack.hide();
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


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;
        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                clearUIState();
                break;
        }
        return super.onTouchEvent(event);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.mlj("e1.x=" + e1.getX() + ",e2.x=" + e2.getX()+ "e1.y=" + e1.getY() + ",e2.y=" + e2.getY());
            int x = (int) e1.getX();
            int y = (int) e1.getY();
            int newX = (int) e2.getX();
            int newY = (int) e2.getY();

            int deltaX = x-newX;
            int deltaY = y-newY;
            if(newX>mScreenWidth/2 && Math.abs(deltaY)>Math.abs(deltaX)){
                Log.mlj("=====调节亮度====");
                onBrightnessSlide(deltaY);

            }else if(newX<mScreenWidth/2 && Math.abs(deltaY)>Math.abs(deltaX)){
                //左半屏，调节音量
                Log.mlj("=====调节音量====");
                onVolumeSlide(deltaY);


            }else if(Math.abs(deltaX)>Math.abs(deltaY) && deltaX>40){
                //快退
                Log.mlj("=====快退====");
                onVideoBackSlide(deltaX);


            }else if(Math.abs(deltaX)>Math.abs(deltaY) && deltaX<-40){
                //快退
                Log.mlj("=====快进====");
                onVideoForwardSlide(deltaX);

            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }


    }

    private void clearUIState(){
        mHandler.sendEmptyMessage(MSG_HIDE_PLUGIN_UI);

    }

    private void onBrightnessSlide(int deltaY){
        mPluginBrightness.onBrightnessSlide(deltaY);
    }

    private void onVolumeSlide(int deltaY){
        mPluginVolume.onVolumeSlide(deltaY);
    }

    private void onVideoForwardSlide(int deltaX){
        mPluginFastForward.onForwardSlide(deltaX);
    }

    private void onVideoBackSlide(int deltaX){
        mPluginBack.onBackSlide(deltaX);
    }


}
