package com.example.picturebrower.Adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;

import com.example.picturebrower.Activity.MainActivity;
import com.example.picturebrower.Entity.PhotoAibum;
import com.example.picturebrower.Entity.PhotoAll;
import com.example.picturebrower.Entity.PhotoItem;
import com.example.picturebrower.R;
import com.example.picturebrower.View.PhotoGridItem;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015/8/20.
 */
public class PictureAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private int width;
    private Context context;
    //private ArrayList<HashMap<String, String>> list= new ArrayList<HashMap<String, String>>();
    private ContentResolver cr;
    private boolean isMainGrid=true;

    private PhotoAibum aibum;
    private ArrayList<PhotoItem> gl_arr;//分组图片信息
    private List<PhotoAll> listAll;//所有图片信息
    private int selectNum=0;
    DisplayImageOptions options;
    ImageLoaderConfiguration config;
    public static ImageLoader imageLoader ;

    public PictureAdapter(List<PhotoAll> listAll, PhotoAibum aibum,ArrayList<PhotoItem> gl_arr, Context context,int width)
    {
        super();
        this.context=context;
        this.width=width;

        cr = context.getContentResolver();
        inflater = LayoutInflater.from(context);
        if(listAll==null){
            isMainGrid=false;
            this.aibum = aibum;
            this.gl_arr=gl_arr;
        }else {
            isMainGrid=true;
            this.listAll = listAll;
        }

        if(selectNum>0) {
            Message msg = new Message();
            msg.what = 0;
            MainActivity.setHandler.sendMessage(msg);
        }else{
            Message msg=new Message();
            msg.what=1;
            MainActivity.setHandler.sendMessage(msg);
        }

        imageLoader= ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Cache"); //缓存文件的存放地址
        options = new DisplayImageOptions
                .Builder()
                .resetViewBeforeLoading(true)
                .showImageOnLoading(R.drawable.ic_empty)
                        //.delayBeforeLoading(1000)
                .showStubImage(R.drawable.ic_empty)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                        //.imageScaleType(ImageScaleType)
                        //.cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        config = new ImageLoaderConfiguration
                .Builder(context)
                .defaultDisplayImageOptions(options)
                .memoryCacheExtraOptions(480, 800) // max width, max height
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)  //降低线程的优先级保证主UI线程不受太大影响
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(8 * 1024 * 1024)) //建议内存设在5-10M,可以有比较好的表现
                .memoryCacheSize(5 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .discCache(new UnlimitedDiscCache(cacheDir))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                        //.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)
                .writeDebugLogs() // Remove for release app
                .build();

    }

    @Override
    public int getCount() {
        if(isMainGrid) {
            if (null != listAll) {
                return listAll.size()+1;
            } else {
                return 0;
            }
        }else{
            if (gl_arr==null) {
                return aibum.getBitList().size();
            }else{
                return gl_arr.size();
            }
        }
    }

    @Override
    public Object getItem(int position) {
        if(isMainGrid) {
            if(position==0){
                return null;
            }else {
                return listAll.get(position - 1);
            }
        }else{
            if(gl_arr==null){
                return aibum.getBitList().get(position);
            }else{
                return gl_arr.get(position);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {



        if (isMainGrid) {
            //显示拍照图片

            if (position == 0) {
                final PhotoGridItem item;
                if (convertView == null) {
                    item = new PhotoGridItem(context);
                    //item.getmSelect().clearColorFilter();
                    item.setLayoutParams(new LayoutParams(width,width));

                } else {
                    item = (PhotoGridItem) convertView;
                    //item.removeView(item.getmSelect());
                    //item.getmSelect().clearColorFilter();
                }

                item.getmImageView().setImageResource(R.drawable.pic_photo);

                //item.removeView(item.getmSelect());
                //item.getmSelect().setVisibility(View.GONE);
                return item;
            } else {
                //显示所有照片
                //final ViewHolder viewHolder;
                //viewHolder = new ViewHolder();
                final PhotoGridItem itemAll;

                if (convertView == null) {
                    itemAll = new PhotoGridItem(context);
                    itemAll.setLayoutParams(new LayoutParams(width,width));
                } else {
                    itemAll = (PhotoGridItem) convertView;
                }

                // 通过ID 加载缩略图
                //Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(),  listAll.get(position-1).photoId, MediaStore.Images.Thumbnails.MICRO_KIND, null);

                //convertView = inflater.inflate(R.layout.picture_item, null);
                //convertView.setLayoutParams(new LayoutParams(width, width));//设置每个item长度、宽度
                /*viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
                viewHolder.select = (ImageView) convertView.findViewById(R.id.photo_select);
                convertView.setTag(viewHolder);
*/
                //显示右上角选中图片
                if(listAll.get(position-1).isSelect){
                    itemAll.getmSelect().setImageResource(R.drawable.cb_on);
                }else {
                    itemAll.getmSelect().setImageResource(R.drawable.cb_normal);
                }

                //点击选中或者取消
                itemAll.getmSelect().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isSelected = listAll.get(position - 1).isSelect;
                        if (!isSelected) {
                            itemAll.getmSelect().setImageResource(R.drawable.cb_on);
                            listAll.get(position - 1).isSelect = true;
                            selectNum++;
                            Log.i("jimmy_click", "true");
                        } else {
                            itemAll.getmSelect().setImageResource(R.drawable.cb_normal);
                            listAll.get(position - 1).isSelect = false;
                            selectNum--;
                        }

                        //显示选中不选中右上角图片
                        if (selectNum > 0) {
                            Message msg = new Message();
                            msg.what = 0;
                            Log.e("jimmy_pictureadapter","msg="+msg.what);
                            MainActivity.setHandler.sendMessage(msg);
                        } else {
                            Message msg = new Message();
                            msg.what = 1;
                            Log.e("jimmy_pictureadapter","msg="+msg.what);
                            MainActivity.setHandler.sendMessage(msg);
                        }
                    }
                });

                itemAll.getmImageView().setTag(listAll.get(position-1).photoId);
                itemAll.getmSelect().setTag(position-1);

                //显示缩略图

                    //viewHolder.image.setImageBitmap(ClipBitmap(bitmap));
                    imageLoader.displayImage("file:///"+listAll.get(position-1).realPath,itemAll.getmImageView(),options);


                return itemAll;
            }
        } else {
            //显示分类图片
            final PhotoGridItem itemAibum;

            if (convertView == null) {
                itemAibum = new PhotoGridItem(context);
                itemAibum.setLayoutParams(new LayoutParams(width, width));
            } else {
                itemAibum = (PhotoGridItem) convertView;
            }


                 // 通过ID 加载缩略图
            if (gl_arr==null) {
               // Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(),  aibum.getBitList().get(position).getPhotoID(), MediaStore.Images.Thumbnails.MICRO_KIND, null);
                imageLoader.displayImage("file:///" + aibum.getBitList().get(position).getPath(), itemAibum.getmImageView(), options);
                //itemAibum.getmImageView().setImageBitmap(ClipBitmap(bitmap));
                //item.setChecked(flag);
            }else{
                imageLoader.displayImage("file:///" + gl_arr.get(position).getPath(), itemAibum.getmImageView(), options);
               // Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(),  gl_arr.get(position).getPhotoID(), MediaStore.Images.Thumbnails.MICRO_KIND, null);
                //itemAibum.getmImageView().setImageBitmap(ClipBitmap(bitmap));
            }
            if(aibum.getBitList().get(position).isSelect()){
                itemAibum.getmSelect().setImageResource(R.drawable.cb_on);

            }else {
                itemAibum.getmSelect().setImageResource(R.drawable.cb_normal);
            }

            //点击选中或者取消
            itemAibum.getmSelect().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isSelected = aibum.getBitList().get(position).isSelect();
                    if (!isSelected) {
                        itemAibum.getmSelect().setImageResource(R.drawable.cb_on);
                        aibum.getBitList().get(position).setSelect(true);

                        selectNum++;
                        Log.i("jimmy_click", "true");
                    } else {
                        itemAibum.getmSelect().setImageResource(R.drawable.cb_normal);
                        aibum.getBitList().get(position).setSelect(false);

                        selectNum--;
                    }

                    //显示选中或非选中右上角图片
                    if (selectNum > 0) {
                        Message msg = new Message();
                        msg.what = 0;
                        MainActivity.setHandler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = 1;
                        MainActivity.setHandler.sendMessage(msg);
                    }
                }
            });

            return itemAibum;
        }
    }

    //对要显示的缩略图进行裁剪
    private Bitmap ClipBitmap(Bitmap bitmap){
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        int lastHW=0;

        int wh = width > height ? height : width;// 裁切后所取的正方形区域边长

        int retX = width > height ? (width - height) / 2 : 0;//基于原图，取正方形左上角x坐标
        int retY = width > height ? 0 : (height -width) / 2;
        return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
    }
}
