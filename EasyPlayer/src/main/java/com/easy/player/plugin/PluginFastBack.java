package com.easy.player.plugin;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easy.player.R;

import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by malijie on 2017/7/7.
 */

public class PluginFastBack extends BasePlugin{
    private static PluginFastBack sPluginFastBack = null;
    private Activity mActivity = null;
    private VideoView mVideoView = null;

    private RelativeLayout mLayoutBack = null;
    private TextView mTextTime = null;


    private PluginFastBack(Activity activity,VideoView videoView){
        mActivity = activity;
        mVideoView = videoView;
        mLayoutBack = (RelativeLayout) activity.findViewById(R.id.id_fb_layout_back);
        mTextTime = (TextView) activity.findViewById(R.id.id_fb_text_back);
    }

    public static PluginFastBack getInstance(Activity activity, VideoView videoView){
        if(sPluginFastBack == null){
            synchronized (PluginFastBack.class){
                if(sPluginFastBack == null){
                    sPluginFastBack = new PluginFastBack(activity,videoView);
                }
            }
        }
        return sPluginFastBack;
    }

    public void onBackSlide(int deltaX){
        long currentPosition = mVideoView.getCurrentPosition();
        mLayoutBack.setVisibility(View.VISIBLE);
        mVideoView.pause();
        mVideoView.seekTo(currentPosition - deltaX/10);
        disableUpdateOtherPlugin(PLUGIN_TYPE_FASTBACK);
    }

    @Override
    public void hide() {
        if(mLayoutBack != null){
            mLayoutBack.setVisibility(View.GONE);
            updatingFastBack = true;
            sPluginFastBack = null;
        }
    }
}
