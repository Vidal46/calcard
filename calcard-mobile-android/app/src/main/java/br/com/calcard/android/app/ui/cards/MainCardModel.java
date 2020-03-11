package br.com.calcard.android.app.ui.cards;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
import br.com.calcard.android.app.model.Cards;
import br.com.calcard.android.app.model.InvoiceSumary;
import br.com.calcard.android.app.model.Limits;
import br.com.calcard.android.app.model.Profile;
import br.com.calcard.android.app.model.Version;
import br.com.calcard.android.app.utils.Constants;
import br.com.calcard.android.app.utils.CurrencyHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainCardModel extends ViewModel {
    private  MainCardModelCallback callback;

    public ObservableField<String> name = new ObservableField<>("");
    public ObservableField<String> avatar = new ObservableField<>("");
    public ObservableBoolean isInvoiceOpen = new ObservableBoolean(true);
    public ObservableBoolean isReloaded = new ObservableBoolean(false);
    public ObservableField<String> idAccount = new ObservableField<>("");
    public ObservableField<String> limit = new ObservableField<>("");
    public ObservableArrayList<InvoiceSumary> sumarys = new ObservableArrayList<>();
    public MutableLiveData<Boolean> isLastVersion = new MutableLiveData<>();

    public void onClickSettings() {
        if (!isInvoiceOpen.get()) {
            isInvoiceOpen.set(true);
        } else {
            isInvoiceOpen.set(false);
        }
    }

    void getLastVersion(String version) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Version> call = api.updateCheck(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null), version);
        call.enqueue(new Callback<Version>() {
            @Override
            public void onResponse(Call<Version> call, Response<Version> response) {
                String mensagem = getErrorMessage(response.errorBody());
                if (mensagem.contains("false")) {
                    isLastVersion.postValue(true);
                }
            }

            @Override
            public void onFailure(Call<Version> call, Throwable t) {

            }
        });
    }

    private String getErrorMessage(@NonNull ResponseBody errorBody) {
        String errorMessage = "Erro desconhecido";
        if (errorBody != null) {
            try {
                errorMessage = errorBody.string();
            } catch (IOException e) {
                errorMessage = e.getMessage();
            }
        }
        return errorMessage;
    }


    public void profileApi(String accessToken, String userToken) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Profile> call = api.profile(accessToken, userToken);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.code() == 200) {
                    name.set(Objects.requireNonNull(response.body()).getNickname());
                    avatar.set(response.body().getAvatar());
                    MyApplication.preferences.edit().putString("name", name.get()).apply();
                    MyApplication.preferences.edit().putString("avatar", avatar.get()).apply();
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

            }
        });

    }

    public void getAccounts(String accessToken, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<List<Cards>> call = api.getCards(accessToken, token);
        call.enqueue(new Callback<List<Cards>>() {
            @Override
            public void onResponse(Call<List<Cards>> call, Response<List<Cards>> response) {
                if (response.code() == 200) {
                    Cards card = new Cards();
                    if (response.body().size() > 0) {
                        for (int i = 0; i < response.body().size(); i++) {
                            if (!response.body().get(i).getStatus().contains("CANCELADO")) {
                                card = response.body().get(i);
                                break;
                            }
                        }
                        if (card.getStatus() == null) {
                            card = response.body().get(0);
                        }
                        String cardIdAccount = String.valueOf(card.getIdAccount());
                        MyApplication.preferences.edit().putString("idAccount", cardIdAccount).apply();
                        idAccount.set(cardIdAccount);
                        System.out.println("idAccount " + idAccount.get());
                        MyApplication.preferences.edit().putString("statusCard", card.getStatus()).apply();
                        MyApplication.preferences.edit().putBoolean("allowsLock", card.isAllowsLock()).apply();
                        MyApplication.preferences.edit().putBoolean("allowsUnlock", card.isAllowsUnlock()).apply();
                        MyApplication.preferences.edit().putBoolean("firstUnlock", card.isFirstUnlock()).apply();
                        System.out.println(response.body().get(0).getStatus());
                        String account = String.valueOf(card.getIdCard());
                        MyApplication.preferences.edit().putString("card", account).apply();
                        MyApplication.preferences.edit().putBoolean("neverUnlocked", getNeverUnlocked(card.getStage())).apply();
                        getInvoiceSummary(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null));
                        callback.notifyReloadMenu();
                    }
                } else {
                    try {
                        Log.e("ErrorBody", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Cards>> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    private boolean getNeverUnlocked(String stage) {
        return stage.equals("CREATED") || stage.equals("EMBOSSING_PREVIOUS") || stage.equals("RECEIVED") || stage.equals("FORWARDED");
    }

    public void getInvoiceSummary(String accessToken, String token) {
        sumarys.clear();
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
                    sumarys.addAll(response.body());
                } else {
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                getLimits(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null));

            }


            @Override
            public void onFailure(Call<List<InvoiceSumary>> call, Throwable t) {
                System.out.println(t.getMessage());
                getInvoiceSummary(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null));

            }
        });

    }

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

                    limit.set(CurrencyHelper.format(response.body().getAvailable()));

                } else {
                    try {
                        System.out.println("erro no /limits " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }


            @Override
            public void onFailure(Call<Limits> call, Throwable t) {
                System.out.println(t.getMessage());


            }
        });

    }

    public void setMainCardModelCallback(MainCardModelCallback callback) {
    this.callback = callback;
    }
}


