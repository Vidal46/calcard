package br.com.calcard.android.app.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cards  {

    @Expose
    @SerializedName("idCard")
    private Long idCard;

    @Expose
    @SerializedName("idAccount")
    private Long idAccount;

    @Expose
    @SerializedName("idPlatform")
    private Long idPlatform;

    @Expose
    @SerializedName("platform")
    private String platform;

    @Expose
    @SerializedName("idAccountPlatform")
    private Long idAccountPlatform;

    @Expose
    @SerializedName("stage")
    private String stage;

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("embossingName")
    private String embossingName;

    @Expose
    @SerializedName("cardNumber")
    private String cardNumber;

    @Expose
    @SerializedName("cardSequencial")
    private Integer cardSequence;

    @Expose
    @SerializedName("titular")
    private Boolean holder;

    @Expose
    @SerializedName("validAt")
    private String validAt;

    @Expose
    @SerializedName("sinceAt")
    private String sinceAt;

    @Expose
    @SerializedName("allowsUnlock")
    private boolean allowsUnlock;

    @Expose
    @SerializedName("allowsLock")
    private boolean allowsLock;

    @Expose
    @SerializedName("firstUnlock")
    private boolean firstUnlock;

    public boolean isAllowsUnlock() {
        return allowsUnlock;
    }

    public void setAllowsUnlock(boolean allowsUnlock) {
        this.allowsUnlock = allowsUnlock;
    }

    public boolean isAllowsLock() {
        return allowsLock;
    }

    public void setAllowsLock(boolean allowsLock) {
        this.allowsLock = allowsLock;
    }

    public boolean isFirstUnlock() {
        return firstUnlock;
    }

    public void setFirstUnlock(boolean firstUnlock) {
        this.firstUnlock = firstUnlock;
    }

    public Long getIdCard() {
        return idCard;
    }

    public void setIdCard(Long idCard) {
        this.idCard = idCard;
    }

    public Long getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Long idAccount) {
        this.idAccount = idAccount;
    }

    public Long getIdPlatform() {
        return idPlatform;
    }

    public void setIdPlatform(Long idPlatform) {
        this.idPlatform = idPlatform;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Long getIdAccountPlatform() {
        return idAccountPlatform;
    }

    public void setIdAccountPlatform(Long idAccountPlatform) {
        this.idAccountPlatform = idAccountPlatform;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmbossingName() {
        return embossingName;
    }

    public void setEmbossingName(String embossingName) {
        this.embossingName = embossingName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getCardSequence() {
        return cardSequence;
    }

    public void setCardSequence(Integer cardSequence) {
        this.cardSequence = cardSequence;
    }

    public Boolean getHolder() {
        return holder;
    }

    public void setHolder(Boolean holder) {
        this.holder = holder;
    }

    public String getValidAt() {
        return validAt;
    }

    public void setValidAt(String validAt) {
        this.validAt = validAt;
    }

    public String getSinceAt() {
        return sinceAt;
    }

    public void setSinceAt(String sinceAt) {
        this.sinceAt = sinceAt;
    }
}
