package br.com.calcard.android.app.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import br.com.calcard.android.app.R;

public class MenuHolder extends RecyclerView.ViewHolder  {
     ImageView img;
     TextView title;
    public MenuHolder(View view){
        super(view);
        img= view.findViewById(R.id.imgIcon);
        title= view.findViewById(R.id.tvtitle);
    }
}
