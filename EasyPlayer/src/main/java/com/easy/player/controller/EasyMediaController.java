package com.easy.player.controller;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.easy.player.R;
import com.easy.player.base.PlayerMessage;
import com.easy.player.plugin.PluginVideoQuality;
import com.easy.player.utils.SharePreferenceUtil;
import com.easy.player.utils.ToastManager;
import com.easy.player.utils.Utils;
import com.ldoublem.loadingviewlib.view.LVCircularRing;

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
    private ImageView mImageBrightness;
    private ImageView mImageVolume = null;
    private ImageView mImageFastBack = null;
    private ImageView mImageFastForward = null;
    private TextView mTextBrightness = null;
    private TextView mTextVolume = null;
    private LVCircularRing mLoadingView = null;

    private boolean mIsPlaying = false;
    private int mBrightness;
    private GestureDetector mGestureDetector = null;

    private PluginVideoQuality mPluginVideoQuality = null;
//    private int lastX = 0;
//    private int lastY = 0;
//    private int newX = 0;
//
//    private int newY = 0;
    private boolean updatingVolume = true;
    private boolean updatingBrightness = true;
    private boolean updatingFastBack = true;
    private boolean updatingFastForward = true;
    private long mSeekDelta;
    private long currentPosition;
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

        initListener();
        initData();
    }

    private void initData() {
        mScreenWidth = Utils.getWindowWidth();
        mBrightness = Utils.getAppBrightness(mActivity);

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
            mIsPlaying = true;
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
            mIsPlaying = false;
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
            mPluginVideoQuality = new PluginVideoQuality(mContext);
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
            mPluginVideoQuality.show();
        }
    };


    private void handleChangeQuality(int quality){
        mLoadingView.setVisibility(VISIBLE);
        mLoadingView.startAnim();

        mVideoView.setVideoQuality(quality);
        updateVideoQualityUI(quality);
        mPluginVideoQuality.dismiss();
        changeQuality(quality);

        mLoadingView.stopAnim();
        mLoadingView.setVisibility(GONE);

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
        mImageBrightness = (ImageView) v.findViewById(R.id.id_controller_img_brightness);
        mImageVolume = (ImageView) v.findViewById(R.id.id_controller_img_volume);
        mImageFastBack  = (ImageView) findViewById(R.id.id_controller_img_fast_back);
        mImageFastForward = (ImageView) findViewById(R.id.id_controller_img_fast_forward);
        mTextVolume = (TextView)findViewById(R.id.id_controller_text_volume);
        mTextBrightness =  (TextView)findViewById(R.id.id_controller_text_brightness);
        mLoadingView = (LVCircularRing) findViewById(R.id.lv_circularring);

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
        mLoadingView.setBarColor(Utils.getColor(R.color.loading_bar));
        mLoadingView.setViewColor(Utils.getColor(R.color.loading_view));
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

    private void onBrightnessSlide(int deltaY){
        mImageBrightness.setVisibility(View.VISIBLE);
        mTextBrightness.setVisibility(View.VISIBLE);
        mImageVolume.setVisibility(View.GONE);


        if(mBrightness + (deltaY/20) <=0){
            mBrightness = 0;
        }else if(mBrightness + (deltaY/20) >=255f){
            mBrightness = 255;
        }else{
            mBrightness += deltaY/20;
        }
        updateBrightnessUI(mBrightness);
        Utils.changeAppBrightness(mActivity, mBrightness);
        updatingFastBack = false;
        updatingVolume = false;
        updatingFastForward = false;
    }

    private void onVolumeSlide(int deltaY){
        mImageVolume.setVisibility(View.VISIBLE);
        mTextVolume.setVisibility(View.VISIBLE);
        mImageBrightness.setVisibility(View.GONE);

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

        updateVolumeUI(volume);
        Utils.setPlayerVolume(volume);

        updatingFastBack = false;
        updatingBrightness = false;
        updatingFastForward = false;
    }

    private void onVideoForwardSlide(int deltaX){
        mVideoView.pause();

        if(updatingFastForward){
            currentPosition = mVideoView.getCurrentPosition();
        }

        mImageFastForward.setVisibility(VISIBLE);
        mSeekDelta += Math.abs(deltaX/100);

        long seekPosition = currentPosition + mSeekDelta;
        if(seekPosition >= mVideoView.getDuration()){
            ToastManager.showShortMsg("视频已播放完毕");
            return;
        }

        mVideoView.seekTo(seekPosition);
        updatingVolume = false;
        updatingBrightness = false;
        updatingFastBack = false;
    }

    private void onVideoBackSlide(int deltaX){
        long currentPosition = mVideoView.getCurrentPosition();
        mImageFastBack.setVisibility(VISIBLE);
        mVideoView.pause();
        mVideoView.seekTo(currentPosition - deltaX/10);

        updatingVolume = false;

        updatingBrightness = false;
        updatingFastForward = false;
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
                updateBrightnessUI(mBrightness);
                break;

            case MotionEvent.ACTION_MOVE:


                break;

            case MotionEvent.ACTION_SCROLL:

                break;
        }
        return super.onTouchEvent(event);
    }

    private void updateBrightnessUI(float brightness){
        int percent = (int)brightness*100/255 ;

        if(percent> 95 && percent <= 100){
            mImageBrightness.setImageResource(R.mipmap.brightness_full);
        }else if(percent <=4){
            mImageBrightness.setImageResource(R.mipmap.brightness_empty);
        }else if(percent>4 && percent <=15){
            mImageBrightness.setImageResource(R.mipmap.brightness_10);

        }else if(percent>15 && percent <=25){
            mImageBrightness.setImageResource(R.mipmap.brightness_20);

        }else if(percent>25 && percent <=35){
            mImageBrightness.setImageResource(R.mipmap.brightness_30);

        }else if(percent>35 && percent <=45){
            mImageBrightness.setImageResource(R.mipmap.brightness_40);

        }else if(percent>45 && percent <=55){
            mImageBrightness.setImageResource(R.mipmap.brightness_50);

        }else if(percent>55 && percent <=65){
            mImageBrightness.setImageResource(R.mipmap.brightness_60);

        }else if(percent>65 && percent <=75){
            mImageBrightness.setImageResource(R.mipmap.brightness_70);

        }else if(percent>75 && percent <=85){
            mImageBrightness.setImageResource(R.mipmap.brightness_80);

        }else if(percent>85 && percent <=95){
            mImageBrightness.setImageResource(R.mipmap.brightness_90);
        }
        mTextBrightness.setText(percent + "%");
    }

    private void updateVolumeUI(float volume){
        int volumePercent = Math.round(volume/15*100);
        if(volume == 15){
            mImageVolume.setImageResource(R.mipmap.volume_full);
        }else if(volume <=1){
            mImageVolume.setImageResource(R.mipmap.volume_10);
        }else if(volume>1 && volume <=2){
            mImageVolume.setImageResource(R.mipmap.volume_20);

        }else if(volume>2 && volume <=3){
            mImageVolume.setImageResource(R.mipmap.volume_30);

        }else if(volume>3 && volume <=4){
            mImageVolume.setImageResource(R.mipmap.volume_40);

        }else if(volume>4 && volume <=7){
            mImageVolume.setImageResource(R.mipmap.volume_50);

        }else if(volume>7 && volume <=8){
            mImageVolume.setImageResource(R.mipmap.volume_60);

        }else if(volume>8 && volume <=10){
            mImageVolume.setImageResource(R.mipmap.volume_70);

        }else if(volume>10 && volume <=13){
            mImageVolume.setImageResource(R.mipmap.volume_80);

        }else if(volume>13 && volume <=14) {
            mImageVolume.setImageResource(R.mipmap.volume_90);
        }
        mTextVolume.setText(volumePercent + "%");
    }

    private void clearUIState(){
        updatingFastBack = true;
        updatingVolume = true;
        updatingBrightness= true;
        updatingFastForward = true;
        mSeekDelta = 0;

        mImageBrightness.setVisibility(View.GONE);
        mImageVolume.setVisibility(View.GONE);
        mImageFastForward.setVisibility(View.GONE);
        mImageFastBack.setVisibility(View.GONE);
        mTextVolume.setVisibility(View.GONE);
        mTextBrightness.setVisibility(View.GONE);

    }


}
