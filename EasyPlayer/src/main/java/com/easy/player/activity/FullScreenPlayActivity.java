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
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easy.player.R;
import com.easy.player.controller.EasyMediaController;
import com.easy.player.utils.Utils;

import java.io.File;
import java.lang.ref.WeakReference;

import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.VideoView;

import static io.vov.vitamio.MediaPlayer.VIDEOQUALITY_HIGH;

/**
 * Created by malijie on 2017/6/9.
 */

public class FullScreenPlayActivity extends BaseActivity{
    private static final int MSG_UPDATE_CURRENT_TIME = 1;
    private static final int MSG_UPDATE_BATTERY = 2;
    private static final int MSG_HIDE_BRIGHTNESS_UI = 3;
    private static final int MSG_HIDE_VOLUME_UI = 4;

    private static final long UPDATE_TIME_FREQUENCE = 60 * 1000;
    private GestureDetector mGestureDetector = null;


    private String FILE_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "apk" + File.separator + "test.mkv";


    private VideoView mVideoView = null;
    private RelativeLayout mVolumeLayout = null;
    private RelativeLayout mBrightnessLayout = null;
    private TextView mTextVolume = null;
    private TextView mTextBrightness = null;
    private MediaControllerHandler mHandler = new MediaControllerHandler(this);
    private EasyMediaController mEasyMediaController = null;
    private int mScreenWidth;
    private int mBrightness;

    private boolean updatingVolume = true;
    private boolean updatingBrightness = true;
    private boolean updatingFastBack = true;
    private boolean updatingFastForward = true;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_play_layout);

        initViews();
        initData();

    }

    @Override
    public void initViews() {
        mVideoView = (VideoView) findViewById(R.id.id_fullscreen_video_view);
        mVolumeLayout = (RelativeLayout) findViewById(R.id.id_vb_layout_volume);
        mBrightnessLayout = (RelativeLayout) findViewById(R.id.id_vb_layout_brightness);
        mTextVolume = (TextView) findViewById(R.id.id_vb_text_volume);
        mTextBrightness = (TextView) findViewById(R.id.id_vb_text_brightness);

        mEasyMediaController = new EasyMediaController(this,this,mVideoView);
        mVideoView.setVideoPath(FILE_PATH);
        mVideoView.setMediaController(mEasyMediaController);
        mVideoView.setVideoQuality(VIDEOQUALITY_HIGH);
        mVideoView.requestFocus();
    }

    @Override
    public void initData() {
        mScreenWidth = Utils.getWindowWidth();
        mBrightness = Utils.getAppBrightness(this);

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

                 case MSG_HIDE_BRIGHTNESS_UI:
                     mBrightnessLayout.setVisibility(View.GONE);
                     break;

                 case MSG_HIDE_VOLUME_UI:
                     mVolumeLayout.setVisibility(View.GONE);
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
            if(newX>mScreenWidth/2 && Math.abs(deltaY)>Math.abs(deltaX) && updatingBrightness){
                Log.mlj("=====调节亮度====");
                onBrightnessSlide(deltaY);

            }else if(newX<mScreenWidth/2 && Math.abs(deltaY)>Math.abs(deltaX) && updatingVolume){
                //左半屏，调节音量
                Log.mlj("=====调节音量====");
                onVolumeSlide(deltaY);


            }else if(Math.abs(deltaX)>Math.abs(deltaY) && deltaX>40 && updatingFastBack){
                //快退
                Log.mlj("=====快退====");
                onVideoBackSlide(deltaX);


            }else if(Math.abs(deltaX)>Math.abs(deltaY) && deltaX<-40 && updatingFastForward){
                //快退
                Log.mlj("=====快进====");
                onVideoForwardSlide(deltaX);

            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }


    }

    private void clearUIState(){
        updatingFastBack = true;
        updatingVolume = true;
        updatingBrightness= true;
        updatingFastForward = true;

        mHandler.sendEmptyMessageDelayed(MSG_HIDE_BRIGHTNESS_UI,0);
//        mSeekDelta = 0;
//
//        mImageBrightness.setVisibility(View.GONE);
//        mImageVolume.setVisibility(View.GONE);
//        mImageFastForward.setVisibility(View.GONE);
//        mImageFastBack.setVisibility(View.GONE);
//        mTextVolume.setVisibility(View.GONE);
//        mTextBrightness.setVisibility(View.GONE);

    }

    private void onBrightnessSlide(int deltaY){
        Log.mlj("bright visible = "+  mBrightnessLayout.getVisibility());
        mBrightnessLayout.setVisibility(View.VISIBLE);

        if(mBrightness + (deltaY/20) <=0){
            mBrightness = 0;
        }else if(mBrightness + (deltaY/20) >=255f){
            mBrightness = 255;
        }else{
            mBrightness += deltaY/20;
        }

        Utils.changeAppBrightness(this, mBrightness);

        int percent =  mBrightness*100/255 ;
        mTextBrightness.setText(percent + "%");

        updatingFastBack = false;
        updatingVolume = false;
        updatingFastForward = false;

    }

    private void onVolumeSlide(int deltaY){
//        mImageVolume.setVisibility(View.VISIBLE);
//        mTextVolume.setVisibility(View.VISIBLE);
//        mImageBrightness.setVisibility(View.GONE);

        int savedVolume = Utils.getPlayerVolume();
        int volume;
        int maxVolume = Utils.getPlayerMaxVolume();

        if(savedVolume + (deltaY/100) <0){
            volume = 0;
        }else if(savedVolume + (deltaY/100) >maxVolume){
            volume = maxVolume;
        }else{
            volume = savedVolume + (deltaY/100);
        }

//        updateVolumeUI(volume);
        Utils.setPlayerVolume(volume);

        updatingFastBack = false;
        updatingBrightness = false;
        updatingFastForward = false;
    }

    private void onVideoForwardSlide(int deltaX){
//        mVideoView.pause();
//
//        if(updatingFastForward){
//            currentPosition = mVideoView.getCurrentPosition();
//        }
//
//        mImageFastForward.setVisibility(VISIBLE);
//        mSeekDelta += Math.abs(deltaX/100);
//
//        long seekPosition = currentPosition + mSeekDelta;
//        if(seekPosition >= mVideoView.getDuration()){
//            ToastManager.showShortMsg("视频已播放完毕");
//            return;
//        }
//
//        mVideoView.seekTo(seekPosition);
//        updatingVolume = false;
//        updatingBrightness = false;
//        updatingFastBack = false;
    }

    private void onVideoBackSlide(int deltaX){
//        long currentPosition = mVideoView.getCurrentPosition();
//        mImageFastBack.setVisibility(View.VISIBLE);
//        mVideoView.pause();
//        mVideoView.seekTo(currentPosition - deltaX/10);
//
//        updatingVolume = false;
//
//        updatingBrightness = false;
//        updatingFastForward = false;
    }
}
