package com.example.suitupdaily;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private TextView filter_recently, filter_sex, filter_age, text_searched_word, notify_no_codi;
    private String user_id, codi_idx;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Boolean click_like = false;
    private Button button_search, button_default;
    private EditText edit_text_search_word;

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
        button_search = (Button)findViewById(R.id.btn_search);
        edit_text_search_word = (EditText)findViewById(R.id.et_search_word);
        button_default = (Button)findViewById(R.id.btn_search_default);
        text_searched_word = (TextView)findViewById(R.id.tv_searched_word);

        // 검색결과 안내문 처음에 보이지 않게 처리
        button_default.setVisibility(View.GONE);
        text_searched_word.setVisibility(View.GONE);

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
                    // 클릭된 리사이클러뷰의 게시글 index 를 파악하고, 좋아요를 업로드 한다.
                    codi_idx = clothList.get(position).getIdx();
                    uploadLike();
            }
        };
        // 필터 버튼 클릭 리스너
        filter_recently.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupButtonClick(filter_recently);
            }
        });
        // 해쉬태그 검색 버튼 클릭 리스너
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 검색창에 문자열이 입력되었는지 확인 필요.
                String search_word = edit_text_search_word.getText().toString();
                if(!search_word.equals("")) {
                    // 서버에 해쉬태그 문자열 검색 요청하는 메소드
                    searchHashTag(search_word);
                }
                // 검색창 초기화
                edit_text_search_word.setText("");
            }
        });
        // 초기화 버튼 클릭 리스너
        button_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
//                    Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_SHORT).show();

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
    // 서버에 해쉬태그 검색 요청하는 메소드
    public void searchHashTag (final String search_word) {
        String id = user_id;
        Call<List<ResponsePOJO>> call = RetrofitClient.getInstance().getApi().searchCodi(id, search_word);
        call.enqueue(new Callback<List<ResponsePOJO>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<ResponsePOJO>> call, Response<List<ResponsePOJO>> response) {
                clothList = response.body();
                if(response.body() != null) {
                    Log.d("서버 응답 확인: ", response.body().toString());
                    // 검색결과 안내문 보여주기
                    String result_count = String.valueOf(clothList.size());
                    // 검색어 몇 글자인지 확인
                    int string_count = search_word.length();
                    Log.d("검색어: ", String.valueOf(string_count));
                    // 검색 후 안내문 작성
                    text_searched_word.setText("'"+search_word+"' 총 "+result_count+" 건의 검색 결과");
                    // 검색어 부분만 컬러로 강조
                    Spannable span = (Spannable) text_searched_word.getText();
                    String color_blue = "#1e90ff";
                    span.setSpan(new ForegroundColorSpan(Color.parseColor(color_blue)),1, string_count+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    // 검색 초기화 버튼 활성화
                    button_default.setVisibility(View.VISIBLE);
                    // 서버로부터 받아온 데이터 리사이클러뷰에 적용
                    text_searched_word.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    notify_no_codi.setVisibility(View.GONE);
                    adapter = new CodiShareAdapter(clothList, ShareCodi.this, listener);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else if (response.body() == null) {
                    Toast.makeText(getApplicationContext(), "일치하는 데이터가 없습니다." , Toast.LENGTH_SHORT).show();
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

        Log.d("코디 idx : ", idx);

        Call<ResponsePOJO> call = RetrofitClient.getInstance().getApi().uploadLike(id, idx);
        call.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
//                Toast.makeText(ShareCodi.this, response.body().getRemarks(), Toast.LENGTH_SHORT).show();
                if(response.body().isStatus()) {
                    Toast.makeText(getApplicationContext(), "좋아요 성공" , Toast.LENGTH_SHORT).show();
                } else {
                }
            }
            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
//                Toast.makeText(ShareCodi.this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });
//        Toast.makeText (this, encodedImage, Toast.LENGTH_SHORT).show();
    }

    private void savePreferences(String idx) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("liked", idx);
        editor.commit();
    }

    public void textFormColor( View v, String text, int left, int right ) {
        if ( text == null ) return;
        if ( left < 0 || right < 0 ) return;
        if ( text.length( ) < left + right ) return;
        if ( v.getClass( ) == AppCompatTextView.class || v.getClass( ) == TextView.class ) {
            SpannableString s = new SpannableString( text );
            s.setSpan( new ForegroundColorSpan( Color.BLUE ), 0, left, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
            s.setSpan( new ForegroundColorSpan( Color.RED ), text.length( ) - right, text.length( ), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
            ( ( TextView ) v ).setText( s );

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getCodiBoard();
    }
}