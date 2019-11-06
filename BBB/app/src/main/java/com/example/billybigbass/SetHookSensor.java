package com.example.billybigbass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SetHookSensor implements SensorEventListener {

    private SensorManager mSensorManager;//used to store the SensorManager for use throughout the model class
    private Sensor mAcc;//used to get and start/register the Sensor
    private SensorUpdateCallback mCallback;//used to keep track of the activity to callback to

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double Gx = sensorEvent.values[0];
        double Gy = sensorEvent.values[1];
        double Gz = sensorEvent.values[2];
        if (Gx >= 5) {
            mCallback.update(0.0f);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void start() {
        mSensorManager.registerListener(this, mAcc, SensorManager.SENSOR_DELAY_UI);
    }

    public SetHookSensor(Context context, SensorUpdateCallback callback) {
        mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        mAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mCallback = callback;
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
    }
}
