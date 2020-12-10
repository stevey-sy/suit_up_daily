package com.example.suitupdaily;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class CodiDetail extends AppCompatActivity {

    private String user_id, picture, tags, memo, idx, season;
    private ImageView img_codi;
    private TextView text_view_codi_season, text_view_codi_tags, text_view_codi_memo, input_tag;
    private Button button_remove_tag, button_input_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codi_detail);

        // xml 요소 연결
        img_codi = (ImageView) findViewById(R.id.image_view_codi_detail);
        text_view_codi_tags = (TextView) findViewById(R.id.text_view_tag_list);
        text_view_codi_memo = (TextView) findViewById(R.id.text_view_memo_detail);

        // ShowRoom 클래스에서 받은 intent 데이터
        Intent getCodiInfo = getIntent();
        user_id = getCodiInfo.getStringExtra("userID");
        picture = getCodiInfo.getStringExtra("picture");
        season = getCodiInfo.getStringExtra("season");
        tags = getCodiInfo.getStringExtra("tags");
        memo = getCodiInfo.getStringExtra("memo");

        // 받아온 intent 데이터를 view 에 뿌려주는 메소드
        setDataFromIntentExtra();
    }

    // 받아온 intent 데이터를 view 에 뿌려주는 메소드
    @SuppressLint("CheckResult")
    private void setDataFromIntentExtra() {

        readMode();
        text_view_codi_tags.setText(tags);
        text_view_codi_memo.setText(memo);

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

    // 사용자의 조회 모드
    // 텍스트뷰를 선택해도 정보를 수정할 수 없다.
    private void readMode() {


    }
}