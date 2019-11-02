package com.example.billybigbass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private UserDataModel mUserDataModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startFishingActivity(View view) {
        Intent intent = new Intent(this, FishingActivity.class);
        startActivity(intent);
    }
}
