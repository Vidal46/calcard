package br.com.calcard.android.app.ui.cards;

import android.util.Log;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
import br.com.calcard.android.app.model.Goals;
import br.com.calcard.android.app.model.InvoiceSumary;
import br.com.calcard.android.app.model.Limits;
import br.com.calcard.android.app.utils.Constants;
import br.com.calcard.android.app.utils.CurrencyHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvoiceDataViewModel extends ViewModel {
    public ObservableField<String> used = new ObservableField<>("");
    public ObservableField<String> available = new ObservableField<>("");
    public ObservableField<String> withdraw = new ObservableField<>("");
    public ObservableField<String> balance = new ObservableField<>("");
    public ObservableField<String> bestDayToBuy = new ObservableField<>("");
    public ObservableField<String> discount = new ObservableField<>("");
    public ObservableField<String> dueDate = new ObservableField<>("");
    public ObservableArrayList<Integer> dates = new ObservableArrayList<>();
    public ObservableField<String> newDateDue = new ObservableField<>("");
    public ObservableField<String> changedDueDate = new ObservableField<>("");
    public List<Goals> goals = new ArrayList<>();
    public ObservableBoolean isExpensesOk = new ObservableBoolean();


    public ObservableBoolean loadingState = new ObservableBoolean();


    public double nUsedPercentage;
    public double usedPercentage;

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
                    withdraw.set(CurrencyHelper.format(response.body().getWithdraw()));
                    available.set(CurrencyHelper.format(response.body().getAvailable()));
                    nUsedPercentage = (response.body().getAvailable() * 100) / response.body().getWithdraw();
                    usedPercentage = 100 - nUsedPercentage;
                    used.set(CurrencyHelper.format(response.body().getUsed()));

                }
                getInvoiceSummary(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null));
            }


            @Override
            public void onFailure(Call<Limits> call, Throwable t) {
                getInvoiceSummary(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null));
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
                                String[] dateParts = invoiceSumary.getBestDayToBuy().split("-");
                                String day = dateParts[2];
                                bestDayToBuy.set(day);
                                String[] dateParts2 = invoiceSumary.getDue().getDueDate().split("-");
                                String day2 = dateParts2[2];
                                dueDate.set(day2);
                                dates.addAll(invoiceSumary.getDue().getAvailableDue());
                                getGoals();
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

    public void changeDueDate(String accessToken, String token) {
        loadingState.set(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.changeDueDate(accessToken, token, MyApplication.preferences.getString("idAccount", null), newDateDue.get());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    dueDate.set(newDateDue.get());
                    changedDueDate.set("ok");
                } else {
                    JSONObject jObjError;
                    try {
                        jObjError = new JSONArray(response.errorBody().string()).getJSONObject(0);
                        if (errorvalid(response.code())) {
                            changedDueDate.set(Objects.requireNonNull(jObjError).getString("message"));
                        } else {
                            try {
                                changedDueDate.set(jObjError.getString("message"));
                            } catch (JSONException e) {
                                changedDueDate.set("Servidor fora do ar");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                loadingState.set(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                changedDueDate.set(t.getMessage());

            }
        });
    }

    public void getGoals() {
        goals.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<List<Goals>> call = api.getGoals(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null), MyApplication.preferences.getString("idAccount", null));
        call.enqueue(new Callback<List<Goals>>() {
            @Override
            public void onResponse(Call<List<Goals>> call, Response<List<Goals>> response) {
                System.out.println(response.raw());
                if (response.code() == 200) {
                    goals.addAll(response.body());
                    isExpensesOk.set(true);
                } else {
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Goals>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private boolean errorvalid(int code) {
        return code < 500;
    }
}
