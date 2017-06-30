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
    private int mBrightness;
    private Activity mActivity = null;
    private TextView mTextBrightness = null;

    public PluginBrightness(Activity activity){
        mActivity = activity;
        mLayoutBrightness = (RelativeLayout) activity.findViewById(R.id.id_vb_layout_brightness);
        mTextBrightness = (TextView) activity.findViewById(R.id.id_vb_text_brightness);
    }

    public void onBrightnessSlide(int deltaY){
        mLayoutBrightness.setVisibility(View.VISIBLE);

        if(mBrightness + (deltaY/20) <=0){
            mBrightness = 0;
        }else if(mBrightness + (deltaY/20) >=255f){
            mBrightness = 255;
        }else{
            mBrightness += deltaY/20;
        }

        Utils.changeAppBrightness(mActivity, mBrightness);

        int percent =  mBrightness*100/255 ;
        mTextBrightness.setText(percent + "%");

        updatingFastBack = false;
        updatingVolume = false;
        updatingFastForward = false;
    }

    public void hideBrightnessUI(){
        if(mLayoutBrightness != null){
            mLayoutBrightness.setVisibility(View.GONE);
        }
    }
}
