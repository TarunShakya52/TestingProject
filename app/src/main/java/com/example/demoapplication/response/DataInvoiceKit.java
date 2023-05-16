package com.example.demoapplication.response;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataInvoiceKit implements Parcelable {
    @SerializedName("invoice_number")
    @Expose
    private String invoiceNumber;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("item_details")
    @Expose
    private ItemDetails itemDetails;
    @SerializedName("tax_details")
    @Expose
    private TaxDetails taxDetails;

    public DataInvoiceKit(String invoiceNumber, String date, ItemDetails itemDetails, TaxDetails taxDetails) {
        this.invoiceNumber = invoiceNumber;
        this.date = date;
        this.itemDetails = itemDetails;
        this.taxDetails = taxDetails;
    }

    protected DataInvoiceKit(Parcel in) {
        invoiceNumber = in.readString();
        date = in.readString();
        itemDetails = in.readParcelable(ItemDetails.class.getClassLoader());
        taxDetails = in.readParcelable(TaxDetails.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(invoiceNumber);
        dest.writeString(date);
        dest.writeParcelable( itemDetails, flags);
        dest.writeParcelable( taxDetails, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DataInvoiceKit> CREATOR = new Creator<DataInvoiceKit>() {
        @Override
        public DataInvoiceKit createFromParcel(Parcel in) {
            return new DataInvoiceKit(in);
        }

        @Override
        public DataInvoiceKit[] newArray(int size) {
            return new DataInvoiceKit[size];
        }
    };

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ItemDetails getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(ItemDetails itemDetails) {
        this.itemDetails = itemDetails;
    }

    public TaxDetails getTaxDetails() {
        return taxDetails;
    }

    public void setTaxDetails(TaxDetails taxDetails) {
        this.taxDetails = taxDetails;
    }
}
