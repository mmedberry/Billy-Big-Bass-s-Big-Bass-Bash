package com.example.billybigbass;

/**
 * This is an interface that can be implemented in the activity. This allows for the corresponding
 * update() method to be called in the activity from a Model.
 */
public interface SensorUpdateCallback {
    void updateHook(boolean success);

    void updateReel(float val, int flag);

    void updateCast(boolean success);
}
