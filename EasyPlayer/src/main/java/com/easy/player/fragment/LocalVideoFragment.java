package com.easy.player.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.easy.player.R;
import com.easy.player.activity.FullScreenPlayActivity;
import com.easy.player.database.DBHelper;
import com.easy.player.entity.POMedia;
import com.easy.player.service.MediaScanService;
import com.easy.player.ui.adapter.FileAdapter;
import com.easy.player.utils.Utils;
import com.easy.player.utils.VideoUtils;
import com.easy.player.widget.EasyLoading;

import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.utils.Log;

import static com.easy.player.service.MediaScanService.SCAN_STATUS_END;
import static com.easy.player.service.MediaScanService.SCAN_STATUS_NORMAL;
import static com.easy.player.service.MediaScanService.SCAN_STATUS_RUNNING;
import static com.easy.player.service.MediaScanService.SCAN_STATUS_START;
import static com.easy.player.service.MediaScanService.TAG;

/**
 * Created by malijie on 2017/6/23.
 */

public class LocalVideoFragment extends Fragment implements AdapterView.OnItemClickListener, MediaScanService.IMediaObserver{
    private static final String TAG = LocalVideoFragment.class.getSimpleName();
    private MediaScanService mService;
    private Activity mActivity = null;
    private Context mContext = null;
    private ListView mListView = null;

    private DBHelper mDBHelper = null;
    private FileAdapter mAdapter;

    private List<POMedia> mMediaList = null;
    private EasyLoading loading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.mlj(TAG,"=====onCreateView====");
        View v = Utils.getView(R.layout.local_video_layout);
        mActivity = getActivity();
        mContext = getContext();
        initViews(v);
        initData();


        mActivity.bindService(new Intent(mActivity.getApplicationContext(),MediaScanService.class),
                mMediaServiceConn, Context.BIND_AUTO_CREATE);


        return v;
    }

    private void initData() {
        mDBHelper = new DBHelper();
    }


    private void initViews(View v) {
        loading = new EasyLoading(mActivity);
        loading.show();
        mListView = (ListView) v.findViewById(R.id.id_local_video_listview);
        mListView.setOnItemClickListener(this);
    }


    private ServiceConnection mMediaServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.mlj(TAG,"======onServiceConnected=======");
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
        mActivity.unbindService(mMediaServiceConn);
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
                new ThumbTask(mMediaList).execute();
                loading.hide();

                break;
            case SCAN_STATUS_RUNNING:
                Log.mlj(TAG,"SCAN_STATUS_RUNNING");
                break;
        }
    }

    private class ThumbTask extends AsyncTask<Void,Void,List<Bitmap>>{
        private List<POMedia> mMediaList = null;
        private List<Bitmap> mBitmaps = new ArrayList<>();

        public ThumbTask(List<POMedia> mediaList){
            mMediaList = mediaList;
        }
        @Override
        protected List<Bitmap> doInBackground(Void... params) {
            for(int i=0; i<mMediaList.size();i++){
                mBitmaps.add(VideoUtils.getThumbNail(mMediaList.get(i).path));
            }
            return mBitmaps;
        }

        @Override
        protected void onPostExecute(List<Bitmap> bitmaps) {
            mAdapter = new FileAdapter(mContext,mMediaList);
            mAdapter.setThumbListener(new FileAdapter.ThumbListener() {
                @Override
                public void update(ImageView imageview,int position) {
                    imageview.setImageBitmap(mBitmaps.get(position));
                    mAdapter.notifyDataSetChanged();
                }
            });

            mListView.setAdapter(mAdapter);
            super.onPostExecute(bitmaps);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(mActivity,FullScreenPlayActivity.class);
        i.putExtra("po_media", mMediaList.get(position));
        startActivity(i);
    }
}
