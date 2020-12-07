package com.example.suitupdaily;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DialogSeason {

    private Context context;
    public DialogSeason (Context context) {
        this.context = context;
    }
    public void callFunction (final EditText text_season) {

        final Dialog dialog = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_season);

        // 커스텀 다이얼로그의 사이즈를 조절한다.
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        // 커스텀 다이얼로그를 화면에 출력.
        dialog.show();

        final ImageView btn_cancel = (ImageView) dialog.findViewById(R.id.btn_cancel);

        final RadioGroup rg_season = (RadioGroup) dialog.findViewById(R.id.radio_group);
        final RadioButton rb_all_season = (RadioButton) dialog.findViewById(R.id.rb_all_season);
        final RadioButton rb_spring = (RadioButton) dialog.findViewById(R.id.rb_spring);
        final RadioButton rb_summer = (RadioButton) dialog.findViewById(R.id.rb_summer);
        final RadioButton rb_fall = (RadioButton) dialog.findViewById(R.id.rb_fall);
        final RadioButton rb_winter = (RadioButton) dialog.findViewById(R.id.rb_winter);
        final Button btn_season_confirm = (Button) dialog.findViewById(R.id.btn_season_confirm);

        rg_season.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_all_season) {
                    text_season.setText("모든 시즌 가능");

                } else if (checkedId == R.id.rb_spring) {
                    text_season.setText("봄");

                } else if (checkedId == R.id.rb_summer) {
                    text_season.setText("여름");

                } else if (checkedId == R.id.rb_fall) {
                    text_season.setText("가을");

                } else if (checkedId == R.id.rb_winter) {
                    text_season.setText("겨울");

                }
            }
        });

        btn_season_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}
