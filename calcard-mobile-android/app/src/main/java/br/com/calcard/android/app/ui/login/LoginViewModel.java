package br.com.calcard.android.app.ui.login;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
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

import br.com.calcard.android.app.BuildConfig;
import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
import br.com.calcard.android.app.model.Authorization;
import br.com.calcard.android.app.model.UpdateUser;
import br.com.calcard.android.app.utils.CipherUtil;
import br.com.calcard.android.app.utils.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginViewModel extends ViewModel {

    public ObservableField<String> cpf = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableBoolean firstLogin = new ObservableBoolean();
    public ObservableBoolean loginBiometric = new ObservableBoolean();
    public ObservableBoolean succesfulLogin = new ObservableBoolean();
    public ObservableField<String> errorMessage = new ObservableField<>("");
    private MutableLiveData<Authorization> liveData;
    public ObservableBoolean loadingState = new ObservableBoolean();
    public ObservableBoolean isLoginDone = new ObservableBoolean();

    public void setBiometric() {
        loginBiometric.set(MyApplication.preferences.getBoolean("loginBiometric", false));

    }

    public void onClick(String id) {
        liveData = null;
        succesfulLogin.set(true);
        loadingState.set(true);
        login(id);
    }

    private void login(String deviceId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        String doc = Objects.requireNonNull(cpf.get()).replace(".", "").replace("-", "");
        Call<Authorization> call = api.authorize(Constants.BEARER_API, CipherUtil.Companion.encrypt(doc),
                CipherUtil.Companion.encrypt(password.get()), "password", BuildConfig.VERSION_NAME,
                deviceId, "Android", Build.VERSION.RELEASE, Build.MANUFACTURER, Build.MODEL);

        call.enqueue(new Callback<Authorization>() {
            @Override
            public void onResponse(@NonNull Call<Authorization> call, @NonNull Response<Authorization> response) {
                if (response.code() == 200) {
                    MyApplication.preferences.edit().putString("tokenUser", "Bearer " + response.body().getToken().getAccessToken()).apply();
                    MyApplication.preferences.edit().putString("cpf", doc).apply();
                    //temos que discutir uma melhor implementaçao de segurança para senha, como algoritimo de criptografia, etc.
                    MyApplication.preferences.edit().putString("version", password.get()).apply();
                    firstLogin.set(MyApplication.preferences.getBoolean("firstLogin", true));
                    MyApplication.preferences.edit().putBoolean("firstLogin", false).apply();
                    updateUser();
                    if (!firstLogin.get()) {
                        isLoginDone.set(true);
                    }
                } else {
                    JSONObject jObjError;
                    try {
                        jObjError = new JSONArray(response.errorBody().string()).getJSONObject(0);
                        if (errorvalid(response.code())) {
                            errorMessage.set(Objects.requireNonNull(jObjError).getString("message"));
                        } else {
                            try {
                                errorMessage.set(jObjError.getString("message"));
                            } catch (JSONException e) {
                                errorMessage.set("Servidor fora do ar");
                            }
                        }
                        succesfulLogin.set(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                loadingState.set(false);
            }

            @Override
            public void onFailure(Call<Authorization> call, Throwable t) {
                loadingState.set(false);
                errorMessage.set(t.getMessage());
            }
        });

    }

    private void updateUser() {
        UpdateUser updateUser = getUpdateUser();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.updateUser(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null), updateUser);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    System.out.println(response.code());
                    MyApplication.preferences.edit().putBoolean("generateFirebaseToken", false).apply();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("onFaillure chamado ", t.getMessage());
            }
        });
    }

    private UpdateUser getUpdateUser() {
        UpdateUser user = new UpdateUser();
        user.setCpf(MyApplication.preferences.getString("cpf", null));
        user.setFirebaseToken(FirebaseInstanceId.getInstance().getToken());
        return user;
    }

    private boolean errorvalid(int code) {
        return code < 500;
    }
}
