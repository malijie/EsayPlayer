package com.easy.player.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.easy.player.R;

import java.io.File;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by malijie on 2017/6/7.
 */

public class PlayerActivity extends Activity{
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "apk" + File.separator + "testmkv.mkv";
    private VideoView mVideoView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easy_player_layout);
        mVideoView = (VideoView) findViewById(R.id.id_video_view);
        mVideoView.setVideoPath(FILE_PATH);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setPlaybackSpeed(1.0f);
            }
        });


    }
}
