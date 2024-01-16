package com.realappraiser.gharvalue.alarm;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.gson.Gson;
import com.realappraiser.gharvalue.AppDatabase;
import com.realappraiser.gharvalue.MyApplication;
import com.realappraiser.gharvalue.activities.LoginActivity;
import com.realappraiser.gharvalue.activities.SplashActivity;
import com.realappraiser.gharvalue.communicator.JsonRequestData;
import com.realappraiser.gharvalue.communicator.RequestParam;
import com.realappraiser.gharvalue.communicator.TaskCompleteListener;
import com.realappraiser.gharvalue.communicator.WebserviceCommunicator;
import com.realappraiser.gharvalue.model.OfflineDataModel;
import com.realappraiser.gharvalue.utils.Connectivity;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.SettingsUtils;
import com.realappraiser.gharvalue.worker.GeoUpdate;
import com.realappraiser.gharvalue.worker.OreoLocation;
import com.realappraiser.gharvalue.worker.WorkerManager;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LogOutReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(isLocationEnabled(context) && Connectivity.isConnected(context)){
            AppDatabase appDatabase = AppDatabase.getAppDatabase(MyApplication.getAppContext());
            ArrayList<OfflineDataModel> oflineData = (ArrayList) appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(true);
            if (oflineData.size() > 0) {
                General.customToast("Please sync your offline documents before logout!", context);
            }else{
                getLocation();
            }
        }else{
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> General.customToast("Please enable your GPS for Session Logout", context), 1000 );

        }
    }

    private boolean isLocationEnabled(Context context){
        return ((LocationManager) context.getSystemService(android.content.Context.LOCATION_SERVICE)).isProviderEnabled("gps");
    }

    private void getLocation() {
        AsyncTask.execute(() -> {
            String address = convertLatLngToAddress(MyApplication.getAppContext());
            @SuppressLint("SimpleDateFormat") String time =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(Calendar.getInstance().getTime());

            if (Connectivity.isConnected(MyApplication.getAppContext())) {
                JsonRequestData requestData = new JsonRequestData();
                String url = SettingsUtils.getInstance().getValue(SettingsUtils.API_BASE_URL,
                        "") + SettingsUtils.LocationTracker;
                requestData.setUrl(url);
                requestData.setCaseId("");
                requestData.setEmpId(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGIN_ID, ""));
                requestData.setLocationType("Logout");
                requestData.setLatitude(SettingsUtils.getInstance().getValue("lat", ""));
                requestData.setLongitude(SettingsUtils.getInstance().getValue("long", ""));
                requestData.setTrackerTime(time);
                requestData.setActivityType("4");
                requestData.setComments("");
                requestData.setAgencyBId(SettingsUtils.getInstance().getValue(SettingsUtils.BranchId, ""));
                Log.e("convertLatLngToAddrJson", address);

                SettingsUtils.getInstance().putValue("current_address", address);

                if (!address.isEmpty()) {
                    requestData.setAddress(address);
                } else {
                    address = SettingsUtils.convertLatLngToAddress(MyApplication.getAppContext());
                    requestData.setAddress(address);
                }


                requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
                requestData.setRequestBody(RequestParam.LocationTracker(requestData));

                Log.e("AlarmReceiver Request", new Gson().toJson(requestData));

                WebserviceCommunicator webserviceTask = new WebserviceCommunicator(MyApplication.getAppContext(),
                        requestData, SettingsUtils.POST_TOKEN);
                webserviceTask.setFetchMyData((TaskCompleteListener<JsonRequestData>) requestData1 -> {
                    SettingsUtils.getInstance().putValue(SettingsUtils.KEY_LOGGED_IN, false);

                    if(!SettingsUtils.getInstance().getValue(SettingsUtils.APP_STATUS, false)){
//                        System.exit(0);
//                    }else{
                        redirectLogin();
                    }
                });

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    AndroidAlarmScheduler.cancelAlarm();
                }
                webserviceTask.execute();
            }
        });
    }

    private String convertLatLngToAddress(Context context) {


        SettingsUtils.init(MyApplication.getAppContext());

        double latt = Double.parseDouble(SettingsUtils.getInstance().getValue("lat",""));
        double longi = Double.parseDouble(SettingsUtils.getInstance().getValue("long",""));

        String address = "Address not found";
        try {
            List<Address> addresses = new Geocoder(context, Locale.getDefault()).getFromLocation(latt, longi, 1);
            if (addresses.size() <= 0) {
                return address;
            }
            String addres = ((Address) addresses.get(0)).getAddressLine(0);
            StringBuilder sb = new StringBuilder();
            sb.append("convertLatLngToAddress: ");
            sb.append(addres);
            return addres;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Alarm Receiver", "onRunJob: Could not get address..!");
            return address;
        }
    }

    private void redirectLogin() {
        if (Build.VERSION.SDK_INT < 26) {
            MyApplication.getAppContext().stopService(new Intent(MyApplication.getAppContext(), GeoUpdate.class));
        } else {
            new OreoLocation(MyApplication.getAppContext()).stopOreoLocationUpdates();
        }
        new WorkerManager(MyApplication.getAppContext()).stopWorker();
        Intent intent = new Intent(MyApplication.getAppContext(), SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getAppContext().startActivity(intent);
    }
}