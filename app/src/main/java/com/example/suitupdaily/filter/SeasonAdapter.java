package com.example.suitupdaily.filter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitupdaily.R;

import java.util.ArrayList;

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder> {

    Activity activity;

    Context context;
    ArrayList<String> arrayList;
    MainViewModel mainViewModel;
    boolean isEnable = false;
    boolean isSelectAll = false;
    ArrayList<String> selectList = new ArrayList<>();

    public SeasonAdapter(Context context, ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SeasonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_season, parent, false);
        // Initialize view model
        mainViewModel = ViewModelProviders.of((FragmentActivity) context).get(MainViewModel.class);

        return new SeasonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SeasonViewHolder holder, int position) {

        //Set text on text view
        holder.textView.setText(arrayList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEnable) {
                    // when action mode is not enable
                    // Initiate action mode
                    ActionMode.Callback callback = new ActionMode.Callback() {

                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            // Initialize menu inflater

                            return false;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            //When action mode is prepare
                            // Set isEnable true

                            isEnable = true;
                            // Create method
                            ClickItem(holder);

                            // Set observeer on get text method
                            mainViewModel.getText().observe((LifecycleOwner)context, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    //When text change
                                    // Set text on action mode title


                                }
                            });

                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            return false;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {

                        }
                    };


                }
            }
        });

    }

    private void ClickItem(SeasonViewHolder holder) {

        String s = arrayList.get(holder.getAdapterPosition());

        if (holder.ivCheckBox.getVisibility() == View.GONE) {
            holder.ivCheckBox.setVisibility(View.VISIBLE);

            holder.itemView.setBackgroundColor(Color.LTGRAY);

            selectList.add(s);
        } else {
            // When item selected
            holder.ivCheckBox.setVisibility(View.GONE);
            // Set Background Color
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            // REmove
            selectList.remove(s);
        }

        mainViewModel.setText(String.valueOf(selectList.size()));



    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class SeasonViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView ivCheckBox;

        public SeasonViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view_season);
            ivCheckBox = itemView.findViewById(R.id.iv_check_box);

        }
    }

}
