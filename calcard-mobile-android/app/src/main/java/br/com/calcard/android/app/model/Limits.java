package br.com.calcard.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Limits {
    public Double getAvailable() {
        return availableBalanceGlobal;
    }

    public void setAvailable(Double available) {
        this.availableBalanceGlobal = available;
    }

    public Double getUsed() {
        return usedGlobalLimit;
    }

    public void setUsed(Double used) {
        this.usedGlobalLimit = used;
    }

    public Double getWithdraw() {
        return limitGlobal;
    }

    public void setWithdraw(Double withdraw) {
        this.limitGlobal = withdraw;
    }

    @SerializedName("availableBalanceGlobal")
    @Expose
    private Double availableBalanceGlobal;
    @SerializedName("usedGlobalLimit")
    @Expose
    private Double usedGlobalLimit;
    @SerializedName("limitGlobal")
    @Expose
    private Double limitGlobal;

    public Double getAvailableBalanceWithdraw() {
        return availableBalanceWithdraw;
    }

    public void setAvailableBalanceWithdraw(Double availableBalanceWithdraw) {
        this.availableBalanceWithdraw = availableBalanceWithdraw;
    }

    @SerializedName("availableBalanceWithdraw")
    @Expose
    private Double availableBalanceWithdraw;

}
