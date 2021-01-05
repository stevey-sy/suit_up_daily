package com.example.suitupdaily;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.suitupdaily.filter.FilterSeason;
import com.example.suitupdaily.filter.SeasonAdapter;
import com.example.suitupdaily.grid.ClosetItem;
import com.example.suitupdaily.grid.ClosetViewer;
import com.example.suitupdaily.grid.GridAdapter;
import com.example.suitupdaily.listview.FilterSeasonAdapter;
import com.example.suitupdaily.recycler.ColorAdapter;
import com.example.suitupdaily.recycler.RecyclerAdapter;
import com.example.suitupdaily.viewpager.SeasonPageAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyClosetActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    RecyclerAdapter.RecyclerViewClickListener listener;
    Api apiInterface;
    private List<ResponsePOJO> clothList;
//    GridAdapter gridAdapter;
    private TextView tv_closet_empty;
    private Button btn_load_upper, btn_load_bottom, btn_load_outer, btn_load_shoes, btn_load_accessory, btn_filter_on, btn_filter_off;
    private String user_id, load_type;
    private FloatingActionButton btn_add_action;
    private SlidingUpPanelLayout slidingLayout;

    private Toolbar toolbar;
    private ActionBar actionBar;

    private RecyclerView recycler_view_season;
    private SeasonAdapter season_adapter;
    private RecyclerView.LayoutManager season_layout_manager;
    private ArrayList<String> arrayList = new ArrayList<>();

    private List<FilterSeason> season_list;

    private RadioGroup season_radio_group;
    private String selected_season = "all";

    private FirebaseAuth mAuth;
    private RecyclerView recycler_color;
    private ColorAdapter color_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_closet);

        //Firebase 로그인한 사용자 정보
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        Log.d("구글 닉네임: ", user.getDisplayName());

        // 툴바 세팅
        // 툴바 세팅
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        Intent intent_id = getIntent();
        user_id = intent_id.getStringExtra("userID");
        load_type = "upper";

        recyclerView = findViewById(R.id.recycler_view);
        btn_load_upper = findViewById(R.id.btn_upper);
        btn_load_upper.setBackgroundColor(Color.BLACK);
        btn_load_upper.setTextColor(Color.WHITE);

        btn_load_bottom = findViewById(R.id.btn_bottom);
        btn_load_bottom.setBackgroundColor(Color.LTGRAY);
        btn_load_outer = findViewById(R.id.btn_outer);
        btn_load_outer.setBackgroundColor(Color.LTGRAY);
        btn_load_shoes = findViewById(R.id.btn_shoes);
        btn_load_shoes.setBackgroundColor(Color.LTGRAY);
        btn_load_accessory = findViewById(R.id.btn_accessory);
        btn_load_accessory.setBackgroundColor(Color.LTGRAY);
        // 옷장의 컬러 필터(리사이클러뷰)를 세팅하는 메소드
        setColorFilter();

