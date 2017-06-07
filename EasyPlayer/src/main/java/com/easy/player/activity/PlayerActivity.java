package com.easy.player.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.easy.player.R;

import java.io.File;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by malijie on 2017/6/7.
 */

public class PlayerActivity extends Activity implements View.OnClickListener{
    private VideoView mVideoView = null;
    private Button mButtonZoomIn = null;
    private Button mButtonZommOut = null;
    private Button mButtonPlay = null;


    private static final String FILE_PATH = Environment.getExternalStorageDirectory() +
    File.separator + "apk" + File.separator + "testmkv.mkv";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easy_player_layout);
        mVideoView = (VideoView) findViewById(R.id.id_video_view);
        mButtonZoomIn = (Button) findViewById(R.id.id_button_zoom_in);
        mButtonZommOut =  (Button) findViewById(R.id.id_button_zoom_out);
        mButtonPlay = (Button) findViewById(R.id.id_button_play);

        mButtonZoomIn.setOnClickListener(this);
        mButtonZommOut.setOnClickListener(this);
        mButtonPlay.setOnClickListener(this);

    }

    private void startPlay(){
        mVideoView.setVideoPath(FILE_PATH);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setPlaybackSpeed(1.0f);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_button_play:
                startPlay();
                break;
            case R.id.id_button_zoom_in:
                break;
            case R.id.id_button_zoom_out:

                break;

        }
    }
}
