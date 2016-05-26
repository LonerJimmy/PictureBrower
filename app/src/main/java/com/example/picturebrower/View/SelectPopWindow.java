package com.example.picturebrower.View;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.picturebrower.Adapter.ListViewAdapter;
import com.example.picturebrower.Entity.PhotoAibum;
import com.example.picturebrower.Entity.PhotoAll;
import com.example.picturebrower.R;

import java.util.List;

/**
 * Created by lenovo on 2015/8/21.
 */
public class SelectPopWindow extends PopupWindow  {

    private Context context;
    private View conentView;
    private int height,width;
    private int bottomHeight,topHeight;
    private Handler handler=null;
    private List<PhotoAibum> aibumList=null;

    AnimationSet dismissSet=new AnimationSet(true);
    Animation translateAnimation;
    AnimationSet set;
    ListView listView;

    List<PhotoAll> allList=null;

    public SelectPopWindow(final Context context,int width,int height,int bottomHeight,int topHeight, List<PhotoAibum> aibumList,List<PhotoAll> allList, final Handler handler ) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.select_item, null);

        if(allList==null){
            this.allList=null;
        }else{
            this.allList=allList;
        }

        if(aibumList!=null){
            this.aibumList=aibumList;
        }else{
            aibumList=null;
        }
        this.handler=handler;

        this.handler=handler;
        this.context=context;
        this.height=height;
        this.width=width;
        this.bottomHeight=bottomHeight;
        this.topHeight=topHeight;

        //Log.i("jimmy_selectpopwindow", "image_sort=" + image_sort);

        ListView listView= (ListView) conentView.findViewById(R.id.list_select);
        ListViewAdapter adapter=new ListViewAdapter(aibumList,allList,context,width/4,width/4);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Jimmy_selepopwindow", "position=" + position + "   id=" + id);
                //file_sort.get(position);
                Message msg=new Message();
                msg.what=position+1;
                Log.i("Jimmy_selepopwindow", "handler msg=" + msg.what);
                handler.sendMessage(msg);
            }
        });

        showPopupWindow();
        showListView();
    }

    private void showListView(){
        listView = (ListView) conentView.findViewById(R.id.list_select);
    }

    //显示popupwindow
    private void showPopupWindow(){
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(width);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(height-topHeight*2-bottomHeight);

        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        translateAnimation=new TranslateAnimation(0,0,height-topHeight*2-bottomHeight,0);
        translateAnimation.setDuration(300);
        set=new AnimationSet(true);    //创建动画集对象
        set.addAnimation(translateAnimation);
        set.setFillAfter(true);                 //停留在最后的位置
        set.setFillEnabled(true);
        this.getContentView().setAnimation(set);
        set.startNow();

    }

    //弹出popupwindow位置
    public void showPopupWindow(View parent,int topHeight,int bottomHeight) {
        if (!this.isShowing()) {
           // this.showAsDropDown(parent,width, height-bottomHeight-topHeight*2);
            this.showAtLocation(parent, Gravity.BOTTOM | Gravity.LEFT, 0, bottomHeight);

            //this.setAnimationStyle(R.style.AnimationPreview);
        } else {
            this.dismiss();
        }
    }

}
