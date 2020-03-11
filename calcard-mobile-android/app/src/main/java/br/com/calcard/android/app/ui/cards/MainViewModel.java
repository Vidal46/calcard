package br.com.calcard.android.app.ui.cards;

import android.util.Log;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
import br.com.calcard.android.app.model.Detail;
import br.com.calcard.android.app.model.Goals;
import br.com.calcard.android.app.model.InvoiceSumary;
import br.com.calcard.android.app.model.LastTransaction;
import br.com.calcard.android.app.model.Limits;
import br.com.calcard.android.app.model.Spending;
import br.com.calcard.android.app.model.Timeline;
import br.com.calcard.android.app.model.Transaction;
import br.com.calcard.android.app.utils.Constants;
import br.com.calcard.android.app.utils.CurrencyHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainViewModel extends ViewModel {
    public ObservableField<String> avaliable = new ObservableField<>("");
    public ObservableField<String> dispo = new ObservableField<>("");
    public ObservableField<String> rapido = new ObservableField<>("");
    public ObservableField<String> limit = new ObservableField<>("");
    public ObservableField<Double> used = new ObservableField<>();
    public ObservableField<Double> withdraw = new ObservableField<>();
    public List<Spending> spendings = new ArrayList<>();
    public MutableLiveData<Timeline> mutableTimeLine = new MutableLiveData<>();
    public ObservableField<String> listOk = new ObservableField<>("");
    public ObservableField<Integer> page = new ObservableField<>();
    public ObservableBoolean hasNextPage = new ObservableBoolean();
    public MutableLiveData<Boolean> error = new MutableLiveData<Boolean>();

    //

    public ObservableField<String> balance = new ObservableField<>("");
    public ObservableField<String> bestDayToBuy = new ObservableField<>("");
    public ObservableField<String> discount = new ObservableField<>("");
    public ObservableField<String> dueDate = new ObservableField<>("");
    public ObservableArrayList<Integer> dates = new ObservableArrayList<>();
    public List<Goals> goals = new ArrayList<>();


    //

    public List<Transaction> transactions = new ArrayList<>();
    public LastTransaction lastTransaction = null;
    public ObservableField<String> detailsOk = new ObservableField<>("");


    public void getLimits(String accessToken, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Limits> call = api.getLimits(accessToken, token, MyApplication.preferences.getString("idAccount", null));
        call.enqueue(new Callback<Limits>() {
            @Override
            public void onResponse(Call<Limits> call, Response<Limits> response) {
                if (response.code() == 200) {
                    avaliable.set(CurrencyHelper.format(response.body().getAvailable()));
                    withdraw.set(response.body().getWithdraw());
                    limit.set(CurrencyHelper.format(response.body().getWithdraw()));
                    used.set(response.body().getUsed());
                    dispo.set(CurrencyHelper.format(response.body().getUsed()));
                    rapido.set(CurrencyHelper.format(response.body().getAvailableBalanceWithdraw()));
                } else {
                    error.setValue(true);
                    try {
                        Log.e("ErrorBody", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Limits> call, Throwable t) {
                error.setValue(true);
                getLimits(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null));
                t.getMessage();

            }
        });

    }

    public void getTimeline(String accessToken, String token, Map<String, String> data) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Timeline> call = api.getTimeline(accessToken, token, MyApplication.preferences.getString("idAccount", null), data);
        call.enqueue(new Callback<Timeline>() {
            @Override
            public void onResponse(Call<Timeline> call, Response<Timeline> response) {
                if (response.code() == 200) {

                    spendings.addAll(response.body().getContent());
                    listOk.set("ok");
                    page.set(response.body().getNumber());
                    hasNextPage.set(response.body().isHasNextPage());

                    mutableTimeLine.setValue(response.body());

                    if (hasNextPage.get()) {
                        data.put("page", String.valueOf(page.get() + 1));
                        getTimeline(accessToken, token, data);
                    }
                } else {
                    error.setValue(true);
                }


            }

            @Override
            public void onFailure(Call<Timeline> call, Throwable t) {
                error.setValue(true);
            }
        });

    }

    public void getPagedTimeLine(String accessToken, String token, Map<String, String> data) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Timeline> call = api.getTimeline(accessToken, token, MyApplication.preferences.getString("idAccount", null), data);
        call.enqueue(new Callback<Timeline>() {
            @Override
            public void onResponse(Call<Timeline> call, Response<Timeline> response) {
                if (response.isSuccessful()) {
                    mutableTimeLine.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Timeline> call, Throwable t) {

            }
        });
    }

    public void getInvoiceSummary(String accessToken, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<List<InvoiceSumary>> call = api.getInvoiceSummary(accessToken, token, MyApplication.preferences.getString("idAccount", null));
        call.enqueue(new Callback<List<InvoiceSumary>>() {
            @Override
            public void onResponse(Call<List<InvoiceSumary>> call, Response<List<InvoiceSumary>> response) {
                if (response.code() == 200) {
                    if (response.body().size() > 0) {
                        for (InvoiceSumary invoiceSumary : response.body()) {
                            if (invoiceSumary.getStatus().equals("OPEN")) {
                                discount.set(CurrencyHelper.format(invoiceSumary.getDiscount()));
                                balance.set(CurrencyHelper.format(invoiceSumary.getBalance()));
                                bestDayToBuy.set(invoiceSumary.getBestDayToBuy());
                                dueDate.set(invoiceSumary.getDue().getDueDate());
                                dates.addAll(invoiceSumary.getDue().getAvailableDue());
                                break;
                            }
                        }
                    } else {
                        Log.e("No InvoiceSummary ", response.body().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<InvoiceSumary>> call, Throwable t) {
                t.getMessage();
            }
        });

    }

    public void getDetails(String accessToken, String token, String id, String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Detail> call = api.getDetails(accessToken, token, id, query);
        call.enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                if (response.code() == 200) {
                    transactions.clear();
                    transactions.addAll(response.body().getTransactions());
                    detailsOk.set("ok");
                } else {
                    error.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    public void getLastTransaction(String accessToken, String token, String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<LastTransaction> call = api.lastTransaction(accessToken, token, id);
        call.enqueue(new Callback<LastTransaction>() {
            @Override
            public void onResponse(Call<LastTransaction> call, Response<LastTransaction> response) {
                if (response.code() == 200) {
                    lastTransaction = response.body();
                }
                detailsOk.set("ok");
            }

            @Override
            public void onFailure(Call<LastTransaction> call, Throwable t) {
                t.getMessage();
            }
        });
    }


}
