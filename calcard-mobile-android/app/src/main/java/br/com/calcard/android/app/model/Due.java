package br.com.calcard.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Due implements Serializable {
    @SerializedName("availableDue")
    @Expose
    private List<Integer> availableDue = null;
    @SerializedName("dueDate")
    @Expose
    private String dueDate;

    public List<Integer> getAvailableDue() {
        return availableDue;
    }

    public void setAvailableDue(List<Integer> availableDue) {
        this.availableDue = availableDue;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
