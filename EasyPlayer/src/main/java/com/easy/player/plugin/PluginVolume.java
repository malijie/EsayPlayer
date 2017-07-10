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
    private int volume;
    private int maxVolume = Utils.getMaxVolume();
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

            if(volume + (deltaVolume/100) <0){
                volume = 0;
            }else if(volume + (deltaVolume/100) >maxVolume){
                volume = maxVolume;
            }else{
                volume += (deltaVolume/100);
            }

            int volumePercent = Math.round(volume/15f * 100);
            mTextVolume.setText(volumePercent + "%");

            Utils.changeAppVolume(volume);

        }

    }

    private boolean canUpdateVolume(){
        return updatingVolume;
    }


    @Override
    public void hide() {
        if(mLayoutVolume != null){
            updatingVolume = true;
            mLayoutVolume.setVisibility(View.GONE);
            sPluginVolume = null;
        }
    }
}
