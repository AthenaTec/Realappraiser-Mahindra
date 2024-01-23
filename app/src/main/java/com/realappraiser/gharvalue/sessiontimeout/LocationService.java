package com.realappraiser.gharvalue.sessiontimeout;


import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.LocationServices;
import com.realappraiser.gharvalue.R;


public class LocationService extends Service {
    private LocationClient locationClient;

    public static final String ACTION_START = "START";
    public static final String ACTION_STOP = "STOP";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationClient = new DefaultLocationClient(
                getApplicationContext(),
                LocationServices.getFusedLocationProviderClient(getApplicationContext()));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_START:
                    start();
                    break;
                case ACTION_STOP:
                    stop(startId);
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void start() {
          NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "MyChannelId")
                    .setContentTitle("Fetching your location...")
                    .setSmallIcon(R.mipmap.launcher_logo)
                    .setOngoing(true);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

           Location location = locationClient.getLocationUpdates(10000L);

           if(location != null ){
               String lat = String.valueOf(location.getLatitude()).substring(Math.max(String.valueOf(location.getLatitude()).length() - 3, 0));
               String lon = String.valueOf(location.getLongitude()).substring(Math.max(String.valueOf(location.getLongitude()).length() - 3, 0));
               NotificationCompat.Builder updatedNotification = notification.setContentText("Location: (" + lat + ", " + lon + ")");
               Log.e("MyLocation", "lat " + lat + " long  " + lon);
               notificationManager.notify(1, updatedNotification.build());
           }
            startForeground(1, notification.build());
        }

    private  void stop(int startId) {
            stopForeground(true);
            stopSelf(startId);
    }

}
