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

public class Fragment_class extends Fragment {

    private ImageView imageView;
    private RecyclerView recyclerView;
    private ClassListAdapter classListAdapter;
    private TextView tv_num;
    private TextView tv_time;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case 1:
                    classListAdapter.setClasslist((ArrayList<class_information>)msg.obj);
                    recyclerView.setAdapter(classListAdapter);
                    tv_num.setText(String.valueOf(classListAdapter.getItemCount()));
                    break;
                case 2:
                    tv_time.setText(getTime((long)msg.obj));
                    break;
            }
            return false;
        }
    });

    public static Fragment_class newInstance(){
        Fragment_class fragment=new Fragment_class();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_class,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView=view.findViewById(R.id.iv);
        recyclerView=view.findViewById(R.id.rv);
        tv_num=view.findViewById(R.id.tv_num);
        tv_time=view.findViewById(R.id.tv_time);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrCode();
            }
        });

        find();

        classListAdapter=new ClassListAdapter(getActivity(), new ClassListAdapter.OnItemClickListener() {
            @Override
            public void onClick(String class_id, int position) {
                Intent intent=new Intent(getActivity(),ClassinfoActivity.class);
                intent.putExtra("class_id",class_id);
                startActivityForResult(intent,12);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    void find(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message message=new Message();
                    message.what=1;
                    message.obj=Get_data.get_sc(User.user_id);
                    handler.sendMessage(message);
                    Message message1=new Message();
                    message1.what=2;
                    message1.obj=Get_data.get_time(User.user_id);
                    handler.sendMessage(message1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 开始扫码
    private void startQrCode() {
        // 申请相机权限
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission
                    .CAMERA)) {
                Toast.makeText(getActivity(), "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 申请文件读写权限（部分朋友遇到相册选图需要读写权限的情况，这里一并写一下）
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(getActivity(), "请至权限中心打开本应用的文件读写权限", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.REQ_PERM_EXTERNAL_STORAGE);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            //tvResult.setText(scanResult);
            Intent intent=new Intent(getActivity(),ClassinfoActivity.class);
            intent.putExtra("class_id",scanResult);
            startActivityForResult(intent,12);
        }
        else if(requestCode==12){
            find();
        }
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
