package com.example.demoapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapplication.Adapter.BluetoothDevicesAdapter;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class Blutooth_Driver_Activity extends AppCompatActivity implements BluetoothDevicesAdapter.DeviceDetails {

    MaterialButton btnOpen, btnClose, btnLock, btnUnlock,btnDisconnect;
    BluetoothAdapter mBTAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 642;
    BluetoothDevicesAdapter bluetoothDevicesAdapter;
    ArrayList<BluetoothDevice> arrBluethoothDevices = new ArrayList<>();
    RecyclerView recyclerView;
    BluetoothSocket mBTSocket = null;
    Handler mHandler;
    private static ConnectedThread mConnectedThread;
    private final static int MESSAGE_READ = 2;
    private final static int CONNECTING_STATUS = 3;
    IntentFilter filter;
    TextView txtConnectedDevice,txtMacAddress;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blutooth_driver);

        btnOpen = findViewById(R.id.materialButtonopen);
//        btnClose = findViewById(R.id.materialButtonclose);
        recyclerView = findViewById(R.id.devicesDetails_rv);
        btnLock = findViewById(R.id.btnLock);
        btnUnlock = findViewById(R.id.btnUnlock);
        txtConnectedDevice = findViewById(R.id.txtDeviceName);
        txtMacAddress = findViewById(R.id.txtMacAddress);
        btnDisconnect = findViewById(R.id.btnDisconnect);
        mHandler = new Handler();
        bluetoothDevicesAdapter = new BluetoothDevicesAdapter(Blutooth_Driver_Activity.this, arrBluethoothDevices, this);

        mBTAdapter = BluetoothAdapter.getDefaultAdapter(); // get a handle on the bluetooth radio

//
//        filter = new IntentFilter();
//        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
//        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
//        this.registerReceiver(blReceiver, filter);

        btnLock.setVisibility(View.GONE);
        btnUnlock.setVisibility(View.GONE);


        btnOpen.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (!mBTAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    Toast.makeText(getApplicationContext(), "Bluetooth turned on", Toast.LENGTH_SHORT).show();
                    getDevices();
                } else {
//                    btnOpen.setText("Stop Search");
//                    btnOpen.setBackgroundColor(getResources().getColor(R.color.red));
//                    recyclerView.setVisibility(View.VISIBLE);
//                    getDevices();
//                    registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
                    discover();
                }
            }
        });

        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBTSocket != null){
                    mConnectedThread.cancel();
                }
            }
        });
//        btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(Blutooth_Driver_Activity.this, "Closing the connection", Toast.LENGTH_LONG).show();
//                if (mConnectedThread != null) {
//                    mConnectedThread.cancel();
//                    Toast.makeText(Blutooth_Driver_Activity.this, "Connection is Closed", Toast.LENGTH_SHORT).show();
//                    btnUnlock.setVisibility(View.GONE);
//                    btnLock.setVisibility(View.GONE);
//                } else {
//                    Toast.makeText(Blutooth_Driver_Activity.this, "Connection is already closed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mConnectedThread != null)
                    btnLock.setVisibility(View.GONE);
                    btnUnlock.setVisibility(View.VISIBLE);
                    mConnectedThread.write("1");
            }
        });

        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mConnectedThread != null)
                    btnLock.setVisibility(View.VISIBLE);
                    btnUnlock.setVisibility(View.GONE);
                    mConnectedThread.write("2");


            }
        });
    }

