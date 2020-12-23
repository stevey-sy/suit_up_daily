package com.example.suitupdaily;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;

public class MyProfile extends AppCompatActivity {

    private String user_id, user_sex;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Button btn_save;
    private RadioGroup radio_group_sex;
    private RadioButton radio_button_male, radio_button_female;
    private EditText text_nick, text_birth, text_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // user id 가져오는 intent
        Intent intent_id = getIntent();
        user_id = intent_id.getStringExtra("userID");

        // 툴바 설정
        toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        // xml 연결
        text_id = (EditText) findViewById(R.id.tv_profile_id);
        text_nick = (EditText) findViewById(R.id.tv_profile_nick);
        text_birth = (EditText) findViewById(R.id.tv_profile_birth);
        btn_save = (Button) findViewById(R.id.btn_profile_confirm);
        radio_group_sex = (RadioGroup)findViewById(R.id.rg_profile_sex);
        radio_button_male = (RadioButton)findViewById(R.id.rb_profile_male);
        radio_button_female = (RadioButton)findViewById(R.id.rb_profile_female);
        // 성별 체크 버튼 이벤트
        radio_group_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_profile_male :
                        user_sex = "남";
                        break;
                    case R.id.rb_profile_female :
                        user_sex = "여";
                        break;
                }
            }
        });
    }
    // 서버에서 기본정보 받아오는 메소드 필요
    public void getProfileInfo () {

        String id = user_id;

        Call<ResponsePOJO> call = RetrofitClient.getInstance().getApi().getProfile(id);
        call.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, retrofit2.Response<ResponsePOJO> response) {
//                Toast.makeText(ShareCodi.this, response.body().getRemarks(), Toast.LENGTH_SHORT).show();
                if(response.body().isStatus()) {
                    String nick = response.body().getNick();
                    String birth = response.body().getBirth();
                    String sex = response.body().getSex();

                    text_id.setText(user_id);
                    text_nick.setText(nick);
                    text_birth.setText(birth);
                    if (sex.equals("남")) {
                        radio_button_male.setChecked(true);
                    } else if (sex.equals("여")) {
                        radio_button_female.setChecked(true);
                    }

                } else {

                }
            }
            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(MyProfile.this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 서버에서 정보 받아오는 메소드
        getProfileInfo ();
    }
}