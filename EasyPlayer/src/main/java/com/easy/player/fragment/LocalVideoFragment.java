package com.easy.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easy.player.R;
import com.easy.player.utils.Utils;

/**
 * Created by malijie on 2017/6/23.
 */

public class LocalVideoFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = Utils.getView(R.layout.local_video_layout);
        return v;
    }
}
