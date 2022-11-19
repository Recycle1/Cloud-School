package com.example.vp;

public class leave_information {

    String leave_id;
    String user_id;
    String student_name;
    String type;
    String start_time;
    String end_time;
    String need;
    String reason;
    String photo;
    String teacher_id;
    int status;
    public leave_information(String leave_id,String student_name,String type,String start_time,String end_time,String need,String reason,String photo,String teacher_id,int status){
        this.leave_id=leave_id;
        this.student_name=student_name;
        this.type=type;
        this.start_time=start_time;
        this.end_time=end_time;
        this.need=need;
        this.reason=reason;
        this.photo=photo;
        this.teacher_id=teacher_id;
        this.status=status;
    }

}
