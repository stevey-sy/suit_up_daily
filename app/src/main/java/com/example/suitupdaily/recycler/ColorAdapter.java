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
import com.example.suitupdaily.ResponsePOJO;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    private View.OnClickListener onClickItem;
    private ArrayList<String> itemList;
    private List<String> colors;
    private Context context;

    private String[] color_list = { "#000000", "#FFFFFF", "#C0C0C0", "#F5F5DC", "#0000CD", "#FF0000", "#006400", "#BDB76B", "#FFA500", "#8B4513", "#87CEEB", "#000080", "#EE82EE", "#8A2BE2"};
    private String[] color_names = {"블랙", "화이트", "그레이", "베이지", "블루", "레드", "그린", "카키", "오렌지", "브라운", "SkyBlue", "Navy", "핑크", "Purple"};

    public ColorAdapter(Context context, ArrayList<String> itemList, View.OnClickListener onClickItem) {
        this.context = context;
        this.itemList = itemList;
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public ColorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recycler_color_item, parent, false);
        return new ColorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorAdapter.ViewHolder holder, int position) {
        String item = itemList.get(position);
        holder.text_view.setText(item);
        holder.view_color.setBackgroundColor(Color.parseColor(color_list[position]));
        holder.color_item.setTag(item);
        holder.color_item.setOnClickListener(onClickItem);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text_view;
        public View view_color;
        public LinearLayout color_item;
        public ViewHolder(View itemView) {
            super(itemView);
            color_item = (LinearLayout) itemView.findViewById(R.id.color_item_layout);
            view_color = itemView.findViewById(R.id.view_color);
            text_view = itemView.findViewById(R.id.text_color_item);
        }
    }
}
