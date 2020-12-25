package com.example.suitupdaily.recycler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.suitupdaily.ImageEditActivity;
import com.example.suitupdaily.MyClosetActivity;
import com.example.suitupdaily.R;
import com.example.suitupdaily.ResponsePOJO;
import com.example.suitupdaily.RetrofitClient;
import com.example.suitupdaily.ShowRoom;
import com.example.suitupdaily.TimeConverter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodiShareAdapter extends RecyclerView.Adapter<CodiShareAdapter.ShareViewHolder> {

    List<ResponsePOJO> clothList;
    String[] likedList;
    private Context context;
    private CodiShareAdapter.ShareViewClickListener mListener;
    private TimeConverter timeConverter;
    private String clicked_idx, response_likes;

    private FirebaseAuth mAuth;
    private boolean checked;

    public CodiShareAdapter(List<ResponsePOJO> list, Context context, CodiShareAdapter.ShareViewClickListener listener) {
        this.clothList = list;
        this.context = context;
        this.mListener = listener;
    }
    @NonNull
    @Override
    public CodiShareAdapter.ShareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.codi_share_item, parent, false);
        return new CodiShareAdapter.ShareViewHolder(mView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final CodiShareAdapter.ShareViewHolder holder, final int position) {

        holder.idx.setText(clothList.get(position).getIdx());
        clicked_idx = clothList.get(position).getIdx();
        // 서버에서 받아온 날짜 데이터 예시
        // 2020-12-10 00:00:00
        // 여기서 -, :, " " 같은 불순물을 제거하면
        // 20201210000000
        // 이것을 timeConverter 클래스를 사용하여
        // ex) 4시간 전  이런 식으로 바꿔줌

        // 서버에서 받아온 date
        String date_from_server = clothList.get(position).getDate();
        // 불순물 제거
        String replace_dash = date_from_server.replace("-","");
        String replace_time = replace_dash.replace(":", "");
        String pure_date = replace_time.replace(" ", "");
        // timeConverter 클래스를 선언, 메소드 사용.
        // 현재 시간 기준으로 얼마나 지난 포스트인지 date 를 변환함.
        timeConverter = new TimeConverter();
        String converted_date = TimeConverter.CreateDataWithCheck(pure_date);
        // 변환된 date 를 view 에 담는다.
        holder.date.setText(converted_date);
        holder.text_view_hash_tags.setText(clothList.get(position).getTags());
        holder.memo.setText(clothList.get(position).getMemo());
        holder.text_view_user_name.setText(clothList.get(position).getUserNick());

        String user_id = "sinsy8989@gmail.com";
        // 좋아요 부분
        // 서버에서 해당글을 좋아요 누른 사람 리스트를 받는다.
        String who_liked = clothList.get(position).getWhoLiked();
         // 서버에서 받아온 리스트 문자열에 사용자 id 가 포함되어 있는지 체크
        if(who_liked.contains(user_id)) {
            // 리스트에 이미 id가 존재한다면 좋아요 누른처리
            int like_edit = clothList.get(position).getLike() -1;
            holder.text_view_like_num.setText(String.valueOf(like_edit));
            holder.check_like.setChecked(true);

        } else {
            // 리스트에 id가 존재하지 않는다면 좋아요 버튼 default 처리
            holder.check_like.setChecked(false);
            holder.text_view_like_num.setText(String.valueOf(clothList.get(position).getLike()));
        }
        // 이미지 데이터에 문제생겼을 경우 표시될 대체 이미지.
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.ic_clothes_hanger);
        requestOptions.error(R.drawable.ic_baseline_accessibility_24);
        // 서버에서 받아온 코디 이미지 출력.
        Glide.with(context)
                .load(clothList.get(position).getPicture())
                .apply(requestOptions)
                .into(holder.image_codi);
        // 서버에서 받아온 프로필 이미지 출력
        Glide.with(context)
                .load(clothList.get(position).getImgURL())
                .apply(requestOptions)
                .circleCrop()
                .into(holder.circleImageView);
    }

    // 리사이클러 뷰 안에 들어갈 아이템 개수를 불러오는 메서드
    @Override
    public int getItemCount() {
        return clothList.size();
    }

    public interface ShareViewClickListener {
        void onRowClick(View view, int position);
        void onLikeClick(View view, int position);
    }
    // 서버에서 내가 좋아요 누른 글들의 index를 조회를 해서
    // 이미 좋아요 누른 index의 글들은 색을 바꾸어 놓는다.
    private void getLikedList() {
        // 서버에 보낼 데이터 정의
        String id = "sinsy8989@gmail.com";
        Call<ResponsePOJO> call = RetrofitClient.getInstance().getApi().readLikedList(id);
        call.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                likedList = response.body().getMyLikeList();
                Log.d("좋아요 리스트 from 서버: ", Arrays.toString(likedList));
                if(response.body().isStatus()) {
                } else {
                }
            }
            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Log.d("리스트33: ", "통신 실패");
            }
        });
