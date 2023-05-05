package com.example.demoapplication.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tax {
    @SerializedName("gst_rate")
    @Expose
    private Double gstRate;
    @SerializedName("taxable_value")
    @Expose
    private Double taxableValue;
    @SerializedName("igst_value")
    @Expose
    private Double igstValue;
    @SerializedName("cgst_value")
    @Expose
    private Double cgstValue;
    @SerializedName("sgst_value")
    @Expose
    private Double sgstValue;
    @SerializedName("cess_value")
    @Expose
    private Double cessValue;
    @SerializedName("total_value")
    @Expose
    private Double totalValue;

    public Double getGstRate() {
        return gstRate;
    }

    public void setGstRate(Double gstRate) {
        this.gstRate = gstRate;
    }

    public Double getTaxableValue() {
        return taxableValue;
    }

    public void setTaxableValue(Double taxableValue) {
        this.taxableValue = taxableValue;
    }

    public Double getIgstValue() {
        return igstValue;
    }

    public void setIgstValue(Double igstValue) {
        this.igstValue = igstValue;
    }

    public Double getCgstValue() {
        return cgstValue;
    }

    public void setCgstValue(Double cgstValue) {
        this.cgstValue = cgstValue;
    }

    public Double getSgstValue() {
        return sgstValue;
    }

    public void setSgstValue(Double sgstValue) {
        this.sgstValue = sgstValue;
    }

    public Double getCessValue() {
        return cessValue;
    }

    public void setCessValue(Double cessValue) {
        this.cessValue = cessValue;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }
}
