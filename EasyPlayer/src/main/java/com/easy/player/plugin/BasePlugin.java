package com.easy.player.plugin;

import io.vov.vitamio.MediaPlayer;

/**
 * Created by malijie on 2017/6/12.
 */

public abstract class BasePlugin{
    public static final int VIDEO_QUALITY_HIGH = MediaPlayer.VIDEOQUALITY_HIGH;
    public static final int VIDEO_QUALITY_MEDIUM = MediaPlayer.VIDEOQUALITY_MEDIUM;
    public static final int VIDEO_QUALITY_LOW = MediaPlayer.VIDEOQUALITY_LOW;

    public boolean updatingFastBack = true;
    public boolean updatingVolume = true;
    public boolean updatingFastForward = true;


}
