package com.example.billybigbass;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FishDao {

    @Query("SELECT * FROM fish")
    List<Fish> getAll();


    @Query("SELECT * FROM fish WHERE uid = :id")
    Fish getByID(int id);

    @Query("SELECT * FROM fish WHERE name = :name")
    List<Fish> getByName(String name);

    @Insert
    void insertFish(Fish event);


    @Delete
    void delete(Fish event);


}
