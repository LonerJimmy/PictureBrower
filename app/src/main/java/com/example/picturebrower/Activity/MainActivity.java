package com.example.picturebrower.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.picturebrower.Adapter.PictureAdapter;
import com.example.picturebrower.Entity.PhotoAibum;
import com.example.picturebrower.Entity.PhotoAll;
import com.example.picturebrower.Entity.PhotoItem;
import com.example.picturebrower.R;
import com.example.picturebrower.View.SelectPopWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements View.OnClickListener {

    private GridView gridView;
    // private List<PhotoAibum> aibumList;
    private Button selectButton;
    private static Button sendButton;
    private static Button checkButton;
    private Button backButton;
    private int width,height;
    private int bottomHeight,topHeight;
    private LinearLayout layoutTop,layoutBottom;
    private RelativeLayout layoutAll;
    private String title=null;
    SelectPopWindow morePopWindow=null;
    private Context context;
    private ArrayList<PhotoItem> gl_arr=new ArrayList<PhotoItem>();
    private PhotoAibum aibum=new PhotoAibum();

    private boolean isAllPhtoDisplay=true;//所有图片或者分类图片界面

    PictureAdapter adapter;
    private boolean isReturn=false;
    List<PhotoAll> list=new ArrayList<PhotoAll>();

    List<PhotoAibum> aibumList = new ArrayList<PhotoAibum>();//存储分类照片信息数组
    private List<PhotoAll> listAll=new ArrayList<PhotoAll>();//存储所有照片信息数组

    String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID,
            Thumbnails.DATA };

    String columns[] = new String[] { Media.DATA, Media._ID, Media.TITLE, Media.DISPLAY_NAME, Media.SIZE };

    // 设置获取图片的字段信
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media.DISPLAY_NAME, // 显示的名�?
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.LONGITUDE, // 经度
            MediaStore.Images.Media._ID, // id
            MediaStore.Images.Media.BUCKET_ID, // dir id 目录
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME // dir name 目录名字
    };

    //HashMap<String,String> image_sort=new HashMap<String,String>();
    //ArrayList<String> file_sort=new ArrayList<String>();
    //ArrayList<Integer> file_num=new ArrayList<Integer>();

    private ContentResolver cr;
    // private ArrayList<HashMap<String, String>> list;//缩略图img_id和path
    // private ArrayList<HashMap<String, String>> realList;//原照片的id和path


    Timer timer = new Timer();

    //返回分类图片信息
    private List<PhotoAibum> getPhotoAlbum() {

        //Cursor cursor = MediaStore.Images.Media.query(getContentResolver(),
        //      MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES);
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                STORE_IMAGES, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
        // Cursor cursor1=Thumbnails.query(getContentResolver(),Thumbnails.EXTERNAL_CONTENT_URI, projection);
        // Cursor cursor1 = getContentResolver().query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
        //       null, null, null);
        Map<String, PhotoAibum> countMap = new HashMap<String, PhotoAibum>();
        PhotoAibum pa = null;
        //int dataColumn = cursor1.getColumnIndex(Thumbnails.DATA);
        cursor.moveToFirst();

        do {
            String realPath=cursor.getString(1);//真实图片的存储地址
            String id = cursor.getString(3);//真实图片的id，可以根据这个id获取缩略图的信息
            String dir_id = cursor.getString(4);//分组id
            String dir = cursor.getString(5);//图片名字

            //String path = cursor1.getString(dataColumn);//缩略图地址

            Log.e("info", "id==="+id+"==dir_id=="+dir_id+"==dir=="+dir+"==path="+realPath);
            if (!countMap.containsKey(dir_id)) {
                pa = new PhotoAibum();
                pa.setName(dir);
                pa.setBitmap(Integer.parseInt(id));
                pa.setCount("1");
                pa.getBitList().add(new PhotoItem(Integer.valueOf(id),realPath));
                countMap.put(dir_id, pa);
            } else {
                pa = countMap.get(dir_id);
                pa.setCount(String.valueOf(Integer.parseInt(pa.getCount()) + 1));
                pa.getBitList().add(new PhotoItem(Integer.valueOf(id),realPath));
            }
        } while (cursor.moveToNext());
        cursor.close();
        Iterable<String> it = countMap.keySet();
        for (String key : it) {
            aibumList.add(countMap.get(key));
        }
        return aibumList;
    }

    //返回所有图片信息
    private List<PhotoAll> getPotoAll() {

        //Cursor cursor = MediaStore.Images.Media.query(getContentResolver(),
        //    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES);
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                STORE_IMAGES, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
        int x=0;
        cursor.moveToFirst();

        do{
            String realPath = cursor.getString(1);//真实图片存储地址
            String id = cursor.getString(3);//真实图片id

            Log.i("jimmy_main","get realpath="+realPath);
            PhotoAll photoAll=new PhotoAll();
            //photoAll.path=path;

            File parentFile = new File(realPath).getParentFile();
            if(realPath!=null && new File(realPath).exists()) {
                photoAll.photoId = Integer.parseInt(id);
                photoAll.realPath = realPath;
                photoAll.isSelect = false;
                String[] projection1 = { MediaStore.Images.Thumbnails.IMAGE_ID,
                MediaStore.Images.Thumbnails.DATA };
                Cursor cursor1 = getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection1,
                        MediaStore.Images.Thumbnails.IMAGE_ID + " = ?", new String[]{""+photoAll.photoId}, null, null);
                if(cursor1.moveToFirst()){
                    photoAll.thumbilPath=cursor1.getString(1);
                }
                list.add(photoAll);
            }
        }while (cursor.moveToNext() );

        return list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;
        layoutAll=(RelativeLayout)findViewById(R.id.layout_all);
        layoutTop=(LinearLayout)findViewById(R.id.layout_top);
        layoutBottom=(LinearLayout)findViewById(R.id.layout_bottom);
        layoutBottom.getBackground().setAlpha(220);

        selectButton=(Button)findViewById(R.id.btn_select);
        sendButton=(Button)findViewById(R.id.btn_send);
        checkButton=(Button)findViewById(R.id.btn_check);
        backButton=(Button)findViewById(R.id.btn_back);
        init();

        TimerTask task = new TimerTask(){
            public void run() {
                Message message = new Message();
                message.what = 0;
                myHandler.sendMessage(message);
            }
        };
        //延迟每次延迟10 毫秒 隔1秒执行一次
        timer.schedule(task, 10, 1000);
    }

    private void init(){

        listAll = getPotoAll();//获取所有图片信息，存储到listAll
        aibumList = getPhotoAlbum();//获取分类图片信息，存储到aibumList

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        gridView = (GridView) findViewById(R.id.gridview);
        cr=getContentResolver();

        setAllPhotoDisplay();//初始化显示所有照片

        //图片点击触发事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(isAllPhtoDisplay) {
                    if (position == 0) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    } else {
                        //Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(),  aibum.getBitList().get(position).getPhotoID(), MediaStore.Images.Thumbnails.MICRO_KIND, null);
                        String p = listAll.get(position - 1).realPath;
                        ArrayList<String> list=new ArrayList<String>();
                        for(int x=0;x<listAll.size();x++){
                            list.add(x,listAll.get(x).realPath);
                        }

                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, PhotoDisplayActivity.class);
                        intent.putExtra("url", list);
                        intent.putExtra("flag", "one");
                        intent.putExtra("position",position-1);
                        startActivity(intent);
                    }
                }else{
                    String q=aibum.getBitList().get(position).getPath();
                    ArrayList<String> list=new ArrayList<String>();
                    for(int x=0;x<aibum.getBitList().size();x++){
                        list.add(x,aibum.getBitList().get(x).getPath());
                    }
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, PhotoDisplayActivity.class);
                    intent.putExtra("url", list);
                    intent.putExtra("flag","one");
                    intent.putExtra("position",position);
                    startActivity(intent);
                }

                Toast.makeText(MainActivity.this, "pic" + (position + 1), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0) {
                if (layoutBottom.getHeight() != 0) {
                    bottomHeight = layoutBottom.getHeight();
                    topHeight = layoutTop.getHeight();
                    Log.i("jimmy_th", "bt=" + bottomHeight + "  " + topHeight);
                    //取消定时器
                    timer.cancel();
                }
            }else{
                // title=file_sort.get(msg.what-1);
                if(msg.what==1){
                    //findViews();
                    setAllPhotoDisplay();
                }else {
                    aibum = aibumList.get(msg.what - 1);
                    Log.e("jimmy_mainactivity", "handler aibum=" + aibumList);
                    for (int i = 0; i < aibum.getBitList().size(); i++) {
                        if (aibum.getBitList().get(i).isSelect()) {
                            //chooseNum++;
                        }
                    }

                    setPartPhotoDisplay();
                    //   Log.e("jimmy_MainActivity","get aibumList"+aibumList);

                    // list=getNewList(title);
                    // PictureAdapter adapter = new PictureAdapter(list, context,width/3);
                    // gridView.setAdapter(adapter);
                    //Log.e("jimmy_mainactivity", "handler list=" + list);
                }
            }
        }
    };

    public static  Handler setHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    checkButton.setBackgroundResource(R.drawable.main_btn_green_light);
                    sendButton.setBackgroundResource(R.drawable.main_btn_green_light);
                    sendButton.setEnabled(true);
                    checkButton.setEnabled(true);

                    break;
                case 1:
                    checkButton.setBackgroundColor(00000000);
                    sendButton.setBackgroundColor(00000000);
                    sendButton.setEnabled(false);
                    checkButton.setEnabled(false);

                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        }
    };

    private void setAllPhotoDisplay(){

        adapter = new PictureAdapter(listAll, null, null, context, width / 3);
        gridView.setAdapter(adapter);
        if (morePopWindow != null)
            morePopWindow.dismiss();

        isAllPhtoDisplay = true;

    }

    private void setPartPhotoDisplay(){
        if (morePopWindow != null)
            morePopWindow.dismiss();
        Log.e("jimmy_mainactivity", "handler aibum=" + aibum);
        adapter = new PictureAdapter(null, aibum, null, context, width / 3);
        gridView.setAdapter(adapter);
        isAllPhtoDisplay=false;
    }

    /**
     *
     */
    @Override
    public void onResume(){
        super.onResume();

        backButton.setOnClickListener(this);
        selectButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);
        checkButton.setOnClickListener(this);

        if(isReturn){
            sendButton.setEnabled(true);
            checkButton.setEnabled(true);
        }else {
            //init();
            Log.e("jimmy_main", "onresume");
            Log.e("jimmy_main","isreturn"+isReturn);
            sendButton.setEnabled(false);
            checkButton.setEnabled(false);
            checkButton.setBackgroundColor(00000000);
            sendButton.setBackgroundColor(00000000);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //选择相册
            case R.id.btn_select:
                //Log.e("Jimmy_main", "image_sort=" + image_sort);
                morePopWindow = new SelectPopWindow(MainActivity.this,width,height,bottomHeight,topHeight,aibumList,listAll,myHandler);
                // Log.i("Jimmy_main", "bottomH=" + bottomHeight + "toph=" + topHeight + "width=" + width + "height=" + height);
                morePopWindow.showPopupWindow(layoutBottom, topHeight, bottomHeight);
                break;

            //发送图片
            case R.id.btn_send:

                ArrayList<String> pList=new ArrayList<String>();//存储图片地址

                if(isAllPhtoDisplay) {
                    for(int x=0;x<listAll.size();x++){
                        if(listAll.get(x).isSelect==true){
                            pList.add(listAll.get(x).realPath);
                            listAll.get(x).isSelect=false;
                        }
                    }
                }else{
                    for(int x=0;x<aibum.getBitList().size();x++){
                        if(aibum.getBitList().get(x).isSelect()==true){
                            //存储图片地址
                            pList.add(aibum.getBitList().get(x).getPath());
                            aibum.getBitList().get(x).setSelect(false);
                        }
                    }
                }

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TpActivity.class);

                //发送图片地址
                intent.putExtra("url", pList);
                startActivityForResult(intent, 0);

                Log.e("jimmy_mainactivity", "pList=" + pList);
                break;
            case R.id.btn_check:

                ArrayList<String> qList=new ArrayList<String>();//存储查看照片地址

                if(isAllPhtoDisplay) {
                    for(int x=0;x<listAll.size();x++){
                        if(listAll.get(x).isSelect==true){
                            qList.add(listAll.get(x).realPath);
                            // listAll.get(x).isSelect=false;
                        }
                    }
                }else{
                    for(int x=0;x<aibum.getBitList().size();x++){
                        if(aibum.getBitList().get(x).isSelect()==true){
                            qList.add(aibum.getBitList().get(x).getPath());
                            //.aibum.getBitList().get(x).setSelect(false);
                        }
                    }
                }

                Intent intent1 = new Intent();
                intent1.setClass(MainActivity.this, PhotoDisplayActivity.class);
                //发送查看图片地址
                intent1.putExtra("url", qList);
                intent1.putExtra("flag", "more");
                startActivityForResult(intent1,0);
                break;
            case R.id.btn_back:
                break;
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                Log.e("jimmy_main","onresult ok");
                Bundle b=data.getExtras(); //data为B中回传的Intent
                String str=b.getString("flag");//str即为回传的值
                ArrayList<String> list=b.getStringArrayList("url");
                int flag=b.getInt("flag");
                isReturn=true;
                Log.e("jimmy_main","onresult ok"+isReturn);
                break;
            default:
                break;
        }
    }

}

