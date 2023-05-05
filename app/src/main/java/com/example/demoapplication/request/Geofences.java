package com.example.demoapplication.request;

import com.google.gson.annotations.SerializedName;

public class Geofences {

    @SerializedName("geometry")
    private Geometry geometry;

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



    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @SerializedName("radius")
    private Integer radius;

    @SerializedName("name")
    private String name;

    public Object getPlannedTime() {
        return plannedTime;
    }

    public void setPlannedTime(Object plannedTime) {
        this.plannedTime = plannedTime;
    }

    @SerializedName("plannedTime")
    private Object plannedTime;

    @SerializedName("metadata")
    private Metadata metadata;

}
