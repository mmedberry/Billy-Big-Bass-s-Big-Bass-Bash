package com.example.billybigbass;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel to contain and save/load the UserData
 */
public class UserDataModel extends ViewModel implements Serializable {
    private HighScores highScores;

    public UserDataModel(Context context) {
        highScores = loadUserData(context);
    }

    /**
     * Returns the list of high score Fish objects
     *
     * @return highScores
     */
    public HighScores getHighScores() {
        return highScores;
    }

    /**
     * Saves user high scores to local file system
     *
     * @param context
     */
    public void saveUserData(Context context) {
        File file = new File(context.getFilesDir(), "highScores.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            Log.v("Saved", "true");
            oos.writeObject(highScores);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads user high scores from local file system
     *
     * @param context
     * @return HighScores to populate a UserDataModel object
     */
    public HighScores loadUserData(Context context) {
        File file = new File(context.getFilesDir(), "highScores.txt");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (HighScores) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new HighScores();
    }
}
