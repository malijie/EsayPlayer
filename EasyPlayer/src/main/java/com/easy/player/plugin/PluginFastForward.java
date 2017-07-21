package com.easy.player.plugin;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easy.player.R;
import com.easy.player.utils.ToastManager;

import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by malijie on 2017/7/7.
 */

public class PluginFastForward extends BasePlugin{
    private static PluginFastForward sPluginFastForward = null;
    private Activity mActivity = null;
    private RelativeLayout mLayoutForward = null;
    private TextView mTextTime = null;
    private VideoView mVideoView = null;
    private int mSeekDelta;
    private long currentPosition;

    private PluginFastForward(Activity activity, VideoView videoView){
        mActivity = activity;
        mVideoView = videoView;
        mLayoutForward = (RelativeLayout) activity.findViewById(R.id.id_fb_layout_forward);
        mTextTime = (TextView) activity.findViewById(R.id.id_fb_text_forward);
    }

    public static PluginFastForward getInstance(Activity activity, VideoView videoView){
        if(sPluginFastForward == null){
            synchronized (PluginFastForward.class){
                if(sPluginFastForward == null){
                    sPluginFastForward = new PluginFastForward(activity,videoView);
                }
            }
        }
        return sPluginFastForward;
    }

    public void onForwardSlide(int deltaX){
        if(canForward()){
            mVideoView.pause();
            if(updatingFastForward){
                currentPosition = mVideoView.getCurrentPosition();
            }
            mLayoutForward.setVisibility(View.VISIBLE);
            mSeekDelta += Math.abs(deltaX/100);
            long seekPosition = currentPosition + mSeekDelta;
            if(seekPosition >= mVideoView.getDuration()){
                ToastManager.showShortMsg("视频已播放完毕");
                return;
            }
            mVideoView.seekTo(seekPosition);
            disableUpdateOtherPlugin(PLUGIN_TYPE_FASTFORWARD);
        }
    }

    private boolean canForward(){
        return updatingFastForward && isEnable;
    }

    @Override
    public void hide() {
        if(mLayoutForward != null){
            mLayoutForward.setVisibility(View.GONE);
            updatingFastForward = true;
            sPluginFastForward = null;
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
