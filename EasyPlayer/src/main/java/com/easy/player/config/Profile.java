package com.easy.player.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by malijie on 2017/7/10.
 */

public class Profile {
    private static final String SCAN_DIRECTORY = Environment.getExternalStorageDirectory() + File.separator + "apk" + File.separator;
//    private static final String SCAN_DIRECTORY = Environment.getExternalStorageDirectory() + File.separator ;

    private static final String SCAN_DIRECTORY_KEY = "directory_key";

    public static String getScanDirectory(){
        return SCAN_DIRECTORY;
    }

    public static String getScanDirectoryKey(){
        return SCAN_DIRECTORY_KEY;
    }
}
