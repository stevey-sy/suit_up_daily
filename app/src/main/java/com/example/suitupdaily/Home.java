package com.example.suitupdaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class Home extends AppCompatActivity {

    private Button btn_closet, btn_my_codi, btn_share_codi;
    private TextView text_view_nick;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Context context = this;
    private DrawerLayout drawerLayout;
    private String userID;
    private CircleImageView image_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // custom toolbar 가져오기
        toolbar = findViewById(R.id.toolbar_home);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_hamburger);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(drawable);

        // home activity 내 포함한 모든 컨텐츠를 담는 drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // drawer 버튼 누르면 나올 view 선언 (프로필 설정, 로그아웃)
        NavigationView navigationView = (NavigationView) findViewById(R.id.home_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.my_profile:
                        // 프로필 설정
                        Intent intent = new Intent(Home.this, MyProfile.class);
                        intent.putExtra("userID", userID);
                        startActivity(intent);
                        return true;

                    // 공유버튼 클릭했을 때의 이벤트
                    case R.id.logout:
                        // TODO: 2020-12-21 로그아웃 구현 필요
                        return true;
                }
                return true;
            }
        });

        // xml 연결
        btn_closet = findViewById(R.id.btn_closet);
        btn_my_codi = findViewById(R.id.btn_my_codi);
        btn_share_codi = findViewById(R.id.btn_share_codi);
        image_profile = findViewById(R.id.iv_home_profile);
        text_view_nick = findViewById(R.id.tv_home_nick);
        // 사용자 아이디 값 받아오는 intent
        Intent intent_get_id = getIntent();
        userID = intent_get_id.getStringExtra("userID");

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

    // 툴바 메뉴 클릭 시 이벤트 (설정 버튼)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    // 서버로부터 사용자 정보 가져오는 메소드
    // 서버에서 기본정보 받아오는 메소드
    public void getProfileInfo() {
        String id = userID;
        Call<ResponsePOJO> call = RetrofitClient.getInstance().getApi().getProfile(id);
        call.enqueue(new Callback<ResponsePOJO>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<ResponsePOJO> call, retrofit2.Response<ResponsePOJO> response) {
//                Toast.makeText(ShareCodi.this, response.body().getRemarks(), Toast.LENGTH_SHORT).show();
                if(response.body().isStatus()) {
                    String nick = response.body().getNick();
                    String photo_url = response.body().getPhotoUrl();
                    // 이미지 데이터에 문제생겼을 경우 표시될 대체 이미지.
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.skipMemoryCache(true);
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                    requestOptions.placeholder(R.drawable.ic_clothes_hanger);
                    requestOptions.error(R.drawable.ic_baseline_accessibility_24);
                    // 서버에서 받아온 데이터를 view 에 뿌려줌
//                    Glide.with(Home.this).load(photo_url).apply(requestOptions).circleCrop().into(image_profile);
//                    text_view_nick.setText(nick);
                } else {

                }
            }
            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(Home.this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 서버에서 사용자의 프로필 받아오는 메소드
        getProfileInfo ();
    }
}