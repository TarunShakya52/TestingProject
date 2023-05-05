package com.example.demoapplication.request;

import com.google.gson.annotations.SerializedName;

public class Coordinates {

    private Float lat;
    private Float lng;

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }
}

