package com.example.vp;

import java.util.Random;

public class User {
    static String user_id;
    static String user_name;
    static String identity;
    static String gender;
    static String school_id;
    static String school;
    public static synchronized String  getUniqueKey(){

        Random random = new Random();
        Integer number = random.nextInt(900)+100;

        return String.valueOf(System.currentTimeMillis()).substring(7,13)+ String.valueOf(number);
    }
}
