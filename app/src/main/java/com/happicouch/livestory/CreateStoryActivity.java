package com.happicouch.livestory;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

public class CreateStoryActivity extends AppCompatActivity {

    //Const
    private static final String TAG = "CreateStoryActivity";

    //Widgets
    private EditText title;
    private ImageView thumbnail;
    private Button createStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);

        title = findViewById(R.id.createStoryEditTitle);
        thumbnail = findViewById(R.id.createStoryThumbnail);
        createStory = findViewById(R.id.createStoryButton);
        init();

        createStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().trim().equals("")){
                    //Create story. (upload to database).
                }
            }
        });
    }

    protected void init(){
        String filePath = getIntent().getStringExtra("image");
        Glide
                .with(this)
                .load(Uri.fromFile(new File(filePath)))
                .centerCrop()
                .into(thumbnail);
    }
}