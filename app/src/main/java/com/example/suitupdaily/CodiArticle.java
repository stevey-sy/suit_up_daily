package com.example.suitupdaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class CodiArticle extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private String user_id, date, like_num, view_num, memo, hash_tags, profile_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codi_article);
        //사용자 아이디를 받는 intent
        Intent intent_id = getIntent();
        user_id = intent_id.getStringExtra("userID");
        // 툴바 설정
        toolbar = findViewById(R.id.toolbar_article);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_left_arrow);
    }
    // 툴바 메뉴 클릭시 이벤트 (홈 버튼)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}