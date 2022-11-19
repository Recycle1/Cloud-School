package com.example.vp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NotifyActivity extends AppCompatActivity {

    private EditText et_title;
    private EditText et_text;
    private Button btn_back;
    private Button btn_add;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(NotifyActivity.this, "标题为空", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(NotifyActivity.this, "内容为空", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(NotifyActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_notify);
        et_title=findViewById(R.id.et_title);
        et_text=findViewById(R.id.et_text);
        btn_back=findViewById(R.id.btn_back);
        btn_add=findViewById(R.id.btn_add);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String title=et_title.getText().toString();
                        String text=et_text.getText().toString();
                        if(title.length()==0){
                            handler.sendEmptyMessage(1);
                        }
                        else if(text.length()==0){
                            handler.sendEmptyMessage(2);
                        }
                        else{
                            try {
                                if(Get_data.touchHtml("https://www.recycle11.top/cloud_classroom/insert_notify.php?user_id="+User.user_id+"&title="+title+"&text="+text).equals("yes")){
                                    handler.sendEmptyMessage(3);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
    }
}