package com.example.suitupdaily;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CuttingImage extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private ImageView image_edit;
    private Button btn_target, btn_cutter, btn_finish;
    private Bitmap bitmap;
    private Bitmap resized;
    private Bitmap grabCutImage;
    Uri uri = null;

    private BackgroundThread mBackThread;

    ProgressDialog progressDialog;

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;

    private String mCurrentPhotoPath;
    private int touchCount=0;
    Point tl;
    Point br;
    boolean targetChose = false;
    ProgressDialog dlg;
    int request;



    String name;

    @Override
    public void onBackPressed() {
        // 프로그래스 바 해체
        progressDialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutting_image);

        toolbar = findViewById(R.id.toolbar_crop);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        image_edit = (ImageView)findViewById(R.id.image_edit);
        btn_target = (Button)findViewById(R.id.btn_target);
        btn_cutter = (Button)findViewById(R.id.btn_grab);
        btn_finish = (Button)findViewById(R.id.btn_finish);

        Intent intent_get_id = getIntent();
        name = intent_get_id.getStringExtra("userID");
        request = intent_get_id.getIntExtra("requestCode", 1);

        if (request == 1005) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                }
                if(photoFile != null) {
                    photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            }

//            int permissionCheck = ContextCompat.checkSelfPermission(CuttingImage.this, Manifest.permission.CAMERA);
//            if(permissionCheck == PackageManager.PERMISSION_DENIED) {
//                // 권한 없을 때
//                ActivityCompat.requestPermissions(CuttingImage.this, new String[] {Manifest.permission.CAMERA}, 0);
//            } else {
//                // 권한 있을 때
//                Intent intent_capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (intent_capture.resolveActivity(getPackageManager()) != null) {
//                    File photoFile = null;
//                    try {
//                        photoFile = createImageFile();
//                    } catch (IOException e) {
//
//                    }
//
//                    if (photoFile != null) {
//                        photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
//                    }
//                }
//            }

        } else if (request == 1004) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            // 고르고 나면, startActivityForResult 로 넘어간다.
            startActivityForResult(intent, 1);

        }

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        // 고르고 나면, startActivityForResult 로 넘어간다.
//        startActivityForResult(intent, 1);

        dlg = new ProgressDialog(this);
        tl = new Point();
        br = new Point();
        if (!OpenCVLoader.initDebug()) {

        }

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImageEditActivity.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                grabCutImage.compress(Bitmap.CompressFormat.JPEG, 30, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("edited_image", byteArray);
                intent.putExtra("code", 888);
                intent.putExtra("userID", name);
                startActivity(intent);
//                if (mCurrentPhotoPath != null) {
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    Bitmap bitmap2 = grabCutImage;
//                    float scale = (float) (1024/(float)bitmap2.getWidth());
//                    int image_w = (int) (bitmap2.getWidth() * scale);
//                    int image_h = (int) (bitmap2.getHeight() * scale);
//
//                    Bitmap resize = Bitmap.createScaledBitmap(bitmap2, image_w, image_h, true);
//                    resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                    byte[] byteArray = stream.toByteArray();
//
//                    Intent intent = new Intent(CuttingImage.this, ImageEditActivity.class);
//                    intent.putExtra("image", byteArray);
//                    intent.putExtra("code", "888");
//
//                    startActivity(intent);
//
//                }
            }
        });

        btn_target.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                if (mCurrentPhotoPath != null) {
                    targetChose = false;
                    image_edit.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                if (touchCount == 0) {
                                    tl.x = event.getX();
                                    tl.y = event.getY();
                                    touchCount++;
                                }
                                else if (touchCount == 1) {
                                    br.x = event.getX();
                                    br.y = event.getY();

                                    Paint rectPaint = new Paint();
                                    rectPaint.setARGB(255, 255, 0, 0);
                                    rectPaint.setStyle(Paint.Style.STROKE);
                                    rectPaint.setStrokeWidth(3);
                                    Bitmap tmpBm = Bitmap.createBitmap(bitmap.getWidth(),
                                            bitmap.getHeight(), Bitmap.Config.RGB_565);
                                    Canvas tmpCanvas = new Canvas(tmpBm);

                                    tmpCanvas.drawBitmap(bitmap, 0, 0, null);
                                    tmpCanvas.drawRect(new RectF((float) tl.x, (float) tl.y, (float) br.x, (float) br.y),
                                            rectPaint);
                                    image_edit.setImageDrawable(new BitmapDrawable(getResources(), tmpBm));

                                    targetChose = true;
                                    touchCount = 0;
                                    image_edit.setOnTouchListener(null);
                                }
                            }
                            return true;
                        }
                    });
                }

            }
        });

        btn_cutter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                doWork();


