package com.example.demoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DummyActivity extends AppCompatActivity {

    private Button btnHelp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

        btnHelp = findViewById(R.id.btnHelp);

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(DummyActivity.this, UserGuideActivity.class);
                startActivity(myIntent);
            }
        });
    }
}