package com.example.demoapplication.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item implements Parcelable {

    @SerializedName("index")
    @Expose
    private Integer index;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("hsn_code")
    @Expose
    private String hsnCode;

    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    @SerializedName("mrp")
    @Expose
    private Double mrp;

    @SerializedName("unit_price")
    @Expose
    private Double unitPrice;

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

    @SerializedName("item_value")
    @Expose
    private Double itemValue;

    public Item(Integer index, String description, String hsnCode, Integer quantity, Double mrp,
                Double unitPrice, Double taxableValue, Double igstValue, Double cgstValue,
                Double sgstValue, Double cessValue, Double itemValue) {
        this.index = index;
        this.description = description;
        this.hsnCode = hsnCode;
        this.quantity = quantity;
        this.mrp = mrp;
        this.unitPrice = unitPrice;
        this.taxableValue = taxableValue;
        this.igstValue = igstValue;
        this.cgstValue = cgstValue;
        this.sgstValue = sgstValue;
        this.cessValue = cessValue;
        this.itemValue = itemValue;
    }

    protected Item(Parcel in) {
        index = in.readInt();
        description = in.readString();
        hsnCode = in.readString();
        quantity = in.readInt();
        mrp = in.readDouble();
        unitPrice = in.readDouble();
        taxableValue = in.readDouble();
        igstValue = in.readDouble();
        cgstValue = in.readDouble();
        sgstValue = in.readDouble();
        cessValue = in.readDouble();
        itemValue = in.readDouble();
    }

    public static final Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(index);
        dest.writeString(description);
        dest.writeString(hsnCode);
        dest.writeInt(quantity);
        dest.writeDouble(mrp);
        dest.writeDouble(unitPrice);
        dest.writeDouble(taxableValue);
        dest.writeDouble(igstValue);
        dest.writeDouble(cgstValue);
        dest.writeDouble(sgstValue);
        dest.writeDouble(cessValue);
        dest.writeDouble(itemValue);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
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

    public Double getItemValue() {
        return itemValue;
    }

    public void setItemValue(Double itemValue) {
        this.itemValue = itemValue;
    }
}
