package com.example.suitupdaily.styling;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;

public class MovingUnit {

    //이미지
    private Bitmap Image;
    // 현재 이미지 위치 좌표
    private float X;
    private float Y;
    // 현재 이미지의 사이즈
    private float Width;
    private float Height;
    //처음 이미지를 선택했을 때,
    // 실제 이미지의 X,Y 좌표와
    // 클릭 지점 좌표 간의 거리
    private float offsetX;
    private float offsetY;
    // 이동하기 전 X,Y 좌표
    int posX1=0, posY1=0;
    // 이동하고난 후의 X,Y 좌표
    int posX2=0, posY2=0;
    // 사이즈 변경 드래그 전, 후 변수
    float oldDist = 1f;
    float newDist = 1f;
    // 드래그 모드인지 핀치줌 모드인지 구분
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    // 드래그 모드를 판단하는 변수
    int mode = NONE;

    public MovingUnit(int picX, int picY, Bitmap Image) {
        // TODO Auto-generated constructor stub
        this.Image=Image;
        // 이미지의 사이즈를 정의하는 함수
        setSize(Image.getHeight(),Image.getWidth());
        // 이미지가 출력될 위치를 지정하는 함수
        setXY(picX,picY);
    }
    // 이미지의 Motion 을 감지하여 return 하는 메소드
    public void TouchProcess(MotionEvent event) {
        int act = event.getAction();
        switch(act & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:    //첫번째 손가락 터치
                // 사용자가 터치한 위치 좌표
                posX1 = (int) event.getX();
                posY1 = (int) event.getY();
                // offset 변수에 사용자가 실제 터치한 위치와
                // 이미지의 현재 위치의 오차를 구한다.
                offsetX=posX1-X;
                offsetY=posY1-Y;
                posX2 = posX1;
                posY2 = posY1;
                Log.d("zoom", "mode=DRAG" );
                mode = DRAG;
                break;

            case MotionEvent.ACTION_MOVE:
                if(mode == DRAG) {
                    // 드래그 중이면, 이미지의 X,Y값을 변환시키면서 위치 이동.
                    X=posX2-offsetX;
                    Y=posY2-offsetY;
                    posX2 = (int) event.getX();
                    posY2 = (int) event.getY();
                    // 사용자가 20 pixel 이상 만큼 움직였을 때 작동한다.
                    if(Math.abs(posX2-posX1)>20 || Math.abs(posY2-posY1)>20) {
                        posX1 = posX2;
                        posY1 = posY2;
                        Log.d("drag","mode=DRAG");
                    }

                } else if (mode == ZOOM) {    // 핀치줌 중이면, 이미지의 거리를 계산해서 확대를 한다.
                    try {
                        newDist = spacing(event);
                    } catch (IllegalArgumentException e) {
                        Log.d("Zoom Exception: ", String.valueOf(e));
                    }

                    if (newDist - oldDist > 20) {  // zoom in
                        float scale=(float)Math.sqrt(((newDist-oldDist)*(newDist-oldDist))/(Height*Height + Width * Width));
                        Y=Y-(Height*scale/2);
                        X=X-(Width*scale/2);

                        Height=Height*(1+scale);
                        Width=Width*(1+scale);

                        oldDist = newDist;

                    } else if(oldDist - newDist > 20) {  // zoom out
                        float scale=(float)Math.sqrt(((newDist-oldDist)*(newDist-oldDist))/(Height*Height + Width * Width));
                        scale=0-scale;
                        Y=Y-(Height*scale/2);
                        X=X-(Width*scale/2);

                        Height=Height*(1+scale);
                        Width=Width*(1+scale);

                        oldDist = newDist;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:    // 첫번째 손가락을 떼었을 경우
            case MotionEvent.ACTION_POINTER_UP:  // 두번째 손가락을 떼었을 경우
                mode = NONE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //두번째 손가락 터치(손가락 2개를 인식하였기 때문에 핀치 줌으로 판별)
                mode = ZOOM;
                newDist = spacing(event);
                oldDist = spacing(event);
                Log.d("zoom", "newDist=" + newDist);
                Log.d("zoom", "oldDist=" + oldDist);
                Log.d("zoom", "mode=ZOOM");
                break;
            case MotionEvent.ACTION_CANCEL:
            default :
                break;
        }

    }
    //Rect 형태로 넘겨준다.
    public Rect getRect(){
        Rect rect=new Rect();
        // 업데이트 된 X, Y 좌표를 세팅하는 코드
        rect.set((int)X,(int)Y, (int)(X+Width), (int)(Y+Height));
        return rect;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);

    }
    public boolean InObject(float eventX,float eventY){
        if(eventX<(X+Width+30) &&  eventX>X-30 && eventY<Y+Height+30 && eventY>Y-30){
            return true;
        }
        return false;
    }
    public void setSize(float Height,float Width){
        this.Height=Height;
        this.Width=Width;

    }
    public void setXY(float X, float Y){
        this.X=X;
        this.Y=Y;
    }

    public Bitmap getImage(){
        return Image;
    }

    public boolean isOnPic(float nowX, float nowY) {
        float w = Width;
        float h = Height;
        Log.d("WWW width ", String.valueOf(w));
        Log.d("WWW nowX", String.valueOf(nowX));
        Log.d("WWW X", String.valueOf(X));
        if(nowX >= X && nowX <= X +w){
            if(nowY >= Y && nowY<= Y +h){
                return true;
            }
        }
        return false;
    }

}
