package com.example.suitupdaily.styling;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.suitupdaily.R;
import com.example.suitupdaily.api.ResponsePOJO;
import com.example.suitupdaily.api.RetrofitClient;
import com.example.suitupdaily.dialog.SeasonMultipleChoice;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class CodiModify extends AppCompatActivity {

    private String user_id, picture, season, tags, memo, date, idx;
    private ImageView image_view_codi;
    private TextView text_view_hash_tag, text_view_codi_season, memo_area, title_codi_info;
    private EditText tag_area;
    private Button button_tag_remove, button_add_tag, button_codi_upload;
    private ArrayList<String> mTagLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codi_info);

        // xml 연결
        title_codi_info = (TextView)findViewById(R.id.title_codi_info);
        title_codi_info.setText("코디 수정");
        image_view_codi = (ImageView)findViewById(R.id.image_view_codi);
        text_view_hash_tag = (TextView)findViewById(R.id.text_view_hash_tag);
        text_view_codi_season = (TextView)findViewById(R.id.text_view_codi_season);
        memo_area = (TextView)findViewById(R.id.memo_area);
        button_tag_remove = (Button)findViewById(R.id.button_tag_remove);
        button_add_tag = (Button)findViewById(R.id.button_tag_test);
        button_codi_upload = (Button)findViewById(R.id.button_codi_upload);
        button_codi_upload.setText("수정");
        tag_area = (EditText)findViewById(R.id.text_view_codi_tag);

        // CodiDetail 클래스에서 받은 intent 데이터
        Intent getCodiInfo = getIntent();
        user_id = getCodiInfo.getStringExtra("userID");
        picture = getCodiInfo.getStringExtra("picture");
        season = getCodiInfo.getStringExtra("season");
        tags = getCodiInfo.getStringExtra("tags");
        memo = getCodiInfo.getStringExtra("memo");
        date = getCodiInfo.getStringExtra("date");
        idx = getCodiInfo.getStringExtra("idx");

        // 받아온 해쉬 리스트에서 # 을 제거.
        String no_hash = removeHash(tags);
        // string에서 " "을 기준으로 구분, array list 에 담는다.
        Log.d("노해쉬1: ", no_hash);
        // 해쉬 태그 문자열을 넣을 array list
        mTagLists = new ArrayList<String> ();
        String [] toColumnNm = no_hash.split(" ");
        for(int i=0; i< toColumnNm.length; i++) {
            mTagLists.add(toColumnNm[i]);
        }

        // 받아온 intent 데이터를 view 에 뿌려주는 메소드
        setDataFromIntentExtra();

        // 코디 계절 선택 버튼 눌렀을 때의 이벤트
        text_view_codi_season.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 계절 다중선택 가능한 커스텀 다이얼로그 생성
                SeasonMultipleChoice seasonMultipleChoice = new SeasonMultipleChoice(CodiModify.this);
                seasonMultipleChoice.callFunction(text_view_codi_season);
            }
        });

        // 태그 적용 버튼을 눌렀을 때의 이벤트
        button_add_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 입력한 해쉬태그에 들어갈 문자열
                String new_hash;
                new_hash = tag_area.getText().toString();

                // 사용자가 입력한 string에서 ","을 기준으로 나누어, array list 에 담는다.
                String [] toColumnNm = new_hash.split(", ");
                for(int i=0; i< toColumnNm.length; i++) {
                    mTagLists.add(toColumnNm[i]);
                }

                if(!isEmpty(new_hash)) {
                    // string 을 hash tag 처럼 보이도록 하는 메서드
                    setContent();
                    // hash tag 입력된 후에는 edit text 초기화.
                    tag_area.setText(null);
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

        // 저장 버튼 눌렀을 때의 이벤트
        button_codi_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadCodi();
            }
        });

    }

    // 받아온 intent 데이터를 view 에 뿌려주는 메소드
    @SuppressLint("CheckResult")
    private void setDataFromIntentExtra() {

        // 이미지, 해쉬태그, 계절, 메모
        text_view_hash_tag.setText(tags);
        text_view_codi_season.setText(season);
        memo_area.setText(memo);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.ic_hanger);
        requestOptions.error(R.drawable.ic_baseline_accessibility_24);

        Glide.with(CodiModify.this)
                .load(picture)
                .apply(requestOptions)
                .into(image_view_codi);
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

        if(text_view_hash_tag != null) {
            text_view_hash_tag.setMovementMethod(LinkMovementMethod.getInstance());
            text_view_hash_tag.setText(tagsContent);
            text_view_hash_tag.setVisibility(View.VISIBLE);
            // 해쉬태그 확인용 로그
            Log.d("문자열 확인: ", text_view_hash_tag.getText().toString());
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

    // 서버 업로드 메소드
    private void uploadCodi() {

        Drawable drawable = image_view_codi.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("코디 수정 중...");
        progressDialog.show();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);

        byte[] imageInByte = byteArrayOutputStream.toByteArray();

        // 서버에 보낼 데이터 정의
        String encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);
        String id = user_id;
        String key = idx;
        // 해쉬태그에서 "#" 문자만 제거
        String hash_tag = text_view_hash_tag.getText().toString();
        String tags_no_hash = removeHash(hash_tag);
        String memo = memo_area.getText().toString();
        String season = text_view_codi_season.getText().toString();

        Call<ResponsePOJO> call = RetrofitClient.getInstance().getApi().modifyCodi(key, id, season, hash_tag, memo);
        call.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                progressDialog.dismiss();
                Toast.makeText(CodiModify.this, response.body().getRemarks(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ShowRoom.class);
                intent.putExtra("userID", user_id);
                startActivity(intent);//액티비티 띄우기

                if(response.body().isStatus()) {
                } else {
                }
            }
            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(CodiModify.this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}