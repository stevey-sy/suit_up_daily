package com.example.suitupdaily;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.ScaleGestureDetector;

public class MyPicture {

    int picX,picY ;
    Bitmap picture;
    Paint paint;

    // Moving Unit 에서 가져온 변수들
    // 이미지를 canvas 에 표시할 좌표에 사용할 변수 x,y
    private float X, Y;
    // 비트맵을 생성자의 매개변수로 받았을 때, 해당 이미지의 높이와 너비를 담을 변수
    private float Width, Height;
    // 사용자가 이미지를 클릭했을 때,
    // 이미지의 현재 위치 좌표(x,y)값과 클릭된 지점간의 거리를 담을 변수
    private float offsetX, offSetY;
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

    MyPicture(int picX, int picY, Bitmap picture, Paint paint){
        this.picX = picX;
        this.picY = picY;
        this.picture = picture;
        this.paint = paint;
    }
//    MyPicture(int picX, int picY, Bitmap picture, Paint paint) {
//        this.picture = picture;
//        setSize(picture.getHeight(), picture.getWidth());
//        setXY(0,0);
//    }

    public void setSize(float Height,float Width){
        this.Height=Height;
        this.Width=Width;
    }
    public void setXY(float X, float Y){
        this.X=X;
        this.Y=Y;
    }

    int getPicX(){
        return picX;
    }

    int getPicY(){
        return picY;
    }

    void setPicX(int x){
        this.picX = x;
    }

    void setPicY(int Y){
        this.picY = Y;
    }

    void setPaint(Paint paint){
        this.paint = paint;
    }

    Paint getPaint(){
        return paint;
    }

    Bitmap getPicture(){
        return picture;
    }

    void setPicture(Bitmap picture){
        this.picture = picture;
    }

    public float getFloatX() {
        return X;
    }

    public float getFloatY() {
        return Y;
    }

    boolean isOnPic(int x, int y){
        int w = picture.getWidth() ;
        int h = picture.getHeight() ;

        Log.d("ZZZ width ", String.valueOf(w));
        Log.d("ZZZ Height", String.valueOf(h));
        Log.d("ZZZ x", String.valueOf(x));
        Log.d("ZZZ pic", String.valueOf(picX));
        if(x >= picX && x <= picX +w){
            if(y >= picY && y<= picY +h){
                return true;
            }
        }
        return false;
    }
    //Rect 형태로 넘겨준다.
    public Rect getRect(){
        Rect rect=new Rect();
        rect.set((int)X,(int)Y, (int)(X+Width), (int)(Y+Height));
        return rect;
    }
}
