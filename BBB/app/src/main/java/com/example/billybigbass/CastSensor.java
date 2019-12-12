package com.example.billybigbass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * SensorEventListener using the accelerometer to sense "cast" motions
 */
public class CastSensor implements SensorEventListener {
    /**
     * Contains the magnitude and direction of the first movement of the cast motion
     */
    private double firstMovement = 0.0;
    private SensorManager mSensorManager;//used to store the SensorManager for use throughout the model class
    private Sensor mAcc;//used to get and start/register the Sensor
    private SensorUpdateCallback mCallback;//used to keep track of the activity to callback to
    /**
     * The timestamp of the first cast movement. Works to slow down the sensor to wait for a reasonable amount of time for the user to make motion.
     */
    private double firstMovementTime;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double Gx = sensorEvent.values[0];
        if (Math.abs(Gx) > 5) { //limit read input to exclude background readings
            if (firstMovement == 0.0) {
                firstMovement = Gx;
                firstMovementTime = System.currentTimeMillis();
            } else if (firstMovement > 0.0 && System.currentTimeMillis() > firstMovementTime + 500) {
                if (Gx < 0.0) {
                    mCallback.updateCast(true);
                } else {
                    mCallback.updateCast(false);
                }
                stop();
            } else if (firstMovement < 0.0 && System.currentTimeMillis() > firstMovementTime + 500) {
                if (Gx > 0.0) {
                    mCallback.updateCast(true);

                } else {
                    mCallback.updateCast(false);
                }
                stop();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public CastSensor(Context context, SensorUpdateCallback callback) {
        mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        mAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mCallback = callback;
        mSensorManager.registerListener(this, mAcc, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * Private helper method to stop the sensor after it's no longer needed.
     */
    private void stop() {
        mSensorManager.unregisterListener(this);
    }

}
