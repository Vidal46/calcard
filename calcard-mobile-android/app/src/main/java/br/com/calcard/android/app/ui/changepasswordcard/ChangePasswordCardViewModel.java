package br.com.calcard.android.app.ui.changepasswordcard;

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

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
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

public class ChangePasswordCardViewModel extends ViewModel {

    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<String> oldPassword = new ObservableField<>("");
    public ObservableField<String> cvv = new ObservableField<>("");
    public ObservableField<String> responseMessage = new ObservableField<>("");
    public ObservableBoolean progress = new ObservableBoolean();
    public MutableLiveData<Boolean> success = new MutableLiveData<>();
    public MutableLiveData<String> error = new MutableLiveData<>();
    public MutableLiveData<Boolean> fail = new MutableLiveData<>();

    public void changePassword(String password, String oldPassword, String cvv) {
        progress.set(true);
        String bodyString = "{" +
                "\t\"confirmPassword\": " + "\"" + password + "\"," +
                "\t\"password\": " + "\"" + password + "\"," +
                "\t\"oldPassword\": " + "\"" + oldPassword + "\"," +
                "\t\"cvv\": " + "\"" + cvv + "\"" +
                "}";


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
        Call<ResponseBody> call = api.changePasswordCard(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null), MyApplication.preferences.getString("card", null), body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    responseMessage.set("ok");
                    success.setValue(true);
                } else {
                    JSONObject jObjError;
                    try {
                        jObjError = new JSONArray(response.errorBody().string()).getJSONObject(0);
                        if (errorvalid(response.code())) {
                            responseMessage.set(Objects.requireNonNull(jObjError).getString("message"));
                            error.setValue(Objects.requireNonNull(jObjError).getString("message"));
                        } else {
                            try {
                                responseMessage.set(jObjError.getString("message"));
                            } catch (JSONException e) {
                                responseMessage.set("Servidor fora do ar");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                progress.set(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                fail.setValue(true);
                responseMessage.set(t.getMessage());
                progress.set(false);
            }
        });
    }

    private boolean errorvalid(int code) {
        return code < 500;
    }
}
