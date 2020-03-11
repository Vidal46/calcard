package br.com.calcard.android.app.model;

import com.google.gson.annotations.SerializedName;

public class Version {
    @SerializedName("available")
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
