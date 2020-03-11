package br.com.calcard.android.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.calcard.android.app.R;
import br.com.calcard.android.app.model.SettingsData;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsViewHolder> {
    private Context mContext;
    private List<SettingsData> settingsData;
    private String status;


    public SettingsAdapter(Context mContext, List<SettingsData> settingsData, String status) {
        this.mContext = mContext;
        this.settingsData = settingsData;
        this.status = status;
    }


    @NonNull
    @Override
    public SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_row_item, viewGroup, false);
        return new SettingsViewHolder(mView);
    }


    @Override
    public void onBindViewHolder(@NonNull final SettingsViewHolder holder, int position) {

        holder.mImage.setImageResource(settingsData.get(position).getImgMenu());
        holder.mTitle.setText(settingsData.get(position).getNameMenu());
        if ((isCanceled(status)) && (settingsData.get(position).getNameMenu().equalsIgnoreCase("Alterar senha") || settingsData.get(position).getNameMenu().equalsIgnoreCase("Perda/Roubo") || settingsData.get(position).getNameMenu().equalsIgnoreCase("Desbloqueio de Cartão"))) {
            holder.mImage.setColorFilter(mContext.getResources().getColor(R.color.cinza));
            holder.mImage.setBackground(mContext.getDrawable(R.drawable.button_settins_disable));
            holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.cinza));
        }

    }

    public boolean isCanceled(String stauts) {
        if (stauts.contains("CANCELADO")) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public int getItemCount() {
        return settingsData.size();
    }
}
