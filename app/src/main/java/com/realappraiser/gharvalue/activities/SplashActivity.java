package com.realappraiser.gharvalue.activities;

import static com.realappraiser.gharvalue.utils.General.REQUEST_ID_MULTIPLE_PERMISSIONS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.realappraiser.gharvalue.AppDatabase;
import com.realappraiser.gharvalue.BuildConfig;
import com.realappraiser.gharvalue.MyApplication;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.communicator.DataResponse;
import com.realappraiser.gharvalue.communicator.JsonRequestData;
import com.realappraiser.gharvalue.communicator.RequestParam;
import com.realappraiser.gharvalue.communicator.ResponseParser;
import com.realappraiser.gharvalue.communicator.TaskCompleteListener;
import com.realappraiser.gharvalue.communicator.WebserviceCommunicator;
import com.realappraiser.gharvalue.model.Case;
import com.realappraiser.gharvalue.model.IndProperty;
import com.realappraiser.gharvalue.model.IndPropertyValuation;
import com.realappraiser.gharvalue.model.OfflineDataModel;
import com.realappraiser.gharvalue.model.Property;
import com.realappraiser.gharvalue.model.SafeNetModel;
import com.realappraiser.gharvalue.utils.Connectivity;
import com.realappraiser.gharvalue.utils.GPSService;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.Singleton;
import com.realappraiser.gharvalue.utils.security.SafetyNetChecker;
import com.realappraiser.gharvalue.utils.SettingsUtils;
import com.realappraiser.gharvalue.worker.GeoUpdate;
import com.realappraiser.gharvalue.worker.OreoLocation;
import com.realappraiser.gharvalue.worker.WorkerManager;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * Created by kaptas on 14/12/17.
 */

