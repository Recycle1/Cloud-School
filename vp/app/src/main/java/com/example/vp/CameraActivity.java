package com.example.vp;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.faucamp.simplertmp.RtmpHandler;
import com.seu.magicfilter.utils.MagicFilterType;

import net.ossrs.yasea.SrsCameraView;
import net.ossrs.yasea.SrsEncodeHandler;
import net.ossrs.yasea.SrsPublisher;
import net.ossrs.yasea.SrsRecordHandler;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

public class CameraActivity extends Activity implements RtmpHandler.RtmpListener,
        SrsRecordHandler.SrsRecordListener, SrsEncodeHandler.SrsEncodeListener,View.OnClickListener{
    private static final String TAG = "CameraActivity";

    public final static int RC_CAMERA = 100;
    private boolean isPermissionGranted = false;
//    private Button mPublishBtn;
    private RelativeLayout mPublish;
    private TextView tv_start;
    private RelativeLayout mCameraSwitchBtn;
    private RelativeLayout mSign;
//    private Button mEncoderBtn;
    private SrsPublisher mPublisher;
    private String rtmpUrl;
    private String class_id;
    private RecyclerView recyclerView;
    private boolean flag=true;
    private ChatAdapter chatAdapter;
    private EditText editText;
    private Button button;
    private LinearLayout ll_et;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    editText.setText("");
                    break;
                case 1:
                    Toast.makeText(CameraActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    chatAdapter.addChatlist((ArrayList<chat_information>)msg.obj);
                    chatAdapter.notifyDataSetChanged();
                    if(chatAdapter.getItemCount()>0){
                        recyclerView.smoothScrollToPosition(chatAdapter.getItemCount()-1);
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);

        class_id=getIntent().getStringExtra("class_id");

        requestPermission();

        //mPublishBtn = findViewById(R.id.publish);
        mPublish = findViewById(R.id.rl_start);
        tv_start=findViewById(R.id.tv_start);
        mCameraSwitchBtn = findViewById(R.id.rl_camera);
        mSign=findViewById(R.id.rl_sign);
        //mEncoderBtn = findViewById(R.id.swEnc);
        recyclerView=findViewById(R.id.rv);
        editText=findViewById(R.id.et);
        button=findViewById(R.id.btn);
        ll_et=findViewById(R.id.ll_et);

        chatAdapter=new ChatAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        mPublish.setOnClickListener(this);
        mCameraSwitchBtn.setOnClickListener(this);
        mSign.setOnClickListener(this);
//        mEncoderBtn.setOnClickListener(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(Get_data.touchHtml("https://www.recycle11.top/cloud_classroom/insert_chat.php?class_id="+class_id+"&user_id="+User.user_id+"&text="+editText.getText().toString()).equals("yes")){
                                handler.sendEmptyMessage(0);
                            }
                            else{
                                handler.sendEmptyMessage(1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        mPublisher = new SrsPublisher((SrsCameraView) findViewById(R.id.glsurfaceview_camera));
        //编码状态回调
        mPublisher.setEncodeHandler(new SrsEncodeHandler(this));
        mPublisher.setRecordHandler(new SrsRecordHandler(this));
        //rtmp推流状态回调
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        //预览分辨率
        mPublisher.setPreviewResolution(1280, 720);
        //推流分辨率
        mPublisher.setOutputResolution(720, 1280);
        //传输率
        mPublisher.setVideoHDMode();
        //开启美颜（其他滤镜效果在MagicFilterType中查看）
        mPublisher.switchCameraFilter(MagicFilterType.BEAUTY);
        //打开摄像头，开始预览（未推流）
        mPublisher.startCamera();
    }

    private void requestPermission() {
        //1. 检查是否已经有该权限
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {
            //2. 权限没有开启，请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_CAMERA);
        }else{
            //权限已经开启，做相应事情
            isPermissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限被用户同意,做相应的事情
                isPermissionGranted = true;
            } else {
                //权限被用户拒绝，做相应的事情
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //开始/停止推流
            case R.id.rl_start:
                if (tv_start.getText().toString().contentEquals("开始")) {
                    ll_et.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (flag){
                                try {
                                    int chat_num=Get_data.get_num(class_id);
                                    int my_num=recyclerView.getAdapter().getItemCount();
                                    if(my_num<chat_num){
                                        ArrayList<chat_information> list=Get_data.get_chat(class_id,chat_num-my_num);
                                        Message message=new Message();
                                        message.what=2;
                                        message.obj=list;
                                        handler.sendMessage(message);
                                    }
                                    handler.sendEmptyMessage(3);
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                    rtmpUrl = "rtmp://101.201.109.91:1935/live/"+class_id;
                    if (TextUtils.isEmpty(rtmpUrl)) {
                        Toast.makeText(getApplicationContext(), "地址不能为空！", Toast.LENGTH_SHORT).show();
                    }
                    mPublisher.startPublish(rtmpUrl);
                    mPublisher.startCamera();

//                    if (mEncoderBtn.getText().toString().contentEquals("软编码")) {
//                        Toast.makeText(getApplicationContext(), "当前使用硬编码", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "当前使用软编码", Toast.LENGTH_SHORT).show();
//                    }
                    tv_start.setText("停止");
//                    mEncoderBtn.setEnabled(false);
                } else if (tv_start.getText().toString().contentEquals("停止")) {
                    ll_et.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    mPublisher.stopPublish();
                    mPublisher.stopRecord();
                    tv_start.setText("开始");
//                    mEncoderBtn.setEnabled(true);
                    flag=false;
                }
                break;
            //切换摄像头
            case R.id.rl_camera:
                mPublisher.switchCameraFace((mPublisher.getCameraId() + 1) % Camera.getNumberOfCameras());
                break;
//            //切换编码方式
//            case R.id.swEnc:
//                if (mEncoderBtn.getText().toString().contentEquals("软编码")) {
//                    mPublisher.switchToSoftEncoder();
//                    mEncoderBtn.setText("硬编码");
//                } else if (mEncoderBtn.getText().toString().contentEquals("硬编码")) {
//                    mPublisher.switchToHardEncoder();
//                    mEncoderBtn.setText("软编码");
//                }
//                break;
            case R.id.rl_sign:
                final String []array2=new String[]{"1分钟","5分钟","15分钟"};
                AlertDialog.Builder builder2=new AlertDialog.Builder(CameraActivity.this);
                builder2.setTitle("发起签到").setItems(array2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //ToastUtil.showWsg(DialogActivity.this,array2[which]);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    long duration=0;
                                    switch (which){
                                        case 0:
                                            duration=60000;
                                            break;
                                        case 1:
                                            duration=300000;
                                            break;
                                        case 2:
                                            duration=900000;
                                            break;
                                    }
                                    Get_data.touchHtml("https://www.recycle11.top/cloud_classroom/insert_sign.php?class_id="+class_id+"&duration="+duration);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        Toast.makeText(CameraActivity.this, "发起成功", Toast.LENGTH_SHORT).show();
                    }
                }).show();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPublisher.resumeRecord();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPublisher.pauseRecord();
    }

    @Override
    protected void onDestroy() {
        flag=false;
        super.onDestroy();
        mPublisher.stopPublish();
        mPublisher.stopRecord();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPublisher.stopEncode();
        mPublisher.stopRecord();
        mPublisher.setScreenOrientation(newConfig.orientation);
        if (tv_start.getText().toString().contentEquals("停止")) {
            mPublisher.startEncode();
        }
        mPublisher.startCamera();
    }

    @Override
    public void onNetworkWeak() {
        Toast.makeText(getApplicationContext(), "网络型号弱", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkResume() {

    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    private void handleException(Exception e) {
        try {
            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            mPublisher.stopPublish();
            mPublisher.stopRecord();
            tv_start.setText("开始");
        } catch (Exception e1) {
            //
        }
    }

    @Override
    public void onRtmpConnecting(String msg) {
        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpConnected(String msg) {
        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpVideoStreaming() {

    }

    @Override
    public void onRtmpAudioStreaming() {

    }

    @Override
    public void onRtmpStopped() {
        Toast.makeText(getApplicationContext(), "已停止", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpDisconnected() {
        Toast.makeText(getApplicationContext(), "未连接服务器", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpVideoFpsChanged(double fps) {

    }

    @Override
    public void onRtmpVideoBitrateChanged(double bitrate) {

    }

    @Override
    public void onRtmpAudioBitrateChanged(double bitrate) {

    }

    @Override
    public void onRtmpSocketException(SocketException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIOException(IOException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {
        handleException(e);
    }

    @Override
    public void onRecordPause() {
        //Toast.makeText(getApplicationContext(), "Record paused", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordResume() {
        //Toast.makeText(getApplicationContext(), "Record resumed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordStarted(String msg) {
        //Toast.makeText(getApplicationContext(), "Recording file: " + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordFinished(String msg) {
        //Toast.makeText(getApplicationContext(), "MP4 file saved: " + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordIOException(IOException e) {
        handleException(e);
    }

    @Override
    public void onRecordIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

}
