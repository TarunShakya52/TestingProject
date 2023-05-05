package com.example.demoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.demoapplication.Contract.VmMachineContract;
import com.example.demoapplication.request.VMMachineModel;
import com.google.gson.Gson;

public class VmMachineDetails extends AppCompatActivity {

    private EditText etname,etStatus;
    private Button btnSubmit;
    VMMachineModel vmMachineModel;
    SharedPreferences mPrefs;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vm_machine_details);

        etname = findViewById(R.id.editTextTextPersonName);
        etStatus = findViewById(R.id.editTextTextPersonName2);
        btnSubmit = findViewById(R.id.button4);
        vmMachineModel = new VMMachineModel("","");
        mPrefs = getSharedPreferences("App", Context.MODE_PRIVATE);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vmMachineModel.setMachineName(etname.getText().toString());
                vmMachineModel.setStatus(etStatus.getText().toString());

                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(vmMachineModel);
                prefsEditor.putString("MyObject", json);
                prefsEditor.apply();

                VmMachineDetails.this.finish();

//                Intent i = new Intent(VmMachineDetails.this,CreateTripActivity.class);
//                i.putExtra("Vmdata",vmMachineModel);
//                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(vmMachineModel);
        prefsEditor.putString("MyObject", null);
        prefsEditor.apply();
    }
}