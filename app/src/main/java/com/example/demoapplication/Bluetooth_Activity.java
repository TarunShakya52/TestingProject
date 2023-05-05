package com.example.demoapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class Bluetooth_Activity extends AppCompatActivity {

    Button startBluethooth, discoverBluethooth, connectDevice, closeBluethooth,pairedDevices;
    ListView devicesListView;
    BluetoothAdapter mBTAdapter;
    ArrayAdapter<String> mBTArrayAdapter;
    BluetoothSocket mBTSocket = null;
    Handler mHandler;
    RadioButton btnOpen,btnClose;
    TextView txtDeviceName,txtDeviceAddress;
    private Set<BluetoothDevice> mPairedDevices;
    static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final static int REQUEST_ENABLE_BT = 1;
    private final static int MESSAGE_READ = 2;
    private final static int CONNECTING_STATUS = 3;
    private ConnectedThread mConnectedThread;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 642;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String deviceName = "nameKey";
    public static final String deviceMacId = "phoneKey";
    String address;
    String name;
    Boolean closeConnectionCheck = false;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        startBluethooth = findViewById(R.id.btnStartBluethooth);
        discoverBluethooth = findViewById(R.id.btnBluethoothDiscover);
        devicesListView = findViewById(R.id.devicesListView);
        connectDevice = findViewById(R.id.btnConnectDevice);
        closeBluethooth = findViewById(R.id.btnCloseBluethooth);
        pairedDevices = findViewById(R.id.PairedBtn);
        btnOpen = findViewById(R.id.btnOpen);
        btnClose = findViewById(R.id.btnClose);
        txtDeviceName = findViewById(R.id.txtName);
        txtDeviceAddress = findViewById(R.id.txtAddress);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mHandler = new Handler();

        mBTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter(); // get a handle on the bluetooth radio

        devicesListView.setAdapter(mBTArrayAdapter); // assign model to view
        devicesListView.setOnItemClickListener(mDeviceClickListener);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(blReceiver, filter);


        startBluethooth.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (!mBTAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//                    mBluetoothStatus.setText("Bluetooth enabled");
                    Toast.makeText(getApplicationContext(), "Bluetooth turned on", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth is already on", Toast.LENGTH_SHORT).show();
                }
            }
        });

        discoverBluethooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(Bluetooth_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED){
                        new AlertDialog.Builder(Bluetooth_Activity.this)
                                .setTitle("Give Permission")
                                .setMessage("Required Permission")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ActivityCompat.requestPermissions(Bluetooth_Activity.this,
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
        });


        closeBluethooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBT(v);
            }
        });

        pairedDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPairedDevices(v);
            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mConnectedThread != null)
                    mConnectedThread.write("2");
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mConnectedThread != null)
                    mConnectedThread.write("1");
            }
        });

        connectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!mBTAdapter.isEnabled()) {
//                    Toast.makeText(getBaseContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
//                    return;
//                }
////                // Get the device MAC address, which is the last 17 chars in the View
////                String info = ((TextView) v).getText().toString();
////                final String address = info.substring(info.length() - 17);
////                final String name = info.substring(0,info.length() - 17);
//                String address = "98:D3:34:91:07:B4";
////               String address = "8C:B8:7E:8B:EA:74";
//                new Thread() {
//                    @SuppressLint("MissingPermission")
//                    public void run() {
//                        boolean fail = false;
//
//                        BluetoothDevice device = mBTAdapter.getRemoteDevice(address);
//
//                        try {
//                            mBTSocket = createBluetoothSocket(device);
//                        } catch (IOException e) {
//                            fail = true;
//                            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                        // Establish the Bluetooth socket connection.
//                        try {
//                            mBTSocket.connect();
//                            Log.e("checkConnection","connected");
//                        } catch (IOException e) {
//                            Log.e("checkConnection",e.getMessage());
//                            try {
//                                fail = true;
//                                mBTSocket.close();
//                                mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
//                                        .sendToTarget();
//                            } catch (IOException e2) {
//                                //insert code to deal with this
//                                Toast.makeText(getBaseContext(), e2.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        if (!fail) {
//                            mConnectedThread = new ConnectedThread(mBTSocket);
//                            mConnectedThread.start();
//                            mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, "HC-05").sendToTarget();
////                            mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, "lenovo-ThinkBook-14-G2-ITL").sendToTarget();
//                        }
//                    }
//                }.start();
                Toast.makeText(Bluetooth_Activity.this, "Closing the connection", Toast.LENGTH_LONG).show();
                if (mConnectedThread != null) {
                    mConnectedThread.cancel();
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(deviceName, null);
                    editor.putString(deviceMacId, null);
                    editor.apply();
                } else {
                    Toast.makeText(Bluetooth_Activity.this, "Connection is already closed", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    @SuppressLint("MissingPermission")
    private void listPairedDevices(View v) {
        mPairedDevices = mBTAdapter.getBondedDevices();
        if(mBTAdapter.isEnabled()) {
            // put it's one to the adapter
            for (BluetoothDevice device : mPairedDevices)
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());

            Toast.makeText(Bluetooth_Activity.this, "Show Paired Devices", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(Bluetooth_Activity.this, "Bluetooth not on", Toast.LENGTH_SHORT).show();
    }

    private void closeBT(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            mBTAdapter.disable(); // turn off
            Toast.makeText(getApplicationContext(),"Bluetooth turned Off", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Toast.makeText(Bluetooth_Activity.this,"ENABLED",Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(Bluetooth_Activity.this,"DISABLED",Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("MissingPermission")
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    private void discover() {
        // Check if the device is already discovering
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            if (mBTAdapter.isDiscovering()) {
                mBTAdapter.cancelDiscovery();
                Toast.makeText(getApplicationContext(), "Discovery stopped", Toast.LENGTH_SHORT).show();
            } else {
                if (mBTAdapter.isEnabled()) {
                    mBTArrayAdapter.clear(); // clear items
                    mBTAdapter.startDiscovery();
                    Toast.makeText(getApplicationContext(), "Discovery started", Toast.LENGTH_SHORT).show();
                    registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // add the name to the list
                if (ActivityCompat.checkSelfPermission(Bluetooth_Activity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    if(device.getName() == null){
                        mBTArrayAdapter.add("N/A"+ "\n" + device.getAddress());
                    }else {
                    mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());}
                    mBTArrayAdapter.notifyDataSetChanged();
                }

            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                Toast.makeText(Bluetooth_Activity.this,"Device is now Connected",Toast.LENGTH_SHORT).show();
                txtDeviceName.setText(name);
                txtDeviceAddress.setText(address);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(deviceName,name);
                editor.putString(deviceMacId,address);
                editor.apply();
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Toast.makeText(Bluetooth_Activity.this,"Done with Searching",Toast.LENGTH_SHORT).show();
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                Toast.makeText(Bluetooth_Activity.this,"Device is about to Disconnect",Toast.LENGTH_SHORT).show();
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Toast.makeText(Bluetooth_Activity.this,"Device is now Disconnected",Toast.LENGTH_SHORT).show();
                txtDeviceName.setText("Device Disconnected");
                txtDeviceAddress.setText("Device Disconnected");
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(deviceName,null);
                editor.putString(deviceMacId,null);
                editor.apply();
                try {
                    mBTSocket.close();
                    mBTSocket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

private class ConnectedThread extends Thread {
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
        } catch (IOException e) { }

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
                if(bytes != 0) {
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
        } catch (IOException e) { }
    }

    /* Call this from the main activity to shutdown the connection */
    @SuppressLint("SetTextI18n")
    public void cancel() {
        try {
            mmSocket.close();
            txtDeviceName.setText("No Device Connected");
            txtDeviceAddress.setText("No Device Connected");
        } catch (IOException e) {
            Toast.makeText(Bluetooth_Activity.this,e.getMessage() , Toast.LENGTH_SHORT).show();
        }

    }
}

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (mBTSocket == null){
           onDeviceClick(view);
        }else {
                try {
                    mBTSocket.close();
                    onDeviceClick(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mConnectedThread != null)
            mConnectedThread.cancel();
        try {
            mBTSocket.close();
            mBTSocket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDeviceClick(View view){
        mHandler = new Handler();
        if (!mBTAdapter.isEnabled()) {
            Toast.makeText(getBaseContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
            return;
        }
        // Get the device MAC address, which is the last 17 chars in the View
        String info = ((TextView) view).getText().toString();
        address = info.substring(info.length() - 17);
        name = info.substring(0,info.length() - 19);
//            String address = "98:D3:34:91:07:B4";
////               String address = "8C:B8:7E:8B:EA:74";
        new Thread() {
            @SuppressLint("MissingPermission")
            public void run() {
                boolean fail = false;

                BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                try {
                    mBTSocket = createBluetoothSocket(device);
                    mConnectedThread = new ConnectedThread(mBTSocket);
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        public void run() {
                            Toast.makeText(Bluetooth_Activity.this, "Connecting with device ", Toast.LENGTH_LONG).show();
                            if (!(mConnectedThread.mmSocket.isConnected())){
                                txtDeviceName.setText("Connecting....");
                                txtDeviceAddress.setText("Connecting....");
                            }}
                    });
                } catch (IOException e) {
                    fail = true;
                }
                // Establish the Bluetooth socket connection.
                try {
                    mBTSocket.connect();
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        public void run() {
                            Toast.makeText(Bluetooth_Activity.this, e.getMessage()+" "+ "Please try to close Connection", Toast.LENGTH_SHORT).show();
                            try {
                                mBTSocket.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            mBTSocket = null;
                            if (!(mConnectedThread.mmSocket.isConnected())){
                                txtDeviceName.setText("Device not Connected");
                                txtDeviceAddress.setText("Device not Connected");
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
                                Toast.makeText(Bluetooth_Activity.this, e2.getMessage()+" "+ "Please try to close Connection", Toast.LENGTH_SHORT).show();
                                if (!(mConnectedThread.mmSocket.isConnected())){
                                    txtDeviceName.setText("Device not Connected");
                                    txtDeviceAddress.setText("Device not Connected");
                                }
                            }
                        });
                    }
                }
                if (!fail) {
                    mConnectedThread.start();
                    mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name).sendToTarget();
//                            mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, "lenovo-ThinkBook-14-G2-ITL").sendToTarget();
                }
            }
        }.start();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();

        String DeviceID,DeviceName;
        DeviceName = sharedPreferences.getString(deviceName,null);
        DeviceID = sharedPreferences.getString(deviceMacId,null);

        Log.e("devicename", sharedPreferences.getString(deviceName, null) + " " + sharedPreferences.getString(deviceMacId, null));

        if (DeviceID == null && DeviceName == null) {
            Toast.makeText(Bluetooth_Activity.this,"Device is not Connected", Toast.LENGTH_SHORT).show();
        } else if (mBTSocket == null){
            Log.e("devicename", sharedPreferences.getString(deviceName, null) + " " + sharedPreferences.getString(deviceMacId, null));

            if (!mBTAdapter.isEnabled()) {
                Toast.makeText(getBaseContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread() {
                @SuppressLint("MissingPermission")
                public void run() {
                    boolean fail = false;

                    BluetoothDevice device = mBTAdapter.getRemoteDevice(DeviceID);

                    try {
                        mBTSocket = createBluetoothSocket(device);
                        runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            public void run() {
                                Toast.makeText(Bluetooth_Activity.this, "Connecting with device ", Toast.LENGTH_LONG).show();
                                txtDeviceName.setText("Connecting....");
                                txtDeviceAddress.setText("Connecting....");
                            }
                        });
                    } catch (IOException e) {
                        fail = true;
                    }
                    // Establish the Bluetooth socket connection.
                    try {
                        mBTSocket.connect();
                        runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            public void run() {
                                Toast.makeText(Bluetooth_Activity.this, "Connected", Toast.LENGTH_SHORT).show();
                                txtDeviceName.setText("Device Name: "+ DeviceName);
                                txtDeviceAddress.setText("Device Address: "+ DeviceID);
                            }
                        });
                    } catch (IOException e) {
                        runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            public void run() {
                                Toast.makeText(Bluetooth_Activity.this, e.getMessage()+" "+ "Please try to close Connection or may be device already connected", Toast.LENGTH_SHORT).show();
                                txtDeviceName.setText("Device not Connected");
                                txtDeviceAddress.setText("Device not Connected");
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
                                    Toast.makeText(Bluetooth_Activity.this, e2.getMessage()+" "+ "Please try to close Connection or may be device already connected", Toast.LENGTH_SHORT).show();
                                    txtDeviceName.setText("Device not Connected");
                                    txtDeviceAddress.setText("Device not Connected");
                                }
                            });
                        }
                    }
                    if (!fail) {
                        mConnectedThread = new ConnectedThread(mBTSocket);
                        mConnectedThread.start();
                        mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, DeviceName).sendToTarget();
                    }
                }
            }.start();
        }else{
            Toast.makeText(Bluetooth_Activity.this,"Device is connected",Toast.LENGTH_SHORT).show();
            txtDeviceName.setText(DeviceName);
            txtDeviceAddress.setText(DeviceID);
        }
//        txtDeviceName.setText("Device not Connected");
//        txtDeviceAddress.setText("Device not Connected");
    }
}