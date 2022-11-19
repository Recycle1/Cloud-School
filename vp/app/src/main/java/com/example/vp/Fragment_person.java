package com.example.vp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Fragment_person extends Fragment {

    private ImageView imageView;
    private TextView tv_name;
    private TextView tv_school;
    private RelativeLayout rl_1;
    private RelativeLayout rl_2;
    private RelativeLayout rl_3;
    private RelativeLayout rl_5;

    public static Fragment_person newInstance(){
        Fragment_person fragment=new Fragment_person();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_person,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView=view.findViewById(R.id.iv);
        tv_name=view.findViewById(R.id.tv_name);
        tv_school=view.findViewById(R.id.tv_school);
        rl_1=view.findViewById(R.id.rl_1);
        rl_2=view.findViewById(R.id.rl_2);
        rl_3=view.findViewById(R.id.rl_3);
        rl_5=view.findViewById(R.id.rl_5);
        if(User.user_id==null){
            tv_name.setText("登录/注册");
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),LoginActivity.class);
                    startActivityForResult(intent,0);
                }
            });
            rl_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                }
            });
            rl_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                }
            });
            rl_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            if(User.identity.equals("教师")){
                rl_1.setVisibility(View.GONE);
                rl_2.setVisibility(View.GONE);
                rl_3.setVisibility(View.GONE);
            }
            else if(User.identity.equals("教师（辅导员）")){
                rl_2.setVisibility(View.GONE);
            }
            tv_name.setText(User.user_name);
            tv_school.setText(User.school);
            if(User.gender.equals("男")){
                imageView.setBackground(getActivity().getDrawable(R.drawable.man));
            }
            else{
                imageView.setBackground(getActivity().getDrawable(R.drawable.woman));
            }
            rl_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), NotifylistActivity.class);
                    startActivity(intent);
                }
            });
            rl_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),Sign_inActivity.class);
                    startActivity(intent);
                }
            });
            rl_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),LeavelistActivity.class);
                    startActivity(intent);
                }
            });
            rl_5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User.user_id=null;
                    User.user_name=null;
                    User.gender=null;
                    User.school_id=null;
                    User.school=null;
                    User.identity=null;
                    Intent intent=new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent=null;
        if(resultCode==0){
            intent=new Intent(getActivity(),MainActivity.class);
        }
        else{
            intent=new Intent(getActivity(),TeacherActivity.class);
        }
        startActivity(intent);
        getActivity().finish();
    }
}
