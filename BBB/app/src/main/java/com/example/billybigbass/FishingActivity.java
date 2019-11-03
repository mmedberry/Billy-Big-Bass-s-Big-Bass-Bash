package com.example.billybigbass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.Random;

public class FishingActivity extends AppCompatActivity {

    private FishModel fishModel;
    private AppDatabase database;
    private UserDataModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishing);
        database = AppDatabase.getAppDatabase(this);
        userDataModel =(UserDataModel) getIntent().getSerializableExtra("userDataModel");
        if (fishModel == null) {
            Random random = new Random();
            String[] fishNames = {"minnow", "bluegill", "bass", "trash"};
            fishModel = new FishModel(fishNames[random.nextInt(4)]);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        catchFish();
    }

    private void catchFish(){
        Fish fish = new Fish(fishModel.getName(), fishModel.getLength(), fishModel.getWeight());
        database.eventDao().insertFish(fish);

        Log.v("saving", "here");

        //Only save if high scores have been updated
        if(userDataModel.getHighScores().addFish(fish)){
            userDataModel.saveUserData(this);
        }

    }
}
