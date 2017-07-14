package com.easy.player.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easy.player.EasyPlayer;
import com.easy.player.R;
import com.easy.player.entity.POMedia;
import com.easy.player.service.MediaScanService;
import com.easy.player.utils.ToastManager;
import com.easy.player.utils.Utils;

import io.vov.vitamio.utils.Log;

import static com.easy.player.service.MediaScanService.SCAN_STATUS_END;
import static com.easy.player.service.MediaScanService.SCAN_STATUS_NORMAL;
import static com.easy.player.service.MediaScanService.SCAN_STATUS_RUNNING;
import static com.easy.player.service.MediaScanService.SCAN_STATUS_START;

/**
 * Created by malijie on 2017/6/23.
 */

public class LocalVideoFragment extends Fragment implements MediaScanService.IMediaObserver{
    private static final String TAG = LocalVideoFragment.class.getSimpleName();
    private MediaScanService mService;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.mlj(TAG,"=====onCreateView====");
        View v = Utils.getView(R.layout.local_video_layout);

        getActivity().bindService(new Intent(getActivity().getApplicationContext(),MediaScanService.class),
                mMediaServiceConn, Context.BIND_AUTO_CREATE);
        return v;
    }


    private ServiceConnection mMediaServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((MediaScanService.MediaScanServiceBinder)service).getService();
            mService.addObserver(LocalVideoFragment.this);
            Log.mlj(TAG,"绑定成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService =null;
        }
    };

    @Override
    public void onDestroy() {
        getActivity().unbindService(mMediaServiceConn);
        super.onDestroy();
    }

    @Override
    public void update(int flag, POMedia media) {
        switch (flag){
            case SCAN_STATUS_NORMAL:

                break;
            case SCAN_STATUS_START:

                break;
            case SCAN_STATUS_END:

                break;
            case SCAN_STATUS_RUNNING:

                break;
        }
    }


}
