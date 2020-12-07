package com.example.suitupdaily.login;

import retrofit2.Call;
import retrofit2.http.GET;

public interface userApi {

// 서버로부터 결과를 받는다.
                      // 메서드 이름
    @GET("login/login.php")
    Call<ResultModel> login();
}
