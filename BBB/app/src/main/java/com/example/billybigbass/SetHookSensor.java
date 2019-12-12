package com.example.billybigbass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * SensorEventListener using the accelerometer to check if the fish was successfully hooked
 */
public class SetHookSensor implements SensorEventListener {

    private SensorManager mSensorManager;//used to store the SensorManager for use throughout the model class
    private Sensor mAcc;//used to get and start/register the Sensor
    private SensorUpdateCallback mCallback;//used to keep track of the activity to callback to
    private long start;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double Gx = sensorEvent.values[0];
        long stop = System.currentTimeMillis() / 1000;
        if (stop - start > 2) {
            mCallback.updateHook(false);
            stop();
        } else {
            if (Math.abs(Gx) >= 5) {
                mCallback.updateHook(true);
                stop();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * Start the sensor
     * @param start time the sensor was started
     */
    public void start(long start) {
        this.start = start;
        mSensorManager.registerListener(this, mAcc, SensorManager.SENSOR_DELAY_UI);
    }

    public SetHookSensor(Context context, SensorUpdateCallback callback) {
        mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        mAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mCallback = callback;
    }

    /**
     * Stops the sensor
     */
    public void stop() {
        mSensorManager.unregisterListener(this);
    }
}
