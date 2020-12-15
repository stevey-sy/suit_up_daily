package com.example.suitupdaily;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

public class ShareCodi extends AppCompatActivity {

    private TextView filter_recently, filter_sex, filter_age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_codi);

        //xml 연결
        filter_recently = (TextView)findViewById(R.id.filter_recently);
        filter_sex = (TextView)findViewById(R.id.filter_sex);
        filter_age = (TextView)findViewById(R.id.filter_age);

        // 필터 버튼 클릭 리스터
        filter_recently.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupButtonClick(filter_recently);
            }
        });

    }
    // 필터링 버튼 "최신순" 클릭했을 때의 이벤트 메소드
    public void onPopupButtonClick(View button) {
        //popup menu 객체 생성
        PopupMenu popup = new PopupMenu (this, button);
        // xml 과 연결
        popup.getMenuInflater().inflate(R.menu.menu_filter_recently, popup.getMenu());

        // popup 메뉴 클릭 시 이벤트
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.recently:
                        break;
                    case R.id.popular:
                        break;
                }
                return true;
            }
        });
        popup.show();
    }
}