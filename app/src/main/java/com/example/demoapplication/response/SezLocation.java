package com.example.demoapplication.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SezLocation {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("gstin")
    @Expose
    private String gstin;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("supply_place")
    @Expose
    private String supplyPlace;
    @SerializedName("state_code")
    @Expose
    private String stateCode;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSupplyPlace() {
        return supplyPlace;
    }

    public void setSupplyPlace(String supplyPlace) {
        this.supplyPlace = supplyPlace;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
}
