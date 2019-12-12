package com.example.billybigbass;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Dao implementation for the project
 */
@Dao
public interface FishDao {

    /**
     * Get all the entries in the table
     * @return List of Fish objects
     */
    @Query("SELECT * FROM fish")
    List<Fish> getAll();

    /**
     * Get Fish by ID
     * @param id ID of Fish
     * @return desired Fish
     */
    @Query("SELECT * FROM fish WHERE uid = :id")
    Fish getByID(int id);

    /**
     * Get Fish by species name
     * @param name Species of Fish
     * @return List of Fish objects with matching name
     */
    @Query("SELECT * FROM fish WHERE name = :name")
    List<Fish> getByName(String name);

    /**
     * Insert a new Fish into the table
     * @param event Fish to insert
     */
    @Insert
    void insertFish(Fish event);

    /**
     * Delete Fish from table
     * @param event Fish to delete
     */
    @Delete
    void delete(Fish event);


}
