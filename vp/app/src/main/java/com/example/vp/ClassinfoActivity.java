package com.example.vp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.util.QrCodeGenerator;

import java.util.regex.Pattern;

public class ClassinfoActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView tv_name;
    private TextView tv_school;
    private TextView tv_teacher;
    private TextView tv_introduce;
    private ImageView iv_zxing;
    private RelativeLayout rl_group;
    private Button btn_group;
    private TextView tv_group_title;
    private TextView tv_group;
    private Button btn;
    private String class_id;
    private AlertDialog dialog;
    private class_information classInformation;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    classInformation=(class_information)msg.obj;
                    Glide.with(ClassinfoActivity.this).load(classInformation.picture).into(imageView);
                    tv_name.setText(classInformation.class_name);
                    tv_school.setText(classInformation.school);
                    tv_teacher.setText(classInformation.user_name);
                    tv_introduce.setText(classInformation.introduce);
                    generateQrCode(classInformation.class_id);
                    break;
                case 2:
                    Toast.makeText(ClassinfoActivity.this, "加入课程失败", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    rl_group.setVisibility(View.VISIBLE);
                    btn_group.setVisibility(View.VISIBLE);
                    int group_num=msg.arg1;
                    btn_group.setText("分组任务");
                    btn_group.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(ClassinfoActivity.this,GroupActivity.class);
                            intent.putExtra("group_num",group_num);
                            intent.putExtra("class_id",class_id);
                            startActivityForResult(intent,0);
                        }
                    });
                    break;
                case 4:
                    rl_group.setVisibility(View.VISIBLE);
                    tv_group.setVisibility(View.VISIBLE);
                    btn_group.setVisibility(View.GONE);
                    tv_group_title.setVisibility(View.VISIBLE);
                    tv_group.setText("第"+(String)msg.obj+"组");
                    break;
                case 5:
                    rl_group.setVisibility(View.VISIBLE);
                    btn_group.setVisibility(View.VISIBLE);
                    int student_num=msg.arg1;
                    btn_group.setText("发起分组");
                    btn_group.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder=new AlertDialog.Builder(ClassinfoActivity.this);
                            View view= LayoutInflater.from(ClassinfoActivity.this).inflate(R.layout.layout_group_ask,null);
                            EditText et_group=view.findViewById(R.id.et_group_sum);
                            Button btnLogin=view.findViewById(R.id.btn_group_ask);
                            dialog=builder.setTitle("分组任务").setView(view).show();
                            btnLogin.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String num=et_group.getText().toString();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try{
                                                if(!isInteger(num)||num.length()==0){
                                                   handler.sendEmptyMessage(6);
                                                }
                                                else if(Integer.valueOf(num)<2){
                                                    handler.sendEmptyMessage(7);
                                                }
                                                else if(Integer.valueOf(num)>student_num){
                                                    handler.sendEmptyMessage(8);
                                                }
                                                else if(Get_data.touchHtml("https://www.recycle11.top/cloud_classroom/insert_group.php?class_id="+class_id+"&class_group_sum="+num).equals("yes")){
                                                    handler.sendEmptyMessage(9);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }
                            });

                        }
                    });
                    break;
                case 6:
                    Toast.makeText(ClassinfoActivity.this, "请您输入数字", Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    Toast.makeText(ClassinfoActivity.this, "请您输入大于1整数", Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    Toast.makeText(ClassinfoActivity.this, "分组超出人数上限", Toast.LENGTH_SHORT).show();
                    break;
                case 9:
                    Toast.makeText(ClassinfoActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classinfo);
        imageView=findViewById(R.id.iv);
        tv_name=findViewById(R.id.tv_name);
        tv_school=findViewById(R.id.tv_school);
        tv_teacher=findViewById(R.id.tv_teacher);
        tv_introduce=findViewById(R.id.tv_introduce);
        iv_zxing=findViewById(R.id.iv_zxing);
        rl_group=findViewById(R.id.rl_group);
        btn_group=findViewById(R.id.btn_group);
        tv_group_title=findViewById(R.id.tv_group_title);
        tv_group=findViewById(R.id.tv_group);
        btn=findViewById(R.id.btn);
        class_id=getIntent().getStringExtra("class_id");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    class_information class_info=Get_data.get_class(class_id);
                    Message message=new Message();
                    message.what=1;
                    message.obj=class_info;
                    handler.sendMessage(message);
                    group(class_info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(User.user_id!=null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(Get_data.touchHtml("https://www.recycle11.top/cloud_classroom/insert_sc.php?user_id="+User.user_id+"&class_id="+class_id).equals("no")){
                                    handler.sendEmptyMessage(2);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    Intent intent;
                    if(User.identity.equals("学生")){
                        intent=new Intent(ClassinfoActivity.this,PlayerActivity.class);
                    }
                    else{
                        intent=new Intent(ClassinfoActivity.this,CameraActivity.class);
                    }
                    intent.putExtra("class_id",class_id);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ClassinfoActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void generateQrCode(String class_id) {
        Bitmap bitmap = QrCodeGenerator.getQrCodeImage(class_id, iv_zxing.getWidth(), iv_zxing.getHeight());
        if (bitmap == null) {
            Toast.makeText(this, "生成二维码出错", Toast.LENGTH_SHORT).show();
            iv_zxing.setImageBitmap(null);
        } else {
            iv_zxing.setImageBitmap(bitmap);
        }
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    void group(class_information class_info) throws Exception {
        if(User.identity.equals("学生")){
            String group=Get_data.get_group(class_id,User.user_id);
            if(group.startsWith("有新分组任务")){
                Message message1=new Message();
                message1.what=3;
                message1.arg1=Integer.valueOf(group.split("有新分组任务")[1]);
                handler.sendMessage(message1);
            }
            else if(!group.equals("没有选课")){
                Message message1=new Message();
                message1.what=4;
                message1.obj=group;
                handler.sendMessage(message1);
            }
        }
        else{
            Message message1=new Message();
            message1.what=5;
            message1.arg1=class_info.student_num;
            handler.sendMessage(message1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        group(classInformation);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}