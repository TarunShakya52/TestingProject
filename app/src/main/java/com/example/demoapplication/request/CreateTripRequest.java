package com.example.demoapplication.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateTripRequest {

    @SerializedName("deviceId")
    private Long deviceId;

    @SerializedName("name")
    private String name;

    @SerializedName("forceClose")
    private Boolean forceClose;

    public Integer getClosureType() {
        return closureType;
    }

    public void setClosureType(Integer closureType) {
        this.closureType = closureType;
    }

    @SerializedName("closureType")
    private Integer closureType;

    @SerializedName("plannedEndTime")
    private Long plannedEndTime;

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getForceClose() {
        return forceClose;
    }

    public void setForceClose(Boolean forceClose) {
        this.forceClose = forceClose;
    }

    public Long getPlannedEndTime() {
        return plannedEndTime;
    }

    public void setPlannedEndTime(Long plannedEndTime) {
        this.plannedEndTime = plannedEndTime;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public List<Geofences> getGeofences() {
        return geofences;
    }

    public void setGeofences(List<Geofences> geofences) {
        this.geofences = geofences;
    }

    @SerializedName("metadata")
    private Metadata metadata;

    @SerializedName("destination")
    private Destination destination;

    @SerializedName("geofences")
    private List<Geofences> geofences;

}
