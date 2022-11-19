package com.example.vp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class PlayerActivity extends Activity{
    private static final String TAG = "MainActivity";
    private String path;
    //private HashMap<String, String> options;
    private VideoView mVideoView;
    private EditText editText;
    private RecyclerView recyclerView;
    private Button button;
    private boolean flag=true;
    private String class_id;
    private ChatAdapter chatAdapter;
    private long time=0;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    editText.setText("");
                    break;
                case 1:
                    Toast.makeText(PlayerActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    chatAdapter.addChatlist((ArrayList<chat_information>)msg.obj);
                    chatAdapter.notifyDataSetChanged();
                    if(chatAdapter.getItemCount()>0){
                        recyclerView.smoothScrollToPosition(chatAdapter.getItemCount()-1);
                    }
                    break;
                case 3:
                    time+=1000;
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mVideoView = findViewById(R.id.videoView);
        editText=findViewById(R.id.et);
        recyclerView=findViewById(R.id.rv);
        button=findViewById(R.id.btn);
        class_id=getIntent().getStringExtra("class_id");
        path = "rtmp://101.201.109.91:1935/live/"+class_id;//这里写你自己的拉流地址
        //mVideoView.setVideoPath(path);
        mVideoView.setVideoURI(Uri.parse(path));
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();

        chatAdapter=new ChatAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        mVideoView.setOnPreparedListener(new io.vov.vitamio.MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(io.vov.vitamio.MediaPlayer mp) {
                mp.setPlaybackSpeed(1.0f);
            }
        });

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

    }

    @Override
    protected void onDestroy() {
        flag=false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Get_data.touchHtml("https://www.recycle11.top/cloud_classroom/insert_time.php?class_id="+class_id+"&user_id="+User.user_id+"&time="+time);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        super.onDestroy();
    }

}
