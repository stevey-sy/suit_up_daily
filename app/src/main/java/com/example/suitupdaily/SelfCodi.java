package com.example.suitupdaily;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.suitupdaily.dialog.DialogCodi;
import com.example.suitupdaily.dialog.DialogColorGuide;
import com.example.suitupdaily.recycler.RecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelfCodi extends AppCompatActivity implements View.OnClickListener {

    private Paint server_image;

    static Bitmap bitmap_picture;
    //이미지가 있나 없나를 공유하는 전역변수
    boolean imgFlag = false;
    // 이미지를 불러올 뷰
    private MyGraphicView mg;

    // 툴바 관련 변수 선언
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Menu action;

    // 서버에 저장될 변수
    private Bitmap bitmap;

    private Button btn_load, btn_my_drawer, btn_save_codi;
    private SlidingUpPanelLayout slidingLayout;

    private String user_id = null;
    private RadioGroup season_radio_group;
    private String selected_season = "all";

    private List<ResponsePOJO> clothList;
    private List<ResponsePOJO> bottomList;
    private List<ResponsePOJO> outerList;

    private RecyclerView recyclerView;
    private RecyclerView bottom_recycler;
    private RecyclerView outer_recycler;

    private TextView tv_no_upper, tv_no_bottom, tv_no_outer;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    RecyclerAdapter.RecyclerViewClickListener listener;
    RecyclerAdapter.RecyclerViewClickListener listener_bottom;
    RecyclerAdapter.RecyclerViewClickListener listener_outer;

    private Boolean check_show;

    private Button btn_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_codi);

        Intent intent_id = getIntent();
        user_id = intent_id.getStringExtra("userID");

        SharedPreferences sf = getSharedPreferences("codi",MODE_PRIVATE);
        check_show = sf.getBoolean("codi_checker", false);

        check_show = sf.getBoolean("checker", false);

        if (!check_show) {
            // false 면은
            DialogCodi dialogCodi = new DialogCodi(SelfCodi.this);
            dialogCodi.callFunction();
        }
        SharedPreferences.Editor shared_editor = sf.edit();
        shared_editor.putBoolean("checker", true);
        shared_editor.commit();


        // 툴바 연결
        toolbar = findViewById(R.id.toolbar_add_codi);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        mg = (MyGraphicView) findViewById(R.id.my_graphic_view);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(this);

        btn_save_codi = (Button) findViewById(R.id.btn_save_codi);
        btn_save_codi.setOnClickListener(this);

        btn_my_drawer = (Button) findViewById(R.id.btn_my_drawer);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_upper);
        bottom_recycler = (RecyclerView) findViewById(R.id.recycler_bottom);
        outer_recycler = (RecyclerView) findViewById(R.id.recycler_outer);

        tv_no_upper = (TextView) findViewById(R.id.tv_no_upper);
        tv_no_upper.setVisibility(View.GONE);

        tv_no_bottom = (TextView) findViewById(R.id.tv_no_bottom);
        tv_no_bottom.setVisibility(View.GONE);

        tv_no_outer = (TextView) findViewById(R.id.tv_no_outer);
        tv_no_outer.setVisibility(View.GONE);

        season_radio_group = (RadioGroup) findViewById(R.id.codi_season_rg);

        // 계절 필터 버튼 눌렸을 때의 이벤트
        season_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_all_season_codi:
                        selected_season = "all";
                        getCloth();
                        getBottom();
                        getOuter();
                        break;

                    case R.id.radio_spring_codi:
                        selected_season = "봄";
                        getCloth();
                        getBottom();
                        getOuter();
                        break;

                    case R.id.radio_summer_codi:
                        selected_season = "여름";
                        getCloth();
                        getBottom();
                        getOuter();
                        break;

                    case R.id.radio_fall_codi:
                        selected_season = "가을";
                        getCloth();
                        getBottom();
                        getOuter();
                        break;

                    case R.id.radio_winter_codi:
                        selected_season = "겨울";
                        getCloth();
                        getBottom();
                        getOuter();
                        break;
                }
            }
        });

        btn_my_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    btn_my_drawer.setText("내 옷장 닫기");
                    slidingLayout.setTouchEnabled(false);

                } else if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    btn_my_drawer.setText("내 옷장 열기");
                    slidingLayout.setTouchEnabled(true);
                }

            }
        });

        slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.closet_panel);
        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });

        // 리사이클러 뷰에 layoutManager 를 설정.

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // 리사이클러뷰 아이템 클릭했을 때의 이벤트
        listener = new RecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                // 리사이클러뷰 아이템 클릭했을 때의 이벤트
                Toast.makeText(getApplicationContext(), "상의 이미지가 출력되었습니다.", Toast.LENGTH_SHORT).show();

                //sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent,1);

                Glide.with(SelfCodi.this)
                        .asBitmap()
                        .load(clothList.get(position).getPicture())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                mg.addImage(resource);
                            }
                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
                // bitmap 필요함.
                            // setImage();
            }
        };

        // 리사이클러뷰 아이템 클릭했을 때의 이벤트
        listener_bottom = new RecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                // 리사이클러뷰 아이템 클릭했을 때의 이벤트
                Toast.makeText(getApplicationContext(), "하의 이미지가 출력되었습니다.", Toast.LENGTH_SHORT).show();

                //sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent,1);

                Glide.with(SelfCodi.this)
                        .asBitmap()
                        .load(bottomList.get(position).getPicture())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                mg.addImage(resource);
                            }
                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
                // bitmap 필요함.
                // setImage();
            }
        };

        // 리사이클러뷰 아이템 클릭했을 때의 이벤트
        listener_outer = new RecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                // 리사이클러뷰 아이템 클릭했을 때의 이벤트
                Toast.makeText(getApplicationContext(), "아우터 이미지가 출려되었습니다.", Toast.LENGTH_SHORT).show();

                //sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent,1);

                Glide.with(SelfCodi.this)
                        .asBitmap()
                        .load(outerList.get(position).getPicture())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                mg.addImage(resource);
                            }
                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
                // bitmap 필요함.
                // setImage();

            }
        };

        LinearLayoutManager layoutManager_bottom = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        bottom_recycler.setLayoutManager(layoutManager_bottom);

        LinearLayoutManager layoutManager_outer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        outer_recycler.setLayoutManager(layoutManager_outer);

    }

    // 툴바 메뉴 불러오기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.codi_menu, menu);

