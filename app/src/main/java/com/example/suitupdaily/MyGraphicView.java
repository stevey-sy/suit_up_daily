package com.example.suitupdaily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Layer;

import java.util.ArrayList;

import static android.app.usage.UsageEvents.Event.NONE;
import static android.view.MotionEvent.INVALID_POINTER_ID;

public class MyGraphicView extends View {

//    int X,Y,Height,Width;
//    private MovingUnit MU;
//    private MovingUnit bottom;
//    private MovingUnit outer;

    //그래픽 요소를 저장하는 리스트
    ArrayList<MyPicture> pics = new ArrayList<MyPicture>();
//    ArrayList<MyPicture> bottom = new ArrayList<MyPicture>();
//    ArrayList<MyPicture> outer = new ArrayList<MyPicture>();

//    ArrayList<MovingUnit> MU = new ArrayList<MovingUnit>();
//    ArrayList<MovingUnit> bottom= new ArrayList<MovingUnit>();
//    ArrayList<MovingUnit> outer = new ArrayList<MovingUnit>();

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    final int LINE = 1, SQUARE = 2, CIRCLE = 3,TEXT=4, ERASER=5 ,FINGER = 6;
    int startX= -1, startY = -1, stopX = -1, stopY = -1, curX=-1, curY=-1, curX_bottom=-1, curY_bottom=-1;
    int oldPX=1,oldPY=1;
    int oldPX_bottom=1, oldPY_bottom=1;

    //FOCUS된 객체의 인덱스
    int focusImg = -1;

    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;
    private float mPosX;
    private float mPosY;

    private double touch_interval_X = 0; // X 터치 간격
    private double touch_interval_Y = 0; // Y 터치 간격
    private int zoom_in_count = 0; // 줌 인 카운트
    private int zoom_out_count = 0; // 줌 아웃 카운트
    private int touch_zoom = 0; // 줌 크기

    Context context;

    public MyGraphicView(Context context) {
        super(context);
//        this.setOnTouchListener(this);
        this.context = context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
//        mScaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
//            @Override
//            public boolean onScale(ScaleGestureDetector detector) {
//
//                mScaleFactor *= detector.getScaleFactor();
//                invalidate();
//                return true;
//            }
//        });
    }

    public MyGraphicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        this.setOnTouchListener(this);
        this.context = context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
//        mScaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
//            @Override
//            public boolean onScale(ScaleGestureDetector detector) {
//
//                mScaleFactor *= detector.getScaleFactor();
//                invalidate();
//                return true;
//            }
//        });
    }

    public MyGraphicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        this.setOnTouchListener(this);
        this.context = context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

//        mScaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
//            @Override
//            public boolean onScale(ScaleGestureDetector detector) {
//
//                mScaleFactor *= detector.getScaleFactor();
//                invalidate();
//                return true;
//            }
//        });
    }

