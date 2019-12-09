package com.example.billybigbass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class RecordsActivity extends AppCompatActivity {

    private AppDatabase database;
    private UserDataModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        database = AppDatabase.getAppDatabase(this);
        userDataModel = (UserDataModel) getIntent().getSerializableExtra("userDataModel");
        updateUI();
    }

    /**
     * Method to display past fish caught and the record fish of each category
     */
    //TODO Implement recycler view to add scrolling
    public void updateUI() {
        List<Fish> fishList = database.eventDao().getAll();
        List<Fish> highScores = userDataModel.getHighScores().getFishList();
        TableLayout table = findViewById(R.id.resultTable);
        TableLayout highScoresTable = findViewById(R.id.highScoresTable);
        int i = 0;
        for (Fish fish : fishList) {
            i++;
            TableRow tableRow = new TableRow(this);
            TextView textView = new TextView(this);
            String text = String.format("%d. %s, length: %d, weight: %d", i, fish.getName(), fish.getLength(), fish.getWeight());
            textView.setText(text);
            tableRow.addView(textView);
            table.addView(tableRow);
        }
        for (Fish fish : highScores) {
            TableRow tableRow = new TableRow(this);
            TextView textView = new TextView(this);
            String text = String.format("Record %s, length: %d, weight: %d", fish.getName(), fish.getLength(), fish.getWeight());
            textView.setText(text);
            tableRow.addView(textView);
            highScoresTable.addView(tableRow);
        }
    }
}
