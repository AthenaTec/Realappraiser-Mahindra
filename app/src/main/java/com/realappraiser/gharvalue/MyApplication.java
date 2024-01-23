package com.realappraiser.gharvalue;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.realappraiser.gharvalue.utils.ConnectionReceiver;
import com.realappraiser.gharvalue.utils.SettingsUtils;

/**
 * Created by kaptas on 15/12/17.
 */

@SuppressWarnings("ALL")
public class MyApplication extends Application implements LifecycleObserver {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    public static final String TAG = MyApplication.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    public static MyApplication mInstance;


    public void onCreate() {
        if (false && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }
        super.onCreate();
       /*Fabric.with(this, new Crashlytics());*/
        MultiDex.install(this);
//        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false);

        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        context = this;
        mInstance = this;


        //For Location fetching
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "MyChannelId",
                    "Location",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public static MyApplication getApplication() {
        return (MyApplication) context;
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }

    // Method to return DisplayMetrics
    public DisplayMetrics getDisplayMatrix() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return dm;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


    /*******
     * Network Listener for Entire application - now other details only called
     * ********/
    public void setConnectionListener(ConnectionReceiver.ConnectionReceiverListener listener) {
        ConnectionReceiver.connectionReceiverListener = listener;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        SettingsUtils.getInstance().putValue(SettingsUtils.APP_STATUS, false);
        Log.e("MyApplication","APP in background");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        // App in foreground
        SettingsUtils.getInstance().putValue(SettingsUtils.APP_STATUS, true);
        Log.e("MyApplication","APP in Foreground");
    }
}
