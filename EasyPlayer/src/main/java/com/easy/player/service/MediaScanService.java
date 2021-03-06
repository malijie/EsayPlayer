package com.easy.player.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.easy.player.activity.MainActivity;
import com.easy.player.config.Profile;
import com.easy.player.database.DBHelper;
import com.easy.player.entity.POMedia;
import com.easy.player.fragment.LocalVideoFragment;
import com.easy.player.utils.FileUtils;
import com.easy.player.utils.PinyinUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.vov.vitamio.utils.Log;

/**
 * Created by malijie on 2017/7/10.
 */

public class MediaScanService extends Service implements Runnable{
    public static final String TAG = MediaScanService.class.getSimpleName();
    private String mScanDirectory;

    private ConcurrentHashMap<String,String> mScanMap = new ConcurrentHashMap<String, String>();
    private Map<String, Object> mDbWhere = new HashMap<String, Object>(2);
    private ArrayList<IMediaObserver> observers = new ArrayList<>();

    private volatile int SERVICE_SCAN_STATUS = SCAN_STATUS_NORMAL ;
    public static final int SCAN_STATUS_NORMAL = 0;
    public static final int SCAN_STATUS_START = 1;
    public static final int SCAN_STATUS_END = 2;
    public static final int SCAN_STATUS_RUNNING = 3;

    private DBHelper<POMedia> mDBHelper = null;

    public MediaScanServiceBinder mBinder = new MediaScanServiceBinder();

    public class MediaScanServiceBinder extends Binder{
        public MediaScanService getService(){
            return MediaScanService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.mlj(TAG,"=====onCreate====");
        mDBHelper = new DBHelper<>();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.mlj(TAG,"=====onStartCommand====");
        if(intent != null){
            mDBHelper.clearTable(POMedia.class);
            mScanDirectory = intent.getStringExtra(Profile.getScanDirectoryKey());

            if (!mScanMap.containsKey(mScanDirectory)){
                mScanMap.put(mScanDirectory, "");
            }

            if(SERVICE_SCAN_STATUS == SCAN_STATUS_NORMAL || SERVICE_SCAN_STATUS == SCAN_STATUS_END){
                new Thread(this).start();
            }
        }



        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void run() {
        scan();
    }

    private void scan(){
        Log.mlj(TAG,"=====scan====");
        while (mScanMap.keySet().size() > 0) {
            String path = "";
            for (String key : mScanMap.keySet()) {
                path = key;
                break;
            }

            if (mScanMap.containsKey(path)) {

                String mimeType = mScanMap.get(path);
                if ("".equals(mimeType)) {
                    scanDirectory(path);
                } else {
                    scanFile(path, mimeType);
                }
                //扫描完成一个
                mScanMap.remove(path);
            }

            //任务之间歇息一秒
        }

        notifyObservers(SCAN_STATUS_END,null);
        stopSelf();
    }

    /** 扫描文件 */
    private void scanFile(String path, String mimeType) {
        save(new POMedia(path, mimeType));
    }

    /** 扫描文件夹 */
    private void scanDirectory(String path) {
        eachAllMedias(new File(path));
    }

    /** 递归查找视频 */
    private void eachAllMedias(File f) {
        if (f != null && f.exists() && f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null) {
                for (File file : f.listFiles()) {
                    if (file.isDirectory()) {
                        //忽略.开头的文件夹
                        if (!file.getAbsolutePath().startsWith("."))
                            eachAllMedias(file);
                    } else if (file.exists() && file.canRead() && FileUtils.isVideo(file)) {
                        save(new POMedia(file));
                    }
                }
            }
        }
    }

    /**
     * 保存入库
     *
     * @throws FileNotFoundException
     */
    private void save(POMedia media) {
        mDbWhere.put("path", media.path);
        mDbWhere.put("last_modify_time", media.last_modify_time);
        //检测
        if (!mDBHelper.exists(media, mDbWhere)) {
            try {
                if (media.title != null && media.title.length() > 0)
                    media.title_key = PinyinUtils.chineneToSpell(media.title.charAt(0) + "");
            } catch (Exception ex) {

            }
            media.last_access_time = System.currentTimeMillis();

            //提取缩略图
            //			extractThumbnail(media);
            media.mime_type = FileUtils.getMimeType(media.path);

            //入库
            mDBHelper.create(media);

            //扫描到一个
            notifyObservers(SCAN_STATUS_RUNNING, media);
        }
    }

    private void notifyObservers(int flag, POMedia media) {
        mHandler.sendMessage(mHandler.obtainMessage(flag,media));
    }

    public void addObserver(IMediaObserver observer){
        synchronized (this){
            if(!observers.contains(observer)){
                observers.add(observer);
            }
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            for(int i=0;i<observers.size();i++){
                IMediaObserver observer = observers.get(i);
                observer.update(msg.what,(POMedia)msg.obj);
            }

        }
    };

    public interface IMediaObserver{
        void update(int flag,POMedia media);
    }
}
