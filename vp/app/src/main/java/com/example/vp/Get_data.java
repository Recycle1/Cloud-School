package com.example.vp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Get_data {

        public static ArrayList<class_information> getclasslist(String category, String key) throws Exception{
            String path = "https://www.recycle11.top/cloud_classroom/get_classlist.php?";
            if(category!=null){
                path+="category="+category;
            }
            else{
                path+="key="+key;
            }
            HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if(conn.getResponseCode() == 200){
                InputStream json = conn.getInputStream();
                return parseJSON(json);
            }
            return null;
        }

    public static ArrayList<class_information> get_teach_class_list(String user_id) throws Exception{
        String path = "https://www.recycle11.top/cloud_classroom/get_teach_class.php?teacher_id="+user_id;
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            return parseJSON(json);
        }
        return null;
    }

    public static ArrayList<class_information> getgoodclasslist() throws Exception{
        String path = "https://www.recycle11.top/cloud_classroom/get_good_class.php";
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            return parseJSON(json);
        }
        return null;
    }

    public static class_information get_class(String class_id) throws Exception{
        String path = "https://www.recycle11.top/cloud_classroom/get_class.php?class_id="+class_id;
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            JSONArray jsonArray = new JSONArray(json_str);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String class_name=jsonObject.getString("class_name");
            String school=jsonObject.getString("school");
            String picture=jsonObject.getString("picture");
            String teacher=jsonObject.getString("user_name");
            String category=jsonObject.getString("category");
            int student_num=jsonObject.getInt("student_num");
            String introduce=jsonObject.getString("introduce");
            return new class_information(class_id,class_name,school,picture,category,teacher,student_num,introduce);
        }
        return null;
    }

    public static String get_group(String class_id,String user_id) throws Exception{
        String path = "https://www.recycle11.top/cloud_classroom/get_group.php?class_id="+class_id+"&user_id="+user_id;
        System.out.println(path);
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            return json_str;
        }
        return null;
    }

    public static String get_group_sum(String class_id) throws Exception{
        String path = "https://www.recycle11.top/cloud_classroom/get_group_sum.php?class_id="+class_id;
        System.out.println(path);
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            return json_str;
        }
        return null;
    }

    public static String get_group_other(String class_id,String group_name,String group_num) throws Exception{
        String path = "https://www.recycle11.top/cloud_classroom/get_group_other.php?class_id="+class_id+"&group_name="+group_name+"&group_num="+group_num;
        System.out.println(path);
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            return json_str;
        }
        return null;
    }

    public static String touchHtml(String path) throws Exception {
            System.out.println(path);
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            return "yes";
        }
        return "no";
    }

    public static user_information get_user(String school_id,String school) throws Exception{
        String path = "https://www.recycle11.top/cloud_classroom/get_user.php?school_id="+school_id+"&school="+school;
        System.out.println(path);
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            JSONArray jsonArray = new JSONArray(json_str);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String user_id=jsonObject.getString("user_id");
            String user_name=jsonObject.getString("user_name");
            String identity=jsonObject.getString("identity");
            String gender=jsonObject.getString("gender");
            return new user_information(user_id,user_name,identity,gender,school_id,school);
        }
        return null;
    }

    public static String get_user_used(String school_id,String school) throws Exception{
        String path = "https://www.recycle11.top/cloud_classroom/get_user_used.php?school_id="+school_id+"&school="+school;
        System.out.println(path);
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            return json_str;
        }
        return null;
    }



    public static int get_num(String class_id) throws Exception{
        String path = "https://www.recycle11.top/cloud_classroom/get_num.php?class_id="+class_id;
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            JSONArray jsonArray = new JSONArray(json_str);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            int num=jsonObject.getInt("count(*)");
            return num;
        }
        return 0;
    }

    public static long get_time(String user_id) throws Exception{
        String path = "https://www.recycle11.top/cloud_classroom/get_time.php?user_id="+user_id;
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        long time=0;
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            JSONArray jsonArray = new JSONArray(json_str);
            for(int i = 0; i < jsonArray.length() ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                time+=jsonObject.getLong("time");
            }
            return time;
        }
        return 0;
    }

    public static ArrayList<String> get_ad() throws Exception{
        ArrayList<String> list = new ArrayList<>();
        String path = "https://www.recycle11.top/cloud_classroom/get_ad.php";
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            JSONArray jsonArray = new JSONArray(json_str);
            for(int i = 0; i < jsonArray.length() ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String class_id=jsonObject.getString("class_id");
                list.add(class_id);
            }
            return list;
        }
        return null;
    }

    public static ArrayList<notify_information> get_notify(String user_id) throws Exception{
        ArrayList<notify_information> list = new ArrayList<>();
        String path = "https://www.recycle11.top/cloud_classroom/get_notify.php?user_id="+user_id;
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            JSONArray jsonArray = new JSONArray(json_str);
            for(int i = 0; i < jsonArray.length() ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String user_name=jsonObject.getString("user_name");
                String time=jsonObject.getString("time");
                String title=jsonObject.getString("title");
                String text=jsonObject.getString("text");
                list.add(new notify_information(user_name,time,title,text));
            }
            return list;
        }
        return null;
    }

    public static ArrayList<sign_information> get_sign(String user_id) throws Exception{
        ArrayList<sign_information> list = new ArrayList<>();
        String path = "https://www.recycle11.top/cloud_classroom/get_sign.php?user_id="+user_id;
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            JSONArray jsonArray = new JSONArray(json_str);
            for(int i = 0; i < jsonArray.length() ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String class_id=jsonObject.getString("class_id");
                String class_name=jsonObject.getString("class_name");
                String time=jsonObject.getString("sign_time");
                String duration=jsonObject.getString("duration");
                String people=jsonObject.getString("people");
                list.add(new sign_information(class_id,class_name,time,duration,people));
            }
            return list;
        }
        return null;
    }

    public static String login(String school,String school_id,String password) throws Exception{
        String path = "https://www.recycle11.top/cloud_classroom/login.php?school_id="+school_id+"&password="+password+"&school="+school;
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            System.out.println(json_str);
            return json_str;
        }
        return null;
    }

    public static ArrayList<leave_information> get_leave(String user_id,int identity) throws Exception{
        ArrayList<leave_information> list = new ArrayList<>();
        String path="";
        if(identity==0){
            path = "https://www.recycle11.top/cloud_classroom/get_leave.php?user_id="+user_id;
        }
        else{
            path = "https://www.recycle11.top/cloud_classroom/get_teacher_leave.php?teacher_id="+user_id;
        }
        System.out.println(path);
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            JSONArray jsonArray = new JSONArray(json_str);
            for(int i = 0; i < jsonArray.length() ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String leave_id=jsonObject.getString("leave_id");
                String student_name=jsonObject.getString("user_name");
                String type=jsonObject.getString("type");
                String start_time=jsonObject.getString("start_time");
                String end_time=jsonObject.getString("end_time");
                String need=jsonObject.getString("need");
                String reason=jsonObject.getString("reason");
                String photo=jsonObject.getString("photo");
                String teacher_id=jsonObject.getString("teacher_id");
                int status=jsonObject.getInt("status");
                list.add(new leave_information(leave_id,student_name,type,start_time,end_time,need,reason,photo,teacher_id,status));
            }
            return list;
        }
        return null;
    }

    public static int get_sc_sum(String class_id) throws Exception{
        String path = "https://www.recycle11.top/cloud_classroom/get_sc_sum.php?class_id="+class_id;
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            JSONArray jsonArray = new JSONArray(json_str);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            int num=jsonObject.getInt("count(*)");
            return num;
        }
        return 0;
    }

    public static ArrayList<user_information> get_teacher(String user_id) throws Exception{
        ArrayList<user_information> list = new ArrayList<>();
        String path = "https://www.recycle11.top/cloud_classroom/get_teacher.php?user_id="+user_id;
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            JSONArray jsonArray = new JSONArray(json_str);
            for(int i = 0; i < jsonArray.length() ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String teacher_id=jsonObject.getString("user_id");
                String teacher_name=jsonObject.getString("user_name");
                list.add(new user_information(teacher_id,teacher_name));
            }
            return list;
        }
        return null;
    }

    public static ArrayList<chat_information> get_chat(String class_id,int num) throws Exception{
        ArrayList<chat_information> list = new ArrayList<>();
        String path = "https://www.recycle11.top/cloud_classroom/get_chat.php?class_id="+class_id+"&num="+num;
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            JSONArray jsonArray = new JSONArray(json_str);
            for(int i = 0; i < jsonArray.length() ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name=jsonObject.getString("user_name");
                String text=jsonObject.getString("text");
                list.add(new chat_information(name,text));
            }
            return list;
        }
        return null;
    }

    public static ArrayList<class_information> get_sc(String user_id) throws Exception{
        ArrayList<class_information> list=new ArrayList<>();
        String path = "https://www.recycle11.top/cloud_classroom/get_sc.php?user_id="+user_id;
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            JSONArray jsonArray = new JSONArray(json_str);
            for(int i = 0; i < jsonArray.length() ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String class_id=jsonObject.getString("class_id");
                String class_name=jsonObject.getString("class_name");
                String school=jsonObject.getString("school");
                String picture=jsonObject.getString("picture");
                list.add(new class_information(class_id,class_name,school,picture));
            }
            return list;
        }
        return null;
    }

        private static ArrayList<class_information> parseJSON(InputStream jsonStream) throws Exception{
            ArrayList<class_information> list = new ArrayList<>();
            byte[] data = StreamTool.read(jsonStream);
            String json = new String(data);
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length() ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String class_id=jsonObject.getString("class_id");
                String class_name=jsonObject.getString("class_name");
                String school=jsonObject.getString("school");
                String picture=jsonObject.getString("picture");
                list.add(new class_information(class_id,class_name,school,picture));
            }
            return list;
        }

    public static class StreamTool {
        //从流中读取数据
        public static byte[] read(InputStream inStream) throws Exception{
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = inStream.read(buffer)) != -1)
            {
                outStream.write(buffer,0,len);
            }
            inStream.close();
            return outStream.toByteArray();
        }
    }

}
