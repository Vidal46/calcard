package br.com.calcard.android.app.ui.register;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import br.com.calcard.android.app.api.Api;
import br.com.calcard.android.app.model.TokenSms;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterViewModel extends ViewModel {

    public ObservableField<String> cpf = new ObservableField<>("");

    ObservableField<String> cpfOk = new ObservableField<>("");

    public ObservableField<String> phone = new ObservableField<>();

    public ObservableBoolean loadingState = new ObservableBoolean();

    public MutableLiveData<String> errorHandler = new MutableLiveData<>();

    public MutableLiveData<String> mutableLivePhone = new MutableLiveData<>();

    public MutableLiveData<Boolean> cpfObserver = new MutableLiveData<>();

    public MutableLiveData<Boolean> fail = new MutableLiveData<>();

    public void generateToken(String accessToken, String cpfIntent, String origin) {
        loadingState.set(true);
        String bodyString;
        if (cpfIntent == null) {
            bodyString = "{" +
                    "\t\"cpf\" : " + "\"" + cpf.get().replace(".", "").replace("-", "") + "\"" + "," +
                    "\t\"origin\" : " + "\"" + origin + "\"" + " }";
        } else {
            bodyString = "{" +
                    "\t\"cpf\" : " + "\"" + cpfIntent.replace(".", "").replace("-", "") + "\"" + "," +
                    "\t\"origin\" : " + "\"" + origin + "\"" + " }";

        }
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
        Call<TokenSms> call = api.generateToken(accessToken, body);
        call.enqueue(new Callback<TokenSms>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<TokenSms> call, Response<TokenSms> response) {
                if (response.code() == 200) {
                    phone.set(Objects.requireNonNull(response.body()).getPhone());
                    mutableLivePhone.setValue(Objects.requireNonNull(response.body()).getPhone());
                    cpfOk.set("valido");
                    cpfObserver.setValue(true);
                } else {
                    JSONObject jObjError;
                    try {
                        jObjError = new JSONArray(response.errorBody().string()).getJSONObject(0);
                        if (errorvalid(response.code())) {
                            cpfOk.set(Objects.requireNonNull(jObjError).getString("message"));
                            errorHandler.postValue(Objects.requireNonNull(jObjError).getString("message"));
                        } else {
                            try {
                                cpfOk.set(jObjError.getString("message"));
                                errorHandler.postValue(jObjError.getString("message"));
                            } catch (JSONException e) {
                                errorHandler.postValue("Servidor fora do ar");
                                cpfOk.set("Servidor fora do ar");
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
            public void onFailure(Call<TokenSms> call, Throwable t) {
                cpfOk.set("Erro na conexão do servidor");
                loadingState.set(false);
                fail.setValue(true);
            }
        });
    }

    private boolean errorvalid(int code) {
        return code < 500;
    }
}
