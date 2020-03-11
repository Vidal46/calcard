package br.com.calcard.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Timeline {
    @SerializedName("content")
    @Expose
    public List<Spending> spendings = null;
    @SerializedName("hasNextPage")
    @Expose
    private boolean hasNextPage;
    @SerializedName("totalPages")
    @Expose
    private int totalPages;
    @SerializedName("number")
    @Expose
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<Spending> getContent() {
        return spendings;
    }

    public void setContent(List<Spending> spendings) {
        this.spendings = spendings;
    }
}

