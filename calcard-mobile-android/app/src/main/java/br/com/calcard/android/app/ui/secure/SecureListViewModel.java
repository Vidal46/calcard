package br.com.calcard.android.app.ui.secure;

import androidx.databinding.ObservableField;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
import br.com.calcard.android.app.model.SecureList;
import br.com.calcard.android.app.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecureListViewModel {
    public List<SecureList> insurances = new ArrayList<>();
    public ObservableField<String> listOk = new ObservableField<>("");

    public void getInsurances(){
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
        Call <List<SecureList>> call = api.getInsurance(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser",null),MyApplication.preferences.getString("card",null));
        call.enqueue(new Callback<List<SecureList>>() {
            @Override
            public void onResponse(Call<List<SecureList>> call, Response<List<SecureList>> response) {
                if (response.code()==200){
                    insurances.addAll(response.body());
                    listOk.set("ok");
                }else {
                    JSONObject jObjError=null;
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

            }

            @Override
            public void onFailure(Call<List<SecureList>> call, Throwable t) {
                listOk.set(t.getMessage());

            }
        });
    }


}
