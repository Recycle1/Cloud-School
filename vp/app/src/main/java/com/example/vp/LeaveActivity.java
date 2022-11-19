
package com.example.vp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LeaveActivity extends AppCompatActivity{

    private Button button2;
    private Button back;
    private Spinner spinner,spinner1;
    private TextView tv_start,tv_end;
    EditText edittext1,edittext2;
    LinearLayout llDate,llTime;
    RadioButton no,yes;
    private ImageView imageView;
    String s;
    private TimePickerView timePickerView;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private Bitmap leave_photo;
    private String leave_photo_name;
    private BaseAdapter baseAdapter;
    ArrayList<user_information> teacher_list;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    teacher_list=((ArrayList<user_information>)msg.obj);
                    baseAdapter=new BaseAdapter() {
                        @Override
                        public int getCount() {
                            return teacher_list.size();
                        }

                        @Override
                        public Object getItem(int position) {
                            return null;
                        }

                        @Override
                        public long getItemId(int position) {
                            return 0;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            TextView textView = new TextView(LeaveActivity.this);
                            textView.setHeight(125);
                            textView.setGravity(0);
                            textView.setPadding(25,25,12,15);
                            textView.setTextSize(18);
                            textView.setTextColor(Color.parseColor("#000000"));
                            textView.setText(teacher_list.get(position).user_name);
                            return textView;
                        }
                    };
                    spinner1.setAdapter(baseAdapter);
                    break;
                case 2:
                    Toast.makeText(LeaveActivity.this, "请假成功", Toast.LENGTH_SHORT).show();
                    finish();
            }
            return false;
        }
    });

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        initView();
        button2 = (Button)super.findViewById(R.id.button2);
        //button2.setOnClickListener(new myonclicklistener());
//        获取请假原因
        edittext1 = (EditText)super.findViewById(R.id.edittext1);
//        获取审批人姓名
        //edittext2 = (EditText)super.findViewById(R.id.edittext2);
//        单选按钮不需要离校
        no = (RadioButton)super.findViewById(R.id.no);
//        单选按钮需要离校
        yes = (RadioButton)super.findViewById(R.id.yes);
//        定义一个字符串变量存储是否需要离校
        imageView=findViewById(R.id.iv);
        spinner=findViewById(R.id.info_type);
        spinner1=findViewById(R.id.spnner1);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Message message=new Message();
                    message.what=1;
                    message.obj=Get_data.get_teacher(User.user_id);
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2022, 0, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2055, 11, 28);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });

        timePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                TextView btn = (TextView) v;
                btn.setText(getTimes(date));
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, true, true, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(16)//字号
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .build();

        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timePickerView!=null){
                    timePickerView.show(tv_start);
                }
            }
        });
        tv_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timePickerView!=null){
                    timePickerView.show(tv_end);
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(leave_photo!=null){
                    uploadPic(leave_photo);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String leave_id="leave_"+User.getUniqueKey();
                            String need;
                            String leave_img="https://www.recycle11.top/cloud_classroom/leave_img/"+leave_photo_name+".png";
                            if(no.isChecked()){
                                need="不需要";
                            }
                            else{
                                need="需要";
                            }
                            if(Get_data.touchHtml("https://www.recycle11.top/cloud_classroom/insert_leave.php?leave_id="+leave_id+"&user_id="+User.user_id+"&type="+spinner.getSelectedItem().toString()
                                    +"&start_time="+tv_start.getText()+"&end_time="+tv_end.getText()+"&need="+need+"&reason="+
                                    edittext1.getText().toString()+"&photo="+leave_img+"&teacher_id="+teacher_list.get(spinner1.getSelectedItemPosition()).user_id+"&status=0").equals("yes")){
                                handler.sendEmptyMessage(2);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        s = null;
        if (no.isChecked()){
            s = "不需要离校";
        }
        if (yes.isChecked()){
            s = "需要离校";
        }
    }

    private String getTimes(Date date) {//年月日时分秒格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    //    设置开始日期和结束日期
    private void initView() {
        llDate = findViewById(R.id.row2);
        tv_start = findViewById(R.id.tv_start);
        llTime = findViewById(R.id.row3);
        tv_end = findViewById(R.id.tv_end);
    }
    private int year, month, day, hour, minute;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
    }
    //    获取下拉框的值
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        String cardNumber = getResources().getStringArray(R.array.type_labels)[arg2];
    }

    /**
     * 显示修改头像的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择封面");
        String[] items = { "选择本地照片" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_PICK);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
//                    case TAKE_PICTURE: // 拍照
//                        takePicture();
//                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    // 裁剪图片方法实现
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }


    //     保存裁剪之后的图片数据
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            //photo = ImageUtils.toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            imageView.setBackground(null);
            imageView.setImageBitmap(photo);
            leave_photo=photo;

//            上传到服务器
            //uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        String imagePath = ImageUtils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath+"");
        if(imagePath != null){
            // 使用imagePath上传
            new Thread(new Runnable() {
                @Override
                public void run() {
                    leave_photo_name="leave_"+User.getUniqueKey();
                    Upload upload=new Upload(imagePath,"https://www.recycle11.top/cloud_classroom/upload_leave.php",leave_photo_name);
                }
            }).start();


        }
    }

}