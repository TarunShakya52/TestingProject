package com.example.demoapplication.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataInvoiceKit {
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
