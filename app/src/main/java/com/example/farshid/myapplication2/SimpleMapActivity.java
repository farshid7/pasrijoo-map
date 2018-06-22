package com.example.farshid.myapplication2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ir.waspar.farshid.map.FarshidMapView;

public class SimpleMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        FarshidMapView farshidMapView = findViewById(R.id.mapview);
        farshidMapView.setZoomLevel(8);

        farshidMapView.setLocationEnabled(true);
        farshidMapView.setZoomEnabled(true);


    }
}
