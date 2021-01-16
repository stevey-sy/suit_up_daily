package com.example.suitupdaily.home;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.suitupdaily.R;
import com.example.suitupdaily.api.ResponsePOJO;
import com.example.suitupdaily.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;

public class UserBasicInfo extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private EditText tv_user_name, tv_user_nick, tv_user_birth;
    private RadioGroup radio_group_sex;
    private Button btn_save;
    private String user_id, user_sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_basic_info);

        // user id 가져오는 intent
        Intent intent_id = getIntent();
        user_id = intent_id.getStringExtra("userID");

        // 툴바 설정
        toolbar = findViewById(R.id.toolbar_basic_info);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        // xml 연결
        tv_user_name = (EditText)findViewById(R.id.text_view_user_name);
        tv_user_nick = (EditText)findViewById(R.id.text_user_nick_name);
        tv_user_birth = (EditText)findViewById(R.id.text_user_birth);
        radio_group_sex = (RadioGroup)findViewById(R.id.radio_group_sex);
        btn_save = (Button)findViewById(R.id.btn_basic_info);

        // 성별 체크 버튼 이벤트
        radio_group_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_male :
                        user_sex = "남";
                        break;
                    case R.id.radio_button_female :
                        user_sex = "여";
                        break;
                }
            }
        });

        // 저장 버튼 눌렀을 때 이벤트
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 서버에 데이터 전달하는 메소드
                uploadBasicInfo();
            }
        });
    }

    // 서버 연결 메소드 필요.
    private void uploadBasicInfo() {

        String id = user_id;
        String nick = tv_user_nick.getText().toString();
        String birth = tv_user_birth.getText().toString();
        String sex = user_sex;

        Call<ResponsePOJO> call = RetrofitClient.getInstance().getApi().uploadBasicInfo(id, nick, birth, sex);
        call.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, retrofit2.Response<ResponsePOJO> response) {
//                Toast.makeText(ShareCodi.this, response.body().getRemarks(), Toast.LENGTH_SHORT).show();
                if(response.body().isStatus()) {
                    Toast.makeText(getApplicationContext(), "기본정보 저장 완료." , Toast.LENGTH_SHORT).show();
                    // 기본 정보 입력 필요.
                    Intent intent = new Intent(UserBasicInfo.this, Home.class);
                    intent.putExtra("userID", user_id);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "저장 실패" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserBasicInfo.this, Home.class);
                    intent.putExtra("userID", user_id);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(UserBasicInfo.this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });
//        Toast.makeText (this, encodedImage, Toast.LENGTH_SHORT).show();
    }

}