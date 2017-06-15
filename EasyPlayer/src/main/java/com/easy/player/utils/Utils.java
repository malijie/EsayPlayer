package com.easy.player.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.easy.player.EasyPlayer;
import com.easy.player.R;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static int getColor(int colorId){
        return EasyPlayer.sContext.getResources().getColor(colorId);
    }

    public static int getScreenBrightness(){

        int value = SharePreferenceUtil.loadBrightness();
        if(value == 0){
            ContentResolver cr = EasyPlayer.sContext.getContentResolver();
            try {
                value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
            } catch (Settings.SettingNotFoundException e) {

            }
        }

        return value;
    }

    public static void setScreenBrightness(Activity activity,int value){
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.screenBrightness = value / 255f;
        activity.getWindow().setAttributes(params);
        SharePreferenceUtil.saveBrightness(value);
    }

    public static int getPlayerVolume(){
        AudioManager mAudioManager = (AudioManager) EasyPlayer.sContext.getSystemService(Context.AUDIO_SERVICE);
        int volume = SharePreferenceUtil.loadVolume();
        if(volume == 0){
            volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC );
        }
        return volume;
    }

    public static int getPlayerMaxVolume(){
        AudioManager mAudioManager = (AudioManager) EasyPlayer.sContext.getSystemService(Context.AUDIO_SERVICE);
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    public static void setPlayerVolume(int volume){
        AudioManager mAudioManager = (AudioManager) EasyPlayer.sContext.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        SharePreferenceUtil.saveVolume(volume);
    }
}
