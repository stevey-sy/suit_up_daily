package com.example.suitupdaily.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.suitupdaily.HashTag;
import com.example.suitupdaily.R;

import java.util.ArrayList;

public class DialogCodiPlace {

    private Context context;
    private CustomDialogClickListener customDialogClickListener;

    private TextView text_view_near_home;
    private EditText edit_text_hash_tag;

    public DialogCodiPlace (@NonNull Context context) {
        this.context = context;
    }

//    public void callFunction (final TextView codi_place) {
//
//        //커스텀 다이얼로그를 정의하기 위해 Dialog 클래스를 생성한다.
//        final Dialog dialog = new Dialog(context);
//
//        // 액티비티의 타이틀바를 숨긴다.
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //다이얼로그의 배경을 투명으로 만듭니다.
////        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        // 커스텀 다이얼로그의 레이아웃을 설정한다.
//        dialog.setContentView(R.layout.activity_cloth_category_dialog);
//
//        // 커스텀 다이얼로그의 사이즈를 조절한다.
//        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
////        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
//
//        // 커스텀 다이얼로그를 화면에 출력.
//        dialog.show();
//
//        // xml 요소와 연결
////        final TextView text_view_near_home = (TextView) dialog.findViewById(R.id.text_view_near_home);
////        final TextView text_view_company_style = (TextView) dialog.findViewById(R.id.text_view_company_style);
////        final TextView text_view_chill_out = (TextView) dialog.findViewById(R.id.text_view_chill_out);
//        final EditText edit_text_self_input = (EditText) dialog.findViewById(R.id.text_input_hash);
//        final Button button_codi_confirm = (Button) dialog.findViewById(R.id.button_codi_confirm);
//
//        String self_input = edit_text_self_input.getText().toString();
//
//        text_view_near_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                codi_place.setText("집 근처 룩");
//                dialog.dismiss();
//            }
//        });
//
//        button_codi_confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                a = self_input;
//
//                if ( self_input.length() == 0 ) {
//                    //공백일 때 처리할 내용
//
//                } else {
//                    //공백이 아닐 때 처리할 내용
//                    codi_place.setText(self_input);
//                    dialog.dismiss();
//                }
//
//            }
//        });
//    }
}
