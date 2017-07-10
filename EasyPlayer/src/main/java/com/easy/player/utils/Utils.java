package com.easy.player.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.easy.player.EasyPlayer;
import com.easy.player.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.vov.vitamio.utils.Log;

/**
 * Created by malijie on 2017/6/10.
 */

public class Utils {

    public static String getHHmmCurrentTime(){
       return new SimpleDateFormat("HH:mm").format(new Date());
    }

    public static View getView(int resId){
        return LayoutInflater.from(EasyPlayer.sContext).inflate(resId,null);
    }

    public static View getView(int resId,ViewGroup parent){
        return LayoutInflater.from(EasyPlayer.sContext).inflate(resId,parent);
    }


    public static int getColor(int colorId){
        return EasyPlayer.sContext.getResources().getColor(colorId);
    }

    // 根据亮度值修改当前window亮度
   public static void changeAppBrightness(Activity activity, int brightness) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
                lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            } else {
                lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
       window.setAttributes(lp);
   }

   public static void changeAppVolume(int volume){
       AudioManager mAudioManager = (AudioManager) EasyPlayer.sContext.getSystemService(Context.AUDIO_SERVICE);
       mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
   }

    public static int getWindowWidth(){
        return EasyPlayer.sContext.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getMaxVolume(){
        AudioManager mAudioManager = (AudioManager)EasyPlayer.sContext.getSystemService(Context.AUDIO_SERVICE);
        return  mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }
}
