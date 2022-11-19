package com.example.vp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ClassListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    public ArrayList<class_information> classlist;
    int what;

    public ClassListAdapter(Context context, OnItemClickListener listener){
        this.context=context;
        this.listener=listener;
        classlist= new ArrayList<>();
    }

    public ClassListAdapter(int what,Context context, OnItemClickListener listener){
        this.what=what;
        this.context=context;
        this.listener=listener;
        classlist= new ArrayList<>();
    }

    public void setClasslist(ArrayList<class_information> classlist) {
        this.classlist = classlist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(what==2){
            return new chatholder(LayoutInflater.from(context).inflate(R.layout.layout_classlist2,parent,false));
        }
        else{
            return new chatholder(LayoutInflater.from(context).inflate(R.layout.layout_classlist,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((chatholder)holder).tv_class.setText(classlist.get(position).class_name);
        Glide.with(context).load(classlist.get(position).picture).into(((chatholder)holder).iv);
        ((chatholder)holder).tv_school.setText(classlist.get(position).school);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(classlist.get(position).class_id,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classlist.size();
    }

    class chatholder extends RecyclerView.ViewHolder{

        private ImageView iv;
        private TextView tv_class;
        private TextView tv_school;

        public chatholder(View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.iv);
            tv_class=itemView.findViewById(R.id.tv_class);
            tv_school=itemView.findViewById(R.id.tv_school);
        }
    }

    public interface OnItemClickListener{
        void onClick(String class_id,int position);
    }
}
