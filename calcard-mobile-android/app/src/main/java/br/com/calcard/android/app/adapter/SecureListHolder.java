package br.com.calcard.android.app.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.calcard.android.app.R;

public class SecureListHolder  extends RecyclerView.ViewHolder {

     TextView description,price;
     View view;

    public SecureListHolder(@NonNull View itemView) {
        super(itemView);
        description= itemView.findViewById(R.id.textView56);
        price= itemView.findViewById(R.id.textView59);
        view= itemView.findViewById(R.id.view29);

    }
}
