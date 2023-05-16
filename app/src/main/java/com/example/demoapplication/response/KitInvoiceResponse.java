package com.example.demoapplication.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KitInvoiceResponse implements Parcelable {
    @SerializedName("status")
    @Expose
    private StatusResponse status;
    @SerializedName("data")
    @Expose
    private DataInvoiceKit data;

    public KitInvoiceResponse() {
        // default constructor
    }

    protected KitInvoiceResponse(Parcel in) {
        status = in.readParcelable(StatusResponse.class.getClassLoader());
        data = in.readParcelable(DataInvoiceKit.class.getClassLoader());
    }

    public static final Parcelable.Creator<KitInvoiceResponse> CREATOR = new Parcelable.Creator<KitInvoiceResponse>() {
        @Override
        public KitInvoiceResponse createFromParcel(Parcel in) {
            return new KitInvoiceResponse(in);
        }

        @Override
        public KitInvoiceResponse[] newArray(int size) {
            return new KitInvoiceResponse[size];
        }
    };

    public StatusResponse getStatus() {
        return status;
    }

    public void setStatus(StatusResponse status) {
        this.status = status;
    }

    public DataInvoiceKit getData() {
        return data;
    }

    public void setData(DataInvoiceKit data) {
        this.data = data;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(status, flags);
        dest.writeParcelable(data, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}