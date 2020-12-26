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
import android.view.inputmethod.InputMethodManager;
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
    private String user_id, codi_idx, string_recently, string_sex, string_age;
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

        // filter 메뉴 초기화
        string_recently = "recently";

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
        // 필터 "최신순" 버튼 클릭 리스너
        filter_recently.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupButtonClick(filter_recently);
            }
        });
        // 필터 "성별" 버튼 클릭 리스너
        filter_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupButtonClick(filter_sex);
            }
        });
        // 필터 "연령" 버튼 클릭 리스너
        filter_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupButtonClick(filter_age);
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
                // 검색 후 가상 키보드 내리기
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit_text_search_word.getWindowToken(), 0);
            }
        });
        // 초기화 버튼 클릭 리스너
        button_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 전체 코디 가져오는 메소드
                getCodiBoard();
                // 검색결과 안내문 보이지 않게 처리
                button_default.setVisibility(View.GONE);
                text_searched_word.setVisibility(View.GONE);
            }
        });
    }
    // 필터링 버튼 클릭했을 때의 메소드
    public void onPopupButtonClick(View button) {
        // 필터링 버튼 "최신순" 클릭했을 때의 메소드
        if (button == filter_recently) {
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
                            string_recently = "recently";
                            // 서버에 필터링 요청 보내는 메소드
                            setFilter();
                            break;
                        case R.id.popular:
                            string_recently = "popular";
                            // 서버에 필터링 요청 보내는 메소드
                            setFilter();
                            break;
                    }
                    return true;
                }
            });
            popup.show();
        }
        // 성별 filter 클릭했을 때 메뉴 생성
        if (button == filter_sex) {
            //popup menu 객체 생성
            PopupMenu popup = new PopupMenu (this, button);
            // xml 과 연결
            popup.getMenuInflater().inflate(R.menu.menu_filter_sex, popup.getMenu());
            // popup 메뉴 클릭 시 이벤트
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.male:
                            string_sex = "남";
                            // 서버에 필터링 요청 보내는 메소드
                            setFilter();
                            break;
                        case R.id.female:
                            string_sex = "여";
                            // 서버에 필터링 요청 보내는 메소드
                            setFilter();
                            break;
                    }
                    return true;
                }
            });
            popup.show();
        }
        // 나이대 filter 클릭했을 때 메뉴 생성
        if (button == filter_age) {
            //popup menu 객체 생성
            PopupMenu popup = new PopupMenu (this, button);
            // xml 과 연결
            popup.getMenuInflater().inflate(R.menu.menu_filter_age, popup.getMenu());
            // popup 메뉴 클릭 시 이벤트
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.teenager:
                            string_age = "10대";
                            // 서버에 필터링 요청 보내는 메소드
                            setFilter();
                            break;
                        case R.id.twenty:
                            string_age = "20대";
                            // 서버에 필터링 요청 보내는 메소드
                            setFilter();
                            break;
                        case R.id.thirty:
                            string_age = "30대";
                            // 서버에 필터링 요청 보내는 메소드
                            setFilter();
                            break;
                    }
                    return true;
                }
            });
            popup.show();
        }
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
    // 서버에 게시글 필터링 요청하는 메소드
    public void setFilter() {
        String recently = string_recently;
        String sex = string_sex;
        String age = string_age;
        Call<List<ResponsePOJO>> call = RetrofitClient.getInstance().getApi().setFilter(recently, sex, age);
        call.enqueue(new Callback<List<ResponsePOJO>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<ResponsePOJO>> call, Response<List<ResponsePOJO>> response) {
                clothList = response.body();
                if(response.body() != null) {
                    // 서버로부터 받아온 데이터 리사이클러뷰에 적용
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
                    // 검색 후 안내문 작성
                    text_searched_word.setText("'"+search_word+"' 총 "+result_count+"건의 검색 결과");
                    int test = text_searched_word.getText().length();
                    // 검색어 부분만 컬러로 강조
                    Spannable span = (Spannable) text_searched_word.getText();
                    String color_blue = "#1e90ff";
                    span.setSpan(new ForegroundColorSpan(Color.parseColor(color_blue)),1, string_count+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    span.setSpan(new ForegroundColorSpan(Color.RED),string_count+5, string_count+6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
    // 좋아요 버튼 눌렸을 때의 메소드
    private void uploadLike() {
        String id = user_id;
        String idx = codi_idx;
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

    @Override
    protected void onResume() {
        super.onResume();
        getCodiBoard();
    }
}