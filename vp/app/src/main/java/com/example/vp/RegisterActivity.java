package com.example.vp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText password;
    private EditText user_name;
    private EditText grade;
    private EditText school_id;
    private RadioButton man;
    private RadioButton woman;
    private Spinner identity;
    private Spinner school;
    private Spinner college;

    private Button back;
    private Button register;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case 1:
                    Toast.makeText(RegisterActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(RegisterActivity.this, "账号已注册", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 4:
                    Toast.makeText(RegisterActivity.this, "年级为入学年份", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(RegisterActivity.this, "学号为数字", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        password=findViewById(R.id.et_password);
        user_name=findViewById(R.id.et_user_name);
        grade=findViewById(R.id.et_grade);
        school_id=findViewById(R.id.et_school_id);
        man=findViewById(R.id.man);
        woman=findViewById(R.id.woman);
        identity=findViewById(R.id.identity);
        school=findViewById(R.id.school);
        college=findViewById(R.id.college);

        back=findViewById(R.id.back);
        register=findViewById(R.id.register);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password_str=password.getText().toString();
                String user_name_str=user_name.getText().toString();
                String grade_str=grade.getText().toString();
                String school_id_str=school_id.getText().toString();
                String gender_str="";
                if(man.isChecked()){
                    gender_str="男";
                }
                else{
                    gender_str="女";
                }
                String identity_str=identity.getSelectedItem().toString();
                String school_str=school.getSelectedItem().toString();
                String college_str=college.getSelectedItem().toString();
                if(password_str.length()==0||user_name_str.length()==0||grade_str.length()==0||school_id_str.length()==0){
                    handler.sendEmptyMessage(1);
                }
                else if(!isInteger(grade_str)){
                    handler.sendEmptyMessage(4);
                }
                else if(!isInteger(school_id_str)){
                    handler.sendEmptyMessage(5);
                }
                else{
                    String finalGender_str = gender_str;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(Get_data.get_user_used(school_id_str,school_str).equals("有")){
                                    handler.sendEmptyMessage(2);
                                }
                                else{
                                    String user_id="user_"+User.getUniqueKey();
                                    if(Get_data.touchHtml("https://www.recycle11.top/cloud_classroom/register.php?user_id="+user_id+"&user_name="+user_name_str+"&password="+password_str+"&gender="+ finalGender_str
                                            +"&identity="+identity_str+"&school="+school_str+"&school_id="+school_id_str+"&grade="+grade_str+"&college="+college_str).equals("yes")){
                                        handler.sendEmptyMessage(3);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

            }
        });

    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
