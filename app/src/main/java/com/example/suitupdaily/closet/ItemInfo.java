package com.example.suitupdaily.closet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.suitupdaily.api.Api;
import com.example.suitupdaily.dialog.FitCategoryDialog;
import com.example.suitupdaily.R;
import com.example.suitupdaily.api.ResponsePOJO;
import com.example.suitupdaily.api.RetrofitClient;
import com.example.suitupdaily.styling.SelfCodi;
import com.example.suitupdaily.dialog.SeasonMultipleChoice;
import com.example.suitupdaily.recycler.ColorGridAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ImageView img_cloth;
    private EditText main_category, text_modify_color,text_modify_date, text_modify_season, text_idx, modify_type, modify_fit;
    private String user_id ,category, picture, idx, date, season, color, fit, cloth_type;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private FloatingActionButton btn_change_pic, button_codi;
    private Menu action;
    private Bitmap bitmap;
    private Api apiInterface;
    private TextView title_category, title_color, title_buy_date, title_season, title_fit;

    // 컬러 수정 다이얼로그
    private Dialog dialog;
    private GridView gridView;
    String[] color_name = {"블랙", "화이트", "아이보리", "베이지", "그레이"};
    String[] color_code = {"#000000","#FFFFFF", "#f5f5dc", "#ece6cc", "#d3d3d3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        Intent intent_id = getIntent();
        user_id = intent_id.getStringExtra("userID");
        cloth_type = intent_id.getStringExtra("type");

        toolbar = findViewById(R.id.toolbar_cloth_info);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        title_category = (TextView)findViewById(R.id.title_category);
        title_color = (TextView)findViewById(R.id.title_color);
        title_fit = (TextView)findViewById(R.id.title_fit);
        title_buy_date = (TextView)findViewById(R.id.title_buy_date);
        title_season = (TextView)findViewById(R.id.title_season);

        img_cloth = (ImageView)findViewById(R.id.image_cloth);
        main_category = (EditText)findViewById(R.id.text_main_category);
        btn_change_pic = findViewById(R.id.btn_change_pic);
        button_codi = findViewById(R.id.button_do_codi);
        text_modify_color = (EditText)findViewById(R.id.text_modify_color);
        text_modify_date = (EditText)findViewById(R.id.text_modify_date);
        text_modify_season = (EditText)findViewById(R.id.text_modify_season);
        text_idx = (EditText)findViewById(R.id.text_idx);
        modify_type = (EditText)findViewById(R.id.modify_type);
        modify_fit = (EditText)findViewById(R.id.tv_fit_info);

        modify_type.setVisibility(View.GONE);
        text_idx.setVisibility(View.GONE);

        Intent intent = getIntent();
        idx = intent.getStringExtra("idx");
        category = intent.getStringExtra("main_category");
        picture = intent.getStringExtra("picture");
        color = intent.getStringExtra("color");
        date = intent.getStringExtra("date");
        season = intent.getStringExtra("season");
        fit = intent.getStringExtra("fit");

        setDataFromIntentExtra();

        btn_change_pic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
        // 코디 버튼 눌렀을 때의 이벤트
        button_codi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) img_cloth.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
                byte[] byteArray = stream.toByteArray();
                // 이미지 뷰에 담긴 이미지를 bitmap -> byte array 로 변환
                // 액티비티 넘긴다.
                // 특정 intent 받으면, canvas로 자동 출력하기
                // 다음 액티비티에 필요한 데이터를 intent에 담는다.
                Intent intent = new Intent(ItemInfo.this, SelfCodi.class);
                intent.putExtra("code", 800);
                intent.putExtra("userID", user_id);
                intent.putExtra("image_cloth", byteArray);
                startActivity(intent);
            }
        });
    }

    // 툴바 메뉴 불러오기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_cloth_info, menu);

        action = menu;
        action.findItem(R.id.confirm).setVisible(false);
        action.findItem(R.id.modify_cancel).setVisible(false);

        return true;
    }
    // 툴바 메뉴 클릭 이벤트 설정 (수정, 삭제, 확인 버튼)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.modify:
                editMode();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(main_category, InputMethodManager.SHOW_IMPLICIT);

                action.findItem(R.id.modify).setVisible(false);
                action.findItem(R.id.delete).setVisible(false);
                action.findItem(R.id.modify_cancel).setVisible(true);
                action.findItem(R.id.confirm).setVisible(true);

                Toast.makeText(getApplicationContext(), "데이터를 수정할 수 있습니다.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete:

                AlertDialog.Builder dialog = new AlertDialog.Builder(ItemInfo.this);
                dialog.setMessage("옷 데이터를 삭제하시겠습니까?");
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

                Toast.makeText(getApplicationContext(), "삭제버튼", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.confirm:

                postData(idx);
                action.findItem(R.id.modify).setVisible(true);
                action.findItem(R.id.delete).setVisible(true);
                action.findItem(R.id.confirm).setVisible(false);
                action.findItem(R.id.modify_cancel).setVisible(false);

                main_category.setFocusableInTouchMode(false);
                text_modify_color.setFocusableInTouchMode(false);
                text_modify_date.setFocusableInTouchMode(false);
                text_modify_season.setFocusableInTouchMode(false);
                modify_fit.setFocusableInTouchMode(false);

                readMode();

                return true;

            case R.id.modify_cancel:

                action.findItem(R.id.modify).setVisible(true);
                action.findItem(R.id.delete).setVisible(true);
                action.findItem(R.id.confirm).setVisible(false);
                action.findItem(R.id.modify_cancel).setVisible(false);

                main_category.setFocusableInTouchMode(false);
                text_modify_color.setFocusableInTouchMode(false);
                text_modify_date.setFocusableInTouchMode(false);
                text_modify_season.setFocusableInTouchMode(false);
                modify_fit.setFocusableInTouchMode(false);

                readMode();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("CheckResult")
    private void setDataFromIntentExtra() {

        readMode();
        main_category.setText(category);
        text_modify_color.setText(color);
        text_modify_date.setText(date);
        text_modify_season.setText(season);
        modify_fit.setText(fit);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.ic_hanger);
        requestOptions.error(R.drawable.ic_baseline_accessibility_24);

        Glide.with(ItemInfo.this)
                .load(picture)
                .apply(requestOptions)
                .into(img_cloth);
    }

    // 사용자의 조회 모드
    // 텍스트뷰를 선택해도 정보를 수정할 수 없다.
    private void readMode() {

        title_category.setTextColor(Color.parseColor("#696969"));
        title_color.setTextColor(Color.parseColor("#696969"));
        title_fit.setTextColor(Color.parseColor("#696969"));
        title_buy_date.setTextColor(Color.parseColor("#696969"));
        title_season.setTextColor(Color.parseColor("#696969"));

        main_category.setTextColor(Color.parseColor("#000000"));
        text_modify_color.setTextColor(Color.parseColor("#000000"));
        text_modify_date.setTextColor(Color.parseColor("#000000"));
        modify_fit.setTextColor(Color.parseColor("#000000"));
        text_modify_season.setTextColor(Color.parseColor("#000000"));

        main_category.setFocusableInTouchMode(false);
        text_modify_color.setFocusableInTouchMode(false);
        text_modify_date.setFocusableInTouchMode(false);
        text_modify_season.setFocusableInTouchMode(false);
        modify_fit.setFocusableInTouchMode(false);

        btn_change_pic.setVisibility(View.INVISIBLE);
    }

    // 사용자의 옷 데이터 수정 모드
    private void editMode() {
        // 사용자가 수정 버튼을 누르면 수정가능한 항목들의 텍스트 컬러 변경 됨.
        // 텍스트 뷰 클릭시 데이터를 수정할 수 있음.
        // 옷 카테고리 선택 버튼 눌렀을 때의 이벤트

        // 텍스트뷰 컬러
        title_category.setTextColor(Color.parseColor("#008B8B"));
        title_color.setTextColor(Color.parseColor("#008B8B"));
        title_fit.setTextColor(Color.parseColor("#008B8B"));
        title_buy_date.setTextColor(Color.parseColor("#008B8B"));
        title_season.setTextColor(Color.parseColor("#008B8B"));

        main_category.setTextColor(Color.parseColor("#808080"));
        text_modify_color.setTextColor(Color.parseColor("#808080"));
        text_modify_date.setTextColor(Color.parseColor("#808080"));
        modify_fit.setTextColor(Color.parseColor("#808080"));
        text_modify_season.setTextColor(Color.parseColor("#808080"));

        main_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClothCategoryDialog clothCategoryDialog = new ClothCategoryDialog(ItemInfo.this);
                clothCategoryDialog.callFunction(modify_type, main_category);
            }
        });

        modify_fit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FitCategoryDialog fitCategoryDialog = new FitCategoryDialog(ItemInfo.this);
                fitCategoryDialog.callFunction(modify_fit);
            }
        });

        // 구매일 수정버튼 눌렀을 때의 이벤트
        text_modify_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // 시즌 버튼 눌렀을 때의 이벤트
        text_modify_season.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeasonMultipleChoice seasonMultipleChoice = new SeasonMultipleChoice(ItemInfo.this);
                seasonMultipleChoice.callFunction(text_modify_season);
