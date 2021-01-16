package com.example.suitupdaily.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.suitupdaily.api.ResponsePOJO;

import java.util.List;

public class MyClosetAdapter extends RecyclerView.Adapter <MyClosetAdapter.CustomViewHolder> {

    List<ResponsePOJO> clothList;
    private Context context;
    private RecyclerViewClickListener mListener;

    public MyClosetAdapter(List<ResponsePOJO> list, Context context, RecyclerViewClickListener listener) {
        this.clothList = list;
        this.context = context;
        this.mListener = listener;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout, parent, false);
        return new CustomViewHolder(view, mListener);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {

        // 서버에서 받아온 데이터를 view에 표시한다.
        holder.mCategory.setText(clothList.get(position).getCategory());
        holder.idx.setText(clothList.get(position).getIdx());
        holder.mColor.setText(clothList.get(position).getColor());
        holder.mDate.setText(clothList.get(position).getDate());
        holder.mSeason.setText(clothList.get(position).getSeason());
        holder.mType.setText(clothList.get(position).getType());
        holder.mFit.setText(clothList.get(position).getType());

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

    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecyclerViewClickListener mListener = null;
        private TextView mCategory, mDate, mColor, mSeason, idx, mType, mFit;
        private ImageView mPicture;
        private LinearLayout mRowContainer;

        public CustomViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            mPicture = itemView.findViewById(R.id.iv_gird_item);

            mCategory = itemView.findViewById(R.id.text_category);
            mCategory.setVisibility(View.GONE);

            idx = itemView.findViewById(R.id.text_idx);
            idx.setVisibility(View.GONE);

            mColor = itemView.findViewById(R.id.text_load_color);
            mColor.setVisibility(View.GONE);

            mDate = itemView.findViewById(R.id.text_load_date);
            mDate.setVisibility(View.GONE);

            mSeason = itemView.findViewById(R.id.text_load_season);
            mSeason.setVisibility(View.GONE);

            mType = itemView.findViewById(R.id.text_type);
            mType.setVisibility(View.GONE);

            mFit = itemView.findViewById(R.id.text_fit_info);
            mFit.setVisibility(View.GONE);

            mRowContainer = itemView.findViewById(R.id.row_container);

            mListener = listener;
            mRowContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.row_container:
                    mListener.onRowClick(mRowContainer, getAdapterPosition());
                    break;
            }
        }
    }

        public interface RecyclerViewClickListener {
            void onRowClick(View view, int position);
    }
}
