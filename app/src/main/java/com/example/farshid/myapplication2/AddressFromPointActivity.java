package com.example.farshid.myapplication2;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ir.waspar.farshid.map.Address;
import ir.waspar.farshid.map.FarshidMapView;

public class AddressFromPointActivity extends AppCompatActivity {


    private FarshidMapView farshidMapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        farshidMapView=findViewById(R.id.mapview);
        farshidMapView.setZoomEnabled(true);
        farshidMapView.setLocationEnabled(true);
        farshidMapView.setZoomLevel(8);
        farshidMapView.addOnClickMap(new FarshidMapView.OnClickMap() {
            @Override
            public void onClick(Double X, Double Y) {
                farshidMapView.getAddress(X, Y, new FarshidMapView.OnAddressReceive() {
                    @Override
                    public void onReceive(Address address) {
                        showAddress(address);
                    }

                    @Override
                    public void onNotFound() {

                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        });
    }

    private void showAddress(Address address) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("آدرس")
                .setMessage(formatString(address.toString()))
                .setPositiveButton("تایید",null)
                .show();
    }


    public String formatString(String text) {

        StringBuilder json = new StringBuilder();
        String indentString = "";

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            switch (letter) {
                case '{':
                case '[':
                    json.append("\n" + indentString + letter + "\n");
                    indentString = indentString + "\t";
                    json.append(indentString);
                    break;
                case '}':
                case ']':
                    indentString = indentString.replaceFirst("\t", "");
                    json.append("\n" + indentString + letter);
                    break;
                case ',':
                    json.append(letter + "\n" + indentString);
                    break;

                default:
                    json.append(letter);
                    break;
            }
        }

        return json.toString();
    }
}
