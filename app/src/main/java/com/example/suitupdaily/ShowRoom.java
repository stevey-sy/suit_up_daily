package com.example.suitupdaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suitupdaily.recycler.ShowRoomAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowRoom extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ShowRoomAdapter adapter;
    ShowRoomAdapter.ShowViewClickListener listener;
    private Menu action;
    private TextView tv_no_codi;
    private String user_id, selected_season;
    private List<ResponsePOJO> clothList;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private SlidingUpPanelLayout slidingLayout;
    private RadioGroup radio_group_season;
    private Button btn_drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_room);

        //사용자 아이디를 받는 intent
        Intent intent_id = getIntent();
        user_id = intent_id.getStringExtra("userID");

        // 툴바 설정
        toolbar = findViewById(R.id.toolbar_show_room);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        // xml 연결
        recyclerView = findViewById(R.id.recycler_show_room);
        tv_no_codi = (TextView) findViewById(R.id.tv_no_codi);
        radio_group_season = (RadioGroup) findViewById(R.id.rg_show_room);
        btn_drawer = (Button) findViewById(R.id.btn_sliding_show_room);
        slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.show_room_sliding);

        // 필터 버튼 활성화
        btn_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    btn_drawer.setText("FILTER CLOSE");
                } else if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    btn_drawer.setText("FILTER");
                }
            }
        });

        // 필터: 라디오 그룹 세팅
        radio_group_season.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_all_season:
                        selected_season = null;
                        getCodi();
                        break;
                    case R.id.btn_spring:
                        selected_season = "봄";
                        getCodi();
                        break;
                    case R.id.btn_summer:
                        selected_season = "여름";
                        getCodi();
                        break;
                    case R.id.btn_fall:
                        selected_season = "가을";
                        getCodi();
                        break;
                    case R.id.btn_winter:
                        selected_season = "겨울";
                        getCodi();
                        break;
                }
            }
        });
        // 그리드 뷰의 한 열에 아이템의 갯수
        int numberOfColumns =2;
        layoutManager = new GridLayoutManager(this, numberOfColumns);
        recyclerView.setLayoutManager(layoutManager);

        // 리사이클러 아이템 클릭 시, 이벤트
        listener = new ShowRoomAdapter.ShowViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                // 다음 액티비티에 필요한 데이터를 intent에 담는다.
                Intent intent = new Intent(ShowRoom.this, CodiDetail.class);
                intent.putExtra("userID", user_id);
                intent.putExtra("idx", clothList.get(position).getIdx());
                intent.putExtra("picture", clothList.get(position).getPicture());
                intent.putExtra("season", clothList.get(position).getSeason());
                intent.putExtra("tags", clothList.get(position).getTags());
                intent.putExtra("memo", clothList.get(position).getMemo());
                intent.putExtra("date", clothList.get(position).getDate());
                startActivity(intent);
            }
        };
    }

    // 서버에서 내 코디 정보 가져오는 메서드
    public void getCodi() {
        String id = user_id;
//        String type = load_type;
        String season = selected_season;

        Call<List<ResponsePOJO>> call = RetrofitClient.getInstance().getApi().getCodi(id, season);
        call.enqueue(new Callback<List<ResponsePOJO>>() {
            @Override
            public void onResponse(Call<List<ResponsePOJO>> call, Response<List<ResponsePOJO>> response) {
                clothList = response.body();

                if(response.body() != null) {
                    Log.d("서버 응답 확인: ", response.body().toString());

                    recyclerView.setVisibility(View.VISIBLE);
                    tv_no_codi.setVisibility(View.GONE);

                    adapter = new ShowRoomAdapter(clothList, ShowRoom.this, listener);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else if (response.body() == null) {
                    recyclerView.setVisibility(View.GONE);
                    tv_no_codi.setVisibility(View.VISIBLE);
                    tv_no_codi.setText("불러올 데이터가 없습니다.");
                }
            }

            @Override
            public void onFailure(Call<List<ResponsePOJO>> call, Throwable t) {
                Toast.makeText(ShowRoom.this, "rp :"+
                                t.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCodi();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), Home.class);
        intent.putExtra("userID", user_id);
        startActivity(intent);//액티비티 띄우기
    }

    // 툴바 메뉴 불러오기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_show_room, menu);
        action = menu;
        return true;
    }

    // 툴바 메뉴 클릭 시 이벤트 (코디 추가)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_codi:
                // 다음 액티비티로 데이터 넘기기
                // 다음 액티비티에 필요한 데이터를 intent에 담는다.
                Intent intent = new Intent(ShowRoom.this, SelfCodi.class);
                intent.putExtra("userID", user_id);
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}