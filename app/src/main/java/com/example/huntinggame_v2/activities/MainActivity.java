package com.example.huntinggame_v2.activities;

import static com.example.huntinggame_v2.GameManager.COLS;
import static com.example.huntinggame_v2.GameManager.ROWS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.example.huntinggame_v2.GameManager;
import com.example.huntinggame_v2.R;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {
    private TextView main_LBL_score;
    private ImageView hearts[] = new ImageView[3];
    private ImageView main_IMG_heart1, main_IMG_heart2, main_IMG_heart3;
    private ImageButton main_BTN_up, main_BTN_right, main_BTN_left, main_BTN_down;
    private ImageView main_IMG_1, main_IMG_2, main_IMG_3, main_IMG_4, main_IMG_5, main_IMG_6, main_IMG_7, main_IMG_8, main_IMG_9, main_IMG_10,
            main_IMG_11, main_IMG_12, main_IMG_13, main_IMG_14, main_IMG_15, main_IMG_16, main_IMG_17, main_IMG_18, main_IMG_19, main_IMG_20,
            main_IMG_21, main_IMG_22, main_IMG_23, main_IMG_24, main_IMG_25;
    private LinearLayoutCompat main_LAY_buttons;
    private ImageView mat[][] = new ImageView[ROWS][COLS];
    private int score = 0;
    private CountDownTimer timer;
    private boolean isTimerRunning = false;
    //sensor
    private SensorManager sensorManager;
    private Sensor accSensor;
    private SensorEventListener sensorListener;
    private GameManager game = new GameManager();
    private String gameType;
    //Location
    private LocationManager locationManager;
    private Location location;
    private float LOCATION_REFRESH_DISTANCE = 100;
    private long LOCATION_REFRESH_TIME = 15000;
    private double latitude;
    private double longitude;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    //sound
    MediaPlayer itemCatch;
    MediaPlayer catCatch;


    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocationSettings();
        setContentView(R.layout.activity_main);
        findViews();
        itemCatch = MediaPlayer.create(MainActivity.this,R.raw.item);
        catCatch = MediaPlayer.create(MainActivity.this,R.raw.cat);
        gameType = getIntent().getExtras().getString("type");
        if (gameType.equals("buttons")) {
            initButtons();
        } else if (gameType.equals("sensor")) {
            initSensors();
        }
        //initialize mat array
        hearts[0] = main_IMG_heart1;
        hearts[1] = main_IMG_heart2;
        hearts[2] = main_IMG_heart3;
        ImageView temp[][] = {{main_IMG_1, main_IMG_2, main_IMG_3,main_IMG_4, main_IMG_5}, {main_IMG_6,main_IMG_7, main_IMG_8, main_IMG_9,main_IMG_10},
                {main_IMG_11, main_IMG_12,main_IMG_13, main_IMG_14,main_IMG_15}, {main_IMG_16,main_IMG_17,main_IMG_18,main_IMG_19,main_IMG_20},
                {main_IMG_21,main_IMG_22,main_IMG_23,main_IMG_24,main_IMG_25}};
        mat = temp;
        //timer
        timer = new CountDownTimer(1000000, 1000) {
            @Override
            public void onTick(long l) {
                everySecond();
            }

            @Override
            public void onFinish() {
            }
        };
        timer.start();
        isTimerRunning = true;
    }

    private void initSensors() {
        main_LAY_buttons.setVisibility(View.GONE);
        //sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                //x>2 left ; x<-2 right ; y>7 up ; y<5 down
                Log.d("ccc", "X= " + x);
                Log.d("ccc", "Y= " + y);
                if (x > 2) {
                    game.setDirection(GameManager.DIRECTION.LEFT);
                } else if (x < -2) {
                    game.setDirection(GameManager.DIRECTION.RIGHT);
                } else if (y > 9) {
                    game.setDirection(GameManager.DIRECTION.UP);
                } else if (y < 7) {
                    game.setDirection(GameManager.DIRECTION.DOWN);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    private void initButtons() {
        //move according to buttons
        main_BTN_up.setOnClickListener(view -> game.setDirection(GameManager.DIRECTION.UP));
        main_BTN_right.setOnClickListener(view -> game.setDirection(GameManager.DIRECTION.RIGHT));
        main_BTN_left.setOnClickListener(view -> game.setDirection(GameManager.DIRECTION.LEFT));
        main_BTN_down.setOnClickListener(view -> game.setDirection(GameManager.DIRECTION.DOWN));
    }

    private void setLocationSettings() {
        Criteria locationCriteria = new Criteria();
        locationCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationCriteria.setAltitudeRequired(false);
        locationCriteria.setBearingRequired(false);
        locationCriteria.setCostAllowed(true);
        locationCriteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        locationManager = (LocationManager) MainActivity.this.getSystemService(LOCATION_SERVICE);
        @SuppressLint("MissingPermission")
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                                // Then update all the time and at every meters change.
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                                        0, mLocationListener);
                                String providerName = locationManager.getBestProvider(locationCriteria, true);
                                location = locationManager.getLastKnownLocation(providerName);
                                if (location == null) {
                                    latitude = defaultLocation.latitude;
                                    longitude = defaultLocation.longitude;
                                } else {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                                LatLng curLoc = new LatLng(latitude, longitude);
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                                // Then update every limited time and at every limited meters change.
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                                        LOCATION_REFRESH_DISTANCE, mLocationListener);
                                String providerName = locationManager.getBestProvider(locationCriteria, true);
                                location = locationManager.getLastKnownLocation(providerName);
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                LatLng curLoc = new LatLng(latitude, longitude);
                            } else {
                                // No location access granted.
                                // Then we can't use the location track and the 1st score location functionality is disabled.
                                // That's why we have set the default location to Ness Ziona(could be anywhere else just need default).
                            }
                        }
                );
        // check whether the app already has the permissions,
        // and whether the app needs to show a permission rationale dialog.
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    private void everySecond() {
        //start with moving the hunted character down default
        moveCharacter(game.getDirection());
        main_LBL_score.setText(String.valueOf(++score));
    }

    private void moveCharacter(GameManager.DIRECTION direction) {
        mat[game.getRowHunted()][game.getColHunted()].setImageResource(0);
        mat[game.getRowHunter()][game.getColHunter()].setImageResource(0);
        if(game.getColItem() == game.getColHunter() && game.getRowItem() == game.getRowHunter()){
            mat[game.getRowItem()][game.getColItem()].setImageResource(R.drawable.cheese);
        }
        game.moveCharacter(direction);
        mat[game.getRowHunted()][game.getColHunted()].setImageResource(R.drawable.mouse);
        mat[game.getRowHunter()][game.getColHunter()].setImageResource(R.drawable.cat);
        if (game.isCatch()) {
            //game restart position
            mat[game.getRowHunted()][game.getColHunted()].setImageResource(0);
            mat[game.getRowHunter()][game.getColHunter()].setImageResource(0);
            game.startPosition();
            mat[game.getRowHunted()][game.getColHunted()].setImageResource(R.drawable.mouse);
            mat[game.getRowHunter()][game.getColHunter()].setImageResource(R.drawable.cat);
            hearts[game.getLives()].setVisibility(View.INVISIBLE);
            if (game.isGameOver()) {
                Intent intent = new Intent(MainActivity.this, HighScoreActivity.class);
                intent.putExtra("score", score);
                intent.putExtra("type", gameType);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
                finish();
            }else{
                catCatch.start();
            }
        }
        //did hunted catch item
        if (game.extraPoints()) {
            itemCatch.start();
            game.changeItemLoc();
            score += 10;
            mat[game.getRowItem()][game.getColItem()].setImageResource(R.drawable.cheese);
        }
    }

    private void findViews() {
        //score
        main_LBL_score = findViewById(R.id.main_LBL_score);
        //hearts
        main_IMG_heart1 = findViewById(R.id.main_IMG_heart1);
        main_IMG_heart2 = findViewById(R.id.main_IMG_heart2);
        main_IMG_heart3 = findViewById(R.id.main_IMG_heart3);
        //button
        main_LAY_buttons = findViewById(R.id.main_LAY_buttons);
        main_BTN_up = findViewById(R.id.main_BTN_up);
        main_BTN_right = findViewById(R.id.main_BTN_right);
        main_BTN_left = findViewById(R.id.main_BTN_left);
        main_BTN_down = findViewById(R.id.main_BTN_down);
        //mat
        main_IMG_1 = findViewById(R.id.main_IMG_1);
        main_IMG_2 = findViewById(R.id.main_IMG_2);
        main_IMG_3 = findViewById(R.id.main_IMG_3);
        main_IMG_4 = findViewById(R.id.main_IMG_4);
        main_IMG_5 = findViewById(R.id.main_IMG_5);
        main_IMG_6 = findViewById(R.id.main_IMG_6);
        main_IMG_7 = findViewById(R.id.main_IMG_7);
        main_IMG_8 = findViewById(R.id.main_IMG_8);
        main_IMG_9 = findViewById(R.id.main_IMG_9);
        main_IMG_10 = findViewById(R.id.main_IMG_10);
        main_IMG_11 = findViewById(R.id.main_IMG_11);
        main_IMG_12 = findViewById(R.id.main_IMG_12);
        main_IMG_13 = findViewById(R.id.main_IMG_13);
        main_IMG_14 = findViewById(R.id.main_IMG_14);
        main_IMG_15 = findViewById(R.id.main_IMG_15);
        main_IMG_16 = findViewById(R.id.main_IMG_16);
        main_IMG_17 = findViewById(R.id.main_IMG_17);
        main_IMG_18 = findViewById(R.id.main_IMG_18);
        main_IMG_19 = findViewById(R.id.main_IMG_19);
        main_IMG_20 = findViewById(R.id.main_IMG_20);
        main_IMG_21 = findViewById(R.id.main_IMG_21);
        main_IMG_22 = findViewById(R.id.main_IMG_22);
        main_IMG_23 = findViewById(R.id.main_IMG_23);
        main_IMG_24 = findViewById(R.id.main_IMG_24);
        main_IMG_25 = findViewById(R.id.main_IMG_25);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ccc", "destroy");
        timer.cancel();
        isTimerRunning = false;
        score = 0;
    }

    @Override
    protected void onPause() {
        Log.d("ccc", "pause");
        super.onPause();
        timer.cancel();
        isTimerRunning = false;
        if (gameType.equals("sensor")) {
            sensorManager.unregisterListener(sensorListener);
        }
    }

    @Override
    protected void onResume() {
        Log.d("ccc", "resume");
        super.onResume();
        if (!isTimerRunning) {
            timer.start();
        }
        if (gameType.equals("sensor")) {
            sensorManager.registerListener(sensorListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}