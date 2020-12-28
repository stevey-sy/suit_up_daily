package com.example.suitupdaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class CodiArticle extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private String user_id, date, like_num, view_num, memo, hash_tags, profile_url, writer_nick, codi_url, who_liked;
    private TextView text_writer_name, text_date, text_view_num, text_like_num, text_reply_num, text_hash_tags, text_memo, title_like;
    private ImageView img_codi;
    private CircleImageView writer_pic;
    private CheckBox check_like;

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
        who_liked = intent_id.getStringExtra("who_liked");
        like_num = intent_id.getStringExtra("like_num");

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
        check_like = (CheckBox)findViewById(R.id.cb_article_like);
        title_like = (TextView) findViewById(R.id.title_article_like);
        // intent 로 부터 받아온 데이터를 view 에 뿌려준다.
        setDataFromIntent();
        // 좋아요 버튼 클릭 리스너 생성
        check_like.setOnCheckedChangeListener(this);
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
        // 좋아요 개수 가져오기 && 사용자가 좋아요 눌렀던 글인지 확인하기
        if(who_liked.contains(user_id)) {
            // 리스트에 이미 id가 존재한다면 좋아요 누른처리
//            int like = Integer.parseInt(like_num);
//            int like_edit = like -1;
//            text_like_num.setText(String.valueOf(like_edit));
            text_like_num.setText(like_num);
            check_like.setChecked(true);
            String color_orange = "#FF8C00";
            title_like.setTextColor(Color.parseColor(color_orange));
        } else {
            // 리스트에 id가 존재하지 않는다면 좋아요 버튼 default 처리
            check_like.setChecked(false);
            text_like_num.setText(like_num);
            String color_black = "#000000";
            title_like.setTextColor(Color.parseColor(color_black));
        }
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
    // 좋아요 체크박스 클릭 이벤트트
   @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // 좋아요 버튼 눌렸을 때
        if (isChecked) {
            String like_string = text_like_num.getText().toString();
            int like_int = Integer.parseInt(like_string);
            like_int += 1;
            text_like_num.setText(String.valueOf(like_int));
            String color_orange = "#FF8C00";
            title_like.setTextColor(Color.parseColor(color_orange));
        } else {
            // 좋아요 버튼 한번 더 눌렀을 때
            String like_string = text_like_num.getText().toString();
            int like_int = Integer.parseInt(like_string);
            like_int -= 1;
            text_like_num.setText(String.valueOf(like_int));
            String color_black = "#000000";
            title_like.setTextColor(Color.parseColor(color_black));
        }
    }
}