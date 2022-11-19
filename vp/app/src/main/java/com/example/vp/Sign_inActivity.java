package com.example.vp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Sign_inActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SignAdapter signAdapter;
    private ImageView back;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case 1:
                    signAdapter.setSignlist((ArrayList<sign_information>)msg.obj);
                    recyclerView.setAdapter(signAdapter);
                    if(signAdapter.getItemCount()>0){
                        recyclerView.smoothScrollToPosition(signAdapter.getItemCount()-1);
                    }
                    break;
                case 2:
                    ((Button)msg.obj).setEnabled(false);
                    ((Button)msg.obj).setText("已签到");
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        recyclerView=findViewById(R.id.rv);
        back=findViewById(R.id.iv_back);
        signAdapter=new SignAdapter(this, new SignAdapter.OnItemClickListener() {
            @Override
            public void onClick(String class_id, String sign_time, int position, View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("https://www.recycle11.top/cloud_classroom/update_sign.php?class_id="+class_id+"&user_id="+User.user_id+"&sign_time="+sign_time);
                            if(Get_data.touchHtml("https://www.recycle11.top/cloud_classroom/update_sign.php?class_id="+class_id+"&user_id="+User.user_id+"&sign_time="+sign_time).equals("yes")){
                                Message message=new Message();
                                message.what=2;
                                message.obj=v;
                                handler.sendMessage(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<sign_information> s=Get_data.get_sign(User.user_id);
                    Message message=new Message();
                    message.what=1;
                    message.obj=s;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}