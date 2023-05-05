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

public class RouteRouting_Activity extends AppCompatActivity {

    Button btnGetRoute;
    String restKey;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_routing);

        btnGetRoute = findViewById(R.id.btnHitme);
        restKey = "lrFxI-iSEg8tV9-1yeugARhtM_DGzNiuspVKfRlk_xvOfDBNHiuTuwF8Bur9fRUuAVCiGLIs90j2vlQd5AjSPb03UdsUEJpF";
        btnGetRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiGetRoute();
            }
        });

    }

    private void apiGetRoute() {
//       String url =  "https://intouch.mapmyindia.com/iot/api/devices?includeInActive=true&ignoreBeacon=false";
        String url = "https://apis.mapmyindia.com/advancedmaps/v1/"+restKey+"/route_adv/"+"driving/"+"KIC4FK;77.131123,28.552413;77.113091,28.544649";

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
                        Log.e("geterrooror",result);
                    }
                } catch (Exception e) {
                    Log.e("checkApi",e.getMessage());
                }
            }
        });
        aTask.execute(url,"","","8cbe85e6-d30b-48e6-b12f-e8aef19d6bde");

    }

//    if (arrCheckpoint.get(i).getType().equals("source")){
//        meta.setMyattribute("Source");
//    }else {
//        meta.setMyattribute("Geofence"+" "+String.valueOf(i));
//    }

}