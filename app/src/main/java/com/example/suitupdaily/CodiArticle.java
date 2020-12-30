package com.example.suitupdaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.suitupdaily.recycler.CodiShareAdapter;
import com.example.suitupdaily.recycler.CommentAdapter;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodiArticle extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private String user_id, date, like_num, view_num, memo, hash_tags, profile_url, writer_nick, codi_url, who_liked, codi_idx, reply_content, comment_count;
    private TextView text_writer_name, text_date, text_view_num, text_like_num, text_reply_num, text_hash_tags, text_memo, title_like;
    private ImageView img_codi;
    private CircleImageView writer_pic, reply_profile_pic;
    private CheckBox check_like;
    private EditText edit_text_reply;
    private Button button_comment;
    private List<ResponsePOJO> commentList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CommentAdapter adapter;
    private CommentAdapter.CommentViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codi_article);
        // 게시글의 데이터를 받아오는 intent
        Intent intent_id = getIntent();
        user_id = intent_id.getStringExtra("userID");
        writer_nick = intent_id.getStringExtra("nick");
        date = intent_id.getStringExtra("date");
        hash_tags = intent_id.getStringExtra("tags");
        memo = intent_id.getStringExtra("memo");
        codi_url = intent_id.getStringExtra("picture");
        profile_url = intent_id.getStringExtra("profile_pic");
        who_liked = intent_id.getStringExtra("who_liked");
        like_num = intent_id.getStringExtra("like_num");
        codi_idx = intent_id.getStringExtra("idx");
        view_num = intent_id.getStringExtra("view");
        comment_count = intent_id.getStringExtra("comment_count");

        // 툴바 세팅
        toolbar = findViewById(R.id.toolbar_article);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        //xml 연결
        text_writer_name = (TextView) findViewById(R.id.tv_article_writer);
        text_date = (TextView) findViewById(R.id.tv_article_date);
        text_view_num = (TextView) findViewById(R.id.tv_article_views);
        text_like_num = (TextView) findViewById(R.id.tv_article_like);
        text_memo = (TextView) findViewById(R.id.tv_article_memo);
        text_hash_tags = (TextView) findViewById(R.id.tv_article_hash);
        text_reply_num = (TextView) findViewById(R.id.tv_article_reply_num);
        writer_pic = (CircleImageView)findViewById(R.id.civ_article_profile);
        img_codi = (ImageView)findViewById(R.id.iv_article_codi);
        check_like = (CheckBox)findViewById(R.id.cb_article_like);
        title_like = (TextView) findViewById(R.id.title_article_like);
        edit_text_reply = (EditText) findViewById(R.id.et_article_input_reply);
        reply_profile_pic = (CircleImageView)findViewById(R.id.civ_reply_profile_pic);
        button_comment = (Button)findViewById(R.id.btn_comment);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_reply);
        // 댓글 리사이클러뷰 세팅
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // intent 로 부터 받아온 데이터를 view 에 뿌려준다.
        setDataFromIntent();
        // 서버에 게시글 조회수++ & 사용자의 프로필 불러오는 메소드
        uploadView();
        // 좋아요 버튼 클릭 리스너 생성
        check_like.setOnCheckedChangeListener(this);
        // 가상 키보드가 editText 를 가리는 현상을 막아주는 코드
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 댓글 입력 버튼 클릭 이벤트
        button_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 만약 댓글 창이 빈값이 아니라면
                reply_content = edit_text_reply.getText().toString();
                if (!reply_content.isEmpty()) {
                    // 서버에 댓글 저장 요청하는 메소드
                    uploadComment();
                    // 댓글 창 초기화
                    edit_text_reply.setText("");
                    // 입력 후 가상 키보드 없애기
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_text_reply.getWindowToken(), 0);
                }
            }
        });
        // 리사이클러뷰, 댓글 창에서 메뉴 클릭 시, 이벤트
        listener = new CommentAdapter.CommentViewClickListener() {
            @Override
            public void onRowClick(View view, final int position) {
                // TODO: 2020-12-29 popup 메뉴로 수정, 삭제 버튼 나오게 하기
                // 수정 or 삭제할 댓글의 index 번호를 확정한다.
                final String comment_idx = String.valueOf(commentList.get(position).getCommentIdx());
                //popup menu 객체 생성
                PopupMenu popup = new PopupMenu (CodiArticle.this, view);
                // xml 과 연결 (수정, 삭제 xml)
                popup.getMenuInflater().inflate(R.menu.comment_menu, popup.getMenu());
                // popup 메뉴 클릭 시 이벤트
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.modify:
                                // 수정 이벤트
                                // 수정 dialog 를 생성
                                final EditText edit_text = new EditText(CodiArticle.this);
                                AlertDialog.Builder builder = new AlertDialog.Builder(CodiArticle.this);
                                builder.setTitle("댓글 수정");
                                builder.setMessage("댓글을 수정해주세요.");
                                builder.setView(edit_text);
                                edit_text.setText(commentList.get(position).getContent());
                                builder.setPositiveButton("수정",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // 서버에 데이터 수정 요청
                                                // 댓글의 index 와 수정한 댓글 내용을 매개변수로 사용.
                                                String content = edit_text.getText().toString();
                                                editComment(comment_idx, content);
                                            }
                                        });
                                builder.setNegativeButton("취소",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.d("댓글 수정 ", "수정 취소버튼 눌림");
                                            }
                                        });
                                builder.show();

                                return true;
                            case R.id.delete:
                                // 삭제 이벤트
                                // 정말 삭제할 것인지 물어보는 dialog 생성
                                AlertDialog.Builder dialog = new AlertDialog.Builder(CodiArticle.this);
                                dialog.setMessage("댓글을 삭제하시겠습니까?");
                                dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        Toast.makeText(getApplicationContext(), String.valueOf(commentList.get(position).getCommentIdx()), Toast.LENGTH_SHORT).show();
                                        // 확인후 서버에 댓글 삭제 요청
                                        Log.d("삭제될 codi_idx: ", codi_idx);
                                        deleteComment(comment_idx, codi_idx);
                                        dialog.dismiss();
                                    }
                                });
                                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                                return true;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        };
    }

    // 서버에 댓글 수정 요청하는 메소드
    private void editComment(String comment_idx, String content) {
        String id = user_id;
        Call<ResponsePOJO> call = RetrofitClient.getInstance().getApi().editComment(id, comment_idx, content);
        call.enqueue(new Callback<ResponsePOJO>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                Log.d("서버 응답: ", "성공");
                String remarks = response.body().getRemarks();
                Toast.makeText(getApplicationContext(), remarks, Toast.LENGTH_SHORT).show();
                // 서버 응답 성공하면 댓글 담당하는 recycler view 를 reload 한다.
                getComment();
            }
            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(CodiArticle.this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 서버에 댓글 삭제 요청하는 메소드
    private void deleteComment(String comment_idx, String codi_idx) {
        String id = user_id;
        Call<ResponsePOJO> call = RetrofitClient.getInstance().getApi().deleteComment(id, comment_idx, codi_idx);
        call.enqueue(new Callback<ResponsePOJO>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                Log.d("서버 응답: ", "성공");
                String remarks = response.body().getRemarks();
                Toast.makeText(getApplicationContext(), remarks, Toast.LENGTH_SHORT).show();
                // 서버 응답 성공하면 댓글 담당하는 recycler view 를 reload 한다.
                getComment();
                // 댓글 표시란에 -1을 한다.
                String comment_num = text_reply_num.getText().toString();
                int num = Integer.parseInt(comment_num)-1;
                text_reply_num.setText(String.valueOf(num));
            }
            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(CodiArticle.this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 서버로부터 댓글 리스트 가져오는 메소드
    public void getComment() {
        String idx = codi_idx;
        Call<List<ResponsePOJO>> call = RetrofitClient.getInstance().getApi().getComment(idx);
        call.enqueue(new Callback<List<ResponsePOJO>>() {
            @Override
            public void onResponse(Call<List<ResponsePOJO>> call, Response<List<ResponsePOJO>> response) {
                commentList = response.body();
                if(response.body() != null) {
                    Log.d("getComment 서버 응답 확인: ", "성공");
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter = new CommentAdapter(commentList, CodiArticle.this, listener);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else if (response.body() == null) {
                    recyclerView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<List<ResponsePOJO>> call, Throwable t) {
                Toast.makeText(CodiArticle.this, "rp :"+
                                t.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 서버에 댓글 저장 요청하는 메소드
    private void uploadComment() {
        String id = user_id;
        String content = reply_content;
        // 게시글 번호
        String idx = codi_idx;
        Call<ResponsePOJO> call = RetrofitClient.getInstance().getApi().uploadComment(id, idx, content);
        call.enqueue(new Callback<ResponsePOJO>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                Log.d("서버 응답: ", "성공");
                String remarks = response.body().getRemarks();
                Toast.makeText(getApplicationContext(), remarks, Toast.LENGTH_SHORT).show();
                // 서버 응답 성공하면 댓글 담당하는 recycler view 를 reload 한다.
                getComment();
                // 댓글 표시란에 +1을 한다.
                String comment_num = text_reply_num.getText().toString();
                int num = Integer.parseInt(comment_num)+1;
                text_reply_num.setText(String.valueOf(num));

            }
            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(CodiArticle.this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 인텐트로 받아온 데이터를 뷰에 뿌려주는 메소드
    @SuppressLint("CheckResult")
    private void setDataFromIntent() {
        text_writer_name.setText(writer_nick);
        text_memo.setText(memo);
        text_hash_tags.setText(hash_tags);
        // 게시글 등록일 데이터 시간부분 자르기
        // ex) 2020-12-10 00:00:00 --------> 2020-12-10
        String short_date = date.substring(0,10);
        text_date.setText(short_date);
        // 조회수 데이터 view 에 삽입
        text_view_num.setText(view_num);
        // 댓글 개수 데이터 삽입
        text_reply_num.setText(comment_count);
        // 이미지 데이터에 문제생겼을 경우 표시될 대체 이미지.
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.ic_clothes_hanger);
        requestOptions.error(R.drawable.ic_baseline_accessibility_24);
        // 프로필 이미지 세팅
        Glide.with(this)
                .load(profile_url)
                .apply(requestOptions)
                .circleCrop()
                .into(writer_pic);
        // 코디 이미지 세팅
        Glide.with(this)
                .load(codi_url)
                .apply(requestOptions)
                .into(img_codi);
        // 좋아요 개수 가져오기 && 사용자가 좋아요 눌렀던 글인지 확인하기
        if(who_liked.contains(user_id)) {
            // 리스트에 이미 id가 존재한다면 좋아요 누른처리
//            int like = Integer.parseInt(like_num);
//            int like_edit = like -1;
//            text_like_num.setText(String.valueOf(like_edit));
            text_like_num.setText(like_num);
            check_like.setChecked(true);
            String color_orange = "#FF8C00";
            title_like.setTextColor(Color.parseColor(color_orange));
        } else {
            // 리스트에 id가 존재하지 않는다면 좋아요 버튼 default 처리
            check_like.setChecked(false);
            text_like_num.setText(like_num);
            String color_black = "#000000";
            title_like.setTextColor(Color.parseColor(color_black));
        }
    }

    // 툴바 메뉴 클릭시 이벤트 (홈 버튼)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // 좋아요 체크박스 클릭 이벤트트
   @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // 좋아요 버튼 눌렸을 때
        if (isChecked) {
            String like_string = text_like_num.getText().toString();
            int like_int = Integer.parseInt(like_string);
            like_int += 1;
            text_like_num.setText(String.valueOf(like_int));
            String color_orange = "#FF8C00";
            title_like.setTextColor(Color.parseColor(color_orange));
        } else {
            // 좋아요 버튼 한번 더 눌렀을 때
            String like_string = text_like_num.getText().toString();
            int like_int = Integer.parseInt(like_string);
            like_int -= 1;
            text_like_num.setText(String.valueOf(like_int));
            String color_black = "#000000";
            title_like.setTextColor(Color.parseColor(color_black));
        }
    }
    // 조회수 count 하는 서버통신 메소드.
    private void uploadView() {
        String id = user_id;
        String idx = codi_idx;
        Call<ResponsePOJO> call = RetrofitClient.getInstance().getApi().uploadView(id, idx);
        call.enqueue(new Callback<ResponsePOJO>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                Log.d("조회수 count: ", "성공");
                String photo_url = response.body().getPhotoUrl();
                // photo url이 존재한다면 댓글창 프로필 image view 에 이미지를 출력
                if(photo_url != null) {
                    // 이미지 데이터에 문제생겼을 경우 표시될 대체 이미지.
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.skipMemoryCache(true);
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                    requestOptions.placeholder(R.drawable.ic_clothes_hanger);
                    requestOptions.error(R.drawable.ic_baseline_accessibility_24);
                    // 서버에서 받아온 데이터를 view 에 뿌려줌
                    Glide.with(CodiArticle.this).load(photo_url).apply(requestOptions).circleCrop().into(reply_profile_pic);
                }
            }
            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
//                Toast.makeText(ShareCodi.this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getComment();
    }
}