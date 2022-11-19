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

public class LeavelistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LeaveAdapter leaveAdapter;
    private ImageView back;
    private ImageView add;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case 1:
                    leaveAdapter.setLeavelist((ArrayList<leave_information>)msg.obj);
                    recyclerView.setAdapter(leaveAdapter);
                    if(leaveAdapter.getItemCount()>0){
                        recyclerView.smoothScrollToPosition(leaveAdapter.getItemCount()-1);
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leavelist);
        recyclerView=findViewById(R.id.rv);
        add=findViewById(R.id.iv_add);
        back=findViewById(R.id.iv_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        leaveAdapter=new LeaveAdapter(this, new ClassListAdapter.OnItemClickListener() {
            @Override
            public void onClick(String class_id, int position) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(User.identity.equals("学生")){
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(LeavelistActivity.this,LeaveActivity.class);
                    startActivityForResult(intent,0);
                }
            });
        }
        else{
            add.setVisibility(View.GONE);
        }


        find();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            find();
        }
    }

    void find(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int identity;
                    if(User.identity.equals("学生")){
                        identity=0;
                    }
                    else{
                        identity=1;
                    }
                    ArrayList<leave_information> l=Get_data.get_leave(User.user_id,identity);
                    Message message=new Message();
                    message.what=1;
                    message.obj=l;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}