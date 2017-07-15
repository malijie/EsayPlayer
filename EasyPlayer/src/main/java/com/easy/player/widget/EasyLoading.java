package com.easy.player.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.view.View;

import com.easy.player.R;
import com.easy.player.utils.Utils;

/**
 * Created by malijie on 2017/7/15.
 */

public class EasyLoading{

    private Context mContext = null;
    private Dialog mDialog = null;

    public EasyLoading(Context context) {
        mContext = context;
        View v = Utils.getView(R.layout.loading_dialog);
        mDialog = new AlertDialog.Builder(context,R.style.loading_dialog)
                .setView(v)
                .create();
        mDialog.setCanceledOnTouchOutside(false);

    }

    public void show(){
        mDialog.show();
    }

    public void hide(){
        mDialog.hide();
    }

}
