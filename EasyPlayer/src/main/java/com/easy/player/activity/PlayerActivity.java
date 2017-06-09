package com.easy.player.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.easy.player.R;
import com.easy.player.widget.EasyMediaController;

import java.io.File;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by malijie on 2017/6/7.
 */

public class PlayerActivity extends BaseActivity{
    private VideoView mVideoView = null;
    private MediaController mController = null;
    private int mPosition = 0;

    private static final String FILE_PATH = Environment.getExternalStorageDirectory() +
    File.separator + "apk" + File.separator + "test.mkv";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easy_player_layout);

        mVideoView = (VideoView) findViewById(R.id.id_video_view);
        mVideoView.setMediaController(new EasyMediaController(this,mVideoView,this));
        mVideoView.setVideoPath(FILE_PATH);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setPlaybackSpeed(1.0f);
            }
        });
    }

    @Override
    protected void onResume() {
        if (mPosition > 0) {
            mVideoView.seekTo(mPosition);
            mPosition = 0;
        }
        super.onResume();
        mVideoView.start();
    }
}


