package com.example.android_ex2.Utillities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.android_ex2.MainActivity;

public class LocationHelper {

    private final LocationManager locationManager;
    private final LocationListener locationListener;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private final Context context;

    public LocationHelper(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = createLocationListener();
        checkPermissionsAndStartLocationUpdates();
    }

    private LocationListener createLocationListener() {
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }

    private void checkPermissionsAndStartLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((MainActivity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        startLocationUpdates();
        getLastKnownLocation();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Use GPS_PROVIDER if available, otherwise NETWORK_PROVIDER
            String provider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ?
                    LocationManager.GPS_PROVIDER :
                    LocationManager.NETWORK_PROVIDER;

            // Request location updates with minimum time interval of 1000 ms (1 second) and minimum distance interval of 10 meters
            locationManager.requestLocationUpdates(provider, 1000, 10, locationListener);
        }
    }


    private void getLastKnownLocation() {
        try {
            // Use GPS_PROVIDER if available, otherwise NETWORK_PROVIDER
            String provider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ?
                    LocationManager.GPS_PROVIDER :
                    LocationManager.NETWORK_PROVIDER;

            Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
            if (lastKnownLocation != null) {
                latitude = lastKnownLocation.getLatitude();
                longitude = lastKnownLocation.getLongitude();
                Log.d("Location", "Last known Latitude: " + latitude + ", Longitude: " + longitude);
            } else {
                Log.d("Location", "Last known location is null");
            }
        } catch (SecurityException e) {
            Log.e("Location", "Failed to get last known location: " + e.getMessage());
        }
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                }
            }
        }
    }
}

