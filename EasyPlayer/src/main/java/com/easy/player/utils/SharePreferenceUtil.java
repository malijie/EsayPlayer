package com.easy.player.utils;

import android.content.Context;

import com.easy.player.EasyPlayer;

/**
 * Created by malijie on 2016/10/10.
 */
public class SharePreferenceUtil {
    public static final String SP_VIDEO_INFO = "user_info";
    public static final String VIDEO_QUALITY_KEY = "video_quality";
    public static final String SCREEN_BRIGHTNESS = "screen_brightness";
    public static final String VIDEO_VOLUME = "video_volume";


    public static void saveVideoQuality(int quality){
        EasyPlayer.sContext.getSharedPreferences(SP_VIDEO_INFO, Context.MODE_PRIVATE).edit()
                .putInt(VIDEO_QUALITY_KEY,quality).commit();
    }

    public static int loadVideoQuality(){
        return EasyPlayer.sContext.getSharedPreferences(SP_VIDEO_INFO, Context.MODE_PRIVATE)
                .getInt(VIDEO_QUALITY_KEY,0);
    }

    public static void saveBrightness(int value){
        EasyPlayer.sContext.getSharedPreferences(SP_VIDEO_INFO, Context.MODE_PRIVATE).edit()
                .putInt(SCREEN_BRIGHTNESS,value).commit();
    }

    public static int loadBrightness(){
        return EasyPlayer.sContext.getSharedPreferences(SP_VIDEO_INFO, Context.MODE_PRIVATE)
                .getInt(SCREEN_BRIGHTNESS,0);

    }

    public static void saveVolume(int value){
        EasyPlayer.sContext.getSharedPreferences(SP_VIDEO_INFO, Context.MODE_PRIVATE).edit()
                .putInt(VIDEO_VOLUME,value).commit();
    }

    public static int loadVolume(){
        return EasyPlayer.sContext.getSharedPreferences(SP_VIDEO_INFO, Context.MODE_PRIVATE)
                .getInt(VIDEO_VOLUME,0);

    }

}
