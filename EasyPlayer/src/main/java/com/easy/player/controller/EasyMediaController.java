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
import com.easy.player.plugin.PluginVideoQuality;
import com.easy.player.utils.Utils;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by malijie on 2017/6/9.
 */

public class EasyMediaController extends MediaController{
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

    private boolean mIsPlaying = false;
    private GestureDetector mGestureDetector = null;


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
            final PluginVideoQuality pluginVideoQuality = new PluginVideoQuality(mContext);
            pluginVideoQuality.setSelectQualityListener(new PluginVideoQuality.ISelectQualityListener() {
                @Override
                public void selectHighQuality() {
                    mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
                    updateVideoQualityUI(MediaPlayer.VIDEOQUALITY_HIGH);
                    pluginVideoQuality.dismiss();
                }

                @Override
                public void selectMediumQuality() {
                    mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_MEDIUM);
                    updateVideoQualityUI(MediaPlayer.VIDEOQUALITY_MEDIUM);
                    pluginVideoQuality.dismiss();
                }

                @Override
                public void selectLowQuality() {
                    mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_LOW);
                    updateVideoQualityUI(MediaPlayer.VIDEOQUALITY_LOW);
                    pluginVideoQuality.dismiss();
                }
            });
            pluginVideoQuality.show();
        }
    };

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

        return v;
    }

    private void initListener() {
        mVideoView.setOnCompletionListener(onCompletionListener);
        mVideoView.setOnSeekCompleteListener(onSeekCompleteListener);
    }

    private void updateVideoQualityUI(int quality){
Log.mlj("quality=" + quality);
        if(quality == MediaPlayer.VIDEOQUALITY_HIGH){
            mTextVideoQuality.setText("超清");
        }else if(quality == MediaPlayer.VIDEOQUALITY_MEDIUM){
            mTextVideoQuality.setText("高清");
        }else if(quality == MediaPlayer.VIDEOQUALITY_LOW){
            mTextVideoQuality.setText("标清");
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
    //手势
    public class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            toggleHideOrShow();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;
        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }
}
