package com.easy.player.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by malijie on 2017/6/10.
 */

public class Utils {
    public static String getHHmmCurrentTime(){
       return new SimpleDateFormat("HH:mm").format(new Date());
    }

}
