package com.example.suitupdaily;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    private Button btn_closet, btn_my_codi, btn_share_codi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // xml 연결
        btn_closet = findViewById(R.id.btn_closet);
        btn_my_codi = findViewById(R.id.btn_my_codi);
        btn_share_codi = findViewById(R.id.btn_share_codi);
        // 사용자 아이디 값 받아오는 intent
        Intent intent_get_id = getIntent();
        final String userID = intent_get_id.getStringExtra("userID");

        // 옷장 버튼 클릭 이벤트
        btn_closet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, MyClosetActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
        // 코디 버튼 클릭 이벤트
        btn_my_codi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ShowRoom.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
        // 코디 공유하기 버튼 클릭 이벤트
        btn_share_codi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ShareCodi.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }
}