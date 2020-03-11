package br.com.calcard.android.app.ui.cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
import br.com.calcard.android.app.model.Profile;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileViewModel extends ViewModel {

    public ObservableField<String> avatar = new ObservableField<>("");

    public MutableLiveData<String> avatarLiveData = new MutableLiveData<>();

    public ObservableField<String> name = new ObservableField<>("");

    public MutableLiveData<String> nameLiveData = new MutableLiveData<>();

    public ObservableBoolean loadingState = new ObservableBoolean();

    public MutableLiveData<Boolean> success = new MutableLiveData<>();

    public MutableLiveData<String> error = new MutableLiveData<>();

    public MutableLiveData<Boolean> fail = new MutableLiveData<>();

    public ObservableField<String> changeOK = new ObservableField<>("");

    public void updateAvatar(String accessToken, String token) {
        loadingState.set(true);
        String bodyString = "{" +
                "\t\"avatar\" : " + "\"" + avatarLiveData.getValue() + "\",\n" +
                "\t\"nickname\" : " + "\"" + nameLiveData.getValue() + "\"" +
                " }";
        RequestBody body =
                RequestBody.create(MediaType.parse("text/plain"), bodyString);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Profile> call = api.avatarUpdate(accessToken, token, body);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.code() == 200) {
                    MyApplication.preferences.edit().putString("avatar", response.body().getAvatar()).apply();
                    MyApplication.preferences.edit().putString("name", response.body().getNickname()).apply();
                    loadingState.set(false);
                    changeOK.set("true");
                    success.setValue(true);
                } else {
                    changeOK.set("false");
                    JSONObject jObjError;
                    try {
                        assert response.errorBody() != null;
                        jObjError = new JSONArray(response.errorBody().string()).getJSONObject(0);
                        error.postValue(Objects.requireNonNull(jObjError).getString("message"));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    loadingState.set(false);
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                loadingState.set(false);
                changeOK.set("false");
                fail.setValue(true);
            }
        });

    }


    public String base64FromUri(Uri selectedImageUri, Context context) {
        Bitmap imageProfile = null;
        try {
            imageProfile = BitmapFactory.decodeStream(Objects.requireNonNull(context.getContentResolver().openInputStream(selectedImageUri)));
            imageProfile = Bitmap.createScaledBitmap(imageProfile, 250, 250, false);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageProfile.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP);
    }

    public String base64FromExtras(Bundle extras) {
        Bitmap image = (Bitmap) Objects.requireNonNull(extras).get("data");
        image = Bitmap.createScaledBitmap(Objects.requireNonNull(image), 250, 250, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP);

    }

}
