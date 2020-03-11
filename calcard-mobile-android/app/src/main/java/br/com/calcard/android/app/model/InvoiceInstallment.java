package br.com.calcard.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceInstallment {

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("rate")
    @Expose
    private Double rate;

    @SerializedName("costEffective")
    @Expose
    private Double costEffective;

    @SerializedName("iof")
    @Expose
    private Double iof;

    @SerializedName("tac")
    @Expose
    private Double tac;

    @SerializedName("amount")
    @Expose
    private Double amount;

    @SerializedName("invoiceAmount")
    @Expose
    private Double invoiceAmount;

    @SerializedName("firstDueDate")
    @Expose
    private String firstDueDate;

    @SerializedName("lastDueDate")
    @Expose
    private String lastDueDate;

    public InvoiceInstallment() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getCostEffective() {
        return costEffective;
    }

    public void setCostEffective(Double costEffective) {
        this.costEffective = costEffective;
    }

    public Double getIof() {
        return iof;
    }

    public void setIof(Double iof) {
        this.iof = iof;
    }

    public Double getTac() {
        return tac;
    }

    public void setTac(Double tac) {
        this.tac = tac;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getFirstDueDate() {
        return firstDueDate;
    }

    public void setFirstDueDate(String firstDueDate) {
        this.firstDueDate = firstDueDate;
    }

    public String getLastDueDate() {
        return lastDueDate;
    }

    public void setLastDueDate(String lastDueDate) {
        this.lastDueDate = lastDueDate;
    }
}
