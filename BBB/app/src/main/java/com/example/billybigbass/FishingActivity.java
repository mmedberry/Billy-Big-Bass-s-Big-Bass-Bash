package com.example.billybigbass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class FishingActivity extends AppCompatActivity implements SensorUpdateCallback {

    private FishModel fishModel;
    private AppDatabase database;
    private UserDataModel userDataModel;
    private SetHookSensor hookSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishing);
        database = AppDatabase.getAppDatabase(this);
        userDataModel = (UserDataModel) getIntent().getSerializableExtra("userDataModel");
        if (fishModel == null) {
            Random random = new Random();
            String[] fishNames = {"minnow", "bluegill", "bass", "trash"};
            fishModel = new FishModel(fishNames[random.nextInt(4)]);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        hookSensor = new SetHookSensor(this, this);
        hookSensor.start();
    }

    private void catchFish() {
        Fish fish = new Fish(fishModel.getName(), fishModel.getLength(), fishModel.getWeight());
        database.eventDao().insertFish(fish);

        Log.v("saving", "here");

        //Only save if high scores have been updated
        if (userDataModel.getHighScores().addFish(fish)) {
            userDataModel.saveUserData(this);
        }

    }

    @Override
    public void update(float orientation) {
        catchFish();
        TextView text = findViewById(R.id.textView3);
        text.setText("Success!");
        hookSensor.stop();
    }
}
