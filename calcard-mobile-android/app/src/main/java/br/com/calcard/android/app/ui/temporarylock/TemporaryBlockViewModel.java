package br.com.calcard.android.app.ui.temporarylock;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import java.util.concurrent.TimeUnit;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TemporaryBlockViewModel {
    public ObservableField<String> cvv = new ObservableField<>("");
    public ObservableField<String> isCvvUnlocked = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<String> passwordConfirm = new ObservableField<>("");
    public ObservableBoolean isLoading = new ObservableBoolean();

    public void lock(String accessToken,String token, String id){
        isLoading.set(true);
        String bodyString = "{" +
                "\t\"confirmPassword\" : " + "\"" + passwordConfirm.get() + "\",\n" +
                "\t\"password\" : " + "\"" + password.get() + "\",\n" +
                "\t\"cvv\" : " + "\"" + cvv.get() + "\"" +
                " }";

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        RequestBody body =
                RequestBody.create(MediaType.parse("text/plain"), bodyString);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.lock(accessToken,token,id,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200){
                    MyApplication.preferences.edit().putString("statusCard","BLOQUEADO").apply();
                    isCvvUnlocked.set("locked");
                }else {
                    isCvvUnlocked.set("error");
                }
                isLoading.set(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isCvvUnlocked.set("error");
                isLoading.set(false);

            }
        });
    }

}
