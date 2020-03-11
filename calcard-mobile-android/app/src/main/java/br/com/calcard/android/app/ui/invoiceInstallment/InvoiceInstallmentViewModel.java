package br.com.calcard.android.app.ui.invoiceInstallment;

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

public class InvoiceInstallmentViewModel extends ViewModel {

    public ObservableBoolean isInstallment2 = new ObservableBoolean(false);
    public ObservableBoolean isInstallment3 = new ObservableBoolean(false);
    public ObservableBoolean isInstallment4 = new ObservableBoolean(false);
    public ObservableBoolean isInstallment5 = new ObservableBoolean(false);
    public ObservableBoolean isInstallment6 = new ObservableBoolean(false);
    public ObservableBoolean isInstallment7 = new ObservableBoolean(false);
    public ObservableBoolean isButton = new ObservableBoolean(false);
    public ObservableBoolean success = new ObservableBoolean(false);
    public List<InvoiceInstallment> invoiceInstallment = new ArrayList<>();

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
                    invoiceInstallment.size();
                    success.set(!success.get());
                }
            }

            @Override
            public void onFailure(Call<List<InvoiceInstallment>> call, Throwable t) {
                Log.e("Erro ", t.getMessage());
            }
        });
    }
}
