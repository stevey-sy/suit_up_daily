package com.example.suitupdaily;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suitupdaily.dialog.DialogCodi;
import com.example.suitupdaily.dialog.DialogCodiPlace;
import com.example.suitupdaily.dialog.SeasonMultipleChoice;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

// 사용자가 코디 이미지를 선택 후, 코디에 대한 정보를 입력 할 수 있는 액티비티
public class CodiInfo extends AppCompatActivity {

    private Bitmap bitmap;
    private TextView text_view_codi_season, text_view_codi_place, tags_view;
    private EditText text_input_tag, memo_area;
    private String user_id, string_hash_tag;
    private Button button_codi_upload;

    private ArrayList<String> mTagLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codi_info);

        ImageView image_view_codi = (ImageView) findViewById(R.id.image_view_codi);
        text_view_codi_season = (TextView) findViewById(R.id.text_view_codi_season);
        text_input_tag = (EditText) findViewById(R.id.text_view_codi_tag);
        Button button_tag_test = (Button) findViewById(R.id.button_tag_test);
        Button button_tag_remove = (Button) findViewById(R.id.button_tag_remove);
        button_codi_upload = (Button) findViewById(R.id.button_codi_upload);
        tags_view = (TextView)findViewById(R.id.text_view_hash_tag);
        memo_area = (EditText)findViewById(R.id.memo_area);
//        text_view_codi_place = (TextView) findViewById(R.id.text_view_codi_place);
        // 해쉬 태그가 표시될 텍스트 뷰

        // 해쉬 태그 문자열을 넣을 array list
        mTagLists = new ArrayList<String> ();

        // SelfCodi 클래스에서 사용자가 편집한 이미지를 받아오는 intent
        Intent getDataFromSelfCodi = getIntent();
        byte[] arr = getDataFromSelfCodi.getByteArrayExtra("edited_image");
        user_id = getDataFromSelfCodi.getStringExtra("userID");


        // intent 로 받아온 이미지(byte array)를 비트맵 으로 바꾸는 코드
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
//        text_view_codi_place.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // layout 을 새로 생성하여 만들어놓은 dialog xml 파일을 연결
//                final ConstraintLayout layout = (ConstraintLayout) View.inflate(CodiInfo.this, R.layout.dialog_codi_place, null);
//                new AlertDialog.Builder(CodiInfo.this)
//                        .setView(layout)
//                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // dialog에서 사용자의 입력을 받은 EditText 선언
//                                EditText hash = (EditText) layout.findViewById(R.id.text_input_hash);
//                                // EditText에 담긴 내용을 string으로 변환
//                                string_hash_tag = hash.getText().toString();
//                                // 액티비티에 있는 EditText로 String을 담는다. (사용자에게 보여주기 위해서)
//                                text_view_codi_place.setText(string_hash_tag);
//
//                                // 사용자가 입력한 string에서 ","을 기준으로 나누어, array list 에 담는다.
//                                String [] toColumnNm = string_hash_tag.split(",");
//                                for(int i=0; i< toColumnNm.length; i++) {
//                                    mTagLists.add(toColumnNm[i]);
//                                }
//
//                                //
//                                if(!isEmpty(string_hash_tag)) {
//                                    // string을 hastag 처럼 보이도록 하는 메서드
//                                    setContent();
//                                }
//
//                                dialog.dismiss();
//                            }
//                        })
//                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .show();
//            }
//        });

        // 태그 적용 버튼을 눌렀을 때의 이벤트
        button_tag_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 입력한 해쉬태그에 들어갈 문자열
                string_hash_tag = text_input_tag.getText().toString();

                // 사용자가 입력한 string에서 ","을 기준으로 나누어, array list 에 담는다.
                String [] toColumnNm = string_hash_tag.split(", ");
                for(int i=0; i< toColumnNm.length; i++) {
                    mTagLists.add(toColumnNm[i]);
                }

                if(!isEmpty(string_hash_tag)) {
                    // string 을 hash tag 처럼 보이도록 하는 메서드
                    setContent();
                    // hash tag 입력된 후에는 edit text 초기화.
                    text_input_tag.setText(null);
                }
            }
        });

        // 해쉬태그 지우기 버튼 눌렀을 때의 이벤트
        button_tag_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼을 누르면, 해쉬태그 리스트에서 가장 마지막 항목을 지우게 된다.
                if (mTagLists.size() > 0) {
                    mTagLists.remove(mTagLists.size() -1);
                    setContent();
                }
            }
        });

        // 저장 버튼을 눌렀을 때의 이벤트
        button_codi_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // retrofit으로 서버에 업로드하는 메소드
                uploadCodi();
            }
        });
    }

    // 사용자가 입력한 문자열을 해쉬태그처럼 보이도록 하는 메서드
    private void setContent() {
        String tag = "";
        int i;
        // 사용자가 입력했던 해쉬태그 리스트 (array list) 에 담긴 단어마다 #을 붙여서, 새로운 string 변수에 담는다.
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

        if(tags_view != null) {
            tags_view.setMovementMethod(LinkMovementMethod.getInstance());
            tags_view.setText(tagsContent);
            tags_view.setVisibility(View.VISIBLE);
            // 해쉬태그 확인용 로그
            Log.d("문자열 확인: ", tags_view.getText().toString());
        }
    }
    // 해쉬태그 요소 순서용?
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

    // 서버 업로드 메소드
    private void uploadCodi() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("코디 데이터 추가 중...");
        progressDialog.show();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);

        byte[] imageInByte = byteArrayOutputStream.toByteArray();

        // 서버에 보낼 데이터 정의
        String encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);
        String id = user_id;
        // 해쉬태그에서 "#" 문자만 제거
        String hash_tag = tags_view.getText().toString();
        String tags_no_hash = removeHash(hash_tag);
        String memo = memo_area.getText().toString();
        String season = text_view_codi_season.getText().toString();

        Call<ResponsePOJO> call = RetrofitClient.getInstance().getApi().uploadCodi(id, encodedImage, season, hash_tag, memo);
        call.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                progressDialog.dismiss();
                Toast.makeText(CodiInfo.this, response.body().getRemarks(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ShowRoom.class);
                intent.putExtra("userID", user_id);
                startActivity(intent);//액티비티 띄우기

                if(response.body().isStatus()) {
                } else {
                }
            }
            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(CodiInfo.this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });
//        Toast.makeText (this, encodedImage, Toast.LENGTH_SHORT).show();
    }
    // 해쉬태그 문자열에서 #만 제거하는 메소드
    public String removeHash(String with_hash) {

        // string 에서 특정 문자열 치환하는 코드
//        with_hash = tags_view.getText().toString();
        String no_hash;
        no_hash = with_hash.replace("#", "");

        Log.d("노해쉬: ", no_hash);

//        // array list 에서 특정 문자열 제거하는 코드
//        ArrayList<String> list = new ArrayList<String>(Arrays.asList("a", "b", "c", "d"));
//        Iterator<String> iter = list.iterator();
//        while (iter.hasNext()) {
//            String s = iter.next();
//            if (s.equals("a")) {
//                iter.remove();
//            }
//        }
        return no_hash;
    }
}