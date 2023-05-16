package com.example.demoapplication.request;

import com.google.gson.annotations.SerializedName;

public class WarehouseDetailsSave {

    @SerializedName("warehouseName")
    private String warehouseName;

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    @SerializedName("warehouseId")
    private int warehouseId;
}
