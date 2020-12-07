package com.example.suitupdaily.grid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.suitupdaily.R;
import com.example.suitupdaily.ResponsePOJO;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {
    LayoutInflater inflater;
    List<ResponsePOJO> closetItems;

    public GridAdapter (LayoutInflater inflater, List<ResponsePOJO> closetItems) {
        this.inflater = inflater;
        this.closetItems = closetItems;
    }

    @Override
    public int getCount() {
        return closetItems.size();
    }

    @Override
    public Object getItem(int position) {
        return closetItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_layout, parent, false);
        }

//        Glide.with(context)
//                .load(pets.get(position).getPicture())
//                .apply(requestOptions)
//                .into(holder.mPicture);

        ImageView iv = convertView.findViewById(R.id.iv_gird_item);

        Glide.with(convertView).load(closetItems.get(position).getPicture()).into(iv);

        return convertView;
    }
}
