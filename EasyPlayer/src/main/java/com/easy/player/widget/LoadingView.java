package com.easy.player.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.easy.player.R;
import com.easy.player.utils.Utils;
import com.ldoublem.loadingviewlib.LVCircular;
import com.ldoublem.loadingviewlib.view.LVCircularRing;

/**
 * Created by malijie on 2017/6/25.
 */

public class LoadingView extends LVCircularRing{
    private int mViewColor;
    private int mBarColor;


    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs,defStyleAttr);
    }

    private void initView(Context context,AttributeSet attrs, int defStyleAttr) {
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingView,defStyleAttr,0);
        int n = typeArray.getIndexCount();
        for(int i=0;i<n;i++){
            int attr = typeArray.getIndex(i);
            switch (attr){
                case R.styleable.LoadingView_bar_color:
                    mBarColor = typeArray.getColor(attr, Utils.getColor(R.color.loading_bar));
                    break;

                case R.styleable.LoadingView_view_color:
                    mViewColor = typeArray.getColor(attr, Utils.getColor(R.color.loading_view));
                    break;
            }
        }

        setViewColor(mViewColor);
        setBarColor(mBarColor);
        startAnim();

        typeArray.recycle();
    }

}
