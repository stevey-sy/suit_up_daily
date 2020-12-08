package com.example.suitupdaily;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suitupdaily.dialog.SeasonMultipleChoice;

// 사용자가 코디 이미지를 선택 후, 코디에 대한 정보를 입력 할 수 있는 액티비티
public class CodiInfo extends AppCompatActivity {

    private ImageView image_view_codi;
    private Bitmap bitmap;
    private TextView text_view_codi_season;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codi_info);

        image_view_codi = (ImageView) findViewById(R.id.image_view_codi);
        text_view_codi_season = (TextView) findViewById(R.id.text_view_codi_season);

        // SelfCodi 클래스에서 사용자가 편집한 이미지를 받아오는 intent
        Intent getDataFromSelfCodi = getIntent();
        byte[] arr = getDataFromSelfCodi.getByteArrayExtra("edited_image");
        // 받아온 이미지(byte array)를 비트맵 으로 바꾸는 코드
        bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        image_view_codi.setImageBitmap(bitmap);

        // 코디 계절 선택 버튼 눌렀을 때의 이벤트
        text_view_codi_season.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 계절 다중선택 가능한 커스텀 다이얼로그 생성
                SeasonMultipleChoice seasonMultipleChoice = new SeasonMultipleChoice(CodiInfo.this);
                seasonMultipleChoice.callFunction(text_view_codi_season);

//                DialogSeason dialogSeason = new DialogSeason(ImageEditActivity.this);
//                dialogSeason.callFunction(text_season);
            }
        });







    }
}