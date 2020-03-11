package br.com.calcard.android.app.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import br.com.calcard.android.app.R;

public class SettingsViewHolder extends RecyclerView.ViewHolder {
    ImageView mImage;
    TextView mTitle;

    SettingsViewHolder(View itemView) {
        super(itemView);
         mImage = itemView.findViewById(R.id.ivImage);
         mTitle = itemView.findViewById(R.id.tvTitle);
    }


}
