package com.example.vp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

public class Fragment_main extends Fragment {

    private Banner banner;
    private RecyclerView recyclerView;
    private ClassListAdapter classListAdapter;
    private RelativeLayout rl_search;
    private RelativeLayout rl_1;
    private RelativeLayout rl_2;
    private RelativeLayout rl_3;
    private RelativeLayout rl_4;
    private RelativeLayout rl_5;
    private RelativeLayout rl_6;
    private RelativeLayout rl_7;
    private RelativeLayout rl_8;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    ArrayList<String> ad=(ArrayList<String>)msg.obj;
                    banner.setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            Intent intent=new Intent(getActivity(),ClassinfoActivity.class);
                            intent.putExtra("class_id",ad.get(position));
                            startActivity(intent);
                        }
                    });
                    break;
                case 1:
                    ArrayList<class_information> classlist=(ArrayList<class_information>)msg.obj;
                    classListAdapter.setClasslist(classlist);
                    recyclerView.setAdapter(classListAdapter);
                    break;
            }
            return false;
        }
    });

    public static Fragment_main newInstance(){
        Fragment_main fragment=new Fragment_main();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_main,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        banner=view.findViewById(R.id.banner);
        recyclerView=view.findViewById(R.id.rv);
        rl_search=view.findViewById(R.id.rl_search);
        rl_1=view.findViewById(R.id.rl_1);
        rl_2=view.findViewById(R.id.rl_2);
        rl_3=view.findViewById(R.id.rl_3);
        rl_4=view.findViewById(R.id.rl_4);
        rl_5=view.findViewById(R.id.rl_5);
        rl_6=view.findViewById(R.id.rl_6);
        rl_7=view.findViewById(R.id.rl_7);
        rl_8=view.findViewById(R.id.rl_8);
        Onclick onclick=new Onclick();
        rl_1.setOnClickListener(onclick);
        rl_2.setOnClickListener(onclick);
        rl_3.setOnClickListener(onclick);
        rl_4.setOnClickListener(onclick);
        rl_5.setOnClickListener(onclick);
        rl_6.setOnClickListener(onclick);
        rl_7.setOnClickListener(onclick);
        rl_8.setOnClickListener(onclick);
        rl_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SearchActivity.class);
                startActivity(intent);
            }
        });

        classListAdapter=new ClassListAdapter(2,getActivity(), new ClassListAdapter.OnItemClickListener() {
            @Override
            public void onClick(String class_id, int position) {
                Intent intent=new Intent(getActivity(),ClassinfoActivity.class);
                intent.putExtra("class_id",class_id);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<String> ad=Get_data.get_ad();
                    Message message1=new Message();
                    message1.what=0;
                    message1.obj=ad;
                    handler.sendMessage(message1);
                    ArrayList<class_information> classlist=Get_data.getgoodclasslist();
                    Message message2=new Message();
                    message2.what=1;
                    message2.obj=classlist;
                    handler.sendMessage(message2);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

        initBanner();
    }

    void initBanner(){
        ArrayList list=new ArrayList();
        list.add("https://www.recycle11.top/cloud_classroom/ad/1.png");
        list.add("https://www.recycle11.top/cloud_classroom/ad/2.png");
        list.add("https://www.recycle11.top/cloud_classroom/ad/3.png");
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(list);
        banner.start();
    }

    class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //RequestOptions options=RequestOptions.bitmapTransform(new RoundedCorners(10));
            //imageView.setPaddingRelative(37,0,37,0);
            Glide.with(context).load(path).into(imageView);
            imageView.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(50, 0, view.getWidth()-50, view.getHeight(), 55);
                }
            });
            imageView.setClipToOutline(true);
            imageView.setPadding(50,0,50,0);

        }

    }

    class Onclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getActivity(),ClasslistActivity.class);
            switch(v.getId()){
                case R.id.rl_1:
                    intent.putExtra("category","外语");
                    intent.putExtra("index",0);
                    break;
                case R.id.rl_2:
                    intent.putExtra("category","经济管理");
                    intent.putExtra("index",1);
                    break;
                case R.id.rl_3:
                    intent.putExtra("category","计算机");
                    intent.putExtra("index",2);
                    break;
                case R.id.rl_4:
                    intent.putExtra("category","大学数学");
                    intent.putExtra("index",3);
                    break;
                case R.id.rl_5:
                    intent.putExtra("category","医疗健康");
                    intent.putExtra("index",4);
                    break;
                case R.id.rl_6:
                    intent.putExtra("category","化学工程");
                    intent.putExtra("index",5);
                    break;
                case R.id.rl_7:
                    intent.putExtra("category","音乐与艺术");
                    intent.putExtra("index",6);
                    break;
                case R.id.rl_8:
                    intent.putExtra("category","大学工科");
                    intent.putExtra("index",7);
                    break;
            }
            startActivity(intent);
        }
    }
}
