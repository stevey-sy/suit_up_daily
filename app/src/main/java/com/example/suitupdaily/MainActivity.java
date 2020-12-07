package com.example.suitupdaily;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragHome frag_home;
    private FragCloset frag_closet;
    private FragCodi frag_codi;
    private FragSNS frag_sns;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Tool Bar 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        // 뒤로가기 버튼
        actionBar.setDisplayHomeAsUpEnabled(true);

        bottomNavigationView = findViewById(R.id.bottomNavi);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder (R.id.homeFragment, R.id.closetFragment, R.id.codiFragment, R.id.snsFragment).build();
        NavController navController = Navigation.findNavController(this, R.id.fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);







//       bottomNavigationView = findViewById(R.id.bottomNavi);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.action_home:
//                        setFrag(0);
//                        break;
//                    case R.id.action_closet:
//                        setFrag(1);
//                        break;
//                    case R.id.action_codi:
//                        setFrag(2);
//                        break;
//                    case R.id.action_sns:
//                        setFrag(3);
//                        break;
//
//                }
//
//                return true;
//            }
//        });
//        frag_home = new FragHome();
//        frag_closet = new FragCloset();
//        frag_codi = new FragCodi();
//        frag_sns = new FragSNS();
//        // 첫 프래그먼트 화면 지정. 0번.
//        setFrag(0);
    }


    public boolean onCreateOptionMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id== R.id.main) {
//            Toast.makeText(getApplicationContext(), "You click main", Toast.LENGTH_SHORT).show();
//        } else if (id== R.id.main2) {
//            Toast.makeText(getApplicationContext(), "You click main2", Toast.LENGTH_SHORT).show();
//        } else if (id== R.id.main3) {
//            Toast.makeText(getApplicationContext(), "You click main3", Toast.LENGTH_SHORT).show();
//        }
//
//        return true;
//    }

    // 프래그먼트 교체 일어나는 실행문
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch(n) {
            case 0:
                ft.replace(R.id.main_frame, frag_home);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, frag_closet);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, frag_codi);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_frame, frag_sns);
                ft.commit();
                break;
        }
    }
}