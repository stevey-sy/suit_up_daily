package com.example.suitupdaily;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suitupdaily.color.ColorUtils;
import com.example.suitupdaily.dialog.DialogColorGuide;
import com.example.suitupdaily.dialog.SeasonMultipleChoice;
import com.example.suitupdaily.grid.ColorGridAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gun0912.tedpermission.PermissionListener;
import com.skydoves.colorpickerview.AlphaTileView;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;


public class ImageEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;
    private FloatingActionButton add_img_fab;
    private ColorPickerView colorPickerView;
    private AlphaTileView color_square;
    private Drawable default_drawable;
    private View underline_color;
    private String color_hex_code;
    private int int_color;
    private View transparent_view;
    private TextView first_guide;
    private ColorUtils colorUtils;
    private ImageView img_cloth;
    private Button btn_crop;
    private Bitmap bitmap;
    private EditText text_main_category, text_color, text_color2, text_date, text_season, cloth_type, border_line, text_fit, text_show_again;
    private TextView text_guide, tv_color;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Boolean check_show;
    private String check = "off";
    Context context;
    private int status = 0;
    private int say = 1;
    private Mat img;
    private Dialog dialog;
    private GridView gridView;
    String[] color_name = {"블랙", "화이트", "아이보리", "베이지", "그레이"};
    String[] color_code = {"#000000","#FFFFFF", "#f5f5dc", "#ece6cc", "#d3d3d3"};
    final String[] season_list = new String[] {"모두", "봄", "여름", "가을", "겨울"};
    final int CODE_GALLERY_REQUEST = 999;
    private String name = "";
    int IMG_REQUEST = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);
        context = this.getBaseContext();

        SharedPreferences sf = getSharedPreferences("check",MODE_PRIVATE);
        check_show = sf.getBoolean("checker", false);
        // SharedPreferences 의 데이터를 저장/편집 하기위해 Editor 변수를 선언한다.

//        checkPermissions();

        //권한 체크
//                                TedPermission.with(getApplicationContext())
//                                        .setPermissionListener(permissionListener)
//                                        .setRationaleMessage("카메라 권한이 필요합니다.")
//                                        .setDeniedMessage("카메라 권한이 허가되었습니다.")
//                                        .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                                        .check();

        Intent intent_get_id = getIntent();
        name = intent_get_id.getStringExtra("userID");

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        Intent get_intent = getIntent();

        status = extras.getInt("code");
        // 툴바 세팅
        toolbar = findViewById(R.id.toolbar_add_cloth);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        // xml 연결
        img_cloth = (ImageView) findViewById(R.id.fake_image_view);
        colorPickerView = (ColorPickerView) findViewById(R.id.img_cloth);
        tv_color = (TextView) findViewById(R.id.tv_color_title);
        color_square = (AlphaTileView) findViewById(R.id.color_square);
        underline_color = (View) findViewById(R.id.underline_season);
        btn_crop = (Button) findViewById(R.id.btn_crop);
        text_main_category = (EditText) findViewById(R.id.text_main_category);
        text_color = (EditText) findViewById(R.id.text_color);
        text_date = (EditText) findViewById(R.id.text_date);
        text_fit = (EditText) findViewById(R.id.text_cloth_size);
        text_season = (EditText) findViewById(R.id.text_season);
        text_guide = (TextView) findViewById(R.id.msg_choose);
        cloth_type = (EditText) findViewById(R.id.cloth_type);
        cloth_type.setVisibility(GONE);
        text_show_again = (EditText) findViewById(R.id.text_show_again);
        text_show_again.setVisibility(GONE);
        border_line = (EditText) findViewById(R.id.border_line);
        border_line.setClickable(false);
        add_img_fab = (FloatingActionButton) findViewById(R.id.add_img_fab);
        add_img_fab.setVisibility(View.GONE);

        // 여기서 code 888 왔을 때, 안왔을 때로 나뉘어서 작동
        default_drawable = ContextCompat.getDrawable(this, R.drawable.ic_clothes_hanger);
        colorPickerView.setPaletteDrawable(default_drawable);
        tv_color.setVisibility(View.INVISIBLE);
        color_square.setVisibility(View.INVISIBLE);
        underline_color.setVisibility(View.INVISIBLE);
        colorPickerView.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                color_square.setBackgroundColor(envelope.getColor());
//                tv_color.setText("#" + envelope.getHexCode());
                color_hex_code = "#" + envelope.getHexCode();
                int_color = envelope.getColor();
                int[] array = envelope.getArgb();
                Log.d("argb 값: ", Arrays.toString(array));
                array = sliceArray(array, 1, 4);
                Log.d("rgb 값: ", Arrays.toString(array));
                colorUtils = new ColorUtils();
                String color_name2 = colorUtils.getColorNameFromRgb2(array);
                Log.d("rgb 컬러 값2: ", color_name2);
                tv_color.setText(color_name2);
                String color = Integer.toString(int_color);
            }
        });

        //  이미지 불러오기 버튼 눌렀을 때
        img_cloth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                transparent_view.setVisibility(View.GONE);
