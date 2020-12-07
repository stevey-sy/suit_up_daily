package com.example.suitupdaily;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    private Button btn_closet, btn_weather, btn_show_room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_closet = findViewById(R.id.btn_closet);
        btn_weather = findViewById(R.id.btn_weather);
        btn_show_room = findViewById(R.id.btn_show_room);

        Intent intent_get_id = getIntent();
        final String userID = intent_get_id.getStringExtra("userID");

        btn_closet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, MyClosetActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        btn_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, SelfCodi.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        btn_show_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ShowRoom.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

    }
}