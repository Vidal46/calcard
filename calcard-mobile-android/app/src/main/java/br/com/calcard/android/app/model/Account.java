package br.com.calcard.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Account {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("daysLate")
    @Expose
    private Integer daysLate;
    @SerializedName("dtRegistration")
    @Expose
    private String dtRegistration;
    @SerializedName("dtLastBuy")
    @Expose
    private String dtLastBuy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDaysLate() {
        return daysLate;
    }

    public void setDaysLate(Integer daysLate) {
        this.daysLate = daysLate;
    }

    public String getDtRegistration() {
        return dtRegistration;
    }

    public void setDtRegistration(String dtRegistration) {
        this.dtRegistration = dtRegistration;
    }

    public String getDtLastBuy() {
        return dtLastBuy;
    }

    public void setDtLastBuy(String dtLastBuy) {
        this.dtLastBuy = dtLastBuy;
    }

}