package com.easy.player.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.easy.player.R;

import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by malijie on 2017/6/9.
 */

public class EasyMediaController extends MediaController{
    private VideoView mVideoView = null;
    private Activity mActivity;
    private Context mContext = null;
    private ImageButton mButtonPlay = null;

    public EasyMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EasyMediaController(Context context) {
        super(context);
    }

    public EasyMediaController(Context context, VideoView videoView, Activity activity){
        super(context);
        mContext = context;
        mVideoView = videoView;
        mActivity = activity;

    }

    @Override
    protected View makeControllerView() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.my_media_controller,this);
        mButtonPlay = (ImageButton) v.findViewById(R.id.id_controller_button_play);
        mButtonPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.mlj("play onclick");
            }
        });
        return v;
    }
}
