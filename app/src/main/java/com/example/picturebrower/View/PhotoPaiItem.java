package com.example.picturebrower.View;

/**
 * Created by lenovo on 2015/9/12.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.picturebrower.R;

/**
 * PhotoGridItem
 */

public class PhotoPaiItem extends RelativeLayout {

    private Context mContext;
    private ImageView mImageView;


    public PhotoPaiItem(Context context) {
        super(context, null, 0);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.picture_item, this);
        mImageView = (ImageView)findViewById(R.id.image);

    }

    public PhotoPaiItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoPaiItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.picture_item, this);
        mImageView = (ImageView)findViewById(R.id.image);

    }

    public ImageView getmImageView() {
        return mImageView;
    }

}
