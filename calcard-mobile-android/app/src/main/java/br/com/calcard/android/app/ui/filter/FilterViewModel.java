package br.com.calcard.android.app.ui.filter;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import java.util.ArrayList;
import java.util.List;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
import br.com.calcard.android.app.model.Expenses;
import br.com.calcard.android.app.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FilterViewModel {
    public ObservableField<String> initialDate = new ObservableField<>("");
    public ObservableField<String> finalDate = new ObservableField<>("");
    public List<Expenses> expenses = new ArrayList<>();
    public ObservableBoolean isExpensesOk = new ObservableBoolean();


    public void getExpenses(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<List<Expenses>> call = api.getExpenses(Constants.BEARER_API,MyApplication.preferences.getString("tokenUser",null));
        call.enqueue(new Callback<List<Expenses>>() {
            @Override
            public void onResponse(Call<List<Expenses>> call, Response<List<Expenses>> response) {
                if (response.code()==200){
                    expenses.addAll(response.body());
                    isExpensesOk.set(true);
                }
            }

            @Override
            public void onFailure(Call<List<Expenses>> call, Throwable t) {

            }
        });

    }
    public String getDate(String date){
        if (date.contains("/")){
            String init[] = date.split("/");
            String day= init[0];
            String month = init[1];
            String year = init[2];
            return String.valueOf(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
        }
        return null;
    }

}
