package br.com.calcard.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Goals {
    @SerializedName("goal")
    @Expose
    private Double goal;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("percentage")
    @Expose
    private Double percentage;
    @SerializedName("spent")
    @Expose
    private Double spent;

    public Integer getExpenseGoalId() {
        return expenseGoalId;
    }

    public void setExpenseGoalId(Integer expenseGoalId) {
        this.expenseGoalId = expenseGoalId;
    }

    @SerializedName("expenseGoalId")
    @Expose
    private  Integer expenseGoalId;

    public Integer getGauge() {
        return gauge;
    }

    public void setGauge(Integer gauge) {
        this.gauge = gauge;
    }

    @SerializedName("gauge")
    @Expose
    private Integer gauge;

    public Double getGoal() {
        return goal;
    }

    public void setGoal(Double goal) {
        this.goal = goal;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Double getSpent() {
        return spent;
    }

    public void setSpent(Double spent) {
        this.spent = spent;
    }
}
