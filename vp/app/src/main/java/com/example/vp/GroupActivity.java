package com.example.vp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GroupActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GroupAdapter groupAdapter;
    private ImageView iv;
    private TextView tv;
    private int my_group=-1;
    int group_num;
    private String class_id;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    groupAdapter=new GroupAdapter(GroupActivity.this,msg.arg1,group_num,msg.arg2, class_id, new GroupAdapter.OnItemClickListener() {
                        @SuppressLint("ResourceType")
                        @Override
                        public void onClick(int position,View v) {
                            my_group=position;
                        }
                    });
                    recyclerView.setAdapter(groupAdapter);
                    break;
                case 2:
                    Toast.makeText(GroupActivity.this, "加入成功", Toast.LENGTH_SHORT).show();
                    setResult(0);
                    finish();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        recyclerView=findViewById(R.id.rv);
        iv=findViewById(R.id.iv_back);
        tv=findViewById(R.id.tv_add);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        group_num=getIntent().getIntExtra("group_num",0);
        class_id=getIntent().getStringExtra("class_id");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(Get_data.touchHtml("https://www.recycle11.top/cloud_classroom/update_group_student.php?group_name="+(my_group+1)+"&group_num="+group_num+"&user_id="+User.user_id).equals("yes")){
                                handler.sendEmptyMessage(2);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String group_information=Get_data.get_group_sum(class_id);
                    int student_num=Integer.valueOf(group_information.split("]")[0]);
                    int group_sum=Integer.valueOf(group_information.split("]")[1]);
                    Message message=new Message();
                    message.what=1;
                    message.arg1=group_sum;
                    message.arg2=student_num;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}