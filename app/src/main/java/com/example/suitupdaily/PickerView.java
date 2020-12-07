package com.example.suitupdaily;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skydoves.colorpickerview.AlphaTileView;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.flag.BubbleFlag;
import com.skydoves.colorpickerview.flag.FlagMode;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.listeners.ColorListener;
import com.skydoves.colorpickerview.preference.ColorPickerPreferenceManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PickerView extends AppCompatActivity {

    private ImageView colorPickerView;
    private int REQUEST_CODE_GALLERY = 1;

    private ColorPickerPreferenceManager manager;

    private Uri uri;
    private Bitmap bitmap, copied_bitmap;
    private Button button;
    private Drawable drawable;
    private AlphaTileView alphaTileView2;
    private TextView textViewColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker_view);

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//
////                // 고르고 나면, startActivityForResult 로 넘어간다.
//        startActivityForResult(intent, 1);

        textViewColor = findViewById(R.id.textViewColor);
        alphaTileView2 = findViewById(R.id.alphaTileView2);
        colorPickerView = (ImageView)findViewById(R.id.colorPickerView);
        button = findViewById(R.id.button7);

        // 이미지 뷰에 이미지를 불러오면
        // 포인트를 자동으로 지정
        // 지정된 포인트 색을 listen 한다.

        colorPickerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

//                // 고르고 나면, startActivityForResult 로 넘어간다.
                startActivityForResult(intent, 1);

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ColorThread thread = new ColorThread();
//                thread.start();
//                Drawable d = colorPickerView.getDrawable();
//                Bitmap bitmap2 = ((BitmapDrawable)d).getBitmap();

//                setToolbarColor(copied_bitmap);

                Bitmap bitmap3 = ((BitmapDrawable)colorPickerView.getDrawable()).getBitmap();

                Palette.from(copied_bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@Nullable Palette palette) {
                        setPalette(palette);
                    }
                });


            }
        });



        if (bitmap != null) {
            Thread thread = new Thread(new Runnable() {//main스레드에서 돌리면 null값만 나옴. 반드시 따로 스레드 생성
                @Override
                public void run() {
                    Log.d("스레드 작동 확인: ", "thread");
                    Log.d("스레드 작동 확인: ", "thread");
                    Log.d("스레드 작동 확인: ", "thread");
                    Log.d("스레드 작동 확인: ", "thread");
                    Log.d("스레드 작동 확인: ", "thread");


//                    Drawable d = drawable;
//                    Bitmap bitmap2 = ((BitmapDrawable)d).getBitmap();//bitmap추출

                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {

                            if(palette==null)
                                return;

                            Palette.Swatch vibrantSwatch = palette.getDarkVibrantSwatch();//원하는 색깔 성향으로 swatch 생성
                            if(vibrantSwatch!=null)
                            {
                                Log.d("스레드 작동 확인: ", "thread2");
                                Log.d("스레드 작동 확인: ", "thread2");
                                Log.d("스레드 작동 확인: ", "thread2");
                                int color = vibrantSwatch.getRgb();//swatch에서 대표색 추출
                                textViewColor.setTextColor(color);//text색깔 설정
                                textViewColor.setAlpha(1);
                                alphaTileView2.setBackgroundColor(color);
                                Log.d("스레드 작동 확인: ", "thread3");
                                Log.d("스레드 작동 확인: ", "thread3");
                                Log.d("스레드 작동 확인: ", "thread3");

                            }
                        }
                    });
                }
            });
            thread.start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            try {
                final Uri imageUri = data.getData();
                InputStream in = null;
                if (imageUri != null) {
//                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                    bitmap = BitmapFactory.decodeStream(imageStream);
//                    drawable = new BitmapDrawable(getResources(), bitmap);
//                    colorPickerView.setImageDrawable(drawable);

//                    in = getContentResolver().openInputStream(data.getData());
//                    Bitmap bitmap3 = BitmapFactory.decodeStream(in);
//                    Log.d("비트맵3: ", String.valueOf(bitmap3));
//                    Log.d("비트맵3: ", String.valueOf(bitmap3));
//                    Log.d("비트맵3: ", String.valueOf(bitmap3));
//                    Log.d("비트맵3: ", String.valueOf(bitmap3));
//                    Log.d("비트맵3: ", String.valueOf(bitmap3));
//                    in.close();
//                    colorPickerView.setImageBitmap(bitmap3);

                    uri = data.getData();
                    //img_cloth.setImageURI(uri);
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), uri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                    copied_bitmap = ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.RGBA_F16, true);
                    Log.d("비트맵3: ", String.valueOf(bitmap));
                    Log.d("비트맵3: ", String.valueOf(bitmap));
                    Log.d("비트맵3: ", String.valueOf(bitmap));
                    Log.d("비트맵3: ", String.valueOf(bitmap));
                    Log.d("비트맵3: ", String.valueOf(bitmap));


                    colorPickerView.setImageBitmap(bitmap);

//                    doThread();



                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void doThread () {
    }

    public class ColorThread extends Thread {
            @Override
            public void run() {
                Log.d("스레드 작동 확인: ", "thread");
                Log.d("스레드 작동 확인: ", "thread");
                Log.d("스레드 작동 확인: ", "thread");
                Log.d("스레드 작동 확인: ", "thread");
                Log.d("스레드 작동 확인: ", "thread");

                Drawable d = colorPickerView.getDrawable();
//                Drawable d = drawable;
                Bitmap bitmap2 = ((BitmapDrawable)d).getBitmap();//bitmap추출

                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

                    @Override
                    public void onGenerated(Palette palette) {
                        Log.d("스레드 작동 확인: ", "thread22");
                        Log.d("스레드 작동 확인: ", "thread22");
                        Log.d("스레드 작동 확인: ", "thread22");
                        Log.d("스레드 작동 확인: ", "thread22");
                        if(palette==null)
                            return;

                        Log.d("스레드 작동 확인: ", "thread33");
                        Log.d("스레드 작동 확인: ", "thread33");
                        Log.d("스레드 작동 확인: ", "thread33");
                        Log.d("스레드 작동 확인: ", "thread33");

                        Palette.Swatch vibrantSwatch = palette.getDarkVibrantSwatch();//원하는 색깔 성향으로 swatch 생성
                        if(vibrantSwatch!=null)
                        {
                            Log.d("스레드 작동 확인: ", "thread44");
                            Log.d("스레드 작동 확인: ", "thread44");
                            Log.d("스레드 작동 확인: ", "thread44");
                            Log.d("스레드 작동 확인: ", "thread44");
                            int color = vibrantSwatch.getRgb();//swatch에서 대표색 추출
                            textViewColor.setTextColor(color);//text색깔 설정
                            textViewColor.setAlpha(1);
                            alphaTileView2.setBackgroundColor(color);
                        }
                    }
                });
            }
    }
    public void setToolbarColor(Bitmap bitmap) {
        Palette p2 = createPaletteSync(bitmap);
        Palette.Swatch vibrantSwatch = p2.getDarkVibrantSwatch();

        if(vibrantSwatch != null) {
            int color = vibrantSwatch.getRgb();
            textViewColor.setTextColor(color);
            alphaTileView2.setBackgroundColor(color);
        }

    }

    public Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }

    private void setPalette(Palette palette) {
        if (palette == null) {
            return;
        }

        Palette.Swatch vibrantSwatch = palette.getDarkVibrantSwatch();

        if(vibrantSwatch != null) {
            textViewColor.setTextColor(vibrantSwatch.getRgb());
            alphaTileView2.setBackgroundColor(vibrantSwatch.getRgb());
        }
    }
}