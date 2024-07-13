package com.example.android_ex2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;

import com.example.android_ex2.Enums.Mode;
import com.example.android_ex2.Enums.Speed;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;


public class WelcomeActivity extends AppCompatActivity {


    private AppCompatTextView welcome_LBL_title;
    private AppCompatImageView welcome_IMG_dwarf;
    private AppCompatTextView welcome_LBL_speed;
    private AppCompatToggleButton toggle_speed;
    private AppCompatTextView welcome_LBL_mode;
    private AppCompatToggleButton toggle_mode;
    private ExtendedFloatingActionButton welcome_BTN_record;
    private ExtendedFloatingActionButton welcome_BTN_play;
    private Speed speed = Speed.SLOW;
    private Mode mode = Mode.BUTTONS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        findViews();
        initViews();
    }

    private void initViews() {

        toggle_mode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mode = Mode.SENSORS;
            } else {
                mode = Mode.BUTTONS;
            }
        });

        toggle_speed.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                speed = Speed.FAST;
            } else {
                speed = Speed.SLOW;
            }
        });

        welcome_BTN_play.setOnClickListener(v -> changeToMainActivity());
        welcome_BTN_record.setOnClickListener(v -> changeToRecordActivity());


    }


    private void changeToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.KEY_MODE, mode);
        intent.putExtra(MainActivity.KEY_SPEED, speed);
        startActivity(intent);
        finish();
    }

    private void changeToRecordActivity() {
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
        finish();
    }

    private void findViews() {
        welcome_LBL_title = findViewById(R.id.welcome_LBL_title);
        welcome_IMG_dwarf = findViewById(R.id.welcome_IMG_dwarf);
        welcome_LBL_speed = findViewById(R.id.welcome_LBL_speed);
        toggle_speed = findViewById(R.id.toggle_speed);
        welcome_LBL_mode = findViewById(R.id.welcome_LBL_mode);
        toggle_mode = findViewById(R.id.toggle_mode);
        welcome_BTN_record = findViewById(R.id.welcome_BTN_record);
        welcome_BTN_play = findViewById(R.id.welcome_BTN_play);
    }
}