//        Toast.makeText (this, encodedImage, Toast.LENGTH_SHORT).show();
    }

    public static class ShareViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        private CodiShareAdapter.ShareViewClickListener mListener = null;
        private CircleImageView circleImageView;
        private ImageView image_codi, image_user_profile, image_like, image_like_orange, image_comment;
        private TextView text_view_user_name, text_view_hash_tags, text_view_like, text_view_comment, text_view_date, text_view_like_num;
        private TextView idx, hash_tags, memo, date;
        private LinearLayout mRowContainer;
        private Button btn_like;
        private CheckBox check_like;
        private Thread thread;
        private Boolean isThread;
        private int like_count;

        List<ResponsePOJO> clothList;
        private FirebaseAuth mAuth;
        private boolean checked;

        // 리사이클러 뷰 아이템에 무엇을 담을 건지 선언
        public ShareViewHolder(@NonNull View itemView, ShareViewClickListener listener) {
            super(itemView);
            // xml 연결
            mRowContainer = (LinearLayout) itemView.findViewById(R.id.share_item_view);
            image_codi = (ImageView) itemView.findViewById(R.id.img_view_codi_share);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.img_user_profile);
            check_like = (CheckBox) itemView.findViewById(R.id.check_like);
            text_view_user_name = (TextView) itemView.findViewById(R.id.text_view_user_name);
            text_view_hash_tags = (TextView) itemView.findViewById(R.id.text_view_hash_tags);
            text_view_comment = (TextView) itemView.findViewById(R.id.text_view_comment_num);
            date = (TextView) itemView.findViewById(R.id.text_written_date);
            idx = (TextView) itemView.findViewById(R.id.share_idx);
            idx.setVisibility(View.GONE);
            memo = (TextView) itemView.findViewById(R.id.share_memo);
            memo.setVisibility(View.GONE);
            text_view_like_num = (TextView) itemView.findViewById(R.id.text_view_like_num);
            // 클릭 리스너
            mListener = listener;
            image_codi.setOnClickListener(this);
            check_like.setOnClickListener(this);
            check_like.setOnCheckedChangeListener(this);
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_view_codi_share:
                    mListener.onRowClick(image_codi, getAdapterPosition());
                    break;
                case R.id.check_like:
                    mListener.onLikeClick(check_like, getAdapterPosition());
            }
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // 좋아요 버튼 눌렸을 때
            if (isChecked) {
                String like_string = text_view_like_num.getText().toString();
                int like_int = Integer.parseInt(like_string);
                like_int += 1;
                text_view_like_num.setText(String.valueOf(like_int));
            } else {
                // 좋아요 버튼 한번 더 눌렀을 때
                String like_string = text_view_like_num.getText().toString();
                int like_int = Integer.parseInt(like_string);
                like_int -= 1;
                text_view_like_num.setText(String.valueOf(like_int));
            }
        }
    }
}


