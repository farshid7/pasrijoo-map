package com.example.farshid.myapplication2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.osmdroid.views.overlay.Marker;

import ir.waspar.farshid.map.FarshidMapView;

public class OnLongClickMapActivity extends AppCompatActivity {

    private FarshidMapView farshidMapView;
    private Marker marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        farshidMapView = findViewById(R.id.mapview);
        farshidMapView.setZoomLevel(8);

        farshidMapView.setLocationEnabled(true);
        farshidMapView.setZoomEnabled(true);

        farshidMapView.addOnLongClickMap(new FarshidMapView.OnLongClickMap() {
            @Override
            public void onClick(Double X, Double Y) {
                if(marker!=null){
                    farshidMapView.removeMarker(marker);
                }
                marker=farshidMapView.addMarker(X,Y,null,null);
            }
        });


    }
}
