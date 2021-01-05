package com.example.suitupdaily.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.suitupdaily.R;
import com.example.suitupdaily.RetrofitOpenWeather;

import retrofit2.Call;
import retrofit2.Callback;

public class WeatherFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    private TextView tv_highest_temper, tv_lowest_temper, tv_current_temper, tv_weather_status, tv_codi_guide;
    private LottieAnimationView lottie;

    // newInstance constructor for creating fragment with arguments
    public static WeatherFragment newInstance(int page, String title) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_weather, container, false);
        // xml 연결
        tv_highest_temper = (TextView)view.findViewById(R.id.tv_highest_temper);
        tv_lowest_temper = (TextView)view.findViewById(R.id.tv_lowest_temper);
        tv_current_temper = (TextView)view.findViewById(R.id.tv_current_temper);
        tv_weather_status = (TextView)view.findViewById(R.id.tv_weather_status);
        lottie = (LottieAnimationView)view.findViewById(R.id.animation_view);
        tv_codi_guide = (TextView)view.findViewById(R.id.tv_codi_guide);

        // 서버에 날씨 데이터 요청하는 메소드
        getCurrentWeather();
        return view;
    }

    private void getCurrentWeather() {
        String lat = "37.38";
        String lon = "127.12";
        String APPID = getString(R.string.APPID);
        String metric = "metric";

        Call<ResponseOpenWeather> call = RetrofitOpenWeather.getInstance().getApi().getCurrentWeather(lat, lon, APPID, metric);
        call.enqueue(new Callback<ResponseOpenWeather>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<ResponseOpenWeather> call, retrofit2.Response<ResponseOpenWeather> response) {
//                Toast.makeText(ShareCodi.this, response.body().getRemarks(), Toast.LENGTH_SHORT).show();
                ResponseOpenWeather weather_data = response.body();

                assert weather_data != null;
                String body = String.valueOf(weather_data.name);
                Log.d("날씨 데이터 test: ", body);
                // 필요한 데이터 = 맑음, 최저, 최고, 현재온도, 위치명, 체감온도
                String location_name = weather_data.name;
                String weather_status = weather_data.weather.get(0).description;
                String temper_current = String.valueOf(weather_data.main.temp);
                String temper_lowest = String.valueOf(weather_data.main.temp_min);
                String temper_highest = String.valueOf(weather_data.main.temp_max);
                String temper_feel = String.valueOf(weather_data.main.temp_feel);
                // 가져온 데이터를 view 에 세팅하는 메소드
                setWeatherData(location_name, weather_status, temper_current, temper_lowest, temper_highest, temper_feel);
            }
            @Override
            public void onFailure(Call<ResponseOpenWeather> call, Throwable t) {
                Log.d("날씨 데이터: ", "서버통신 실패");
//                Toast.makeText(this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setWeatherData(String location, String status, String temp, String temp_min, String temp_max, String feel) {
        tv_highest_temper.setText(temp_min +"°");
        tv_lowest_temper.setText(temp_max +"°");
        tv_current_temper.setText(feel +"°");
    }
}
