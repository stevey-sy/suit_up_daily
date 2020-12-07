package com.example.suitupdaily;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.suitupdaily.grid.ColorGridAdapter;

import java.util.ArrayList;

public class ColorDialog extends Dialog {

    private Activity activity;
    private String title;
    private View.OnClickListener checkBtListener;
    private ColorGridAdapter adapter;
    private TextView dialogTitle;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_color_list);

        gridView.setAdapter(adapter);

    }

    public ColorDialog(Activity activity, ColorGridAdapter adapter, View.OnClickListener checkBtListener) {
        super(activity, android.R.style.Theme_Translucent_NoTitleBar);
        this.activity = activity;
        this.adapter = adapter;
        this.checkBtListener = checkBtListener;
    }


}
