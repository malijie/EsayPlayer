package com.easy.player.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easy.player.R;
import com.easy.player.entity.POMedia;
import com.easy.player.utils.ToastManager;
import com.easy.player.utils.Utils;

import java.util.List;

import io.vov.vitamio.utils.Log;

/**
 * Created by malijie on 2017/7/14.
 */

public class FileAdapter extends BaseAdapter{
    private List<POMedia> mMediaList = null;

    public FileAdapter (List<POMedia> medias){
        mMediaList = medias;
    }

    @Override
    public int getCount() {
        return mMediaList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMediaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = Utils.getView(R.layout.local_video_list_item);
            holder = new ViewHolder();
            holder.mImageThumb = (ImageView) convertView.findViewById(R.id.id_local_item_image_thumb);
            holder.mTextTitle = (TextView) convertView.findViewById(R.id.id_local_item_text_title);
            holder.mTextSize = (TextView) convertView.findViewById(R.id.id_local_item_text_size);
            convertView.setTag(holder);

        }else{
             holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextTitle.setText(mMediaList.get(position).title);

        return convertView;
    }

    public static class ViewHolder{
        public ImageView mImageThumb = null;
        public TextView mTextTitle = null;
        public TextView mTextSize = null;
    }
}