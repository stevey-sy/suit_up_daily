package com.example.suitupdaily;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText id_text, pass_text;
    private Button login_btn, register_btn;
    private String user_id;

    private SignInButton btn_google;
    private FirebaseAuth auth;
    private GoogleApiClient googleApiClient;
    // 구글 로그인 결과 코드
    private static final int REQ_SIGN_GOOGLE = 100;
    private boolean check_new_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        sessionCallback = new SessionCallback();
//        Session.getCurrentSession().addCallback(sessionCallback);
//        Session.getCurrentSession().checkAndImplicitOpen();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        auth= FirebaseAuth.getInstance();

        btn_google = findViewById(R.id.btn_google);
        // 구글 로그인 버튼 클릭시 진행 코드
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });

        id_text = findViewById(R.id.et_id);
        pass_text = findViewById(R.id.et_pass);
        login_btn = findViewById(R.id.bt_login);
        register_btn = findViewById(R.id.bt_register);

        //회원가입 버튼 클릭시 수행
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // 로그인 버튼 클릭시 수행
        login_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String userID = id_text.getText().toString();
                String userPass = pass_text.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                String userID = jsonObject.getString("userID");
//                                String userPass = jsonObject.getString("userPass");

                                // 로그인에 성공한 경우
                                Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다." , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, Home.class);
                                intent.putExtra("userID", userID);
//                                intent.putExtra("userPass", userPass);
                                startActivity(intent);
                            } else {
                                // 로그인에 실패 경우
                                Log.d("인증번호 버튼 눌림: ", "사용자 이메일 입력안함");
                                Log.d("인증번호 버튼 눌림: ", "사용자 이메일 입력안함");
                                Log.d("인증번호 버튼 눌림: ", "사용자 이메일 입력안함");
                                Log.d("인증번호 버튼 눌림: ", "사용자 이메일 입력안함");
                                Log.d("인증번호 버튼 눌림: ", "사용자 이메일 입력안함");
                                Toast.makeText(getApplicationContext(), "아이디 혹은 비밀번호가 잘못되었습니다." , Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, userPass, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

            }
        });
    }

    // 구글 로그인 인증을 요청했을 때 결과값을 되돌려 받는 코드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SIGN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            // 인증 결과가 성공적일 때의 코드
            if(result.isSuccess()) {
                // account 라는 데이터는 구글 로그인 정보를 담고 있다.

                GoogleSignInAccount account = result.getSignInAccount();
                // 로그인 결과 값 출력 수행 메소드
                resultLogin(account);
            }
        }
    }

    private void resultLogin(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 로그인이 성공했으면 코드 진행
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "로그인이 되었습니다.", Toast.LENGTH_SHORT).show();
                            user_id = account.getEmail();
                            uploadUserList(user_id);
                            Log.d("resultLogin: ", user_id);
//                            if(check_new_user) {
//                                Intent intent = new Intent(LoginActivity.this, UserBasicInfo.class);
//                                intent.putExtra("userID", user_id);
//                                startActivity(intent);
//                            } else {
//
//                            }
                            // 서버에다가 email 조회, 있으면 넘어감
                            // 없으면 회원 리스트에 추가

//                            Intent intent = new Intent(getApplicationContext(), Home.class);
//                            intent.putExtra("userID",account.getEmail());
//                            startActivity(intent);

                       } else {
                            Toast.makeText(LoginActivity.this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                         // 로그인 실패

                        }
                    }
                });
    }

    private void uploadUserList(String id) {

        Call<ResponsePOJO> call = RetrofitClient.getInstance().getApi().uploadUserList(id);
        call.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, retrofit2.Response<ResponsePOJO> response) {
//                Toast.makeText(ShareCodi.this, response.body().getRemarks(), Toast.LENGTH_SHORT).show();
                if(response.body().getRemarks().equals("저장")) {
                    Toast.makeText(getApplicationContext(), "회원 리스트에 추가되었습니다." , Toast.LENGTH_SHORT).show();
                    check_new_user = true;
                    // 기본 정보 입력 필요.
                    Intent intent = new Intent(LoginActivity.this, UserBasicInfo.class);
                    intent.putExtra("userID", user_id);
                    startActivity(intent);
                } else if (response.body().getRemarks().equals("중복")){
                    Toast.makeText(getApplicationContext(), "기존 회원입니다." , Toast.LENGTH_SHORT).show();
                    check_new_user = false;
                    Intent intent = new Intent(LoginActivity.this, Home.class);
                    intent.putExtra("userID", user_id);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });
//        Toast.makeText (this, encodedImage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}