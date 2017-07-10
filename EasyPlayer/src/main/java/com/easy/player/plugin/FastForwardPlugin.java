package com.easy.player.plugin;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.easy.player.R;
import com.easy.player.utils.ToastManager;

/**
 * Created by malijie on 2017/7/7.
 */

public class FastForwardPlugin extends BasePlugin{
    private static FastForwardPlugin sPluginFastForward = null;
    private Activity mActivity = null;
    private RelativeLayout mLayoutForward = null;
    private TextView mTextTime = null;
    private VideoView mVideoView = null;
    int mSeekDelta;
    int currentPosition;

    private FastForwardPlugin(Activity activity, VideoView videoView){
        mActivity = activity;
        mVideoView = videoView;
        mLayoutForward = (RelativeLayout) activity.findViewById(R.id.id_fb_layout_forward);
        mTextTime = (TextView) activity.findViewById(R.id.id_fb_text_forward);
    }

    public static FastForwardPlugin getInstance(Activity activity,VideoView videoView){
        if(sPluginFastForward == null){
            synchronized (FastForwardPlugin.class){
                if(sPluginFastForward == null){
                    sPluginFastForward = new FastForwardPlugin(activity,videoView);
                }
            }
        }
        return sPluginFastForward;
    }

    public void onForwardSlide(int deltaX){
        mVideoView.pause();
        if(updatingFastForward){
            currentPosition = mVideoView.getCurrentPosition();
        }
        mLayoutForward.setVisibility(View.VISIBLE);
        mSeekDelta += Math.abs(deltaX/100);
        int seekPosition = currentPosition + mSeekDelta;
        if(seekPosition >= mVideoView.getDuration()){
            ToastManager.showShortMsg("视频已播放完毕");
            return;
        }
        mVideoView.seekTo(seekPosition);
        updatingVolume = false;
        updatingBrightness = false;
        updatingFastBack = false;
    }

    public void hideBrightnessUI(){
        if(mLayoutForward != null){
            mLayoutForward.setVisibility(View.GONE);
            updatingBrightness = true;
            sPluginFastForward = null;
        }
    }

}
