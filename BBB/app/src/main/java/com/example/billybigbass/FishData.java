package com.example.billybigbass;

import java.util.Random;

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
        if (size == 0) {
            length = 0;
            weight = 0;
            difficulty = 1;
        } else {
            Random random = new Random();
            length = random.nextInt(size * 5 - 1) + size;
            weight = random.nextInt(size * 5 - 1) + size;
            difficulty = size;
        }

    }
}
