package com.example.suitupdaily.recycler;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitupdaily.R;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    private ArrayList<String> itemList;
    private Context context;
    private View.OnClickListener onClickItem;
    private String[] color_list = { "#000000", "#FFFFFF", "#C0C0C0", "#F5F5DC", "#0000CD", "#FF0000"};
    private String[] color_names = {"블랙", "화이트", "그레이", "베이지", "블루", "레드"};

    public ColorAdapter(Context context, ArrayList<String> itemList, View.OnClickListener onClickItem) {
        this.context = context;
        this.itemList = itemList;
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.recycler_color_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorAdapter.ViewHolder holder, int position) {
        String item = itemList.get(position);

        holder.view_color.setBackgroundResource(R.drawable.color_list);
        holder.view_color.setBackgroundColor(Color.parseColor(color_list[position]));
        holder.text_view.setText(color_names[position]);
        holder.text_view.setTag(item);
        holder.color_item.setOnClickListener(onClickItem);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text_view;
        public View view_color;
        public LinearLayout color_item;
        public ViewHolder(View itemView) {
            super(itemView);
            color_item = itemView.findViewById(R.id.color_item);
            view_color = itemView.findViewById(R.id.view_color);
            text_view = itemView.findViewById(R.id.text_color_item);

        }
    }
}
