package com.example.demoapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapplication.R;
import com.example.demoapplication.response.SezAddressGetResponse;
import com.example.demoapplication.response.SezLocation;
import com.google.android.material.card.MaterialCardView;

public class SEZddressAdapter extends RecyclerView.Adapter<SEZddressAdapter.ViewHolder> {

    private Context context;
    private SezAddressGetResponse response;
    private sezDetails listner;
    private Boolean edit;
    private Boolean shipping;
    private Boolean billing;

    public SEZddressAdapter(Context context, SezAddressGetResponse response,sezDetails listner,Boolean edit){
        this.context = context;
        this.response = response;
        this.listner = listner;
        this.edit = edit;
    }

    public SEZddressAdapter(Context context,SezAddressGetResponse response,sezDetails listner,Boolean shipping,Boolean billing,Boolean edit){
        this.context = context;
        this.response = response;
        this.shipping = shipping;
        this.listner = listner;
        this.billing = billing;
        this.edit = edit;
    }

    @NonNull
    @Override
    public SEZddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sez_address_rv, parent, false);
        return new SEZddressAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SEZddressAdapter.ViewHolder holder, int position) {

        SezLocation data = response.getData().getSezLocations().get(position);


        if (edit){
            holder.edit.setVisibility(View.VISIBLE);
        }else {
            holder.edit.setVisibility(View.GONE);
            if (shipping && !billing){
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.getShippingDetails(data.getTitle(),data.getGstin(),data.getSupplyPlace(),data.getStateCode(),data.getAddress(),data.getId());
                }
            });
            }else {
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listner.getBillingDetails(data.getTitle(),data.getGstin(),data.getSupplyPlace(),data.getStateCode(),data.getAddress(),data.getId());
                    }
                });
            }
        }

        holder.tvcompanyName.setText(data.getTitle());
        holder.tvGstIn.setText("GSTIN - "+data.getGstin());
        holder.tvAddressLine3.setText(data.getSupplyPlace() +" - "+data.getStateCode());
        holder.tvAddressLine1.setText(data.getAddress());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.getDetails(data.getTitle(),data.getGstin(),data.getSupplyPlace(),data.getStateCode(),data.getAddress(),data.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return response.getData().getSezLocations().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvcompanyName,tvAddressLine1,tvAddressLine2,tvAddressLine3,tvGstIn;
        private CardView edit;
        private MaterialCardView layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvcompanyName = itemView.findViewById(R.id.tvCompanyName);
            tvAddressLine1 =itemView.findViewById(R.id.tvAdressLine1);
//            tvAddressLine2 =itemView.findViewById(R.id.tvAdressLine2);
            tvAddressLine3 = itemView.findViewById(R.id.tvAdressLine3);
            tvGstIn = itemView.findViewById(R.id.tvGstIn);
            edit = itemView.findViewById(R.id.imgEdit);
            layout = itemView.findViewById(R.id.materialCardView2);
        }
    }

    public interface sezDetails{
        public void getDetails(String title, String gstin, String supplyPlace, String stateCode, String address,int id);
        public void getShippingDetails(String title, String gstin, String supplyPlace, String stateCode, String address,int id);
        public void getBillingDetails(String title, String gstin, String supplyPlace, String stateCode, String address,int id);
    }
}
