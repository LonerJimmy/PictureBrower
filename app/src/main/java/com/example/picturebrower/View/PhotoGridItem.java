package com.example.picturebrower.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.picturebrower.R;


/**
 * PhotoGridItem
 */

public class PhotoGridItem extends RelativeLayout implements Checkable {

    private Context mContext;
    private boolean mCheck;
    private ImageView mImageView;
    private ImageView mSelect;

    public PhotoGridItem(Context context) {
        super(context, null, 0);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.photoalbum_gridview_item, this);
        mImageView = (ImageView)findViewById(R.id.photo_img_view);
        mSelect = (ImageView)findViewById(R.id.photo_grid_select);

    }


    public PhotoGridItem(Context context, int i) {
        this(context, null, 0);
        LayoutInflater.from(mContext).inflate(R.layout.photoalbum_gridview_item, this);
        mImageView = (ImageView)findViewById(R.id.photo_img_view);
        mSelect = (ImageView)findViewById(R.id.photo_grid_select);
        if(i==1) {

            mSelect.setVisibility(View.GONE);
        }
    }

    public PhotoGridItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.photoalbum_gridview_item, this);
        mImageView = (ImageView)findViewById(R.id.photo_img_view);
        mSelect = (ImageView)findViewById(R.id.photo_grid_select);
    }

    public ImageView getmSelect() {
        return mSelect;
    }

    public ImageView getmImageView() {
        return mImageView;
    }


    /**
     * Checkable
     */
    @Override
    public void setChecked(boolean checked) {
        mCheck = checked;
        System.out.println(checked);
        //if(checked) {
        mSelect.setImageDrawable(checked ? getResources().getDrawable(R.drawable.cb_on) : getResources().getDrawable(R.drawable.cb_normal));
        //}
    }

    @Override
    public boolean isChecked() {
        return mCheck;
    }

    @Override
    public void toggle() {
        setChecked(!mCheck);
    }

    public void setImgResID(int id){
        if(mImageView != null){
            mImageView.setBackgroundResource(id);
        }
    }

    public void SetBitmap(Bitmap bit){
        if(mImageView != null){
            mImageView.setImageBitmap(bit);
        }
    }
}
