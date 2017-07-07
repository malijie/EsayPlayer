package com.easy.player.plugin;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easy.player.R;
import com.easy.player.utils.Utils;

import org.w3c.dom.Text;

import io.vov.vitamio.utils.Log;

/**
 * Created by malijie on 2017/6/30.
 */

public class PluginBrightness extends BasePlugin {
    private RelativeLayout mLayoutBrightness = null;
    private Activity mActivity = null;
    private TextView mTextBrightness = null;
    private static PluginBrightness sPluginBrightness = null;

    private int brightness;

    private PluginBrightness(Activity activity){
        mActivity = activity;
        mLayoutBrightness = (RelativeLayout) activity.findViewById(R.id.id_vb_layout_brightness);
        mTextBrightness = (TextView) activity.findViewById(R.id.id_vb_text_brightness);
    }

    public static PluginBrightness getInstance(Activity activity){
        if(sPluginBrightness == null){
            synchronized (PluginBrightness.class){
                if(sPluginBrightness == null){
                    sPluginBrightness = new PluginBrightness(activity);
                }
            }
        }
        return sPluginBrightness;
    }



    public void onBrightnessSlide(int deltaBrightness){
Log.mlj("canUpdateBrightness() = " + canUpdateBrightness());
        if(canUpdateBrightness()){
            disableUpdateOtherPlugin(PLUGIN_TYPE_BRIGHTNESS);
            mLayoutBrightness.setVisibility(View.VISIBLE);

            if(brightness + (deltaBrightness/20) <=0){
                brightness = 0;
            }else if(brightness + (deltaBrightness/20) >=255f){
                brightness = 255;
            }else{
                brightness += deltaBrightness/20;
            }

            Utils.changeAppBrightness(mActivity, brightness);

            int percent =  brightness*100/255 ;
            mTextBrightness.setText(percent + "%");

        }

    }

    public void hideBrightnessUI(){
        if(mLayoutBrightness != null){
            mLayoutBrightness.setVisibility(View.GONE);
            updatingBrightness = true;
            sPluginBrightness = null;
        }
    }

    private boolean canUpdateBrightness(){
        return updatingBrightness;
    }


}
