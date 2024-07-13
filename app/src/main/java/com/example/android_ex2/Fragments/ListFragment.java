package com.example.android_ex2.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_ex2.Adapters.RecordAdapter;
import com.example.android_ex2.Interfaces.Callback_ListItemClicked;
import com.example.android_ex2.Models.Record;
import com.example.android_ex2.Models.RecordList;
import com.example.android_ex2.R;
import com.example.android_ex2.Utillities.SharePreferencesManager;
import com.google.gson.Gson;


public class ListFragment extends Fragment {
    private RecyclerView main_LST_records;
    Callback_ListItemClicked callbackListItemClicked;
    RecordList recordList;
    public static final String KEY_RECORD = "KEY_RECORD";


    public ListFragment() {
        // Required empty public constructor
    }

    public void setCallbackListItemClicked(Callback_ListItemClicked callbackListItemClicked) {
        this.callbackListItemClicked = callbackListItemClicked;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(v);
        initViews();
        return v;
    }


    private void initViews() {
        Gson gson = new Gson();
        String recordListAsJson = SharePreferencesManager
                .getInstance()
                .getString(KEY_RECORD, "");
        recordList = gson.fromJson(recordListAsJson, RecordList.class);
        if (recordList == null) {
            recordList = new RecordList();
        }
        RecordAdapter recordAdapter = new RecordAdapter(recordList.getSortedRecords(), (lat, lon) -> {
            if (callbackListItemClicked != null) {
                callbackListItemClicked.listItemClicked(lat, lon);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        main_LST_records.setLayoutManager(linearLayoutManager);
        main_LST_records.setAdapter(recordAdapter);
    }

    private void itemClicked(double lat, double lon) {
        if (callbackListItemClicked != null)
            callbackListItemClicked.listItemClicked(lat, lon);
    }

    private void findViews(View v) {
        main_LST_records = v.findViewById(R.id.main_LST_records);
    }


}