package com.example.vp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ClasslistActivity extends AppCompatActivity {

    private TextView tv_category[]=new TextView[8];
    private ClassListAdapter classListAdapter;
    private RecyclerView recyclerView;
    private ImageView imageView;
    private EditText editText;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    ArrayList<class_information> classlist=(ArrayList<class_information>)msg.obj;
                    classListAdapter.setClasslist(classlist);
                    recyclerView.setAdapter(classListAdapter);
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classlist);
        tv_category[0]=findViewById(R.id.tv_english);
        tv_category[1]=findViewById(R.id.tv_economic);
        tv_category[2]=findViewById(R.id.tv_computer);
        tv_category[3]=findViewById(R.id.tv_math);
        tv_category[4]=findViewById(R.id.tv_medicine);
        tv_category[5]=findViewById(R.id.tv_chemistry);
        tv_category[6]=findViewById(R.id.tv_music);
        tv_category[7]=findViewById(R.id.tv_machinery);
        recyclerView=findViewById(R.id.rv);
        imageView=findViewById(R.id.iv);
        editText=findViewById(R.id.et);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=editText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ArrayList<class_information> classlist=Get_data.getclasslist(null, s);
                            Message message=new Message();
                            message.what=1;
                            message.obj=classlist;
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        int index=getIntent().getIntExtra("index",-1);
        String key=new String();
        key=null;
        key=getIntent().getStringExtra("key");
        if(index<0){
            String finalKey = key;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ArrayList<class_information> classlist=Get_data.getclasslist(null, finalKey);
                        Message message=new Message();
                        message.what=1;
                        message.obj=classlist;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else{
            tv_category[index].setTextColor(Color.parseColor("#000000"));
            TextPaint tp = tv_category[index].getPaint();
            tp.setFakeBoldText(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ArrayList<class_information> classlist=Get_data.getclasslist(tv_category[index].getText().toString(),null);
                        Message message=new Message();
                        message.what=1;
                        message.obj=classlist;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        classListAdapter=new ClassListAdapter(this, new ClassListAdapter.OnItemClickListener() {
            @Override
            public void onClick(String class_id, int position) {
                Intent intent=new Intent(ClasslistActivity.this,ClassinfoActivity.class);
                intent.putExtra("class_id",class_id);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for(int i=0;i<8;i++){
            tv_category[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int j=0;j<8;j++){
                        tv_category[j].setTextColor(Color.parseColor("#7A7676"));
                        TextPaint tp = tv_category[j].getPaint();
                        tp.setFakeBoldText(false);
                    }
                    ((TextView)v).setTextColor(Color.parseColor("#000000"));
                    TextPaint tp = ((TextView)v).getPaint();
                    tp.setFakeBoldText(true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                List<class_information> classlist=Get_data.getclasslist(((TextView)v).getText().toString(),null);
                                Message message=new Message();
                                message.what=1;
                                message.obj=classlist;
                                handler.sendMessage(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
        }
    }
}