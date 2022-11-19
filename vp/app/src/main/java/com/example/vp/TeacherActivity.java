package com.example.vp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TeacherActivity extends AppCompatActivity {

    private RelativeLayout rl1;
    private ImageView img_class;
    private TextView tv_class;
    private RelativeLayout rl2;
    private ImageView img_person;
    private TextView tv_person;

    private Fragment_teach_class fragment_teacher_class;
    private Fragment_person fragment_person;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        rl1=findViewById(R.id.rl_class);
        rl2=findViewById(R.id.rl_person);
        img_class=findViewById(R.id.img_class);
        img_person=findViewById(R.id.img_person);
        tv_class=findViewById(R.id.tv_class);
        tv_person=findViewById(R.id.tv_person);

        fragment_teacher_class=Fragment_teach_class.newInstance();
        getFragmentManager().beginTransaction().add(R.id.fragment,fragment_teacher_class,"main").commitAllowingStateLoss();

        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment_teacher_class==null){
                    fragment_teacher_class=fragment_teacher_class.newInstance();
                }
                if(fragment!=null){
                    getFragmentManager().beginTransaction().hide(fragment).replace(R.id.fragment,fragment_teacher_class).commitAllowingStateLoss();
                }else{
                    getFragmentManager().beginTransaction().replace(R.id.fragment,fragment_teacher_class).commitAllowingStateLoss();
                }
                img_class.setBackground(getDrawable(R.drawable.class2));
                img_person.setBackground(getDrawable(R.drawable.person1));
                tv_class.setTextColor(Color.parseColor("#30B6EC"));
                tv_person.setTextColor(Color.parseColor("#414343"));
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment_person == null) {
                    fragment_person=Fragment_person.newInstance();
                }
                if(fragment!=null){
                    getFragmentManager().beginTransaction().hide(fragment).replace(R.id.fragment,fragment_person).commitAllowingStateLoss();
                }else{
                    getFragmentManager().beginTransaction().replace(R.id.fragment,fragment_person).commitAllowingStateLoss();
                }
                img_class.setBackground(getDrawable(R.drawable.class1));
                img_person.setBackground(getDrawable(R.drawable.person2));
                tv_class.setTextColor(Color.parseColor("#414343"));
                tv_person.setTextColor(Color.parseColor("#30B6EC"));
            }
        });
    }
}