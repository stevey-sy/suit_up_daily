package com.example.suitupdaily;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

// 사용자가 코디 이미지를 선택 후, 코디에 대한 정보를 입력 할 수 있는 액티비티
public class CodiInfo extends AppCompatActivity {

    private ImageView image_view_codi;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codi_info);

        image_view_codi = (ImageView) findViewById(R.id.image_view_codi);

        Intent intent = getIntent();
        byte[] arr = getIntent().getByteArrayExtra("edited_image");

        bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        image_view_codi.setImageBitmap(bitmap);





    }
}