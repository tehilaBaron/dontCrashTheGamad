package com.example.android_ex2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_ex2.Interfaces.Callback_ListItemClicked;
import com.example.android_ex2.Models.Record;
import com.example.android_ex2.Models.RecordList;
import com.example.android_ex2.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {
    private final ArrayList<Record> records;
    private final Callback_ListItemClicked recordCallback;


    public RecordAdapter(ArrayList<Record> records, Callback_ListItemClicked recordCallback) {
        this.records = records;
        this.recordCallback = recordCallback;
    }


    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_list_item, parent, false);
        return new RecordViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = getItem(position);
        holder.record_LBL_name.setText(record.getName());
        holder.record_LBL_score.setText(String.valueOf(record.getPoints()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordCallback.listItemClicked(record.getLat(), record.getLon());
            }
        });
    }


    @Override
    public int getItemCount() {
        return records == null ? 0 : records.size();
    }

    private Record getItem(int position) {
        return records.get(position);
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView record_LBL_name;
        private final MaterialTextView record_LBL_score;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            record_LBL_name = itemView.findViewById(R.id.record_LBL_name);
            record_LBL_score = itemView.findViewById(R.id.record_LBL_score);
        }


    }
}