@SuppressWarnings("ALL")
public class SplashActivity extends AppCompatActivity implements OnSuccessListener<SafetyNetApi.AttestationResponse>, OnFailureListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final long SPLASH_TIME = 1000;

    private String  address = "";

    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    private Location mylocation;

    private static final int GPS_ENABLE_REQUEST = 0x1001;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONSS = 0x2;

    private Handler mHandler;
    private Runnable mJumpRunnable;
    private General general;
    private String msg = "", info = "";
    RotateLoading rotateloading;

    // Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    private TextView tvVersion, tvCopyRight;
    private boolean isConnected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO - > For Bug and error report send to api
        General.report_bug(SplashActivity.this);
        setContentView(R.layout.splash_activity);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Splash");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        mFirebaseAnalytics.logEvent("Splash", bundle);
        tvVersion = findViewById(R.id.tvVersion);
        tvCopyRight = findViewById(R.id.tvCopyRight);


        int year = Calendar.getInstance().get(Calendar.YEAR);

        tvCopyRight.setText(getResources().getString(R.string.copyright) + " " + year + ". " + getResources().getString(R.string.copyright1));

        rotateloading = (RotateLoading) findViewById(R.id.rotateloading);
        if (rotateloading != null) {
            rotateloading.start();
        }

        SettingsUtils.init(this);
        general = new General(SplashActivity.this);

        Translucent();

        GPSService gpsService = new GPSService(this);
        gpsService.getLocation();
        setUpGClient();

      /*  if (Connectivity.isConnected(this)) {
            SafetyNetChecker safetyNetChecker = new SafetyNetChecker(this, this, this);
        } else if (General.rootAndEmulatorChecker(SplashActivity.this) == false) {
            splashRunner();
        }*/

        if (General.rootAndEmulatorChecker(SplashActivity.this) == false) {
            splashRunner();
            setVersionDetails();
        }

       /* splashRunner();
        setVersionDetails();*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    private synchronized void setUpGClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public void showLocationDialog(final Context context) {
        Singleton.getInstance().networkListenerAlert = true;

         android.app.AlertDialog alert_show;

        android.app.AlertDialog.Builder alert_build = new android.app.AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
        alert_build.setTitle("Location Information");
        alert_build.setMessage("Please Enable your Location ");
        alert_build.setCancelable(false);
        alert_build.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_ENABLE_REQUEST);
            }
        });
        alert_build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        alert_show = alert_build.create();
        alert_show.show();
    }

    private void splashRunner() {
        mJumpRunnable = new Runnable() {
            public void run() {
                LoggedInAction();
            }
        };
        mHandler = new Handler();
        mHandler.postDelayed(mJumpRunnable, SPLASH_TIME);
    }


    private void setVersionDetails() {
        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String version = pInfo.versionName;
            String vers = "Version: " + version + "    Date: " + BuildConfig.DATE;
            tvVersion.setText(vers);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void Translucent() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    public void LoggedInAction() {
        if (!SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGGED_IN, false)) {
            // Without Login
           // openLogin();
           // navigateLoginScreen();
            removeUser(this);
            //startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        } else {
            // Already Login
            if (Connectivity.isConnected(this)) {
                // Get Drop down API
                getSessionOut(true);
            } else {
                getSessionOut(false);
            }


        }
    }

    /*******
     * GetFieldDropDowns Webservice API call
     * *******/
    private void InitiateGetFieldDropDownTask() {
        String url = general.ApiBaseUrl() + SettingsUtils.GetFieldDropDowns;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(SplashActivity.this,
                requestData, SettingsUtils.GET_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {

                if (requestData.isSuccessful()){
                parseGetDropDownsDataResponse(requestData.getResponse(),
                        requestData.getResponseCode(), requestData.isSuccessful());
                }else{
                    openLogin();
                }
            }
        });
        webserviceTask.execute();
    }

    private void parseGetDropDownsDataResponse(String response, int responseCode, boolean successful) {
        if (successful) {
            SettingsUtils.getInstance().putValue(SettingsUtils.DropDownSave, response);
            DataResponse dataResponse = ResponseParser.parseGetFieldDropDownResponse(response);
            String result = "";
            if (response != null) {
                result = dataResponse.status;
                msg = dataResponse.msg;
                info = dataResponse.info;
            }
            if (result != null) {
                if (result.equals("1")) {
                    // general.CustomToast(msg);
                } else if (result.equals("2")) {
                    general.CustomToast(msg);
                } else if (result.equals("0")) {
                    general.CustomToast(msg);
                }
            } else {
                general.CustomToast(getResources().getString(R.string.serverProblem));
            }
            // Get offline count
            InitiateOfflineCaseCount();
        } else {
            General.customToast(getString(R.string.something_wrong), SplashActivity.this);
        }
    }

    /*******
     * Offline Count limitation Webservice API call
     * *******/
    private void InitiateOfflineCaseCount() {
        String url = general.ApiBaseUrl() + SettingsUtils.OfflineCaseCount;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(SplashActivity.this,
                requestData, SettingsUtils.GET_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {

                if (requestData.isSuccessful()){
                    parseOfflineCaseCountLimit(requestData.getResponse(),
                            requestData.getResponseCode(), requestData.isSuccessful());
                }else {
                   openLogin();
                   // navigateLoginScreen();
                }
            }
        });
        webserviceTask.execute();
    }

    private void openLogin(){
        General.logoutUser(this);
    }

    public  void removeUser(Activity activity){
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
        SettingsUtils.getInstance().putValue(SettingsUtils.KEY_LOGGED_IN, false);
        AppDatabase appDatabase = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        ArrayList<OfflineDataModel> oflineData = (ArrayList) appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(true);
        if (appDatabase == null) {
            return;
        }
        if (oflineData.size() > 0) {
            General.customToast("Please sync your offline documents before logout!", activity);
            return;
        }

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

        activity.finishAffinity();

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
    }

    private void parseOfflineCaseCountLimit(String response, int responseCode, boolean successful) {
        if (!general.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (jsonArray.length() > 2) {
                    // No of cases move to offline
                    String no_offline_case = jsonArray.getJSONObject(1).getString("Value");
                    if (!general.isEmpty(no_offline_case)) {
                        SettingsUtils.getInstance().putValue(SettingsUtils.KEY_OFFLINECASE_COUNT, no_offline_case);
                    } else {
                        SettingsUtils.getInstance().putValue(SettingsUtils.KEY_OFFLINECASE_COUNT, "");
                    }
                    // Offline module enable or not
                    String enable_offline = jsonArray.getJSONObject(2).getString("Value");
                    if (!general.isEmpty(enable_offline)) {
                        if (enable_offline.equalsIgnoreCase("TRUE")) {
                            // enable
                            SettingsUtils.getInstance().putValue(SettingsUtils.KEY_ENABLE_OFFLINE, true);
                        } else {
                            // Hide offline button
                            SettingsUtils.getInstance().putValue(SettingsUtils.KEY_ENABLE_OFFLINE, false);
                        }
                    } else {
                        // Hide offline button
                        SettingsUtils.getInstance().putValue(SettingsUtils.KEY_ENABLE_OFFLINE, false);
                    }

                    // Will get weather the instance is jaipur or kakode
                    String instance_type = jsonArray.getJSONObject(3).getString("Value");
                    if (!general.isEmpty(instance_type)) {
                        if (instance_type.equalsIgnoreCase("1")) {
                            // Jaipur
                            SettingsUtils.getInstance().putValue(SettingsUtils.real_appraiser_jaipur, true);
                        } else {
                            // Kakode
                            SettingsUtils.getInstance().putValue(SettingsUtils.real_appraiser_jaipur, false);
                        }
                    } else {
                        SettingsUtils.getInstance().putValue(SettingsUtils.real_appraiser_jaipur, true);
                    }

                    // check for KEY_INTERNAL_COMPOSTION_NOT_COPY  or not
                    String KEY_INTERNAL_COMPOSTION_NOT_COPY = jsonArray.getJSONObject(4).getString("Value");
                    if (!general.isEmpty(KEY_INTERNAL_COMPOSTION_NOT_COPY)) {
                        if (KEY_INTERNAL_COMPOSTION_NOT_COPY.equalsIgnoreCase("TRUE")) {
                            // enable
                            SettingsUtils.getInstance().putValue(SettingsUtils.KEY_INTERNAL_COMPOSTION_NOT_COPY, true);
                        } else {
                            // Hide
                            SettingsUtils.getInstance().putValue(SettingsUtils.KEY_INTERNAL_COMPOSTION_NOT_COPY, false);
                        }
                    } else {
                        // Hide
                        SettingsUtils.getInstance().putValue(SettingsUtils.KEY_INTERNAL_COMPOSTION_NOT_COPY, false);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getLocationFetch();

            }
        }, 500);

    }

    private void getLocationFetch(){
        if(general.checkLatLong()){
            getSessionOut(false);
           // startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        }else{
            if (general.checkPermissions()) {
                getCurrentLocation(this);
            }else{
                Log.e(TAG,"Permission");
            }
        }
    }


    @Override
    public void onSuccess(@NonNull SafetyNetApi.AttestationResponse attestationResponse) {
        SafeNetModel sNet = SafetyNetChecker.decodeJWS(attestationResponse.getJwsResult());
        if (sNet.getBasicIntegrity() && sNet.getCtsProfileMatch()) {
            splashRunner();
        } else {
            General.exitDialog(SplashActivity.this, "Your phone is rooted,\nPlease unroot to use " +
                    "app");
        }

    }

    @Override
    public void onFailure(@NonNull Exception e) {
        if (e instanceof ApiException) {
            // An error with the Google Play Services API contains some additional details.
            ApiException apiException = (ApiException) e;
            Log.d(TAG, "Error: " +
                    CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()) + ": " +
                    apiException.getStatusMessage());
        } else {
            // A different, unknown type of error occurred.
            Log.d(TAG, "ERROR! " + e.getMessage());
        }
    }

    private void navigateLoginScreen(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        SplashActivity.this.startActivity(intent);
    }


    private  void getCurrentLocation(Activity activity){
        if (general.GPS_status()) {
            try {
                GPSService gpsService = new GPSService(activity);
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
                            SettingsUtils.getInstance().putValue("long", String.valueOf(general.getcurrent_longitude(activity)));
                           // startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                            getSessionOut(false);
                        }
                    }
                }, 1500);
            }catch (Exception e){
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GPS_ENABLE_REQUEST) {
            checkingPermission();
        }
    }

    private void checkingPermission(){
        if(general.checkPermissions()){
            splashRunner();
            setVersionDetails();
        }else{
            showLocationDialog(this);
        }
    }

    private void getMyLocation() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(5 * 3000);
                    locationRequest.setFastestInterval(5 * 3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(mGoogleApiClient, locationRequest, this);
                    /*PendingResult result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(mGoogleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback() {
*/

                    PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(SplashActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(mGoogleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {General.customToast( getResources().getString(R.string.fetching_loca), SplashActivity.this);

                                            }
                                        }, 3000);

                                        status.startResolutionForResult(SplashActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied. However, we have no way to fix the
                                    // settings so we won't show the dialog.
                                    //finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    private void checkPermissionS() {
        int permissionLocation = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONSS);
            }
        } else {
            getMyLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    private void getSessionOut(boolean b){
        if (!SettingsUtils.getInstance().getValue("sessionCountDown", "").isEmpty() && !general.getOfflineCase())
        {
            String VisitTime = SettingsUtils.getInstance().getValue("sessionCountDown", "");
            long currentVisitTime = System.currentTimeMillis();
            long totalVisitTime = currentVisitTime - Long.parseLong(VisitTime);
            long minutes = (totalVisitTime / 1000) / 60;
            Log.e(TAG, "onResume: " + minutes);
            //minutes >= 1
            if (minutes >= 120)
            {
                Log.e(TAG, "onResume: Latitude" + SettingsUtils.Latitudes);
                if (general.checkLatLong()) { //SettingsUtils.Latitudes > 0
                    General.showloading(this);
                    sessionLogout(this);

                } else {
                    if (general.checkPermissions()) {
                        General.showloading(this);
                        getCurrentLocation(this);

                    } else {
                        String str3 = "android.permission.ACCESS_COARSE_LOCATION";
                        String str4 = "android.permission.ACCESS_FINE_LOCATION";
                        if (!SettingsUtils.hasMultiplePermissions(this, str3) || !SettingsUtils.hasMultiplePermissions(this, str4)) {
                            ActivityCompat.requestPermissions(this, new String[]{str3, str4}, REQUEST_ID_MULTIPLE_PERMISSIONS);
                        }
                    }
                }
            }
            else  startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        }else{
            if(b)InitiateGetFieldDropDownTask();
            else  startActivity(new Intent(SplashActivity.this, HomeActivity.class));

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
//            requestData.setAuthToken("Bearer FFcxMfxHjm79qtRcMFNbp4Ydf7l_3jGiLSeSuY2tC3QJmiurkOSfEQGtbN-M6S3kF13VMSM5CALbIJNnT37zMi81gCRCz6YWZD7Usqs9i73kIgJGoHdDsPJdHkWyzD52JuORASt5p-jEB5jN2abX2HXdcIDrZD_YxVHWlFVn4uITc1SA8nk5OPCy5-xmpSq4VrHoUPsRrRMPx411C8gfcJvdaOCTodGRKFVwzVffHRC2cTRi-");
            requestData.setRequestBody(RequestParam.LocationTracker(requestData));

            Log.e("Location Params", new Gson().toJson(requestData));

            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(SplashActivity.this,
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
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.finish();
        startActivity(intent);
        General.customToast("Session Time out",this);


    }
}
