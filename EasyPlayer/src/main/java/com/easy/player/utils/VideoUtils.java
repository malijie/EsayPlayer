package com.easy.player.utils;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

/**
 * Created by malijie on 2017/7/15.
 */

public class VideoUtils {
    public static Bitmap getThumbNail(String path){
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, 100, 60);
        return bitmap;
    }
}
