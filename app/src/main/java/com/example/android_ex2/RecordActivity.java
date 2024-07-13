package com.example.android_ex2;


import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.android_ex2.Fragments.ListFragment;
import com.example.android_ex2.Fragments.MapFragment;
import com.example.android_ex2.Interfaces.Callback_ListItemClicked;

public class RecordActivity extends AppCompatActivity implements Callback_ListItemClicked {
    private FrameLayout main_FRAME_list;
    private FrameLayout main_FRAME_map;

    private AppCompatImageButton back_BTN;
    private ListFragment listFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        findViews();
        initViews();

    }

    private void initViews() {
        back_BTN.setOnClickListener(v -> changeToWelcomeActivity());
        listFragment = new ListFragment();
        listFragment.setCallbackListItemClicked(this);
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_list, listFragment).commit();
        mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_map, mapFragment).commit();

    }

    private void changeToWelcomeActivity() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void findViews() {
        main_FRAME_list = findViewById(R.id.main_FRAME_list);
        main_FRAME_map = findViewById(R.id.main_FRAME_map);
        back_BTN = findViewById(R.id.back_BTN);

    }

    @Override
    public void listItemClicked(double lat, double lon) {
        if (mapFragment != null) {
            mapFragment.updateLocation(lat, lon);
        }
    }

}
