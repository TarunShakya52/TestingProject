package com.example.demoapplication.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataAllSez {
    @SerializedName("warehouse_location")
    @Expose
    private WarehouseLocation warehouseLocation;
    @SerializedName("sez_locations")
    @Expose
    private List<SezLocation> sezLocations;

    public WarehouseLocation getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(WarehouseLocation warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public List<SezLocation> getSezLocations() {
        return sezLocations;
    }

    public void setSezLocations(List<SezLocation> sezLocations) {
        this.sezLocations = sezLocations;
    }
}
