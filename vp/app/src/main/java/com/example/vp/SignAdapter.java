package com.example.vp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    public ArrayList<sign_information> signlist;

    public SignAdapter(Context context, OnItemClickListener listener){
        this.context=context;
        this.listener=listener;
        signlist= new ArrayList<>();
    }

    public void setSignlist(ArrayList<sign_information> signlist) {
        this.signlist = signlist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new signholder(LayoutInflater.from(context).inflate(R.layout.layout_sign,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((signholder)holder).tv_class.setText("《"+signlist.get(position).class_name+"》发来签到");
        ((signholder)holder).tv_time.setText("发起时间："+signlist.get(position).time);
        ((signholder)holder).tv_duration.setText("时长："+Long.valueOf(signlist.get(position).duration)/1000/60+"分钟");
        String [] people_list=signlist.get(position).people.split("/p/");
        boolean flag=false;
        for(int i=0;i< people_list.length;i++){
            System.out.println(people_list[i]);
            if(User.user_id.equals(people_list[i])){
                flag=true;
                break;
            }
        }
        if(flag){
            System.out.println("111111111111111111");
            ((signholder)holder).button.setEnabled(false);
            ((signholder)holder).button.setText("已签到");
        }
        else if(Time_out(signlist.get(position).time,Long.valueOf(signlist.get(position).duration))){
            ((signholder)holder).button.setEnabled(false);
            ((signholder)holder).button.setText("已过期");
        }
        ((signholder) holder).button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(signlist.get(position).class_id,signlist.get(position).time,position,v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return signlist.size();
    }

    class signholder extends RecyclerView.ViewHolder{

        private TextView tv_class;
        private TextView tv_time;
        private TextView tv_duration;
        private Button button;

        public signholder(View itemView) {
            super(itemView);
            tv_class=itemView.findViewById(R.id.tv_class);
            tv_time=itemView.findViewById(R.id.tv_time);
            tv_time=itemView.findViewById(R.id.tv_time);
            tv_duration=itemView.findViewById(R.id.tv_duration);
            button=itemView.findViewById(R.id.btn);
        }
    }

    public interface OnItemClickListener{
        void onClick(String class_id,String sign_time,int position,View v);
    }

    public String getTime(String day, long x)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
        Date date = null;
        try
        {
            date = format.parse(day);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        if (date == null) return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND,Integer.parseInt(String.valueOf(x/1000)));//24小时制
        date = cal.getTime();
        System.out.println("front:" + date);
        cal = null;
        return format.format(date);
    }

    public boolean Time_out(String day, long x){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
        Date date = null;
        try
        {
            date = format.parse(day);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        if (date == null) return true;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND,Integer.parseInt(String.valueOf(x/1000)));//24小时制
        date = cal.getTime();

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));

        if(date1.compareTo(date)>0){
            return true;
        }
        else{
            return false;
        }
    }

}
