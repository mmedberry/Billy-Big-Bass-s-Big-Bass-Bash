package com.example.billybigbass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class FishingActivity extends AppCompatActivity implements SensorUpdateCallback {

    private FishModel fishModel;
    private AppDatabase database;
    private UserDataModel userDataModel;
    private ImageView indicatorImage;
    private ImageView reelImage;
    private FishingAwaitTimer fishingAwaitTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishing);
        database = AppDatabase.getAppDatabase(this);
        userDataModel = (UserDataModel) getIntent().getSerializableExtra("userDataModel");
        indicatorImage = findViewById(R.id.imageView);
        reelImage = findViewById(R.id.reelView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fishingAwaitTimer != null) {
            fishingAwaitTimer.cancel(true);
        }
    }

    private void catchFish() {
        Fish fish = new Fish(fishModel.getName(), fishModel.getLength(), fishModel.getWeight());
        database.eventDao().insertFish(fish);
        //Only save if high scores have been updated
        if (userDataModel.getHighScores().addFish(fish)) {
            userDataModel.saveUserData(this);
        }
    }

    public void getSetHookSensorStatus(SetHookSensor setHookSensor) {
        setHookSensor.start(System.currentTimeMillis() / 1000);
    }

    @Override
    public void update(boolean success) {
        if (success) {
            catchFish();
            TextView text = findViewById(R.id.textView3);
            text.setText("Success!");
        } else {
            TextView text = findViewById(R.id.textView3);
            text.setText("Failure!");
        }

    }

    public void cast(View view) {
        Random random = new Random();
        String[] fishNames = {"minnow", "bluegill", "bass", "trash"};
        fishModel = new FishModel(fishNames[random.nextInt(4)]);
        Log.v("cast", "clicked");
        indicatorImage.setVisibility(View.VISIBLE);
        fishingAwaitTimer = new FishingAwaitTimer(indicatorImage.getRotation(), this, this);
        fishingAwaitTimer.execute();
        findViewById(R.id.button3).setVisibility(View.GONE);
    }

    private class FishingAwaitTimer extends AsyncTask<String, String, String> {
        private String resp;
        int time;
        private Float orientation;
        private Context context;
        private FishingActivity fishingActivity;

        public FishingAwaitTimer(float orientation, Context context, FishingActivity fishingActivity) {
            this.context = context;
            this.orientation = orientation;
            this.fishingActivity = fishingActivity;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Random random = new Random();
                this.time = random.nextInt(10) + 5;
                int i = 0;
                while (i < time * 10) {
                    publishProgress(orientation.toString());
                    Thread.sleep(100);
                    i++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.fish_hit);
            reelImage.animate().translationY(100.0f);
            mediaPlayer.start();
            SetHookSensor hookSensor = new SetHookSensor(context, fishingActivity);
            getSetHookSensorStatus(hookSensor);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.v("orientation", "" + indicatorImage.getRotation());
            if (indicatorImage.getRotation() == 0.0f) {
                indicatorImage.animate().rotation(30.0f);
            } else if (indicatorImage.getRotation() == 30.0f) {
                indicatorImage.animate().rotation(-30.0f);
            } else if (indicatorImage.getRotation() == -30.0f) {
                indicatorImage.animate().rotation(30.0f);
            }

        }
    }

    public void reel(View view) {
        indicatorImage.setVisibility(View.GONE);
        Log.v("reel", "clicked");

        reelImage.setVisibility(View.VISIBLE);
        FishingAwaitTimer fishingAwaitTimer = new FishingAwaitTimer(indicatorImage.getRotation(), this, this);
        fishingAwaitTimer.execute();
    }

    public void rotateReel(View view) {
        //Rotate the reel based on the sensor
    }

}
