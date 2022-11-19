package com.example.vp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Big_imageActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        imageView=findViewById(R.id.iv);
        Glide.with(this).load(getIntent().getStringExtra("image")).into(imageView);
    }
}