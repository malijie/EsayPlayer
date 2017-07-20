package com.easy.player.plugin;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.easy.player.R;
import com.easy.player.utils.ToastManager;


/**
 * Created by malijie on 2017/7/20.
 */

public class PluginLock extends BasePlugin{

    private static PluginLock sPluginLock = null;
    private ImageView mImageLock = null;
    private boolean isLocked = false;

    private PluginLock(Activity activity){
        mImageLock = (ImageView) activity.findViewById(R.id.id_plugin_image_lock);
        mImageLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastManager.showShortMsg("onClicked");
                onLockSlide();
            }
        });
    }

    public static PluginLock getInstance(Activity activity){
        if(sPluginLock == null){
            synchronized (PluginLock.class){
                if(sPluginLock == null){
                    sPluginLock = new PluginLock(activity);
                }
            }
        }
        return sPluginLock;
    }

    public void onLockSlide(){
        if(isLocked()){
            isLocked = false;
            handleUnlock();

        }else{
            isLocked = true;
            handleLock();
        }
    }

    private void handleUnlock() {
        mImageLock.setImageResource(R.mipmap.lock_normal);
    }

    private void handleLock() {
        mImageLock.setImageResource(R.mipmap.lock_selected);
    }

    public boolean isLocked(){
        return isLocked;
    }

    @Override
    public void hide() {
        if(mImageLock != null){
            mImageLock.setVisibility(View.GONE);
        }
    }
}
