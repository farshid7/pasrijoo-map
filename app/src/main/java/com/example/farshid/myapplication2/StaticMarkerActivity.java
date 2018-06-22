package com.example.farshid.myapplication2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import ir.waspar.farshid.map.FarshidMapView;

public class StaticMarkerActivity extends AppCompatActivity {

    private FarshidMapView farshidMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        farshidMapView=findViewById(R.id.mapview);
        farshidMapView.addStaticMarker(35.711191, 51.400505,null,null,12);
    }
}
