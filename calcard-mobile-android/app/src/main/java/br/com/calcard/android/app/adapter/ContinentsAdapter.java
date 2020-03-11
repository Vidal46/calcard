package br.com.calcard.android.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.calcard.android.app.R;
import br.com.calcard.android.app.model.Continent;
import br.com.calcard.android.app.ui.travel_notification.ChooseCountryActivity;

public class ContinentsAdapter extends RecyclerView.Adapter {

    private List<Continent> continents;
    private Activity context;

    public ContinentsAdapter(List<Continent> continents, Activity context) {
        this.continents = continents;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recycler_buttons_continent, viewGroup, false);
        return new ContinentsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ContinentsHolder holder = (ContinentsHolder) viewHolder;
        Continent continent = continents.get(i);
        holder.btn.setText(continent.getName());
        holder.btn.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChooseCountryActivity.class);
            intent.putExtra("name", continent.getName());
            intent.putExtra("code", continent.getCode());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return continents.size();
    }
}