//    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
//        @SuppressLint("SetTextI18n")
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                // add the name to the list
//                if (ActivityCompat.checkSelfPermission(Blutooth_Driver_Activity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                    arrBluethoothDevices.add(device);
//                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//
//                    assert recyclerView != null;
//                    recyclerView.setLayoutManager(mLayoutManager);
////                    checkpointAdapter.setListener(this);
//                    recyclerView.setAdapter(bluetoothDevicesAdapter);
//                    bluetoothDevicesAdapter.notifyDataSetChanged();
//                } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
//                    Toast.makeText(Blutooth_Driver_Activity.this, "Device is Connected", Toast.LENGTH_SHORT).show();
//                } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
//                    Toast.makeText(Blutooth_Driver_Activity.this, "Device is now Disconnected", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        }
//    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Toast.makeText(Blutooth_Driver_Activity.this, "ENABLED", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(Blutooth_Driver_Activity.this, "DISABLED", Toast.LENGTH_LONG).show();
        }
    }


    @SuppressLint("MissingPermission")
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        discover();
                    }

                } else {
                    Log.e("denied", "Permission Denied");
                }
                return;
            }
        }
    }

    private void discover() {
        // Check if the device is already discovering
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            if (mBTAdapter.isDiscovering()) {
                mBTAdapter.cancelDiscovery();
                Toast.makeText(getApplicationContext(), "Discovery stopped", Toast.LENGTH_SHORT).show();
                btnOpen.setText("Start Discovery");
                btnOpen.setBackgroundColor(getResources().getColor(R.color.green));
            } else {
                if (mBTAdapter.isEnabled()) {
                    arrBluethoothDevices.clear(); // clear items
                    mBTAdapter.startDiscovery();
                    Toast.makeText(getApplicationContext(), "Discovery started", Toast.LENGTH_SHORT).show();
                    btnOpen.setText("Stop Search");
                    btnOpen.setBackgroundColor(getResources().getColor(R.color.red));
                    recyclerView.setVisibility(View.VISIBLE);
//                    getDevices();
//                    registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
                    txtConnectedDevice.setVisibility(View.GONE);
                    txtMacAddress.setVisibility(View.GONE);
                    registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    void getDevices() {
        int permissionCheck = ContextCompat.checkSelfPermission(Blutooth_Driver_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(Blutooth_Driver_Activity.this)
                    .setTitle("Give Permission")
                    .setMessage("Required Permission")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(Blutooth_Driver_Activity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    })
                    .create()
                    .show();
        } else {
            Log.d("DISCOVERING-PERMISSIONS", "Permissions Granted");
            discover();
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(bluetoothReceiver, filter);


    }

    @Override
    public void onConnectDevice(View view, String deviceName, String deviceId) {
        if (mBTSocket == null) {
            onDeviceClick(view, deviceName, deviceId);
        } else {
            try {
                mBTSocket.close();
                onDeviceClick(view, deviceName, deviceId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onDeviceClick(View view, String deviceName, String address) {
        mHandler = new Handler();
        if (!mBTAdapter.isEnabled()) {
            Toast.makeText(getBaseContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread() {
            @SuppressLint("MissingPermission")
            public void run() {
                boolean fail = false;

                BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                try {
                    mBTSocket = createBluetoothSocket(device);
                    mConnectedThread = new Blutooth_Driver_Activity.ConnectedThread(mBTSocket);
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        public void run() {
                            Toast.makeText(Blutooth_Driver_Activity.this, "Connecting with device ", Toast.LENGTH_LONG).show();

//                            if (!(mConnectedThread.mmSocket.isConnected())) {
//                                btnLock.setVisibility(View.VISIBLE);
//                                btnUnlock.setVisibility(View.VISIBLE);
//                                Toast.makeText(Blutooth_Driver_Activity.this, "Device is connected", Toast.LENGTH_SHORT).show();
//                            }
                        }
                    });
                } catch (Exception e) {
                    fail = true;
                }
                // Establish the Bluetooth socket connection.
                try {
                    mBTSocket.connect();
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        public void run() {
                            Toast.makeText(Blutooth_Driver_Activity.this, e.getMessage() + " " + "Please try to close Connection", Toast.LENGTH_SHORT).show();
                            try {
                                mBTSocket.close();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            mBTSocket = null;
                            if (!(mConnectedThread.mmSocket.isConnected())) {
                                Toast.makeText(Blutooth_Driver_Activity.this, "Device is not able connect", Toast.LENGTH_SHORT).show();
                            }
//
                        }
                    });
                    try {
                        fail = true;
                        mBTSocket.close();
                        mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                .sendToTarget();
                    } catch (IOException e2) {
                        //insert code to deal with this
                        runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            public void run() {
                                Toast.makeText(Blutooth_Driver_Activity.this, e2.getMessage() + " " + "Please try to close Connection", Toast.LENGTH_SHORT).show();
                                if (!(mConnectedThread.mmSocket.isConnected())) {
                                    Toast.makeText(Blutooth_Driver_Activity.this, "Device is not able connect", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                if (!fail) {
                    mConnectedThread.start();
                    mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, deviceName).sendToTarget();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            btnLock.setVisibility(View.VISIBLE);
//                            btnUnlock.setVisibility(View.VISIBLE);
                            Toast.makeText(Blutooth_Driver_Activity.this, "Device is connected", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Blutooth_Driver_Activity.this,MobilityActivity.class);
                            intent.putExtra("checkConnection",true);
                            startActivity(intent);
//
//                            txtConnectedDevice.setVisibility(View.VISIBLE);
//                            txtMacAddress.setVisibility(View.VISIBLE);
//                            txtConnectedDevice.setText("Connected Device:-  "+deviceName);
//                            txtMacAddress.setText("MAC Address:-  "+address);
//                            recyclerView.setVisibility(View.GONE);
//                            btnOpen.setVisibility(View.GONE);
//                            btnDisconnect.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }.start();
    }

    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                        bytes = mmInStream.available(); // how many bytes are ready to be read?
                        bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget(); // Send the obtained bytes to the UI activity
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        @SuppressLint("SetTextI18n")
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(Blutooth_Driver_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }


    }

    public static ConnectedThread getInstance(){
        return  mConnectedThread;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mConnectedThread.cancel();
//        unregisterReceiver(blReceiver);
            unregisterReceiver(bluetoothReceiver);
        }catch (Exception e){

        }

    }

    private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // add the name to the list
                if (ActivityCompat.checkSelfPermission(Blutooth_Driver_Activity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    arrBluethoothDevices.add(device);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                    assert recyclerView != null;
                    recyclerView.setLayoutManager(mLayoutManager);
//                    checkpointAdapter.setListener(this);
                    recyclerView.setAdapter(bluetoothDevicesAdapter);
                    bluetoothDevicesAdapter.notifyDataSetChanged();
                }
            }
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                if (ActivityCompat.checkSelfPermission(Blutooth_Driver_Activity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }else {
                    Toast.makeText(Blutooth_Driver_Activity.this, "Device is Connected ", Toast.LENGTH_SHORT).show();
                }

            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            Toast.makeText(Blutooth_Driver_Activity.this,"Device is Disconnected ",Toast.LENGTH_SHORT).show();
                txtConnectedDevice.setText("Device Connected:-  "+"No Device Connected");
                txtMacAddress.setText("MAC Address:-  "+"No Device Connected");
                btnLock.setVisibility(View.GONE);
                btnUnlock.setVisibility(View.GONE);
                txtMacAddress.setVisibility(View.VISIBLE);
                txtConnectedDevice.setVisibility(View.VISIBLE);
                btnOpen.setVisibility(View.VISIBLE);
                btnDisconnect.setVisibility(View.GONE);
                btnOpen.setText("Start Discover");
                btnOpen.setBackgroundColor(getResources().getColor(R.color.green));
                recyclerView.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(bluetoothReceiver);
    }
}