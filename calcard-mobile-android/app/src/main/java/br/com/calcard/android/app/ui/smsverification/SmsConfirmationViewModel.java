package br.com.calcard.android.app.ui.smsverification;

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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SmsConfirmationViewModel extends ViewModel {

    ObservableField<String> tokenOk = new ObservableField<>("");
    public ObservableField<String> cpf = new ObservableField<>("");
    public ObservableField<String> cpfOk = new ObservableField<>("");
    public ObservableField<String> phone = new ObservableField<>();
    public ObservableBoolean loadingState = new ObservableBoolean();
    public MutableLiveData<Boolean> successCodeLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorCodeLiveDataBoolean = new MutableLiveData<>();
    public MutableLiveData<String> errorCodeLiveDataString = new MutableLiveData<>();
    public MutableLiveData<Boolean> failCodeLiveData = new MutableLiveData<>();


    public void validateToken(String accessToken, String cpf, String token) {
        loadingState.set(true);
        String bodyString = "{" +
                "\t\"cpf\" : " + "\"" + cpf + "\",\n" +
                "\t\"token\" : " + "\"" + token + "\"" +
                " }";
        RequestBody body =
                RequestBody.create(MediaType.parse("text/plain"), bodyString);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.validateToken(accessToken, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    tokenOk.set("ok");
                    successCodeLiveData.postValue(true);
                } else {
                    tokenOk.set("n");
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONArray(response.errorBody().string()).getJSONObject(0);
                        try {
                            errorCodeLiveDataString.postValue(Objects.requireNonNull(jObjError).getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
                loadingState.set(false);
                errorCodeLiveDataBoolean.postValue(true);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                tokenOk.set("Erro na conexão do servidor");
                loadingState.set(false);
                failCodeLiveData.setValue(true);
            }

        });
    }

    public void generateToken(String accessToken, String cpf) {
        loadingState.set(true);
        String bodyString = "{" +
                "\t\"cpf\" : " + "\"" + cpf.replace(".", "").replace("-", "") + "\"" +
                " }";
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
            @Override
            public void onResponse(Call<TokenSms> call, Response<TokenSms> response) {
                if (response.code() == 200) {
                    phone.set(Objects.requireNonNull(response.body()).getPhone());
                    cpfOk.set("valido");
                } else {
                    JSONObject jObjError;
                    try {
                        jObjError = new JSONArray(response.errorBody().string()).getJSONObject(0);
                        if (errorvalid(response.code())) {
                            cpfOk.set(Objects.requireNonNull(jObjError).getString("message"));
                        } else {
                            try {
                                cpfOk.set(jObjError.getString("message"));
                            } catch (JSONException e) {
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
            }
        });
    }

    private boolean errorvalid(int code) {
        return code < 500;
    }
}
