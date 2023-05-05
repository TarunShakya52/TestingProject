package com.example.demoapplication.request;

import com.google.gson.annotations.SerializedName;

public class Metadata {

    @SerializedName("myattribute")
    private String myattribute;

    public String getMyattribute() {
        return myattribute;
    }

    public void setMyattribute(String myattribute) {
        this.myattribute = myattribute;
    }
}
