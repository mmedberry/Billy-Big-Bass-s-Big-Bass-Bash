package com.example.billybigbass;

import java.util.Random;

/**
 * Provides data for a new fish, based on the fishes size. Creates randomized length and weight.
 */
//TODO Add more variety. More types, different fields maybe?
public enum FishData {
    LARGE(3),
    MEDIUM(2),
    SMALL(1),
    TRASH(0);
    private int length;
    private int weight;
    private int difficulty;

    public int getLength() {
        return length;
    }

    public int getWeight() {
        return weight;
    }

    public int getDifficulty() {
        return difficulty;
    }

    FishData(int size) {
        //for trash
        if (size == 0) {
            length = 0;
            weight = 0;
            difficulty = 1;
        } else {
            Random random = new Random();
            length = random.nextInt(size * 5 - 1) + size; //calculate length of fish within range of (size, size*5)
            weight = random.nextInt(size * 5 - 1) + size; //calculate weight of fish within range of (size, size*5)
            difficulty = size;
        }

    }
}
