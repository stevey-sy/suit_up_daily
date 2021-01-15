package com.example.suitupdaily.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.suitupdaily.Home;
import com.example.suitupdaily.R;
import com.example.suitupdaily.RetrofitOpenWeather;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class WeatherFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    private TextView tv_highest_temper, tv_lowest_temper, tv_current_temper, tv_weather_status, tv_codi_guide, tv_location;
    private LottieAnimationView lottie;
    private String city_name;
    private double latitude, longitude;

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
        tv_location = (TextView)view.findViewById(R.id.tv_location);
        tv_weather_status = (TextView)view.findViewById(R.id.tv_weather_status);
        lottie = (LottieAnimationView)view.findViewById(R.id.animation_view);

        // GPS 사용하여 현재 위치의 좌표 출력
        GpsTracker gpsTracker = new GpsTracker(getActivity());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        String where = getCurrentAddress(latitude, longitude);

        Log.d("*** latitude ", String.valueOf(latitude));
        Log.d("*** longitude ", String.valueOf(longitude));
        Log.d("*** where ", where);

        // 서버에 날씨 데이터 요청하는 메소드
        getCurrentWeather();
        return view;
    }

    // 위치 정보를 가져오는 메소드
    public String getCurrentAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        // 주소 정보를 담을 리스트
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException ioException) {
            return "지오코더 서비스 사용 불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            return "잘못된 GPS 좌표";
        }

        if(addresses == null || addresses.size() == 0) {
            return "주소 미발견";
        }

        Address address = addresses.get(0);
        city_name = addresses.get(0).getLocality();
        return address.getAddressLine(0).toString()+"\n";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getCurrentWeather() {
        // 받아온 위도 경도에서 소수점 2자리 이외는 버린다.
        double edited_lat = Math.round(latitude*100)/100.0;
        double edited_lon = Math.round(longitude*100)/100.0;
        // 편집된 위도 경도 데이터를 api 에 보낸다.
        String lat = String.valueOf(edited_lat);
        String lon = String.valueOf(edited_lon);
        Log.d("위도, 경도: ", String.valueOf(edited_lat) + ", " + String.valueOf(edited_lon));
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
//                Log.d("날씨 데이터 test: ", body);
                // 필요한 데이터 = 맑음, 최저, 최고, 현재온도, 위치명, 체감온도
                String location_name = weather_data.name;
                String weather_status = weather_data.weather.get(0).description;
                Log.d("날씨 데이터 test: ", weather_status);
                String temper_current = String.valueOf(weather_data.main.temp);
                String temper_lowest = String.valueOf(weather_data.main.temp_min);
                String temper_highest = String.valueOf(weather_data.main.temp_max);
                String temper_feel = String.valueOf(weather_data.main.temp_feel);
                // TODO: 2021-01-15 받아온 온도 수치에 따라 코디 Tip이 바뀌도록 하는 메소드 필요.

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
        tv_location.setText(city_name);
        // 날씨의 상태에 따라 다른 애니매이션이 삽입 됨.
        if (status.contains("rain")) {
            // 비올 때
            tv_weather_status.setText("비");
            lottie.setAnimation("rain.json");
        } else if (status.contains("shower")) {
            // 비올 때
            tv_weather_status.setText("소나기");
            lottie.setAnimation("rain.json");
        } else if (status.contains("snow") || status.contains("sleet")) {
            // 눈올 때
            tv_weather_status.setText("눈");
            lottie.setAnimation("snow.json");
        } else if (status.contains("haze") || status.contains("cloud") || status.contains("fog")
                    || status.contains("dust") || status.contains("mist")) {
            // 흐릴 때
            tv_weather_status.setText("흐림");
            lottie.setAnimation("cloud.json");
        } else if (status.contains("storm") || status.contains("hurricane")) {
            // 천둥 번개
            tv_weather_status.setText("천둥 번개");
            lottie.setAnimation("weather-storm.json");
        } else if (status.contains("windy")) {
            tv_weather_status.setText("강한 바람");
            lottie.setAnimation("windy.json");
        } else {
            // 그 외에는
            lottie.setAnimation("sunny.json");
            tv_weather_status.setText("맑음");
        }
        lottie.loop(true);
        lottie.playAnimation();
    }
}
