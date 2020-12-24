package com.example.suitupdaily;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class MyProfile extends AppCompatActivity {

    private String user_id, user_sex;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Button btn_save;
    private RadioGroup radio_group_sex;
    private RadioButton radio_button_male, radio_button_female;
    private FloatingActionButton button_camera;
    private EditText text_nick, text_birth, text_id;
    private ImageView image_profile;
    private TextView tv_user_id;
    private Menu action;
    private Uri uri;
    private Bitmap bitmap;

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
        tv_user_id = (TextView) findViewById(R.id.text_view_user_id);
        text_id = (EditText) findViewById(R.id.tv_profile_id);
        text_nick = (EditText) findViewById(R.id.tv_profile_nick);
        text_birth = (EditText) findViewById(R.id.tv_profile_birth);
        btn_save = (Button) findViewById(R.id.btn_profile_confirm);
        radio_group_sex = (RadioGroup)findViewById(R.id.rg_profile_sex);
        radio_button_male = (RadioButton)findViewById(R.id.rb_profile_male);
        radio_button_female = (RadioButton)findViewById(R.id.rb_profile_female);
        button_camera = (FloatingActionButton)findViewById(R.id.fb_profile_camera);
        image_profile = (CircleImageView)findViewById(R.id.iv_profile_pic);
        // 정보 기입란 비활성화 메소드
        readMode();
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
        // 카메라 버튼 이벤트
        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // 고르고 나면, startActivityForResult 로 넘어간다.
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                try {
                    uri= data.getData();
                    image_profile.setImageURI(uri);
//                       mCurrentPhotoPath = createCopyAndReturnRealPath(this, uri);
//                       Log.d("절대경로 : ", uri.toString() + "|n" + mCurrentPhotoPath);
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), uri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                    //image_edit.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 툴바 메뉴 불러오기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_cloth_info, menu);
        action = menu;
        action.findItem(R.id.confirm).setVisible(false);
        action.findItem(R.id.modify_cancel).setVisible(false);
        action.findItem(R.id.delete).setVisible(false);
        return true;
    }

    // 툴바 메뉴 클릭시 이벤트 (프로필 수정 버튼)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modify:
                // 버튼들 활성화
                editMode();
                // 메뉴바에서 수정 버튼 사라지고 수정 취소, 저장 메뉴가 생김
                action.findItem(R.id.modify).setVisible(false);
                action.findItem(R.id.modify_cancel).setVisible(true);
                action.findItem(R.id.confirm).setVisible(true);
                return true;
            case R.id.modify_cancel:
                readMode();
                action.findItem(R.id.modify).setVisible(true);
                action.findItem(R.id.modify_cancel).setVisible(false);
                action.findItem(R.id.confirm).setVisible(false);
                return true;
            case R.id.confirm:
                action.findItem(R.id.modify).setVisible(true);
                action.findItem(R.id.modify_cancel).setVisible(false);
                action.findItem(R.id.confirm).setVisible(false);
                // TODO: 2020-12-24 서버에 수정된 데이터 업로드 메소드 필요
        }
        return super.onOptionsItemSelected(item);
    }

    // 서버에서 기본정보 받아오는 메소드
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

                    tv_user_id.setText(nick);
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

    // 사용자가 정보를 조회만 할 수 있는 메소드
    public void readMode() {
        // 정보기입란 비활성화
        String color_code = "#000000";
        text_nick.setTextColor(Color.parseColor(color_code));
        text_birth.setTextColor(Color.parseColor(color_code));
        text_id.setClickable(false);
        text_id.setFocusable(false);
        text_nick.setClickable(false);
        text_nick.setFocusable(false);
        text_birth.setClickable(false);
        text_birth.setFocusable(false);
        radio_button_male.setEnabled(false);
        radio_button_female.setEnabled(false);
        btn_save.setVisibility(View.GONE);
        button_camera.setVisibility(View.GONE);
    }

    // 정보 기입란이 활성화 됨. (수정 버튼 눌렸을 때 사용)
    public void editMode() {
        String color_code = "#1e90ff";
        text_nick.setFocusableInTouchMode(true);
        text_nick.setTextColor(Color.parseColor(color_code));
        text_birth.setFocusableInTouchMode(true);
        text_birth.setTextColor(Color.parseColor(color_code));
        radio_button_male.setEnabled(true);
        radio_button_female.setEnabled(true);
        btn_save.setVisibility(View.VISIBLE);
        button_camera.setVisibility(View.VISIBLE);
        Toast.makeText(MyProfile.this, "프로필을 수정할 수 있습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 서버에서 정보 받아오는 메소드
        getProfileInfo ();
    }
}