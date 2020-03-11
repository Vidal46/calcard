package br.com.calcard.android.app.ui.travel_notification;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
import br.com.calcard.android.app.model.NotificationTravel;
import br.com.calcard.android.app.utils.Constants;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfirmTravelNotificationViewModel {

    public ObservableField<String> initialDate = new ObservableField<>("");
    public ObservableField<String> finalDate = new ObservableField<>("");
    public ObservableField<String> listOk = new ObservableField<>("");
    public ObservableBoolean progress = new ObservableBoolean();

    public void postNotification(String inicio, String fim, List<String> countriesList) {
        progress.set(true);
        NotificationTravel nt = new NotificationTravel();
        nt.setCardId(MyApplication.preferences.getString("idCard",null));
        nt.setStartDate(inicio);
        nt.setEndDate(fim);
        nt.setCountries(countriesList);
        Gson gson = new Gson();
        String bodyString = gson.toJson(nt);
        RequestBody body =
                RequestBody.create(MediaType.parse("text/plain"), bodyString);

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
        Call<ResponseBody> call = api.postTravelNotification(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null), body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                  listOk.set("ok");
                } else {
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        listOk.set(Objects.requireNonNull(jObjError).getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                progress.set(false);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listOk.set(t.getMessage());
                progress.set(false);

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
