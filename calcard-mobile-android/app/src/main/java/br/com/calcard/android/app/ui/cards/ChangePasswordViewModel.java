package br.com.calcard.android.app.ui.cards;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
import br.com.calcard.android.app.model.Authorization;
import br.com.calcard.android.app.model.Profile;
import br.com.calcard.android.app.utils.Constants;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePasswordViewModel extends ViewModel {

    public ObservableBoolean loadingState = new ObservableBoolean();
    public ObservableField<String> isChangeDone = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public MutableLiveData<Boolean> success = new MutableLiveData<>();
    public MutableLiveData<String> error = new MutableLiveData<>();
    public MutableLiveData<Boolean> fail = new MutableLiveData<>();

    public void changePassword(String oldPassword, String newPassword) {
        loadingState.set(true);
        String bodyString = "{" +
                "\t\"newPassword\" : " + "\"" + newPassword + "\",\n" +
                "\t\"oldPassword\" : " + "\"" + oldPassword + "\"" +
                " }";
        RequestBody body =
                RequestBody.create(MediaType.parse("text/plain"), bodyString);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Profile> call = api.avatarUpdate(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null), body);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.code() == 200) {
                    success.setValue(true);
                } else {
                    isChangeDone.set("false");

                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONArray(response.errorBody().string()).getJSONObject(0);
                        try {
                            error.postValue(Objects.requireNonNull(jObjError).getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
                loadingState.set(false);
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                fail.setValue(true);
            }
        });
    }
}
