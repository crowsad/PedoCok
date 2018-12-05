package com.github.gist.pedocok;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.gist.pedocok.Model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SensorEventListener, com.github.gist.pedocok.StepListener {
    private TextView textView;
    private com.github.gist.pedocok.StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    private TextView TvSteps, Welcome, Highscore;
    private Button BtnStart;
    private Button BtnStop;
    private Button BtnSignOut;

    FirebaseDatabase database;
    DatabaseReference users;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        Intent mainIntent = getIntent();
        final String username = mainIntent.getStringExtra("usr");

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new com.github.gist.pedocok.StepDetector();
        simpleStepDetector.registerListener(this);

        Welcome = (TextView) findViewById(R.id.welcome);
        TvSteps = (TextView) findViewById(R.id.tv_steps);
        BtnStart = (Button) findViewById(R.id.btn_start);
        BtnStop = (Button) findViewById(R.id.btn_stop);
        BtnSignOut = (Button) findViewById(R.id.sign_out);
        Highscore = (TextView) findViewById(R.id.HighScore);
        Welcome.setText("Welcome " + username + "!");

        BtnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                numSteps = 0;
                sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

            }
        });


        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                sensorManager.unregisterListener(MainActivity.this);
                push_score(username);

            }
        });



    }

    private void push_score(final String usern){
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference updateRef = users.child(usern);
                Map<String, Object> UpdateCurr = new HashMap<>();
                UpdateCurr.put("currentScore", numSteps);
                updateRef.updateChildren(UpdateCurr);
                User score = dataSnapshot.child(usern).getValue(User.class);
                if( score.getHighScore() <= numSteps){
                    Map<String, Object> UpdateHigh = new HashMap<>();
                    UpdateHigh.put("highScore", numSteps);
                    updateRef.updateChildren(UpdateHigh);
                    Map<String, Object> UpdateZero = new HashMap<>();
                    UpdateZero.put("currentScore", 0);
                    updateRef.updateChildren(UpdateZero);
                    Highscore.setText("Your Highscore is: " + numSteps);
                } else {
                    Map<String, Object> UpdateZero = new HashMap<>();
                    UpdateZero.put("currentScore", 0);
                    updateRef.updateChildren(UpdateZero);
                    Highscore.setText("Your Highscore is: " + score.getHighScore().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        TvSteps.setText(TEXT_NUM_STEPS + numSteps);
    }

}

