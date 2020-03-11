package br.com.calcard.android.app.ui.insertvalueinvoice;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
import br.com.calcard.android.app.model.InvoiceInstallment;
import br.com.calcard.android.app.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InsertValueViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    public List<InvoiceInstallment> invoiceInstallment = new ArrayList<>();
    public ObservableBoolean isInstallmentButton = new ObservableBoolean(false);

    public void getInstallmentInvoice(String dueDate) {
        invoiceInstallment = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        //TODO - remover para prod
        Call<List<InvoiceInstallment>> call = api.getInstallmentInvoice(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null), dueDate);
        call.enqueue(new Callback<List<InvoiceInstallment>>() {
            @Override
            public void onResponse(Call<List<InvoiceInstallment>> call, Response<List<InvoiceInstallment>> response) {
                if (response.code() == 200) {
                    invoiceInstallment.addAll(response.body());
                    isInstallmentButton.set(invoiceInstallment.size() > 0);
                }
            }

            @Override
            public void onFailure(Call<List<InvoiceInstallment>> call, Throwable t) {
                Log.e("Erro ", t.getMessage());
            }
        });
    }
}
