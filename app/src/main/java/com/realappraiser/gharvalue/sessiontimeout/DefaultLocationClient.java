package com.realappraiser.gharvalue.sessiontimeout;



import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import android.os.Looper;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.realappraiser.gharvalue.utils.SettingsUtils;

import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class DefaultLocationClient implements LocationClient {
    private final Context context;
    private final FusedLocationProviderClient client;

    Location userLocation = null;

    public DefaultLocationClient(Context context, FusedLocationProviderClient client) {
        this.context = context;
        this.client = client;
    }


    @Override
    public Location getLocationUpdates(Long intervel) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGpsEnabled && !isNetworkEnabled) {
            Log.e("DefaultLocation", "Permission denied");
        } else {

            LocationRequest request = LocationRequest.create()
                    .setInterval(intervel)
                    .setFastestInterval(intervel);
            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult result) {
                    super.onLocationResult(result);
                    Location location = result.getLastLocation();

                    SettingsUtils.Latitudes = location.getLatitude();
                    SettingsUtils.Longitudes = location.getLongitude();
                    SettingsUtils.getInstance().putValue("lat", String.valueOf(location.getLatitude()));
                    SettingsUtils.getInstance().putValue("long",String.valueOf(location.getLongitude()));

                    Log.e(DefaultLocationClient.class.getName(), "lat" + location.getLatitude() + "....Long" + location.getLongitude());
                    userLocation = location;
                }
            };
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return userLocation;
            }
            client.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
            );
        }

        return userLocation;
    }

    @Override
    public Exception getException(String message) {
        return null;
    }
}
