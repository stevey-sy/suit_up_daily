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
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;

// 사용자가 코디 이미지를 선택 후, 코디에 대한 정보를 입력 할 수 있는 액티비티
public class CodiInfo extends AppCompatActivity {

    private ImageView image_view_codi;
    private Bitmap bitmap;
    private TextView text_view_codi_season, text_view_codi_place;
    private EditText text_input_tag;
    private String string_hash_tag;
    private Button button_tag_test, button_tag_remove;

    private ArrayList<String> mTagLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codi_info);

        image_view_codi = (ImageView) findViewById(R.id.image_view_codi);
        text_view_codi_season = (TextView) findViewById(R.id.text_view_codi_season);
        text_input_tag = (EditText) findViewById(R.id.text_view_codi_tag);
        button_tag_test = (Button) findViewById(R.id.button_tag_test);
        button_tag_remove = (Button) findViewById(R.id.button_tag_remove);
        text_view_codi_place = (TextView) findViewById(R.id.text_view_codi_place);
        // 해쉬 태그가 표시될 텍스트 뷰

        // 해쉬 태그 문자열을 넣을 array list
        mTagLists = new ArrayList<String> ();

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

                                string_hash_tag = hash.getText().toString();
                                text_view_codi_place.setText(string_hash_tag);

                                String [] toColumnNm = string_hash_tag.split(",");
                                for(int i=0; i< toColumnNm.length; i++) {
                                    mTagLists.add(toColumnNm[i]);
                                }

                                if(!isEmpty(string_hash_tag)) {
                                    setContent();
                                }

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

        button_tag_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                string_hash_tag = text_input_tag.getText().toString();

                String [] toColumnNm = string_hash_tag.split(", ");
                for(int i=0; i< toColumnNm.length; i++) {
                    mTagLists.add(toColumnNm[i]);
                }

                if(!isEmpty(string_hash_tag)) {
                    setContent();
                    text_input_tag.setText(null);
                }
            }
        });

        button_tag_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagLists.remove(mTagLists.size() -1);
                setContent();
            }
        });

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
//        tags_view.setVisibility(View.INVISIBLE);

        if(tags_view != null) {
            tags_view.setMovementMethod(LinkMovementMethod.getInstance());
            tags_view.setText(tagsContent);
            tags_view.setVisibility(View.VISIBLE);
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