package br.com.calcard.android.app.ui.travel_notification;

import androidx.databinding.ObservableField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
import br.com.calcard.android.app.model.Continent;
import br.com.calcard.android.app.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TravelNotificationViewModel {

    public List<Continent> continents = new ArrayList<>();
    public ObservableField<String> listOk = new ObservableField<>("");

    public void getContinents() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<List<Continent>> call = api.getContinents(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null));
        call.enqueue(new Callback<List<Continent>>() {
            @Override
            public void onResponse(Call<List<Continent>> call, Response<List<Continent>> response) {
                if (response.code() == 200) {
                    continents.addAll(response.body());
                    listOk.set("ok");
                } else {
                    JSONObject jObjError;
                    try {
                        jObjError = new JSONArray(response.errorBody().string()).getJSONObject(0);
                        if (errorvalid(response.code())) {
                            listOk.set(Objects.requireNonNull(jObjError).getString("message"));
                        } else {
                            try {
                                listOk.set(jObjError.getString("message"));
                            } catch (JSONException e) {
                                listOk.set("Servidor fora do ar");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Continent>> call, Throwable t) {
                listOk.set(t.getMessage());
            }
        });
    }

    private boolean errorvalid(int code) {
        return code < 500;
    }

}
