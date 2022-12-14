
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
                    Toast.makeText(LeaveActivity.this, "????????????", Toast.LENGTH_SHORT).show();
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
//        ??????????????????
        edittext1 = (EditText)super.findViewById(R.id.edittext1);
//        ?????????????????????
        //edittext2 = (EditText)super.findViewById(R.id.edittext2);
//        ???????????????????????????
        no = (RadioButton)super.findViewById(R.id.no);
//        ????????????????????????
        yes = (RadioButton)super.findViewById(R.id.yes);
//        ???????????????????????????????????????????????????
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
            public void onTimeSelect(Date date, View v) {//??????????????????
                // ?????????????????????v,??????show()???????????????????????? View ???????????????show??????????????????????????????v??????null
                TextView btn = (TextView) v;
                btn.setText(getTimes(date));
            }
        })
                //?????????????????? ????????????????????????????????????????????????
                .setType(new boolean[]{true, true, true, true, true, false})
                .setLabel("???", "???", "???", "???", "???", "???")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(16)//??????
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
                                need="?????????";
                            }
                            else{
                                need="??????";
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
            s = "???????????????";
        }
        if (yes.isChecked()){
            s = "????????????";
        }
    }

    private String getTimes(Date date) {//????????????????????????
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    //    ?????????????????????????????????
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
    //    ?????????????????????
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        String cardNumber = getResources().getStringArray(R.array.type_labels)[arg2];
    }

    /**
     * ??????????????????????????????
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("????????????");
        String[] items = { "??????????????????" };
        builder.setNegativeButton("??????", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // ??????????????????
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_PICK);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
//                    case TAKE_PICTURE: // ??????
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
        if (resultCode == RESULT_OK) { // ??????????????????????????????
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // ?????????????????????????????????
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // ?????????????????????????????????
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // ??????????????????????????????????????????????????????
                    }
                    break;
            }
        }
    }

    // ????????????????????????
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // ????????????
        intent.putExtra("crop", "true");
        // aspectX aspectY ??????????????????
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY ?????????????????????
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }


    //     ?????????????????????????????????
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            //photo = ImageUtils.toRoundBitmap(photo); // ???????????????????????????????????????????????????
            imageView.setBackground(null);
            imageView.setImageBitmap(photo);
            leave_photo=photo;

//            ??????????????????
            //uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // ??????????????????
        // ... ??????????????????Bitmap?????????file???????????????file???url????????????????????????
        // ???????????????????????????????????????????????????
        // bitmap??????????????????????????????????????????????????????
        String imagePath = ImageUtils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath+"");
        if(imagePath != null){
            // ??????imagePath??????
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