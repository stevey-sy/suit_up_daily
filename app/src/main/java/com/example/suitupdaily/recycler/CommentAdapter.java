package com.example.suitupdaily.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.suitupdaily.R;
import com.example.suitupdaily.ResponsePOJO;
import com.example.suitupdaily.TimeConverter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter <CommentAdapter.CommentViewHolder>{

    List<ResponsePOJO> commentList;
    private Context context;
    private CommentAdapter.CommentViewClickListener mListener;

    public CommentAdapter (List<ResponsePOJO> list, Context context, CommentAdapter.CommentViewClickListener listener) {
        this.commentList = list;
        this.context = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_comment, parent, false);
        return new CommentAdapter.CommentViewHolder(mView, mListener);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        // 서버에서 받아온 데이터를 view 에 세팅.
        Log.d("유저 닉네임: ", commentList.get(position).getUserNick());
        holder.tv_user_nick.setText(commentList.get(position).getUserNick());
        holder.tv_reply_content.setText(commentList.get(position).getContent());
        // 서버에서 받아온 date
        String date_from_server = commentList.get(position).getDate();
        // 불순물 제거
        String replace_dash = date_from_server.replace("-","");
        String replace_time = replace_dash.replace(":", "");
        String pure_date = replace_time.replace(" ", "");
        // timeConverter 클래스를 선언, 메소드 사용.
        // 현재 시간 기준으로 얼마나 지난 포스트인지 date 를 변환함.
        String converted_date = TimeConverter.CreateDataWithCheck(pure_date);
        holder.tv_reply_date.setText(converted_date);
        // 이미지 데이터에 문제생겼을 경우 표시될 대체 이미지.
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.ic_clothes_hanger);
        requestOptions.error(R.drawable.ic_baseline_accessibility_24);
        // 서버에서 받아온 프로필 이미지 출력
        Glide.with(context)
                .load(commentList.get(position).getImgURL())
                .apply(requestOptions)
                .circleCrop()
                .into(holder.profile_img);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public interface CommentViewClickListener {
        void onRowClick(View view, int position);
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CommentAdapter.CommentViewClickListener mListener;
        private CircleImageView profile_img;
        private TextView tv_user_nick, tv_reply_content, tv_reply_date;
        private ImageView iv_reply_menu;

        public CommentViewHolder(@NonNull View itemView, CommentViewClickListener listener) {
            super(itemView);
            // xml 연결
            profile_img = (CircleImageView) itemView.findViewById(R.id.civ_reply_profile_pic);
            tv_user_nick = (TextView) itemView.findViewById(R.id.tv_reply_nick);
            tv_reply_content = (TextView) itemView.findViewById(R.id.tv_reply_content);
            tv_reply_date = (TextView) itemView.findViewById(R.id.tv_reply_date);
            iv_reply_menu = (ImageView) itemView.findViewById(R.id.iv_reply_menu);
            // 클릭 리스너
            mListener = listener;
            iv_reply_menu.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.img_view_codi_share) {
                mListener.onRowClick(iv_reply_menu, getAdapterPosition());
            }
        }
    }
}
