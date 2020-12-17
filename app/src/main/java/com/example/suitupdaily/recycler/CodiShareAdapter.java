package com.example.suitupdaily.recycler;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.suitupdaily.R;
import com.example.suitupdaily.ResponsePOJO;
import com.example.suitupdaily.TimeConverter;

import java.util.ArrayList;
import java.util.List;

public class CodiShareAdapter extends RecyclerView.Adapter<CodiShareAdapter.ShareViewHolder> {

    List<ResponsePOJO> clothList;
    private Context context;
    private CodiShareAdapter.ShareViewClickListener mListener;
    private TimeConverter timeConverter;

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
    public void onBindViewHolder(@NonNull CodiShareAdapter.ShareViewHolder holder, int position) {

        holder.idx.setText(clothList.get(position).getIdx());
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
        timeConverter = new TimeConverter();
        String converted_date = TimeConverter.CreateDataWithCheck(pure_date);
        // 변환된 date 를 holder 에 담는다.
        holder.date.setText(converted_date);

        holder.text_view_hash_tags.setText(clothList.get(position).getTags());
        holder.memo.setText(clothList.get(position).getMemo());

        // int를 String 으로
        holder.text_view_like_num.setText(String.valueOf(clothList.get(position).getLike()));

        // 이미지 데이터에 문제생겼을 경우 표시될 대체 이미지.
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.ic_clothes_hanger);
        requestOptions.error(R.drawable.ic_baseline_accessibility_24);

        // 서버에서 받아온 이미지 데이터 출력.
        Glide.with(context)
                .load(clothList.get(position).getPicture())
                .apply(requestOptions)
                .into(holder.image_codi);
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

    public static class ShareViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        private CodiShareAdapter.ShareViewClickListener mListener = null;
        private ImageView image_codi, image_user_profile, image_like, image_like_orange, image_comment;
        private TextView text_view_user_name, text_view_hash_tags, text_view_like, text_view_comment, text_view_date, text_view_like_num;
        private TextView idx, hash_tags, memo, date;
        private LinearLayout mRowContainer;
        private Button btn_like;
        private CheckBox check_like;
        private Thread thread;
        private Boolean isThread;
        private int like_count;
//        private ArrayList<ResponsePOJO> clothList;

        // 리사이클러 뷰 아이템에 무엇을 담을 건지 선언
        public ShareViewHolder(@NonNull View itemView, ShareViewClickListener listener) {
            super(itemView);
            // xml 연결
            mRowContainer = (LinearLayout) itemView.findViewById(R.id.share_item_view);
            image_codi = (ImageView) itemView.findViewById(R.id.img_view_codi_share);
            image_user_profile = (ImageView) itemView.findViewById(R.id.img_user_profile);
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
//            check_like.setOnClickListener(this);
            check_like.setOnCheckedChangeListener(this);

//            clothList = new ArrayList<ResponsePOJO> ();

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
            // 좋아요 버튼 클릭 되면,
            // 일단 현재 클릭된 게시글 번호를 가져와야 함.

            int position = getLayoutPosition();

//            Log.d("좋아요: ", isChecked + "/" + idx_num + "/");

            // 좋아요 버튼 눌렸을 때
            if (isChecked) {
                // 스레드 실행 -> 서버에 보낸다, 사용자 id(게시글 table에 좋아요 누른 사람 이름을 배열에 담는다.), 게시글 번호, 좋아요 카운트 수++
                // 서버 응답 받으면 좋아요 카운트 수 수정.

                // 다음에 view 생성 할 때
                // 게시글 table 에서 좋아요 리스트에 내 id가 들어가 있다면
                // 아니면 회원 정보 table에 내가 좋아요 누른 게시글의 index 배열이 있다면
                // 배열의 index 들을 isChecked true 처리한다.
                mListener.onLikeClick(check_like, getAdapterPosition());
                like_count += 1;
                text_view_like_num.setText(String.valueOf(like_count));
            } else {
                // 좋아요 버튼 한번 더 눌렀을 때
                // 스레드 실행
                mListener.onLikeClick(check_like, getAdapterPosition());
                like_count -= 1;
                text_view_like_num.setText(String.valueOf(like_count));
            }
        }


    }



}


