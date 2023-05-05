package com.example.demoapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapplication.R;
import com.example.demoapplication.request.VMMachineModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class InvoiceData_Adapter extends RecyclerView.Adapter<InvoiceData_Adapter.ViewHolder> {

    private Context context;

    public InvoiceData_Adapter(Context context){
        this.context = context;
    }


    @NonNull
    @Override
    public InvoiceData_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoicetable_rv, parent, false);
        return new InvoiceData_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceData_Adapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
