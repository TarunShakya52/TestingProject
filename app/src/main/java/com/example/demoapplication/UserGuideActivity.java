package com.example.demoapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.demoapplication.Adapter.UserGuideAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserGuideActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public UserGuideAdapter userGuideAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);


        ArrayList<String> listGuide = new ArrayList<>(Arrays.asList("How to place an order via vending machine",
                "How to place an order via mobile","How to pick up your order placed via mobile","ed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium",
                "ed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium",
                "ed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium"));
        recyclerView = findViewById(R.id.userGuideRV);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        userGuideAdapter = new UserGuideAdapter(this, listGuide);
        recyclerView.setAdapter(userGuideAdapter);

    }
}