//        action = menu;

        return true;
    }

    // 툴바 메뉴 클릭이벤트
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.codi_save:
                // 이미지 저장
                Toast.makeText(getApplicationContext(), "Test 중입니다." , Toast.LENGTH_SHORT).show();
//                View MyView = mg.getView();
//                bitmap = getBitmapFromView(MyView);
                // View 에서 Bitmap 추출하고
                // 서버 업로드 메서드 실행.
//                uploadCodi();

//                MyView.setDrawingCacheEnabled(true);
//                Bitmap screenshot = MyView.getDrawingCache(); //스샷 형태로 저장
                // 필요한 것:  php 저장 페이지 / DB table / 변수 =  아이디값, url, 파일 / 레트로핏 설정
                return true;
        }

        return false;
    }

    // View에서 Bitmap 추출하는 메서드
    // 사용자가 완성한 코디(View) 에서 Bitmap을 추출한다.
    public Bitmap getBitmapFromView(View view)
    {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    bitmap_picture = BitmapFactory.decodeStream(in);
                    imgFlag = true;
                    in.close();
                    mg.addImage(bitmap_picture);
                    //picture.recycle(); //비트맵 리소스 해제
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Pets 따라하기 메소드
    public void getCloth() {

        String id = user_id;
        String type = "upper";
//        String type = load_type;
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
//                    tv_closet_empty.setVisibility(View.GONE);

                    adapter = new RecyclerAdapter(clothList, SelfCodi.this, listener);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                } else if (response.body() == null) {
                    recyclerView.setVisibility(View.GONE);
                    tv_no_upper.setVisibility(View.VISIBLE);
//                    tv_closet_empty.setText("불러올 데이터가 없습니다.");
                }
            }

            @Override
            public void onFailure(Call<List<ResponsePOJO>> call, Throwable t) {
                Toast.makeText(SelfCodi.this, "rp :"+
                                t.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 하의 받아오기
    public void getBottom() {

        String id = user_id;
        String type = "bottom";
//        String type = load_type;
        String season = selected_season;

        Call<List<ResponsePOJO>> call = RetrofitClient.getInstance().getApi().getCloth(id, type, season);
        call.enqueue(new Callback<List<ResponsePOJO>>() {
            @Override
            public void onResponse(Call<List<ResponsePOJO>> call, Response<List<ResponsePOJO>> response) {
                bottomList = response.body();

                String content = "";

                if(response.body() != null) {
                    Log.d("서버 응답 확인: ", response.body().toString());

                    bottom_recycler.setVisibility(View.VISIBLE);
//                    tv_closet_empty.setVisibility(View.GONE);

                    adapter = new RecyclerAdapter(bottomList, SelfCodi.this, listener_bottom);
                    bottom_recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                } else if (response.body() == null) {
                    bottom_recycler.setVisibility(View.GONE);
                    tv_no_bottom.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<List<ResponsePOJO>> call, Throwable t) {
                Toast.makeText(SelfCodi.this, "rp :"+
                                t.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 아우터 받아오기
    public void getOuter() {

        String id = user_id;
        String type = "outer";
//        String type = load_type;
        String season = selected_season;

        Call<List<ResponsePOJO>> call = RetrofitClient.getInstance().getApi().getCloth(id, type, season);
        call.enqueue(new Callback<List<ResponsePOJO>>() {
            @Override
            public void onResponse(Call<List<ResponsePOJO>> call, Response<List<ResponsePOJO>> response) {
                outerList = response.body();

                String content = "";

                if(response.body() != null) {
                    Log.d("서버 응답 확인: ", response.body().toString());

                    outer_recycler.setVisibility(View.VISIBLE);
//                    tv_closet_empty.setVisibility(View.GONE);

                    adapter = new RecyclerAdapter(outerList, SelfCodi.this, listener_outer);
                    outer_recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                } else if (response.body() == null) {
                    outer_recycler.setVisibility(View.GONE);
                    tv_no_outer.setVisibility(View.VISIBLE);
//                    tv_closet_empty.setVisibility(View.VISIBLE);
//                    tv_closet_empty.setText("불러올 데이터가 없습니다.");
                }
            }

            @Override
            public void onFailure(Call<List<ResponsePOJO>> call, Throwable t) {
                Toast.makeText(SelfCodi.this, "rp :"+
                                t.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadCodi() {

        View MyView = mg.getView();
        bitmap = getBitmapFromView(MyView);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("코디 데이터 추가 중...");
        progressDialog.show();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);

        byte[] imageInByte = byteArrayOutputStream.toByteArray();

        // 서버에 보낼 데이터 정의
        String encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);
        String id = user_id;

        Call<ResponsePOJO> call = RetrofitClient.getInstance().getApi().uploadCodi(id, encodedImage);
        call.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                progressDialog.dismiss();
                Toast.makeText(SelfCodi.this, response.body().getRemarks(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(),MyClosetActivity.class);
//                intent.putExtra("userID", user_id);
//                startActivity(intent);//액티비티 띄우기

                if(response.body().isStatus()) {

                } else {

                }
            }
            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(SelfCodi.this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });
//        Toast.makeText (this, encodedImage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCloth();
        getBottom();
        getOuter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset:
                mg.reset();
//                View MyView = mg.getView();
//                bitmap = getBitmapFromView(MyView);
                // View 에서 Bitmap 추출하고
                // 서버 업로드 메서드 실행.
//                uploadCodi();
                break;

            // 저장 버튼을 눌렀을 때의 이벤트
            case R.id.btn_save_codi:

//                uploadCodi();
//                mg.reset();

                // 편집된 이미지를 byteArray 로 바꾸어 intent 에 담아, 다음 액티비티로 넘기는 코드
                Intent intent = new Intent(getApplicationContext(), CodiInfo.class);

                View MyView = mg.getView();
                bitmap = getBitmapFromView(MyView);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                intent.putExtra("edited_image", byteArray);
                intent.putExtra("code", 888);
                intent.putExtra("userID", user_id);
                startActivity(intent);

        }
    }

    public class MyImgView extends View {

        private Canvas mCanvas;
        private Paint mPaint;
        private Bitmap mBitmap;

        public MyImgView(Context context) {
            super(context);
            mPaint = new Paint();
        }

        public MyImgView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            mPaint = new Paint();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (bitmap_picture != null) {
                int picX = (this.getWidth() - bitmap_picture.getWidth()) / 2 ;
                int picY = (this.getHeight() - bitmap_picture.getHeight()) / 2 ;
                canvas.drawBitmap(bitmap_picture, picX, picY, null);
                bitmap_picture.recycle(); //비트맵 리소스 해제
            }
        }

        public void reset() {
            // Paint Point ArrayList Clear
            imgFlag = false;
            // 비트맵 변수 초기화
            bitmap_picture = null;
            // 화면을 갱신함
            invalidate();
        }
    }
}