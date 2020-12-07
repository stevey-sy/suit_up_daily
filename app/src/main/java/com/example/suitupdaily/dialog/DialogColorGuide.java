package com.example.suitupdaily.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.suitupdaily.R;

import static android.content.Context.MODE_PRIVATE;

public class DialogColorGuide {

    private Context context;
    private String SHARE_NAME = "SHARE_PREF";

    public DialogColorGuide(Context context) {
        this.context = context;
    }


    public void callFunction (final EditText text_show_again) {
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
        Glide.with(dialog.getContext()).load(R.raw.color_guide).into(img_color_guide);
        final Button btn_confirm = (Button) dialog.findViewById(R.id.btn_guide_confirm);
        final CheckBox check_see_again = (CheckBox) dialog.findViewById(R.id.check_see_again);
//        final Button btn_never = (Button) dialog.findViewById(R.id.btn_never);

//        btn_never.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                text_show_again.setText("온");
//                dialog.dismiss();
//            }
//        });

//        btn_confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                if(checkBox.isChecked()) {
////                    text_show_again.setText("on");
////
////                } else {
////                    text_show_again.setText("off");
////                }
//                text_show_again.setText("오프");
//                dialog.dismiss();
//            }
//        });

        btn_confirm.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";

//                if(cb_spring.isChecked() == true) result += cb_spring.getText().toString();
//                if(cb_summer.isChecked() == true) result += cb_summer.getText().toString();
//                if(cb_fall.isChecked() == true) result += cb_fall.getText().toString();
//                if(cb_winter.isChecked() == true) result += cb_winter.getText().toString();

                if(check_see_again.isChecked() == true) result += "on";

                text_show_again.setText(result);
                dialog.dismiss();


            }
        });

    }
}
