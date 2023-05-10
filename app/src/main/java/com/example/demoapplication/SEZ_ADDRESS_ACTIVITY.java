package com.example.demoapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.demoapplication.Adapter.SEZddressAdapter;
import com.example.demoapplication.Adapter.UserGuideAdapter;
import com.example.demoapplication.api.GetRequest;
import com.example.demoapplication.api.PostRequest;
import com.example.demoapplication.request.AddSEZAdress_Request;
import com.example.demoapplication.response.AddSezAddressResponse;
import com.example.demoapplication.response.SezAddressGetResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashSet;
import java.util.Set;

public class SEZ_ADDRESS_ACTIVITY extends AppCompatActivity implements SEZddressAdapter.sezDetails {

    RecyclerView recyclerView;
    SEZddressAdapter seZddressAdapter;
    Boolean shipping,billing = false;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sez_address);

        Intent intent = getIntent();
        shipping = intent.getBooleanExtra("shipping",false);
        billing = intent.getBooleanExtra("billing",false);
        getSezAddress();


    }

    private void getSezAddress() {

        GetRequest aTask = new GetRequest(this);
        aTask.setListener(new GetRequest.MyAsyncTaskkGetListener() {
            @Override
            public void onPreExecuteConcluded() {
                //Loader Will come here
            }

            @Override
            public void onPostExecuteConcluded(String result) {

                Log.d("ResultCheck", "=" + result);
                try {
                    if (result != null) {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                        Gson gson = gsonBuilder.create();
                        SezAddressGetResponse response = gson.fromJson(result, SezAddressGetResponse.class);
                        Log.e("responsecreate", response.getStatus().getMessage());
                        recyclerView =  findViewById(R.id.recyclerView);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(mLayoutManager);
                        setAdapter(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();


                }

            }
        });

        String token = "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJqdGkiOiI1OGNkYjk4MC0yNzU4LTQxOGEtODY4MC1jZmU1MTAzMGU1ZDIiLCJpZCI6MjExLCJtb2JpbGUiOiI4MDg4NDAzNDMyIiwicm9sZSI6IkFETUlOIiwicGVybWlzc2lvbnMiOlsidm1fYWxsb3dlZCIsInByb2R1Y3RfYWxsb3dlZCIsIm9yZGVyc19hbGxvd2VkIiwidXNlcl9hbGxvd2VkIiwidm1fc2V0dGluZ3MiLCJ2bV9zYWxlcyIsIm5vdGlmaWNhdGlvbl9lbmFibGVkIiwiYWJzb2x1dGVfc2FsZXMiLCJ2ZW5kb3JfYWxsb3dlZCIsIndhbGxldF9oaXN0b3J5Iiwidmlld19icmFuZHMiLCJjcmVhdGVfYnJhbmQiLCJ2aWV3X2Jhc2UiLCJ2aWV3X3ZhcmlhbnRzIiwibWFwX3ZlbmRvciIsInZpZXdfcHJvZHVjdHMiLCJjcmVhdGVfcHJvZHVjdHMiLCJjcmVhdGVfYmFzZSIsImNyZWF0ZV92YXJpYW50IiwiY3JlYXRlX3ZlbmRvciIsInZpZXdfdmVuZG9yIiwidmlld191c2VycyIsIm1hcF91c2VyIiwiYXR0ZW5kYW5jZSIsImtpdHRpbmdfcmVmaWxsaW5nIiwiYXVkaXRfbWFjaGluZSIsInN0b2NraW5nIiwic3BhcmVfcGFydHMiLCJlX2RhYWxjaGluaSIsInNsb3RfdXRpbGl6YXRpb24iLCJoZWFsdGhfYWxlcnQiLCJ0cm91Ymxlc2hvb3RfZ3VpZGUiLCJyYWlzZV90aWNrZXQiLCJzZWxmX2Fzc2Vzc21lbnQiLCJyZWNjZSIsInZtX2luc3RhbGxhdGlvbiIsInZpZXdfdGFnIiwiYWN0aXZhdGVfdGFnIiwibWVhbHNfbWVudSIsInZtX3BhcmFtZXRlcnMiLCJzdXBwb3J0X25vdGlmaWNhdGlvbiIsInNsb3RfcmVwYWlyIiwicXVpY2tfdW5ibG9jayIsInVuYmxvY2tfYWxsX3Nsb3RzIiwic2xvdF9yZXBvcnQiLCJwYXJ0bmVyX3JlY2hhcmdlIiwib3JkZXJfYXBwcm92ZSIsImJ1bGtfcHVyY2hhc2UiLCJtb2JpbGl0eV9yZWZpbGwiLCJtYXBfYnBfdXNlciIsInZpZXdfZnJhbmNoaXNlZSIsImNyZWF0ZV9mcmFuY2hpc2VlIiwidmlld19iYW5rIiwiY3JlYXRlX2JhbmsiLCJ2aWV3X3BsYXRmb3JtX2NoYXJnZSIsIm1hcF9wbGF0Zm9ybV9jaGFyZ2UiLCJjcmVhdGVfcGxhdGZvcm1fY2hhcmdlIiwibW9iaWxpdHlfY2hlY2tpbiIsImNyZWF0ZV9jb3Vwb24iLCJ2aWV3X2NvdXBvbiIsImJ1eV9hdF92cCIsIm1hcF9tYWNoaW5lcyIsInZpZXdfYnBfdXNlciIsInZtX2NyZWF0ZSIsInZtX3VwZGF0ZSIsInZtX3ZpZXciLCJjb2hvcnRfY3JlYXRlIiwiY29ob3J0X3ZpZXciLCJjb2hvcnRfbWFwX3VzZXIiLCJjb2hvcnRfbWFwX21hY2hpbmUiLCJiYW5uZXJfY3JlYXRlIiwiYXBwcm92ZV9hdHRlbmRhbmNlIiwic2xvdF9vcGVyYXRpb24iLCJyZWZ1bmRfcmV2Il0sImlhdCI6MTY4MzcxMjE3NiwiZXhwIjoxNjgzNzk4NTc2fQ.tOFPJTfLI8BhpZirc_Na3PDrFBvu37D_ggiFepmfY4MVYyCklOweik9b4X8rYXV6";
        String url = "https://api-stage.daalchini.co.in/partner/api/v2/warehouse/1/sez-location";
        aTask.execute(url,"","",token);
    }

    private void setAdapter(SezAddressGetResponse response) {


        if (shipping || billing){
            seZddressAdapter = new SEZddressAdapter(SEZ_ADDRESS_ACTIVITY.this,response,this,shipping,billing,false);
            recyclerView.setAdapter(seZddressAdapter);
        }else {
            seZddressAdapter = new SEZddressAdapter(SEZ_ADDRESS_ACTIVITY.this, response, this,true);
            recyclerView.setAdapter(seZddressAdapter);
        }
    }

    @Override
    public void getDetails(String title, String gstin, String supplyPlace, String stateCode, String address,int id) {
        Intent i = new Intent(SEZ_ADDRESS_ACTIVITY.this,AddSEZAddress.class);
        i.putExtra("title",title);
        i.putExtra("sezaddress",address);
        i.putExtra("gst",gstin);
        i.putExtra("placeofsupply",supplyPlace);
        i.putExtra("statecode",stateCode);
        i.putExtra("edit",true);
        i.putExtra("id",id);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void getShippingDetails(String title, String gstin, String supplyPlace, String stateCode, String address, int id) {

        AddSEZAdress_Request shippingAddress = new AddSEZAdress_Request();
        shippingAddress.setTitle(title);
        shippingAddress.setAddress(address);
        shippingAddress.setGstin(gstin);
        shippingAddress.setSupply_place(supplyPlace);
        shippingAddress.setState_code(stateCode);
        shippingAddress.setShipping(true);
        shippingAddress.setBilling(false);
        Gson gson = new Gson();
        String jsonString = gson.toJson(shippingAddress);
        SharedPreferences prefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = prefs.edit();
        editor.putString("gsonShippingAddress", jsonString);
        editor.apply();
        finish();

//        Intent i = new Intent(SEZ_ADDRESS_ACTIVITY.this,InvoiceActivity.class);
//        i.putExtra("title",title);
//        i.putExtra("sezaddress",address);
//        i.putExtra("gst",gstin);
//        i.putExtra("placeofsupply",supplyPlace);
//        i.putExtra("statecode",stateCode);
//        i.putExtra("edit",true);
//        i.putExtra("shipping",true);
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
    }

    @Override
    public void getBillingDetails(String title, String gstin, String supplyPlace, String stateCode, String address, int id) {

        AddSEZAdress_Request billingAddress = new AddSEZAdress_Request();
        billingAddress.setTitle(title);
        billingAddress.setAddress(address);
        billingAddress.setGstin(gstin);
        billingAddress.setSupply_place(supplyPlace);
        billingAddress.setState_code(stateCode);
        billingAddress.setBilling(true);
        billingAddress.setShipping(false);
        Gson gson = new Gson();
        String jsonString = gson.toJson(billingAddress);
        SharedPreferences prefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = prefs.edit();
        editor.putString("gsonBillingAddress", jsonString);
        editor.apply();
        finish();

//        Intent i = new Intent(SEZ_ADDRESS_ACTIVITY.this,InvoiceActivity.class);
//        i.putExtra("title",title);
//        i.putExtra("sezaddress",address);
//        i.putExtra("gst",gstin);
//        i.putExtra("placeofsupply",supplyPlace);
//        i.putExtra("statecode",stateCode);
//        i.putExtra("edit",true);
//        i.putExtra("billing",true);
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);

    }
}