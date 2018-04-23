package com.example.lukehogan.friendtrack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

@SuppressLint("Registered")
public class StartTracking extends Activity {

    private static final int PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (lm != null) {
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "Please allow app to use location services", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Please enable location , displaying last known location", Toast.LENGTH_SHORT).show();
        }
        int permission = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        //request permission
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startService(new Intent(this, TrackerService.class));
            finish();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST);
        }
    }


    @Override //once permission granted start tracker service
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startService(new Intent(this, TrackerService.class));
            finish();
        } else {
            finish();
        }
    }
}
