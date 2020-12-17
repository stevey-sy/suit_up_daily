package com.example.suitupdaily;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suitupdaily.recycler.CodiShareAdapter;
import com.example.suitupdaily.recycler.ShowRoomAdapter;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareCodi extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CodiShareAdapter adapter;
    CodiShareAdapter.ShareViewClickListener listener;
    private List<ResponsePOJO> clothList;
    private TextView filter_recently, filter_sex, filter_age;
    private String user_id, codi_idx;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Boolean click_like = false;

    private TextView notify_no_codi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_codi);

        //사용자 아이디를 받는 intent
        Intent intent_id = getIntent();
        user_id = intent_id.getStringExtra("userID");

        // 툴바 설정
        toolbar = findViewById(R.id.toolbar_share_codi);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        //xml 연결
        filter_recently = (TextView)findViewById(R.id.filter_recently);
        filter_sex = (TextView)findViewById(R.id.filter_sex);
        filter_age = (TextView)findViewById(R.id.filter_age);
        notify_no_codi = (TextView)findViewById(R.id.notify_no_codi);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_codi_view);

        // 그리드 뷰의 한 열에 아이템의 갯수
        int numberOfColumns =2;
        // 리사이클러 뷰를 그리드 뷰 형식으로 사용하기 위해 선언
        layoutManager = new GridLayoutManager(this, numberOfColumns);
        recyclerView.setLayoutManager(layoutManager);

        // 리사이클러 아이템 클릭 시, 이벤트
        listener = new CodiShareAdapter.ShareViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "view 클릭." , Toast.LENGTH_SHORT).show();
                // 다음 액티비티에 필요한 데이터를 intent에 담는다.
//                Intent intent = new Intent(ShareCodi.this, CodiDetail.class);
//                intent.putExtra("userID", user_id);
//                intent.putExtra("idx", clothList.get(position).getIdx());
//                intent.putExtra("picture", clothList.get(position).getPicture());
//                intent.putExtra("season", clothList.get(position).getSeason());
//                intent.putExtra("tags", clothList.get(position).getTags());
//                intent.putExtra("memo", clothList.get(position).getMemo());
//                intent.putExtra("date", clothList.get(position).getDate());
//                startActivity(intent);
            }

            @Override
            public void onLikeClick(View view, int position) {
//                Toast.makeText(getApplicationContext(), "좋아요 버튼 클릭." , Toast.LENGTH_SHORT).show();
                if (!click_like) {
                    Toast.makeText(getApplicationContext(), "좋아요 버튼 클릭." , Toast.LENGTH_SHORT).show();
                    click_like = true;
                    // 게시글 번호
                    codi_idx = clothList.get(position).getIdx();
                    // 좋아요 개수 올라가는 서버통신 메소드
                    uploadLike();
                } else {
                    Toast.makeText(getApplicationContext(), "좋아요 취소" , Toast.LENGTH_SHORT).show();
                    click_like = false;
                    // 좋아요 취소해서 개수 내려가는 서버통신 메소드
                }
            }
        };

        // 필터 버튼 클릭 리스너
        filter_recently.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupButtonClick(filter_recently);
            }
        });

    }
    // 필터링 버튼 "최신순" 클릭했을 때의 이벤트 메소드
    public void onPopupButtonClick(View button) {
        //popup menu 객체 생성
        PopupMenu popup = new PopupMenu (this, button);
        // xml 과 연결
        popup.getMenuInflater().inflate(R.menu.menu_filter_recently, popup.getMenu());
        // popup 메뉴 클릭 시 이벤트
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.recently:
                        break;
                    case R.id.popular:
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    // 서버에서 코디 정보 가져오는 메서드
    public void getCodiBoard() {

        String id = user_id;

        Call<List<ResponsePOJO>> call = RetrofitClient.getInstance().getApi().getCodiBoard(id);
        call.enqueue(new Callback<List<ResponsePOJO>>() {
            @Override
            public void onResponse(Call<List<ResponsePOJO>> call, Response<List<ResponsePOJO>> response) {
                clothList = response.body();
                String content = "";

                if(response.body() != null) {
                    Log.d("서버 응답 확인: ", response.body().toString());
                    Toast.makeText(getApplicationContext(), "Test 중입니다. 서버 통신 중" , Toast.LENGTH_SHORT).show();

                    recyclerView.setVisibility(View.VISIBLE);
                    notify_no_codi.setVisibility(View.GONE);

                    adapter = new CodiShareAdapter(clothList, ShareCodi.this, listener);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else if (response.body() == null) {
                    Toast.makeText(getApplicationContext(), "Test 중입니다. 서버 통신 실패" , Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.GONE);
                    notify_no_codi.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ResponsePOJO>> call, Throwable t) {
                Toast.makeText(ShareCodi.this, "rp :"+
                                t.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadLike() {
        String id = user_id;
        String idx = codi_idx;

        //홈페이지에서 더 많은 컨텐츠를 확인하세요,  https://stickode.com/
        Log.d("코디 idx : ", idx);

        Call<ResponsePOJO> call = RetrofitClient.getInstance().getApi().uploadLike(id, idx);
        call.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                Toast.makeText(ShareCodi.this, response.body().getRemarks(), Toast.LENGTH_SHORT).show();
                if(response.body().isStatus()) {

                } else {

                }
            }
            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(ShareCodi.this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });
//        Toast.makeText (this, encodedImage, Toast.LENGTH_SHORT).show();
    }

    public void getLikeInfo (String idx) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        getCodiBoard();
    }
}