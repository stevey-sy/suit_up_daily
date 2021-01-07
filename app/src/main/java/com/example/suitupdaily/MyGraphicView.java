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

    //그래픽 요소를 저장하는 리스트
    ArrayList<MyPicture> pics = new ArrayList<MyPicture>();

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    int startX= -1, startY = -1, curX=-1, curY=-1;
    int oldPX=1,oldPY=1;
    //FOCUS된 객체의 인덱스
    int focusImg = -1;
    private float mPosX;
    private float mPosY;
    Context context;

//    private float mLastTouchX;
//    private float mLastTouchY;
//    private int mActivePointerId = INVALID_POINTER_ID;
//    private double touch_interval_X = 0; // X 터치 간격
//    private double touch_interval_Y = 0; // Y 터치 간격
//    private int zoom_in_count = 0; // 줌 인 카운트
//    private int zoom_out_count = 0; // 줌 아웃 카운트
//    private int touch_zoom = 0; // 줌 크기

    // Moving Unit 에서 가져온 변수들
    // 이미지를 canvas 에 표시할 좌표에 사용할 변수 x,y
    private float X, Y;
    // 비트맵을 생성자의 매개변수로 받았을 때, 해당 이미지의 높이와 너비를 담을 변수
    private float Width, Height;
    // 사용자가 이미지를 클릭했을 때,
    // 이미지의 현재 위치 좌표(x,y)값과 클릭된 지점간의 거리를 담을 변수
    private float offsetX, offsetY;
    // Drag 이벤트시 좌표를 저장할 변수  (이동하기 전, 이동 후)
    int posX1=0, posX2=0, posY1=0, posY2=0;
    // 핀치시 두 좌표간의 거리 저장 (핀치하기 전, 핀치하고 난 후)
    float oldDist = 1f;
    float newDist = 1f;
    // 드래그 모드인지 핀치 줌 모드인지 구분할 때 사용할 변수
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    private float mLastTouchX;
    private float mLastTouchY;
    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;

    public MyGraphicView(Context context) {
        super(context);
//        this.setOnTouchListener(this);
        this.context = context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
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
        mScaleDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                 //현재 스크린의 터치 좌표를 구한다.
                curX = (int) event.getX();
                curY = (int) event.getY();
                startX = curX;
                startY = curY;
                // 클릭한 이미지에만 focus 한다.
                focusImg = findPicOn();
                if (focusImg != -1) {
                    if(!pics.isEmpty()) {
                        oldPX = pics.get(focusImg).getPicX();
                        oldPY = pics.get(focusImg).getPicY();
                        // Moving Unit
                        // 현재 터치가 일어난 위치 좌표
                        posX1 = (int) event.getX();
                        posY1 = (int) event.getY();
                        X = pics.get(focusImg).getFloatX();
                        Y = pics.get(focusImg).getFloatY();
                        Log.d("posX1, posY1  = ", String.valueOf(posX1) +", "+String.valueOf(posY1));
                        Log.d("X, Y = ", String.valueOf(X) +", "+String.valueOf(Y));
                        // 현재 클릭이 일어난 좌표 - 현재 좌표 근처의 이미지의 좌표
                        offsetX=posX1-X;
                        offsetY=posY1-Y;
                        Log.d("offsetX = posX1 - X :  ", "posX1 = "+String.valueOf(posX1)+ " X = "+ String.valueOf(X)+ " offsetX = " + String.valueOf(offsetX));
                        mode = DRAG;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                 //Moving Unit
                if(mode == DRAG) {
                    // 0 - 터치되었던 부분의 좌표 - 현재 이미지의 좌표
                    X = posX2 - offsetX;
                    Y = posY2 - offsetY;

                    posX2 = (int) event.getX();
                    posY2 = (int) event.getY();
                    // Math.abs 는 절대값을 return 해주는 함수
                    if(Math.abs(posX2-posX1)>20 || Math.abs(posY2-posY1)>20) {
                        posX1 = posX2;
                        posY1 = posY2;
                    }
                }
                 //Moving Unit End
              curX = (int) event.getX();
              curY = (int) event.getY();

              int addX = startX - curX;
              int addY = startY - curY;

              if (focusImg == -1 ) {
                  break;
              } else {
                  if (!mScaleDetector.isInProgress()) {
                      if (!pics.isEmpty()) {
                          pics.get(focusImg).setPicX(oldPX-addX);
                          pics.get(focusImg).setPicY(oldPY-addY);
                      }
                  }
                  invalidate();
                  break;
              }
            case MotionEvent.ACTION_UP:
                focusImg = -1;
                break;
        }
        return true;
    }

//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        final int action = event.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                curX = (int) event.getX();
//                curY = (int) event.getY();
//                startX = curX;
//                startY = curY;
//                focusImg = findPicOn();
//                if (focusImg != -1) {
//                    if(!pics.isEmpty()) {
//                        oldPX = pics.get(focusImg).getPicX();
//                        oldPY = pics.get(focusImg).getPicY();
//                        mode = DRAG;
//                    }
//                }
//            break;
//            case MotionEvent.ACTION_MOVE:
//                if(mode == DRAG) {
//                    curX = (int) event.getX();
//                    curY = (int) event.getY();
//                    int addX = startX - curX;
//                    int addY = startY - curY;
//                    if (focusImg != -1) {
//                        if (!mScaleDetector.isInProgress()) {
//                            if (!pics.isEmpty()) {
//                                pics.get(focusImg).setPicX(oldPX - addX);
//                                pics.get(focusImg).setPicY(oldPY - addY);
//                            }
//                        }
//                        invalidate();
//                    }
//                    break;
//                } else if (mode == ZOOM) {
//                    newDist = spacing(event);
//                }
//
//            case MotionEvent.ACTION_UP:
//                focusImg = -1;
//                break;
//
//
//        }
//        return true;
//    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        final int action = event.getAction();
//        switch (action & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                curX = (int) event.getX();
//                curY = (int) event.getY();
//                startX = curX;
//                startY = curY;
//                focusImg = findPicOn();
//                if (focusImg != -1) {
//                    if (!pics.isEmpty()) {
////                        oldPX = pics.get(focusImg).getPicX();
////                        oldPY = pics.get(focusImg).getPicY();
//                        posX1 = (int) event.getX();
//                        posY1 = (int) event.getY();
////                        X = pics.get(focusImg).getFloatX();
////                        Y = pics.get(focusImg).getFloatY();
//                        Log.d("여기 X, Y", String.valueOf(X) + ", " +String.valueOf(Y));
//                        offsetX=posX1-X;
//                        offsetY=posY1-Y;
//                        mode = DRAG;
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if(mode == DRAG) {
//                    // 0 - 터치되었던 부분의 좌표 - 현재 이미지의 좌표
//                    X = posX2 - offsetX;
//                    Y = posY2 - offsetY;
//
//                    posX2 = (int) event.getX();
//                    posY2 = (int) event.getY();
//                    // Math.abs 는 절대값을 return 해주는 함수
//                    if(Math.abs(posX2-posX1)>20 || Math.abs(posY2-posY1)>20) {
//                        posX1 = posX2;
//                        posY1 = posY2;
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                focusImg = -1;
//                break;
//        }
//        return true;
//    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.WHITE);
        if(!pics.isEmpty()) {
            for (int i = 0; i < pics.size(); i++) {
                canvas.drawBitmap(pics.get(i).getPicture(), pics.get(i).getPicX(), pics.get(i).getPicY(), pics.get(i).getPaint());
//                canvas.drawBitmap(pics.get(i).getPicture(), null, pics.get(i).getRect(), null);
//                canvas.drawBitmap(MU.get(i).getImage(), null,  MU.get(i).getRect(), null);
            }
            canvas.translate(mPosX, mPosY);
            canvas.scale(mScaleFactor, mScaleFactor);
//            canvas.restore();
        }
    }

    public void reset() {
//        paints.clear(); // PaintPoint ArrayList Clear
//        MU.clear();
//        bottom.clear();
//        outer.clear();
        pics.clear();
        invalidate(); // 화면을 갱신함 -> onDraw()를 호출
    }

    public void addImage(Bitmap picture){
        Log.d("화면 크기 ", String.valueOf(this.getWidth()));
        int picX = (this.getWidth() - picture.getWidth()) / 2 ;

        Log.d("비트맵 크기 ", String.valueOf(picture.getWidth()));
        int picY = (this.getHeight() - picture.getHeight()) / 2 ;
        Log.d("화면 높이 ", String.valueOf(this.getHeight()));
        pics.add(new MyPicture(picX,picY,picture,null));
//        MU.add(new MovingUnit(picture));
////        MU = new MovingUnit(picture);
        invalidate();
    }

    private int spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (int)Math.sqrt(x * x + y * y);
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
                    Log.d("findPicOn i : ", String.valueOf(i));
                    return i;
                }
            }
        }
        return -1;
    }

}
