package com.example.demoapplication.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KitInvoiceResponse {
    @SerializedName("status")
    @Expose
    private StatusResponse status;
    @SerializedName("data")
    @Expose
    private DataInvoiceKit data;

    public StatusResponse getStatus() {
        return status;
    }

    public void setStatus(StatusResponse status) {
        this.status = status;
    }

    public DataInvoiceKit getData() {
        return data;
    }

    public void setData(DataInvoiceKit data) {
        this.data = data;
    }
}
