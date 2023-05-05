package com.example.demoapplication.request;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public  class VMMachineModel implements Parcelable {

    public VMMachineModel(String machineName, String status) {
        this.machineName = machineName;
        this.status = status;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String machineName;
    private String status;

    public VMMachineModel(Parcel parcel){

        machineName = parcel.readString();
        status = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(machineName);
        parcel.writeString(status);
    }

    public static final Creator<VMMachineModel> CREATOR = new Creator<VMMachineModel>() {
        @Override
        public VMMachineModel createFromParcel(Parcel in) {
            return new VMMachineModel(in);
        }

        @Override
        public VMMachineModel[] newArray(int size) {
            return new VMMachineModel[size];
        }
    };

}
