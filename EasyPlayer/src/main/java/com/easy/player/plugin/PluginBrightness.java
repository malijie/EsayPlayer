package com.easy.player.plugin;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private ImageView mImageBrightnessBg = null;
    private ImageView mImageBrightnessFront = null;
    private static PluginBrightness sPluginBrightness = null;

    private float brightness;

    private PluginBrightness(Activity activity){
        mActivity = activity;
        mLayoutBrightness = (RelativeLayout) activity.findViewById(R.id.id_vb_layout_brightness);
        mImageBrightnessBg = (ImageView) activity.findViewById(R.id.id_vb_image_brightness_bg);
        mImageBrightnessFront = (ImageView) activity.findViewById(R.id.id_vb_image_brightness_front);
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

            float percent =  brightness /255 ;
            ViewGroup.LayoutParams lp = mImageBrightnessFront.getLayoutParams();
            lp.width = (int) (mImageBrightnessBg.getLayoutParams().width * percent);
            mImageBrightnessFront.setLayoutParams(lp);
        }

    }


    private boolean canUpdateBrightness(){
        return updatingBrightness && isEnable;
    }


    @Override
    public void hide() {
        if(mLayoutBrightness != null){
            mLayoutBrightness.setVisibility(View.GONE);
            updatingBrightness = true;
            sPluginBrightness = null;
        }
    }

    @Override
    public void setEnable(boolean open) {
        if(!open){
            isEnable = false;
        }else{
            isEnable = true;
        }
    }
}
