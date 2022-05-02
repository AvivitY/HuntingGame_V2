package com.example.huntinggame_v2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.huntinggame_v2.R;
import com.google.android.material.button.MaterialButton;

public class StartActivity extends AppCompatActivity {
    private MaterialButton start_BTN_startButton;
    private MaterialButton start_BTN_startSensor;
    private MaterialButton start_BTN_highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViews();
        start_BTN_startButton.setOnClickListener(view -> buttonsGame());
        start_BTN_startSensor.setOnClickListener(view -> sensorGame());
        start_BTN_highScore.setOnClickListener(view -> highScore());
    }

    private void findViews() {
        start_BTN_startButton = findViewById(R.id.start_BTN_startButton);
        start_BTN_startSensor = findViewById(R.id.start_BTN_startSensor);
        start_BTN_highScore = findViewById(R.id.start_BTN_highScore);
    }

    private void highScore() {
        Intent game = new Intent(StartActivity.this, HighScoreActivity.class);
        startActivity(game);
        finish();
    }

    private void sensorGame() {
        Intent game = new Intent(StartActivity.this, MainActivity.class);
        game.putExtra("type","sensor");
        startActivity(game);
        finish();
    }

    private void buttonsGame() {
        Intent game = new Intent(StartActivity.this, MainActivity.class);
        game.putExtra("type", "buttons");
        startActivity(game);
        finish();
    }
}