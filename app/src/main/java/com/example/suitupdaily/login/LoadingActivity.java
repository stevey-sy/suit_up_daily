package com.example.suitupdaily.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.suitupdaily.R;
import com.example.suitupdaily.home.Home;

import org.json.JSONException;
import org.json.JSONObject;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        // 로딩 화면 시작할 때에 사용될 메소드
        startLoading();
    }
    // 4초간 대기 후 자동 종료되는 메소드
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Shared Preferences 에 사용자의 데이터를 조회
                checkUserInfo();
            }
        }, 4000);
    }

    private void checkUserInfo () {
        // Shared Preferences 에 auto login 관련 데이터가 저장되어 있는지 조회
        // 있다면, 자동로그인 진행
        String loadSharedName = "AutoLogIn"; // 가져올 SharedPreferences 이름 지정
        String loadKeyNameID = "id"; // 가져올 데이터의 Key값 지정
        String loadKeyNamePass = "pass";
        String savedID = ""; // 가져올 데이터가 담기는 변수
        String savedPass = "";
        String defaultValue = ""; // 가져오는것에 실패 했을 경우 기본 지정 텍스트.

        SharedPreferences loadShared = getSharedPreferences(loadSharedName,MODE_PRIVATE);
        savedID = loadShared.getString(loadKeyNameID, defaultValue);
        savedPass = loadShared.getString(loadKeyNamePass, defaultValue);
        // 만약 savedID, savedPass 가 빈값이 아니라면
        if (!savedID.isEmpty() && !savedPass.isEmpty()) {
            // 저장되어있는 회원 정보로 자동 로그인하여 메인화면으로 전환
            RequestAutoLogIn(savedID, savedPass);
        } else {
            // 저장된 데이터가 없다면 로그인 화면으로 전환
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("userData", "none");
            startActivity(intent);
        }
    }

    private void RequestAutoLogIn (String userID, final String userPass) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        String userID = jsonObject.getString("userID");
                        Toast.makeText(getApplicationContext(), "자동 로그인에 성공하였습니다." , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoadingActivity.this, Home.class);
                        intent.putExtra("userID", userID);
                        startActivity(intent);
                    } else {
                        // 로그인에 실패 경우
                        Log.d("인증번호 버튼 눌림: ", "사용자 이메일 입력안함");
                        Toast.makeText(getApplicationContext(), "아이디 혹은 비밀번호가 잘못되었습니다." , Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        LoginRequest loginRequest = new LoginRequest(userID, userPass, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoadingActivity.this);
        queue.add(loginRequest);

    }
}