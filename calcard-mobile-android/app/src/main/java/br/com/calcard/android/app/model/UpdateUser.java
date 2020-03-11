package br.com.calcard.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateUser {

    @Expose
    @SerializedName("cpf")
    private String cpf;

    @Expose
    @SerializedName("firebaseToken")
    private String firebaseToken;

    public UpdateUser() {
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
