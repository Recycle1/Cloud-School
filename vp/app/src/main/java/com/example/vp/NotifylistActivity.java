package com.example.vp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class NotifylistActivity extends AppCompatActivity {

    private ImageView iv_back;
    private RecyclerView recyclerView;
    private ImageView iv_add;
    private NotifyAdapter notifyAdapter;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    notifyAdapter.setNotifylist((ArrayList<notify_information>)msg.obj);
                    recyclerView.setAdapter(notifyAdapter);
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_list);
        iv_back=findViewById(R.id.iv_back);
        recyclerView=findViewById(R.id.rv);
        iv_add=findViewById(R.id.iv_add);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(User.identity.equals("学生")){
            iv_add.setVisibility(View.GONE);
        }
        else{
            iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(NotifylistActivity.this,NotifyActivity.class);
                    startActivityForResult(intent,0);
                }
            });
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notifyAdapter=new NotifyAdapter(this);
        find();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==0){
            find();
        }
    }

    void find(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<notify_information> n=Get_data.get_notify(User.user_id);
                    Message message=new Message();
                    message.what=1;
                    message.obj=n;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}