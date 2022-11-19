package com.example.vp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LeaveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ClassListAdapter.OnItemClickListener listener;
    public ArrayList<leave_information> leavelist;

    public LeaveAdapter(Context context, ClassListAdapter.OnItemClickListener listener){
        this.context=context;
        this.listener=listener;
        leavelist= new ArrayList<>();
    }

    public void setLeavelist(ArrayList<leave_information> leavelist) {
        this.leavelist = leavelist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new chatholder(LayoutInflater.from(context).inflate(R.layout.layout_leave,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((chatholder)holder).tv_name.setText(leavelist.get(position).student_name);
        ((chatholder)holder).tv_start_time.setText("开始时间："+leavelist.get(position).start_time);
        ((chatholder)holder).tv_end_time.setText("结束时间："+leavelist.get(position).end_time);

        if(Time_out(leavelist.get(position).end_time)){
            leavelist.get(position).status=3;
        }

        ((chatholder)holder).yes.setVisibility(View.VISIBLE);
        if(leavelist.get(position).status==3){
            ((chatholder)holder).yes.setBackground(null);
            ((chatholder)holder).yes.setEnabled(false);
            ((chatholder)holder).yes.setText("已过期");
        }
        else if(User.identity.equals("学生")){
            if(leavelist.get(position).status==0){
                ((chatholder)holder).yes.setBackground(null);
                ((chatholder)holder).yes.setEnabled(false);
                ((chatholder)holder).yes.setText("等待审批");
            }
            else if(leavelist.get(position).status==1){
                ((chatholder)holder).yes.setBackground(null);
                ((chatholder)holder).yes.setEnabled(false);
                ((chatholder)holder).yes.setText("已通过");
            }
            else{
                ((chatholder)holder).yes.setBackground(null);
                ((chatholder)holder).yes.setEnabled(false);
                ((chatholder)holder).yes.setText("未通过");
            }

        }
        else if(User.identity.equals("教师（辅导员）")&&(leavelist.get(position).status!=3)){
            if(leavelist.get(position).status==1){
                ((chatholder)holder).yes.setEnabled(false);
                ((chatholder)holder).yes.setText("已通过");
                ((chatholder)holder).no.setVisibility(View.GONE);
            }
            else if(leavelist.get(position).status==2){
                ((chatholder)holder).yes.setEnabled(false);
                ((chatholder)holder).yes.setText("未通过");
                ((chatholder)holder).no.setVisibility(View.GONE);
            }
            else{
                ((chatholder)holder).no.setVisibility(View.VISIBLE);
                ((chatholder)holder).yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Get_data.touchHtml("https://www.recycle11.top/cloud_classroom/update_leave.php?leave_id="+leavelist.get(position).leave_id+"&type=1");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        ((chatholder)holder).yes.setEnabled(false);
                        ((chatholder)holder).yes.setText("已通过");
                        ((chatholder)holder).no.setVisibility(View.GONE);

                    }
                });
                ((chatholder)holder).no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Get_data.touchHtml("https://www.recycle11.top/cloud_classroom/update_leave.php?leave_id="+leavelist.get(position).leave_id+"&type=2");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        ((chatholder)holder).yes.setEnabled(false);
                        ((chatholder)holder).yes.setText("未通过");
                        ((chatholder)holder).no.setVisibility(View.GONE);
                    }
                });
            }

        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,LeaveinfoActivity.class);
                intent.putExtra("student_name",leavelist.get(position).student_name);
                intent.putExtra("start_time",leavelist.get(position).start_time);
                intent.putExtra("end_time",leavelist.get(position).end_time);
                intent.putExtra("need",leavelist.get(position).need);
                intent.putExtra("reason",leavelist.get(position).reason);
                intent.putExtra("leave_photo",leavelist.get(position).photo);
                intent.putExtra("status",leavelist.get(position).status);
                context.startActivity(intent);
            }
        });

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onClick(leavelist.get(position).leave_id,position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return leavelist.size();
    }

    class chatholder extends RecyclerView.ViewHolder{

        private TextView tv_name;
        private TextView tv_start_time;
        private TextView tv_end_time;
        private Button yes;
        private Button no;

        public chatholder(View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_start_time=itemView.findViewById(R.id.tv_start_time);
            tv_end_time=itemView.findViewById(R.id.tv_end_time);
            yes=itemView.findViewById(R.id.btn_yes);
            no=itemView.findViewById(R.id.btn_no);
        }
    }

    public interface OnItemClickListener{
        void onClick(String class_id,int position);
    }

    public boolean Time_out(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
        Date date1 = null;
        try
        {
            date1 = format.parse(date);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2 = new Date(System.currentTimeMillis());
        if(date2.compareTo(date1)>0){
            return true;
        }
        else{
            return false;
        }
    }
}
