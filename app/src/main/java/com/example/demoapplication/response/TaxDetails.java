package com.example.demoapplication.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TaxDetails implements Parcelable {
    @SerializedName("taxes")
    @Expose
    private List<Tax> taxes;
    @SerializedName("totals")
    @Expose
    private Totals__1 totals;

    public List<Tax> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<Tax> taxes) {
        this.taxes = taxes;
    }

    public Totals__1 getTotals() {
        return totals;
    }

    public void setTotals(Totals__1 totals) {
        this.totals = totals;
    }

    protected TaxDetails(Parcel in) {
        taxes = in.createTypedArrayList(Tax.CREATOR);
        totals = in.readParcelable(Totals__1.class.getClassLoader());
    }

    public static final Creator<TaxDetails> CREATOR = new Creator<TaxDetails>() {
        @Override
        public TaxDetails createFromParcel(Parcel in) {
            return new TaxDetails(in);
        }

        @Override
        public TaxDetails[] newArray(int size) {
            return new TaxDetails[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(taxes);
        dest.writeParcelable(totals, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
