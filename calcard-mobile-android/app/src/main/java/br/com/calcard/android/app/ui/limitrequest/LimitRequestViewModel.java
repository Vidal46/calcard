package br.com.calcard.android.app.ui.limitrequest;

import androidx.databinding.ObservableField;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
import br.com.calcard.android.app.model.Limits;
import br.com.calcard.android.app.utils.Constants;
import br.com.calcard.android.app.utils.CurrencyHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LimitRequestViewModel {
    public ObservableField<String> available = new ObservableField<>("");
    public ObservableField<String> withdraw = new ObservableField<>("");
    public ObservableField<String> used = new ObservableField<>("");
    public ObservableField<Double> valueProgress = new ObservableField<>();
    public ObservableField<Double> total = new ObservableField<>();

    public void getLimits (String accessToken, String token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Limits> call= api.getLimits(accessToken,token, MyApplication.preferences.getString("idCard",null));
        call.enqueue(new Callback<Limits>() {
            @Override
            public void onResponse(Call<Limits> call, Response<Limits> response) {
                if (response.code()==200){
                    available.set(CurrencyHelper.format(response.body().getAvailable()));
                    withdraw.set(CurrencyHelper.format(response.body().getWithdraw()));
                    used.set(CurrencyHelper.format(response.body().getUsed()));
                    valueProgress.set(response.body().getUsed());
                    total.set(response.body().getWithdraw());

                }
            }

            @Override
            public void onFailure(Call<Limits> call, Throwable t) {
                getLimits(Constants.BEARER_API,MyApplication.preferences.getString("tokenUser",null));

            }
        });

    }

}
