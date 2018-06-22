package ir.waspar.farshid.map;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import com.intentfilter.androidpermissions.PermissionManager;

import static java.util.Collections.singleton;


public class PermissionHandler {


    private Context context;
    private PermissionManager permissionManager;
    private OnPermissionHandler onPermissionHandler;


    public PermissionHandler(final Context context, OnPermissionHandler onPermissionHandler) {

        this.context = context;


        permissionManager = PermissionManager.getInstance(context);
        this.onPermissionHandler = onPermissionHandler;


        HandlePermission();
    }

    private void HandlePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                permissionManager.checkPermissions(singleton(Manifest.permission.WRITE_EXTERNAL_STORAGE), new PermissionManager.PermissionRequestListener() {
                    @Override
                    public void onPermissionGranted() {

                    }

                    @Override
                    public void onPermissionDenied() {
                        Toast.makeText(context, "دسترسی به موقعیت صادر نشد !", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionManager.checkPermissions(singleton(Manifest.permission.ACCESS_FINE_LOCATION), new PermissionManager.PermissionRequestListener() {
                    @Override
                    public void onPermissionGranted() {
                        onPermissionHandler.Handled();
                    }

                    @Override
                    public void onPermissionDenied() {
                        finish();
                    }
                });



            }
        }
    }

    private void finish() {
        ((Activity)context).finish();
    }
    public interface OnPermissionHandler {
        void Handled();
    }


}
