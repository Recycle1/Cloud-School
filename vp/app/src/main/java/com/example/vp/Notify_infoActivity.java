package com.example.vp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Notify_infoActivity extends AppCompatActivity {

    private TextView tv_title;
    private TextView tv_time;
    private TextView tv_text;
    private TextView tv_user_name;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_info);
        tv_title=findViewById(R.id.tv_title);
        tv_time=findViewById(R.id.tv_time);
        tv_text=findViewById(R.id.tv_text);
        tv_user_name=findViewById(R.id.tv_user_name);
        imageView=findViewById(R.id.iv_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title.setText(getIntent().getStringExtra("title"));
        tv_time.setText(getIntent().getStringExtra("time"));
        tv_text.setText(getIntent().getStringExtra("text"));
        tv_user_name.setText("发起人："+getIntent().getStringExtra("user_name"));
    }
}