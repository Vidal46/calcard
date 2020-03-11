package br.com.calcard.android.app.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import br.com.calcard.android.app.R;


public class DetailsHolder  extends RecyclerView.ViewHolder {
    ImageView img;
    TextView tvTitle,tvDetails,tvData,parcela;
    ConstraintLayout layout;
    public DetailsHolder(@NonNull View itemView) {
        super(itemView);
        img= itemView.findViewById(R.id.imgIcon);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvDetails = itemView.findViewById(R.id.tvDetail);
        tvData = itemView.findViewById(R.id.tvDate);
        layout= itemView.findViewById(R.id.layout);
        parcela= itemView.findViewById(R.id.tvParcela);
    }
}
