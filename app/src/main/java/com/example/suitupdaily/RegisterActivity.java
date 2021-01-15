package com.example.suitupdaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity {
    private EditText id_text, pass_text, name_text, verify_text;
    private Button register_btn, send_verify_btn, confirm_verify_btn, btn_nick_check;
    private TextView tv_verify_result, tv_nick_check_result;

    private Toolbar toolbar;
    private ActionBar actionBar;

    // 체크박스 체크 여부를 나타낼 변수
    // 체크가 안되어있을 떄 = 0
    // 체크가 되어있을 때 = 1
    public int terms_agree_usage = 0;
    public int terms_agree_private_info = 0;
    public int terms_agree_all = 0;

    // 체크박스
    AppCompatCheckBox check1;
    AppCompatCheckBox check2;
    AppCompatCheckBox check3;


    static String verify_num;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 툴바 세팅
        // custom toolbar 가져오기
        toolbar = findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        // xml 파일과 자바 클래스 연결
        id_text = (EditText) findViewById(R.id.et_id);
        pass_text = (EditText) findViewById(R.id.et_pass);
        name_text = (EditText) findViewById(R.id.et_name);
        verify_text = (EditText) findViewById(R.id.et_verify_num);
        tv_nick_check_result = (TextView) findViewById(R.id.tv_check_result);
        tv_nick_check_result.setVisibility(View.GONE);

        btn_nick_check = (Button) findViewById(R.id.btn_nick_check);
        send_verify_btn = (Button) findViewById(R.id.bt_send_verify);
        confirm_verify_btn = (Button) findViewById(R.id.bt_confirm_verify);
        register_btn = (Button) findViewById(R.id.bt_register);

        check1 = (AppCompatCheckBox) findViewById(R.id.check_app_usage);
        check2 = (AppCompatCheckBox) findViewById(R.id.check_private_info);
        check3 = (AppCompatCheckBox) findViewById(R.id.check_all_agree);

        tv_verify_result = (TextView) findViewById(R.id.tv_verify_result);

        // 인증번호 받기 버튼 눌렀을 때 코드진행
        send_verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = id_text.getText().toString();
                // 서버로부터 응답 받았을 때의 진행

                if (TextUtils.isEmpty(userID)) {
                    Toast.makeText(getApplicationContext(), "이메일 주소를 입력해 주세요." , Toast.LENGTH_SHORT).show();
                    Log.d("인증번호 버튼 눌림: ", "사용자 이메일 입력안함");
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject2 = new JSONObject(response);
                                boolean success = jsonObject2.getBoolean("success");
                                if (success) {
                                    String verifyNumber = jsonObject2.getString("verifyNum");
                                    // 서버로부터 메일발송 성공 데이터를 받았을 경우
                                    // 사용자가 입력해야 할 인증번호
                                    // 로그로 서버로부터 인증번호 받았는지 확인
                                    Log.d("서버로부터 받은 인증번호: ", verifyNumber);
                                    verify_num = verifyNumber;
                                    Toast.makeText(getApplicationContext(), "인증번호가 발송되었습니다." , Toast.LENGTH_SHORT).show();
                                } else {
                                    // 서버로부터 메일발송 실패 데이터를 받았을 경우
                                    Toast.makeText(getApplicationContext(), "인증번호 발송에 실패하였습니다." , Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    };
                    // 서버로 volley 를 이용하여 요청을 보냄.
                    RegisterVerify registerVerify = new RegisterVerify(userID, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerVerify);
                }
            }
        });

        // 인증확인 버튼 눌렀을 때의 코드
        // 인증번호 일치 불일치 체크.
        // 일치하다면 하단부에 메일주소 인증이 완료되었습니다.
        // 불일치하다면 하단부에 메일주소 인증 실패.
        confirm_verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자가 입력한 인증번호
                String verify_code = verify_text.getText().toString();
                // 서버로 부터 받은 인증번호와 사용자가 입력한 인증번호를 비교한다.
                if(verify_code.equals(verify_num)) {
                    String verify_success = "인증 성공";
                    tv_verify_result.setText(verify_success);
                    tv_verify_result.setTextColor(Color.BLUE);
                    tv_verify_result.setVisibility(View.VISIBLE);
                } else {
                    String verify_fail = "인증 실패";
                    tv_verify_result.setText(verify_fail);
                    tv_verify_result.setTextColor(Color.RED);
                    tv_verify_result.setVisibility(View.VISIBLE);
                    return;
                }
            }
        });
        // 인증번호 중복 체크 버튼 리스너
        btn_nick_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 서버 통신하여 닉네임 중복 되는지 체크
                checkNickName();
            }
        });

        // 약관 동의 버튼 리스너
        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    terms_agree_usage = 1;
                } else {
                    terms_agree_private_info = 0;
                }
            }
        });

        check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    terms_agree_private_info = 1;
                } else {
                    terms_agree_private_info = 0;
                }
            }
        });

        check3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check1.setChecked(true);
                    check2.setChecked(true);
                    terms_agree_all = 1;
                } else {
                    check1.setChecked(false);
                    check2.setChecked(false);
                    terms_agree_all = 0;
                }
            }
        });

        // 회원 가입 버튼 클릭 시 수행
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = id_text.getText().toString();
                String userPass = pass_text.getText().toString();
                String userName = name_text.getText().toString();
                // 모두 동의가 체크되어 있지 않은 상태의 진행
                if (terms_agree_all != 1) {
                    if (terms_agree_private_info == 1) {
                        if (terms_agree_usage == 1) {
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        if (success) {
                                            // 회원 등록에 성공한 경우
                                            Toast.makeText(getApplicationContext(), "회원등록에 성공하였습니다." , Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        } else {
                                            // 회원 등록 실패 경우
                                            Toast.makeText(getApplicationContext(), "회원등록에 실패하였습니다." , Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            // 서버로 volley 를 이용하여 요청을 보냄.
                            RegisterRequest registerRequest = new RegisterRequest(userID, userPass, userName, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                            queue.add(registerRequest);
                        } else {
                            Toast.makeText(getApplicationContext(), "약관을 체크해주세요." , Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "약관을 체크해주세요." , Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    // 모두 동의 버튼이 체크되어 있는 상태
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    // 회원 등록에 성공한 경우
                                    Toast.makeText(getApplicationContext(), "회원등록에 성공하였습니다." , Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    // 회원 등록 실패 경우
                                    Toast.makeText(getApplicationContext(), "회원등록에 실패하였습니다." , Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    // 서버로 volley 를 이용하여 요청을 보냄.
                    RegisterRequest registerRequest = new RegisterRequest(userID, userPass, userName, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                }
            }
        });

    } // OnCreate 닫기
    // 닉네임 중복 체크하는 메소드 필요
    private void checkNickName() {
        // 서버에 보낼 데이터 정의
        String nick = name_text.getText().toString();
        // 서버 통신에 사용할 api instance 생성
        Call<ResponsePOJO> call = RetrofitClient.getInstance().getApi().checkNickName(nick);
        // 통신 응답
        call.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, retrofit2.Response<ResponsePOJO> response) {
                if(response.body().isStatus()) {
                    // 서버로부터 true 값을 받았다면, 닉네임 사용 가능
                    tv_nick_check_result.setText("사용 가능한 닉네임 입니다.");
                    tv_nick_check_result.setTextColor(Color.BLUE);
                } else {
                    // 서버로부터 false 값을 받았다면, 닉네임 사용 불가능
                    tv_nick_check_result.setText("이미 사용 중인 닉네임 입니다.");
                    tv_nick_check_result.setTextColor(Color.RED);
                }
                tv_nick_check_result.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // 툴바 메뉴 클릭 시 이벤트 (설정 버튼)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}