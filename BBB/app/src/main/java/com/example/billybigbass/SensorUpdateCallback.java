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
     * @param value is a float to pass from a Model class to the corresponding Activity
     */
    void update(boolean success);
}
