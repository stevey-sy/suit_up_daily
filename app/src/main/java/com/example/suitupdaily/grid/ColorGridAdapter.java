package com.example.suitupdaily.grid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.suitupdaily.ColorGridItem;
import com.example.suitupdaily.ImageEditActivity;
import com.example.suitupdaily.R;

import java.util.ArrayList;

public class ColorGridAdapter extends BaseAdapter {

    ArrayList<ColorGridItem> items = new ArrayList<ColorGridItem>();
    private Activity activity;
    Context context;
    String[] color_name;
    String[] color_code;

    public ColorGridAdapter(Activity activity, String[] color_name, String[] color_code) {
        this.context= activity;
        this.color_name = color_name;
        this.color_code = color_code;

    }

    public void addItem(String color_name) {
        ColorGridItem item = new ColorGridItem(color_name);
        item.setColor_name(color_name);
        items.add(item);
    }

    @Override
    public int getCount() {
        return color_name.length;
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.color_grid_item, parent, false);

            TextView btn_color = convertView.findViewById(R.id.btn_color);
            TextView text_color = convertView.findViewById(R.id.text_color_name);
            btn_color.setBackgroundColor(Color.parseColor(color_code[position]));
            text_color.setText(color_name[position]);

//            return convertView;

        }



//        context = parent.getContext();
//        ColorGridItem colorGridItem = items.get(position);
//
//        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.color_grid_item, parent, false);
//        }
//
//        Button btn_color = convertView.findViewById(R.id.btn_color);
//        TextView text_color = convertView.findViewById(R.id.text_color_name);
//
//        text_color.setText(colorGridItem.getColor_name());

        return convertView;
    }

    private class GridViewHolder {
        TextView name;
    }
}
