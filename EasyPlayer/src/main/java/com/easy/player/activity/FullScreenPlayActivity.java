package com.easy.player.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.easy.player.R;

import java.io.File;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.utils.Log;

/**
 * Created by malijie on 2017/6/9.
 */

public class FullScreenPlayActivity extends BaseActivity{

    private static final String FILE_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "apk" + File.separator + "test.mkv";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_media_controller);
    }

}
