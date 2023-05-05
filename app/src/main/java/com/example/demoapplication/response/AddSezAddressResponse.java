package com.example.demoapplication.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddSezAddressResponse {
    @SerializedName("status")
    @Expose
    private StatusResponse status;
    @SerializedName("data")
    @Expose
    private DataSezResponse data;

    public StatusResponse getStatus() {
        return status;
    }

    public void setStatus(StatusResponse status) {
        this.status = status;
    }

    public DataSezResponse getData() {
        return data;
    }

    public void setData(DataSezResponse data) {
        this.data = data;
    }
}
