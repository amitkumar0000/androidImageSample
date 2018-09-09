package com.android.imageslide.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.imageslide.R;
import com.android.imageslide.Utils.Const;

import java.io.ByteArrayOutputStream;

public class ImageActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = findViewById(R.id.imageView2);
        byte[] image = (byte[]) getIntent().getExtras().get(Const.IMAGESRC);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length, options);
        imageView.setImageBitmap(bitmap);


    }
}
