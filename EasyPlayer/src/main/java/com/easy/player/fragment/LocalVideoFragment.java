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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.easy.player.EasyPlayer;
import com.easy.player.R;
import com.easy.player.activity.FullScreenPlayActivity;
import com.easy.player.database.DBHelper;
import com.easy.player.entity.POMedia;
import com.easy.player.service.MediaScanService;
import com.easy.player.ui.adapter.FileAdapter;
import com.easy.player.utils.ToastManager;
import com.easy.player.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.utils.Log;

import static com.easy.player.service.MediaScanService.SCAN_STATUS_END;
import static com.easy.player.service.MediaScanService.SCAN_STATUS_NORMAL;
import static com.easy.player.service.MediaScanService.SCAN_STATUS_RUNNING;
import static com.easy.player.service.MediaScanService.SCAN_STATUS_START;

/**
 * Created by malijie on 2017/6/23.
 */

public class LocalVideoFragment extends Fragment implements AdapterView.OnItemClickListener, MediaScanService.IMediaObserver{
    private static final String TAG = LocalVideoFragment.class.getSimpleName();
    private MediaScanService mService;
    private ListView mListView = null;
    private DBHelper mDBHelper = null;

    private List<POMedia> mMediaList = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.mlj(TAG,"=====onCreateView====");
        View v = Utils.getView(R.layout.local_video_layout);
        initViews(v);
        initData();


        getActivity().bindService(new Intent(getActivity().getApplicationContext(),MediaScanService.class),
                mMediaServiceConn, Context.BIND_AUTO_CREATE);


        return v;
    }

    private void initData() {
        mDBHelper = new DBHelper();
    }


    private void initViews(View v) {
        mListView = (ListView) v.findViewById(R.id.id_local_video_listview);
        mListView.setOnItemClickListener(this);

    }


    private ServiceConnection mMediaServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((MediaScanService.MediaScanServiceBinder)service).getService();
            mService.addObserver(LocalVideoFragment.this);
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
                Log.mlj(TAG,"SCAN_STATUS_NORMAL");
                break;
            case SCAN_STATUS_START:
                Log.mlj(TAG,"SCAN_STATUS_START");
                break;
            case SCAN_STATUS_END:
                Log.mlj(TAG,"SCAN_STATUS_END");

                mMediaList = mDBHelper.queryForAll(POMedia.class);

                FileAdapter adapter = new FileAdapter(mMediaList);
                mListView.setAdapter(adapter);

                break;
            case SCAN_STATUS_RUNNING:
                Log.mlj(TAG,"SCAN_STATUS_RUNNING");
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(),FullScreenPlayActivity.class);
        i.putExtra("po_media", mMediaList.get(position));
        startActivity(i);
    }
}
