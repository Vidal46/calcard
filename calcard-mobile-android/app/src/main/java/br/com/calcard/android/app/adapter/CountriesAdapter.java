package br.com.calcard.android.app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.calcard.android.app.R;
import br.com.calcard.android.app.model.Countries;

public class CountriesAdapter extends RecyclerView.Adapter {
    private List<Countries> countries;
    private Activity context;

    public CountriesAdapter(List<Countries> countries, Activity context) {
        this.countries = countries;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recycler_countrys, viewGroup, false);
        return new CountriesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        CountriesHolder holder = (CountriesHolder) viewHolder;
        Countries country = countries.get(i);
        holder.country.setText(country.getName());

    }

    @Override
    public int getItemCount() {
        return countries.size();
    }
}
