package br.com.calcard.android.app.adapter;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.calcard.android.app.R;

public class ContinentsHolder extends RecyclerView.ViewHolder {
    Button btn;
    public ContinentsHolder(@NonNull View itemView) {
        super(itemView);
        btn= itemView.findViewById(R.id.btn);
    }
}
