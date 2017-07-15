package com.easy.player.plugin;

import io.vov.vitamio.MediaPlayer;

/**
 * Created by malijie on 2017/6/12.
 */

public abstract class BasePlugin{
    public static final int PLUGIN_TYPE_BRIGHTNESS = 1;
    public static final int PLUGIN_TYPE_VOLUME = 2;
    public static final int PLUGIN_TYPE_FASTFORWARD = 3;
    public static final int PLUGIN_TYPE_FASTBACK = 4;

    public static final int VIDEO_QUALITY_HIGH = MediaPlayer.VIDEOQUALITY_HIGH;
    public static final int VIDEO_QUALITY_MEDIUM = MediaPlayer.VIDEOQUALITY_MEDIUM;
    public static final int VIDEO_QUALITY_LOW = MediaPlayer.VIDEOQUALITY_LOW;

    public static boolean updatingFastBack = true;
    public static boolean updatingVolume = true;
    public static boolean updatingFastForward = true;
    public static boolean updatingBrightness = true;

    public void disableUpdateOtherPlugin(int currentPlugin){
         if(currentPlugin == PLUGIN_TYPE_BRIGHTNESS){
            updatingFastBack = false;
            updatingVolume = false;
            updatingFastForward = false;
             updatingBrightness = true;
        }else if(currentPlugin == PLUGIN_TYPE_VOLUME){
             updatingFastBack = false;
             updatingFastForward = false;
             updatingBrightness = false;
             updatingVolume = true;
         }else if(currentPlugin == PLUGIN_TYPE_FASTBACK){
             updatingVolume = false;
             updatingFastForward = false;
             updatingBrightness = false;
             updatingFastBack = true;
         }else if(currentPlugin == PLUGIN_TYPE_FASTFORWARD){
             updatingFastBack = false;
             updatingVolume = false;
             updatingBrightness = false;
             updatingFastForward = true;
         }
    }



    public abstract void hide();

}
