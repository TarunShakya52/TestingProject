package com.example.demoapplication.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Totals implements Parcelable {
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("item_value")
    @Expose
    private Double itemValue;
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

    public Totals(Integer quantity, Double itemValue, Double taxableValue, Double igstValue, Double cgstValue, Double sgstValue, Double cessValue) {
        this.quantity = quantity;
        this.itemValue = itemValue;
        this.taxableValue = taxableValue;
        this.igstValue = igstValue;
        this.cgstValue = cgstValue;
        this.sgstValue = sgstValue;
        this.cessValue = cessValue;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getItemValue() {
        return itemValue;
    }

    public void setItemValue(Double itemValue) {
        this.itemValue = itemValue;
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

    protected Totals(Parcel in) {
        quantity = in.readInt();
        itemValue = in.readDouble();
        taxableValue = in.readDouble();
        igstValue = in.readDouble();
        cgstValue = in.readDouble();
        sgstValue = in.readDouble();
        cessValue = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quantity);
        dest.writeDouble(itemValue);
        dest.writeDouble(taxableValue);
        dest.writeDouble(igstValue);
        dest.writeDouble(cgstValue);
        dest.writeDouble(sgstValue);
        dest.writeDouble(cessValue);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Totals> CREATOR = new Creator<Totals>() {
        @Override
        public Totals createFromParcel(Parcel in) {
            return new Totals(in);
        }

        @Override
        public Totals[] newArray(int size) {
            return new Totals[size];
        }
    };
}