package com.example.demoapplication.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SezAddressGetResponse {
    @SerializedName("status")
    @Expose
    private StatusResponse status;
    @SerializedName("data")
    @Expose
    private DataAllSez data;

    public StatusResponse getStatus() {
        return status;
    }

    public void setStatus(StatusResponse status) {
        this.status = status;
    }

    public DataAllSez getData() {
        return data;
    }

    public void setData(DataAllSez data) {
        this.data = data;
    }
}
