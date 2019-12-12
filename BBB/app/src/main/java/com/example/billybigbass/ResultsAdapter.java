package com.example.billybigbass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder> {
    private List<Fish> fishList;

    public static class ResultsViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;

        public ResultsViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.fishData);
        }
    }

    @NonNull
    @Override
    public ResultsAdapter.ResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.fish_result, parent, false);
        return new ResultsViewHolder( v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsAdapter.ResultsViewHolder holder, int position) {
        Fish fish = fishList.get(position);
        String text = String.format("%d. %s, length: %d, weight: %d", position+1, fish.getName(), fish.getLength(), fish.getWeight());
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return fishList.size();
    }

    public ResultsAdapter(List<Fish> fishList) {
        this.fishList = fishList;
    }
}
