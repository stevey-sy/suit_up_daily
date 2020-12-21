package com.example.suitupdaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity {

    private Button btn_closet, btn_my_codi, btn_share_codi;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Context context = this;
    private DrawerLayout drawerLayout;

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
}