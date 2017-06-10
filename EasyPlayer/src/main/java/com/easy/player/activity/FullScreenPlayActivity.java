package com.easy.player.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

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
    private static final long UPDATE_TIME_FREQUENCE = 60 * 1000;
    private String FILE_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "apk" + File.separator + "test.mkv";


    private VideoView mVideoView = null;
    private MediaControllerHandler mHandler = new MediaControllerHandler(this);
    public EasyMediaController mEasyMediaController = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_play_layout);
        mVideoView = (VideoView) findViewById(R.id.id_video_view);

        mEasyMediaController = new EasyMediaController(this,this,mVideoView);
        mVideoView.setMediaController(mEasyMediaController);
        mVideoView.setVideoPath(FILE_PATH);


        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_CURRENT_TIME,1000);
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

             }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);

    }
}
