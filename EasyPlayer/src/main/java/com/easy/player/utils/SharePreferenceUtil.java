package com.easy.player.utils;

import android.content.Context;

import com.easy.player.EasyPlayer;

/**
 * Created by malijie on 2016/10/10.
 */
public class SharePreferenceUtil {
    public static final String SP_VIDEO_INFO = "user_info";
    public static final String VIDEO_QUALITY_KEY = "video_quality";


    public static void saveVideoQuality(int quality){
        EasyPlayer.sContext.getSharedPreferences(SP_VIDEO_INFO, Context.MODE_PRIVATE).edit()
                .putInt(VIDEO_QUALITY_KEY,quality).commit();
    }

    public static int loadVideoQuality(){
        return EasyPlayer.sContext.getSharedPreferences(SP_VIDEO_INFO, Context.MODE_PRIVATE)
                .getInt(VIDEO_QUALITY_KEY,0);
    }

}
