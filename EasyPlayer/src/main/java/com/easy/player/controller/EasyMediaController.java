package com.easy.player.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easy.player.R;
import com.easy.player.base.PlayerMessage;
import com.easy.player.plugin.PluginBrightness;
import com.easy.player.plugin.PluginFastBack;
import com.easy.player.plugin.PluginFastForward;
import com.easy.player.plugin.PluginLock;
import com.easy.player.plugin.PluginVideoQuality;
import com.easy.player.plugin.PluginVolume;
import com.easy.player.utils.SharePreferenceUtil;
import com.easy.player.utils.ToastManager;
import com.easy.player.utils.Utils;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by malijie on 2017/6/9.
 */

public class EasyMediaController  extends MediaController implements PlayerMessage{
    private static final String TAG = EasyMediaController.class.getSimpleName();

    private VideoView mVideoView = null;
    private Context mContext = null;
    private Activity mActivity = null;

    private ImageButton mButtonPlay = null;
    private ImageButton mButtonPause;
    private ImageButton mButtonBack;
    private TextView mTextVideoTime;
    private ImageView mImageButtery;
    private TextView mTextBattery;
    private TextView mTextCurrentTime;
    private TextView mTextVideoQuality = null;

    private GestureDetector mGestureDetector = null;

    private PluginVideoQuality mPluginVideoQuality = null;
    private PluginBrightness mPluginBrightness = null;
    private PluginFastForward mPluginFastForward = null;
    private PluginFastBack mPluginFastBack = null;
    private PluginVolume mPluginVolume = null;
    private PluginLock mPluginLock = null;

    private long mSeekDelta;
    private int mScreenWidth;


    public EasyMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EasyMediaController(Context context) {
        super(context);
    }

    public EasyMediaController(Activity activity,Context context, VideoView videoView){
        super(context);

        Log.mlj("==========EasyMediaController init==========");
        mActivity = activity;
        mVideoView = videoView;
        mContext = context;
        mGestureDetector = new GestureDetector(context,new PlayerGestureListener());
        initPlugin();
        initListener();
        initData();
    }

    private void initPlugin(){
        mPluginBrightness =  PluginBrightness.getInstance(mActivity);
        mPluginVolume = PluginVolume.getInstance(mActivity);
        mPluginFastForward = PluginFastForward.getInstance(mActivity,mVideoView);
        mPluginFastBack = PluginFastBack.getInstance(mActivity,mVideoView);
        mPluginLock = PluginLock.getInstance(mActivity);

        mPluginVideoQuality = new PluginVideoQuality(mActivity);
        mPluginVideoQuality.setSelectQualityListener(new PluginVideoQuality.ISelectQualityListener() {
            @Override
            public void selectHighQuality() {
                handleChangeQuality(MediaPlayer.VIDEOQUALITY_HIGH);

            }

            @Override
            public void selectMediumQuality() {
                handleChangeQuality(MediaPlayer.VIDEOQUALITY_MEDIUM);
            }

            @Override
            public void selectLowQuality() {
                handleChangeQuality(MediaPlayer.VIDEOQUALITY_LOW);
            }
        });
    }

    private void initData() {
        mScreenWidth = Utils.getWindowWidth();
    }

    @Override
    public void onFinishInflate() {
        Log.mlj("onFinishInflate");
        super.onFinishInflate();
    }

