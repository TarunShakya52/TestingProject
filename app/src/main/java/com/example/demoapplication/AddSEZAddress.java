package com.example.demoapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapplication.api.PatchRequest;
import com.example.demoapplication.api.PostRequest;
import com.example.demoapplication.networking.Constant;
import com.example.demoapplication.request.AddSEZAdress_Request;
import com.example.demoapplication.response.AddSezAddressResponse;
import com.example.demoapplication.response.CreateTripResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class AddSEZAddress extends AppCompatActivity {

    TextInputLayout textInputLayout;
    ArrayList<String> arrWarehouse;
    AutoCompleteTextView autoCompleteTextView;
    TextView textView;
    MaterialButton btnAddAddressActive,btnViewAddrss,btnAddAddressInactive;
    String whName;
    EditText etGst,etPlaceofSupply,etTitle,etSezAddress,etStateCode;
    Gson gson;
    Boolean wareHouseSelection = false;
    Boolean editAddress = false;
    int id;
    String title,pos,gst,statecode,addresss;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sezaddress);
        textInputLayout =findViewById(R.id.dropDownMenu);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        btnAddAddressActive = findViewById(R.id.btnAddAdressActive);
        btnAddAddressInactive = findViewById(R.id.btnAddAdressInactive);
        etGst = findViewById(R.id.etGst);
        etPlaceofSupply = findViewById(R.id.etPlaceofSupply);
        etTitle = findViewById(R.id.etTitle);
        etSezAddress = findViewById(R.id.etSezAddress);
        etStateCode = findViewById(R.id.etStateCode);
        btnViewAddrss = findViewById(R.id.materialButton2);

        LayoutInflater inflater = getLayoutInflater();
        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.sezAddress);
        View myLayout = inflater.inflate(R.layout.adaptertext, mainLayout, false);
        textView = (TextView) myLayout.findViewById(R.id.textViewFeelings);
        arrWarehouse = new ArrayList<String>();
        arrWarehouse.add("Noida WH");
        arrWarehouse.add("Gurugram WH");
        arrWarehouse.add("Pune WH");


        etTitle.addTextChangedListener(textWatcher);
        etGst.addTextChangedListener(textWatcher);
        etSezAddress.addTextChangedListener(textWatcher);
        etStateCode.addTextChangedListener(textWatcher);
        etPlaceofSupply.addTextChangedListener(textWatcher);



        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        pos = intent.getStringExtra("placeofsupply");
        gst = intent.getStringExtra("gst");
        addresss = intent.getStringExtra("sezaddress");
        statecode = intent.getStringExtra("statecode");

        etTitle.setText(title);
        etSezAddress.setText(addresss);
        etGst.setText(gst);
        etPlaceofSupply.setText(pos);
        etStateCode.setText(statecode);
        editAddress = intent.getBooleanExtra("edit",true);
        id = intent.getIntExtra("id",0);




        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.adaptertext, arrWarehouse);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(AddSEZAddress.this,arrWarehouse.get(i),Toast.LENGTH_SHORT).show();
                whName = arrWarehouse.get(i);
                wareHouseSelection = true;

            }
        });

        btnAddAddressActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wareHouseSelection) {
                    if (!editAddress) {
                        AddSEZAdress_Request address = new AddSEZAdress_Request();
                        address.setGstin(etGst.getText().toString());
                        address.setAddress(etSezAddress.getText().toString());
                        address.setTitle(etTitle.getText().toString());
                        address.setState_code(etStateCode.getText().toString());
                        address.setSupply_place(etPlaceofSupply.getText().toString());
                        saveSEZAddress(address);
                    } else {
                        AddSEZAdress_Request address = new AddSEZAdress_Request();
//                        address.setGstin(etGst.getText().toString());
//                        address.setAddress(etSezAddress.getText().toString());
//                        address.setTitle(etTitle.getText().toString());
//                        address.setState_code(etStateCode.getText().toString());
//                        address.setSupply_place(etPlaceofSupply.getText().toString());
                        String newGST = etGst.getText().toString();
                        String newAddress = etSezAddress.getText().toString();
                        String newTitle = etTitle.getText().toString();
                        String newStateCode = etStateCode.getText().toString();
                        String newSupplyPlace = etPlaceofSupply.getText().toString();

                        if (!newGST.equals(gst)){
                            address.setGstin(newGST);
                        }
                        if (!newAddress.equals(addresss)){
                            address.setAddress(newAddress);
                        }
                        if (!newTitle.equals(title)){
                            address.setTitle(newTitle);
                        }
                        if (!newStateCode.equals(statecode)){
                            address.setState_code(newStateCode);
                        }
                        if (!newSupplyPlace.equals(pos)){
                            address.setSupply_place(newSupplyPlace);
                        }
                        editSezAddress(address);
                    }
                }else{
                        Toast.makeText(AddSEZAddress.this, "Please Select Warehouse to Proceed", Toast.LENGTH_SHORT).show();
                    }
                }
        });

        btnAddAddressInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddSEZAddress.this,"All fields are mandatory",Toast.LENGTH_SHORT).show();
            }
        });

        btnViewAddrss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddSEZAddress.this,SEZ_ADDRESS_ACTIVITY.class);
                startActivity(i);
            }
        });
    }

    private void editSezAddress(AddSEZAdress_Request address) {
        PatchRequest aTask = new PatchRequest(this);
        aTask.setListener(new PatchRequest.MyAsyncTaskListener() {
            @Override
            public void onPreExecuteConcluded() {
                //Loader Will come here
            }

            @Override
            public void onPostExecuteConcluded(String result) {

                Log.d("ResultCheckpatch", "=" + result);
                try {
                    if (result != null) {
                        AddSezAddressResponse response = gson.fromJson(result, AddSezAddressResponse.class);
                        Log.e("responsecreate", response.getStatus().getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();


                }

            }
        });
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        String jsonInString = gson.toJson(address);
        String token = "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJqdGkiOiI2NzYzZWZlMy1jMTUzLTQwNjMtOTgxZi1iMThiZjg5ODc5NjYiLCJpZCI6MjExLCJtb2JpbGUiOiI4MDg4NDAzNDMyIiwicm9sZSI6IkFETUlOIiwicGVybWlzc2lvbnMiOlsidm1fYWxsb3dlZCIsInByb2R1Y3RfYWxsb3dlZCIsIm9yZGVyc19hbGxvd2VkIiwidXNlcl9hbGxvd2VkIiwidm1fc2V0dGluZ3MiLCJ2bV9zYWxlcyIsIm5vdGlmaWNhdGlvbl9lbmFibGVkIiwiYWJzb2x1dGVfc2FsZXMiLCJ2ZW5kb3JfYWxsb3dlZCIsIndhbGxldF9oaXN0b3J5Iiwidmlld19icmFuZHMiLCJjcmVhdGVfYnJhbmQiLCJ2aWV3X2Jhc2UiLCJ2aWV3X3ZhcmlhbnRzIiwibWFwX3ZlbmRvciIsInZpZXdfcHJvZHVjdHMiLCJjcmVhdGVfcHJvZHVjdHMiLCJjcmVhdGVfYmFzZSIsImNyZWF0ZV92YXJpYW50IiwiY3JlYXRlX3ZlbmRvciIsInZpZXdfdmVuZG9yIiwidmlld191c2VycyIsIm1hcF91c2VyIiwiYXR0ZW5kYW5jZSIsImtpdHRpbmdfcmVmaWxsaW5nIiwiYXVkaXRfbWFjaGluZSIsInN0b2NraW5nIiwic3BhcmVfcGFydHMiLCJlX2RhYWxjaGluaSIsInNsb3RfdXRpbGl6YXRpb24iLCJoZWFsdGhfYWxlcnQiLCJ0cm91Ymxlc2hvb3RfZ3VpZGUiLCJyYWlzZV90aWNrZXQiLCJzZWxmX2Fzc2Vzc21lbnQiLCJyZWNjZSIsInZtX2luc3RhbGxhdGlvbiIsInZpZXdfdGFnIiwiYWN0aXZhdGVfdGFnIiwibWVhbHNfbWVudSIsInZtX3BhcmFtZXRlcnMiLCJzdXBwb3J0X25vdGlmaWNhdGlvbiIsInNsb3RfcmVwYWlyIiwicXVpY2tfdW5ibG9jayIsInVuYmxvY2tfYWxsX3Nsb3RzIiwic2xvdF9yZXBvcnQiLCJwYXJ0bmVyX3JlY2hhcmdlIiwib3JkZXJfYXBwcm92ZSIsImJ1bGtfcHVyY2hhc2UiLCJtb2JpbGl0eV9yZWZpbGwiLCJtYXBfYnBfdXNlciIsInZpZXdfZnJhbmNoaXNlZSIsImNyZWF0ZV9mcmFuY2hpc2VlIiwidmlld19iYW5rIiwiY3JlYXRlX2JhbmsiLCJ2aWV3X3BsYXRmb3JtX2NoYXJnZSIsIm1hcF9wbGF0Zm9ybV9jaGFyZ2UiLCJjcmVhdGVfcGxhdGZvcm1fY2hhcmdlIiwibW9iaWxpdHlfY2hlY2tpbiIsImNyZWF0ZV9jb3Vwb24iLCJ2aWV3X2NvdXBvbiIsImJ1eV9hdF92cCIsIm1hcF9tYWNoaW5lcyIsInZpZXdfYnBfdXNlciIsInZtX2NyZWF0ZSIsInZtX3VwZGF0ZSIsInZtX3ZpZXciLCJjb2hvcnRfY3JlYXRlIiwiY29ob3J0X3ZpZXciLCJjb2hvcnRfbWFwX3VzZXIiLCJjb2hvcnRfbWFwX21hY2hpbmUiLCJiYW5uZXJfY3JlYXRlIiwiYXBwcm92ZV9hdHRlbmRhbmNlIiwic2xvdF9vcGVyYXRpb24iLCJyZWZ1bmRfcmV2Il0sImlhdCI6MTY4MzI3MjUyNSwiZXhwIjoxNjgzMzU4OTI1fQ.Y8fctxzH6Bkz0CUkym4W_3cmCw-PXDQg2igd2CEdaXezrVsYpLTFlUBdyVSzWRdM";
//        Log.e("url_________", "=" + Constant.CREATE_TRIP);
        Log.e("Request______________P", "=" + jsonInString);
        String url = "https://api-stage.daalchini.co.in/partner/api/v2/warehouse/1/sez-location/"+id;
        aTask.execute(url, jsonInString,"",token);

    }

    private void saveSEZAddress(AddSEZAdress_Request address) {
        PostRequest aTask = new PostRequest(this);
        aTask.setListener(new PostRequest.MyAsyncTaskListener() {
            @Override
            public void onPreExecuteConcluded() {
                //Loader Will come here
            }

            @Override
            public void onPostExecuteConcluded(String result) {

                Log.d("ResultCheck", "=" + result);
                try {
                    if (result != null) {
                        AddSezAddressResponse response = gson.fromJson(result, AddSezAddressResponse.class);
                        Log.e("responsecreate", response.getStatus().getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();


                }

            }
        });
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        String jsonInString = gson.toJson(address);
        String token = "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJqdGkiOiI2NzYzZWZlMy1jMTUzLTQwNjMtOTgxZi1iMThiZjg5ODc5NjYiLCJpZCI6MjExLCJtb2JpbGUiOiI4MDg4NDAzNDMyIiwicm9sZSI6IkFETUlOIiwicGVybWlzc2lvbnMiOlsidm1fYWxsb3dlZCIsInByb2R1Y3RfYWxsb3dlZCIsIm9yZGVyc19hbGxvd2VkIiwidXNlcl9hbGxvd2VkIiwidm1fc2V0dGluZ3MiLCJ2bV9zYWxlcyIsIm5vdGlmaWNhdGlvbl9lbmFibGVkIiwiYWJzb2x1dGVfc2FsZXMiLCJ2ZW5kb3JfYWxsb3dlZCIsIndhbGxldF9oaXN0b3J5Iiwidmlld19icmFuZHMiLCJjcmVhdGVfYnJhbmQiLCJ2aWV3X2Jhc2UiLCJ2aWV3X3ZhcmlhbnRzIiwibWFwX3ZlbmRvciIsInZpZXdfcHJvZHVjdHMiLCJjcmVhdGVfcHJvZHVjdHMiLCJjcmVhdGVfYmFzZSIsImNyZWF0ZV92YXJpYW50IiwiY3JlYXRlX3ZlbmRvciIsInZpZXdfdmVuZG9yIiwidmlld191c2VycyIsIm1hcF91c2VyIiwiYXR0ZW5kYW5jZSIsImtpdHRpbmdfcmVmaWxsaW5nIiwiYXVkaXRfbWFjaGluZSIsInN0b2NraW5nIiwic3BhcmVfcGFydHMiLCJlX2RhYWxjaGluaSIsInNsb3RfdXRpbGl6YXRpb24iLCJoZWFsdGhfYWxlcnQiLCJ0cm91Ymxlc2hvb3RfZ3VpZGUiLCJyYWlzZV90aWNrZXQiLCJzZWxmX2Fzc2Vzc21lbnQiLCJyZWNjZSIsInZtX2luc3RhbGxhdGlvbiIsInZpZXdfdGFnIiwiYWN0aXZhdGVfdGFnIiwibWVhbHNfbWVudSIsInZtX3BhcmFtZXRlcnMiLCJzdXBwb3J0X25vdGlmaWNhdGlvbiIsInNsb3RfcmVwYWlyIiwicXVpY2tfdW5ibG9jayIsInVuYmxvY2tfYWxsX3Nsb3RzIiwic2xvdF9yZXBvcnQiLCJwYXJ0bmVyX3JlY2hhcmdlIiwib3JkZXJfYXBwcm92ZSIsImJ1bGtfcHVyY2hhc2UiLCJtb2JpbGl0eV9yZWZpbGwiLCJtYXBfYnBfdXNlciIsInZpZXdfZnJhbmNoaXNlZSIsImNyZWF0ZV9mcmFuY2hpc2VlIiwidmlld19iYW5rIiwiY3JlYXRlX2JhbmsiLCJ2aWV3X3BsYXRmb3JtX2NoYXJnZSIsIm1hcF9wbGF0Zm9ybV9jaGFyZ2UiLCJjcmVhdGVfcGxhdGZvcm1fY2hhcmdlIiwibW9iaWxpdHlfY2hlY2tpbiIsImNyZWF0ZV9jb3Vwb24iLCJ2aWV3X2NvdXBvbiIsImJ1eV9hdF92cCIsIm1hcF9tYWNoaW5lcyIsInZpZXdfYnBfdXNlciIsInZtX2NyZWF0ZSIsInZtX3VwZGF0ZSIsInZtX3ZpZXciLCJjb2hvcnRfY3JlYXRlIiwiY29ob3J0X3ZpZXciLCJjb2hvcnRfbWFwX3VzZXIiLCJjb2hvcnRfbWFwX21hY2hpbmUiLCJiYW5uZXJfY3JlYXRlIiwiYXBwcm92ZV9hdHRlbmRhbmNlIiwic2xvdF9vcGVyYXRpb24iLCJyZWZ1bmRfcmV2Il0sImlhdCI6MTY4MzI3MjUyNSwiZXhwIjoxNjgzMzU4OTI1fQ.Y8fctxzH6Bkz0CUkym4W_3cmCw-PXDQg2igd2CEdaXezrVsYpLTFlUBdyVSzWRdM";
        Log.e("url_________", "=" + Constant.CREATE_TRIP);
        Log.e("Request______________", "=" + jsonInString);
        String url = "https://api-stage.daalchini.co.in/partner/api/v2/warehouse/1/sez-location";
        aTask.execute(url, jsonInString,token);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {
            String title = etTitle.getText().toString().trim();
            String sezAddress = etSezAddress.getText().toString().trim();
            String gst = etGst.getText().toString().trim();
            String placeofSupply = etPlaceofSupply.getText().toString().trim();
            String stateCode = etStateCode.getText().toString().trim();

            if (title.length() == 0 || sezAddress.length() == 0 || gst.length() == 0 || placeofSupply.length() == 0 || stateCode.length() == 0) {
                btnAddAddressInactive.setVisibility(View.VISIBLE);
                btnAddAddressActive.setVisibility(View.GONE);
            } else {
                btnAddAddressInactive.setVisibility(View.GONE);
                btnAddAddressActive.setVisibility(View.VISIBLE);

            }
        }
    };
}