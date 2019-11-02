package com.example.billybigbass;

import androidx.lifecycle.ViewModel;

import java.util.Random;

public class FishModel extends ViewModel {
    private String mName;
    private int mLength;
    private int mWeight;
    private int mDifficulty;

    public String getmName() {
        return mName;
    }

    public int getmLength() {
        return mLength;
    }

    public int getmWeight() {
        return mWeight;
    }

    public int getmDifficulty() {
        return mDifficulty;
    }

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
