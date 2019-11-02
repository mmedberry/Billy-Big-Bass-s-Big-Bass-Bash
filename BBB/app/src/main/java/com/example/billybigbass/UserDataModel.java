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

public class UserDataModel extends ViewModel implements Serializable {
    private HighScores highScores;
    public UserDataModel(Context context){
        highScores=loadUserData(context);
    }
    public HighScores getHighScores(){
        return highScores;
    }

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
