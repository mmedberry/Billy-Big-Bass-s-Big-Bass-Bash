package com.example.billybigbass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.Random;

public class FishingActivity extends AppCompatActivity {

    private FishModel fishModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishing);
        if (fishModel==null){
            Random random = new Random();
            String[] fishNames = {"minnow", "bluegill", "bass", "trash"};
            fishModel = new FishModel(fishNames[random.nextInt(4)]);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("fish name", fishModel.getmName());
        Log.v("fish length", ""+fishModel.getmLength());
        Log.v("fish weight", ""+fishModel.getmWeight());
        Log.v("fish difficulty", ""+fishModel.getmDifficulty());
    }
}
