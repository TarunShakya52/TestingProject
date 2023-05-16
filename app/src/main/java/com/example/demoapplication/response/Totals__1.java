package com.example.demoapplication.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Totals__1 implements Parcelable {

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

    // Parcelable implementation

    protected Totals__1(Parcel in) {
        taxableValue = in.readDouble();
        igstValue = in.readDouble();
        cgstValue = in.readDouble();
        sgstValue = in.readDouble();
        cessValue = in.readDouble();
        totalValue = in.readDouble();
    }

    public static final Creator<Totals__1> CREATOR = new Creator<Totals__1>() {
        @Override
        public Totals__1 createFromParcel(Parcel in) {
            return new Totals__1(in);
        }

        @Override
        public Totals__1[] newArray(int size) {
            return new Totals__1[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(taxableValue);
        dest.writeDouble(igstValue);
        dest.writeDouble(cgstValue);
        dest.writeDouble(sgstValue);
        dest.writeDouble(cessValue);
        dest.writeDouble(totalValue);
    }
}