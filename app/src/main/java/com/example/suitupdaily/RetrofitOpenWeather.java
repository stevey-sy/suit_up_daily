package com.example.suitupdaily;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitOpenWeather {
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static RetrofitOpenWeather myClient;
    private Retrofit retrofit;
    private static Retrofit retrofit_static;

    private RetrofitOpenWeather () {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    static Retrofit getApiClient () {
        if (retrofit_static == null) {
            retrofit_static = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit_static;
    }

    public static synchronized RetrofitOpenWeather getInstance() {

        if(myClient == null) {
            myClient = new RetrofitOpenWeather();
        }

        return myClient;
    }

    public ApiOpenWeather getApi () {
        return retrofit.create(ApiOpenWeather.class);
    }
}
