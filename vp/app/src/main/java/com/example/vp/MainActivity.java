package com.example.vp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.github.faucamp.simplertmp.RtmpHandler;
import com.google.zxing.util.Constant;
import com.seu.magicfilter.utils.MagicFilterType;

import net.ossrs.yasea.SrsCameraView;
import net.ossrs.yasea.SrsEncodeHandler;
import net.ossrs.yasea.SrsPublisher;
import net.ossrs.yasea.SrsRecordHandler;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity{

    private RelativeLayout rl1;
    private ImageView img_main;
    private TextView tv_main;
    private RelativeLayout rl2;
    private ImageView img_class;
    private TextView tv_class;
    private RelativeLayout rl3;
    private ImageView img_person;
    private TextView tv_person;

    private Fragment_main fragment_main;
    private Fragment_class fragment_class;
    private Fragment_person fragment_person;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl1=findViewById(R.id.rl_main);
        rl2=findViewById(R.id.rl_class);
        rl3=findViewById(R.id.rl_person);
        img_main=findViewById(R.id.img_main);
        img_class=findViewById(R.id.img_class);
        img_person=findViewById(R.id.img_person);
        tv_main=findViewById(R.id.tv_main);
        tv_class=findViewById(R.id.tv_class);
        tv_person=findViewById(R.id.tv_person);

        fragment_main=Fragment_main.newInstance();
        getFragmentManager().beginTransaction().add(R.id.fragment,fragment_main,"main").commitAllowingStateLoss();


        rl1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                fragment=getFragmentManager().findFragmentByTag("main");
                if(fragment!=null){
                    getFragmentManager().beginTransaction().hide(fragment).replace(R.id.fragment,fragment_main).commitAllowingStateLoss();
                }else{
                    getFragmentManager().beginTransaction().replace(R.id.fragment,fragment_main).commitAllowingStateLoss();
                }
                img_main.setBackground(getDrawable(R.drawable.main2));
                img_class.setBackground(getDrawable(R.drawable.class1));
                img_person.setBackground(getDrawable(R.drawable.person1));
                tv_main.setTextColor(Color.parseColor("#30B6EC"));
                tv_class.setTextColor(Color.parseColor("#414343"));
                tv_person.setTextColor(Color.parseColor("#414343"));
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment_class==null){
                    fragment_class=Fragment_class.newInstance();
                }
                if(fragment!=null){
                    getFragmentManager().beginTransaction().hide(fragment).replace(R.id.fragment,fragment_class).commitAllowingStateLoss();
                }else{
                    getFragmentManager().beginTransaction().replace(R.id.fragment,fragment_class).commitAllowingStateLoss();
                }
                img_main.setBackground(getDrawable(R.drawable.main1));
                img_class.setBackground(getDrawable(R.drawable.class2));
                img_person.setBackground(getDrawable(R.drawable.person1));
                tv_main.setTextColor(Color.parseColor("#414343"));
                tv_class.setTextColor(Color.parseColor("#30B6EC"));
                tv_person.setTextColor(Color.parseColor("#414343"));
            }
        });
        rl3.setOnClickListener(new View.OnClickListener() {
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
                img_main.setBackground(getDrawable(R.drawable.main1));
                img_class.setBackground(getDrawable(R.drawable.class1));
                img_person.setBackground(getDrawable(R.drawable.person2));
                tv_main.setTextColor(Color.parseColor("#414343"));
                tv_class.setTextColor(Color.parseColor("#414343"));
                tv_person.setTextColor(Color.parseColor("#30B6EC"));
            }
        });

    }

}