////                first_guide.setVisibility(View.GONE);
                // 팝업 메뉴가 뜨면서,  카메라 촬영할지 / 겔러리에서 가져올지 선택.
                PopupMenu p = new PopupMenu(getApplicationContext(), v);
                getMenuInflater().inflate(R.menu.context_add_cloth, p.getMenu());

                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()) {
                            case R.id.img_camera:
                                Intent intent_camera = new Intent(ImageEditActivity.this, CuttingImage.class);
                                intent_camera.putExtra("userID", name);
                                intent_camera.putExtra("requestCode", 1005);
                                startActivity(intent_camera);
                                return true;

                            case R.id.img_gallery:
                                Intent intent_gallery = new Intent(ImageEditActivity.this, CuttingImage.class);
                                intent_gallery.putExtra("userID", name);
                                intent_gallery.putExtra("requestCode", 1004);
                                startActivity(intent_gallery);
                                return true;
                        }
                        return false;
                    }
                });
                p.show();
            }
        });

        //  이미지 불러오기 버튼 눌렀을 때
        add_img_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                transparent_view.setVisibility(View.GONE);
////                first_guide.setVisibility(View.GONE);
                // 팝업 메뉴가 뜨면서,  카메라 촬영할지 / 겔러리에서 가져올지 선택.
                PopupMenu p = new PopupMenu(getApplicationContext(), v);
                getMenuInflater().inflate(R.menu.context_add_cloth, p.getMenu());

                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()) {
                            case R.id.img_camera:
                                Intent intent_camera = new Intent(ImageEditActivity.this, CuttingImage.class);
                                intent_camera.putExtra("userID", name);
                                intent_camera.putExtra("requestCode", 1005);
                                startActivity(intent_camera);
                                return true;

                            case R.id.img_gallery:
                                Intent intent_gallery = new Intent(ImageEditActivity.this, CuttingImage.class);
                                intent_gallery.putExtra("userID", name);
                                intent_gallery.putExtra("requestCode", 1004);
                                startActivity(intent_gallery);
                                return true;
                        }
                        return false;
                    }
                });
                p.show();
            }
        });

        // openCV로 편집된 이미지가 있을 때의 진행 코드
        if (status == 888) {
            img_cloth.setVisibility(View.GONE);
            add_img_fab.setVisibility(View.VISIBLE);
            // 이미지 편집이 완료된 상황이면,
            // color listener 활성화
            SharedPreferences shared = getSharedPreferences("check",MODE_PRIVATE);
            // SharedPreferences 의 데이터를 저장/편집 하기위해 Editor 변수를 선언한다.
            SharedPreferences.Editor shared_editor = shared.edit();

            check_show = sf.getBoolean("checker", false);
            Log.d("boolean 값: ", String.valueOf(check_show));
            if (!check_show) {
                // false 면은
                DialogColorGuide dialogColorGuide = new DialogColorGuide(ImageEditActivity.this);
                dialogColorGuide.callFunction(text_show_again);
            }
            shared_editor.putBoolean("checker", true);
            shared_editor.commit();
            // Name
            String check = text_show_again.getText().toString();
            Log.d("체크 값: ", check);
            if (check.contains("on")) {
                // 다시보지 않기
                Log.d("on 체크 값: ", check);
                shared_editor.putBoolean("checker", true);
                shared_editor.commit();

            } else if (check.contains("off")) {
                // 다시 봐도 됨
                Log.d("off 체크 값: ", check);
                shared_editor.putBoolean("checker", false);
                shared_editor.commit();
            }
            tv_color.setVisibility(View.VISIBLE);
            color_square.setVisibility(View.VISIBLE);
            underline_color.setVisibility(View.VISIBLE);
            text_color.setVisibility(View.GONE);

            byte[] arr = getIntent().getByteArrayExtra("edited_image");
            bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
//            img_cloth.setImageBitmap(bitmap);
            text_guide.setVisibility(View.GONE);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            colorPickerView.setPaletteDrawable(drawable);
        }

        //  저장 버튼
        btn_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ImageEditActivity.this, CuttingImage.class);
