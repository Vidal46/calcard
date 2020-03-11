package br.com.calcard.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MigrationElegible implements Serializable {

    @SerializedName("eligibility")
    @Expose
    private String eligibility;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("plLimit")
    @Expose
    private double plLimit;

    @SerializedName("visaLimit")
    @Expose
    private double visaLimit;

    public String getEligibility() {
        return eligibility;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getPlLimit() {
        return plLimit;
    }

    public void setPlLimit(double plLimit) {
        this.plLimit = plLimit;
    }

    public double getVisaLimit() {
        return visaLimit;
    }

    public void setVisaLimit(double visaLimit) {
        this.visaLimit = visaLimit;
    }

}
