package com.example.billybigbass;

/**
 * This is an interface that can be implemented in the activity. This allows for the corresponding
 * update() method to be called in the activity from a Model.
 */
public interface SensorUpdateCallback {
    /**
     * This method is implemented in the acitivty and is called from a Model class when it has
     * a value to pass along to the activity for use with the updateing of the UI
     *
     */
    void updateHook(boolean success);
    void updateReel(float val, int flag);
    void updateCast(boolean success);
}
