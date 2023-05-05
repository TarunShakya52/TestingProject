package com.example.demoapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MobilityActivity extends AppCompatActivity {

    Button btnUnlock,btnLock;
    Blutooth_Driver_Activity.ConnectedThread mConnect;
    Boolean checkDeviceConnect;
    IntentFilter filter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobility);

        btnUnlock = findViewById(R.id.button5);
        btnLock = findViewById(R.id.button6);

        mConnect = Blutooth_Driver_Activity.getInstance();

        Intent intent = getIntent();
        checkDeviceConnect = intent.getBooleanExtra("checkConnection",false);


        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDeviceConnect){
                mConnect.write("2");}
                else {
                    Intent intent = new Intent(MobilityActivity.this,Blutooth_Driver_Activity.class);
                    startActivity(intent);
                }
            }
        });

        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDeviceConnect){
                    mConnect.write("1");}
                else {
                    Intent intent = new Intent(MobilityActivity.this,Blutooth_Driver_Activity.class);
                    startActivity(intent);
                }
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(bluetoothReceiver, filter);

    }

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                if (ActivityCompat.checkSelfPermission(MobilityActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }else {
                    Toast.makeText(MobilityActivity.this, "Device is Connected ", Toast.LENGTH_SHORT).show();
                }

            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Toast.makeText(MobilityActivity.this,"Device is Disconnected ",Toast.LENGTH_SHORT).show();
                checkDeviceConnect = false;
            }
        }
    };
}