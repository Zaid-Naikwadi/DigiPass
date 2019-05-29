package com.example.rohan.hello;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MyTicket extends AppCompatActivity {

    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket);

        img = (ImageView)findViewById(R.id.imgview);

        String imagefile ="/sdcard/bus/currentpass.jpg";



        Bitmap bm = BitmapFactory.decodeFile(imagefile);
        img.setImageBitmap(bm);

    }
}
