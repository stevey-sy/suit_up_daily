package com.example.suitupdaily.recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

public class CodiShareAdapter extends RecyclerView.Adapter<CodiShareAdapter.ShareViewHolder> {

    List<ResponsePOJO> clothList;
    private Context context;
    private CodiShareAdapter.ShareViewClickListener mListener;

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
//        holder.idx.setText(clothList.get(position).getPicture());
        Log.d("코디 이미지: ", clothList.get(position).getPicture());

        holder.date.setText(clothList.get(position).getDate());
        holder.text_view_hash_tags.setText(clothList.get(position).getTags());
        holder.memo.setText(clothList.get(position).getMemo());

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
    }

    public static class ShareViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CodiShareAdapter.ShareViewClickListener mListener = null;
        private ImageView image_codi, image_user_profile, image_like, image_like_orange, image_comment;
        private TextView text_view_user_name, text_view_hash_tags, text_view_like, text_view_comment, text_view_date;
        private TextView idx, hash_tags, memo, date;
        private LinearLayout mRowContainer;

        // 리사이클러 뷰 아이템에 무엇을 담을 건지 선언
        public ShareViewHolder(@NonNull View itemView, ShareViewClickListener listener) {
            super(itemView);
            // 클릭 리스너
            mListener = listener;
            // xml 연결
            mRowContainer = (LinearLayout) itemView.findViewById(R.id.share_item_view);
            image_codi = (ImageView) itemView.findViewById(R.id.img_view_codi_share);
            image_user_profile = (ImageView) itemView.findViewById(R.id.img_user_profile);
            image_like = (ImageView) itemView.findViewById(R.id.img_like);
            text_view_user_name = (TextView) itemView.findViewById(R.id.text_view_user_name);
            text_view_hash_tags = (TextView) itemView.findViewById(R.id.text_view_hash_tags);
            text_view_like = (TextView) itemView.findViewById(R.id.text_view_like_num);
            text_view_comment = (TextView) itemView.findViewById(R.id.text_view_comment_num);
            idx = (TextView) itemView.findViewById(R.id.share_idx);
            idx.setVisibility(View.GONE);
            memo = (TextView) itemView.findViewById(R.id.share_memo);
            memo.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.share_item_view:
                    mListener.onRowClick(mRowContainer, getAdapterPosition());
                    break;
            }
        }
    }

}


