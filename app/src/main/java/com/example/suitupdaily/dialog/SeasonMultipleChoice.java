package com.example.suitupdaily.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.suitupdaily.R;

import java.util.ArrayList;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class SeasonMultipleChoice {

    private Context context;
    // 체크박스 체크 여부를 나타낼 변수
    // 체크가 안되어있을 떄 = 0
    // 체크가 되어있을 때 = 1
    public int terms_agree_usage = 0;
    public int terms_agree_private_info = 0;
    public int terms_agree_all = 0;

    public int all_season_checked = 0;
    public int spring_checked = 0;
    public int summer_checked = 0;
    public int fall_checked = 0;
    public int winter_checked = 0;

    public SeasonMultipleChoice (Context context) {
        this.context = context;
    }
    public void callFunction(final TextView text_season) {

        final Dialog dialog = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.season_check_box);

        // 커스텀 다이얼로그의 사이즈를 조절한다.
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        // 커스텀 다이얼로그를 화면에 출력.
        dialog.show();

        final Button btn_confirm = (Button) dialog.findViewById(R.id.btn_season_confirm);
        final CheckBox cb_all = (CheckBox) dialog.findViewById(R.id.check_all_season);
        final CheckBox cb_spring = (CheckBox) dialog.findViewById(R.id.check_spring);
        final CheckBox cb_summer = (CheckBox) dialog.findViewById(R.id.check_summer);
        final CheckBox cb_fall = (CheckBox) dialog.findViewById(R.id.check_fall);
        final CheckBox cb_winter = (CheckBox) dialog.findViewById(R.id.check_winter);

        // 모든 시즌 버튼
        cb_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                            terms_agree_usage = 1;
                    cb_spring.setChecked(true);
                    cb_summer.setChecked(true);
                    cb_fall.setChecked(true);
                    cb_winter.setChecked(true);
                } else {
//                            terms_agree_private_info = 0;
                    cb_spring.setChecked(false);
                    cb_summer.setChecked(false);
                    cb_fall.setChecked(false);
                    cb_winter.setChecked(false);

                }
            }
        });

        // 봄 버튼
        cb_spring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(cb_spring.isChecked() && cb_summer.isChecked() && cb_fall.isChecked() && cb_winter.isChecked()) {
                        cb_all.setChecked(true);
                    }

                } else {
                    cb_all.setChecked(false);
                }
            }
        });

        // 여름 버튼
        cb_summer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(cb_spring.isChecked() && cb_summer.isChecked() && cb_fall.isChecked() && cb_winter.isChecked()) {
                        cb_all.setChecked(true);
                    }

                } else {
                    cb_all.setChecked(false);
                }
            }
        });

        // 가을 버튼
        cb_fall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(cb_spring.isChecked() && cb_summer.isChecked() && cb_fall.isChecked() && cb_winter.isChecked()) {
                        cb_all.setChecked(true);
                    }

                } else {
                    cb_all.setChecked(false);
                }
            }
        });

        // 겨울 버튼
        cb_winter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    if(cb_spring.isChecked() && cb_summer.isChecked() && cb_fall.isChecked() && cb_winter.isChecked()) {
                        cb_all.setChecked(true);
                    }

                } else {
                    cb_all.setChecked(false);
                }
            }
        });




        btn_confirm.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";

//                if(cb_spring.isChecked() == true) result += cb_spring.getText().toString();
//                if(cb_summer.isChecked() == true) result += cb_summer.getText().toString();
//                if(cb_fall.isChecked() == true) result += cb_fall.getText().toString();
//                if(cb_winter.isChecked() == true) result += cb_winter.getText().toString();

                if(cb_spring.isChecked() == true) result += cb_spring.getText().toString() + ", ";
                if(cb_summer.isChecked() == true) result += cb_summer.getText().toString() + ", ";
                if(cb_fall.isChecked() == true) result += cb_fall.getText().toString() + ", ";
                if(cb_winter.isChecked() == true) result += cb_winter.getText().toString() + ", ";


                if(!cb_spring.isChecked() && !cb_summer.isChecked() && !cb_fall.isChecked() && !cb_winter.isChecked()) {
                    Toast.makeText(context, "적어도 하나 이상은 선택해주세요." , Toast.LENGTH_SHORT).show();
                } else {

                    String noLast = result.substring(0, result.length()-2);

                    text_season.setText(noLast);
                    dialog.dismiss();
                }

            }
        });
    }


}
