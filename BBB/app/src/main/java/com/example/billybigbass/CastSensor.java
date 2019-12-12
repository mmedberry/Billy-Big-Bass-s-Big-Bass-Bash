package com.example.billybigbass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CastSensor implements SensorEventListener {
    double firstMovement = 0.0;
    private SensorManager mSensorManager;//used to store the SensorManager for use throughout the model class
    private Sensor mAcc;//used to get and start/register the Sensor
    private SensorUpdateCallback mCallback;//used to keep track of the activity to callback to
    private double firstMovementTime;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double Gx = sensorEvent.values[0];
        if (Math.abs(Gx) > 5) {
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

    public void stop() {
        mSensorManager.unregisterListener(this);
    }

}
