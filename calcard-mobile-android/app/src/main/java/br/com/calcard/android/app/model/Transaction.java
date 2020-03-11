package br.com.calcard.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Transaction implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("eventTypeId")
    @Expose
    private Integer eventTypeId;
    @SerializedName("eventDescription")
    @Expose
    private String eventDescription;
    @SerializedName("eventId")
    @Expose
    private Integer eventId;
    @SerializedName("accountId")
    @Expose
    private Integer accountId;
    @SerializedName("complement")
    @Expose
    private String complement;
    @SerializedName("real")
    @Expose
    private Double real;
    @SerializedName("dolar")
    @Expose
    private Double dolar;
    @SerializedName("installment")
    @Expose
    private Integer installment;
    @SerializedName("installmentQuantity")
    @Expose
    private Integer installmentQuantity;
    @SerializedName("transactionDateTime")
    @Expose
    private String transactionDateTime;
    @SerializedName("establishmentName")
    @Expose
    private String establishmentName;
    @SerializedName("establishmentDescription")
    @Expose
    private String establishmentDescription;
    @SerializedName("tradingName")
    @Expose
    private String tradingName;
    @SerializedName("creditFlag")
    @Expose
    private Boolean creditFlag;
    @SerializedName("mccId")
    @Expose
    private Integer mccId;
    @SerializedName("mccGroupId")
    @Expose
    private Integer mccGroupId;
    @SerializedName("mccGroupDescription")
    @Expose
    private String mccGroupDescription;
    @SerializedName("personName")
    @Expose
    private String personName;
    @SerializedName("cardNumber")
    @Expose
    private String cardNumber;
    @SerializedName("requestedContestation")
    @Expose
    private Boolean requestedContestation;
    @SerializedName("shippingFeeValue")
    @Expose
    private String shippingFeeValue;
    @SerializedName("descriptionAbbreviated")
    @Expose
    private String descriptionAbbreviated;
    @SerializedName("titular")
    @Expose
    private Boolean titular;
    @SerializedName("expenseGoalName")
    @Expose
    private String expenseGoalName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(Integer eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Object getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public Double getReal() {
        return real;
    }

    public void setReal(Double real) {
        this.real = real;
    }

    public Double getDolar() {
        return dolar;
    }

    public void setDolar(Double dolar) {
        this.dolar = dolar;
    }

    public Integer getInstallment() {
        return installment;
    }

    public void setInstallment(Integer installment) {
        this.installment = installment;
    }

    public Integer getInstallmentQuantity() {
        return installmentQuantity;
    }

    public void setInstallmentQuantity(Integer installmentQuantity) {
        this.installmentQuantity = installmentQuantity;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getEstablishmentName() {
        return establishmentName;
    }

    public void setEstablishmentName(String establishmentName) {
        this.establishmentName = establishmentName;
    }

    public String getEstablishmentDescription() {
        return establishmentDescription;
    }

    public void setEstablishmentDescription(String establishmentDescription) {
        this.establishmentDescription = establishmentDescription;
    }

    public String getTradingName() {
        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public Boolean getCreditFlag() {
        return creditFlag;
    }

    public void setCreditFlag(Boolean creditFlag) {
        this.creditFlag = creditFlag;
    }

    public Integer getMccId() {
        return mccId;
    }

    public void setMccId(Integer mccId) {
        this.mccId = mccId;
    }

    public Integer getMccGroupId() {
        return mccGroupId;
    }

    public void setMccGroupId(Integer mccGroupId) {
        this.mccGroupId = mccGroupId;
    }

    public String getMccGroupDescription() {
        return mccGroupDescription;
    }

    public void setMccGroupDescription(String mccGroupDescription) {
        this.mccGroupDescription = mccGroupDescription;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Boolean getRequestedContestation() {
        return requestedContestation;
    }

    public void setRequestedContestation(Boolean requestedContestation) {
        this.requestedContestation = requestedContestation;
    }

    public String getShippingFeeValue() {
        return shippingFeeValue;
    }

    public void setShippingFeeValue(String shippingFeeValue) {
        this.shippingFeeValue = shippingFeeValue;
    }

    public String getDescriptionAbbreviated() {
        return descriptionAbbreviated;
    }

    public void setDescriptionAbbreviated(String descriptionAbbreviated) {
        this.descriptionAbbreviated = descriptionAbbreviated;
    }

    public Boolean getTitular() {
        return titular;
    }

    public void setTitular(Boolean titular) {
        this.titular = titular;
    }

    public String getExpenseGoalName() {
        return expenseGoalName;
    }

    public void setExpenseGoalName(String expenseGoalName) {
        this.expenseGoalName = expenseGoalName;
    }
}
