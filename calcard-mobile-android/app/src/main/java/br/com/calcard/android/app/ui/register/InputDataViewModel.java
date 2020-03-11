package br.com.calcard.android.app.ui.register;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
import br.com.calcard.android.app.model.RegisterRequest;
import br.com.calcard.android.app.utils.Constants;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InputDataViewModel extends ViewModel {
    public ObservableField<String> token = new ObservableField<>("");
    public ObservableField<String> cpf = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<String> passwordConfirm = new ObservableField<>("");
    public ObservableField<String> isRegisterOk = new ObservableField<>("");
    public ObservableBoolean passwordMatch = new ObservableBoolean();
    public ObservableBoolean wrongPassword = new ObservableBoolean();
    public ObservableBoolean loadingState = new ObservableBoolean();
    public ObservableBoolean forgot = new ObservableBoolean();
    public MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();
    public MutableLiveData<Boolean> registerFail = new MutableLiveData<>();

    public void onClick(String id) {
        if (passwordMatch.get() && !password.get().isEmpty()) {
            if (forgot.get()) {
//                forgot(id);
            } else {
//                register(id);
            }
        } else {
            wrongPassword.set(true);
        }
    }

    private RegisterRequest getRegisterRequest(String deviceId, String cpf, String token, String password) {
        RegisterRequest register = new RegisterRequest();
        register.setCpf(cpf.replace(".", "").replace("-", ""));
        register.setToken(token);
        register.setPassword(password);
        register.setDeviceId(deviceId);
        register.setFirebaseToken(FirebaseInstanceId.getInstance().getToken());
        return register;
    }

    public void register(String deviceId, String cpf, String token, String password) {
        loadingState.set(true);
        RegisterRequest registerRequest = getRegisterRequest(deviceId, cpf, token, password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.register(Constants.BEARER_API, registerRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    isRegisterOk.set("ok");
                    registerSuccess.postValue(true);
                    MyApplication.preferences.edit().putBoolean("generateFirebaseToken", false).apply();

                } else {
                    registerFail.postValue(true);
                    JSONObject jObjError;
                    try {
                        jObjError = new JSONArray(response.errorBody().string()).getJSONObject(0);
                        if (errorvalid(response.code())) {
                            isRegisterOk.set(Objects.requireNonNull(jObjError).getString("message"));
                        } else {
                            try {
                                isRegisterOk.set(jObjError.getString("message"));
                            } catch (JSONException e) {
                                isRegisterOk.set("Servidor fora do ar");
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
                System.out.println(t.getMessage());
                isRegisterOk.set(t.getMessage());
                loadingState.set(false);

            }
        });

    }


    public void forgot(String deviceId, String cpf, String token, String password) {
        loadingState.set(true);
        String bodyString = "{" +
                "\t\"cpf\": " + "\"" + cpf.replace(".", "").replace("-", "") + "\"," +
                "\t\"token\": " + "\"" + token + "\"," +
                "\t\"deviceId\": " + "\"" + deviceId + "\"," +
                "\t\"password\": " + "\"" + password + "\"" +
                "}";

        RequestBody body =
                RequestBody.create(MediaType.parse("text/plain"), bodyString);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.changePassword(Constants.BEARER_API, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    MyApplication.preferences.edit().putString("cpf", cpf.replace(".", "").replace("-", "")).apply();
                    MyApplication.preferences.edit().putString("version", password).apply();
                    isRegisterOk.set("ok");
                    registerSuccess.postValue(true);
                } else {
                    JSONObject jObjError;
                    try {
                        jObjError = new JSONArray(response.errorBody().string()).getJSONObject(0);
                        if (errorvalid(response.code())) {
                            isRegisterOk.set(Objects.requireNonNull(jObjError).getString("message"));
                        } else {
                            try {
                                isRegisterOk.set(jObjError.getString("message"));
                            } catch (JSONException e) {
                                isRegisterOk.set("Servidor fora do ar");
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
                isRegisterOk.set(t.getMessage());
                loadingState.set(false);

            }
        });
    }

    private boolean errorvalid(int code) {
        return code < 500;
    }
}
