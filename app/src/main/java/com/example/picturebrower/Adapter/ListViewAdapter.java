package com.example.picturebrower.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.picturebrower.Entity.PhotoAibum;
import com.example.picturebrower.Entity.PhotoAll;
import com.example.picturebrower.Entity.ViewHolder;
import com.example.picturebrower.R;

import java.util.List;

/**
 * Created by lenovo on 2015/8/21.
 */
public class ListViewAdapter extends BaseAdapter {

    //private HashMap<String,String> img_sort;
    //private ArrayList<String> file_sort;
    //private ArrayList<Integer>file_num;
    private LayoutInflater inflater;
    private Context context;
    private View.OnClickListener myClickListener;
    private int width,height;
    List<PhotoAibum> aibumList;
    List<PhotoAll> allList;

    public ListViewAdapter( List<PhotoAibum> aibumList,List<PhotoAll> allList, Context context,int width,int height)
    {
        super();
        this.width=width;
        this.height=height;
        this.inflater= LayoutInflater.from(context);
        this.context=context;

        if(allList==null){
            this.allList=null;
        }else{
            this.allList=allList;
        }

        if(aibumList!=null){
            this.aibumList=aibumList;
        }else {
            this.aibumList=null;
        }
       /* this.img_sort=img_sort;
        this.file_sort=file_sort;
        this.file_num=file_num;*/
    }

    @Override
    public int getCount() {
        return aibumList.size();
    }

    @Override
    public Object getItem(int position) {
        return aibumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("jimmy_listviewadapter", "position=" + position);
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_adapter, null);
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.img_list_item);

            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
            viewHolder.txtNum = (TextView) convertView.findViewById(R.id.txt_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(position==0){
        //显示所有图片
            Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), aibumList.get(position).getBitmap(), MediaStore.Images.Thumbnails.MICRO_KIND, null);
            viewHolder.image.setImageBitmap(ClipBitmap(bitmap));
            viewHolder.txtTitle.setText("所有图片");
            viewHolder.txtNum.setText("" + allList.size() + "张");

        }else {
            //显示其他分组
            Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), aibumList.get(position).getBitmap(), MediaStore.Images.Thumbnails.MICRO_KIND, null);

            Log.i("jimmy_listviewadapter", "position=" + position);

            viewHolder.image.setImageBitmap(ClipBitmap(bitmap));
            viewHolder.txtTitle.setText(aibumList.get(position).getName());
            viewHolder.txtNum.setText("" + aibumList.get(position).getCount() + "张");
        }
        return convertView;
    }


    private Bitmap ClipBitmap(Bitmap bitmap){
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();

        int wh = width > height ? height : width;// 裁切后所取的正方形区域边长

        int retX = width > height ? (width - height) / 2 : 0;//基于原图，取正方形左上角x坐标
        int retY = width > height ? 0 : (height -width) / 2;
        return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
    }
}