//                DialogSeason dialogSeason = new DialogSeason(ItemInfo.this);
//                dialogSeason.callFunction(text_modify_season);
            }
        });

        // 컬러 버튼 눌렀을 때의 이벤트
        text_modify_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorDialog();

            }
        });

        btn_change_pic.setVisibility(View.VISIBLE);
    }

    // 컬러 수정 다이어로그
    public void openColorDialog() {
        dialog = new Dialog(ItemInfo.this);
        dialog.setContentView(R.layout.dialog_color_list);
//        dialog.findViewById(R.id.color_grid_view);
        gridView = dialog.findViewById(R.id.color_grid_view);
        dialog.setCancelable(true);

//        ArrayAdapter<String>adapter = new ArrayAdapter<>(ImageEditActivity.this, R.layout.color_grid_item, R.id.text_color_name, color_name);
        ColorGridAdapter adapter = new ColorGridAdapter(ItemInfo.this, color_name, color_code);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "position : " + position, Toast.LENGTH_SHORT).show();
                text_modify_color.setText(color_name[position]);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showDatePickerDialog() {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
            this, this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();

            }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            month += 1;
            String date = year + "/" + month + "/" + dayOfMonth;
            text_modify_date.setText(date);
            }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 이미지 선택되었을 때 이미지 뷰에 표시되는 코드
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                Uri uri = null;
                if (data != null) {
                    try {
                        uri = data.getData();
                        //img_cloth.setImageURI(uri);
                        ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), uri);
                        bitmap = ImageDecoder.decodeBitmap(source);
                        img_cloth.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void deleteData(final String key, final String pic) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("삭제중입니다...");
        progressDialog.show();

        readMode();

        apiInterface = RetrofitClient.getApiClient().create(Api.class);

        Call<ResponsePOJO> call = apiInterface.delete(key, pic);

        call.enqueue(new Callback<ResponsePOJO> () {

            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                progressDialog.dismiss();

                String result = response.body().getResult();
                String message = response.body().getMessage();

                if (result.equals("1")) {
                    Toast.makeText(ItemInfo.this, message, Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(ItemInfo.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ItemInfo.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void postData(final String key) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("수정 중...");
        progressDialog.show();

        String category = main_category.getText().toString().trim();
        String picture = null;

        if (bitmap == null) {
            picture = "111";
        } else {
            picture = getStringImage(bitmap);
        }

        Log.d("String picture =  ", picture);
        Log.d("String picture =  ", picture);
        Log.d("String picture =  ", picture);
        Log.d("String picture =  ", picture);
        Log.d("String picture =  ", picture);
        Log.d("String picture =  ", picture);

        String color = text_modify_color.getText().toString().trim();
        String date = text_modify_date.getText().toString().trim();
        String season = text_modify_season.getText().toString().trim();
        String type = cloth_type;
        String fit = modify_fit.getText().toString().trim();

        apiInterface = RetrofitClient.getApiClient().create(Api.class);

        Call<ResponsePOJO> call = apiInterface.modify(key, user_id, picture, category, color, date, season, type, fit);
        call.enqueue(new Callback<ResponsePOJO>() {

            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                progressDialog.dismiss();

                String result = response.body().getResult();
                String message = response.body().getMessage();

                if (result.equals("1")) {
                    Toast.makeText(ItemInfo.this, message, Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(ItemInfo.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ItemInfo.this, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("서버연결 실패: ", Objects.requireNonNull(t.getMessage()));
            }
        });

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}