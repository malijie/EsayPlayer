package com.easy.player.plugin;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easy.player.R;
import com.easy.player.utils.Utils;

import io.vov.vitamio.utils.Log;

/**
 * Created by malijie on 2017/7/2.
 */

public class PluginVolume extends BasePlugin{
    private Activity mActivity;
    private RelativeLayout mLayoutVolume = null;
    private TextView mTextVolume = null;
    private static PluginVolume sPluginVolume;

    private PluginVolume(Activity activity){
        mActivity = activity;
        mLayoutVolume = (RelativeLayout) activity.findViewById(R.id.id_vb_layout_volume);
        mTextVolume = (TextView) activity.findViewById(R.id.id_vb_text_volume);
    }

    public static PluginVolume getInstance(Activity activity){
        if(sPluginVolume == null){
            synchronized (PluginVolume.class){
                if(sPluginVolume == null){
                    sPluginVolume = new PluginVolume(activity);
                }
            }
        }
        return sPluginVolume;
    }

    public void onVolumeSlide(int deltaVolume){

        if(canUpdateVolume()){
            disableUpdateOtherPlugin(PLUGIN_TYPE_VOLUME);
            mLayoutVolume.setVisibility(View.VISIBLE);

            int volume;
            int savedVolume = Utils.getPlayerVolume();
            int maxVolume = Utils.getPlayerMaxVolume();

            if(savedVolume + (deltaVolume/100) <0){
                volume = 0;
            }else if(savedVolume + (deltaVolume/100) >maxVolume){
                volume = maxVolume;
            }else{
                volume = savedVolume + (deltaVolume/100);
            }

            int volumePercent = Math.round(volume/15f * 100);
            mTextVolume.setText(volumePercent + "%");

            Utils.setPlayerVolume(volume);

        }

    }

    private boolean canUpdateVolume(){
        return updatingVolume;
    }

    public void hideVolumeUI(){
        if(mLayoutVolume != null){
            updatingVolume = true;
            mLayoutVolume.setVisibility(View.GONE);
            sPluginVolume = null;
        }
    }
}
