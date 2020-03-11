package br.com.calcard.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Spendin implements Serializable {
    @SerializedName("accountId")
    @Expose
    private Integer accountId;
    @SerializedName("registryType")
    @Expose
    private String registryType;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("transactionId")
    @Expose
    private Integer transactionId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("descriptionStatus")
    @Expose
    private String descriptionStatus;
    @SerializedName("value")
    @Expose
    private Double value;
    @SerializedName("dolarValue")
    @Expose
    private Double dolarValue;
    @SerializedName("installments")
    @Expose
    private Integer installments;
    @SerializedName("installmentValue")
    @Expose
    private Double installmentValue;
    @SerializedName("eventDate")
    @Expose
    private String eventDate;
    @SerializedName("establishment")
    @Expose
    private String establishment;
    @SerializedName("credit")
    @Expose
    private Boolean credit;
    @SerializedName("establishmentType")
    @Expose
    private String establishmentType;
    @SerializedName("mccGroupId")
    @Expose
    private Integer mccGroupId;
    @SerializedName("disputeRequested")
    @Expose
    private Boolean disputeRequested;
    @SerializedName("transactionType")
    @Expose
    private String transactionType;
    @SerializedName("lastPostedInstallment")
    @Expose
    private Integer lastPostedInstallment;
    @SerializedName("expenseGoal")
    @Expose
    private ExpenseGoal expenseGoal;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getRegistryType() {
        return registryType;
    }

    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescriptionStatus() {
        return descriptionStatus;
    }

    public void setDescriptionStatus(String descriptionStatus) {
        this.descriptionStatus = descriptionStatus;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getDolarValue() {
        return dolarValue;
    }

    public void setDolarValue(Double dolarValue) {
        this.dolarValue = dolarValue;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    public Double getInstallmentValue() {
        return installmentValue;
    }

    public void setInstallmentValue(Double installmentValue) {
        this.installmentValue = installmentValue;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEstablishment() {
        return establishment;
    }

    public void setEstablishment(String establishment) {
        this.establishment = establishment;
    }

    public Boolean getCredit() {
        return credit;
    }

    public void setCredit(Boolean credit) {
        this.credit = credit;
    }

    public String getEstablishmentType() {
        return establishmentType;
    }

    public void setEstablishmentType(String establishmentType) {
        this.establishmentType = establishmentType;
    }

    public Integer getMccGroupId() {
        return mccGroupId;
    }

    public void setMccGroupId(Integer mccGroupId) {
        this.mccGroupId = mccGroupId;
    }

    public Boolean getDisputeRequested() {
        return disputeRequested;
    }

    public void setDisputeRequested(Boolean disputeRequested) {
        this.disputeRequested = disputeRequested;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getLastPostedInstallment() {
        return lastPostedInstallment;
    }

    public void setLastPostedInstallment(Integer lastPostedInstallment) {
        this.lastPostedInstallment = lastPostedInstallment;
    }

    public ExpenseGoal getExpenseGoal() {
        return expenseGoal;
    }

    public void setExpenseGoal(ExpenseGoal expenseGoal) {
        this.expenseGoal = expenseGoal;
    }

}