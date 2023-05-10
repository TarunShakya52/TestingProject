package com.example.demoapplication.request;

import com.google.gson.annotations.SerializedName;

public class AddSEZAdress_Request {

    @SerializedName("title")
    private String  title;

    @SerializedName("gstin")
    private String gstin;

    @SerializedName("address")
    private String address;

    @SerializedName("state_code")
    private String state_code;

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

    public String getSupply_place() {
        return supply_place;
    }

    public void setSupply_place(String supply_place) {
        this.supply_place = supply_place;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    @SerializedName("supply_place")
    private String supply_place;

    public Boolean getShipping() {
        return shipping;
    }

    public void setShipping(Boolean shipping) {
        this.shipping = shipping;
    }

    public Boolean getBilling() {
        return billing;
    }

    public void setBilling(Boolean billing) {
        this.billing = billing;
    }

    @SerializedName("shipping")
    private Boolean shipping;

    @SerializedName("billing")
    private Boolean billing;
}