//                mBackThread = new BackgroundThread();
//                mBackThread.setRunning(true);
//                mBackThread.start();

                if (request == 1004) {
                    if (mCurrentPhotoPath != null) {
                        progressDialog = new ProgressDialog(CuttingImage.this);
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.progress_dialog);
                        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                        mBackThread = new BackgroundThread();
                        mBackThread.setRunning(true);
                        mBackThread.start();
//                        grabcutAlgo(bitmap);
                    }
                } else if (request == 1005) {
                    grabcutAlgo(resized);
                }

//                if (request == 1004) {
//
//                    if (mCurrentPhotoPath != null) {
//                        grabcutAlgo(bitmap);
//                    }
//                } else if (request == 1005) {

//                        grabcutAlgo(resized);
//                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                uri= data.getData();
                image_edit.setImageURI(uri);
                mCurrentPhotoPath = createCopyAndReturnRealPath(this, uri);
                Log.d("절대경로 : ", uri.toString() + "|n" + mCurrentPhotoPath);
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), uri);
                bitmap = ImageDecoder.decodeBitmap(source);
                //image_edit.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(getApplicationContext(), mCurrentPhotoPath, Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            bitmap = BitmapFactory.decodeFile(imageFilePath);
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

                image_edit.setImageURI(photoUri);
                mCurrentPhotoPath = createCopyAndReturnRealPath(this, photoUri);

                image_edit.setImageBitmap(rotate(bitmap, exifDegree));

                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
            try {
                bitmap = ImageDecoder.decodeBitmap(source);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 비트맵 사이즈 줄이기
        resized = null;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        resized = Bitmap.createScaledBitmap(bitmap, 400, 600, true);
        }
    }

    public void doWork () {

        progressDialog = new ProgressDialog(CuttingImage.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Thread thread = new Thread(new Runnable() {
            public void run() {
                // 1. 필요한 작업
                if (request == 1004) {

                    if (mCurrentPhotoPath != null) {
                        grabcutAlgo(bitmap);
                    }
                } else if (request == 1005) {
                    progressDialog = new ProgressDialog(CuttingImage.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);

                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    grabcutAlgo(resized);
                }
                handler.sendEmptyMessage(0);
            }
        });
        thread.start();

    }

//    public Handler handler = new Handler(Looper.myLooper()) {
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            progressDialog.dismiss();
//        }
//    };

    public class BackgroundThread extends Thread {
        volatile boolean running = false;
        int cnt;

        void setRunning (boolean b) {
            running = b;
            cnt = 7;
        }

        @Override
        public void run() {

            while (running) {
                try {
                    sleep(1000);
                    grabcutAlgo(bitmap);

//                    if (request == 1004) {
//                        if (mCurrentPhotoPath != null) {
//                        grabcutAlgo(bitmap);
//                        }
//                    } else if (request == 1005) {
//                        grabcutAlgo(resized);
//                    }

                    running = false;
//
//                    if (cnt-- == 0) {
//                        running = false;
//                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(handler.obtainMessage());
            }
        }

    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            progressDialog.dismiss();

            boolean retry = true;
            while(retry) {
                try {
                    mBackThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public static String createCopyAndReturnRealPath(@NonNull Context context, @NonNull Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();

        if (contentResolver == null) {
            return null;
        }
        String filePath = context.getApplicationInfo().dataDir + File.separator + System.currentTimeMillis();
        File file = new File(filePath);
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);

            if (inputStream == null)
                return null;
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);
                outputStream.close();
                inputStream.close();


        } catch (IOException ignore) {
            return null;
        }
        return file.getAbsolutePath();
    }

    public void grabcutAlgo(Bitmap bit) {

        // ARGB_8888 = 한 pixel당 4byte씩 이용해서 우수한 색 표현을 하게 된다.
        // 256*256*256 = 16,777,216 가지 색 표현 가능
        // 각 색 별로 1byte씩 사용하고 있어서 개발시에도 다루기 편하다.

        // 매개변수 bit를 새로운 Bitmap 객체 b 에 복사하였다.
        // 사용자가 불러온 이미지를 새로운 bitmap b 객체에 옮김.
        Bitmap b = bit.copy(Bitmap.Config.ARGB_8888, true);

        // Point는 2차원의 위치를 나타내는 클래스이다.
        Point tl = new Point();
        Point br = new Point();
//GrabCut part
        // 새로운 Mat 객체 img를 생성
        Mat img = new Mat();
        //Mat 객체인 img에
        // 비트맵 b 를 Mat 객체 img에 출력한다.
        Utils.bitmapToMat(b, img);
        // cvtColor() 는 컬러를 변환하는 함수
        // cvtColor(입력영상, 출력영상, COLOR_BGR2GRAY);
        // 컬러를 rgba 에서 rgb 로 바꿈.
        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGBA2RGB);

        // 현재 Mat 객체 img 에는 비트맵 b가 들어가 있다.
        // img의 오와 열의 총 pixel 갯수를 int r,c 에 넣는다.
        int r = img.rows();
        int c = img.cols();
        // 이미지를 배경과 추출물로 구분할 기준 범위 지정.
        Point p1 = new Point(c / 100, r / 100);
        Point p2 = new Point(c - c / 100, r - r / 100);
        // p1 p2의 위치를 지정하여 하나의 사각형을 만든다.
        Rect rect = new Rect(p1, p2);
        //Rect rect = new Rect(tl, br);

        // 새로운 MAT 객체 background에 img.size()
        // CV_8UC3 = Mat의 Element가 어떤 타입의 데이터인지 지정해준다.
        // Scalar 클래스는 4채널 이하의 영상에서 픽셀 값을 표현하는 용도로 자주 사용된다.
        // Scalar(255, 255, 255) = 흰색
        Mat background = new Mat(img.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));

        Mat firstMask = new Mat();
        // 배경 모델
        Mat bgModel = new Mat();
        // 추출물 모델
        Mat fgModel = new Mat();

        Mat mask;
        // GC_PR_FGD = 전경(추출물)에 속할 수도 있는 화소(pixel)
        // 아마도 분리할 때 구분 기준으로 사용할 source 인 것 같다.
        // iterCount = Number of iterations the algorithm should run.
        // source 는 1오 1열에 있는 pixel을 기준
        Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(Imgproc.GC_PR_FGD));
        Mat dst = new Mat();
        // 사각형 범위 안에 있는 데이터들을 기준으로 grabCut 알고리즘 실행
        // 이미지 추출
        Imgproc.grabCut(img, firstMask, rect, bgModel, fgModel, 5, Imgproc.GC_INIT_WITH_RECT);

        // firstMask 와 source의 pixel 값이 같은지 비교해보는 것 같음.
        Core.compare(firstMask, source, firstMask, Core.CMP_EQ);

        // 뽑아낼 이미지(전경)의 객체.
        // background 객체 만들때와 같은 모양임.
        // grabCut 결과물을 foreground 객체에 넣는다.
        Mat foreground = new Mat(img.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
        // 전경 객체를 firstMask Mat 객체에 출력한다.
        // 결과물을 first mask 에 복사했다.
        img.copyTo(foreground, firstMask);

        // color: 무슨 색인지는 모르겠으나, 색을 정의함.
        Scalar color = new Scalar(255, 0, 0, 255);
        // rectangle을 사용하여 범위와 색을 지정함.
        // img는 도화지, tl & br은 범위
        Imgproc.rectangle(img, tl, br, color);

        // tmp 라는 mat 객체를 생성.
        Mat tmp = new Mat();

        Imgproc.resize(background, tmp, img.size());
        background = tmp;
        mask = new Mat(foreground.size(), CvType.CV_8UC1,
                new Scalar(255, 255, 255));

        // 추출물의 컬러를 gray로 변경.
        Imgproc.cvtColor(foreground, mask, Imgproc.COLOR_BGR2GRAY);
        // 이미지를 이진화 한다.
        Imgproc.threshold(mask, mask, 254, 255, Imgproc.THRESH_BINARY_INV);
        System.out.println();
        Mat vals = new Mat(1, 1, CvType.CV_8UC3, new Scalar(0.0));
        background.copyTo(dst);

        background.setTo(vals, mask);
        // 결과물 들을 합치는 명령어.
        Core.add(background, foreground, dst, mask);
        grabCutImage = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);

        Bitmap processedImage = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(dst, grabCutImage);
        //dst.copyTo(sampleImage);
        image_edit.setImageBitmap(grabCutImage);
        firstMask.release();
        source.release();
        bgModel.release();
        fgModel.release();

    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
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


}