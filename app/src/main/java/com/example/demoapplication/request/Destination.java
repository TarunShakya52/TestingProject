package com.example.demoapplication.request;

import com.google.gson.annotations.SerializedName;

public class Destination {

    @SerializedName("geometry")
    private Geometry geometry;

    @SerializedName("radius")
    private Integer radius;

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPlannedTime() {
        return plannedTime;
    }

    public void setPlannedTime(Integer plannedTime) {
        this.plannedTime = plannedTime;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @SerializedName("name")
    private String name;

    @SerializedName("plannedTime")
    private Integer plannedTime;

    @SerializedName("metadata")
    private Metadata metadata;
}
