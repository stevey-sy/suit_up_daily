package com.example.suitupdaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodiDetail extends AppCompatActivity {

    private String user_id, picture, tags, memo, idx, season, date;
    private ImageView img_codi;
    private TextView text_view_codi_season, text_view_codi_tags, text_view_codi_memo, text_view_date;
    private Button button_remove_tag, button_input_tag;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Menu action;
    private Api apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codi_detail);

        // 툴바 설정
        toolbar = findViewById(R.id.toolbar_codi_detail);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        // xml 요소 연결
        img_codi = (ImageView) findViewById(R.id.image_view_codi_detail);
        text_view_codi_tags = (TextView) findViewById(R.id.text_view_tag_list);
        text_view_codi_memo = (TextView) findViewById(R.id.text_view_memo_detail);
        text_view_date = (TextView) findViewById(R.id.text_view_date_detail);

        // ShowRoom 클래스에서 받은 intent 데이터
        Intent getCodiInfo = getIntent();
        user_id = getCodiInfo.getStringExtra("userID");
        picture = getCodiInfo.getStringExtra("picture");
        season = getCodiInfo.getStringExtra("season");
        tags = getCodiInfo.getStringExtra("tags");
        memo = getCodiInfo.getStringExtra("memo");
        date = getCodiInfo.getStringExtra("date");
        idx = getCodiInfo.getStringExtra("idx");

        // 받아온 intent 데이터를 view 에 뿌려주는 메소드
        setDataFromIntentExtra();
    }

    // 받아온 intent 데이터를 view 에 뿌려주는 메소드
    @SuppressLint("CheckResult")
    private void setDataFromIntentExtra() {

        text_view_codi_tags.setText(tags);
        text_view_codi_memo.setText(memo);
        text_view_date.setText("Date: " + date);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.ic_hanger);
        requestOptions.error(R.drawable.ic_baseline_accessibility_24);

        Glide.with(CodiDetail.this)
                .load(picture)
                .apply(requestOptions)
                .into(img_codi);
    }

    // 툴바 메뉴 불러오기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.codi_detail_menu, menu);
        action = menu;
        return true;
    }

    // 툴바 메뉴 클릭 시 이벤트 (수정, 삭제)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modify:
                // 다음 액티비티로 데이터 넘기기
            case R.id.delete:
                AlertDialog.Builder dialog = new AlertDialog.Builder(CodiDetail.this);
                dialog.setMessage("코디를 삭제하시겠습니까?");
                dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteData(idx, picture);
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
        return super.onOptionsItemSelected(item);
    }

    // 코디 삭제 버튼 눌릴 때 사용될 메서드
    private void deleteData(final String key, final String pic) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("삭제중입니다...");
        progressDialog.show();

        apiInterface = RetrofitClient.getApiClient().create(Api.class);
        Call<ResponsePOJO> call = apiInterface.deleteCodi(key, pic);
        call.enqueue(new Callback<ResponsePOJO>() {

            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                progressDialog.dismiss();

                String result = response.body().getResult();
                String message = response.body().getMessage();

                if (result.equals("1")) {
                    Toast.makeText(CodiDetail.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CodiDetail.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CodiDetail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}