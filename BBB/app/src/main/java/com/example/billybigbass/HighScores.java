package com.example.billybigbass;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HighScores implements Serializable {
    List<Fish> fishList;
    public HighScores(){
        fishList = new ArrayList<>();
    }
    public HighScores(List<Fish> list){
        fishList = list;
    }
    public List<Fish> getFishList(){
        return fishList;
    }

    /**
     * Custom add method to add a new fish if it's a record for its type
     * @param fish Fish to be added
     * @return True if fish is added, false if fish isn't added
     */
    public boolean addFish(Fish fish){
        boolean sameName = false;
        for(Fish oldFish: fishList){
            int oldFishSize = oldFish.getLength()*oldFish.getWeight();
            int newFishSize = fish.getLength()*fish.getWeight();
            if (oldFish.getName().equals(fish.getName())){
                sameName = true;
                if(newFishSize>oldFishSize){
                fishList.add(fishList.indexOf(oldFish), fish);
                fishList.remove(oldFish);
                    Log.v("Added fish", fish.getName());
                return true;}
            }

        }
        if(!sameName){
            fishList.add(fish);
            Log.v("Added fish", fish.getName());
            return true;
        }
        return false;
    }
}
