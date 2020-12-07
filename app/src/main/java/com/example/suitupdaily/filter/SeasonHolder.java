package com.example.suitupdaily.filter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitupdaily.R;

public class SeasonHolder extends RecyclerView.ViewHolder {

    TextView season;
    CheckBox chk;

    public SeasonHolder(@NonNull View itemView) {
        super(itemView);

        season = itemView.findViewById(R.id.text_item_season);
        chk = itemView.findViewById(R.id.check_box_season);
    }
}
