package com.example.demoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class PopUpActivity extends AppCompatActivity {

    private Button btnSubmit;
    private Dialog dialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);



        btnSubmit = findViewById(R.id.btnClick);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDailog();
            }
        });
    }

    private void openDailog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogscreen);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        Button bt_yes = (MaterialButton)dialog.findViewById(R.id.btnStop);
        TextView bt_no = (TextView) dialog.findViewById(R.id.txtNo);
        ImageView imgClose = (ImageView) dialog.findViewById(R.id.imgClose);

        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PopUpActivity.this,"Stopped working",Toast.LENGTH_SHORT).show();
            }
        });

        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PopUpActivity.this,"Ok it's Resumed",Toast.LENGTH_SHORT).show();
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}