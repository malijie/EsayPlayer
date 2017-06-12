package com.easy.player.utils;

import android.view.LayoutInflater;
import android.view.View;

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
}
