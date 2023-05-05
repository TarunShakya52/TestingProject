package com.example.demoapplication.Adapter;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapplication.R;

import java.util.ArrayList;

public class BluetoothDevicesAdapter extends RecyclerView.Adapter<BluetoothDevicesAdapter.ViewHolder>{

    private Context context;
    private ArrayList<BluetoothDevice> arrBluethoothDevices;
    private DeviceDetails deviceDetails;


    public BluetoothDevicesAdapter(Context context, ArrayList<BluetoothDevice> arrBluethoothDevices,DeviceDetails deviceDetails){
        this.context = context;
        this.arrBluethoothDevices = arrBluethoothDevices;
        this.deviceDetails = deviceDetails;
    }


    @NonNull
    @Override
    public BluetoothDevicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.devices_rv, parent, false);
        return new BluetoothDevicesAdapter.ViewHolder(view);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(@NonNull BluetoothDevicesAdapter.ViewHolder holder, int position) {
        BluetoothDevice device = arrBluethoothDevices.get(position);
        if (device.getName() == null || device.getName().equals("")){
            holder.deviceName.setText("Device Name: Not Available");
        }else {holder.deviceName.setText("Device Name "+device.getName());}
        holder.deviceId.setText("Device Id "+device.getAddress());

        holder.devicesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String info = ((TextView) view).getText().toString();
//                address = info.substring(info.length() - 17);
//                name = info.substring(0,info.length() - 19);
                String deviceName = device.getName();
                String deviceId = device.getAddress();
                deviceDetails.onConnectDevice(view,deviceName,deviceId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrBluethoothDevices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView deviceName;
        public TextView deviceId;
        public LinearLayout devicesLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceId = itemView.findViewById(R.id.tvDeviceId);
            deviceName = itemView.findViewById(R.id.tvDeviceName);
            devicesLayout = itemView.findViewById(R.id.layoutDevices);
        }
    }

    public interface DeviceDetails{
        void onConnectDevice(View view, String deviceName, String deviceId);
    }
}