//        recycler_view_season = findViewById(R.id.recyclerview_season);
        season_radio_group = (RadioGroup)findViewById(R.id.season_radio_group);

        season_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_all_season:
                        selected_season = "all";
                        getCloth();
                        break;

                    case R.id.radio_spring:
                        selected_season = "봄";
                        getCloth();
                        break;

                    case R.id.radio_summer:
                        selected_season = "여름";
                        getCloth();
                        break;

                    case R.id.radio_fall:
                        selected_season = "가을";
                        getCloth();
                        break;

                    case R.id.radio_winter:
                        selected_season = "겨울";
                        getCloth();
                        break;
                }
            }
        });

        //Add values in array list
        arrayList.addAll(Arrays.asList("봄", "여름", "가을", "겨울"));

        // make vertical adapter for recyclerview


        btn_filter_on = findViewById(R.id.btn_filter_downward);
        btn_filter_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    btn_filter_on.setText("FILTER CLOSE");


                } else if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    btn_filter_on.setText("FILTER");

                }
            }
        });


        slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.drawer);
        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(@NonNull View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            }
        });

        slidingLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        int numberOfColumns =4;
        layoutManager = new GridLayoutManager(this, numberOfColumns);
        recyclerView.setLayoutManager(layoutManager);
        listener = new RecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent intent = new Intent(MyClosetActivity.this, ItemInfo.class);
                intent.putExtra("userID", user_id);
                intent.putExtra("idx", clothList.get(position).getIdx());
                intent.putExtra("main_category", clothList.get(position).getCategory());
                intent.putExtra("picture", clothList.get(position).getPicture());
                intent.putExtra("date", clothList.get(position).getDate());
                intent.putExtra("color", clothList.get(position).getColor());
                intent.putExtra("season", clothList.get(position).getSeason());
                intent.putExtra("type", clothList.get(position).getType());
                intent.putExtra("fit", clothList.get(position).getFit());
                startActivity(intent);
            }
        };

        TextView tv= (TextView) findViewById(R.id.textView);
        tv_closet_empty = (TextView) findViewById(R.id.tv_closet_empty);
        tv_closet_empty.setVisibility(View.GONE);

        // 상의 버튼 클릭 했을 때,
        btn_load_upper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_type = "upper";

                btn_load_upper.setBackgroundColor(Color.BLACK);
                btn_load_upper.setTextColor(Color.WHITE);

                btn_load_bottom.setBackgroundColor(Color.LTGRAY);
                btn_load_bottom.setTextColor(Color.BLACK);
                btn_load_outer.setBackgroundColor(Color.LTGRAY);
                btn_load_outer.setTextColor(Color.BLACK);
                btn_load_shoes.setBackgroundColor(Color.LTGRAY);
                btn_load_shoes.setTextColor(Color.BLACK);
                btn_load_accessory.setBackgroundColor(Color.LTGRAY);
                btn_load_accessory.setTextColor(Color.BLACK);

                getCloth();
            }
        });

        // 하의 버튼 클릭 했을 때,
        btn_load_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_load_bottom.setBackgroundColor(Color.BLACK);
                btn_load_bottom.setTextColor(Color.WHITE);

                btn_load_upper.setBackgroundColor(Color.LTGRAY);
                btn_load_upper.setTextColor(Color.BLACK);
                btn_load_outer.setBackgroundColor(Color.LTGRAY);
                btn_load_outer.setTextColor(Color.BLACK);
                btn_load_shoes.setBackgroundColor(Color.LTGRAY);
                btn_load_shoes.setTextColor(Color.BLACK);
                btn_load_accessory.setBackgroundColor(Color.LTGRAY);
                btn_load_accessory.setTextColor(Color.BLACK);

                load_type = "bottom";
                getCloth();
            }
        });

        // 아우터 버튼 클릭 했을 때,
        btn_load_outer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_type = "outer";

                btn_load_upper.setBackgroundColor(Color.LTGRAY);
                btn_load_upper.setTextColor(Color.BLACK);
                btn_load_bottom.setBackgroundColor(Color.LTGRAY);
                btn_load_bottom.setTextColor(Color.BLACK);
                btn_load_shoes.setBackgroundColor(Color.LTGRAY);
                btn_load_shoes.setTextColor(Color.BLACK);
                btn_load_accessory.setBackgroundColor(Color.LTGRAY);
                btn_load_accessory.setTextColor(Color.BLACK);

                btn_load_outer.setBackgroundColor(Color.BLACK);
                btn_load_outer.setTextColor(Color.WHITE);

                getCloth();
            }
        });

        // 신발 버튼 클릭 했을 때,
        btn_load_shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_load_shoes.setBackgroundColor(Color.BLACK);
                btn_load_shoes.setTextColor(Color.WHITE);
                btn_load_upper.setBackgroundColor(Color.LTGRAY);
                btn_load_upper.setTextColor(Color.BLACK);
                btn_load_outer.setBackgroundColor(Color.LTGRAY);
                btn_load_outer.setTextColor(Color.BLACK);
                btn_load_bottom.setBackgroundColor(Color.LTGRAY);
                btn_load_bottom.setTextColor(Color.BLACK);
                btn_load_accessory.setBackgroundColor(Color.LTGRAY);
                btn_load_accessory.setTextColor(Color.BLACK);
                load_type = "shoes";
                getCloth();
            }
        });

        // 엑세서리 버튼 클릭 했을 때,
        btn_load_accessory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_load_accessory.setBackgroundColor(Color.BLACK);
                btn_load_accessory.setTextColor(Color.WHITE);
                btn_load_upper.setBackgroundColor(Color.LTGRAY);
                btn_load_upper.setTextColor(Color.BLACK);
                btn_load_outer.setBackgroundColor(Color.LTGRAY);
                btn_load_outer.setTextColor(Color.BLACK);
                btn_load_shoes.setBackgroundColor(Color.LTGRAY);
                btn_load_shoes.setTextColor(Color.BLACK);
                btn_load_bottom.setBackgroundColor(Color.LTGRAY);
                btn_load_bottom.setTextColor(Color.BLACK);
                load_type = "accessory";
                getCloth();
            }
        });
    }
    // 컬러 리사이클러뷰 세팅 메소드
    private void setColorFilter() {
        recycler_color = findViewById(R.id.recycler_color);
        // 리사이클러뷰에서 사용할 layout = LinearLayout & Horizontal
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_color.setLayoutManager(layoutManager);

        ArrayList<String> itemList = new ArrayList<>();
        itemList.add("블랙");
        itemList.add("화이트");
        itemList.add("그레이");
        itemList.add("베이지");
        itemList.add("블루");
        itemList.add("레드");

        color_adapter = new ColorAdapter(this, itemList, onClickItem);
        recycler_color.setAdapter(color_adapter);
    }
    // 컬러 Filter 클릭했을 때의 이벤트
    private View.OnClickListener onClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = (String) v.getTag();
            Toast.makeText(getApplicationContext(), str , Toast.LENGTH_SHORT).show();
        }
    };

    // 플러스 버튼 눌렀을 때의 메뉴 생성
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_closet_menu, menu);
    }

    // 툴바 메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_closet_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 툴바 메뉴 눌렀을 때의 코드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.add_cloth:
                // 툴바의 옷 추가 버튼을 눌렀을 때의 이벤트
                // 사용자의 ID를 같이 가져간다.
                Intent intent_get_id = getIntent();
                String userID = intent_get_id.getStringExtra("userID");
                Intent intent = new Intent(getApplicationContext(), ImageEditActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // 서버로부터 옷장 데이터 받아오는 메소드
    public void getCloth() {
        String id = user_id;
        String type = load_type;
        String season = selected_season;
        Call<List<ResponsePOJO>> call = RetrofitClient.getInstance().getApi().getCloth(id, type, season);
        call.enqueue(new Callback<List<ResponsePOJO>>() {
            @Override
            public void onResponse(Call<List<ResponsePOJO>> call, Response<List<ResponsePOJO>> response) {
                clothList = response.body();
                String content = "";
                if(response.body() != null) {
                    Log.d("서버 응답 확인: ", response.body().toString());
                    recyclerView.setVisibility(View.VISIBLE);
                    tv_closet_empty.setVisibility(View.GONE);
                    adapter = new RecyclerAdapter(clothList, MyClosetActivity.this, listener);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else if (response.body() == null) {
                    recyclerView.setVisibility(View.GONE);
                    tv_closet_empty.setVisibility(View.VISIBLE);
                    tv_closet_empty.setText("불러올 데이터가 없습니다.");
                }
            }

            @Override
            public void onFailure(Call<List<ResponsePOJO>> call, Throwable t) {
                Toast.makeText(MyClosetActivity.this, "rp :"+
                                t.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCloth();
    }

    @Override
    public void onBackPressed() {
        // 프로그래스 바 해체
        Intent intent = new Intent(getApplicationContext(), Home.class);
        intent.putExtra("userID", user_id);
        startActivity(intent);//액티비티 띄우기
    }
}