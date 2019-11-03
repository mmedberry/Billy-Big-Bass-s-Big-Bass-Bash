package com.example.billybigbass;

import androidx.lifecycle.ViewModel;

/**
 * ViewModel for updating a FishingActivity
 */
public class FishModel extends ViewModel {
    private String mName;
    private int mLength;
    private int mWeight;
    private int mDifficulty;

    public String getName() {
        return mName;
    }

    public int getLength() {
        return mLength;
    }

    public int getWeight() {
        return mWeight;
    }

    public int getDifficulty() {
        return mDifficulty;
    }

    /**
     * Constructor to create a randomized FishModel with given type
     * @param name Type of fish to create
     */
    public FishModel(String name) {
        mName = name;
        FishData fishData;
        switch (mName) {
            case "bass":
                fishData = FishData.LARGE;
                mLength = fishData.getLength();
                mWeight = fishData.getWeight();
                mDifficulty = fishData.getDifficulty();
                break;
            case "bluegill":
                fishData = FishData.MEDIUM;
                mLength = fishData.getLength();
                mWeight = fishData.getWeight();
                mDifficulty = fishData.getDifficulty();
                break;
            case "minnow":
                fishData = FishData.SMALL;
                mLength = fishData.getLength();
                mWeight = fishData.getWeight();
                mDifficulty = fishData.getDifficulty();
                break;
            case "trash":
                fishData = FishData.TRASH;
                mLength = fishData.getLength();
                mWeight = fishData.getWeight();
                mDifficulty = fishData.getDifficulty();
                break;
        }
    }

}
