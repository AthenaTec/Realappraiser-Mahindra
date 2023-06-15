package com.realappraiser.gharvalue.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.realappraiser.gharvalue.AppDatabase;
import com.realappraiser.gharvalue.BuildConfig;
import com.realappraiser.gharvalue.MyApplication;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.activities.MultiPhotoSelectActivity;
import com.realappraiser.gharvalue.activities.PhotoLatLngTab;
import com.realappraiser.gharvalue.activities.PlacePickerActivity;
import com.realappraiser.gharvalue.adapter.ImageAdapter;
import com.realappraiser.gharvalue.communicator.DataModel;
import com.realappraiser.gharvalue.communicator.DataResponse;
import com.realappraiser.gharvalue.communicator.JsonRequestData;
import com.realappraiser.gharvalue.communicator.RequestParam;
import com.realappraiser.gharvalue.communicator.ResponseParser;
import com.realappraiser.gharvalue.communicator.TaskCompleteListener;
import com.realappraiser.gharvalue.communicator.WebserviceCommunicator;
import com.realappraiser.gharvalue.model.GetPhoto;
import com.realappraiser.gharvalue.model.ImageBase64;
import com.realappraiser.gharvalue.model.LatLongDetails;
import com.realappraiser.gharvalue.model.NewImage;
import com.realappraiser.gharvalue.model.OfflineDataModel;
import com.realappraiser.gharvalue.model.OflineCase;
import com.realappraiser.gharvalue.utils.Connectivity;
import com.realappraiser.gharvalue.utils.GPSService;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.OfflineLocationInterface;
import com.realappraiser.gharvalue.utils.OfflineLocationReceiver;
import com.realappraiser.gharvalue.utils.SettingsUtils;
import com.realappraiser.gharvalue.utils.Singleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;

/**
 * Created by kaptas on 19/12/17.
 */

