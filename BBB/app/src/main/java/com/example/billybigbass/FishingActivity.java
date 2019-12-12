package com.example.billybigbass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class FishingActivity extends AppCompatActivity implements SensorUpdateCallback {

    private FishModel fishModel;
    private AppDatabase database;
    private UserDataModel userDataModel;
    private ImageView indicatorImage;
    private FishingAwaitTimer fishingAwaitTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishing);
        database = AppDatabase.getAppDatabase(this);
        userDataModel = (UserDataModel) getIntent().getSerializableExtra("userDataModel");
        indicatorImage = findViewById(R.id.imageView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fishingAwaitTimer != null) {
            fishingAwaitTimer.cancel(true);
        }
    }

    private boolean catchFish() {
        Fish fish = new Fish(fishModel.getName(), fishModel.getLength(), fishModel.getWeight());
        database.eventDao().insertFish(fish);
        //Only save if high scores have been updated
        if (userDataModel.getHighScores().addFish(fish)) {
            userDataModel.saveUserData(this);
            return true;
        }
        return false;
    }

    public void getSetHookSensorStatus(SetHookSensor setHookSensor) {
        setHookSensor.start(System.currentTimeMillis() / 1000);
    }
    

    @Override
    public void updateHook(boolean success) {
        if (success) {
            setText("Fish hooked! Reel in!");
            indicatorImage.clearAnimation();
            indicatorImage.setRotation(0.0f);
            Bitmap reelImage = BitmapFactory.decodeResource(getResources(), R.drawable.reel);
            indicatorImage.setImageBitmap(reelImage);
            Log.w("Fish type", fishModel.getName());
            ReelSensor rs = new ReelSensor(getApplicationContext(), this, fishModel.getDifficulty());
            rs.start();
        } else {
            indicatorImage.clearAnimation();
            indicatorImage.setRotation(0.0f);
            Bitmap fishImage = BitmapFactory.decodeResource(getResources(), R.drawable.sad_fish);
            indicatorImage.setImageBitmap(fishImage);
            setText("It got away.\nBetter luck next time!");
        }

    }

    @Override
    public void updateReel(float val, int flag) {

        if (flag == 1) {
            indicatorImage.setRotation(0.0f);
            boolean highScore = catchFish();
            int resourceIdentifier = getResources().getIdentifier(fishModel.getName() + "_small", "drawable", getPackageName());
            Bitmap fishImage = BitmapFactory.decodeResource(getResources(), resourceIdentifier);
            indicatorImage.setImageBitmap(fishImage);

            String successMessage;
            if (highScore) {
                successMessage = String.format("HIGH SCORE!\nYou caught a(n) %s!\nLength: %d\nWeight: %d", fishModel.getName(), fishModel.getLength(), fishModel.getWeight());
            } else {
                successMessage = String.format("You caught a(n) %s!\nLength: %d\nWeight: %d", fishModel.getName(), fishModel.getLength(), fishModel.getWeight());
            }
            setText(successMessage);
            Log.w("FISH CAUGHT", " -- Name: " + fishModel.getName() + " | Difficulty: " + fishModel.getDifficulty() + " | Length: " + fishModel.getLength() + " | Weight: " + fishModel.getWeight());
        } else if (flag == -1) {
            //Fail - Display image of fish got away
            Log.w("FISH LOST", " -- Name: " + fishModel.getName() + " | Difficulty: " + fishModel.getDifficulty() + " | Length: " + fishModel.getLength() + " | Weight: " + fishModel.getWeight());
            indicatorImage.clearAnimation();
            indicatorImage.setRotation(0.0f);
            Bitmap fishImage = BitmapFactory.decodeResource(getResources(), R.drawable.sad_fish);
            indicatorImage.setImageBitmap(fishImage);
            setText("It got away.\nBetter luck next time!");
        } else {
            indicatorImage.setRotation(val);
        }
    }

    public void updateCast(boolean success) {
        if (success) {
            setText("Wait for a bite.");
            indicatorImage.setVisibility(View.VISIBLE);
            fishingAwaitTimer = new FishingAwaitTimer(indicatorImage.getRotation(), this, this);
            fishingAwaitTimer.execute();
        } else {
            Bitmap fishImage = BitmapFactory.decodeResource(getResources(), R.drawable.sad_fish);
            indicatorImage.setImageBitmap(fishImage);
            TextView text = findViewById(R.id.textView3);
            text.setText("Bad cast. Try again.");
        }
    }

    //TODO need to make casting motion controlled
    public void cast(View view) {
        Random random = new Random();
        String[] fishNames = {"minnow", "trout", "bass", "trash"};
        fishModel = new FishModel(fishNames[random.nextInt(4)]);
        Log.v("cast", "clicked");

        findViewById(R.id.button3).setVisibility(View.GONE);
        setText("Move phone backward then forward to cast.");
        new CastSensor(this.getApplicationContext(), this);
    }

    private void setText(String text) {
        TextView textView = findViewById(R.id.textView3);
        textView.setText(text);
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
                this.time = random.nextInt(1) + 5;
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
            setText("Shake to set hook!");
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.fish_hit);
            mediaPlayer.start();
            SetHookSensor hookSensor = new SetHookSensor(context, fishingActivity);
            getSetHookSensorStatus(hookSensor);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (indicatorImage.getRotation() == 0.0f) {
                indicatorImage.animate().rotation(30.0f);
            } else if (indicatorImage.getRotation() == 30.0f) {
                indicatorImage.animate().rotation(-30.0f);
            } else if (indicatorImage.getRotation() == -30.0f) {
                indicatorImage.animate().rotation(30.0f);
            }

        }
    }


}