    /**
     * play button click
     */
    private OnClickListener playBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            updatePlayPauseButtonUI(true);
            mVideoView.start();
        }
    };

    /**
     * pause button click
     */
    private OnClickListener pauseBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            updatePlayPauseButtonUI(false);
            mVideoView.pause();
        }
    };


    /**
     * back button click
     */
    private OnClickListener backBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mVideoView.stopPlayback();
            if(mActivity != null){
                mActivity.finish();
            }

        }
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            updatePlayPauseButtonUI(mp.isPlaying());
        }
    };

    /**
     * prepare
     */
    private MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.setPlaybackSpeed(1.0f);
        }
    };

    /**
     * seek complete
     */
    private MediaPlayer.OnSeekCompleteListener onSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            updatePlayPauseButtonUI(true);
        }
    };

    private OnClickListener qualityBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            mPluginVideoQuality.show();
        }
    };


    private void handleChangeQuality(int quality){

        mVideoView.setVideoQuality(quality);
        updateVideoQualityUI(quality);
        mPluginVideoQuality.hide();
        changeQuality(quality);

    }

    private void changeQuality(int quality){
       long currentPosition = mVideoView.getCurrentPosition();
       mVideoView.setVideoQuality(quality);
       mVideoView.seekTo(currentPosition);
       ToastManager.showShortMsg("已切换清晰度为:" + getVideoQuality(quality));
    }

    private String getVideoQuality(int quality){
        String strQuality = "";
        if(quality ==  MediaPlayer.VIDEOQUALITY_HIGH){
            strQuality = VIDEO_HIGH_QUALITY;
        }else if(quality ==  MediaPlayer.VIDEOQUALITY_MEDIUM){
            strQuality = VIDEO_MEDIUM_QUALITY;
        }else if(quality ==  MediaPlayer.VIDEOQUALITY_LOW){
            strQuality = VIDEO_LOW_QUALITY;
        }
        return strQuality;
    }

    @Override
    protected View makeControllerView() {
        Log.mlj("==========EasyMediaController makeControllerView============");

        View v = LayoutInflater.from(mContext).inflate(R.layout.easy_media_controller,this);
        mButtonPlay = (ImageButton) v.findViewById(R.id.id_controller_button_play);
        mButtonPause = (ImageButton) v.findViewById(R.id.id_controller_button_pause);
        mButtonBack = (ImageButton) v.findViewById(R.id.id_controller_button_back);
        mTextCurrentTime = (TextView) v.findViewById(R.id.id_controller_text_current_time);
        mTextVideoTime = (TextView)v.findViewById(R.id.mediacontroller_time_total);
        mTextBattery = (TextView) v.findViewById(R.id.id_controller_text_battery);
        mImageButtery = (ImageView)v.findViewById(R.id.id_controller_img_battery);
        mTextVideoQuality = (TextView) v.findViewById(R.id.id_controller_text_quality);

        mButtonPlay.setOnClickListener(playBtnOnClickListener);
        mButtonPause.setOnClickListener(pauseBtnOnClickListener);
        mButtonBack.setOnClickListener(backBtnOnClickListener);
        mTextVideoQuality.setOnClickListener(qualityBtnOnClickListener);

        mTextVideoTime.setText(mVideoView.getDuration() + "");
        mTextVideoQuality.setText(getVideoQuality(SharePreferenceUtil.loadVideoQuality()));

        initViews();

        return v;
    }

    private void initViews() {
    }

    private void initListener() {
        mVideoView.setOnCompletionListener(onCompletionListener);
        mVideoView.setOnSeekCompleteListener(onSeekCompleteListener);
    }

    private void updateVideoQualityUI(int quality){
        if(quality == MediaPlayer.VIDEOQUALITY_HIGH){
            mTextVideoQuality.setText(VIDEO_HIGH_QUALITY);
        }else if(quality == MediaPlayer.VIDEOQUALITY_MEDIUM){
            mTextVideoQuality.setText(VIDEO_MEDIUM_QUALITY);
        }else if(quality == MediaPlayer.VIDEOQUALITY_LOW){
            mTextVideoQuality.setText(VIDEO_LOW_QUALITY);
        }
    }

    private void updatePlayPauseButtonUI(boolean isPlaying){
        if(isPlaying){
            mButtonPlay.setVisibility(View.INVISIBLE);
            mButtonPause.setVisibility(View.VISIBLE);
        }else{
            mButtonPlay.setVisibility(View.VISIBLE);
            mButtonPause.setVisibility(View.INVISIBLE);
        }
    }

    public void updateCurrentTime(){
        mTextCurrentTime.setText(getCurrentTime());
    }

    private String getCurrentTime(){
        return Utils.getHHmmCurrentTime();
    }

    //手势
    public class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            clearUIState();
            toggleHideOrShow();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int x = (int) e1.getX();
            int y = (int) e1.getY();
            int newX = (int) e2.getX();
            int newY = (int) e2.getY();

            int deltaX = x-newX;
            int deltaY = y-newY;

//Log.mlj("screenWidth=" + mScreenWidth +",x=" + x + ",y=" + y +",newX=" + newX + ",newY=" + newY + ",deltaX=" + deltaX + ",deltaY=" + deltaY + ",distanceX=" + distanceX + ",distanceY="+distanceY);
            //右半屏,调节亮度
            if(newX>mScreenWidth/2 && Math.abs(deltaY)>Math.abs(deltaX)){
                Log.mlj(TAG,"=====调节亮度====");
                mPluginBrightness.onBrightnessSlide(deltaY);

            }else if(newX<mScreenWidth/2 && Math.abs(deltaY)>Math.abs(deltaX)){
                //左半屏，调节音量
                Log.mlj(TAG,"=====调节音量====");
                mPluginVolume.onVolumeSlide(deltaY);


            }else if(Math.abs(deltaX)>Math.abs(deltaY) && deltaX>40){
                //快退
                Log.mlj(TAG,"=====快退====");
                mPluginFastBack.onBackSlide(deltaX);


            }else if(Math.abs(deltaX)>Math.abs(deltaY) && deltaX<-40){
                //快退
                Log.mlj(TAG,"=====快进====");
                mPluginFastForward.onForwardSlide(deltaX);

            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }



    public void updateBatteryUI(int battery) {
        if(battery>0 && battery <35){
            mImageButtery.setImageResource(R.drawable.battery1);
        }else if(battery>=35 && battery <90){
            mImageButtery.setImageResource(R.drawable.battery2);
        }else if(battery>=90 && battery <=100){
            mImageButtery.setImageResource(R.drawable.battery3);
        }
        mTextBattery.setText(battery + "%");

    }

    public void toggleHideOrShow() {
        if(isShowing()){
            hide();
        }else{
            show();
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if (mGestureDetector.onTouchEvent(event)){
            return true;
        }

        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                clearUIState();

                break;
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:

                break;

            case MotionEvent.ACTION_SCROLL:

                break;
        }
        return super.onTouchEvent(event);
    }


    private void clearUIState(){
        mSeekDelta = 0;

        mPluginBrightness.hide();
        mPluginVolume.hide();
        mPluginFastForward.hide();
        mPluginFastBack.hide();
        mPluginLock.hide();

    }


}
