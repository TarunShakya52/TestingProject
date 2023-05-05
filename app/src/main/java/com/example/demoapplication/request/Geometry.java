package com.example.demoapplication.request;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class Geometry {

    @SerializedName("type")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    @SerializedName("coordinates")
    private List<Double> coordinates;

}
