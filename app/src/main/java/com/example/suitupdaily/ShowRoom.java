package com.example.suitupdaily;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.suitupdaily.recycler.ShowRoomAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowRoom extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ShowRoomAdapter adapter;
    ShowRoomAdapter.ShowViewClickListener listener;

    private TextView tv_no_codi;

    private String user_id;

    private List<ResponsePOJO> clothList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_room);

        Intent intent_id = getIntent();
                user_id = intent_id.getStringExtra("userID");


        recyclerView = findViewById(R.id.recycler_show_room);
        tv_no_codi = (TextView) findViewById(R.id.tv_no_codi);

        // 그리드 뷰의 한 열에 아이템의 갯수
        int numberOfColumns =2;
        layoutManager = new GridLayoutManager(this, numberOfColumns);
        recyclerView.setLayoutManager(layoutManager);

        // 리사이클러 아이템 클릭 시, 이벤트
        listener = new ShowRoomAdapter.ShowViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {

                Toast.makeText(getApplicationContext(), "Test 중입니다." , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ShowRoom.this, CodiDetail.class);
                intent.putExtra("userID", user_id);
                intent.putExtra("idx", clothList.get(position).getIdx());
                intent.putExtra("picture", clothList.get(position).getPicture());
                intent.putExtra("season", clothList.get(position).getSeason());
                intent.putExtra("tags", clothList.get(position).getTags());
                intent.putExtra("memo", clothList.get(position).getMemo());

                startActivity(intent);

            }
        };
    }

    public void getCodi() {

        String id = user_id;
//        String type = load_type;
//        String season = selected_season;

        Call<List<ResponsePOJO>> call = RetrofitClient.getInstance().getApi().getCodi(id);
        call.enqueue(new Callback<List<ResponsePOJO>>() {
            @Override
            public void onResponse(Call<List<ResponsePOJO>> call, Response<List<ResponsePOJO>> response) {
                clothList = response.body();

                String content = "";

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

}