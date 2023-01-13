package com.example.demoapplication.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapplication.R;

import java.util.ArrayList;
import java.util.Arrays;

import pl.droidsonroids.gif.GifImageView;

public class UserGuideAdapter extends RecyclerView.Adapter<UserGuideAdapter.ViewHolder> {

    private Context context;
    ArrayList<String> list;
    private Dialog dialog =null;
    private ArrayList<Integer> gif = new ArrayList(Arrays.asList(R.drawable.angry,R.drawable.idea,R.drawable.load,R.drawable.rocket,
            R.drawable.giphy1,R.drawable.giphy));

    public UserGuideAdapter(Context context,ArrayList<String> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public UserGuideAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_userguide, parent, false);
        return new UserGuideAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserGuideAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textGuide.setText(list.get(position));
        holder.imgForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp(gif.get(position));
            }
        });
    }

    private void showPopUp(Integer gif) {
        if ((dialog == null) || !dialog.isShowing()) {
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.gifpopup);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);

            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            GifImageView gifImg = (GifImageView) dialog.findViewById(R.id.imgGif);
            ImageView btnClose = (ImageView) dialog.findViewById(R.id.btnClose);

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            gifImg.setImageResource(gif);
//        Glide.with(context).load(gif).into(gifImg);
            dialog.show();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textGuide;
        public ImageView imgForward;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textGuide = itemView.findViewById(R.id.txtGuide);
            imgForward = itemView.findViewById(R.id.imgForward);
        }
    }
}
