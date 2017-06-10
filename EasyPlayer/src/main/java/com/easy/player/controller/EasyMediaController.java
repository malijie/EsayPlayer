package com.easy.player.controller;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.easy.player.R;
import com.easy.player.utils.Utils;

import java.io.File;

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

    private TextView mTextCurrentTime;
    private boolean mIsPlaying = false;

    public EasyMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EasyMediaController(Context context) {
        super(context);
    }

    public EasyMediaController(Activity activity,Context context, VideoView videoView){
        super(context);
        mActivity = activity;
        mVideoView = videoView;
        mContext = context;

    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
    }

    /**
     * play button click
     */
    private OnClickListener playBtnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mIsPlaying = true;
            updateControllerUI();
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
            updateControllerUI();
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

    /**
     * prepare
     */
    private MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.setPlaybackSpeed(1.0f);
        }
    };

    @Override
    protected View makeControllerView() {
Log.mlj("makeControllerView");

        View v = LayoutInflater.from(mContext).inflate(R.layout.my_media_controller,this);
        mButtonPlay = (ImageButton) v.findViewById(R.id.id_controller_button_play);
        mButtonPause = (ImageButton) v.findViewById(R.id.id_controller_button_pause);
        mButtonBack = (ImageButton) v.findViewById(R.id.id_controller_button_back);
        mTextCurrentTime = (TextView) v.findViewById(R.id.id_controller_text_current_time);

        mButtonPlay.setOnClickListener(playBtnOnClickListener);
        mButtonPause.setOnClickListener(pauseBtnOnClickListener);
        mButtonBack.setOnClickListener(backBtnOnClickListener);

        return v;
    }

    private void updateControllerUI(){
        if(mIsPlaying){
            mButtonPlay.setVisibility(View.INVISIBLE);
            mButtonPause.setVisibility(View.VISIBLE);
        }else{
            mButtonPlay.setVisibility(View.VISIBLE);
            mButtonPause.setVisibility(View.INVISIBLE);
        }
    }

    public void updateCurrentTime(){
        Log.mlj("mTextCurrentTime=" + mTextCurrentTime + ",getCurrentTime" + getCurrentTime());
        mTextCurrentTime.setText(getCurrentTime());
    }

    private String getCurrentTime(){
        return Utils.getHHmmCurrentTime();
    }
}
