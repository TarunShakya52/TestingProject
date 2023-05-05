package com.example.demoapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapplication.CreateTripActivity;
import com.example.demoapplication.R;
import com.example.demoapplication.request.VMMachineModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class CheckpointAdapter extends RecyclerView.Adapter<CheckpointAdapter.ViewHolder> {

    private Context context;
    private List<VMMachineModel> arrVmMachineModel;
    private BottomSheetDialog bottomSheetDialog;
    private Destination listner;

    public CheckpointAdapter(Context context, List<VMMachineModel> arrVmMachineModel, BottomSheetDialog bottomSheetDialog){
        this.context = context;
        this.arrVmMachineModel = arrVmMachineModel;
        this.bottomSheetDialog = bottomSheetDialog;
    }

    @NonNull
    @Override
    public CheckpointAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkpoints_recyclerview, parent, false);
        return new CheckpointAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VMMachineModel response = arrVmMachineModel.get(position);
        holder.machineName.setText(response.getMachineName());
        holder.machineName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.showDestination("Tarun");
                bottomSheetDialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrVmMachineModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView status;
        public TextView machineName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.txtKitStatus);
            machineName = itemView.findViewById(R.id.txtMachineName);
        }
    }

    public void setListener(Destination listener){
        this.listner = listener;
    }

   public interface Destination{
         void showDestination(String name);
    }
}
