package br.com.calcard.android.app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.calcard.android.app.R;
import br.com.calcard.android.app.model.SecureList;
import br.com.calcard.android.app.utils.CurrencyHelper;

public class SecureListAdapter extends RecyclerView.Adapter {
    private List<SecureList> lists;
    private Activity context;

    public SecureListAdapter(List<SecureList> lists, Activity context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) { View view = LayoutInflater.from(context)
            .inflate(R.layout.recicler_secure_list, viewGroup, false);
        return new SecureListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        SecureListHolder holder = (SecureListHolder) viewHolder;
        SecureList secure = lists.get(i);
        if (lists.size()==i+1 ){
            holder.view.setVisibility(View.INVISIBLE);
        }
        holder.description.setText(secure.getDescription());
        holder.price.setText(CurrencyHelper.format(secure.getPrice()));

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
