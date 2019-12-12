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

    /**
     * Adds the newly caught fish to the database and high scores if applicable
     *
     * @return true if fish added to high scores, false otherwise
     */
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

    /**
     * Starts the SetHookSensor with the current time
     *
     * @param setHookSensor SetHookSensor from AsyncTask
     */
    public void getSetHookSensorStatus(SetHookSensor setHookSensor) {
        setHookSensor.start(System.currentTimeMillis() / 1000);
    }

    /**
     * Callback for SetHookSensor
     *
     * @param success true if fish hooked, false if failed
     */
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

    /**
     * Callback for ReelSensor, updates the rotation of the reel image, or changes image if fish is caught or lost
     *
     * @param val  Degrees to rotate image
     * @param flag 0 if rotating image, 1 if fish caught, -1 if fish lost
     */
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

    /**
     * Callback for CastSensor
     *
     * @param success true if success, else false
     */
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

    /**
     * onClick method for "Cast" button. Starts CastSensor
     *
     * @param view button
     */
    public void cast(View view) {
        Random random = new Random();
        String[] fishNames = {"minnow", "trout", "bass", "trash"};
        fishModel = new FishModel(fishNames[random.nextInt(4)]);
        Log.v("cast", "clicked");

        findViewById(R.id.button3).setVisibility(View.GONE);
        setText("Move phone backward then forward to cast.");
        new CastSensor(this.getApplicationContext(), this);
    }

    /**
     * Private helper to set text indicating the stage of the fish catching process.
     *
     * @param text Desired message to display
     */
    private void setText(String text) {
        TextView textView = findViewById(R.id.textView3);
        textView.setText(text);
    }

    /**
     * AsyncTask to wait for a fish to bite the hook. Animates the
     */
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
                this.time = random.nextInt(fishModel.getDifficulty() * 3 + 1) + 1; //randomly generate time for bobber to animate
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
            //below lines animate bobber image
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
