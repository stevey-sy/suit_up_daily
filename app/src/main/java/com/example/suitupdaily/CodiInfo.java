package com.example.suitupdaily;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suitupdaily.dialog.DialogCodi;
import com.example.suitupdaily.dialog.DialogCodiPlace;
import com.example.suitupdaily.dialog.SeasonMultipleChoice;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 사용자가 코디 이미지를 선택 후, 코디에 대한 정보를 입력 할 수 있는 액티비티
public class CodiInfo extends AppCompatActivity {

    private ImageView image_view_codi;
    private Bitmap bitmap;
    private TextView text_view_codi_season, text_view_codi_place;

    private ArrayList<String> mTagLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codi_info);

        image_view_codi = (ImageView) findViewById(R.id.image_view_codi);
        text_view_codi_season = (TextView) findViewById(R.id.text_view_codi_season);
        text_view_codi_place = (TextView) findViewById(R.id.text_view_codi_place);

        // SelfCodi 클래스에서 사용자가 편집한 이미지를 받아오는 intent
        Intent getDataFromSelfCodi = getIntent();
        byte[] arr = getDataFromSelfCodi.getByteArrayExtra("edited_image");

        // 받아온 이미지(byte array)를 비트맵 으로 바꾸는 코드
        bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        image_view_codi.setImageBitmap(bitmap);

        // 코디 계절 선택 버튼 눌렀을 때의 이벤트
        text_view_codi_season.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 계절 다중선택 가능한 커스텀 다이얼로그 생성
                SeasonMultipleChoice seasonMultipleChoice = new SeasonMultipleChoice(CodiInfo.this);
                seasonMultipleChoice.callFunction(text_view_codi_season);

//                DialogSeason dialogSeason = new DialogSeason(ImageEditActivity.this);
//                dialogSeason.callFunction(text_season);
            }
        });

        // 장소 선택 버튼 눌렀을 때의 이벤트
        text_view_codi_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ConstraintLayout layout = (ConstraintLayout) View.inflate(CodiInfo.this, R.layout.dialog_codi_place, null);
                new AlertDialog.Builder(CodiInfo.this)
                        .setView(layout)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText hash = (EditText) layout.findViewById(R.id.text_input_hash);
                                String value = hash.getText().toString();

                                text_view_codi_place.setText(value);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });

        mTagLists = new ArrayList<String> ();
        mTagLists.add("홍길동");
        mTagLists.add("고길동");
        mTagLists.add("둘리");

        setContent();
    }

    private void setContent() {
        String tag = "";
        int i;
        for(i=0; i<mTagLists.size(); i++) {
            tag += "#" + mTagLists.get(i) + " ";
        }

        ArrayList<int[]> hashtagSpans = getSpans(tag, '#');

        SpannableString tagsContent = new SpannableString(tag);

        for (i=0; i<hashtagSpans.size(); i++) {
            int[] span = hashtagSpans.get(i);
            int hashTagStart = span[0];
            int hashTagEnd = span[1];

            HashTag hashTag = new HashTag(this);
            hashTag.setOnClickEventListener(new HashTag.ClickEventListener() {
                @Override
                public void onClickEvent(String data) {

                }
            });

            tagsContent.setSpan(hashTag, hashTagStart, hashTagEnd, 0);
        }

        TextView tags_view = (TextView)findViewById(R.id.text_view_hash_tag);

        if(tags_view != null) {
            tags_view.setMovementMethod(LinkMovementMethod.getInstance());
            tags_view.setText(tagsContent);
        }
    }

    public ArrayList<int[]> getSpans(String body, char prefix) {
        ArrayList<int[]> spans = new ArrayList<int[]>();

        Pattern pattern = Pattern.compile(prefix + "\\w+");
        Matcher matcher = pattern.matcher(body);

        // Check all occurrences
        while (matcher.find()) {
            int[] currentSpan = new int[2];
            currentSpan[0] = matcher.start();
            currentSpan[1] = matcher.end();
            spans.add(currentSpan);
        }
        return spans;
    }

}