package com.realappraiser.gharvalue.activities;

import static com.realappraiser.gharvalue.utils.General.REQUEST_ID_MULTIPLE_PERMISSIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.realappraiser.gharvalue.AppDatabase;
import com.realappraiser.gharvalue.MyApplication;
import com.realappraiser.gharvalue.communicator.JsonRequestData;
import com.realappraiser.gharvalue.communicator.RequestParam;
import com.realappraiser.gharvalue.communicator.TaskCompleteListener;
import com.realappraiser.gharvalue.communicator.WebserviceCommunicator;
import com.realappraiser.gharvalue.model.Case;
import com.realappraiser.gharvalue.model.IndProperty;
import com.realappraiser.gharvalue.model.IndPropertyValuation;
import com.realappraiser.gharvalue.model.Property;
import com.realappraiser.gharvalue.utils.Connectivity;
import com.realappraiser.gharvalue.utils.GPSService;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.SettingsUtils;
import com.realappraiser.gharvalue.utils.Singleton;
import com.realappraiser.gharvalue.worker.GeoUpdate;
import com.realappraiser.gharvalue.worker.OreoLocation;
import com.realappraiser.gharvalue.worker.WorkerManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public abstract class BaseActivity extends AppCompatActivity{

    private final int LOCATION_REQUEST = 12345;
    private String TAG = "BaseActivity";

    private General general;

    private String  address = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "OnCreate");
        setContentView(getLayoutResourceId());

    }

    protected abstract int getLayoutResourceId();



    /*@Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "OnResume "+SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGGED_IN, false));
        if (!SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGGED_IN, false)) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    redirectoLogin();
                }
            }, 500 );
        }
    }*/
    private void redirectoLogin(){
        if (!SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGGED_IN, false)){
            finish();
            Intent intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Ondestroy");
    }

    private void getCurrentLocation(Activity activity) {

        if (general.GPS_status()) {
            try {
                GPSService gpsService = new GPSService(this);
                gpsService.getLocation();
                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        if (general.getcurrent_latitude(activity) != 0) {
                            /*Here store current location of user latLong*/
                            SettingsUtils.Longitudes = general.getcurrent_longitude(activity);
                            SettingsUtils.Latitudes = general.getcurrent_latitude(activity);

                            SettingsUtils.getInstance().putValue("lat", String.valueOf(general.getcurrent_latitude(activity)));
                            SettingsUtils.getInstance().putValue("long",String.valueOf(general.getcurrent_longitude(activity)));

                            sessionLogout(activity);
                        }
                    }
                }, 1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation(this);
                } else {
                    general.customToast("Please enable all permissions to complete access of this application", this);

                   /* new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            general.checkPermissions();
                        }
                    }, 500);*/
                }
        }
    }


    public  void sessionLogout(Activity activity)
    {
        Singleton.getInstance().longitude = 0.0;
        Singleton.getInstance().latitude = 0.0;
        Singleton.getInstance().aCase = new Case();
        Singleton.getInstance().property = new Property();
        Singleton.getInstance().indProperty = new IndProperty();
        Singleton.getInstance().indPropertyValuation = new IndPropertyValuation();
        Singleton.getInstance().indPropertyFloors = new ArrayList<>();
        Singleton.getInstance().proximities = new ArrayList<>();
        Singleton.getInstance().openCaseList.clear();
        Singleton.getInstance().closeCaseList.clear();
        Singleton.getInstance().GetImage_list_flat.clear();
        AppDatabase appDatabase = AppDatabase.getAppDatabase(MyApplication.getAppContext());


        // Total - 16 DB


        // Delete - datamodel
        appDatabase.interfaceDataModelQuery().deleteRow();
        // Delete - offlinedatamodel
        appDatabase.interfaceOfflineDataModelQuery().deleteRow();
        // Delete - casemodal
        appDatabase.interfaceCaseQuery().deleteRow();
        // Delete - propertymodal
        appDatabase.interfacePropertyQuery().deleteRow();
        // Delete - indpropertymodal
        appDatabase.interfaceIndpropertyQuery().deleteRow();
        // Delete - IndPropertyValuationModal
        appDatabase.interfaceIndPropertyValuationQuery().deleteRow();
        // Delete - IndPropertyFloorModal
        appDatabase.interfaceIndPropertyFloorsQuery().deleteRow();
        // Delete - IndPropertyFloorsValuationModal
        appDatabase.interfaceIndPropertyFloorsValuationQuery().deleteRow();
        // Delete - ProximityModal
        appDatabase.interfaceProximityQuery().deleteRow();
        // Delete - GetPhotoModel
        appDatabase.interfaceGetPhotoQuery().deleteRow();
        // Delete - OflineCase
        appDatabase.interfaceOfflineCaseQuery().deleteRow();
        // Delete - Document_list
        appDatabase.interfaceDocumentListQuery().deleteRow();
        // Delete - LatLongDetails
        appDatabase.interfaceLatLongQuery().deleteRow();
        // Delete - typeofproperty
        appDatabase.typeofPropertyQuery().deleteRow();
        // Delete - casedetail
        appDatabase.daoAccess().deleteRow();
        // Delete - propertyupdateroomdb
        appDatabase.propertyUpdateCategory().deleteRow();
        // Delete - GetPhotoMeasurmentQuery
        appDatabase.interfaceGetPhotoMeasurmentQuery().deleteRow();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                address = SettingsUtils.convertLatLngToAddress(activity);
            }
        });

        @SuppressLint("SimpleDateFormat") String time =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(Calendar.getInstance().getTime());
        if (Connectivity.isConnected(this)) {
            JsonRequestData requestData = new JsonRequestData();
            String url = SettingsUtils.getInstance().getValue(SettingsUtils.API_BASE_URL,
                    "") + SettingsUtils.LocationTracker;
            requestData.setUrl(url);
            requestData.setCaseId("");
            requestData.setEmpId(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGIN_ID,""));
            requestData.setLocationType("Logout");
            requestData.setLatitude(SettingsUtils.getInstance().getValue("lat",""));
            requestData.setLongitude(SettingsUtils.getInstance().getValue("long",""));
            requestData.setTrackerTime(time);
            requestData.setActivityType(String.valueOf(4));
            requestData.setComments("");
            requestData.setAgencyBId(SettingsUtils.getInstance().getValue(SettingsUtils.BranchId, ""));
            Log.e("convertLatLngToAddrJson", address);

            if (!address.isEmpty()) {
                requestData.setAddress(address);
            } else {
                address = SettingsUtils.convertLatLngToAddress(this);
                requestData.setAddress(address);
            }

            requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
           requestData.setRequestBody(RequestParam.LocationTracker(requestData));

            Log.e("Location Params", new Gson().toJson(requestData));

            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(BaseActivity.this,
                    requestData, SettingsUtils.POST_TOKEN);
            webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                public void onTaskComplete(JsonRequestData requestData) {
                    String sb = "Location updated sucessfully" +
                            requestData.getResponse();
                    Log.e(TAG, sb);
                    General.hideloading();
                    SettingsUtils.getInstance().putValue("sessionCountDown", "");
                    SettingsUtils.getInstance().putValue(SettingsUtils.KEY_LOGGED_IN, false);

                    redirectLogin();
                }
            });
            webserviceTask.execute();

        } else {
            General.hideloading();
            General.customToastLong("No Internet Connection found",this);
        }







       /* SettingsUtils.getInstance().putValue("api_success",false);

        if(isSuccess){
            if (Build.VERSION.SDK_INT < 26) {
                activity.stopService(new Intent(activity, GeoUpdate.class));
            } else {
                new OreoLocation(activity).stopOreoLocationUpdates();
            }
            new WorkerManager(activity).stopWorker();
            SettingsUtils.getInstance().putValue("api_success",true);

        }*/








        /*if (new LocationTrackerApi(activity).shareLocation("",
                SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGIN_ID, ""),
                "Logout", SettingsUtils.Latitudes, SettingsUtils.Longitudes, "", 4)) {
            if (Build.VERSION.SDK_INT < 26) {
                activity.stopService(new Intent(activity, GeoUpdate.class));
            } else {
                new OreoLocation(activity).stopOreoLocationUpdates();
            }
            new WorkerManager(activity).stopWorker();


            Intent intent = new Intent(activity, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);

           *//* activity.finishAffinity();
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.finish();*//*



            return;
        }

        if (Build.VERSION.SDK_INT < 26) {
            activity.stopService(new Intent(activity, GeoUpdate.class));
        } else {
            new OreoLocation(activity).stopOreoLocationUpdates();
        }
        new WorkerManager(activity).stopWorker();
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);*/

       /* activity.finishAffinity();
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();*/
    }

    private void redirectLogin(){


        if (Build.VERSION.SDK_INT < 26) {
            stopService(new Intent(this, GeoUpdate.class));
        } else {
            new OreoLocation(this).stopOreoLocationUpdates();
        }
        new WorkerManager(this).stopWorker();
            General.hideloading();
            General.customToast("Session Time out",this);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);


    }



}