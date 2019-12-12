package com.example.billybigbass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

/**
 * Activity to display past caught fish and high scores
 */
public class RecordsActivity extends AppCompatActivity {

    private AppDatabase database;
    private UserDataModel userDataModel;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        database = AppDatabase.getAppDatabase(this);
        userDataModel = (UserDataModel) getIntent().getSerializableExtra("userDataModel");
        recyclerView = findViewById(R.id.resultTable);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        recyclerView.setAdapter(new ResultsAdapter(database.eventDao().getAll()));
        updateUI();
    }

    /**
     * Method to display high scores
     */
    private void updateUI() {
        List<Fish> highScores = userDataModel.getHighScores().getFishList();
        TableLayout highScoresTable = findViewById(R.id.highScoresTable);
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
