package br.com.calcard.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Detail {
    @SerializedName("accountId")
    @Expose
    private Integer accountId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("payed")
    @Expose
    private Boolean payed;
    @SerializedName("dueDate")
    @Expose
    private String dueDate;
    @SerializedName("dueDateReal")
    @Expose
    private String dueDateReal;
    @SerializedName("closingDate")
    @Expose
    private String closingDate;
    @SerializedName("value")
    @Expose
    private Double value;
    @SerializedName("valueMinimumPayment")
    @Expose
    private Double valueMinimumPayment;
    @SerializedName("previousBalance")
    @Expose
    private Double previousBalance;
    @SerializedName("bill")
    @Expose
    private String bill;
    @SerializedName("transactions")
    @Expose
    private List<Transaction> transactions = null;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getPayed() {
        return payed;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueDateReal() {
        return dueDateReal;
    }

    public void setDueDateReal(String dueDateReal) {
        this.dueDateReal = dueDateReal;
    }

    public String getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(String closingDate) {
        this.closingDate = closingDate;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getValueMinimumPayment() {
        return valueMinimumPayment;
    }

    public void setValueMinimumPayment(Double valueMinimumPayment) {
        this.valueMinimumPayment = valueMinimumPayment;
    }

    public Double getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(Double previousBalance) {
        this.previousBalance = previousBalance;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
