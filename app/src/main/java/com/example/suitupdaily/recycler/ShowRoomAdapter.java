package com.example.suitupdaily.recycler;

import android.annotation.SuppressLint;
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

public class ShowRoomAdapter extends RecyclerView.Adapter <ShowRoomAdapter.ShowViewHolder> {

    List<ResponsePOJO> clothList;
    private Context context;
    private ShowViewClickListener mListener;

    public ShowRoomAdapter(List<ResponsePOJO> list, Context context, ShowViewClickListener listener) {
        this.clothList = list;
        this.context = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_room_item, parent, false);
        return new ShowViewHolder(mView, mListener);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull ShowRoomAdapter.ShowViewHolder holder, int position) {

        holder.idx.setText(clothList.get(position).getIdx());
//        holder.idx.setText(clothList.get(position).getPicture());
        Log.d("코디 이미지: ", clothList.get(position).getPicture());

        holder.date.setText(clothList.get(position).getDate());
        holder.hash_tags.setText(clothList.get(position).getTags());
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
                .into(holder.mPicture);
    }

    @Override
    public int getItemCount() {
        return clothList.size();
    }

    public interface ShowViewClickListener {
        void onRowClick(View view, int position);
    }

    public static class ShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ShowViewClickListener mListener = null;
        private TextView idx, hash_tags, memo, date;
        private ImageView mPicture;
        private LinearLayout mRowContainer;

        public ShowViewHolder(@NonNull View itemView, ShowViewClickListener listener) {
            super(itemView);

            mPicture = itemView.findViewById(R.id.card_iv);
            // idx
            idx = itemView.findViewById(R.id.tv_codi_idx);
            idx.setVisibility(View.GONE);

            hash_tags = itemView.findViewById(R.id.text_view_show_tags);
            memo = itemView.findViewById(R.id.text_view_show_memo);
            memo.setVisibility(View.GONE);
            date = itemView.findViewById(R.id.text_view_codi_date);

            mRowContainer = itemView.findViewById(R.id.codi_show_view);
            mListener = listener;
            mRowContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.codi_show_view:
                    mListener.onRowClick(mRowContainer, getAdapterPosition());
                    break;
            }

        }
    }
}
