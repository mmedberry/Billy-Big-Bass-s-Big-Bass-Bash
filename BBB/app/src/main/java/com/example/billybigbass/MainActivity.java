package com.example.billybigbass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {

    private AppDatabase database;
    private UserDataModel userDataModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        database = AppDatabase.getAppDatabase(this);
        userDataModel = new UserDataModel(this);
    }

    public void startFishingActivity(View view) {
        Intent intent = new Intent(this, FishingActivity.class);
        intent.putExtra("userDataModel", userDataModel);
        startActivity(intent);
    }

    public void startRecordsActivity(View view) {
        Intent intent = new Intent(this, RecordsActivity.class);
        intent.putExtra("userDataModel", userDataModel);
        startActivity(intent);
    }
}
