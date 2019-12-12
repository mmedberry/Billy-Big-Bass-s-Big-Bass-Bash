package com.example.billybigbass;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;


/**
 * This class represents the Fish object whose data will be stored in the database.
 */
@Entity(tableName = "fish")
public class Fish implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "length")
    private int length;
    @ColumnInfo(name = "weight")
    private int weight;


    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Constructs an Fish given data from database
     */
    @Ignore
    public Fish(String name, int length, int weight) {
        this.name = name;
        this.length = length;
        this.weight = weight;
    }

    /**
     * Constructs an empty Fish, used by Room database query
     */
    public Fish() {
    }

    /**
     * Get Uid of Fish
     * @return uid
     */
    public int getUid() {
        return uid;
    }

    /**
     * Get name of fish
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get length of fish
     * @return length
     */
    public int getLength() {
        return length;
    }

    /**
     * Get weight of fish
     * @return weight
     */
    public int getWeight() {
        return weight;
    }
}