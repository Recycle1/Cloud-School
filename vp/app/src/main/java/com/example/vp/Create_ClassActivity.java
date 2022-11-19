package com.example.vp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Create_ClassActivity extends AppCompatActivity {

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private ImageView iv_personal_icon;
    private Bitmap class_photo=null;
    String photo_name;

    private ImageView imageView;
    Button button;

    Button button1;
    Button button2;
    EditText edit1;
    EditText edit2;
    //EditText edit3;
    Spinner spinner;
    String courseName,coursePhoto,courseTime,courseType,courseInfo;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(Create_ClassActivity.this,"添加成功！",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 2:
                    String class_photo_name="https://www.recycle11.top/cloud_classroom/class_img/"+photo_name+".png";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String class_id="class_"+User.getUniqueKey();
                            try {
                                if(Get_data.touchHtml("https://www.recycle11.top/cloud_classroom/insert_class.php?class_id="+class_id+"&class_name="+courseName+"&teacher_id="+User.user_id
                                        +"&category="+courseType+"&introduce="+courseInfo+"&picture="+class_photo_name).equals("yes")){
                                    handler.sendEmptyMessage(1);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        button = findViewById(R.id.addPhoto);
        imageView = findViewById(R.id.image);

        button1 = findViewById(R.id.top_cancel);
        button2 = findViewById(R.id.top_yes);
        edit1 =findViewById(R.id.courseName);
        edit2 =findViewById(R.id.courseInfo);
        //edit3 =findViewById(R.id.courseTime);
        spinner =findViewById(R.id.courseType);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });

//        取消
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit1.setText("");
                edit2.setText("");
                //edit3.setText("");
            }
        });

//        确认
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseName=edit1.getText().toString();
                courseInfo=edit2.getText().toString();
                //courseTime=edit3.getText().toString();
                courseType=spinner.getSelectedItem().toString();
                //imagePath存的图片路径，在uploadPic函数里
                if ( courseName.equals("")||courseInfo.equals("")||courseType.equals("")){
                    Toast.makeText(Create_ClassActivity.this,"信息不能为空！",Toast.LENGTH_SHORT).show();
                }
                else{

//                    把数据传到网页(不包含图片)
                    if(class_photo!=null){
                        uploadPic(class_photo);
                    }

                }

            }

        });
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
            imageView.setImageBitmap(photo);
            class_photo=photo;

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
                    photo_name="class_"+User.getUniqueKey();
                    Upload upload=new Upload(imagePath,"https://www.recycle11.top/cloud_classroom/upload_class.php",photo_name);
                    handler.sendEmptyMessage(2);
                }
            }).start();


        }
    }

}


