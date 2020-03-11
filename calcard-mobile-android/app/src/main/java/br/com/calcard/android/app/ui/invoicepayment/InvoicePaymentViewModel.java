package br.com.calcard.android.app.ui.invoicepayment;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
import br.com.calcard.android.app.model.Bill;
import br.com.calcard.android.app.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class InvoicePaymentViewModel extends ViewModel {

    public ObservableBoolean loadingState = new ObservableBoolean(false);
    public MutableLiveData<Boolean> mutableLoadingState = new MutableLiveData<>();
    public ObservableField<String> billOk = new ObservableField<>("");
    public Bill bill = new Bill();
    public static final String TAG = "ContentValues";

    public void getBill(String dueDate, String amount) {
        loadingState.set(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Bill> call = api.getBill(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null), dueDate, amount);
        call.enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {
                if (response.code() == 200) {
                    bill = response.body();
                    mutableLoadingState.postValue(true);
                } else {
                    JSONObject jObjError;
                    try {
                        jObjError = new JSONArray(response.errorBody().string()).getJSONObject(0);
                        if (errorvalid(response.code())) {
                            billOk.set(Objects.requireNonNull(jObjError).getString("message"));
                        } else {
                            try {
                                billOk.set(jObjError.getString("message"));
                            } catch (JSONException e) {
                                billOk.set("Servidor fora do ar");
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }

                mutableLoadingState.postValue(true);
                loadingState.set(true);
            }

            @Override
            public void onFailure(Call<Bill> call, Throwable t) {
                loadingState.set(false);
                Log.d(TAG, "server contact failed");
            }
        });
    }

    private boolean errorvalid(int code) {
        return code < 500;
    }
}
