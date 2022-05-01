package com.example.huntinggame_v2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.example.huntinggame_v2.CallBack_MapFocus;
import com.example.huntinggame_v2.R;
import com.example.huntinggame_v2.TopTenManager;
import com.example.huntinggame_v2.fragments.Fragment_List;
import com.example.huntinggame_v2.fragments.Fragment_Map;
import com.google.android.material.button.MaterialButton;

public class HighScoreActivity extends AppCompatActivity {
    private MaterialButton score_BTN_playAgain;
    private Fragment_List fragment_list;
    private Fragment_Map fragment_map;
    private double latitude;
    private double longitude;
    private TopTenManager topTenManager = new TopTenManager();
    private int newScore;
    private String gameType;
    private MediaPlayer highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        findViews();
        //get last score and location
        highScore = MediaPlayer.create(HighScoreActivity.this,R.raw.high_score);
        newScore = 0;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newScore = 0;
                latitude = 0;
                longitude = 0;
                gameType = "";
                score_BTN_playAgain.setText("Back To Menu");
            } else {
                newScore = extras.getInt("score");
                latitude = extras.getDouble("latitude");
                longitude = extras.getDouble("longitude");
                gameType = extras.getString("type");
            }
        } else {
            newScore = (int) savedInstanceState.getSerializable("score");
            latitude = (double) savedInstanceState.getSerializable("latitude");
            longitude = (double) savedInstanceState.getSerializable("longitude");
            gameType = (String) savedInstanceState.getSerializable("type");
        }
        Log.d("ccc", "new score" + String.valueOf(newScore));
        //check score
        if(topTenManager.isTopTen(newScore, latitude, longitude)){
            highScore.start();
        }

        //map
        fragment_map = new Fragment_Map();
        fragment_map.setList(topTenManager.getArray());
        getSupportFragmentManager().beginTransaction().add(R.id.score_FRM_map, fragment_map).commit();

        //list
        fragment_list = new Fragment_List();
        fragment_list.setList(topTenManager.getArray());
        fragment_list.setCallBack_mapFocus(callBack_mapFocus);
        getSupportFragmentManager().beginTransaction().add(R.id.score_FRM_list, fragment_list).commit();
        Fragment_List fragment_list = new Fragment_List();

        //Play Again Button
        score_BTN_playAgain.setOnClickListener(view -> playAgainButtons());
    }

    private CallBack_MapFocus callBack_mapFocus = new CallBack_MapFocus() {
        @Override
        public void focusMap(double latitude, double longitude) {
            fragment_map.gotoLocation(latitude, longitude);
        }
    };

    private void playAgainButtons() {
        Intent intent;
        if (gameType.equals("")) {
            intent = new Intent(HighScoreActivity.this, StartActivity.class);
        } else {
            intent = new Intent(HighScoreActivity.this, MainActivity.class);
            intent.putExtra("type", gameType);
        }
        startActivity(intent);
        finish();
    }

    private void findViews() {
        score_BTN_playAgain = findViewById(R.id.score_BTN_playAgain);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ccc", "start");
    }

    @Override
    protected void onStop() {
        super.onStop();
        topTenManager.saveArray();
    }
}