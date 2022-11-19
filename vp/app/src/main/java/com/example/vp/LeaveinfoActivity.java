package com.example.vp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class LeaveinfoActivity extends AppCompatActivity {

    private TextView tv_name;
    private TextView tv_start_time;
    private TextView tv_end_time;
    private TextView tv_need;
    private TextView tv_reason;
    private TextView tv_status;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaveinfo);
        tv_name=findViewById(R.id.tv_name);
        tv_start_time=findViewById(R.id.tv_start_time);
        tv_end_time=findViewById(R.id.tv_end_time);
        tv_need=findViewById(R.id.tv_need);
        tv_reason=findViewById(R.id.tv_reason);
        tv_status=findViewById(R.id.tv_status);
        iv=findViewById(R.id.iv);
        tv_name.setText(getIntent().getStringExtra("student_name"));
        tv_start_time.setText(getIntent().getStringExtra("start_time"));
        tv_end_time.setText(getIntent().getStringExtra("end_time"));
        tv_need.setText(getIntent().getStringExtra("need"));
        tv_reason.setText(getIntent().getStringExtra("reason"));
        int status=getIntent().getIntExtra("status",0);
        if(status==0){
            tv_status.setText("等待审批");
        }
        else if(status==1){
            tv_status.setText("已通过");
        }
        else if(status==2){
            tv_status.setText("未通过");
        }
        else if(status==3){
            tv_status.setText("已过期");
        }
        Glide.with(this).load(getIntent().getStringExtra("leave_photo")).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LeaveinfoActivity.this,Big_imageActivity.class);
                intent.putExtra("image",getIntent().getStringExtra("leave_photo"));
                startActivity(intent);
            }
        });

    }
}