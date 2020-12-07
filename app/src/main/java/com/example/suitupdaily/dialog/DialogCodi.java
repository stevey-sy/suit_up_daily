package com.example.suitupdaily.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.suitupdaily.R;

public class DialogCodi {
    private Context context;

    public DialogCodi(Context context) {
        this.context = context;
    }

    public void callFunction () {
        final Dialog dialog = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dialog.setContentView(R.layout.dialog_color_guide);

        // 커스텀 다이얼로그의 사이즈를 조절한다.
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        // 커스텀 다이얼로그를 화면에 출력.
        dialog.show();

        final ImageView img_color_guide = (ImageView) dialog.findViewById(R.id.img_color_guide);
        Glide.with(dialog.getContext()).load(R.raw.codi_guide).into(img_color_guide);
        final Button btn_confirm = (Button) dialog.findViewById(R.id.btn_guide_confirm);
        final CheckBox check_see_again = (CheckBox) dialog.findViewById(R.id.check_see_again);
        final TextView dialog_title = (TextView) dialog.findViewById(R.id.dialog_title);
        dialog_title.setText("Tip. 코디 사용법");

        btn_confirm.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                dialog.dismiss();
            }
        });

    }


}
