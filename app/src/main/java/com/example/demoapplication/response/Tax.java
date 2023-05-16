package com.example.demoapplication.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tax implements Parcelable {
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

    // Required method to create the parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.gstRate);
        dest.writeDouble(this.taxableValue);
        dest.writeDouble(this.igstValue);
        dest.writeDouble(this.cgstValue);
        dest.writeDouble(this.sgstValue);
        dest.writeDouble(this.cessValue);
        dest.writeDouble(this.totalValue);
    }

    // Required method to recreate the object from the parcel
    private Tax(Parcel in) {
        this.gstRate = in.readDouble();
        this.taxableValue = in.readDouble();
        this.igstValue = in.readDouble();
        this.cgstValue = in.readDouble();
        this.sgstValue = in.readDouble();
        this.cessValue = in.readDouble();
        this.totalValue = in.readDouble();
    }

    public static final Parcelable.Creator<Tax> CREATOR = new Parcelable.Creator<Tax>() {
        @Override
        public Tax createFromParcel(Parcel source) {
            return new Tax(source);
        }

        @Override
        public Tax[] newArray(int size) {
            return new Tax[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}