package com.example.billybigbass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


public class ReelSensor implements SensorEventListener {

    private SensorManager mSensorManager;//used to store the SensorManager for use throughout the model class
    private Sensor mAcc;//used to get and start/register the Sensor
    private SensorUpdateCallback mCallback;//used to keep track of the activity to callback to
    private float orientation;
    private int fishDifficulty;
    private long startTime;
    private float targetOrientation;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double Gx = Math.abs(sensorEvent.values[0]);
        double Gy = Math.abs(sensorEvent.values[1]);
        double Gz = Math.abs(sensorEvent.values[2]);
        orientation += (float) ((Gx + Gy + Gz) / 5 - fishDifficulty / 2);
        mCallback.updateReel(orientation, 0);
        Log.w("Orientation: ", "" + orientation);
        if (checkFailed()) {
            stop(false);
        }
        if (orientation > targetOrientation) {
            stop(true);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    private boolean checkFailed() {
        if (System.currentTimeMillis() > startTime + 10000 + fishDifficulty * 1000) {
            Log.w("FAILED:", "Ran out of time: " + (10 + fishDifficulty) + " seconds");
            return true;
        } else if (orientation <= -targetOrientation / 2) {
            Log.w("FAILED:", "Fish took all your line");
            return true;
        } else {
            return false;
        }
    }

    public void start() {
        mSensorManager.registerListener(this, mAcc, SensorManager.SENSOR_DELAY_UI);
        startTime = System.currentTimeMillis();
        targetOrientation = 360.0f + fishDifficulty * 360.0f;
        Log.w("Fish difficulty: ", ""+fishDifficulty);
        Log.w("Succeed condition: ", targetOrientation + " degrees");
        Log.w("Fail conditions: ", "Orientation - " + (-targetOrientation/2) + "Time Limit - " + (10 + fishDifficulty) + " seconds");
        Log.w("","");
    }

    public ReelSensor(Context context, SensorUpdateCallback callback, int fishDifficulty) {
        this.fishDifficulty = fishDifficulty;
        mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        mAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mCallback = callback;
    }

    public void stop(boolean success) {
        if (success) {
            //Succeeded
            mCallback.updateReel(orientation, 1);
        } else {
            //Failed
            mCallback.updateReel(orientation, -1);
        }
        mSensorManager.unregisterListener(this);
    }
}
