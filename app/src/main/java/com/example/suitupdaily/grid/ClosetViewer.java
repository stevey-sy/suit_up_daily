package com.example.suitupdaily.grid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.suitupdaily.R;
import com.example.suitupdaily.ResponsePOJO;


// 그리드 뷰의 하나의 아이템 레이아웃
public class ClosetViewer extends LinearLayout {
    
    ImageView imageView;
    
    public ClosetViewer(Context context) {
        super(context);
        
        init(context);
    }

    public ClosetViewer (Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public void init(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.grid_layout,this,true);

        imageView = (ImageView) findViewById(R.id.iv_gird_item);
    }

//    public void setItem(ResponsePOJO responsePOJO) {
//        imageView.setImageResource(responsePOJO.getImage());
//    }

}
