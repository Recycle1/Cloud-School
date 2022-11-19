package com.example.vp;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int group_sum;
    private int group_num;
    private String class_id;
    private int student[];
    private OnItemClickListener listener;
    private int color[];
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    chatholder holder=((chatholder)msg.obj);
                    int other=msg.arg1;
                    int position=msg.arg2;
                    if(color[position]==0){
                        ((chatholder)holder).rl_1.setBackground(context.getDrawable(R.drawable.group_choice));
                    }
                    else{
                        ((chatholder)holder).rl_1.setBackgroundColor(Color.parseColor("#30000000"));
                    }
                    ((chatholder)holder).tv_group_name.setText("第"+(position+1)+"组");
                    ((chatholder)holder).tv_group_sum.setText(other+"/"+student[position]);
                    if(Integer.valueOf(other)<student[position]){
                        ((chatholder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for(int i=0;i<color.length;i++){
                                    color[i]=0;
                                }
                                color[position]=1;
                                notifyDataSetChanged();
                                listener.onClick(position,v);
                            }
                        });
                    }
                    else{
                        ((chatholder)holder).itemView.setEnabled(false);
                        ((chatholder)holder).tv_status.setText("成员已满");
                    }
                    break;
            }
            return false;
        }
    });

    public GroupAdapter(Context context,int group_sum,int group_num,int student_num,String class_id,OnItemClickListener listener){
        this.context=context;
        this.group_sum=group_sum;
        this.group_num=group_num;
        this.class_id=class_id;
        this.listener=listener;
        student=new int[group_sum];
        color=new int[group_sum];

        for(int i=0;i<color.length;i++){
            color[i]=0;
        }

        int group_every_num=student_num/group_sum;
        for(int i=0;i<student.length;i++){
            student[i]=group_every_num;
        }
        if(student_num%group_sum!=0){
            int group_more=student_num%group_sum;
            for(int i=0;i<group_more;i++){
                student[i]++;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new chatholder(LayoutInflater.from(context).inflate(R.layout.layout_group,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String other= null;
                try {
                    other = Get_data.get_group_other(class_id,String.valueOf(position+1),String.valueOf(group_num));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message=new Message();
                message.what=1;
                message.arg1=Integer.valueOf(other);
                message.arg2=position;
                message.obj=holder;
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return group_sum;
    }

    class chatholder extends RecyclerView.ViewHolder{

        private TextView tv_group_name;
        private TextView tv_group_sum;
        private TextView tv_status;
        private RelativeLayout rl_1;

        public chatholder(View itemView) {
            super(itemView);
            tv_group_name=itemView.findViewById(R.id.tv_group_name);
            tv_group_sum=itemView.findViewById(R.id.tv_group_sum);
            tv_status=itemView.findViewById(R.id.tv_status);
            rl_1=itemView.findViewById(R.id.rl_1);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position,View v);
    }

}
