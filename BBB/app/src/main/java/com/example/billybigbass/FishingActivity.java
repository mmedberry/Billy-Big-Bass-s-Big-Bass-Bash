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

    private void setHookToast() {

        CharSequence text = "Shake device to set hook!";
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateHook(boolean success) {
        if (success) {
            TextView text = findViewById(R.id.textView3);
            text.setText("Fish hooked!");
            indicatorImage.clearAnimation();
            indicatorImage.setVisibility(View.GONE);
            indicatorImage = findViewById(R.id.reelView);
            indicatorImage.setVisibility(View.VISIBLE);
            Log.w("Fish type", fishModel.getName());
            ReelSensor rs = new ReelSensor(getApplicationContext(), this, fishModel.getDifficulty());
            rs.start();
        } else {
            indicatorImage.clearAnimation();
            indicatorImage.setVisibility(View.GONE);
            indicatorImage = findViewById(R.id.fishView);
            indicatorImage.setVisibility(View.VISIBLE);
            TextView text = findViewById(R.id.textView3);
            text.setText("It got away.");
        }

    }
    @Override
    public void updateReel(float val, int flag){
        reelImage.setRotation(val);
        if (flag==1){

            catchFish();
            if(fishModel.getName().equals("trash")){
                Bitmap fishImage = BitmapFactory.decodeResource(getResources(),R.drawable.trash_small);
                indicatorImage.setImageBitmap(fishImage);
            } else if(fishModel.getName().equals("minnow")){
                Bitmap fishImage = BitmapFactory.decodeResource(getResources(),R.drawable.minnow_small);
                indicatorImage.setImageBitmap(fishImage);
            }else if(fishModel.getName().equals("trout")){
                Bitmap fishImage = BitmapFactory.decodeResource(getResources(),R.drawable.trout_small);
                indicatorImage.setImageBitmap(fishImage);
            }
            else if(fishModel.getName().equals("bass")){
                Bitmap fishImage = BitmapFactory.decodeResource(getResources(),R.drawable.bass_small);
                indicatorImage.setImageBitmap(fishImage);
            }
            TextView text = findViewById(R.id.textView3);
            CharSequence charSequence = "You caught a "+fishModel.getName()+"!";
            text.setText(charSequence);

            //Succeed - TODO Catch fish, display the image of the fish, and its information
            Log.w("FISH CAUGHT", " -- Name: " + fishModel.getName() + "Difficulty: " + fishModel.getDifficulty() + "Length: " + fishModel.getLength() + "Weight: " + fishModel.getWeight());
        }else if(flag==-1){
            //Fail - Display image of fish got away
            Log.w("FISH LOST", " -- Name: " + fishModel.getName() + "Difficulty: " + fishModel.getDifficulty() + "Length: " + fishModel.getLength() + "Weight: " + fishModel.getWeight());
            indicatorImage.clearAnimation();
            indicatorImage.setVisibility(View.GONE);
            indicatorImage = findViewById(R.id.fishView);
            indicatorImage.setVisibility(View.VISIBLE);
            TextView text = findViewById(R.id.textView3);
            text.setText("It got away.");
        }
    }


    public void cast(View view) {
        Random random = new Random();
        String[] fishNames = {"minnow", "trout", "bass", "trash"};
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
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.fish_hit);


            reelImage.animate().translationY(100.0f);
            mediaPlayer.start();
            SetHookSensor hookSensor = new SetHookSensor(context, fishingActivity);
            getSetHookSensorStatus(hookSensor);
            setHookToast();
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
