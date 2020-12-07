package com.example.suitupdaily.listview;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.suitupdaily.R;

import java.util.ArrayList;

public class FilterSeasonAdapter extends BaseAdapter {

    private ArrayList<SeasonListViewItem> seasonItemList = new ArrayList<SeasonListViewItem>();

    public FilterSeasonAdapter() {

    }

    @Override
    public int getCount() {
        return seasonItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return seasonItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(String text) {
        SeasonListViewItem item = new SeasonListViewItem();

        item.setText(text);

        seasonItemList.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_season, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.text_item_season);
        SeasonListViewItem seasonListViewItem = seasonItemList.get(position);
        textView.setText(seasonListViewItem.getText());

        return convertView;
    }
}
