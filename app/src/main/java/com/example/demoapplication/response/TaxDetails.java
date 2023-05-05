package com.example.demoapplication.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TaxDetails {
    @SerializedName("taxes")
    @Expose
    private List<Tax> taxes;
    @SerializedName("totals")
    @Expose
    private Totals__1 totals;

    public List<Tax> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<Tax> taxes) {
        this.taxes = taxes;
    }

    public Totals__1 getTotals() {
        return totals;
    }

    public void setTotals(Totals__1 totals) {
        this.totals = totals;
    }
}
