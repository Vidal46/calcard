package br.com.calcard.android.app.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import br.com.calcard.android.app.R;

public class GoalsHolder extends RecyclerView.ViewHolder {
    TextView tvCategory, tvGoal, tvConsumed;
    ImageView img,arrow;
    View view, viewChild;
    ProgressBar progress;
    ConstraintLayout header,layoutChild;
    TextView value;
    public GoalsHolder(@NonNull View itemView) {
        super(itemView);
        tvCategory= itemView.findViewById(R.id.tvTitle);
        header= itemView.findViewById(R.id.header);
        layoutChild = itemView.findViewById(R.id.layoutChild);
        tvGoal= itemView.findViewById(R.id.tvGoal);
        tvConsumed = itemView.findViewById(R.id.tvGoalConsumed);
        img= itemView.findViewById(R.id.imgGoal);
        view= itemView.findViewById(R.id.view);
        viewChild = itemView.findViewById(R.id.view31);
        progress = itemView.findViewById(R.id.progressBar);
        arrow= itemView.findViewById(R.id.imageView16);
        value= itemView.findViewById(R.id.editValue);

    }
}
