package br.com.calcard.android.app.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.calcard.android.app.R;

public class CountriesHolder extends RecyclerView.ViewHolder {
    TextView country;
    ImageView check;
    public CountriesHolder(@NonNull View itemView) {
        super(itemView);
        country= itemView.findViewById(R.id.textView71);
        check= itemView.findViewById(R.id.imageView19);
    }
}
