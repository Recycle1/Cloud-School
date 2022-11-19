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

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    public ArrayList<chat_information> chatlist;

    public ChatAdapter(Context context){
        this.context=context;
        chatlist= new ArrayList<>();
    }

    public void setChatlist(ArrayList<chat_information> chatlist) {
        this.chatlist = chatlist;
    }
    public void addChatlist(ArrayList<chat_information> chatlist){
        for(int i=0;i<chatlist.size();i++){
            this.chatlist.add(chatlist.get(i));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new chatholder(LayoutInflater.from(context).inflate(R.layout.layout_chatlist,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((chatholder)holder).tv_name.setText(chatlist.get(position).name+"：");
        ((chatholder)holder).tv_text.setText(chatlist.get(position).text);
    }

    @Override
    public int getItemCount() {
        return chatlist.size();
    }

    class chatholder extends RecyclerView.ViewHolder{

        private TextView tv_name;
        private TextView tv_text;

        public chatholder(View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_text=itemView.findViewById(R.id.tv_text);
        }
    }

}
