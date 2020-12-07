package com.example.suitupdaily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CustomView extends View {

    TextView tv;
    SelfCodi mContext;

    String picture;

    private Bitmap image1, image2, image3;
    private Paint paint, paint2;

    Bitmap myBitmap;

    public CustomView(Context context) {
        super(context);

        // 여기서 서버 통신을 한다 해도,  어떤 이미지를 불러올지 지정을 해야하는데..
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = (SelfCodi)context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint2 = new Paint();
        paint2.setColor(Color.RED);
        canvas.drawRect(10, 10, 100, 100, paint2);

        Paint paint = new Paint();
        @SuppressLint("DrawAllocation") Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.black_frame);
        canvas.drawBitmap(img, 150, 150, paint);

//        Bitmap smallimg = Bitmap.createScaledBitmap(img, img.getWidth()/2, img.getHeight()/2, false);
//        canvas.drawBitmap(smallimg, 400, 350, paint);

//        canvas.drawBitmap(myBitmap, 0, 0, null);

        tv = new TextView(mContext);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        tv.setText("hi, there");

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        ((ConstraintLayout) this.getParent()).addView(tv);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        switch(e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                tv.setX(e.getX());
                tv.setY(e.getY());
                break;
        }
        return true;
    }


}
