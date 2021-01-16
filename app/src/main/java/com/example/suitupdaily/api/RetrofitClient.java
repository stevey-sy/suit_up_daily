package com.example.suitupdaily.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://3.35.131.47/";
    private static RetrofitClient myClient;
    private Retrofit retrofit;
    private static Retrofit retrofit_static;

    private RetrofitClient () {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getApiClient() {
        if (retrofit_static == null) {
            retrofit_static = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit_static;
    }

    public static synchronized RetrofitClient getInstance() {

        if(myClient == null) {
            myClient = new RetrofitClient();
        }

        return myClient;
    }

    public Api getApi () {
        return retrofit.create(Api.class);
    }

}
