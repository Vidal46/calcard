package br.com.calcard.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InvoiceSumary implements Serializable {
    @SerializedName("balance")
    @Expose
    private Double balance;
    @SerializedName("bestDayToBuy")
    @Expose
    private String bestDayToBuy;
    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("due")
    @Expose
    private Due due;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("paid")
    @Expose
    private Boolean paid;
    @SerializedName("valueMinimumPayment")
    @Expose
    private Double valueMinimumPayment;
    @SerializedName("availability")
    @Expose
    private String availability;

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getBestDayToBuy() {
        return bestDayToBuy;
    }

    public void setBestDayToBuy(String bestDayToBuy) {
        this.bestDayToBuy = bestDayToBuy;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Due getDue() {
        return due;
    }

    public void setDue(Due due) {
        this.due = due;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Double getValueMinimumPayment() {
        return valueMinimumPayment;
    }

    public void setValueMinimumPayment(Double valueMinimumPayment) {
        this.valueMinimumPayment = valueMinimumPayment;
    }
}
