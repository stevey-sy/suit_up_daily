package com.example.suitupdaily;

import com.example.suitupdaily.Fragment.ResponseOpenWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiOpenWeather {

    @GET("weather?")
    Call<ResponseOpenWeather> getCurrentWeather(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("APPID") String APPID
    );
}
