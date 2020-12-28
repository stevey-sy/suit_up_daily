package com.example.suitupdaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class CodiArticle extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private String user_id, date, like_num, view_num, memo, hash_tags, profile_url, writer_nick, codi_url;
    private TextView text_writer_name, text_date, text_view_num, text_like_num, text_reply_num, text_hash_tags, text_memo;
    private ImageView img_codi;
    private CircleImageView writer_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codi_article);
        // 게시글의 데이터를 받아오는 intent
        Intent intent_id = getIntent();
        user_id = intent_id.getStringExtra("userID");
        writer_nick = intent_id.getStringExtra("nick");
        date = intent_id.getStringExtra("date");
        hash_tags = intent_id.getStringExtra("tags");
        memo = intent_id.getStringExtra("memo");
        codi_url = intent_id.getStringExtra("picture");
        profile_url = intent_id.getStringExtra("profile_pic");

        // 툴바 세팅
        toolbar = findViewById(R.id.toolbar_article);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        //xml 연결
        text_writer_name = (TextView) findViewById(R.id.tv_article_writer);
        text_date = (TextView) findViewById(R.id.tv_article_date);
        text_view_num = (TextView) findViewById(R.id.tv_article_views);
        text_like_num = (TextView) findViewById(R.id.tv_article_like);
        text_memo = (TextView) findViewById(R.id.tv_article_memo);
        text_hash_tags = (TextView) findViewById(R.id.tv_article_hash);
        text_reply_num = (TextView) findViewById(R.id.tv_article_reply_num);
        writer_pic = (CircleImageView)findViewById(R.id.civ_article_profile);
        img_codi = (ImageView)findViewById(R.id.iv_article_codi);
        // intent 로 부터 받아온 데이터를 view 에 뿌려준다.
        setDataFromIntent();
    }

    // 인텐트로 받아온 데이터를 뷰에 뿌려주는 메소드
    private void setDataFromIntent() {
        text_writer_name.setText(writer_nick);
        text_memo.setText(memo);
        text_hash_tags.setText(hash_tags);
        // 게시글 등록일 데이터 시간부분 자르기
        // ex) 2020-12-10 00:00:00 --------> 2020-12-10
        String short_date = date.substring(0,10);
        text_date.setText(short_date);
        // 이미지 데이터에 문제생겼을 경우 표시될 대체 이미지.
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.ic_clothes_hanger);
        requestOptions.error(R.drawable.ic_baseline_accessibility_24);
        // 프로필 이미지 세팅
        Glide.with(this)
                .load(profile_url)
                .apply(requestOptions)
                .circleCrop()
                .into(writer_pic);
        // 코디 이미지 세팅
        Glide.with(this)
                .load(codi_url)
                .apply(requestOptions)
                .into(img_codi);
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