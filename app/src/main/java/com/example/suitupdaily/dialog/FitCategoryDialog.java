package com.example.suitupdaily.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suitupdaily.R;

public class FitCategoryDialog {

    private Context context;
    ImageView btn_cancel;

    public FitCategoryDialog(Context context) {
        this.context = context;
    }

    public void callFunction (final EditText text_fit) {

        final Dialog dialog = new Dialog(context);
        // 액티비티의 타이틀바를 숨긴다.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dialog.setContentView(R.layout.dialog_fit);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;

        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // 커스텀 다이얼로그를 화면에 출력.
        dialog.show();

        final TextView tv_over_sized = (TextView) dialog.findViewById(R.id.text_over_sized);
        final TextView tv_normal_fit = (TextView) dialog.findViewById(R.id.tv_normal);
        final TextView tv_slim_fit = (TextView) dialog.findViewById(R.id.tv_slim_fit);

        tv_over_sized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_fit.setText("루즈핏");
                dialog.dismiss();
            }
        });

        tv_normal_fit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_fit.setText("Normal");
                dialog.dismiss();
            }
        });

        tv_slim_fit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_fit.setText("슬림 핏");
                dialog.dismiss();
            }
        });

    }
}
