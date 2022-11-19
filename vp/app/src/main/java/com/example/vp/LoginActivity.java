package com.example.vp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText user;
    private EditText password;
    private Button btn_login;
    private Button btn_register;
    private Spinner school;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(LoginActivity.this, "学号或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Message message=new Message();
                    message.what=5;
                    //String user_id=(String)msg.obj;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                message.obj=Get_data.get_user(user.getText().toString(),school.getSelectedItem().toString());
                                handler.sendMessage(message);
                            }catch (Exception e) {
                            e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case 5:
                    user_information u=(user_information)msg.obj;
                    User.user_id=u.user_id;
                    User.user_name=u.user_name;
                    User.identity=u.identity;
                    User.gender=u.gender;
                    User.school_id=u.school_id;
                    User.school=u.school;
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    if(User.identity.equals("学生")){
                        setResult(0);
                    }
                    else{
                        setResult(1);
                    }
                    finish();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user=findViewById(R.id.et_account);
        password=findViewById(R.id.et_password);
        btn_login=findViewById(R.id.btn_login);
        btn_register=findViewById(R.id.btn_register);
        school=findViewById(R.id.school);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String user_text=user.getText().toString().trim();
                        String password_text=password.getText().toString().trim();
                        if(user_text.length()==0){
                            handler.sendEmptyMessage(1);
                        }
                        else if(password_text.length()==0){
                            handler.sendEmptyMessage(2);
                        }
                        else{
                            try {
                                String result=Get_data.login(school.getSelectedItem().toString(),user_text,password_text);
                                if(result.equals("没有")){
                                    handler.sendEmptyMessage(3);
                                }
                                else{
                                    Message message=new Message();
                                    message.what=4;
                                    handler.sendEmptyMessage(4);
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