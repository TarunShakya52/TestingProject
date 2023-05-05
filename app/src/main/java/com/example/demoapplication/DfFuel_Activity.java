package com.example.demoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.demoapplication.api.GetRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DfFuel_Activity extends AppCompatActivity {

    Button btnDFfuelClaim;
    Long deviceId;
    Long startTime;
    Long endTime;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_df_fuel);

        btnDFfuelClaim = findViewById(R.id.btnFuelClaim);
        getTimeStamp();
        deviceId = 1332079L;

        btnDFfuelClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiGetTrips();
            }
        });
    }

    private void apiGetTrips() {
        String url = "https://intouch.mapmyindia.com/iot/api/trips?limit="+10+"&status="+1+"&deviceId="+
                deviceId+"&startTime"+startTime+"&endTime"+endTime;

        GetRequest aTask = new GetRequest(this);
        aTask.setListener(new GetRequest.MyAsyncTaskkGetListener() {
            @Override
            public void onPreExecuteConcluded() {
            }

            @Override
            public void onPostExecuteConcluded(String result) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();
                try {
                    if (result != null){
//                        DeviceInfoResponse response = gson.fromJson(result, DeviceInfoResponse.class);
                        Log.e("fuelResponse",result);
                    }
                } catch (Exception e) {
                    Log.e("checkApi",e.getMessage());
                }
            }
        });
        aTask.execute(url,"","","8cbe85e6-d30b-48e6-b12f-e8aef19d6bde");
    }

    void getTimeStamp(){
        startTime = System.currentTimeMillis() / 1000L;
        Log.e("unixTime", String.valueOf(startTime));
        long duration = ((2 * 60) + 59) * 1000;
        endTime = duration + startTime;

        Log.e("after2days", String.valueOf(endTime));
    }
}