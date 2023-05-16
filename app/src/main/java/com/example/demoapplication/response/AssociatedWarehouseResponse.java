package com.example.demoapplication.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssociatedWarehouseResponse {

    @SerializedName("status")
    @Expose
    private StatusWarehouseAssociated status;
    @SerializedName("data")
    @Expose
    private List<DatumWarehouseAssociated> data;

    public StatusWarehouseAssociated getStatus() {
        return status;
    }

    public void setStatus(StatusWarehouseAssociated status) {
        this.status = status;
    }

    public List<DatumWarehouseAssociated> getData() {
        return data;
    }

    public void setData(List<DatumWarehouseAssociated> data) {
        this.data = data;
    }

}
