package com.example.farshid.myapplication2;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ir.waspar.farshid.map.Address;
import ir.waspar.farshid.map.FarshidMapView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);




        // farshidMapView.addStaticMarker(51.3862788431399, 35.74946287535577,null,null,15);





//         farshidMapView.AddOnClickMap(new FarshidMapView.OnClickMap() {
//             @Override
//             public void onClick(Double X, Double Y) {
//                 farshidMapView.addMarker(X,Y,null,null);
//                 farshidMapView.getAddress(X,Y, new FarshidMapView.OnAddressReceive() {
//                     @Override
//                     public void onReceive(Address address) {
//                         Log.e("zzz", "onReceive: "+address.toString() );
//                     }
//
//                     @Override
//                     public void onNotFound() {
//
//                     }
//
//                     @Override
//                     public void onError() {
//
//                     }
//                 });
//             }
//         });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                startActivity(new Intent(this,SimpleMapActivity.class));
                break;
                case R.id.button2:
                startActivity(new Intent(this,OnClickMapActivity.class));
                break;
                case R.id.button3:
                startActivity(new Intent(this,OnLongClickMapActivity.class));
                break;
                case R.id.button4:
                startActivity(new Intent(this,StaticMarkerActivity.class));
                break;
                case R.id.button5:
                startActivity(new Intent(this,AddressFromPointActivity.class));
                break;
        }


    }
}