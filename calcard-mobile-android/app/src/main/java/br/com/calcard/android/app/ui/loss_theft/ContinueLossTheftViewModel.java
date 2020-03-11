package br.com.calcard.android.app.ui.loss_theft;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
import br.com.calcard.android.app.utils.Constants;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContinueLossTheftViewModel {

    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<String> responseMessage = new ObservableField<>("");
    public ObservableBoolean progress = new ObservableBoolean();



    public void cancel(String reason){
        progress.set(true);
        String bodyString = "{" +
                "\t\"confirmPassword\": " + "\"" + password.get() + "\"," +
                "\t\"password\": " + "\"" + password.get() + "\"," +
                "\t\"reason\": " + "\"" + reason + "\"" +
                "}";

        RequestBody body =
                RequestBody.create(MediaType.parse("text/plain"), bodyString);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.cancelCard(Constants.BEARER_API,MyApplication.preferences.getString("tokenUser",null),MyApplication.preferences.getString("card",null),body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200){
                    responseMessage.set("ok");
                } else {
                    JSONObject jObjError;
                    try {
                        jObjError = new JSONArray(response.errorBody().string()).getJSONObject(0);
                        if (errorvalid(response.code())) {
                            responseMessage.set(Objects.requireNonNull(jObjError).getString("message"));
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
                responseMessage.set("Servidor fora do ar");
                progress.set(false);

            }
        });
    }

    private boolean errorvalid(int code) {
        return code < 500;
    }

}
