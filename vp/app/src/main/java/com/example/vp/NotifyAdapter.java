package com.example.vp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NotifyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    public ArrayList<notify_information> notifylist;

    public NotifyAdapter(Context context){
        this.context=context;
        notifylist= new ArrayList<>();
    }

    public void setNotifylist(ArrayList<notify_information> notifylist) {
        this.notifylist = notifylist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new chatholder(LayoutInflater.from(context).inflate(R.layout.layout_notify,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((chatholder)holder).tv_title.setText(notifylist.get(position).title);
        ((chatholder)holder).tv_time.setText(notifylist.get(position).time);
        ((chatholder)holder).tv_text.setText(notifylist.get(position).text);
        ((chatholder)holder).tv_user_name.setText("发起人："+notifylist.get(position).user_name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Notify_infoActivity.class);
                intent.putExtra("title",notifylist.get(position).title);
                intent.putExtra("time",notifylist.get(position).time);
                intent.putExtra("text",notifylist.get(position).text);
                intent.putExtra("user_name",notifylist.get(position).user_name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifylist.size();
    }

    class chatholder extends RecyclerView.ViewHolder{

        private TextView tv_title;
        private TextView tv_time;
        private TextView tv_text;
        private TextView tv_user_name;
        private RelativeLayout rl_1;

        public chatholder(View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_time=itemView.findViewById(R.id.tv_time);
            tv_text=itemView.findViewById(R.id.tv_text);
            tv_user_name=itemView.findViewById(R.id.tv_user_name);
            rl_1=itemView.findViewById(R.id.rl_1);
        }
    }
}
