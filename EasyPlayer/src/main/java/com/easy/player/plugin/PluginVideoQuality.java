package com.easy.player.plugin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easy.player.R;
import com.easy.player.utils.SharePreferenceUtil;
import com.easy.player.utils.Utils;

import io.vov.vitamio.utils.Log;

/**
 * Created by malijie on 2017/6/12.
 */

public class PluginVideoQuality extends BasePlugin{

    private Dialog mDialog = null;
    private View v = null;

    private TextView mTextQualityHigh = null;
    private TextView mTextQualityMedium = null;
    private TextView mTextQualityLow = null;

    private RelativeLayout mLayoutQualityHigh;
    private RelativeLayout mLayoutQualityMedium;
    private RelativeLayout mLayoutQualityLow;

    private Context mContext = null;

    public PluginVideoQuality(Context context) {
        v = Utils.getView(R.layout.plugin_quality_layout);
        mContext = context;
        mDialog = new AlertDialog.Builder(context, R.style.dialog)
                .setView(v)
                .create();

        mLayoutQualityHigh = (RelativeLayout) v.findViewById(R.id.id_plugin_quality_layout_high);
        mLayoutQualityMedium = (RelativeLayout) v.findViewById(R.id.id_plugin_quality_layout_medium);
        mLayoutQualityLow = (RelativeLayout) v.findViewById(R.id.id_plugin_quality_layout_low);

        mTextQualityHigh = (TextView) v.findViewById(R.id.id_plugin_quality_text_high);
        mTextQualityMedium = (TextView) v.findViewById(R.id.id_plugin_quality_text_medium);
        mTextQualityLow = (TextView) v.findViewById(R.id.id_plugin_quality_text_low);

        mLayoutQualityHigh.setOnClickListener(qualityHighOnClickListener);
        mLayoutQualityMedium.setOnClickListener(qualityMediumOnClickListener);
        mLayoutQualityLow.setOnClickListener(qualityLowOnClickListener);

        initQualityUI();
    }

    private View.OnClickListener qualityHighOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearUIState();
            qualityListener.selectHighQuality();
            mTextQualityHigh.setTextColor(Utils.getColor(R.color.plugin_video_quality_text_select));
            SharePreferenceUtil.saveVideoQuality(VIDEO_QUALITY_HIGH);
        }
    };

    private View.OnClickListener qualityMediumOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearUIState();
            qualityListener.selectMediumQuality();
            mTextQualityMedium.setTextColor(Utils.getColor(R.color.plugin_video_quality_text_select));
            SharePreferenceUtil.saveVideoQuality(VIDEO_QUALITY_MEDIUM);

        }
    };

    private View.OnClickListener qualityLowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearUIState();
            qualityListener.selectLowQuality();
            mTextQualityLow.setTextColor(Utils.getColor(R.color.plugin_video_quality_text_select));
            SharePreferenceUtil.saveVideoQuality(VIDEO_QUALITY_LOW);

        }
    };

    public void show(){
            mDialog.show();
    }


    private void clearUIState(){
        mTextQualityHigh.setTextColor(Utils.getColor(R.color.white));
        mTextQualityMedium.setTextColor(Utils.getColor(R.color.white));
        mTextQualityLow.setTextColor(Utils.getColor(R.color.white));
    }

    private void initQualityUI(){
        int quality = SharePreferenceUtil.loadVideoQuality();
        if(quality == VIDEO_QUALITY_HIGH){
            mTextQualityHigh.setTextColor(Utils.getColor(R.color.plugin_video_quality_text_select));

        }else if(quality == VIDEO_QUALITY_MEDIUM){
            mTextQualityMedium.setTextColor(Utils.getColor(R.color.plugin_video_quality_text_select));

        }else if(quality == VIDEO_QUALITY_LOW){
            mTextQualityLow.setTextColor(Utils.getColor(R.color.plugin_video_quality_text_select));
        }
    }

    private ISelectQualityListener qualityListener;
    public void setSelectQualityListener(ISelectQualityListener listener){
        this.qualityListener = listener;
    }

    @Override
    public void hide() {
        mDialog.dismiss();
    }

    public interface ISelectQualityListener {
        void selectHighQuality();
        void selectMediumQuality();
        void selectLowQuality();
    }


}
