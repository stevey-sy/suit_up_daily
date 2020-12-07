package com.example.suitupdaily;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ClosetActivity extends AppCompatActivity {

    private ImageView img_cloth;
    private Button btn_img_choose, btn_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closet);

        img_cloth = (ImageView) findViewById(R.id.bt_choose);
        btn_upload = (Button) findViewById(R.id.btn_save);

        // 사용자가 사진을 고른다.
        img_cloth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 인텐트에 이미지를 넣었다.
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);


                // 고르고 나면, startActivityForResult 로 넘어간다.
                startActivityForResult(intent, 1);
            }
        });

    }

    // 사용자가 고른 이미지를 비트맵으로 변환. 표시한다.
    // 고른 이미지를 다음 액티비티로 넘기는 인텐트 넣어야 한다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                }
                if (uri != null) {
                    img_cloth.setImageURI(uri);
//                    imgPath = realPathUtil.getRealPath(this, uri);
//                    new AlertDialog.Builder(this).setMessage(uri.toString()+"\n"+imgPath).create().show();
                }
//                try {
//                    InputStream in = getContentResolver().openInputStream(data.getData());
////                    Bitmap img = BitmapFactory.decodeStream(in);
////                    in.close();
//
//                    Intent intent_send_image = new Intent(getApplicationContext(), ImageEditActivity.class);
//                    Bitmap sendBitmap = BitmapFactory.decodeStream(in);
//                    in.close();
////                    Bitmap sendBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.god);
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                    byte[] byteArray = stream.toByteArray();
//                    intent_send_image.putExtra("image",byteArray);
//                    startActivity(intent_send_image);
//
//                    // 이미지 표시
////                    img_cloth.setImageBitmap(img);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }
        }
    }

}