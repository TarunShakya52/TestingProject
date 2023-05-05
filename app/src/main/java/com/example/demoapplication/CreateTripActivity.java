package com.example.demoapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.demoapplication.Adapter.CheckpointAdapter;
import com.example.demoapplication.api.GetRequest;
import com.example.demoapplication.api.PostRequest;
import com.example.demoapplication.api.PostRequestItouchToken;
import com.example.demoapplication.networking.Constant;
import com.example.demoapplication.request.Coordinates;
import com.example.demoapplication.request.CreateTripRequest;
import com.example.demoapplication.request.Destination;
import com.example.demoapplication.request.Geofences;
import com.example.demoapplication.request.Geometry;
import com.example.demoapplication.request.Metadata;
import com.example.demoapplication.request.VMMachineModel;
import com.example.demoapplication.response.CreateTripResponse;
import com.example.demoapplication.response.ItouchTokenResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateTripActivity extends AppCompatActivity implements CheckpointAdapter.Destination{

    Button createTrip,btngenerateToken,btnAuthenticate,btnBottomSheet;
    Destination destination;
    Geometry geometry,geometry1;
    List<Double> coordinates = new ArrayList<>();
    List<Double> coordinates1 = new ArrayList<>();
    Coordinates latlng;
    List<Geofences> listgeofences = new ArrayList<>();
    Geofences geofences;
    Geofences geofences1;
    Metadata newMeta;
    Gson gson;
    String auth;
    VMMachineModel vmMachineModel = new VMMachineModel("","");
    VmMachineDetails vmMachineDetails = new VmMachineDetails();
    SharedPreferences sharedPreferences;
    CheckpointAdapter checkpointAdapter;
    BottomSheetDialog bottomSheetDialog;
    List<VMMachineModel> arrVmModel = new ArrayList<>();
    int count = 1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        createTrip = findViewById(R.id.btnCreateTrip);
        btngenerateToken = findViewById(R.id.btnGenrateToken);
        btnAuthenticate =findViewById(R.id.btnUserAuth);
        btnBottomSheet = findViewById(R.id.btnBottomSheet);
        geofences1 = new Geofences();
        destination = new Destination();
        geometry = new Geometry();
        geometry1 = new Geometry();
        latlng = new Coordinates();
        geofences = new Geofences();
        newMeta = new Metadata();
        auth = "6b8d476a-46f3-44e9-84d9-92f8c107dc29";

        sharedPreferences = getSharedPreferences("App", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(vmMachineModel);
        prefsEditor.putString("MyObject", null);
        prefsEditor.apply();




        long unixTime = System.currentTimeMillis() / 1000L;
        Log.e("unixTime", String.valueOf(unixTime));
        long duration = ((2 * 60) + 59) * 1000;
        long newTime = duration + unixTime;

        Log.e("after2days", String.valueOf(newTime));


        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        createTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiCreateTrip();
            }
        });

        btngenerateToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiGenerateToken();
            }
        });

        btnAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apigetUserAuth();
            }
        });

        btnBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrVmModel.isEmpty()){
                vmMachineModel.setMachineName("Start Point");
                vmMachineModel.setStatus("Source");
                arrVmModel.add(0,vmMachineModel);
                }
                showBottomSheetDialog(arrVmModel);
            }
        });
    }

    private void apigetUserAuth() {
//       String url =  "https://intouch.mapmyindia.com/iot/api/devices?includeInActive=true&ignoreBeacon=false";
        String url = "https://intouch.mapmyindia.com/iot/api/trips?limit=100&status=1&deviceId=1332079";

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
        aTask.execute(url,"","",auth);
    }

    private void apiGenerateToken() {
        String url = "https://outpost.mapmyindia.com/api/security/oauth/token";
//        JSONObject reqObj = new JSONObject();
        //            reqObj.put("grant_type","client_credentials");
//            reqObj.put("client_id","33OkryzDZsLh5RcSvJ6dPWNMBgUUDyOLIZlb3POtWOcenQQ1hBGVzO-8-CPLgT8AzopagDIMb_JdtichASQbPw==");
//            reqObj.put("client_secret","lrFxI-iSEg8tV9-1yeugARhtM_DGzNiuspVKfRlk_xvOfDBNHiuTuwF8Bur9fRUuAVCiGLIs90j2vlQd5AjSPb03UdsUEJpF");

//        Map<String, String> parameters = new HashMap<String, String>();

        // Inserting pairs in above Map
        // using put() method
//        parameters.put("grant_type","client_credentials");
//        parameters.put("client_id","33OkryzDZsLh5RcSvJ6dPWNMBgUUDyOLIZlb3POtWOcenQQ1hBGVzO-8-CPLgT8AzopagDIMb_JdtichASQbPw==");
//        parameters.put("client_secret","lrFxI-iSEg8tV9-1yeugARhtM_DGzNiuspVKfRlk_xvOfDBNHiuTuwF8Bur9fRUuAVCiGLIs90j2vlQd5AjSPb03UdsUEJpF" );
        PostRequestItouchToken aTask = new PostRequestItouchToken(this);
        aTask.setListener(new PostRequestItouchToken.MyAsyncTaskListener() {
            @Override
            public void onPreExecuteConcluded() {
                //Loader Will come here
            }
            @Override
            public void onPostExecuteConcluded(String result) {
                Log.e("Result______________", "=" + result);
                try {
                    ItouchTokenResponse response = gson.fromJson(result, ItouchTokenResponse.class);
                    Log.e("tokkenResponse", String.valueOf(result));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        aTask.execute(url);


    }

    private void apiCreateTrip() {
        CreateTripRequest createTripRequest = new CreateTripRequest();
        createTripRequest.setDeviceId(1332079L);
        createTripRequest.setName("Tarun");
        createTripRequest.setForceClose(true);
        createTripRequest.setClosureType(1);
        createTripRequest.setPlannedEndTime(1591891234L);
        createTripRequest.setDestination(destination);
        createTripRequest.setGeofences(listgeofences);

        newMeta.setMyattribute("Sample Text");

        createTripRequest.setMetadata(createTripRequest.getMetadata());

        geometry.setType("Point");
//        latlng.setLat(77.234f);
//        latlng.setLng(28.456f);
//        coordinates.add(latlng);
        coordinates.add(77.234d);
        coordinates.add(28.456d);
        geometry.setCoordinates(coordinates);
        destination.setGeometry(geometry);
        destination.setRadius(100);
        destination.setName("Tarun");
        destination.setPlannedTime(0);
        destination.setMetadata(newMeta);

        //Setting data for geofence
        geofences.setGeometry(geometry);
        geofences.setRadius(100);
        geofences.setName("geofence");
        geofences.setPlannedTime(null);
        geofences.setMetadata(newMeta);

        //Setting data for geofence1
        geometry1.setType("Point");

        coordinates1.add(77.235d);
        coordinates1.add(28.455d);
        geometry1.setCoordinates(coordinates1);
        geofences1.setGeometry(geometry1);
        geofences1.setRadius(100);
        geofences1.setName("Geofence1");
        geofences1.setPlannedTime(null);
        geofences1.setMetadata(newMeta);

        listgeofences.add(geofences);
        listgeofences.add(geofences1);

//        createTripRequest.setDestination(createTripRequest.getDestination());


        PostRequest aTask = new PostRequest(this);
        aTask.setListener(new PostRequest.MyAsyncTaskListener() {
            @Override
            public void onPreExecuteConcluded() {
                //Loader Will come here
            }

            @Override
            public void onPostExecuteConcluded(String result) {
                Log.d("Result______________", "=" + result);
                try {
                    if (result != null) {
                        CreateTripResponse response = gson.fromJson(result, CreateTripResponse.class);
                        Log.e("responsecreate", result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();


                }

            }
        });

        String jsonInString = gson.toJson(createTripRequest);
        String token = "9ef3a6b1-1e4b-46ac-8865-23257a4e9cbd";
        Log.e("url_________", "=" + Constant.CREATE_TRIP);
        Log.e("Request______________", "=" + jsonInString);
        aTask.execute(Constant.CREATE_TRIP, jsonInString,token);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        Intent intent = getIntent();
//        vmMachineModel = intent.getParcelableExtra("Vmdata");
//        vmMachineModel.setMachineName("tarun");
//        vmMachineModel.setStatus("kitted");
//        arrVmModel.add(0,vmMachineModel);
//        arrVmModel.add(1,vmMachineModel);
//        Log.e("count", String.valueOf(count));
//
//
////            Log.e("aarayVm--", String.valueOf(vmMachineModel.getMachineName()));
//            Log.e("aarayVm--", String.valueOf(arrVmModel.size()));

        Gson gson = new Gson();
        String json = sharedPreferences.getString("MyObject", null);
        VMMachineModel vmMachineModel = gson.fromJson(json, VMMachineModel.class);
        if (vmMachineModel != null){
        arrVmModel.add(count,vmMachineModel);
        count++;
        checkpointAdapter.notifyDataSetChanged();
        for (int i = 0;i<arrVmModel.size();i++) {
            Log.e("aarayVm--", String.valueOf(arrVmModel.get(i).getMachineName()));
        }
        }



    }

    private void showBottomSheetDialog(List<VMMachineModel> arrVmModel) {

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.createtrip_bottomsheet);

        RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.rv_checkpoints);
        Button btnAddCheckpoint = bottomSheetDialog.findViewById(R.id.btnAddCheckpoint);

        btnAddCheckpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateTripActivity.this,VmMachineDetails.class);
                startActivity(i);
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true);
        assert recyclerView != null;
        recyclerView.setLayoutManager(mLayoutManager);
        checkpointAdapter = new CheckpointAdapter(CreateTripActivity.this, arrVmModel,bottomSheetDialog);
        checkpointAdapter.setListener(this);
        recyclerView.setAdapter(checkpointAdapter);
        checkpointAdapter.notifyDataSetChanged();
        bottomSheetDialog.show();
    }

    @Override
    public void showDestination(String name) {
        Toast.makeText(this,name,Toast.LENGTH_SHORT).show();
    }


}