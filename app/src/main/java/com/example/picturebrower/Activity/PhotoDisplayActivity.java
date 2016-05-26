package com.example.picturebrower.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.picturebrower.Gallery.FilePagerAdapter;
import com.example.picturebrower.Gallery.GalleryViewPager;
import com.example.picturebrower.R;

import java.util.ArrayList;
import java.util.List;


public class PhotoDisplayActivity extends ActionBarActivity {

    private class Act_Data{

    }

    private class Act_Ctrl{
        GalleryViewPager m_view;
    }

    Act_Data act_data=new Act_Data();
    Act_Ctrl act_ctrl=new Act_Ctrl();
    public static Handler handler=null;
    String url=null;
    ArrayList<String> list=new ArrayList<String>();
    boolean arrFlag=false;

    int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);

        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
        //获取传值
        Intent intent=this.getIntent();


        final String flag=intent.getStringExtra("flag");
        if(flag.equals("one")){
            arrFlag=true;
            list = intent.getStringArrayListExtra("url");
            position=intent.getExtras().getInt("position");
        }else{
            arrFlag=false;
            list=intent.getStringArrayListExtra("url");
        }

        //获得控件
        act_ctrl.m_view = (GalleryViewPager)this.findViewById(R.id.pd_view);

        FilePagerAdapter pagerAdapter;
        List<String> items = new ArrayList<String>();
        if(arrFlag) {
            items=list;
            pagerAdapter = new FilePagerAdapter(this, items);

        }else{
            items=list;
            pagerAdapter = new FilePagerAdapter(this, items);
        }

        act_ctrl.m_view.setOffscreenPageLimit(3);
        act_ctrl.m_view.setAdapter(pagerAdapter);
        act_ctrl.m_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActReturn();
            }
        });
        act_ctrl.m_view.setCurrentItem(position);//设置当前显示的view

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 0:{
                        Intent intent=new Intent();
                        intent.putExtra("url",list);
                        intent.putExtra("flag",flag);
                        intent.putExtra("position",position);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    }

                }
                super.handleMessage(msg);
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_photo_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            ActReturn();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ActReturn(){
        finish();
    }

    //键盘事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:{//返回键
                ActReturn();
                return false;
            }
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