//                startActivity(intent);
                uploadImage();
            }
        });

        // 옷 카테고리 선택 버튼 눌렀을 때의 이벤트
        text_main_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClothCategoryDialog clothCategoryDialog = new ClothCategoryDialog(ImageEditActivity.this);
                clothCategoryDialog.callFunction(cloth_type, text_main_category);
            }
        });

        // 핏 선택 버튼 눌렀을 떄의 이벤트
        text_fit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FitCategoryDialog fitCategoryDialog = new FitCategoryDialog(ImageEditActivity.this);
                fitCategoryDialog.callFunction(text_fit);

            }
        });

        // 옷 계절 선택 버튼 눌렀을 때의 이벤트
        text_season.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SeasonMultipleChoice seasonMultipleChoice = new SeasonMultipleChoice(ImageEditActivity.this);
                seasonMultipleChoice.callFunction(text_season);

//                DialogSeason dialogSeason = new DialogSeason(ImageEditActivity.this);
//                dialogSeason.callFunction(text_season);
            }
        });

        // 컬러 버튼 눌렀을 때의 이벤트
        text_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openColorPicker();
//                showAlertDialog();
//                openDialog();
            }
        });
        // 구매일 버튼 눌렀을 때의 이벤트
        text_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    // 컬러값이 담긴 Array 를 쪼개는 메서드
    public int[] sliceArray(int[] array, int startIndex, int endIndex) {
        int size = endIndex-startIndex;
        int[] part = new int[size];
        System.arraycopy(array, startIndex, part, 0, part.length);
        return part;
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), "권한이 허용됨", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한이 허용됨", Toast.LENGTH_SHORT).show();
        }
    };


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
        text_date.setText(date);

    }

    public void openDialog() {
        dialog = new Dialog(ImageEditActivity.this);
        dialog.setContentView(R.layout.dialog_color_list);
//        dialog.findViewById(R.id.color_grid_view);
        gridView = dialog.findViewById(R.id.color_grid_view);
        dialog.setCancelable(true);

//        ArrayAdapter<String>adapter = new ArrayAdapter<>(ImageEditActivity.this, R.layout.color_grid_item, R.id.text_color_name, color_name);
        ColorGridAdapter adapter = new ColorGridAdapter(ImageEditActivity.this, color_name, color_code);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "position : " + position, Toast.LENGTH_SHORT).show();
                text_color.setText(color_name[position]);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public boolean getPreferenceBoolean(String key) {
        SharedPreferences pref = getSharedPreferences("check", MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    // 툴바 메뉴 눌렀을 때의 코드 (홈 버튼)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 사진 가져올 때 사용된 메서드
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                        text_guide.setVisibility(View.INVISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bitmap bitmap2 = BitmapFactory.decodeFile(imageFilePath);
                ExifInterface exif = null;

                try {
                    exif = new ExifInterface(imageFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int exifOrientation;
                int exifDegree;

                if (exif != null) {
                    exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    exifDegree = exifOrientationToDegress(exifOrientation);
                } else {
                    exifDegree = 0;
                }

                ((ImageView) findViewById(R.id.img_cloth)).setImageBitmap(rotate(bitmap2, exifDegree));
            }
    }

    private void uploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("옷장 데이터 추가 중...");
        progressDialog.show();

        // 비트맵 사이즈 줄이기
//        Bitmap resized = null;
//        int height = bitmap.getHeight();
//        int width = bitmap.getWidth();
//        resized = Bitmap.createScaledBitmap(bitmap, 80, 120, true);
//        height = resized.getHeight();
//        width = resized.getWidth();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);

        byte[] imageInByte = byteArrayOutputStream.toByteArray();

        // 서버에 보낼 데이터 정의
        String encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);
        String id = name;
        String category = text_main_category.getText().toString();
        String date = text_date.getText().toString();
        String season = text_season.getText().toString();
        String color = tv_color.getText().toString();
//        String color = text_color.getText().toString();
        String type = cloth_type.getText().toString();
        String fit = text_fit.getText().toString();
        int color_integer = int_color;

        Call<ResponsePOJO> call = RetrofitClient.getInstance().getApi().uploadImage(id, category, encodedImage, date, color, season, type, fit, color_integer);
        call.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                progressDialog.dismiss();
                Toast.makeText(ImageEditActivity.this, response.body().getRemarks(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),MyClosetActivity.class);
                intent.putExtra("userID", name);
                startActivity(intent);//액티비티 띄우기

                if(response.body().isStatus()) {

                } else {

                }
            }
            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(ImageEditActivity.this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });
//        Toast.makeText (this, encodedImage, Toast.LENGTH_SHORT).show();
    }

    private int exifOrientationToDegress(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    public void onBackPressed() {
        // 프로그래스 바 해체
        Intent intent = new Intent(getApplicationContext(),MyClosetActivity.class);
        intent.putExtra("userID", name);
        startActivity(intent);//액티비티 띄우기
    }

}