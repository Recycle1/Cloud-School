package com.example.vp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class SearchActivity extends AppCompatActivity {

    private EditText editText;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editText=findViewById(R.id.et);
        imageView=findViewById(R.id.iv);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=editText.getText().toString();
                Intent intent=new Intent(SearchActivity.this,ClasslistActivity.class);
                intent.putExtra("key",s);
                startActivity(intent);
                finish();
            }
        });
    }
}