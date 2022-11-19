package com.example.vp;

public class class_information {
    String class_id;
    String class_name;
    String school;
    String picture;
    String category;
    String user_name;
    String introduce;
    int student_num;


    public class_information(String class_id, String class_name, String school, String picture){
        this.class_id=class_id;
        this.class_name=class_name;
        this.school=school;
        this.picture=picture;
    }

    public class_information(String class_id, String class_name, String school, String picture, String category, String user_name, int student_num,String introduce){
        this.class_id=class_id;
        this.class_name=class_name;
        this.school=school;
        this.picture=picture;
        this.category=category;
        this.user_name=user_name;
        this.student_num=student_num;
        this.introduce=introduce;
    }

}
