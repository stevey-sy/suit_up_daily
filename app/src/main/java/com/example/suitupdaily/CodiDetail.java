package com.example.suitupdaily;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

public class CodiDetail extends AppCompatActivity {

    private String user_id, picture, tags, memo, idx, season, date;
    private ImageView img_codi;
    private TextView text_view_codi_season, text_view_codi_tags, text_view_codi_memo, text_view_date;
    private Button button_remove_tag, button_input_tag;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Menu action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codi_detail);

        // 툴바 설정
        toolbar = findViewById(R.id.toolbar_codi_detail);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        // xml 요소 연결
        img_codi = (ImageView) findViewById(R.id.image_view_codi_detail);
        text_view_codi_tags = (TextView) findViewById(R.id.text_view_tag_list);
        text_view_codi_memo = (TextView) findViewById(R.id.text_view_memo_detail);
        text_view_date = (TextView) findViewById(R.id.text_view_date_detail);

        // ShowRoom 클래스에서 받은 intent 데이터
        Intent getCodiInfo = getIntent();
        user_id = getCodiInfo.getStringExtra("userID");
        picture = getCodiInfo.getStringExtra("picture");
        season = getCodiInfo.getStringExtra("season");
        tags = getCodiInfo.getStringExtra("tags");
        memo = getCodiInfo.getStringExtra("memo");
        date = getCodiInfo.getStringExtra("date");

        // 받아온 intent 데이터를 view 에 뿌려주는 메소드
        setDataFromIntentExtra();
    }

    // 받아온 intent 데이터를 view 에 뿌려주는 메소드
    @SuppressLint("CheckResult")
    private void setDataFromIntentExtra() {

        text_view_codi_tags.setText(tags);
        text_view_codi_memo.setText(memo);
        text_view_date.setText("Date: " + date);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.ic_hanger);
        requestOptions.error(R.drawable.ic_baseline_accessibility_24);

        Glide.with(CodiDetail.this)
                .load(picture)
                .apply(requestOptions)
                .into(img_codi);
    }

    // 툴바 메뉴 불러오기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.codi_detail_menu, menu);

        action = menu;

        return true;
    }


}