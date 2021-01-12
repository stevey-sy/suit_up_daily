package com.example.suitupdaily;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ClothCategoryDialog {

    private Context context;
    ImageView btn_cancel;

    public ClothCategoryDialog (Context context) {
        this.context = context;
    }

    public void callFunction (final EditText cloth_type, final EditText text_main_category) {
        //커스텀 다이얼로그를 정의하기 위해 Dialog 클래스를 생성한다.
        final Dialog dialog = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //다이얼로그의 배경을 투명으로 만듭니다.
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dialog.setContentView(R.layout.activity_cloth_category_dialog);

        // 커스텀 다이얼로그의 사이즈를 조절한다.
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        // 커스텀 다이얼로그를 화면에 출력.
        dialog.show();

        final ImageView btn_cancel = (ImageView) dialog.findViewById(R.id.btn_cancel);
        final TextView tv_outer = (TextView) dialog.findViewById(R.id.tv_outer);
        final TextView tv_upper = (TextView) dialog.findViewById(R.id.tv_upper_cloth);
        final TextView tv_bottom = (TextView) dialog.findViewById(R.id.tv_bottom_cloth);
        final TextView tv_shoes = (TextView) dialog.findViewById(R.id.tv_shoes);
        final TextView tv_accessory = (TextView) dialog.findViewById(R.id.tv_accessory);

        tv_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.dialog_bottom_detail);
                final TextView tv_pants = (TextView) dialog.findViewById(R.id.text_pants);
                final TextView tv_jean = (TextView) dialog.findViewById(R.id.text_jean);
                final TextView tv_slex = (TextView) dialog.findViewById(R.id.text_slex);
                final TextView tv_training_pants = (TextView) dialog.findViewById(R.id.text_training_pants);
                final TextView tv_short_pants = (TextView) dialog.findViewById(R.id.text_short_pants);

                cloth_type.setText("bottom");
                tv_pants.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("면바지");
                        dialog.dismiss();
                    }
                });

                tv_slex.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("슬렉스");
                        dialog.dismiss();
                    }
                });

                tv_jean.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("청바지");
                        dialog.dismiss();
                    }
                });

                tv_training_pants.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("츄리닝");
                        dialog.dismiss();
                    }
                });

                tv_short_pants.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("반바지");
                        dialog.dismiss();
                    }
                });
            }
        });

        tv_outer.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
//                text_main_category.setText("아우터");
//                dialog.dismiss();
                dialog.setContentView(R.layout.dialog_outer_detail);
                final TextView tv_coat = (TextView) dialog.findViewById(R.id.tv_coat);
                final TextView tv_jacket = (TextView) dialog.findViewById(R.id.tv_jacket);
                final TextView tv_padding = (TextView) dialog.findViewById(R.id.tv_padding);
                cloth_type.setText("outer");
                tv_coat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("코트");
                        dialog.dismiss();
                    }
                });
                tv_jacket.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("자켓/점퍼");
                        dialog.dismiss();
                    }
                });
                tv_padding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("패딩");
                        dialog.dismiss();
                    }
                });
            }
        });

        tv_upper.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
//                text_main_category.setText("아우터");
//                dialog.dismiss();
                cloth_type.setText("upper");
                dialog.setContentView(R.layout.dialog_upper_detail);
                final TextView tv_short_tee = (TextView) dialog.findViewById(R.id.tv_short_tee);
                final TextView tv_long_tee = (TextView) dialog.findViewById(R.id.tv_long_tee);
                final TextView tv_man_to_man = (TextView) dialog.findViewById(R.id.tv_man_to_man);
                final TextView tv_shirt = (TextView) dialog.findViewById(R.id.tv_shirt_short);
                final TextView tv_neat = (TextView) dialog.findViewById(R.id.tv_neat);
                final TextView tv_cardigan = (TextView) dialog.findViewById(R.id.tv_cardigan);
                final TextView tv_hood = (TextView) dialog.findViewById(R.id.tv_hood);
                final TextView tv_shirt_long = (TextView) dialog.findViewById(R.id.tv_shirt_long);

                tv_short_tee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("반팔 티셔츠");
                        dialog.dismiss();
                    }
                });

                tv_long_tee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("긴팔 티셔츠");
                        dialog.dismiss();
                    }
                });

                tv_man_to_man.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("맨투맨 티셔츠");
                        dialog.dismiss();
                    }
                });

                tv_shirt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("긴팔 셔츠");
                        dialog.dismiss();
                    }
                });

                tv_neat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("니트");
                        dialog.dismiss();
                    }
                });

                tv_cardigan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("가디건");
                        dialog.dismiss();
                    }
                });

                tv_hood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("후드티");
                        dialog.dismiss();
                    }
                });

                tv_shirt_long.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("긴팔 셔츠");
                        dialog.dismiss();
                    }
                });

            }
        });

        //신발 버튼 클릭 이벤트
        tv_shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cloth_type.setText("shoes");
                dialog.setContentView(R.layout.dialog_shoes);
                final TextView tv_sports_shoes = (TextView) dialog.findViewById(R.id.tv_sports_shoes);
                final TextView tv_casual_shoes= (TextView) dialog.findViewById(R.id.tv_casual_shoes);
                final TextView tv_sneakers = (TextView) dialog.findViewById(R.id.tv_sneakers);
                final ImageView btn_shoes_close = (ImageView) dialog.findViewById(R.id.btn_shoes_cancel);
                tv_sports_shoes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("운동화");
                        dialog.dismiss();
                    }
                });
                tv_casual_shoes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("구두");
                        dialog.dismiss();
                    }
                });
                tv_sneakers.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text_main_category.setText("스니커즈");
                        dialog.dismiss();
                    }
                });
                btn_shoes_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        //엑세서리 버튼
        tv_accessory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 닫기 버튼 눌렀을 때의 이벤트
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}