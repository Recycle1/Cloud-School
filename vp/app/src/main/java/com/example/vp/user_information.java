package com.example.vp;

public class user_information {
    String user_id;
    String user_name;
    String identity;
    String gender;
    String school_id;
    String school;
    public user_information(String user_id,String user_name){
        this.user_id=user_id;
        this.user_name=user_name;
    }
    public user_information(String user_id,String user_name,String identity,String gender,String school_id,String school){
        this.user_id=user_id;
        this.user_name=user_name;
        this.identity=identity;
        this.gender=gender;
        this.school_id=school_id;
        this.school=school;
    }
}