@SuppressWarnings("ALL")
public class PhotoLatLong extends Fragment implements
        View.OnClickListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OfflineLocationInterface {

    // GoogleApiClient.ConnectionCallbacks , GoogleApiClient.OnConnectionFailedListener

    // TODO General class to call typeface and all...
    private General general;

    @BindView(R.id.setlatlng)
    RelativeLayout setlatlng;
    @BindView(R.id.locationimage)
    RecyclerView locationimage;
    @BindView(R.id.textview_latlng)
    TextView textview_latlng;
    @BindView(R.id.textview_addimage)
    TextView textview_addimage;
    @BindView(R.id.textview_next)
    TextView textview_next;


    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    private int PLACE_PICKER_REQUEST = 1;
    protected static final String TAG = "PhotoLatLong";


    private double LAT = 0.0;
    private double LNG = 0.0;
    private Location mLastLocation;
    private LatLng latLng;
    private File destination;
    private String realPath = "";
    private List<String> itemList;

    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
    protected static final int REQUEST_CHECK_SETTINGS = 1000;
    private static final int CAMERA_REQUEST = 123;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 4;
    private int PICK_IMAGE_REQUEST = 2;
    public final static int REQUEST_CODE = 1234;
    private final int LOCATION_REQUEST = 12345;
    private static final int GALLERY_REQUEST = 2;
    private final int LOCATION_PICKER = 23456;
    Uri imageUri_final;

    private String latValue = "";
    private String lngValue = "";

    public static String latsValue = "", longsValue = "";
    private JSONArray jsa;
    ArrayList<ImageBase64> image = new ArrayList<>();
    ArrayList<NewImage> newimage = new ArrayList<>();
    private String mBase64 = "";
    SharedPreferences.Editor e;
    SharedPreferences preferences;
    private long current_time_cam_image;
    private boolean gps_condition_check = false;
    private Location mylocation;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONSS = 0x2;


    /* san Inte */
    ArrayList<GetPhoto> GetPhoto_list = new ArrayList<>();
    Recycler_photo_adapter recycler_photo_adapter;
    public static ArrayList<GetPhoto> GetPhoto_list_response = new ArrayList<>();


    boolean isvalid = true;
    String str_latvalue, str_longvalue;
    @SuppressLint("StaticFieldLeak")
    public static EditText latvalue;
    @SuppressLint("StaticFieldLeak")
    public static EditText longvalue;

    private int PropertyId_is = 0;
    AppDatabase appDatabase = null;
    private boolean is_offline = false;
    String my_case_id;
    ArrayList<DataModel> current_case_dataModels;
    int deleted_pic_postion = 0;
    public boolean enable_offline_button;

    @BindView(R.id.textview_error_photo)
    TextView textview_error_photo;

    private OfflineLocationReceiver offlineLocationReceiver;

    private int updatelatLng = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photoandlatlng, container, false);
        ButterKnife.bind(this, view);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("ImageAPI:", "ShowLoagin");
                Log.e(TAG, "show 244");

            }
        }, 0);

        general = new General(getActivity());
        latvalue = (EditText) view.findViewById(R.id.latvalue);
        longvalue = (EditText) view.findViewById(R.id.longvalue);

        // Room - Declared
        if (MyApplication.getAppContext() != null) {
            appDatabase = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        }
        // check the case is offline (or) online
        is_offline = SettingsUtils.getInstance().getValue(SettingsUtils.is_offline, false);
        // CaseID
        my_case_id = SettingsUtils.getInstance().getValue(SettingsUtils.CASE_ID, "");
        // Property
        if (!general.isEmpty(SettingsUtils.getInstance().getValue(SettingsUtils.PropertyId, ""))) {
            PropertyId_is = Integer.parseInt(SettingsUtils.getInstance().getValue(SettingsUtils.PropertyId, ""));
        }

        Log.e(TAG, "onCreateView: ===>" + SettingsUtils.getInstance().getValue(SettingsUtils.KEY_FLAG_URL, ""));

        // check the offline module is present or not
        enable_offline_button = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_ENABLE_OFFLINE, false);

        // Initial the curent_statusID as 0
        Singleton.getInstance().curent_statusID = "0";
        // hit_photo_api init as false
        Singleton.getInstance().hit_photo_api = false;
        // onResume - map_activity_click - set as false as init
        Singleton.getInstance().map_activity_click = false;

        if (!is_offline) {
            // online
            if (Connectivity.isConnected(getActivity())) {
                // Initial the curent_statusID only on online mode
                current_case_dataModels = (ArrayList<DataModel>) appDatabase.interfaceDataModelQuery().getCase_caseID(my_case_id);
                if (current_case_dataModels.size() > 0) {
                    Singleton.getInstance().curent_statusID = current_case_dataModels.get(0).getStatusId();
                    Log.e("curent_statusID_", "curent_statusID: " + Singleton.getInstance().curent_statusID);
                }

                GPSService gpsService = new GPSService(getActivity());
                gpsService.getLocation();
                GetImageAPI();
                setUpGClient();
            } else {
                Connectivity.showNoConnectionDialog(getActivity());
                Log.e(TAG, "Hide 293");
                general.hideloading();
            }
        } else {
            // Room - offline
            String str3 = "android.permission.ACCESS_COARSE_LOCATION";
            String str4 = "android.permission.ACCESS_FINE_LOCATION";
            if (!SettingsUtils.hasMultiplePermissions(getActivity(), str3) || !SettingsUtils.hasMultiplePermissions(getActivity(), str4)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{str3, str4}, LOCATION_REQUEST);
            } else if (general.isLocationEnabled(getActivity())) {
                this.offlineLocationReceiver = new OfflineLocationReceiver(getActivity(), this);
            } else {
                Toast.makeText(getActivity(), "Please enable your location!", 0).show();
            }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (appDatabase.interfaceGetPhotoQuery() != null) {
                        if (appDatabase.interfaceGetPhotoQuery().getPhoto().size() > 0) {
                            if (appDatabase.interfaceGetPhotoQuery().getPhoto_propertyid(PropertyId_is).size() > 0) {
                                ArrayList<GetPhoto> dataModel_open = new ArrayList<>();
                                dataModel_open = (ArrayList<GetPhoto>)
                                        appDatabase.interfaceGetPhotoQuery().getPhoto_propertyid(PropertyId_is);
                                GetPhoto_list_response = new ArrayList<>();
                                GetPhoto_list_response = dataModel_open;
                                Log.e("GetPhoto_list_response",
                                        " :inti_offline "
                                                + GetPhoto_list_response.size());
                                recycler_photo_adapter =
                                        new Recycler_photo_adapter(getActivity(),
                                                GetPhoto_list_response);

                                locationimage.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                                locationimage.setAdapter(recycler_photo_adapter);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e(TAG, "Hide 334");
                                    general.hideloading();
                                }
                            }, 500);
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e(TAG, "Hide 342");
                                    general.hideloading();
                                }
                            }, 500);
                        }
                    }
                }
            }, 1000);
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        textview_latlng.setTypeface(general.mediumtypeface());
        textview_addimage.setTypeface(general.mediumtypeface());
        textview_next.setTypeface(general.mediumtypeface());
        latvalue.setTypeface(general.regulartypeface());
        longvalue.setTypeface(general.regulartypeface());
        textview_next.setOnClickListener(this);


        setlatlng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (is_offline) {

                   /* if (latValue.length() > 0 && !latValue.contains("Latitude")) {
                        latvalue.setText(PhotoLatLong.this.latValue);
                       longvalue.setText(PhotoLatLong.this.lngValue);
                        create_marker(Double.parseDouble(latValue), Double.parseDouble(lngValue));
                    } else if (general.isLocationEnabled(getActivity())) {*/
                    Snackbar.make(setlatlng, getResources().getString(R.string.fetching_loca), -1).show();
                    offlineLocationReceiver = new OfflineLocationReceiver(getActivity(), PhotoLatLong.this);
                    updatelatLng = 1;
                   /* } else {
                        Toast.makeText(PhotoLatLong.this.getActivity(), "Please enable your location!", 0).show();
                    }*/
                } else if (Connectivity.isConnected(getActivity())) {

                    if (!latvalue.getText().toString().isEmpty() && !longvalue.getText().toString().isEmpty()) {
                        startActivityForResult(new Intent(getActivity(), PlacePickerActivity.class)
                                .putExtra("LATLNG", latvalue.getText().toString() + "," + longvalue.getText().toString()), LOCATION_PICKER);
                    } else {
                        startActivityForResult(new Intent(getActivity(), PlacePickerActivity.class), LOCATION_PICKER);
                    }

                    // TODO - Method 1
                   /* PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    // Todo - check whether the lat long textviews has value
                    String latitude = latvalue.getText().toString();
                    String longitude = longvalue.getText().toString();
                    if (General.isEmpty(latitude) || General.isEmpty(longitude)) {
                        // Null
                        Snackbar.make(setlatlng, getResources().getString(R.string.fetching_loca), Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(setlatlng, getResources().getString(R.string.fetching_prev_loca), Snackbar.LENGTH_SHORT).show();
                        // Todo - set latlong bounds for placing marker in saved location in placepicker
                        double d1 = Double.parseDouble(latitude);
                        double d2 = Double.parseDouble(longitude);
                        LatLng latLng = new LatLng(d1, d2);
                        LatLngBounds latLngBounds = new LatLngBounds(latLng, latLng);
                        builder.setLatLngBounds(latLngBounds);
                    }
                    try {
                        startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }*/
                    // TODO - Method 2
                    /*Intent intent = new Intent(getActivity(), Map_activity.class);
                    intent.putExtra("lat_",latvalue.getText().toString());
                    intent.putExtra("long_",longvalue.getText().toString());
                    startActivity(intent);*/
                } else {
                    Connectivity.showNoConnectionDialog(getActivity());
                }
            }
        });


        itemList = new ArrayList<>();
        locationimage.setHasFixedSize(true);
        locationimage.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        locationimage.setItemAnimator(new DefaultItemAnimator());
        locationimage.setNestedScrollingEnabled(false);


        locationimage.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                locationimage, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                if (GetPhoto_list_response.size() < 22) {
                    /*******Check the Memory for image pick******/
                    if (getAvailableMemory()) {
                        Log.e("can pick image :", getAvailableMemory() + "");
                        if (position == 0) {
                            if (checkPermissions())
                                if (GetPhoto_list_response.size() <= 7) {
                                    TakePicture();
                                } else {
                                    general.CustomToast(getResources().getString(R.string.sizelimit));
                                }
                        } else if (position == 1) {
                            if (checkPermissions()) {
                                if (GetPhoto_list_response.size() <= 7) {
                                    Intent gallery_select = new Intent(getActivity(), MultiPhotoSelectActivity.class);
                                    gallery_select.putExtra("available_photo_size",GetPhoto_list_response.size()-2);
                                    gallery_select.putExtra("photolanlat",true);
                                    startActivityForResult(gallery_select, GALLERY_REQUEST);
                                } else {
                                    general.CustomToast(getResources().getString(R.string.sizelimit));
                                }

                            }
                        }
                    } else {
                        general.customToast("Please delete some images in your device, to add new image", getActivity());
                        Log.e("cannot pick image:", getAvailableMemory() + "");
                    }
                } else {
                    if (position == 0 || position == 1)
                        general.CustomToast(getResources().getString(R.string.sizelimit));
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return view;
    }

    /******
     * Check Available Memory for the photo
     * ********/
    private boolean getAvailableMemory() {
        boolean available = false;
        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long maxHeapSizeInMB = runtime.maxMemory() / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;
        final long limitHeapSizeInMB = maxHeapSizeInMB - 8;

        if (usedMemInMB <= limitHeapSizeInMB) {
            available = true;
        } else {
            available = false;
        }
        /*Log.e("usedmemory:", usedMemInMB + "");
        Log.e("totalmemory:", maxHeapSizeInMB + "");
        Log.e("availmemory:", availHeapSizeInMB + "");
        Log.e("limitmemory:", limitHeapSizeInMB + "");*/
        return available;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setLatLngValues();
    }

    @Override
    public void onResume() {
        super.onResume();
        // call map_activity_click > true in Map_activity
        if (Singleton.getInstance().map_activity_click) {
            Singleton.getInstance().map_activity_click = false;
            String map_activity_lat = Singleton.getInstance().map_activity_lat;
            String map_activity_long = Singleton.getInstance().map_activity_long;
            latvalue.setText(map_activity_lat);
            longvalue.setText(map_activity_long);
            /* set Marker */
            if ((!general.isEmpty(map_activity_lat)) && (!general.isEmpty(map_activity_long))) {
                double dob_split_lat = Double.parseDouble(map_activity_lat);
                double dob_split_lng = Double.parseDouble(map_activity_long);
                create_marker(dob_split_lat, dob_split_lng);
            }
        }
    }

    public void create_marker(double Geolocation_lat, double Geolocation_long) {
        // clear the map
        mMap.clear();
        if (is_offline)
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        else
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mMap.setBuildingsEnabled(false);
        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(Geolocation_lat, Geolocation_long))
                .zoom(11)
                .bearing(0)
                .tilt(45)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        try {
            Drawable circleDrawable = getActivity().getDrawable(R.drawable.ic__building_pin_);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Geolocation_lat, Geolocation_long))
                    .icon(General.getMarkerIconFromDrawable(circleDrawable)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void gps_condition() {
        //TODO Method One
        if (general.GPS_status()) {

            try {
                Log.e("EnableGPS_true", "EnableGPS_true");
                GPSService gpsService = new GPSService(getActivity());
                gpsService.getLocation();
                Snackbar.make(setlatlng, getResources().getString(R.string.fetching_loca), Snackbar.LENGTH_SHORT).show();
                Log.e("EnableGPS_latitude", "EnableGPS_latitude is: " + general.getcurrent_latitude(getActivity()));
                Log.e("EnableGPS_longitude", "EnableGPS_longitude is: " + general.getcurrent_longitude(getActivity()));
                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        if (general.getcurrent_latitude(getActivity()) != 0) {
                            latvalue.setText("" + general.getcurrent_latitude(getActivity()));
                            longvalue.setText("" + general.getcurrent_longitude(getActivity()));
                            create_marker(general.getcurrent_latitude(getActivity()), general.getcurrent_longitude(getActivity()));
                        }
                    }
                }, 1500);
            }catch (Exception e){
                e.printStackTrace();
            }



        } else {
            Log.i("EnableGPS_false", "EnableGPS_false");

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }
            buildGoogleApiClient();
            displayLocationSettingsRequest(getActivity());*/

            /*android.app.AlertDialog.Builder mAlertDialog = new android.app.AlertDialog.Builder(getActivity());
            mAlertDialog.setTitle(getResources().getString(R.string.location_enable))
                    .setCancelable(false)
                    .setMessage(getResources().getString(R.string.location_service))
                    .setPositiveButton(getResources().getString(R.string.enable), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Singleton.getInstance().gps_condition_check = true;
                            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);


                        }
                    }).show();*/
        }
        //TODO Method Two
    }

    private void GetImageAPI() {
        Log.e(TAG, "show: 597" );
        general.showloading(getActivity());
        Singleton.getInstance().base64image.clear();
        JsonRequestData data = new JsonRequestData();
        String PropertyId = SettingsUtils.getInstance().getValue(SettingsUtils.PropertyId, "");
        data.setUrl(general.ApiBaseUrl() + SettingsUtils.GETIMAGE + PropertyId);
        data.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(getActivity(), data,
                SettingsUtils.GET_TOKEN);

        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {


            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                Log.e(TAG, "Hide 610");

                if (requestData.isSuccessful()) {
                    parseViewimageResponse(requestData.getResponse());
                    general.hideloading();
                } else if (!requestData.isSuccessful() && (requestData.getResponseCode() == 400 || requestData.getResponseCode() == 401)) {
                    Log.e(TAG, "Hide 611");
                    General.sessionDialog(getActivity());
                } else {
                    Log.e(TAG, "Hide 615");
                    General.customToast(getActivity().getString(R.string.something_wrong),
                            getActivity());
                }
                general.hideloading();
            }
        });
        webserviceTask.execute();

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 10000);
*/
    }

    private void parseViewimageResponse(String response) {
        /* san */
        Singleton.getInstance().GetPhoto_list_final.clear();

        // Room Delete
        //appDatabase.interfaceGetPhotoQuery().deleteRow(PropertyId_is);

        GetPhoto_list_response = new ArrayList<>();
        GetPhoto dummy_cameraImage = new GetPhoto();
        dummy_cameraImage.setDefaultimage(true);
        dummy_cameraImage.setPropertyId(PropertyId_is);
        Gson gson_dummy_camera = new Gson();
        gson_dummy_camera.toJson(dummy_cameraImage);
        GetPhoto_list_response.add(dummy_cameraImage);

        // Room Add
        //appDatabase.interfaceGetPhotoQuery().insertAll(dummy_cameraImage);

        GetPhoto dummy_galleryImage = new GetPhoto();
        dummy_galleryImage.setDefaultimage(true);
        dummy_galleryImage.setPropertyId(PropertyId_is);
        Gson gson_dummy_gallery = new Gson();
        gson_dummy_gallery.toJson(dummy_galleryImage);
        GetPhoto_list_response.add(dummy_galleryImage);

        // Room Add
        //appDatabase.interfaceGetPhotoQuery().insertAll(dummy_galleryImage);


        DataResponse dataResponse = new DataResponse();
        ArrayList<ImageBase64> list = new ArrayList<>();
        try {
            if (response != null) {
                JSONObject object = new JSONObject(response);
                dataResponse.status = object.getString("status");
                if (dataResponse.status.equalsIgnoreCase("1")) {
                    JSONArray jsonArray = object.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        for (int p = 0; p < jsonArray.length(); p++) {
                            JSONObject IndPropertyFloors_jobj = jsonArray.getJSONObject(p);
                            Gson IndPropertyFloorsgson = new Gson();
                            GetPhoto getPhoto = null;
                            getPhoto = new GetPhoto();
                            getPhoto = IndPropertyFloorsgson.fromJson(IndPropertyFloors_jobj.toString(), GetPhoto.class);
                            GetPhoto_list_response.add(getPhoto);

                            // Room Add
                            //appDatabase.interfaceGetPhotoQuery().insertAll(getPhoto);

                        }
                    }
                    Singleton.getInstance().GetPhoto_list_final = GetPhoto_list_response;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("GetPhoto_list_response", "inti_online : " + GetPhoto_list_response.size());
        recycler_photo_adapter = new Recycler_photo_adapter(getActivity(), GetPhoto_list_response);
        locationimage.setAdapter(recycler_photo_adapter);



        if (GetPhoto_list_response.size() <= 2) {
            textview_error_photo.setVisibility(View.VISIBLE);
        } else {
            textview_error_photo.setVisibility(View.GONE);
        }




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // TODO -  call the mandatory_valiadation
                if (Singleton.getInstance().enable_validation_error) {
                    set_mandatory_valiadation();
                    pagerphotolatlng_validation();
                }


                Log.e("ImageAPI :", "Ok");
                Log.e(TAG, "Hide 699");
                general.hideloading();
//                general.hideloading();
            }
        }, 1000);


    }

    private boolean valiadation_lat_pic() {

        isvalid = true;
        str_latvalue = latvalue.getText().toString();
        str_longvalue = longvalue.getText().toString();


        if (!general.isEmpty(str_latvalue)) {
            latvalue.setError(null);
        } else {
            isvalid = false;
            latvalue.setError(getResources().getString(R.string.error_lat));
        }

        if (!general.isEmpty(str_longvalue)) {
            longvalue.setError(null);
        } else {
            isvalid = false;
            longvalue.setError(getResources().getString(R.string.error_long));
        }


        return isvalid;
    }

    private boolean valiadation_lat_pic_nonmandatory() {

        isvalid = true;
        str_latvalue = latvalue.getText().toString();
        str_longvalue = longvalue.getText().toString();


        if (!general.isEmpty(str_latvalue)) {
            latvalue.setError(null);
        } else {
            isvalid = false;
            //latvalue.setError(getResources().getString(R.string.error_lat));
        }

        if (!general.isEmpty(str_longvalue)) {
            longvalue.setError(null);
        } else {
            isvalid = false;
            //longvalue.setError(getResources().getString(R.string.error_long));
        }
        return isvalid;
    }

    private void set_mandatory_valiadation() {
        str_latvalue = latvalue.getText().toString();
        str_longvalue = longvalue.getText().toString();
        if (!general.isEmpty(str_latvalue)) {
            latvalue.setError(null);
            Singleton.getInstance().latlong_valid = false;
        } else {
            latvalue.setError(getResources().getString(R.string.error_lat));
            Singleton.getInstance().latlong_valid = true;
        }
        if (!general.isEmpty(str_longvalue)) {
            longvalue.setError(null);
        } else {
            longvalue.setError(getResources().getString(R.string.error_long));
            Singleton.getInstance().latlong_valid = true;
        }
        if (GetPhoto_list_response.size() <= 2) {
            textview_error_photo.setVisibility(View.VISIBLE);
            Singleton.getInstance().photo_valid = true;
            general.hideloading();
        } else {
            Singleton.getInstance().photo_valid = false;
            general.hideloading();
        }
    }


    private void pagerphotolatlng_validation() {
        if (Singleton.getInstance().latlong_valid) {
            PhotoLatLngTab.pager.setCurrentItem(0);
        } else if (Singleton.getInstance().photo_valid) {
            PhotoLatLngTab.pager.setCurrentItem(0);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    PhotoLatLngTab.pager.setCurrentItem(1);
                }
            }, 100);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textview_next:

                if (valiadation_lat_pic_nonmandatory()) {

                    /* save lat and long */
                    Singleton.getInstance().latlng_details = str_latvalue + ":" + str_longvalue;
                    if (GetPhoto_list_response.size() > 2) {
                        show_emptyFocus();

                        // check offline or not
                        is_offline = SettingsUtils.getInstance().getValue(SettingsUtils.is_offline, false);
                        if (!is_offline) {
                            // online
                            if (Connectivity.isConnected(getActivity())) {
                                textview_next.setVisibility(View.GONE);
                                Log.e(TAG, "show 814");
                                general.showloading(getActivity());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        JSONArray jsonArray = new JSONArray();
                                        for (int x = 0; x < GetPhoto_list_response.size(); x++) {
                                            if (!GetPhoto_list_response.get(x).isDefaultimage()) {
                                                JSONObject jsonObject = new JSONObject();
                                                try {
                                                    jsonObject.put("ID", GetPhoto_list_response.get(x).getId());
                                                    jsonObject.put("PropertyId", GetPhoto_list_response.get(x).getPropertyId());
                                                    if (SettingsUtils.getInstance().getValue(SettingsUtils.KEY_FLAG_URL, "").equals("NEW"))
                                                        jsonObject.put("FilleName", GetPhoto_list_response.get(x).getFileName());
                                                    if (!general.isEmpty(GetPhoto_list_response.get(x).getTitle())) {
                                                        jsonObject.put("Title", GetPhoto_list_response.get(x).getTitle());
                                                    } else {
                                                        jsonObject.put("Title", "");
                                                    }
                                                    jsonObject.put("Logo", GetPhoto_list_response.get(x).getLogo());
                                                } catch (JSONException e1) {
                                                    e1.printStackTrace();
                                                }
                                                jsonArray.put(jsonObject);
                                            }
                                        }
                                        if (jsonArray.length() > 0) {
                                            UploadImage(jsonArray.toString());
                                        } else {
                                            textview_next.setVisibility(View.VISIBLE);
                                            Log.e(TAG, "Hide 843");
                                            general.hideloading();
                                        }
                                        textview_next.setVisibility(View.VISIBLE);
                                    }
                                }, 500);
                            } else {
                                general.hideloading();
                                String curent_statusID = Singleton.getInstance().curent_statusID;
                                Log.e("curent_statusID", "curent_statusID: " + curent_statusID);
                                if (!general.isEmpty(curent_statusID)) {
                                    if (curent_statusID.equalsIgnoreCase("13")) {
                                        // Start Inspection - Data will save in LOCALDB
                                        // TODO - Incase, If thr is no internet on start Inspection - move the case to offline mode
                                        internet_check_box("start_inspec");
                                    } else {
                                        // Internet Required - Edit case
                                        internet_check_box("edit_inspec");
                                        /*textview_next.setVisibility(View.VISIBLE);
                                        general.hideloading();
                                        Connectivity.showNoConnectionDialog(getActivity());*/
                                    }
                                }
                            }
                        } else {
                            // offline
                            // Room - delete lat long
                            appDatabase.interfaceLatLongQuery().deleteRow(Integer.parseInt(my_case_id));
                            LatLongDetails latLongDetails = new LatLongDetails();
                            latLongDetails.setCaseId(Integer.parseInt(my_case_id));
                            latLongDetails.setLatLongDetails(Singleton.getInstance().latlng_details);
                            // Room - add lat long
                            appDatabase.interfaceLatLongQuery().insertAll(latLongDetails);
                            // Photo delete and add
                            textview_next.setVisibility(View.GONE);
                            Log.e(TAG, "show 879");
                            general.showloading(getActivity());
                            save_image_offline("next_btn");
                        }
                    } else {
                        //Toast.makeText(getActivity(), getResources().getString(R.string.error_oneimage), Toast.LENGTH_LONG).show();
                        PhotoLatLngTab.pager.setCurrentItem(1);
                    }
                } else {
                    PhotoLatLngTab.pager.setCurrentItem(1);
                }

                break;
        }
    }


    public void internet_check_box(final String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Network Information");
        builder.setMessage("Please check your Internet connection");

        if (enable_offline_button) {
            // Show offline Button
            builder.setNeutralButton("Save offline", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (str.equalsIgnoreCase("start_inspec")) {
                        // TODO - Start_inspec - Add the offline case to the modal
                        // set as offline
                        SettingsUtils.getInstance().putValue(SettingsUtils.is_offline, true);
                        is_offline = SettingsUtils.getInstance().getValue(SettingsUtils.is_offline, false);
                        // updateOfflineDataModel > offlinecase as true
                        appDatabase.interfaceOfflineDataModelQuery().updateOfflineDataModel("" + Singleton.getInstance().aCase.getCaseId(), true);
                        // update_sync_edit - true
                        appDatabase.interfaceOfflineDataModelQuery().update_sync_edit_DataModel("" + Singleton.getInstance().aCase.getCaseId(), true);


                        if (!general.isEmpty(String.valueOf(Singleton.getInstance().aCase.getCaseId()))) {
                            // offline set the caseID and propertyID
                            OflineCase oflineCase = new OflineCase();
                            oflineCase.setCaseId(Singleton.getInstance().aCase.getCaseId());
                            oflineCase.setPropertyId(Singleton.getInstance().aCase.getPropertyId());
                            // Room Delete - OfflineCase
                            appDatabase.interfaceOfflineCaseQuery().deleteRow(oflineCase.getCaseId());
                            // Room Add - OfflineCase
                            appDatabase.interfaceOfflineCaseQuery().insertAll(oflineCase);
                            if (appDatabase.interfaceCaseQuery().getCase_caseID(Singleton.getInstance().aCase.getCaseId()).size() == 0) {
                                // save Initial case
                                save_initial_details(Singleton.getInstance().aCase.getCaseId());
                            }
                        }
                        // Room - delete lat long
                        appDatabase.interfaceLatLongQuery().deleteRow(Integer.parseInt(my_case_id));
                        LatLongDetails latLongDetails = new LatLongDetails();
                        latLongDetails.setCaseId(Integer.parseInt(my_case_id));
                        latLongDetails.setLatLongDetails(Singleton.getInstance().latlng_details);
                        // Room - add lat long
                        appDatabase.interfaceLatLongQuery().insertAll(latLongDetails);
                        // Photo delete and add
                        textview_next.setVisibility(View.GONE);
                        Log.e(TAG, "show 940");
                        general.showloading(getActivity());
                        save_image_offline("next_btn");
                    } else {
                        // TODO - Edit_inspec - Add the offline case to the modal
                        // set as offline
                        SettingsUtils.getInstance().putValue(SettingsUtils.is_offline, true);
                        is_offline = SettingsUtils.getInstance().getValue(SettingsUtils.is_offline, false);

                        int check_offline_data = appDatabase.interfaceOfflineDataModelQuery().get_case_list("" + Singleton.getInstance().aCase.getCaseId()).size();
                        if (check_offline_data == 0) {
                            ArrayList<DataModel> dataModel_list = (ArrayList<DataModel>) appDatabase.interfaceDataModelQuery().getCase_caseID("" + Singleton.getInstance().aCase.getCaseId());
                            // Get the online modal data
                            DataModel dataModel = dataModel_list.get(0);
                            // Init the offlinecase modal
                            OfflineDataModel offlineDataModel = new OfflineDataModel();
                            offlineDataModel.setCaseId(dataModel.getCaseId());
                            offlineDataModel.setApplicantName(dataModel.getApplicantName());
                            offlineDataModel.setApplicantContactNo(dataModel.getApplicantContactNo());
                            offlineDataModel.setPropertyAddress(dataModel.getPropertyAddress());
                            offlineDataModel.setContactPersonName(dataModel.getContactPersonName());
                            offlineDataModel.setContactPersonNumber(dataModel.getContactPersonNumber());
                            offlineDataModel.setBankName(dataModel.getBankName());
                            offlineDataModel.setBankBranchName(dataModel.getBankBranchName());
                            offlineDataModel.setBankId(dataModel.getBankId());
                            offlineDataModel.setAgencyBranchId(dataModel.getAgencyBranchId());
                            offlineDataModel.setPropertyType(dataModel.getPropertyType());
                            offlineDataModel.setTypeID(dataModel.getTypeID());
                            offlineDataModel.setAssignedAt(dataModel.getAssignedAt());
                            offlineDataModel.setReportChecker(dataModel.getReportChecker());
                            offlineDataModel.setStatus(dataModel.getStatus());
                            offlineDataModel.setReportDispatcher(dataModel.getReportDispatcher());
                            offlineDataModel.setFieldStaff(dataModel.getFieldStaff());
                            offlineDataModel.setAssignedTo(dataModel.getAssignedTo());
                            offlineDataModel.setReportMaker(dataModel.getReportMaker());
                            offlineDataModel.setReportFinalizer(dataModel.getReportFinalizer());
                            offlineDataModel.setPropertyId(dataModel.getPropertyId());
                            offlineDataModel.setReportFile(dataModel.getReportFile());
                            offlineDataModel.setReportId(dataModel.getReportId());
                            offlineDataModel.setReportTemplateId(dataModel.getReportTemplateId());
                            offlineDataModel.setSignature1(dataModel.getSignature1());
                            offlineDataModel.setPropertyCategoryId(dataModel.getPropertyCategoryId());
                            offlineDataModel.setSignature2(dataModel.getSignature2());
                            offlineDataModel.setEmployeeName(dataModel.getEmployeeName());
                            offlineDataModel.setRole(dataModel.getRole());
                            offlineDataModel.setStatusId(dataModel.getStatusId());
                            offlineDataModel.setShowoffline(true);
                            offlineDataModel.setOfflinecase(false);
                            offlineDataModel.setIs_property_changed(false);
                            offlineDataModel.setSync_edit(true);
                            // Insert into offline database
                            appDatabase.interfaceOfflineDataModelQuery().insertAll(offlineDataModel);
                        }

                        // updateOfflineDataModel > offlinecase as true
                        appDatabase.interfaceOfflineDataModelQuery().updateOfflineDataModel("" + Singleton.getInstance().aCase.getCaseId(), true);
                        // update_sync_edit - true
                        appDatabase.interfaceOfflineDataModelQuery().update_sync_edit_DataModel("" + Singleton.getInstance().aCase.getCaseId(), true);

                        if (!general.isEmpty(String.valueOf(Singleton.getInstance().aCase.getCaseId()))) {
                            // offline set the caseID and propertyID
                            OflineCase oflineCase = new OflineCase();
                            oflineCase.setCaseId(Singleton.getInstance().aCase.getCaseId());
                            oflineCase.setPropertyId(Singleton.getInstance().aCase.getPropertyId());
                            // Room Delete - OfflineCase
                            appDatabase.interfaceOfflineCaseQuery().deleteRow(oflineCase.getCaseId());
                            // Room Add - OfflineCase
                            appDatabase.interfaceOfflineCaseQuery().insertAll(oflineCase);
                            if (appDatabase.interfaceCaseQuery().getCase_caseID(Singleton.getInstance().aCase.getCaseId()).size() == 0) {
                                // Editcase - save Initial case
                                editcase_save_initial_details(Singleton.getInstance().aCase.getCaseId());
                            }
                        }
                        // Room - delete lat long
                        appDatabase.interfaceLatLongQuery().deleteRow(Integer.parseInt(my_case_id));
                        LatLongDetails latLongDetails = new LatLongDetails();
                        latLongDetails.setCaseId(Integer.parseInt(my_case_id));
                        latLongDetails.setLatLongDetails(Singleton.getInstance().latlng_details);
                        // Room - add lat long
                        appDatabase.interfaceLatLongQuery().insertAll(latLongDetails);
                        // Photo delete and add
                        textview_next.setVisibility(View.GONE);
                        Log.e(TAG, "show 1022");
                        general.showloading(getActivity());

                        if (str.equalsIgnoreCase("deletephoto_edit_inspec")) {
                            // From delete cross button
                            save_image_offline("deletephoto_btn");
                        } else {
                            // From save button
                            save_image_offline("next_btn");
                        }
                    }
                }
            });
        }


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Settings.ACTION_SETTINGS);
                startActivity(i);
                dialog.dismiss();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void save_initial_details(final int caseid_int) {
        //offline
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //  Case - Room Delete
                appDatabase.interfaceCaseQuery().deleteRow(caseid_int);
                // Case - Room Add
                Singleton.getInstance().aCase.setCaseId(caseid_int);
                appDatabase.interfaceCaseQuery().insertAll(Singleton.getInstance().aCase);

                // Property - Room Delete
                appDatabase.interfacePropertyQuery().deleteRow(caseid_int);
                // Property - Room Add
                Singleton.getInstance().property.setCaseId(caseid_int);
                appDatabase.interfacePropertyQuery().insertAll(Singleton.getInstance().property);

                // IndProperty - Room Delete
                appDatabase.interfaceIndpropertyQuery().deleteRow(caseid_int);
                // IndProperty - Room Add
                Singleton.getInstance().indProperty.setCaseId(caseid_int);
                appDatabase.interfaceIndpropertyQuery().insertAll(Singleton.getInstance().indProperty);

                // IndPropertyValuation - Room Delete
                appDatabase.interfaceIndPropertyValuationQuery().deleteRow(caseid_int);
                // IndPropertyValuation - Room Add
                Singleton.getInstance().indPropertyValuation.setCaseId(caseid_int);
                appDatabase.interfaceIndPropertyValuationQuery().insertAll(Singleton.getInstance().indPropertyValuation);

                // IndPropertyFloor - Room Delete
                appDatabase.interfaceIndPropertyFloorsQuery().deleteRow(caseid_int);
                // IndPropertyFloor - Room Add
                if (Singleton.getInstance().indPropertyFloors.size() > 0) {
                    for (int x = 0; x < Singleton.getInstance().indPropertyFloors.size(); x++) {
                        appDatabase.interfaceIndPropertyFloorsQuery().insertAll(Singleton.getInstance().indPropertyFloors.get(x));
                    }
                }

                // IndPropertyFloorsValuation - Room Delete
                appDatabase.interfaceIndPropertyFloorsValuationQuery().deleteRow(caseid_int);
                // IndPropertyFloorsValuation - Room Add
                if (Singleton.getInstance().indPropertyFloorsValuations.size() > 0) {
                    for (int x = 0; x < Singleton.getInstance().indPropertyFloorsValuations.size(); x++) {
                        appDatabase.interfaceIndPropertyFloorsValuationQuery().insertAll(Singleton.getInstance().indPropertyFloorsValuations.get(x));
                    }
                }

                // IndPropertyFloorsValuation - Room Delete
                appDatabase.interfaceProximityQuery().deleteRow(caseid_int);
                // IndPropertyFloorsValuation - Room Add
                if (Singleton.getInstance().proximities.size() > 0) {
                    for (int x = 0; x < Singleton.getInstance().proximities.size(); x++) {
                        appDatabase.interfaceProximityQuery().insertAll(Singleton.getInstance().proximities.get(x));
                    }
                }

            }
        }, 0);
    }

    private void editcase_save_initial_details(final int caseid_int) {
        //offline
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //  Case - Room Delete
                appDatabase.interfaceCaseQuery().deleteRow(caseid_int);
                // Case - Room Add
                Singleton.getInstance().aCase.setCaseId(caseid_int);
                appDatabase.interfaceCaseQuery().insertAll(Singleton.getInstance().aCase);

                // Property - Room Delete
                appDatabase.interfacePropertyQuery().deleteRow(caseid_int);
                // Property - Room Add
                Singleton.getInstance().property.setCaseId(caseid_int);
                appDatabase.interfacePropertyQuery().insertAll(Singleton.getInstance().property);

                // IndProperty - Room Delete
                appDatabase.interfaceIndpropertyQuery().deleteRow(caseid_int);
                // IndProperty - Room Add
                Singleton.getInstance().indProperty.setCaseId(caseid_int);
                appDatabase.interfaceIndpropertyQuery().insertAll(Singleton.getInstance().indProperty);

                // IndPropertyValuation - Room Delete
                appDatabase.interfaceIndPropertyValuationQuery().deleteRow(caseid_int);
                // IndPropertyValuation - Room Add
                Singleton.getInstance().indPropertyValuation.setCaseId(caseid_int);
                appDatabase.interfaceIndPropertyValuationQuery().insertAll(Singleton.getInstance().indPropertyValuation);

                // IndPropertyFloor - Room Delete
                appDatabase.interfaceIndPropertyFloorsQuery().deleteRow(caseid_int);
                // IndPropertyFloor - Room Add
                if (Singleton.getInstance().indPropertyFloors.size() > 0) {
                    for (int x = 0; x < Singleton.getInstance().indPropertyFloors.size(); x++) {
                        appDatabase.interfaceIndPropertyFloorsQuery().insertAll(Singleton.getInstance().indPropertyFloors.get(x));
                    }
                }

                // IndPropertyFloorsValuation - Room Delete
                appDatabase.interfaceIndPropertyFloorsValuationQuery().deleteRow(caseid_int);
                // IndPropertyFloorsValuation - Room Add
                if (Singleton.getInstance().indPropertyFloorsValuations.size() > 0) {
                    for (int x = 0; x < Singleton.getInstance().indPropertyFloorsValuations.size(); x++) {
                        appDatabase.interfaceIndPropertyFloorsValuationQuery().insertAll(Singleton.getInstance().indPropertyFloorsValuations.get(x));
                    }
                }

                // IndPropertyFloorsValuation - Room Delete
                appDatabase.interfaceProximityQuery().deleteRow(caseid_int);
                // IndPropertyFloorsValuation - Room Add
                if (Singleton.getInstance().proximities.size() > 0) {
                    for (int x = 0; x < Singleton.getInstance().proximities.size(); x++) {
                        /*if (x > 1) {
                                // TODO - set ID as 0 from Thrid row
                            }*/
                        Singleton.getInstance().proximities.get(x).setId(0);
                        appDatabase.interfaceProximityQuery().insertAll(Singleton.getInstance().proximities.get(x));
                    }
                }

            }
        }, 0);
    }


    private void save_image_offline(final String btn_is) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Room Delete
                appDatabase.interfaceGetPhotoQuery().deleteRow(PropertyId_is);
                GetPhoto dummy_cameraImage = new GetPhoto();
                dummy_cameraImage.setDefaultimage(true);
                dummy_cameraImage.setPropertyId(PropertyId_is);
                // Room Add
                appDatabase.interfaceGetPhotoQuery().insertAll(dummy_cameraImage);

                GetPhoto dummy_galleryImage = new GetPhoto();
                dummy_galleryImage.setDefaultimage(true);
                dummy_galleryImage.setPropertyId(PropertyId_is);
                // Room Add
                appDatabase.interfaceGetPhotoQuery().insertAll(dummy_galleryImage);

                JSONArray jsonArray = new JSONArray();
                for (int x = 0; x < GetPhoto_list_response.size(); x++) {
                    if (!GetPhoto_list_response.get(x).isDefaultimage()) {
                        GetPhoto getPhoto = new GetPhoto();
                        getPhoto.setId(0);
                        //getPhoto.setId(GetPhoto_list_response.get(x).getId());
                        getPhoto.setLogo(GetPhoto_list_response.get(x).getLogo());
                        getPhoto.setTitle(GetPhoto_list_response.get(x).getTitle());
                        getPhoto.setPropertyId(GetPhoto_list_response.get(x).getPropertyId());
                        getPhoto.setFileName(GetPhoto_list_response.get(x).getFileName());
                        getPhoto.setDefaultimage(false);
                        getPhoto.setNewimage(false);
                        // Check the Base64Image should be less than 19,80,000 ...
//                        if (GetPhoto_list_response.get(x).getLogo().length() < 1980000) {
                        // Room Add
                        appDatabase.interfaceGetPhotoQuery().insertAll(getPhoto);
//                        }
                    }
                }

                // update the offline caseID
                UpdateOfflineStatusEditcase(my_case_id, "2");
                textview_next.setVisibility(View.VISIBLE);

                // hide loading and show textnext and move to next screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (btn_is.equalsIgnoreCase("next_btn")) {
                            // hit_photo_api > false
                            Singleton.getInstance().hit_photo_api = false;
                            PhotoLatLngTab.pager.setCurrentItem(1);
                        } else if (btn_is.equalsIgnoreCase("deletephoto_btn")) {
                            // From delete cross button

                            // Delete the image uer click the cross image
                            GetPhoto_list_response.remove(deleted_pic_postion);
                            recycler_photo_adapter.setphoto_adapter(GetPhoto_list_response);
                            // call Room - delete and save
                            save_image_offline("");

                            // hit_photo_api > false
                            Singleton.getInstance().hit_photo_api = true;
                            general.CustomToast(getActivity().getResources().getString(R.string.case_moved_offline));
                        }
                        Log.e(TAG, "Hide 1243");
                        general.hideloading();
                    }
                }, 1000);

            }
        }, 100);
    }


    // Todo update Start to Edit Inspecction
    private void UpdateOfflineStatusEditcase(String case_id, String updateCaseStatus) {
        if (appDatabase.interfaceOfflineDataModelQuery().getDataModels().size() > 0) {
            appDatabase.interfaceOfflineDataModelQuery().updateOfflineCaseStatus(updateCaseStatus, case_id);
            // update the case for casemodal
            appDatabase.interfaceCaseQuery().updateCaseStatus(updateCaseStatus, case_id);
        }
    }

    private void UploadImage(String s) {

        String PropertyId = SettingsUtils.getInstance().getValue(SettingsUtils.PropertyId, "");
        if (general.isEmpty(PropertyId)) {
            PropertyId = "0";
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PropertyId", PropertyId);
            jsonObject.put("LatLongDetails", Singleton.getInstance().latlng_details);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        JSONObject edit_synk_obj = new JSONObject();
        try {
            edit_synk_obj.put("PropertyId", PropertyId);

            edit_synk_obj.put("is_sync", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonRequestData data = new JsonRequestData();
        // set Photo
        data.setCompanyName(s);
        // set the lat and long
        data.setContactPersonName(jsonObject.toString());
        // set edit_synk
        data.setApplicantName(edit_synk_obj.toString());

        data.setUrl(general.ApiBaseUrl() + SettingsUtils.IMAGE);
        data.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        data.setRequestBody(RequestParam.uploadimageRequestParams(data));

        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(getActivity(), data,
                SettingsUtils.POST_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                if (requestData.isSuccessful()) {
                    parseuploadimageResponse(requestData.getResponse());
                } else if (!requestData.isSuccessful() && (requestData.getResponseCode() == 400 || requestData.getResponseCode() == 401)) {
                    Log.e(TAG, "Hide 1306");
                    general.hideloading();
                    General.sessionDialog(getActivity());
                } else {
                    Log.e(TAG, "Hide 1310");
                    general.hideloading();
                    General.customToast(getActivity().getString(R.string.something_wrong),
                            getActivity());
                }
            }
        });
        webserviceTask.execute();
    }

    private void parseuploadimageResponse(String response) {

        // Re - Initial the curent_statusID as 0 for move to case to online
        Singleton.getInstance().curent_statusID = "0";
        // hit_photo_api > false
        Singleton.getInstance().hit_photo_api = false;

        if (Singleton.getInstance().aCase.getStatus() == 13) {
            /* Start Inspection - Update the case ID */
            UpdateStatusCaseIdWebservice();
        } else {
            DataResponse dataResponse = ResponseParser.uploadimage(response);
            if (dataResponse.status.equalsIgnoreCase("1")) {
                Toast.makeText(getActivity(), dataResponse.info, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), dataResponse.msg, Toast.LENGTH_LONG).show();
            }
            Log.e(TAG, "Hide 1337");
            general.hideloading();
            PhotoLatLngTab.pager.setCurrentItem(1);
            if (Connectivity.isConnected(getActivity())) {
                GetImageAPI();
            } else {
                Connectivity.showNoConnectionDialog(getActivity());
                Log.e(TAG, "Hide 1344");
                general.hideloading();
            }
        }
    }

    @Override
    public void sendLocationUpdate(double latitude, double longitude) {

        latValue = String.valueOf(latitude);
        lngValue = String.valueOf(longitude);
        if (latitude != 0.0d && updatelatLng == 1) {
            latvalue.setText(String.valueOf(latitude));
            longvalue.setText(String.valueOf(longitude));
            updatelatLng = 0;
            create_marker(latitude, longitude);
        }

    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                //noinspection deprecation
                @SuppressWarnings("deprecation") Place place = PlacePicker.getPlace(data, getActivity());
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                double LAT = place.getLatLng().latitude;
                double LNG = place.getLatLng().longitude;
                Singleton.getInstance().latitude = LAT;
                Singleton.getInstance().longitude = LNG;
                Singleton.getInstance().latlng_details = LAT + ":" + LNG;
                String address = String.format("%s", place.getAddress());
                stBuilder.append(address);
                latvalue.setText(String.valueOf(LAT));
                longvalue.setText(String.valueOf(LNG));

                /* set Marker */
                if ((!general.isEmpty(String.valueOf(LAT))) && (!general.isEmpty(String.valueOf(LNG)))) {
                    LatLng latLng = new LatLng(LAT, LNG);
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomIn());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                }

            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            // Method one
           /* if (data != null) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                String encoded = Base64.encodeToString(getBytesFromBitmap(thumbnail), Base64.DEFAULT);
                convert_base64_image(encoded);
            }*/

            // Method Two
           /* String path_new = Environment.getExternalStorageDirectory() + File.separator + current_time_cam_image + ".jpg";
            File file = new File(path_new);
            Uri imageUri = Uri.fromFile(file);
            imageUri_final = imageUri;

            // TODO Second try -> using baos
            final InputStream imageStream;
            try {
                imageStream = getContext().getContentResolver().openInputStream(imageUri_final);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                byte[] b = baos.toByteArray();
                String encoded = Base64.encodeToString(b, Base64.DEFAULT);
                Log.e("encoded", "encoded_is: " + encoded);
                convert_base64_image(encoded);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/

            // hit_photo_api > true
         /*   Singleton.getInstance().hit_photo_api = true;
            textview_error_photo.setVisibility(View.GONE);

            // Method Three
            String path_new = Environment.getExternalStorageDirectory() + File.separator + current_time_cam_image + ".jpg";

            Log.e(TAG,"PathNew :"+ path_new);

            if (!general.isEmpty(path_new)) {
                String realPath  = null;

                realPath = new File(SettingsUtils.image_destination_path).getPath();
                Log.e(TAG, "realPath==>"+path_new);
                realPath = SiliCompressor.with(getActivity()).compress(path_new,
                        new File(SettingsUtils.image_destination_path));
                convertToBase64(realPath);
            }*/

            try {


                File imgFile = new File(SettingsUtils.mPhotoPath);

                Uri.fromFile(imgFile);
                Singleton.getInstance().hit_photo_api = true;
                textview_error_photo.setVisibility(View.GONE);
                Log.e("PathNew :", SettingsUtils.mPhotoPath);


                try {
                    File compressedImageFile = new Compressor(getActivity()).compressToFile(imgFile);
                    if (!general.isEmpty(SettingsUtils.mPhotoPath)) {


                        Log.e(TAG, "onActivityResult: " + compressedImageFile.getAbsolutePath());

                        convertToBase64(compressedImageFile.getAbsolutePath());

                    /*convertToBase64(SiliCompressor.with(getActivity())
                            .compress(SettingsUtils.mPhotoPath,
                                    new File(SettingsUtils.image_destination_path)));
                    */
                    }

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {

            // hit_photo_api > true
            Singleton.getInstance().hit_photo_api = true;
            textview_error_photo.setVisibility(View.GONE);

            /***** From Image Adapter *****/
            ImageAdapter imageAdapter = MultiPhotoSelectActivity.imageAdapter;
            Log.e(TAG, "show 1538");
            general.showloading(getActivity());
            for (int i = 0; i < imageAdapter.getCheckedItems().size(); i++) {
                //String realPath = imageAdapter.getCheckedItems().get(i);

                Log.e(TAG, "onActivityResult: " + imageAdapter.getCheckedItems().get(i));


                try {

                    File imgFile = new File(imageAdapter.getCheckedItems().get(i));

                    Log.d(TAG, "onActivityResult: " + imgFile.getAbsolutePath());

                    File compressedImageFile = new Compressor(getActivity()).compressToFile(imgFile);
                    convertToBase64(compressedImageFile.getAbsolutePath());

                    /*convertToBase64(SiliCompressor.with(getActivity())
                            .compress(SettingsUtils.mPhotoPath,
                                    new File(SettingsUtils.image_destination_path)));
                    */

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

               /* String realPath = null;
                realPath = SiliCompressor.with(getActivity()).compress(imageAdapter.getCheckedItems().get(i), new File(SettingsUtils.image_destination_path));
                convertToBase64(realPath);*/

                if (i == imageAdapter.getCheckedItems().size() - 1) {
                    Log.e(TAG, "Hide 1564");
                    general.hideloading();
                }
            }
            //noinspection StatementWithEmptyBody
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            }

        } else if (requestCode == REQUEST_CHECK_SETTINGS_GPS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    getMyLocation();
                    break;
                case Activity.RESULT_CANCELED:

                    break;
            }
        } else if (requestCode == LOCATION_PICKER && resultCode == RESULT_OK) {
            String result = data.getStringExtra("LATLNG");
            Log.e(TAG, "onActivityResult: Location==>" + result);

            if (!result.isEmpty() && result.contains(",")) {
                String[] ltlg = result.split(",");
                latvalue.setText(ltlg[0]);
                latValue = ltlg[0];
                longvalue.setText(ltlg[1]);
                longsValue = ltlg[1];
                create_marker(Double.parseDouble(ltlg[0]), Double.parseDouble(ltlg[1]));
            }
        }






        /*if (requestCode == REQUEST_CODE) {
            // if so check once again if we have permission
            if (Build.VERSION.SDK_INT >= 23) {
                if (Settings.canDrawOverlays(getActivity())) {
                    // continue here - permission was granted

                    if (checkPermissions()) {
                        System.err.println("perm granted");
                    }
                }
            }
        }*/

    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public void convert_base64_image(String encode) {
        if (!general.isEmpty(encode)) {
            Log.e("encode_is", "encode_is: " + encode);
            GetPhoto getPhoto_new_image = new GetPhoto();
            getPhoto_new_image.setNewimage(true);
            getPhoto_new_image.setLogo(encode);
            getPhoto_new_image.setId(0);
            // TODO - we want to set property type in session and set it here
            String PropertyId = SettingsUtils.getInstance().getValue(SettingsUtils.PropertyId, "");
            if (!general.isEmpty(PropertyId)) {
                getPhoto_new_image.setPropertyId(Integer.valueOf(PropertyId));
            } else {
                getPhoto_new_image.setPropertyId(0);
            }
            GetPhoto_list_response.add(getPhoto_new_image);
            recycler_photo_adapter.setphoto_adapter(GetPhoto_list_response);
        }
    }


    private String convertToBase64(String imagePath) {
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodedImage = null;
        try {
            bmp = BitmapFactory.decodeFile(imagePath);
            bos = new ByteArrayOutputStream();

            current_time_cam_image = Calendar.getInstance().getTimeInMillis();
            String fileName = "RA_" + current_time_cam_image;

            if (bmp != null) {
                bmp = printLatLong(fileName, bmp);
            }
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);


            bt = bos.toByteArray();
            encodedImage = Base64.encodeToString(bt, Base64.DEFAULT);
            mBase64 = encodedImage;
            if (mBase64 != null) {
                //Log.e("encode_is", "encode_is: " + mBase64);
                GetPhoto getPhoto_new_image = new GetPhoto();
                getPhoto_new_image.setNewimage(true);
                getPhoto_new_image.setLogo(mBase64);
                getPhoto_new_image.setId(0);
                getPhoto_new_image.setFileName(fileName);
                // TODO - we want to set property type in session and set it here

                String PropertyId = SettingsUtils.getInstance().getValue(SettingsUtils.PropertyId, "");
                if (!general.isEmpty(PropertyId)) {
                    getPhoto_new_image.setPropertyId(Integer.valueOf(PropertyId));
                } else {
                    getPhoto_new_image.setPropertyId(0);
                }
                GetPhoto_list_response.add(getPhoto_new_image);
                recycler_photo_adapter.setphoto_adapter(GetPhoto_list_response);
            }


        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return encodedImage;
    }


    public static File getAbsoluteDir(Context ctx, String optionalPath) {
        String rootPath;
        if (optionalPath != null && !optionalPath.equals("")) {
            rootPath = ctx.getExternalFilesDir(optionalPath).getAbsolutePath();
        } else {
            rootPath = ctx.getExternalFilesDir(null).getAbsolutePath();
        }
        // extraPortion is extra part of file path
        String extraPortion = "Android/data/" + BuildConfig.APPLICATION_ID
                + File.separator + "files" + File.separator;
        // Remove extraPortion
        rootPath = rootPath.replace(extraPortion, "");
        return new File(rootPath);
    }

    private Bitmap printLatLong(String fileName, Bitmap toEdit) {
        try {
            Log.e(TAG, "printLatLong: " + latValue + "\n" + longvalue);

//        Bitmap dest = Bitmap.createBitmap(toEdit.getWidth(), toEdit.getHeight(), Bitmap.Config.ARGB_8888);
            Bitmap dest = Bitmap.createBitmap(toEdit.getWidth(), toEdit.getHeight(), toEdit.getConfig());
            Canvas cs = new Canvas(dest);
            cs.drawBitmap(toEdit, 0, 0, null);
            Paint tPaint = new Paint();
            tPaint.setTextSize(20.0f);
            tPaint.setColor(-16776961);
            tPaint.setTextAlign(Paint.Align.CENTER);
            tPaint.setStyle(Paint.Style.FILL);
            float height = tPaint.measureText("yY");


            if (("").equals(latsValue)) {
                latsValue = latvalue.getText().toString();
                longsValue = longvalue.getText().toString();
            }

            String lattLong = "Latt. " + latsValue + "\n" + "Long. " + longsValue;
            cs.drawText(lattLong, (cs.getWidth() / 2),
                    (float) ((cs.getHeight() / 1.5) - ((tPaint.descent() + tPaint.ascent()) / 1.5)),
                    tPaint);

            Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
            StringBuilder sb2 = new StringBuilder();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                sb2.append(Environment.getExternalStorageDirectory());

            } else {
                sb2.append(getContext().getExternalFilesDir(""));
            }
            sb2.append(File.separator);

            sb2.append(fileName);
            sb2.append(".jpg");
            dest.compress(compressFormat, 100, new FileOutputStream(new File(sb2.toString())));
            return dest;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void convert_uri_to_base64(Uri imageUri) {
        //TODO  using baos
        final InputStream imageStream;
        try {
            imageStream = getContext().getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 10, baos);
            byte[] b = baos.toByteArray();

            mBase64 = Base64.encodeToString(b, Base64.DEFAULT);
            if (mBase64 != null) {
                GetPhoto getPhoto_new_image = new GetPhoto();
                getPhoto_new_image.setNewimage(true);
                getPhoto_new_image.setLogo(mBase64);
                getPhoto_new_image.setId(0);
                // TODO - we want to set property type in session and set it here

                String PropertyId = SettingsUtils.getInstance().getValue(SettingsUtils.PropertyId, "");
                if (!general.isEmpty(PropertyId)) {
                    getPhoto_new_image.setPropertyId(Integer.valueOf(PropertyId));
                } else {
                    getPhoto_new_image.setPropertyId(0);
                }
                GetPhoto_list_response.add(getPhoto_new_image);
                recycler_photo_adapter.setphoto_adapter(GetPhoto_list_response);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

            destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

        } else {
            destination = new File(getContext().getExternalFilesDir(""), System.currentTimeMillis() + ".jpg");
        }
        FileOutputStream fo;
        try {
            destination.createNewFile();
            Log.d("path", destination.toString());
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void TakePicture() {
        /* // Method one
         *//*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);*//*
        // Method Two
        current_time_cam_image = System.currentTimeMillis();
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + current_time_cam_image + ".jpg");
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(takePictureIntent, CAMERA_REQUEST);*/

        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
        File photoFile = null;
        try {
            photoFile = SettingsUtils.createImageFile(getActivity());
        } catch (IOException ex) {
            StringBuilder sb = new StringBuilder();
            sb.append("takeCameraPicture:");
            sb.append(ex);
            Log.e(TAG, sb.toString());
        }
        if (photoFile != null) {
            takePictureIntent.putExtra("output", FileProvider.getUriForFile(getActivity(),
                    SettingsUtils.FILE_PROVIDER, photoFile));
            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        }
//        }
    }

    class Recycler_photo_adapter extends RecyclerView.Adapter<Recycler_photo_adapter.ViewHolder> {

        private final FragmentActivity mContext;
        ArrayList<GetPhoto> GetPhoto_list = new ArrayList<>();

        public Recycler_photo_adapter(FragmentActivity activity, ArrayList<GetPhoto> list_) {
            this.mContext = activity;
            this.GetPhoto_list = list_;
        }

        public void setphoto_adapter(ArrayList<GetPhoto> list_is) {
            this.GetPhoto_list = list_is;
            Log.e("GetPhoto_list", "int: " + GetPhoto_list.size());
            notifyDataSetChanged();
        }


        @Override
        public Recycler_photo_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_imageadapter, parent, false);
            Recycler_photo_adapter.ViewHolder viewHolder = new Recycler_photo_adapter.ViewHolder(view);
            return viewHolder;
        }

        @SuppressLint({"RecyclerView", "SetTextI18n"})
        @Override
        public void onBindViewHolder(final Recycler_photo_adapter.ViewHolder holder, final int position) {

            if (position == 0 || position == 1) {
                holder.imagedefaultlayout.setVisibility(View.VISIBLE);
                if (position == 0)
                    holder.addimage.setText("ADD CAMERA IMAGE");
                if (position == 1)
                    holder.addimage.setText("ADD GALLERY IMAGE");
            } else {
                holder.imagedefaultlayout.setVisibility(View.GONE);
                if (GetPhoto_list.size() > 0) {
                    holder.txtvalue.setVisibility(View.VISIBLE);

                    if (!general.isEmpty(GetPhoto_list.get(position).getTitle())) {
                        holder.edit.setText(GetPhoto_list.get(position).getTitle());
                    } else {
                        holder.edit.setText("");
                    }

                    final String getLogoo = GetPhoto_list.get(position).getLogo();
                    if (!general.isEmpty(getLogoo)) {

                        byte[] decodedString = Base64.decode(getLogoo, Base64.DEFAULT);
                        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        decodedByte.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        // set Bitmap into image view
                        if (decodedByte != null) {
//                            Glide.with(getActivity()).load(stream.toByteArray()).asBitmap().centerCrop().into(holder.imageitem);
                            Glide.with(getActivity()).asBitmap().centerCrop().load(stream.toByteArray()).into(holder.imageitem);
                        }
                        // loadBitmap(getLogoo, holder.imageitem);
                    }

                    holder.close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            is_offline = SettingsUtils.getInstance().getValue(SettingsUtils.is_offline, false);
                            if (!is_offline) {
                                // online
                                if (!GetPhoto_list.get(position).isNewimage()) {
                                    /* Old Image */
                                    if (Connectivity.isConnected(getActivity())) {
                                        Log.e(TAG, "show 1945");
                                        general.showloading(getActivity());
                                        JsonRequestData data = new JsonRequestData();
                                        data.setJobID("" + GetPhoto_list.get(position).getId());
                                        data.setPropertyID("" + GetPhoto_list.get(position).getPropertyId());
                                        data.setUrl(general.ApiBaseUrl() + SettingsUtils.DELETEIMAGE);
                                        data.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
                                        data.setRequestBody(RequestParam.deleteimageRequestParams(data));
                                        Log.e(TAG, "onClick: " + new Gson().toJson(data));

                                        WebserviceCommunicator webserviceTask =
                                                new WebserviceCommunicator(mContext, data,
                                                        SettingsUtils.DELETE_TOKEN);
                                        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                                            @Override
                                            public void onTaskComplete(JsonRequestData requestData) {
                                                if (requestData.isSuccessful()) {
                                                    GetPhoto_list.remove(position);
                                                    notifyDataSetChanged();
                                                    parsedeleteimageResponse(requestData.getResponse(), GetPhoto_list);
                                                } else if (!requestData.isSuccessful() && (requestData.getResponseCode() == 400 || requestData.getResponseCode() == 401)) {
                                                    Log.e(TAG, "Hide 1960");
                                                    general.hideloading();
                                                    General.sessionDialog(mContext);
                                                } else {
                                                    GetPhoto_list.remove(position);
                                                    notifyDataSetChanged();
                                                    Log.e(TAG, "Hide 1966");
                                                    general.hideloading();
                                                    General.customToast(mContext.getString(R.string.something_wrong),
                                                            mContext);
                                                }
                                            }
                                        });
                                        webserviceTask.execute();
                                    } else {
                                        deleted_pic_postion = position;
                                        internet_check_box("deletephoto_edit_inspec");
                                        /*Connectivity.showNoConnectionDialog(getActivity());
                                        general.hideloading();*/
                                    }
                                } else {
                                    /* New Image */
                                    Log.e(TAG, "show 1988");
                                    general.showloading(getActivity());
                                    GetPhoto_list.remove(position);
                                    parsedeleteimageResponse(GetPhoto_list);
                                }
                            } else {
                                // offline
                                Log.e(TAG, "show 1995");
                                general.showloading(getActivity());
                                GetPhoto_list.remove(position);
                                offlineparsedeleteimageResponse(GetPhoto_list);
                            }
                        }
                    });

                } else {
                    holder.txtvalue.setVisibility(View.GONE);
                }
            }

        }

        @Override
        public int getItemCount() {
            return GetPhoto_list.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageitem, close;
            private LinearLayout imagedefaultlayout, txtvalue;
            private EditText edit;
            private TextView addimage;

            public ViewHolder(View itemView) {
                super(itemView);
                imageitem = (ImageView) itemView.findViewById(R.id.imageitem);
                imagedefaultlayout = (LinearLayout) itemView.findViewById(R.id.imagedefaultlayout);
                txtvalue = (LinearLayout) itemView.findViewById(R.id.txtvalue);
                edit = (EditText) itemView.findViewById(R.id.text);
                close = (ImageView) itemView.findViewById(R.id.close);
                addimage = (TextView) itemView.findViewById(R.id.addimage);

                edit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence txt, int start, int before, int count) {
                        if (!general.isEmpty(txt.toString())) {
                            if (getAdapterPosition() != 0) {
                                // TODO - save the title directly to GetPhoto_list_response
                                GetPhoto_list_response.get(getAdapterPosition()).setTitle(txt.toString().trim());
                            }
                        } else {
                            GetPhoto_list_response.get(getAdapterPosition()).setTitle("");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        // hit_photo_api > true
                        Singleton.getInstance().hit_photo_api = true;
                    }
                });

            }
        }

    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void parsedeleteimageResponse(String response, ArrayList<GetPhoto> GetPhoto_list) {
        DataResponse dataResponse = ResponseParser.deleteimage(response);
        if (dataResponse.status.equalsIgnoreCase("1")) {
            Toast.makeText(getContext(), dataResponse.info, Toast.LENGTH_LONG).show();
            GetPhoto_list_response = GetPhoto_list;
            recycler_photo_adapter.setphoto_adapter(GetPhoto_list_response);

            showImageErrorText();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "Hide 2078");
                general.hideloading();
            }
        }, 1000);
    }


    private void parsedeleteimageResponse(ArrayList<GetPhoto> GetPhoto_list) {
        GetPhoto_list_response = GetPhoto_list;
        recycler_photo_adapter.setphoto_adapter(GetPhoto_list_response);

        showImageErrorText();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "Hide 2091");
                general.hideloading();
            }
        }, 1000);
    }


    private void offlineparsedeleteimageResponse(ArrayList<GetPhoto> GetPhoto_list) {
        GetPhoto_list_response = GetPhoto_list;
        recycler_photo_adapter.setphoto_adapter(GetPhoto_list_response);
        // call Room - delete and save
        save_image_offline("");
    }


    private void setLatLngValues() {
        Log.e("mydata_lat", "mydata_lat_old");
        if (Singleton.getInstance().property != null) {

            String latlng = "";
            if (!is_offline) {
                // online
                latlng = Singleton.getInstance().property.getLatLongDetails();
            } else {
                // offline
                if (appDatabase.interfaceLatLongQuery().getLatLongDetails_caseID(Integer.parseInt(my_case_id)).size() > 0) {
                    String latLongDetails = appDatabase.interfaceLatLongQuery().getLatLongDetails_caseID(Integer.parseInt(my_case_id)).get(0).getLatLongDetails();
                    if (general.isEmpty(latLongDetails)) {
                        // If empty
                        latlng = "";
                    } else {
                        latlng = latLongDetails;
                    }
                }
            }

            if (!general.isEmpty(latlng)) {
                Singleton.getInstance().latlng_details = latlng;
                if (latlng.contains(":")) {
                    String[] split_latlng = latlng.split(":");
                    if (split_latlng.length > 0) {
                        latvalue.setText(split_latlng[0]);
                        longvalue.setText(split_latlng[1]);
                        /* set Marker */
                        if ((!general.isEmpty(split_latlng[0])) && (!general.isEmpty(split_latlng[1]))) {
                            double dob_split_lat = Double.parseDouble(split_latlng[0]);
                            double dob_split_lng = Double.parseDouble(split_latlng[1]);
                            create_marker(dob_split_lat, dob_split_lng);
                        }
                    }
                }

                if (latlng.equalsIgnoreCase(":")) {
                    gps_condition();
                }

            } else {
                gps_condition();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Singleton.getInstance().base64image.clear();
    }

    // Status Edit inspection Update  API call //
    private void UpdateStatusCaseIdWebservice() {
        String case_id = SettingsUtils.getInstance().getValue(SettingsUtils.CASE_ID, "");
        if (Connectivity.isConnected(getActivity())) {
            InitiateUpdateStatusCaseidTask(case_id, getResources().getString(R.string.edit_inspection));
        } else {
            Connectivity.showNoConnectionDialog(getActivity());
            Log.e(TAG, "Hide 2166");
            general.hideloading();
        }
    }

    private void InitiateUpdateStatusCaseidTask(String case_id, String statusId) {

        String url = general.ApiBaseUrl() + SettingsUtils.UpdateCaseStatusById;
        // String url = SettingsUtils.BASE_URL + SettingsUtils.UpdateCaseStatusById;
        String modifiedby = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGIN_ID, "");
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);
        requestData.setCaseId(case_id);//case_id
        requestData.setModifiedBy(modifiedby);
        requestData.setStatus(statusId);//statusId
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        requestData.setRequestBody(RequestParam.UpdateCaseStatusRequestParams(requestData));

        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(getActivity(),
                requestData, SettingsUtils.PUT_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                if (requestData.isSuccessful()) {
                    parseUpdateStatusCaseidResponse(requestData.getResponse());
                } else if (!requestData.isSuccessful() && (requestData.getResponseCode() == 400 || requestData.getResponseCode() == 401)) {
                    Log.e(TAG, "Hide 2192");
                    general.hideloading();
                    General.sessionDialog(getActivity());
                } else {
                    Log.e(TAG, "Hide 2196");
                    general.hideloading();
                    General.customToast(getActivity().getString(R.string.something_wrong),
                            getActivity());
                }
            }
        });
        webserviceTask.execute();
    }

    private void parseUpdateStatusCaseidResponse(String response) {

        String msg = "", info = "";
        DataResponse dataResponse = ResponseParser.parseUpdateStatusCaseResponse(response);
        String result = "";
        if (response != null) {
            result = dataResponse.status;
            msg = dataResponse.msg;
            info = dataResponse.info;
            Singleton.getInstance().updateCaseStatusModel = dataResponse.updateCaseStatusModel;
        }

        if (result != null) {
            if (result.equals("1")) {
                Log.e(TAG, "Hide 2220");
                general.hideloading();
                PhotoLatLngTab.pager.setCurrentItem(1);
                if (Connectivity.isConnected(getActivity())) {
                    GetImageAPI();
                } else {
                    Connectivity.showNoConnectionDialog(getActivity());
                    Log.e(TAG, "Hide 2227");
                    general.hideloading();
                }
            } else if (result.equals("2")) {
                general.CustomToast(msg);
            } else if (result.equals("0")) {
                general.CustomToast(msg);
            }
        } else {
            general.CustomToast(getActivity().getResources().getString(R.string.serverProblem));
        }
        Log.e(TAG, "Hide 2238");
        general.hideloading();
    }


    /********Load Bitmap for Photo********/
    public void loadBitmap(String resId, ImageView imageView) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(resId);
    }


    /******Bitmap Async task******/

    @SuppressLint("StaticFieldLeak")
    class BitmapWorkerTask extends AsyncTask<String, Void, Uri> {
        private final WeakReference<ImageView> imageViewReference;
        private String data = "";
        private ImageView ImageView;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
            ImageView = imageView;
        }

        // Decode image in background.
        @Override
        protected Uri doInBackground(String... params) {
            data = params[0];
            Uri resultUri = null;

            if (!general.isEmpty(data)) {
                byte[] bt = null;
                Bitmap decodedByte = null;
                try {
                    bt = Base64.decode(data, Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(bt, 0, bt.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (decodedByte != null) {
                    if (MyApplication.getAppContext() != null) {
                        resultUri = getImageUri(MyApplication.getAppContext(), decodedByte);
                    }
                }
            }

            return resultUri;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Uri imageUri) {
            if (imageViewReference != null && imageUri != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    Picasso.get().load(imageUri).placeholder(getActivity().getDrawable(R.drawable.holder)).error(getActivity().getDrawable(R.drawable.holder)).centerCrop().fit().into(imageView);
                }
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        //
        Singleton.getInstance().gps_condition_check = false;

        // Call GoogleApiClient connection when starting the Activity
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        Singleton.getInstance().gps_condition_check = true;
    }

    @Override
    public void onStop() {
        super.onStop();

        // Disconnect GoogleApiClient when stopping Activity
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }


    private synchronized void setUpGClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        checkPermissionS();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Do whatever you need
        //You can display a message here
    }

    private void checkPermissionS() {
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(getActivity(),
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONSS);
            }
        } else {
            getMyLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //You can display a message here
    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        if (mylocation != null) {
            Double latitude = mylocation.getLatitude();
            Double longitude = mylocation.getLongitude();

            if (Singleton.getInstance().gps_condition_check) {
                setLatLngValuesOn(latitude, longitude);
                Singleton.getInstance().gps_condition_check = false;
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private void setLatLngValuesOn(double latitude, double longitude) {
        Log.e("mydata_lat", "mydata_lat_new");
        if (Singleton.getInstance().property != null) {


            String latlng = "";
            if (!is_offline) {
                // online
                latlng = Singleton.getInstance().property.getLatLongDetails();
            } else {
                // offline
                if (appDatabase.interfaceLatLongQuery().getLatLongDetails_caseID(Integer.parseInt(my_case_id)).size() > 0) {
                    String latLongDetails = appDatabase.interfaceLatLongQuery().getLatLongDetails_caseID(Integer.parseInt(my_case_id)).get(0).getLatLongDetails();
                    if (general.isEmpty(latLongDetails)) {
                        // If empty
                        latlng = "";
                    } else {
                        latlng = latLongDetails;
                    }
                }
            }

            if (!general.isEmpty(latlng)) {
                Singleton.getInstance().latlng_details = latlng;
                if (latlng.contains(":")) {
                    String[] split_latlng = latlng.split(":");
                    if (split_latlng.length > 0) {
                    } else {
                        latvalue.setText(latitude + "");
                        longvalue.setText(longitude + "");
                        create_marker(latitude, longitude);
                    }
                }
            } else {

                latvalue.setText(latitude + "");
                longvalue.setText(longitude + "");
                create_marker(latitude, longitude);
            }
        }
    }

    private void getMyLocation() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
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
                                            .checkSelfPermission(getActivity(),
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
                                            public void run() {
                                                Snackbar.make(setlatlng, getResources().getString(R.string.fetching_loca), Snackbar.LENGTH_LONG).show();

                                            }
                                        }, 3000);

                                        status.startResolutionForResult(getActivity(),
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

    public void show_emptyFocus() {
        // Show focus
        if (OtherDetails.my_focuslayout != null) {
            OtherDetails.my_focuslayout.requestFocus();
        }
    }


    private  void showImageErrorText(){
        if(GetPhoto_list_response.size()<=2){
            textview_error_photo.setVisibility(View.VISIBLE);
        }else{
            textview_error_photo.setVisibility(View.GONE);
        }
    }

}