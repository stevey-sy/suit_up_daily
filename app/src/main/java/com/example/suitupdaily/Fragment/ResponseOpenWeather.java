package com.example.suitupdaily.Fragment;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseOpenWeather {

    @SerializedName("coord")
    public Coord coord;
    @SerializedName("sys")
    public Sys sys;
    @SerializedName("weather")
    public ArrayList<Weather> weather = new ArrayList<Weather>();
    @SerializedName("main")
    public Main main;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("rain")
    public Rain rain;
    @SerializedName("clouds")
    public Clouds clouds;
    @SerializedName("dt")
    public float dt;
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("cod")
    public float cod;

    public class Weather {
        @SerializedName("id")
        public int id;
        @SerializedName("main")
        public String main;
        @SerializedName("description")
        public String description;
        @SerializedName("icon")
        public String icon;
    }

    public class Clouds {
        @SerializedName("all")
        public float all;
    }

    public class Rain {
        @SerializedName("3h")
        public float h3;
    }

    public class Wind {
        @SerializedName("speed")
        public float speed;
        @SerializedName("deg")
        public float deg;
    }

    public class Main {
        @SerializedName("temp")
        public float temp;
        @SerializedName("humidity")
        public float humidity;
        @SerializedName("pressure")
        public float pressure;
        @SerializedName("temp_min")
        public float temp_min;
        @SerializedName("temp_max")
        public float temp_max;
        @SerializedName("feels_like")
        public float temp_feel;
    }

    public class Sys {
        @SerializedName("country")
        public String country;
        @SerializedName("sunrise")
        public long sunrise;
        @SerializedName("sunset")
        public long sunset;
    }

    public class Coord {
        @SerializedName("lon")
        public float lon;
        @SerializedName("lat")
        public float lat;
    }

}