//    public void setSelectedImage(String ImagePath){
//        Bitmap Image=BitmapFactory.decodeFile(ImagePath);
//        MU=new MovingUnit(Image);
//        invalidate();
//    }

    public View getView(){
        return this;
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        mScaleDetector.onTouchEvent(event);
        // Let the ScaleGestureDetector inspect all events.
//        mScaleDetector.onTouchEvent(event);

//        if(MU != null) {
//            MU.TouchProcess(event);
//        }
//        if(bottom != null) {
//            bottom.TouchProcess(event);
//        }
//        if(outer != null) {
//            outer.TouchProcess(event);
//        }



//        MU.TouchProcess(event);
//        bottom.TouchProcess(event);
//        outer.TouchProcess(event);
//        invalidate();

//        MU.TouchProcess(event);
//        invalidate();

//        if (!MU.isEmpty()) {
////            focusImg = findPicOn();
//            // 어떻게 해야.. 자리 값을 가져올 수 있을 까
//            MU.get(0).TouchProcess(event);
//            invalidate();
//        }
//
//        if (!bottom.isEmpty()) {
////            focusImg = findPicOn();
//            // 어떻게 해야.. 자리 값을 가져올 수 있을 까
//            bottom.get(0).TouchProcess(event);
//            invalidate();
//        }
//
//        if (!outer.isEmpty()) {
////            focusImg = findPicOn();
//            // 어떻게 해야.. 자리 값을 가져올 수 있을 까
//            outer.get(0).TouchProcess(event);
//            invalidate();
//        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 스크린의 터치 좌표를 구한다.
                curX = (int) event.getX();
                curY = (int) event.getY();
                startX = curX;
                startY = curY;
                focusImg = findPicOn();

                // 어떻게 선택한 지역이 상의인지, 하의인지 구분할 수 있을까.
                if (focusImg != -1) {
                    if(!pics.isEmpty()) {
                        oldPX = pics.get(0).getPicX();
                        oldPY = pics.get(0).getPicY();
                        // 옵션 버튼 생성되는 코드
                        // 테두리부터 넣어봐 빨강으로 넣던지.
                        // 테두리는 어떻게 표시할 수 있었을까.

                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:

              curX = (int) event.getX();
              curY = (int) event.getY();

              int addX = startX - curX;
              int addY = startY - curY;

              if (focusImg == -1 ) {
                  break;
              } else {

                  if (!pics.isEmpty()) {
                      pics.get(focusImg).setPicX(oldPX-addX);
                      pics.get(focusImg).setPicY(oldPY-addY);
                  }
//
                  invalidate();
                  break;
              }

            case MotionEvent.ACTION_UP:
                focusImg = -1;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

//        //상의 이미지를 출력한다
//        if (MU != null) {
//            canvas.drawColor(Color.WHITE);
//            canvas.drawBitmap(MU.getImage(), null, MU.getRect(), null);
//        }
//
//        if (MU != null && bottom != null) {
//
//        }
//
//        //상의 이미지를 출력한다
//        if (bottom != null) {
//            canvas.drawColor(Color.WHITE);
//            canvas.drawBitmap(bottom.getImage(), null, bottom.getRect(), null);
//        }
//
//        //상의 이미지를 출력한다
//        if (outer != null) {
//            canvas.drawColor(Color.WHITE);
//            canvas.drawBitmap(outer.getImage(), null, outer.getRect(), null);
//        }

        if(!pics.isEmpty()) {
            for (int i = 0; i < pics.size(); i++) {
                canvas.drawBitmap(pics.get(i).getPicture(), pics.get(i).getPicX(), pics.get(i).getPicY(), pics.get(i).getPaint());
//                canvas.drawBitmap(MU.get(i).getImage(), null,  MU.get(i).getRect(), null);
            }
            canvas.drawColor(Color.WHITE);
            canvas.translate(mPosX, mPosY);
            canvas.scale(mScaleFactor, mScaleFactor);
        }

//        if(!MU.isEmpty()) {
//            canvas.drawColor(Color.WHITE);
//            for (int i = 0; i < MU.size(); i++) {
////                canvas.drawBitmap(pics.get(i).getPicture(), pics.get(i).getPicX(), pics.get(i).getPicY(), pics.get(i).getPaint());
//                canvas.drawBitmap(MU.get(i).getImage(), null,  MU.get(i).getRect(), null);
//            }
//        }
//
//        if(!bottom.isEmpty()) {
//            canvas.drawColor(Color.WHITE);
//            for (int i = 0; i < bottom.size(); i++) {
////                canvas.drawBitmap(pics.get(i).getPicture(), pics.get(i).getPicX(), pics.get(i).getPicY(), pics.get(i).getPaint());
//                canvas.drawBitmap(bottom.get(i).getImage(), null,  bottom.get(i).getRect(), null);
//            }
//        }
//
//        if(!outer.isEmpty()) {
//            canvas.drawColor(Color.WHITE);
//            for (int i = 0; i < outer.size(); i++) {
////                canvas.drawBitmap(pics.get(i).getPicture(), pics.get(i).getPicX(), pics.get(i).getPicY(), pics.get(i).getPaint());
//                canvas.drawBitmap(outer.get(i).getImage(), null,  outer.get(i).getRect(), null);
//            }
//        }

    }

    public void reset() {
//        paints.clear(); // PaintPoint ArrayList Clear
//        MU.clear();
//        bottom.clear();
//        outer.clear();
        invalidate(); // 화면을 갱신함 -> onDraw()를 호출
    }

    public void addImage(Bitmap picture){

        int picX = (this.getWidth() - picture.getWidth()) / 2 ;
        int picY = (this.getHeight() - picture.getHeight()) / 2 ;
        pics.add(new MyPicture(picX,picY,picture,null));
//        MU.add(new MovingUnit(picture));
//        MU = new MovingUnit(picture);
        invalidate();
    }

//    public void addBottom(Bitmap picture){
//
//        int picX = (this.getWidth() - picture.getWidth()) / 2 ;
//        int picY = (this.getHeight() - picture.getHeight()) / 2 ;
////        pics.add(new MyPicture(picX,picY,picture,null));
//        bottom.add(new MovingUnit(picture));
////        bottom = new MovingUnit(picture);
//        invalidate();
//    }
//
//    public void addOuter(Bitmap picture){
//
//        int picX = (this.getWidth() - picture.getWidth()) / 2 ;
//        int picY = (this.getHeight() - picture.getHeight()) / 2 ;
////        pics.add(new MyPicture(picX,picY,picture,null));
//        outer.add(new MovingUnit(picture));
////        outer = new MovingUnit(picture);
//        invalidate();
//    }





    public int findPicOn(){
//        if(!MU.isEmpty()){
//            for (int i = MU.size()-1; i >= 0; i--) { //이미 그려진 객체들을 다 순회하여 확인
//                int X = (int) MU.get(i).getPicX();
//                int Y = (int) MU.get(i).getPicY();
//                if(MU.get(i).isOnPic(curX,curY)){
//                    return i;
//                }
//            }
//        }

        if(!pics.isEmpty()){
            for (int i = pics.size()-1; i >= 0; i--) { //이미 그려진 객체들을 다 순회하여 확인
                int X = (int) pics.get(i).getPicX();
                int Y = (int) pics.get(i).getPicY();
                if(pics.get(i).isOnPic(curX,curY)){
                    return i;
                }
            }
        }
        return -1;
    }

}
