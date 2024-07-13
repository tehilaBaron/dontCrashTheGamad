package com.example.android_ex2;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.android_ex2.Enums.Mode;
import com.example.android_ex2.Enums.Speed;
import com.example.android_ex2.Interfaces.MoveCallback;
import com.example.android_ex2.Models.Record;
import com.example.android_ex2.Models.RecordList;
import com.example.android_ex2.Utillities.LocationHelper;
import com.example.android_ex2.Utillities.MoveDetector;
import com.example.android_ex2.Utillities.SharePreferencesManager;
import com.example.android_ex2.Utillities.SignalManager;
import com.example.android_ex2.Utillities.SoundPlayer;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_MODE = "KEY_MODE";
    public static final String KEY_SPEED = "KEY_SPEED";
    public static final String KEY_SCORE = "KEY_SCORE";
    public static final String KEY_RECORD = "KEY_RECORD";
    private LocationHelper locationHelper;
    private long DELAY = 1000L;
    private ExtendedFloatingActionButton main_FAB_left;
    private ExtendedFloatingActionButton main_FAB_right;
    private AppCompatImageView[] main_IMG_dwarfs;
    private AppCompatImageView[][] main_IMG_hammers;
    private AppCompatImageView[][] main_IMG_coins;
    private AppCompatImageView[] main_IMG_hearts;
    private MaterialTextView main_LBL_score;
    private boolean timerOn = false;
    private long startTime;
    private Timer timer;
    private GameManeger gameManeger;
    private int score = 0;
    private MoveDetector moveDetector;
    public SoundPlayer soundPlayer;
    private Location location;

    private Mode mode = Mode.BUTTONS;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
        gameManeger = new GameManeger(main_IMG_hearts.length, main_IMG_hammers.length, main_IMG_hammers[0].length, score);
        locationHelper = new LocationHelper(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        startTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
        if (mode == Mode.SENSORS)
            moveDetector.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
        if (mode == Mode.SENSORS)
            moveDetector.stop();
    }

    private void initViews() {
        main_LBL_score.setText(String.valueOf(score));
        Intent previousIntent = getIntent();
        mode = (Mode) previousIntent.getSerializableExtra(KEY_MODE);
        Speed speed = (Speed) previousIntent.getSerializableExtra(KEY_SPEED);
        if (mode == Mode.SENSORS) {
            main_FAB_left.setVisibility(View.INVISIBLE);
            main_FAB_right.setVisibility(View.INVISIBLE);
            initMoveDetector();
        }
        main_FAB_left.setOnClickListener(v -> moveDwarfClicked(1));
        main_FAB_right.setOnClickListener(v -> moveDwarfClicked(-1));

        if (speed == Speed.SLOW) {
            DELAY = 1000L;
        } else {
            DELAY = 500L;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void initMoveDetector() {
        moveDetector = new MoveDetector(this,
                new MoveCallback() {
                    @Override
                    public void moveX() {
                        moveDwarfClicked(1);
                    }

                    @Override
                    public void moveMinusX() {
                        moveDwarfClicked(-1);
                    }
                }
        );
    }

    private void moveDwarfClicked(int number) {
        gameManeger.moveDwarf(number);
        refreshUI();
    }

    private void moveDwarfUI() {
        for (int i = 0; i < main_IMG_dwarfs.length; i++) {
            if (gameManeger.getDwarfArray()[i]) {
                main_IMG_dwarfs[i].setVisibility(View.VISIBLE);
            } else {
                main_IMG_dwarfs[i].setVisibility(View.INVISIBLE);
            }
        }

    }

    private void updateScore() {
        boolean hasCoinCollition = gameManeger.checkCoinCollition();
        if (hasCoinCollition) {
            main_LBL_score.setText(String.valueOf(gameManeger.getScore()));
        }
    }

    private void showLifeUI() {
        boolean hasCollition = gameManeger.checkCollition();
        if (hasCollition) {
            soundPlayer = new SoundPlayer(this);
            soundPlayer.playSound(R.raw.bang);
            toastAndVibrate();
            main_IMG_hearts[gameManeger.getLife()].setVisibility(View.INVISIBLE);
            soundPlayer.stopSound();
            if (gameManeger.getLife() == 0) {
                saveRecord();
                changeToRecordActivity();
            }
        }
    }

    private void saveRecord() {
        Gson gson = new Gson();
        //getting data
        String recordListAsJson = SharePreferencesManager
                .getInstance()
                .getString(KEY_RECORD, "");
        RecordList recordList = gson.fromJson(recordListAsJson, RecordList.class);
        if (recordList == null) {
            recordList = new RecordList();
        }
        Record newRecord = new Record(gameManeger.getScore(), "tehila", locationHelper.getLatitude(), locationHelper.getLongitude());
        recordList.addRecord(newRecord);
        String newRecordListAsJson = gson.toJson(recordList);
        //saving data
        SharePreferencesManager
                .getInstance()
                .putString(KEY_RECORD, newRecordListAsJson);
    }

    private void changeToRecordActivity() {
        Intent intent = new Intent(this, RecordActivity.class);
        intent.putExtra(MainActivity.KEY_SCORE, gameManeger.getScore());
        startActivity(intent);
        finish();
    }

    private void updateHummerMatrix() {
        gameManeger.updateMatrix();
        refreshUI();
    }


    private void startTimer() {
        if (!timerOn) {
            startTime = System.currentTimeMillis();
            timerOn = true;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> updateHummerMatrix());
                }
            }, 0L, DELAY);
        }
    }

    private void updateHammersUI() {
        for (int i = 0; i < gameManeger.getRowSize(); i++) {
            for (int j = 0; j < gameManeger.getColSize(); j++) {
                if (gameManeger.getHammerMatrix()[i][j]) {
                    main_IMG_hammers[i][j].setVisibility(View.VISIBLE);
                } else {
                    main_IMG_hammers[i][j].setVisibility(View.INVISIBLE);

                }
            }
        }

    }

    private void updateCoinsUI() {
        for (int i = 0; i < gameManeger.getRowSize(); i++) {
            for (int j = 0; j < gameManeger.getColSize(); j++) {
                if (gameManeger.getCoinMatrix()[i][j]) {
                    main_IMG_coins[i][j].setVisibility(View.VISIBLE);
                } else {
                    main_IMG_coins[i][j].setVisibility(View.INVISIBLE);

                }
            }
        }

    }

    private void refreshUI() {
        moveDwarfUI();
        showLifeUI();
        updateScore();
        updateHammersUI();
        updateCoinsUI();
    }


    private void stopTimer() {
        timerOn = false;
        Log.d("stopTimer", "stopTimer: Timer Stopped");
        timer.cancel();
    }

    private void toastAndVibrate() {
        SignalManager.getInstance().vibrate(500);
        SignalManager.getInstance().toast("Oops! You lost a life");
    }

    private void findViews() {
        main_LBL_score = findViewById(R.id.main_LBL_score);
        main_FAB_left = findViewById(R.id.main_FAB_left);
        main_FAB_right = findViewById(R.id.main_FAB_right);
        main_IMG_hearts = new AppCompatImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };
        main_IMG_dwarfs = new AppCompatImageView[]{
                findViewById(R.id.main_IMG_dwarf1),
                findViewById(R.id.main_IMG_dwarf2),
                findViewById(R.id.main_IMG_dwarf3),
                findViewById(R.id.main_IMG_dwarf4),
                findViewById(R.id.main_IMG_dwarf5)

        };
        main_IMG_hammers = new AppCompatImageView[][]{
                {findViewById(R.id.main_IMG_hammer11),
                        findViewById(R.id.main_IMG_hammer12),
                        findViewById(R.id.main_IMG_hammer13),
                        findViewById(R.id.main_IMG_hammer14),
                        findViewById(R.id.main_IMG_hammer15)
                },
                {findViewById(R.id.main_IMG_hammer21),
                        findViewById(R.id.main_IMG_hammer22),
                        findViewById(R.id.main_IMG_hammer23),
                        findViewById(R.id.main_IMG_hammer24),
                        findViewById(R.id.main_IMG_hammer25)
                },
                {findViewById(R.id.main_IMG_hammer31),
                        findViewById(R.id.main_IMG_hammer32),
                        findViewById(R.id.main_IMG_hammer33),
                        findViewById(R.id.main_IMG_hammer34),
                        findViewById(R.id.main_IMG_hammer35)
                },
                {findViewById(R.id.main_IMG_hammer41),
                        findViewById(R.id.main_IMG_hammer42),
                        findViewById(R.id.main_IMG_hammer43),
                        findViewById(R.id.main_IMG_hammer44),
                        findViewById(R.id.main_IMG_hammer45)
                },
                {findViewById(R.id.main_IMG_hammer51),
                        findViewById(R.id.main_IMG_hammer52),
                        findViewById(R.id.main_IMG_hammer53),
                        findViewById(R.id.main_IMG_hammer54),
                        findViewById(R.id.main_IMG_hammer55)
                },
                {findViewById(R.id.main_IMG_hammer61),
                        findViewById(R.id.main_IMG_hammer62),
                        findViewById(R.id.main_IMG_hammer63),
                        findViewById(R.id.main_IMG_hammer64),
                        findViewById(R.id.main_IMG_hammer65)
                },
                {findViewById(R.id.main_IMG_hammer71),
                        findViewById(R.id.main_IMG_hammer72),
                        findViewById(R.id.main_IMG_hammer73),
                        findViewById(R.id.main_IMG_hammer74),
                        findViewById(R.id.main_IMG_hammer75)
                },
                {findViewById(R.id.main_IMG_hammer81),
                        findViewById(R.id.main_IMG_hammer82),
                        findViewById(R.id.main_IMG_hammer83),
                        findViewById(R.id.main_IMG_hammer84),
                        findViewById(R.id.main_IMG_hammer85)
                }

        };
        main_IMG_coins = new AppCompatImageView[][]{
                {findViewById(R.id.main_IMG_coin11),
                        findViewById(R.id.main_IMG_coin12),
                        findViewById(R.id.main_IMG_coin13),
                        findViewById(R.id.main_IMG_coin14),
                        findViewById(R.id.main_IMG_coin15)
                },
                {findViewById(R.id.main_IMG_coin21),
                        findViewById(R.id.main_IMG_coin22),
                        findViewById(R.id.main_IMG_coin23),
                        findViewById(R.id.main_IMG_coin24),
                        findViewById(R.id.main_IMG_coin25)
                },
                {findViewById(R.id.main_IMG_coin31),
                        findViewById(R.id.main_IMG_coin32),
                        findViewById(R.id.main_IMG_coin33),
                        findViewById(R.id.main_IMG_coin34),
                        findViewById(R.id.main_IMG_coin35)
                },
                {findViewById(R.id.main_IMG_coin41),
                        findViewById(R.id.main_IMG_coin42),
                        findViewById(R.id.main_IMG_coin43),
                        findViewById(R.id.main_IMG_coin44),
                        findViewById(R.id.main_IMG_coin45)
                },
                {findViewById(R.id.main_IMG_coin51),
                        findViewById(R.id.main_IMG_coin52),
                        findViewById(R.id.main_IMG_coin53),
                        findViewById(R.id.main_IMG_coin54),
                        findViewById(R.id.main_IMG_coin55)
                },
                {findViewById(R.id.main_IMG_coin61),
                        findViewById(R.id.main_IMG_coin62),
                        findViewById(R.id.main_IMG_coin63),
                        findViewById(R.id.main_IMG_coin64),
                        findViewById(R.id.main_IMG_coin65)
                },
                {findViewById(R.id.main_IMG_coin71),
                        findViewById(R.id.main_IMG_coin72),
                        findViewById(R.id.main_IMG_coin73),
                        findViewById(R.id.main_IMG_coin74),
                        findViewById(R.id.main_IMG_coin75)
                },
                {findViewById(R.id.main_IMG_coin81),
                        findViewById(R.id.main_IMG_coin82),
                        findViewById(R.id.main_IMG_coin83),
                        findViewById(R.id.main_IMG_coin84),
                        findViewById(R.id.main_IMG_coin85)
                }

        };
    }
}
