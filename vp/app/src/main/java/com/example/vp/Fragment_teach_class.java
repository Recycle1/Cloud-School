package com.example.vp;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.util.Constant;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class Fragment_teach_class extends Fragment {

    private ImageView imageView;
    private RecyclerView recyclerView;
    private ClassListAdapter classListAdapter;
    private TextView textView;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case 1:
                    classListAdapter.setClasslist((ArrayList<class_information>)msg.obj);
                    recyclerView.setAdapter(classListAdapter);
                    textView.setText(String.valueOf(classListAdapter.getItemCount()));
                    break;
            }
            return false;
        }
    });

    public static Fragment_teach_class newInstance(){
        Fragment_teach_class fragment=new Fragment_teach_class();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_teach_class,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView=view.findViewById(R.id.iv);
        recyclerView=view.findViewById(R.id.rv);
        textView=view.findViewById(R.id.tv_num);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Create_ClassActivity.class);
                startActivityForResult(intent,0);
            }
        });
        find();

        classListAdapter=new ClassListAdapter(getActivity(), new ClassListAdapter.OnItemClickListener() {
            @Override
            public void onClick(String class_id, int position) {
                Intent intent=new Intent(getActivity(),ClassinfoActivity.class);
                intent.putExtra("class_id",class_id);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        //扫描结果回调
//        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
//            Bundle bundle = data.getExtras();
//            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
//            //将扫描出的信息显示出来
//            //tvResult.setText(scanResult);
//            Intent intent=new Intent(getActivity(),ClassinfoActivity.class);
//            intent.putExtra("class_id",scanResult);
//            startActivity(intent);
//        }
        if(requestCode==0){
            find();
        }
    }

    void find(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message message=new Message();
                    message.what=1;
                    message.obj=Get_data.get_teach_class_list(User.user_id);
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static String getTime(long time){
        long hours = time / (1000 * 60 * 60);
        long minutes = (time-hours*(1000 * 60 * 60 ))/(1000* 60);
        long second = (time-hours*(1000 * 60 * 60 )-minutes*(1000 * 60 ))/1000;
        String diffTime="";
        if(hours<1){
            if(minutes<10){
                diffTime="0"+minutes;
            }else{
                diffTime=String.valueOf(minutes);
            }
            if(second<10){
                diffTime=diffTime+":0"+second;
            }else{
                diffTime=diffTime+":"+second;
            }
        }
        else{
            if(minutes<10){
                diffTime=hours+":0"+minutes;
            }else{
                diffTime=hours+":"+minutes;
            }
            if(second<10){
                diffTime=diffTime+":0"+second;
            }else{
                diffTime=diffTime+":"+second;
            }
        }
        return diffTime;
    }

}
