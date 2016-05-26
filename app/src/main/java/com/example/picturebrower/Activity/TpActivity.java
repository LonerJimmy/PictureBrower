package com.example.picturebrower.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.picturebrower.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 2015/8/25.
 */
public class TpActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tp);

        ArrayList<String> arrayList=getIntent().getStringArrayListExtra("url");

        TextView txt=(TextView)findViewById(R.id.txt_list);
        String list=null;
        for(int x=0;x<arrayList.size();x++){
            list=list+"-----------"+arrayList.get(x);
        }
        txt.setText(list);
    }
}
