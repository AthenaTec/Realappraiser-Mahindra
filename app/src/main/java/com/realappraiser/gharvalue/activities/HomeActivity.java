package com.realappraiser.gharvalue.activities;

import static com.realappraiser.gharvalue.utils.General.REQUEST_ID_MULTIPLE_PERMISSIONS;
import static com.realappraiser.gharvalue.utils.General.savePopup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.realappraiser.gharvalue.AppDatabase;
import com.realappraiser.gharvalue.BuildConfig;
import com.realappraiser.gharvalue.MyApplication;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.adapter.CloseCaseAdapter;
import com.realappraiser.gharvalue.adapter.ImageAdapter;
import com.realappraiser.gharvalue.adapter.OfflineCaseAdapter;
import com.realappraiser.gharvalue.adapter.OfflineCaseCheckboxAdapter;
import com.realappraiser.gharvalue.adapter.OpenCaseAdapter;
import com.realappraiser.gharvalue.alarm.LogOutScheduler;
import com.realappraiser.gharvalue.communicator.DataModel;
import com.realappraiser.gharvalue.communicator.DataResponse;
import com.realappraiser.gharvalue.communicator.JsonRequestData;
import com.realappraiser.gharvalue.communicator.RequestParam;
import com.realappraiser.gharvalue.communicator.ResponseParser;
import com.realappraiser.gharvalue.communicator.TaskCompleteListener;
import com.realappraiser.gharvalue.communicator.WebserviceCommunicator;
import com.realappraiser.gharvalue.convenyancereport.ConvenyanceReport;
import com.realappraiser.gharvalue.model.BankSelection;
import com.realappraiser.gharvalue.model.CaseSelection;
import com.realappraiser.gharvalue.model.ConfigData;
import com.realappraiser.gharvalue.model.FieldStaff;
import com.realappraiser.gharvalue.model.GetPhoto;
import com.realappraiser.gharvalue.model.ImageBase64;
import com.realappraiser.gharvalue.model.OfflineDataModel;
import com.realappraiser.gharvalue.model.OflineCase;
import com.realappraiser.gharvalue.model.RequestApiStatus;
import com.realappraiser.gharvalue.model.SafeNetModel;
import com.realappraiser.gharvalue.model.SubBranchModel;
import com.realappraiser.gharvalue.model.TypeOfFooting;
import com.realappraiser.gharvalue.model.TypeOfMasonry;
import com.realappraiser.gharvalue.model.TypeOfMortar;
import com.realappraiser.gharvalue.model.TypeOfSteel;
import com.realappraiser.gharvalue.model.UrlModel;
import com.realappraiser.gharvalue.noncaseactivity.NonCaseActivity;
import com.realappraiser.gharvalue.ticketRaiseSystem.adapter.TicketRaiseImageAdapter;
import com.realappraiser.gharvalue.ticketRaiseSystem.model.TicketCreationResponse;
import com.realappraiser.gharvalue.ticketRaiseSystem.model.TicketQueryDataModel;
import com.realappraiser.gharvalue.ticketRaiseSystem.model.TicketRaisePhoto;
import com.realappraiser.gharvalue.ticketRaiseSystem.view.TicketRaisePhotoPickerActivity;
import com.realappraiser.gharvalue.utils.Connectivity;
import com.realappraiser.gharvalue.utils.GPSService;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.GpsUtils;
import com.realappraiser.gharvalue.utils.OfflineLocationInterface;
import com.realappraiser.gharvalue.utils.OfflineLocationReceiver;
import com.realappraiser.gharvalue.utils.SettingsUtils;
import com.realappraiser.gharvalue.utils.Singleton;
import com.realappraiser.gharvalue.utils.security.SafetyNetChecker;
import com.realappraiser.gharvalue.viewtickets.view.ViewRaisedTicketsActivity;
import com.realappraiser.gharvalue.worker.GeoUpdate;
import com.realappraiser.gharvalue.worker.LocationTrackerApi;
import com.realappraiser.gharvalue.worker.OreoLocation;
import com.realappraiser.gharvalue.worker.WorkerManager;
import com.victor.loading.rotate.RotateLoading;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import me.itangqi.waveloadingview.WaveLoadingView;

@SuppressWarnings("ALL")
public class HomeActivity extends BaseActivity implements View.OnClickListener, OpenCaseAdapter.TransferClickListener,
        OfflineLocationInterface, OnFailureListener,
        OnSuccessListener<SafetyNetApi.AttestationResponse> {


    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };

    private String[] androidHigherVersionPermission = new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.CAMERA};

    private General general;

    TicketRaiseImageAdapter ticketRaiseImageAdapter;

    public static ArrayList<GetPhoto> GetPhoto_list_response = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.parentLay)
    LinearLayout parentLay;
    @BindView(R.id.openViewLayout)
    LinearLayout openViewLayout;
    @BindView(R.id.closeViewLayout)
    LinearLayout closeViewLayout;

    @Nullable
    @BindView(R.id.offlineViewLayout)
    LinearLayout offlineViewLayout;

    @BindView(R.id.openLayout)
    LinearLayout openLayout;
    @BindView(R.id.closeLayout)
    LinearLayout closeLayout;
    @BindView(R.id.offlineLayout)
    LinearLayout offlineLayout;
    @BindView(R.id.opencutLay)
    LinearLayout opencutLay;
    @BindView(R.id.closecutLay)
    LinearLayout closecutLay;
    @BindView(R.id.offlinecutLay)
    LinearLayout offlinecutLay;
    /*  @BindView(R.id.GoOfflineLayout)
      LinearLayout GoOfflineLayout;*/
    @BindView(R.id.open_cut_image)
    ImageView open_cut_image;
    @BindView(R.id.close_cut_image)
    ImageView close_cut_image;
    @BindView(R.id.offline_cut_image)
    ImageView offline_cut_image;
    @BindView(R.id.openrecyclerview)
    RecyclerView openrecyclerview;
    @BindView(R.id.closerecyclerview)
    RecyclerView closerecyclerview;
    @BindView(R.id.offlinerecyclerview)
    RecyclerView offlinerecyclerview;
    @SuppressLint("StaticFieldLeak")
    public static TextView opened_case;
    @SuppressLint("StaticFieldLeak")
    public static TextView closed_case;
    @SuppressLint("StaticFieldLeak")
    public static TextView offline_case;
    String welcome_name = "";

    private String subBranchName = "";
    private int subBranchNameId = -1;

    private String spReportMakerName = "";
    private int spReportMakerId = -1;

    private String spCaseAdminName = "";
    private int spCaseAdminId = -1;

    private int reportMakerAgentId = -1;

    private int caseAdminAgentId = -1;

    private double latitude = 0.0;
    private double longitude = 0.0;

    ArrayList<DataModel> dataModels = new ArrayList<>();
    ArrayList<DataModel> openCaseDataModels = new ArrayList<>();
    ArrayList<DataModel> closeCaseDataModels = new ArrayList<>();
    ArrayList<OfflineDataModel> offlineCaseDataModels = new ArrayList<>();
    ArrayList<SubBranchModel.Datum> subBranchList = new ArrayList<>();
    DataModel updateCaseStatusModel = new DataModel();


    ArrayList<BankSelection.Datum> caseSelectionsBankName;
    ArrayList<CaseSelection.Data.CaseAdmin> caseSelectionsCaseAdmin;
    ArrayList<CaseSelection.Data.PropertyType> caseSelectionsProperty;
    ArrayList<CaseSelection.Data.ReportMaker> caseSelectionsReportMaker;

    private String msg = "", info = "";
    private String empId = "", startDate = "", endDate = "";

    // TODO for back press
    boolean doubleBackToExitPressedOnce = false;
    // Offline Cases
    public ArrayList<DataModel> Inter_allcase_list = new ArrayList<>();
    public AppDatabase appDatabase;
    public String offlinecase_count;
    public boolean enable_offline_button;
    /*Dialog dialog;*/

    ArrayList<OflineCase> oflineCases_list = new ArrayList<>();
    float percentage_level = 0;
    float percentage_level_total = 0;
    float percentage_level_half = 0;
    int start_ofline_case = 0;
    public Dialog connectionDialog_circle;
    WaveLoadingView mWaveLoadingView;

    public static ArrayList<GetPhoto> createPhotoList = new ArrayList<>();

    @BindView(R.id.no_data_found_open)
    TextView no_data_found_open;
    @BindView(R.id.no_data_found_close)
    TextView no_data_found_close;
    @BindView(R.id.no_data_found_offline)
    TextView no_data_found_offline;

    @BindView(R.id.parentLayout)
    LinearLayout parentLayout;

    @BindView(R.id.shimmer_home_view)
    ShimmerFrameLayout shimmerHomeView;

    RotateLoading rotateloading;

    // Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    boolean real_appraiser_jaipur = false;

    private static final String TAG = "HomeActivity";
    private int propertyId, bankId, caseAdminId, reportMakerId;
    private WorkerManager workManager;
    private boolean isGPS = false;
    private LocationTrackerApi locationTrackerApi;
    private OreoLocation oreoLocation;
    private OfflineLocationReceiver offlineLocationReceiver;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private int querySpinnerPosition = -1;
    private final int GALLERY_REQUEST = 2;
    private final int CAMERA_REQUEST = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO - > For Bug and error report send to api
        General.report_bug(HomeActivity.this);
        //setContentView(R.layout.home_screen);
        ButterKnife.bind(this);
        SettingsUtils.init(this);
        real_appraiser_jaipur = SettingsUtils.getInstance().getValue(SettingsUtils.real_appraiser_jaipur, false);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        if (real_appraiser_jaipur) {
            // Jaipur
            // Obtain the FirebaseAnalytics instance.
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "3");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "OpenCase");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            mFirebaseAnalytics.logEvent("OpenCase", bundle);
        } else {
            // Kakode
            // Obtain the FirebaseAnalytics instance.
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "3");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "OpenCase_kakode");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            mFirebaseAnalytics.logEvent("OpenCase_kakode", bundle);
        }


        general = new General(HomeActivity.this);
        Singleton.getInstance().latlong_valid = false;
        Singleton.getInstance().photo_valid = false;

        String firstname = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_FIRSTNAME, "");
        String lastname = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LASTNAME, "");
        if ((!general.isEmpty(firstname)) && (!general.isEmpty(lastname))) {
            welcome_name = getResources().getString(R.string.hi) + " " + firstname + " " + lastname;
        } else if ((!general.isEmpty(firstname))) {
            welcome_name = getResources().getString(R.string.hi) + " " + firstname;
        } else if ((!general.isEmpty(lastname))) {
            welcome_name = getResources().getString(R.string.hi) + " " + lastname;
        } else {
            welcome_name = getResources().getString(R.string.title);
        }

        initToolbar();

        // Room - Get the Open case adapter
        appDatabase = AppDatabase.getAppDatabase(HomeActivity.this);
        offlinecase_count = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_OFFLINECASE_COUNT, "");
        enable_offline_button = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_ENABLE_OFFLINE, false);


        /*if (Connectivity.isConnected(this)) {
            SafetyNetChecker safetyNetChecker = new SafetyNetChecker(this, this, this);
        } else if (General.rootAndEmulatorChecker(HomeActivity.this) == false) {
            initiateViewsAndData();
        }*/

        //initiateViewsAndData();
        if (General.rootAndEmulatorChecker(HomeActivity.this) == false) {
            initiateViewsAndData();
        }
    }

    private void initiateViewsAndData() {

        InitialLoadOpenCase();
        InitialLoadClosedCase();

        if (enable_offline_button) {
            // Show offline Button
            offlineLayout.setVisibility(View.VISIBLE);
            offlineViewLayout.setVisibility(View.VISIBLE);
//            GoOfflineLayout.setVisibility(View.VISIBLE);
            check_offline_button_function();
        } else {
            // Hide offline Button
            offlineLayout.setVisibility(View.GONE);
            offlineViewLayout.setVisibility(View.GONE);
//            GoOfflineLayout.setVisibility(View.GONE);
        }

        if (!general.isEmpty(Singleton.getInstance().call_offline_tab)) {
            Singleton.getInstance().call_offline_tab = "";
            OfflineLayoutClick();
        } else {
            closeViewLayout.setVisibility(View.GONE);
            offlineViewLayout.setVisibility(View.GONE);
        }

//        settingAndFireRemote();
        getFlagSettings();
    }


    private void settingAndFireRemote() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(5).build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

       /* mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            String json_app = mFirebaseRemoteConfig.getString(SettingsUtils.APP_DATA);
                            Log.e(TAG, "onComplete: " + json_app);
                            ConfigData configData = new Gson().fromJson(json_app, ConfigData.class);
                            try {
                                String email = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_EMAIL, "");

                                if (configData.getUser() == null || configData.getUser().isEmpty() || configData.getUser().equals("") ||
                                        configData.getUser().equals("null") || configData.getUser().equals(email)) {
                                    if (general.checkPermissions())
                                        checkUpdate(configData);
                                }
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });*/
    }

    private void checkUpdate(ConfigData configData) throws PackageManager.NameNotFoundException {
//        ApkDownloader apkDownloader = new ApkDownloader();
//        PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
//        String version = pInfo.versionName;
//        if (!configData.getVersion().equals(version)) {
//            File file = new File(Environment.getExternalStorageDirectory() + "/RealAppraiser/" + configData.getFileName());
//            Log.e(TAG, "checkUpdate: " + file.getAbsolutePath());
//            if (!file.exists()) {
//                AsyncTask.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        apkDownloader.Download(MyApplication.getAppContext(), configData.getUrl(), configData.getFileName());
//                    }
//                });
//
//            } else {
//                showUpdateDialog(file, configData.getVersion());
//            }
//        }
    }

//    private void showUpdateDialog(File file, String newVersion) throws PackageManager.NameNotFoundException {
//        PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
//        String version = pInfo.versionName;
//
//        AlertDialog.Builder alert_build = new AlertDialog.Builder(this);
//
//        alert_build.setTitle("Update Available!");
//        alert_build.setMessage("You have older version(" + version + ").\nPlease update to new version(" + newVersion + ") which contains lot new features.");
//        alert_build.setCancelable(false);
//        alert_build.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                openInstall(file);
//                dialog.dismiss();
//            }
//        });
//        AlertDialog alert_show = alert_build.create();
//        alert_show.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//                alert_show.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorPrimaryDark));
//
//            }
//        });
//        alert_show.show();
//    }

//    private void openInstall(File file) {
//        Uri uri = FileProvider.getUriForFile(this, "com.realappraiser.gharvalue.fileprovider", file);
//        Log.e(TAG, "checkUpdate: install update =>" + uri);
//        Intent promptInstall = new Intent(Intent.ACTION_VIEW);
//        promptInstall.setDataAndType(SettingsUtils.checkAndReturnUri(this, file), "application/vnd.android" + ".package-archive");
//        promptInstall.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
//        promptInstall.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        promptInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        getApplicationContext().startActivity(promptInstall);
//    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(welcome_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        opened_case = (TextView) findViewById(R.id.opened_case);
        closed_case = (TextView) findViewById(R.id.closed_case);
        offline_case = (TextView) findViewById(R.id.offline_case);
        openrecyclerview.setHasFixedSize(true);
        offlinerecyclerview.setHasFixedSize(true);

       /* GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        openrecyclerview.setLayoutManager(manager);*/

        /*GridLayoutManager manager2 = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        offlinerecyclerview.setLayoutManager(manager2);*/
        rotateloading = (RotateLoading) findViewById(R.id.rotateloading);

        openLayout.setOnClickListener(this);
        closeLayout.setOnClickListener(this);
        offlineLayout.setOnClickListener(this);

        locationTrackerApi = new LocationTrackerApi(this);
        oreoLocation = new OreoLocation(this);

        if (general.checkPermissions() && checkGps()) {
            makeLocationUpadte();
        }


//        GoOfflineLayout.setOnClickListener(this);
    }

    private void makeLocationUpadte() {
        if (Build.VERSION.SDK_INT < 26) {
            startService(new Intent(MyApplication.getAppContext(), GeoUpdate.class));
        } else {
            oreoLocation.startLocationUpdate();
        }
        workManager = new WorkerManager(MyApplication.getAppContext());
        offlineLocationReceiver = new OfflineLocationReceiver(this, this);
    }

    private boolean checkGps() {
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            public final void gpsStatus(boolean isGPSEnable) {
                isGPS = isGPSEnable;
            }
        });
        return isGPS;
    }


    private void InitialLoadOpenCase() {
        if (Singleton.getInstance().openCaseList != null) {
            if (Singleton.getInstance().openCaseList.size() > 0) {

                shimmerHomeView.stopShimmer();
                shimmerHomeView.setVisibility(View.GONE);
                parentLayout.setVisibility(View.VISIBLE);
                loadOpenCaseAdapter(Singleton.getInstance().openCaseList);


//                setEnabledColorOffline();
            } else {
                OpenCloseAPIWebserviceCall();
            }
        } else {
            OpenCloseAPIWebserviceCall();
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadOpenCaseAdapter(ArrayList<DataModel> dataModel) {
        OpenCaseAdapter openCaseAdapter = new OpenCaseAdapter(HomeActivity.this, dataModel, this);
        openrecyclerview.setAdapter(openCaseAdapter);
        openrecyclerview.setItemAnimator(new DefaultItemAnimator());
        openrecyclerview.setNestedScrollingEnabled(false);
        openCaseAdapter.notifyDataSetChanged();
        setRecyclerViewClickListner(openrecyclerview);
        opened_case.setText(getResources().getString(R.string.open) + " (" + dataModel.size() + ")");

       /* if(dataModel.size() == 0){
            no_data_found_open.setVisibility(View.VISIBLE);
            openViewLayout.setVisibility(View.GONE);
        }else {
            openViewLayout.setVisibility(View.VISIBLE);
            no_data_found_open.setVisibility(View.GONE);
        }*/


    }

    private void InitialLoadClosedCase() {
        if (Singleton.getInstance().closeCaseList != null) {
            if (Singleton.getInstance().closeCaseList.size() > 0) {
                loadCloseCaseAdapter(Singleton.getInstance().closeCaseList);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadCloseCaseAdapter(ArrayList<DataModel> dataModel) {
        closerecyclerview.setHasFixedSize(true);
        closerecyclerview.setLayoutManager(new LinearLayoutManager(this));
        CloseCaseAdapter caseAdapter = new CloseCaseAdapter(this, dataModel);
        closerecyclerview.setAdapter(caseAdapter);
        closerecyclerview.setItemAnimator(new DefaultItemAnimator());
        closerecyclerview.setNestedScrollingEnabled(false);
        closed_case.setText(getResources().getString(R.string.close) + " (" + dataModel.size() + ")");
    }

    @SuppressLint("SetTextI18n")
    private void loadOfflineCaseAdapter(ArrayList<OfflineDataModel> dataModel) {
        offlinerecyclerview.setHasFixedSize(true);
        OfflineCaseAdapter caseAdapter = new OfflineCaseAdapter(this, dataModel);
        offlinerecyclerview.setAdapter(caseAdapter);
        offlinerecyclerview.setItemAnimator(new DefaultItemAnimator());
        offlinerecyclerview.setNestedScrollingEnabled(false);
        offline_case.setText(getResources().getString(R.string.offline) + " (" + dataModel.size() + ")");
    }

    private void setRecyclerViewClickListner(RecyclerView recyclerView) {
        final GestureDetector gestureDetector = new GestureDetector(HomeActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, final MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildLayoutPosition(child);

                    //general.CustomToast(position+"");

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    private void setRecyclerViewCheckboxClickListner(RecyclerView recyclerView) {
        final GestureDetector gestureDetector = new GestureDetector(HomeActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, final MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildLayoutPosition(child);

                    //   general.CustomToast(position+"");

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    public void IncludeInitialOfflineCases() {
        SharedPreferences preferences = getSharedPreferences("SelectOfflineCase", 0);
        String offline = preferences.getString("checkboxitem", "");

        Singleton.getInstance().offlineCaseList.clear();
        Singleton.getInstance().offlineCaseListOriginal.clear();
        Singleton.getInstance().samplesList.clear();
        Singleton.getInstance().AddSelectValue.clear();
        Singleton.getInstance().mCheckPosition.clear();
        Singleton.getInstance().openCaseList = Singleton.getInstance().openCaseListOriginal;

        Singleton.getInstance().samplesList = new ArrayList(Arrays.asList(offline.split("\\|")));
        /*for (int i = 0; i < Singleton.getInstance().samplesList.size(); i++) {
            System.out.println(" -->" + Singleton.getInstance().samplesList.get(i));
        }

        for (int position = 0; position < Singleton.getInstance().samplesList.size(); position++) {
            *//*Singleton.getInstance().mCheckPosition.add(position);*//*
         */
        if (appDatabase.interfaceOfflineDataModelQuery().getDataModels().size() > 0) {

            if (MyApplication.getAppContext() != null) {
                AppDatabase appDatabase = AppDatabase.getAppDatabase(MyApplication.getAppContext());
                ArrayList<OfflineDataModel> dataModel_offline = new ArrayList<>();
                ArrayList<OfflineDataModel> dataModel_offline_tab = new ArrayList<>();
                dataModel_offline = (ArrayList<OfflineDataModel>) appDatabase.interfaceOfflineDataModelQuery().getDataModels();
                dataModel_offline_tab = (ArrayList<OfflineDataModel>) appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(true);

                Singleton.getInstance().offlineCaseList = dataModel_offline;
                Singleton.getInstance().offlineCaseListOriginal = dataModel_offline_tab;

                for (int i = 0; i < Singleton.getInstance().offlineCaseListOriginal.size(); i++) {
                    String caseID = Singleton.getInstance().offlineCaseListOriginal.get(i).getCaseId();
                    for (int j = 0; j < Singleton.getInstance().openCaseList.size(); j++) {
                        String openCaseId = Singleton.getInstance().openCaseList.get(j).getCaseId();
                        if (openCaseId.equalsIgnoreCase(caseID)) {
                            Singleton.getInstance().openCaseList.remove(j);
                        }
                    }
                }

                Collections.sort(Singleton.getInstance().offlineCaseListOriginal, new Comparator<OfflineDataModel>() {
                    public int compare(OfflineDataModel obj1, OfflineDataModel obj2) {
                        // ## Descending order
                        return obj2.getCaseId().compareToIgnoreCase(obj1.getCaseId()); // To compare string values
                    }
                });

                loadOfflineCaseAdapter(Singleton.getInstance().offlineCaseListOriginal);
                loadOpenCaseAdapter(Singleton.getInstance().openCaseList);

                check_offline_button_function();
            }
        }
    }

    public void IncludeInitialSavedOfflineCase() {
        SharedPreferences preferences = getSharedPreferences("SelectOfflineCase", 0);
        String offline = preferences.getString("checkboxitem", "");

        Singleton.getInstance().offlineCaseList.clear();
        Singleton.getInstance().offlineCaseListOriginal.clear();
        Singleton.getInstance().samplesList.clear();
        Singleton.getInstance().AddSelectValue.clear();
        Singleton.getInstance().mCheckPosition.clear();

        Singleton.getInstance().samplesList = new ArrayList(Arrays.asList(offline.split("\\|")));
        for (int i = 0; i < Singleton.getInstance().samplesList.size(); i++) {
            System.out.println(" -->" + Singleton.getInstance().samplesList.get(i));
        }

        for (int position = 0; position < Singleton.getInstance().samplesList.size(); position++) {

            if (appDatabase.interfaceOfflineDataModelQuery().getDataModels().size() > 0) {
                if (MyApplication.getAppContext() != null) {
                    AppDatabase appDatabase = AppDatabase.getAppDatabase(MyApplication.getAppContext());
                    ArrayList<OfflineDataModel> dataModel_offline = new ArrayList<>();
                    dataModel_offline = (ArrayList<OfflineDataModel>) appDatabase.interfaceOfflineDataModelQuery().getDataModels();

                    String caseID = Singleton.getInstance().samplesList.get(position);
                    Singleton.getInstance().offlineCaseList = dataModel_offline;

                    for (int i = 0; i < dataModel_offline.size(); i++) {

                        String offlinecaseid = dataModel_offline.get(i).getCaseId();
                        if (caseID.equalsIgnoreCase(offlinecaseid)) {
                            Singleton.getInstance().offlineCaseList.get(i).setOfflinecase(true);
                            //long value = appDatabase.interfaceOfflineDataModelQuery().updateOfflineDataModel(dataModel_offline.get(i).getCaseId(), true);
                            Singleton.getInstance().offlineCaseListOriginal.add(Singleton.getInstance().offlineCaseList.get(i));

                            // Singleton.getInstance().mCheckPosition.add(i);
                            //  Log.e("update", value + "");
                        }
                    }
                    loadOfflineCaseAdapter(Singleton.getInstance().offlineCaseListOriginal);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.openLayout:

                if (real_appraiser_jaipur) {
                    // Jaipur
                    // Obtain the FirebaseAnalytics instance.
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "3");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "OpenCase");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                    //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    mFirebaseAnalytics.logEvent("OpenCase", bundle);
                } else {
                    // Kakode
                    // Obtain the FirebaseAnalytics instance.
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "3");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "OpenCase_kakode");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                    //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    mFirebaseAnalytics.logEvent("OpenCase_kakode", bundle);
                }

                OpenLayoutClick();
                break;

            case R.id.closeLayout:

                if (real_appraiser_jaipur) {
                    // Jaipur
                    Bundle bundle1 = new Bundle();
                    bundle1.putString(FirebaseAnalytics.Param.ITEM_ID, "4");
                    bundle1.putString(FirebaseAnalytics.Param.ITEM_NAME, "ClosedCase");
                    bundle1.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                    //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle1);
                    mFirebaseAnalytics.logEvent("ClosedCase", bundle1);
                } else {
                    // Kakode
                    Bundle bundle1 = new Bundle();
                    bundle1.putString(FirebaseAnalytics.Param.ITEM_ID, "4");
                    bundle1.putString(FirebaseAnalytics.Param.ITEM_NAME, "ClosedCase_kakode");
                    bundle1.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                    //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle1);
                    mFirebaseAnalytics.logEvent("ClosedCase_kakode", bundle1);
                }

                ClosedLayoutClick();
                break;

            case R.id.offlineLayout:

                if (real_appraiser_jaipur) {
                    // Jaipur
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(FirebaseAnalytics.Param.ITEM_ID, "5");
                    bundle2.putString(FirebaseAnalytics.Param.ITEM_NAME, "OfflineCase");
                    bundle2.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                    //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle2);
                    mFirebaseAnalytics.logEvent("OfflineCase", bundle2);
                } else {
                    // Kakode
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(FirebaseAnalytics.Param.ITEM_ID, "5");
                    bundle2.putString(FirebaseAnalytics.Param.ITEM_NAME, "OfflineCase_kakode");
                    bundle2.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                    //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle2);
                    mFirebaseAnalytics.logEvent("OfflineCase_kakode", bundle2);
                }

                OfflineLayoutClick();
                break;

           /* case R.id.GoOfflineLayout:
                if (Connectivity.isConnected(HomeActivity.this)) {

                    // Show and hide the loading
                    if (rotateloading != null) {
                        rotateloading.start();
                    }
                    GoOfflineLayout.setVisibility(View.GONE);

                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            if (rotateloading != null) {
                                rotateloading.stop();
                            }
                            GoOfflineLayout.setVisibility(View.VISIBLE);
                        }
                    }, 2000);


                    int offlineapicount = 0, offlineadaptercount = 0;
                    if (!general.isEmpty(offlinecase_count)) {
                        offlineapicount = Integer.parseInt(offlinecase_count);
                        offlineadaptercount = offlineDataModelAdaptercount();
                    }

                    if (offlineadaptercount <= offlineapicount) {
                        final ArrayList<OfflineDataModel> offlineDataModels = getOfflineDisplayList();
                        // No of case in offline
                        if (offlineDataModels.size() > 0) {
                            OfflineCasePopup();
                        } else {
                            general.CustomToast(getResources().getString(R.string.noofflinecase));
                        }
                    } else {
                        general.CustomToast(getResources().getString(R.string.alreadyexceeded));
                    }
                } else {
                    Connectivity.showNoConnectionDialog(HomeActivity.this);
                }
                break;*/
        }
    }

    private void goOffline() {
        if (Connectivity.isConnected(HomeActivity.this)) {
            int offlineapicount = 0, offlineadaptercount = 0;
            if (!general.isEmpty(offlinecase_count)) {
                offlineapicount = Integer.parseInt(offlinecase_count);
                offlineadaptercount = offlineDataModelAdaptercount();
            }

            if (offlineadaptercount <= offlineapicount) {
                final ArrayList<OfflineDataModel> offlineDataModels = getOfflineDisplayList();
                // No of case in offline
                if (offlineDataModels.size() > 0) {
                    OfflineCasePopup();
                } else {
                    general.CustomToast(getResources().getString(R.string.noofflinecase));
                }
            } else {
                general.CustomToast(getResources().getString(R.string.alreadyexceeded));
            }
        } else {
            Connectivity.showNoConnectionDialog(HomeActivity.this);
        }


    }


    private void OpenLayoutClick() {
        opened_case.setTextColor(getResources().getColor(R.color.White));
        closed_case.setTextColor(getResources().getColor(R.color.Activite));
        opencutLay.setBackgroundColor((getResources().getColor(R.color.colorPrimary)));
        closecutLay.setBackgroundColor((getResources().getColor(R.color.white)));
        open_cut_image.setImageResource(R.drawable.cutlayout);
        close_cut_image.setImageResource(R.drawable.white_cutlayout);
        openViewLayout.setVisibility(View.VISIBLE);
        closeViewLayout.setVisibility(View.GONE);

        if (enable_offline_button) {
            // Show offline Button
            /*    GoOfflineLayout.setVisibility(View.VISIBLE);*/
        } else {
            // Hide offline Button
            /*     GoOfflineLayout.setVisibility(View.GONE);*/
        }

        no_data_found_close.setVisibility(View.GONE);
        no_data_found_offline.setVisibility(View.GONE);
        ArrayList<DataModel> dataModel_open = new ArrayList<>();
        dataModel_open = (ArrayList<DataModel>) appDatabase.interfaceDataModelQuery().getDataModal_opencase(true);
        Log.e(TAG, "OpenLayoutClick: no of case" + dataModel_open.size());
        if (dataModel_open.size() == 0) {
            openViewLayout.setVisibility(View.GONE);
            no_data_found_open.setVisibility(View.VISIBLE);
        } else {
            openViewLayout.setVisibility(View.VISIBLE);
            no_data_found_open.setVisibility(View.GONE);
        }
        offlinecutLay.setBackgroundColor((getResources().getColor(R.color.white)));
        offline_cut_image.setImageResource(R.drawable.white_cutlayout);
        offlineViewLayout.setVisibility(View.GONE);
        offline_case.setTextColor(getResources().getColor(R.color.Activite));
    }

    private void ClosedLayoutClick() {
        opened_case.setTextColor(getResources().getColor(R.color.Activite));
        closed_case.setTextColor(getResources().getColor(R.color.White));
        opencutLay.setBackgroundColor((getResources().getColor(R.color.white)));
        closecutLay.setBackgroundColor((getResources().getColor(R.color.colorPrimary)));
        open_cut_image.setImageResource(R.drawable.white_cutlayout);
        close_cut_image.setImageResource(R.drawable.cutlayout);
        openViewLayout.setVisibility(View.GONE);
        closeViewLayout.setVisibility(View.VISIBLE);
        /*GoOfflineLayout.setVisibility(View.GONE);*/

        no_data_found_open.setVisibility(View.GONE);
        no_data_found_offline.setVisibility(View.GONE);
        ArrayList<DataModel> dataModel_close = new ArrayList<>();
        dataModel_close = (ArrayList<DataModel>) appDatabase.interfaceDataModelQuery().getDataModal_opencase(false);
        if (dataModel_close.size() == 0) {
            closeViewLayout.setVisibility(View.GONE);
            no_data_found_close.setVisibility(View.VISIBLE);
        } else {
            closeViewLayout.setVisibility(View.VISIBLE);
            no_data_found_close.setVisibility(View.GONE);
        }

        offlinecutLay.setBackgroundColor((getResources().getColor(R.color.white)));
        offline_cut_image.setImageResource(R.drawable.white_cutlayout);
        offlineViewLayout.setVisibility(View.GONE);
        offline_case.setTextColor(getResources().getColor(R.color.Activite));
    }

    private void OfflineLayoutClick() {
        opened_case.setTextColor(getResources().getColor(R.color.Activite));
        closed_case.setTextColor(getResources().getColor(R.color.Activite));
        opencutLay.setBackgroundColor((getResources().getColor(R.color.white)));
        closecutLay.setBackgroundColor((getResources().getColor(R.color.white)));
        open_cut_image.setImageResource(R.drawable.white_cutlayout);
        close_cut_image.setImageResource(R.drawable.white_cutlayout);
        openViewLayout.setVisibility(View.GONE);
        closeViewLayout.setVisibility(View.GONE);
        /*  GoOfflineLayout.setVisibility(View.GONE);*/

        no_data_found_open.setVisibility(View.GONE);
        no_data_found_close.setVisibility(View.GONE);
        ArrayList<OfflineDataModel> dataModel_offline = (ArrayList<OfflineDataModel>) appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(true);
        if (dataModel_offline.size() == 0) {
            no_data_found_offline.setVisibility(View.VISIBLE);
        } else {
            no_data_found_offline.setVisibility(View.GONE);
        }

        offlinecutLay.setBackgroundColor((getResources().getColor(R.color.colorPrimary)));
        offline_cut_image.setImageResource(R.drawable.cutlayout);
        offlineViewLayout.setVisibility(View.VISIBLE);
        offline_case.setTextColor(getResources().getColor(R.color.White));
    }

    /*******
     * OpenCloseCase Adapter list API call
     * *******/
    private void OpenCloseAPIWebserviceCall() {
        if (Connectivity.isConnected(this)) {
            //general.showloading(HomeActivity.this);
            shimmerHomeView.startShimmer();
            shimmerHomeView.setVisibility(View.VISIBLE);
            parentLayout.setVisibility(View.GONE);

            InitiateOpenClosedCaseListTask();
//            setEnabledColorOffline();
        } else {
            // Room
            if (appDatabase.interfaceDataModelQuery().getDataModels().size() > 0) {
                //  general.showloading(HomeActivity.this);

                shimmerHomeView.startShimmer();
                shimmerHomeView.setVisibility(View.VISIBLE);
                parentLayout.setVisibility(View.GONE);

                if (appDatabase.interfaceDataModelQuery().getDataModal_opencase(true).size() > 0) {
                    // Log.e("appDatabase_opensize", ": " + appDatabase.interfaceDataModelQuery().getDataModal_opencase(true).size());
                    ArrayList<DataModel> dataModel_open = new ArrayList<>();
                    dataModel_open = (ArrayList<DataModel>) appDatabase.interfaceDataModelQuery().getDataModal_opencase(true);
                    loadOpenCaseAdapter(dataModel_open);
                }

                if (appDatabase.interfaceDataModelQuery().getDataModal_opencase(false).size() > 0) {
                    // Log.e("appDatabase_closesize", ": " + appDatabase.interfaceDataModelQuery().getDataModal_opencase(false).size());
                    ArrayList<DataModel> dataModel_close = new ArrayList<>();
                    dataModel_close = (ArrayList<DataModel>) appDatabase.interfaceDataModelQuery().getDataModal_opencase(false);
                    loadCloseCaseAdapter(dataModel_close);
                }

                InitialOfflineCaseList();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // general.hideloading();
                        shimmerHomeView.stopShimmer();
                        shimmerHomeView.setVisibility(View.GONE);
                        parentLayout.setVisibility(View.VISIBLE);
                    }
                }, 2500);
            }
        }
    }

    private void OfflineCaseList(ArrayList<OfflineDataModel> offlineDataModels) {
        // Room for offline cases

        // general.showloading(HomeActivity.this);
        shimmerHomeView.startShimmer();
        shimmerHomeView.setVisibility(View.VISIBLE);
        parentLayout.setVisibility(View.GONE);
        // set true on offline case
        checkBoxsaveRoomDB(offlineDataModels);

        if (appDatabase.interfaceOfflineDataModelQuery().getDataModels().size() > 0) {

            if (appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(true).size() > 0) {
                /* general.showloading(HomeActivity.this);*/
                // Log.e("appDatabase_opensize", ": " + appDatabase.interfaceDataModelQuery().getDataModal_opencase(true).size());
                ArrayList<OfflineDataModel> dataModel_offline = new ArrayList<>();
                dataModel_offline = (ArrayList<OfflineDataModel>) appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(true);

                ArrayList<DataModel> dataModel_open = new ArrayList<>();
                if (appDatabase.interfaceDataModelQuery().getDataModels().size() > 0) {
                    // Log.e("appDatabase_size", ": " + appDatabase.interfaceDataModelQuery().getDataModels().size());
                    if (appDatabase.interfaceDataModelQuery().getDataModal_opencase(true).size() > 0) {
                        dataModel_open = (ArrayList<DataModel>) appDatabase.interfaceDataModelQuery().getDataModal_opencase(true);
                    }
                }

                //Opencase adapter update Offline case datamodel
                for (int i = 0; i < dataModel_offline.size(); i++) {
                    String caseid = dataModel_offline.get(i).getCaseId();
                    for (int j = 0; j < dataModel_open.size(); j++) {
                        String opencaseId = dataModel_open.get(j).getCaseId();
                        if (caseid.equalsIgnoreCase(opencaseId)) {
                            //make the open case datamodel true
                            DataModel dataModel = dataModel_open.get(j);
                            dataModel.setOfflinecase(true);
                            if (appDatabase.interfaceDataModelQuery().getDataModels().size() > 0) {
                                appDatabase.interfaceDataModelQuery().updateOfflineDataModel(dataModel_open.get(j).getCaseId(), true);
                            }
                        }
                    }
                }

                //Retrieve From Room Database query to list the opendatabase
                if (appDatabase.interfaceDataModelQuery().getDataModels().size() > 0) {
                    // Log.e("appDatabase_size", ": " + appDatabase.interfaceDataModelQuery().getDataModels().size());
                    if (appDatabase.interfaceDataModelQuery().getDataModal_opencase(true).size() > 0) {
                        // Log.e("appDatabase_opensize", ": " + appDatabase.interfaceDataModelQuery().getDataModal_opencase(true).size());
                        ArrayList<DataModel> dataModel_open1 = new ArrayList<>();
                        dataModel_open1 = (ArrayList<DataModel>) appDatabase.interfaceDataModelQuery().getDataModal_offlinecase(false, true);
                        loadOpenCaseAdapter(dataModel_open1);
                    }
                }


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Call the api
                        offline_api();

                    }
                }, 1500);


            } else {
                offlinecaseEmptychecked();
            }
        } else {
            // general.hideloading();

            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
        }

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                general.hideloading();
            }
        }, 2500);*/
    }

    private void offline_api() {
        if (Connectivity.isConnected(this)) {
            ArrayList<OfflineDataModel> dataModel_offline_list = new ArrayList<>();
            dataModel_offline_list = (ArrayList<OfflineDataModel>) appDatabase.interfaceOfflineDataModelQuery().getDataModal_current_offline(true);
            Log.e("offline_case_size", "offline_case_size_1 : " + dataModel_offline_list.size());
            if (dataModel_offline_list.size() > 0) {
                // setting caseoffline
                oflineCases_list = new ArrayList<>();
                if (dataModel_offline_list.size() > 0) {
                    for (int x = 0; x < dataModel_offline_list.size(); x++) {
                        OflineCase oflineCase_obj = new OflineCase();
                        oflineCase_obj.setCaseId(Integer.parseInt(dataModel_offline_list.get(x).getCaseId()));
                        oflineCase_obj.setPropertyId(Integer.parseInt(dataModel_offline_list.get(x).getPropertyId()));
                        oflineCases_list.add(oflineCase_obj);
                        // Room Delete - OfflineCase
                        appDatabase.interfaceOfflineCaseQuery().deleteRow(oflineCase_obj.getCaseId());
                        // Room Add - OfflineCase
                        appDatabase.interfaceOfflineCaseQuery().insertAll(oflineCase_obj);
                        // updatecurrent_offlineDataModel as false
                        appDatabase.interfaceOfflineDataModelQuery().updatecurrent_offlineDataModel(dataModel_offline_list.get(x).getCaseId(), false);
                    }
                    if ((oflineCases_list.size() > 0)) {
                        // Set percentage level
                        percentage_level = 90 / oflineCases_list.size();
                        percentage_level_total = percentage_level;
                        percentage_level_half = percentage_level / 2;
                        Log.e("percentage_level_half", "percentage_level_half: " + percentage_level_half);
                        // set a one as default value
                        // general.hideloading();
                        shimmerHomeView.stopShimmer();
                        shimmerHomeView.setVisibility(View.GONE);
                        parentLayout.setVisibility(View.VISIBLE);

                        call_circle_loading();
                        start_ofline_case = 0;
                        // Hit all offline api and save it in local
                        hit_all_offline_api(oflineCases_list.get(start_ofline_case).getCaseId(), oflineCases_list.get(start_ofline_case).getPropertyId());
                    }
                }
            }
        } else {
            Connectivity.showNoConnectionDialog(this);
            // general.hideloading();
            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
        }
    }

    private void offlinecaseEmptychecked() {
        /**********When Offline case is not selected then show the adapter with
         * unchecked checkboxes and load all open cases************/
        if (appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(false).size() > 0) {
            // general.showloading(HomeActivity.this);

            shimmerHomeView.startShimmer();
            shimmerHomeView.setVisibility(View.VISIBLE);

            // Log.e("appDatabase_opensize", ": " + appDatabase.interfaceDataModelQuery().getDataModal_opencase(true).size());
            ArrayList<OfflineDataModel> dataModel_offline = new ArrayList<>();
            //  dataModel_offline = (ArrayList<OfflineDataModel>) appDatabase.interfaceOfflineDataModelQuery().getDataModels();

            ArrayList<DataModel> dataModel_open = new ArrayList<>();
            if (appDatabase.interfaceDataModelQuery().getDataModels().size() > 0) {
                // Log.e("appDatabase_size", ": " + appDatabase.interfaceDataModelQuery().getDataModels().size());
                if (appDatabase.interfaceDataModelQuery().getDataModal_opencase(true).size() > 0) {
                    dataModel_open = (ArrayList<DataModel>) appDatabase.interfaceDataModelQuery().getDataModal_opencase(true);
                }
            }

            loadOfflineCaseAdapter(dataModel_offline);

            loadOpenCaseAdapter(dataModel_open);
            /* GoOfflineLayout.setBackground(getResources().getDrawable(R.drawable.buttonshape));*/

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //general.hideloading();
                shimmerHomeView.stopShimmer();
                shimmerHomeView.setVisibility(View.GONE);
                parentLayout.setVisibility(View.VISIBLE);
            }
        }, 2500);
    }

  /*  private void setEnabledColorOffline() {
        if (appDatabase.interfaceOfflineDataModelQuery().getDataModels().size() > 0) {
            if (appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(true).size() > 0) {
                GoOfflineLayout.setBackground(getResources().getDrawable(R.drawable.buttonshape));
            } else {
                GoOfflineLayout.setBackground(getResources().getDrawable(R.drawable.buttonshape));
            }
        } else {
            GoOfflineLayout.setBackground(getResources().getDrawable(R.drawable.buttonshape));
        }
    }*/

    private void InitialOfflineCaseList() {

        // Room for offline cases
        if (appDatabase.interfaceOfflineDataModelQuery().getDataModels().size() > 0) {

            Log.e("appDatabaseOffline_sizefalse", ": " + appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(false).size());
            Log.e("appDatabaseOffline_sizetrue", ": " + appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(true).size());
            if (appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(true).size() > 0) {
//                GoOfflineLayout.setBackground(getResources().getDrawable(R.drawable.buttonshape));

                // Log.e("appDatabase_opensize", ": " + appDatabase.interfaceDataModelQuery().getDataModal_opencase(true).size());
                ArrayList<OfflineDataModel> dataModel_offline = new ArrayList<>();
                dataModel_offline = (ArrayList<OfflineDataModel>) appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(true);


                ArrayList<DataModel> dataModel_open = new ArrayList<>();
                if (appDatabase.interfaceDataModelQuery().getDataModels().size() > 0) {
                    // Log.e("appDatabase_size", ": " + appDatabase.interfaceDataModelQuery().getDataModels().size());
                    if (appDatabase.interfaceDataModelQuery().getDataModal_opencase(true).size() > 0) {
                        dataModel_open = (ArrayList<DataModel>) appDatabase.interfaceDataModelQuery().getDataModal_opencase(true);
                    }
                }

                /*Opencase adapter set offline data model
                as true to remove its data from opencase list*/
                for (int i = 0; i < dataModel_offline.size(); i++) {
                    String caseid = dataModel_offline.get(i).getCaseId();

                    for (int j = 0; j < dataModel_open.size(); j++) {
                        String opencaseId = dataModel_open.get(j).getCaseId();
                        if (caseid.equalsIgnoreCase(opencaseId)) {

                            //make the open case datamodel true
                            if (MyApplication.getAppContext() != null) {
                                AppDatabase appDatabase = AppDatabase.getAppDatabase(MyApplication.getAppContext());

                                DataModel dataModel = dataModel_open.get(j);
                                dataModel.setOfflinecase(true);
                                if (appDatabase.interfaceDataModelQuery().getDataModels().size() > 0) {
                                    // long value = appDatabase.interfaceDataModelQuery().updateOfflineDataModel(dataModel_open.get(j).getCaseId(), true);
                                    //Log.e("update", value + "");
                                }
                            }
                        }
                    }
                }

                //Retrieve From Room Database query to list the opendatabase
                if (appDatabase.interfaceDataModelQuery().getDataModels().size() > 0) {
                    // Log.e("appDatabase_size", ": " + appDatabase.interfaceDataModelQuery().getDataModels().size());
                    if (appDatabase.interfaceDataModelQuery().getDataModal_opencase(true).size() > 0) {
                        // Log.e("appDatabase_opensize", ": " + appDatabase.interfaceDataModelQuery().getDataModal_opencase(true).size());
                        ArrayList<DataModel> dataModel_open1 = new ArrayList<>();
                        dataModel_open1 = (ArrayList<DataModel>) appDatabase.interfaceDataModelQuery().getDataModal_offlinecase(false, true);
                        loadOpenCaseAdapter(dataModel_open1);
                    }
                }

                Collections.sort(dataModel_offline, new Comparator<OfflineDataModel>() {
                    public int compare(OfflineDataModel obj1, OfflineDataModel obj2) {
                        // ## Descending order
                        return obj2.getCaseId().compareToIgnoreCase(obj1.getCaseId()); // To compare string values
                    }
                });


                loadOfflineCaseAdapter(dataModel_offline);

                //IncludeInitialSavedOfflineCase();
            } else {
//                GoOfflineLayout.setBackground(getResources().getDrawable(R.drawable.buttonshape));

            }
        }
    }

    private void getFlagSettings() {
        Log.e(TAG, "getFlagSettings: ");
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(SettingsUtils.FLAG_URL);

        WebserviceCommunicator webserviceCommunicator = new WebserviceCommunicator(HomeActivity.this, requestData, SettingsUtils.GET);
        webserviceCommunicator.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                Log.e(TAG, "onTaskComplete: " + new Gson().toJson(requestData.getResponse()));
                try {
                    parserFlagResponse(new Gson().fromJson(requestData.getResponse(), UrlModel.class), requestData.isSuccessful(), requestData.getResponseCode());
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });
        webserviceCommunicator.execute();
    }

    private void parserFlagResponse(UrlModel model, boolean isSuccessful, int responseCode) {

        Log.e(TAG, "parserFlagResponse: " + general.ApiBaseUrl() + "\n" + new Gson().toJson(model));

        if (isSuccessful) {
            String url = general.ApiBaseUrl();

            if (general.ApiBaseUrl().contains("https")) {
                url = url.replace("https://", "");
            }
            if (general.ApiBaseUrl().contains("http")) url = url.replace("http://", "");

            Log.e(TAG, "parserFlagResponse: Url=>" + url);

            if (model != null) {
                if (model.getData() != null) {
                    for (int i = 0; i < model.getData().getNew().size(); i++) {
                        if (model.getData().getNew().get(i).getWebsiteName().contains(url)) {
                            SettingsUtils.getInstance().putValue(SettingsUtils.KEY_FLAG_URL, "NEW");
                        }
                    }

                    for (int i = 0; i < model.getData().getOld().size(); i++) {
                        if (model.getData().getOld().get(i).getWebsiteName().contains(url))
                            SettingsUtils.getInstance().putValue(SettingsUtils.KEY_FLAG_URL, "OLD");
                    }
                }

                getTypeLists();
            }
        } else {
            general.customToast(getString(R.string.something_wrong), HomeActivity.this);
        }
    }

    private void getTypeLists() {
        String url = general.ApiBaseUrl() + SettingsUtils.TYPE_OF_MORTAR;
        // String url = SettingsUtils.BASE_URL + SettingsUtils.GetFieldDropDowns;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(HomeActivity.this, requestData, SettingsUtils.GET_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                Log.e(TAG, ": " + requestData.getResponse());
                dropDownResponse(requestData.getResponse(), requestData.getResponseCode(), requestData.isSuccessful());
            }
        });
        webserviceTask.execute();
    }

    private void dropDownResponse(String response, int responseCode, boolean successful) {
        RequestApiStatus requestApiStatus = new Gson().fromJson(response, RequestApiStatus.class);
        if (requestApiStatus.getStatus() == 1) {
            try {
                Log.e(TAG, "dropDownResponse: " + response);
                TypeOfMortar typeOfMortar = new Gson().fromJson(response, TypeOfMortar.class);
                List<TypeOfMortar.Datum> data = new ArrayList<>();
                data.addAll(typeOfMortar.getData());
                data.add(0, new TypeOfMortar.Datum("Select"));
                Singleton.getInstance().typeOfMortarsList = data;
                getMasonry();
            } catch (Exception e) {
                getMasonry();
            }
        } else {
            if (!successful && (responseCode == 400 || responseCode == 401)) {
                General.sessionDialog(HomeActivity.this);
            } else {
                getMasonry();
            }
        }

    }

    private void getMasonry() {
        String url = general.ApiBaseUrl() + SettingsUtils.TYPE_OF_MASONRY;
        // String url = SettingsUtils.BASE_URL + SettingsUtils.GetFieldDropDowns;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(HomeActivity.this, requestData, SettingsUtils.GET_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                try {
                    masonryResponse(requestData.getResponse(), requestData.getResponseCode(), requestData.isSuccessful());
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });
        webserviceTask.execute();
    }

    private void masonryResponse(String response, int responseCode, boolean successful) {
        if (successful) {
            Log.e(TAG, "dropDownResponse: " + response);
            TypeOfMasonry typeOfMortar = new Gson().fromJson(response, TypeOfMasonry.class);
            List<TypeOfMasonry.Datum> data = new ArrayList<>();
            data.addAll(typeOfMortar.getData());
            data.add(0, new TypeOfMasonry.Datum("Select"));
            Singleton.getInstance().typeOfMasonryList = data;
            getFooting();
        } else if (!successful && (responseCode == 400 || responseCode == 401)) {
            General.sessionDialog(HomeActivity.this);
        } else {
            getFooting();
            General.customToast(getString(R.string.something_wrong), HomeActivity.this);
        }
    }

    private void getFooting() {
        String url = general.ApiBaseUrl() + SettingsUtils.TYPE_OF_FOOTING;
        // String url = SettingsUtils.BASE_URL + SettingsUtils.GetFieldDropDowns;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(HomeActivity.this, requestData, SettingsUtils.GET_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                footingResponse(requestData.getResponse(), requestData.getResponseCode(), requestData.isSuccessful());
            }
        });
        webserviceTask.execute();
    }

    private void footingResponse(String response, int responseCode, boolean successful) {
        try {
            if (successful) {
                Log.e(TAG, "dropDownResponse: " + response);
                TypeOfFooting typeOfMortar = new Gson().fromJson(response, TypeOfFooting.class);
                List<TypeOfFooting.Datum> data = new ArrayList<>();
                data.addAll(typeOfMortar.getData());
                data.add(0, new TypeOfFooting.Datum("Select"));
                Singleton.getInstance().typeOfFootingList = data;
                getSteel();
            } else if (!successful && (responseCode == 400 || responseCode == 401)) {
                General.sessionDialog(HomeActivity.this);
            } else {
                getSteel();
                General.customToast(getString(R.string.something_wrong), HomeActivity.this);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void getSteel() {
        String url = general.ApiBaseUrl() + SettingsUtils.TYPE_OF_STEEL;
        // String url = SettingsUtils.BASE_URL + SettingsUtils.GetFieldDropDowns;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(HomeActivity.this, requestData, SettingsUtils.GET_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                try {
                    steelResponse(requestData.getResponse(), requestData.getResponseCode(), requestData.isSuccessful());
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });
        webserviceTask.execute();
    }

    private void steelResponse(String response, int responseCode, boolean successful) {
        if (successful) {
            Log.e(TAG, "dropDownResponse: " + response);
            TypeOfSteel typeOfMortar = new Gson().fromJson(response, TypeOfSteel.class);
            List<TypeOfSteel.Datum> data = new ArrayList<>();
            data.addAll(typeOfMortar.getData());
            data.add(0, new TypeOfSteel.Datum("Select"));
            Singleton.getInstance().typeOfSteelList = data;
        } else if (!successful && (responseCode == 400 || responseCode == 401)) {
            General.sessionDialog(HomeActivity.this);
        } else {
            General.customToast(getString(R.string.something_wrong), HomeActivity.this);
        }
    }


    private void InitiateOpenClosedCaseListTask() {
        getInputParams();
        String url = general.ApiBaseUrl() + SettingsUtils.OpenClosedCaseList;
        // String url = SettingsUtils.BASE_URL + SettingsUtils.OpenClosedCaseList;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setInitQueryUrl(url);
        requestData.setEmpId(empId);
        requestData.setStartDate(startDate);
        requestData.setEndDate(endDate);
        requestData.setAgencyBranchId(SettingsUtils.getInstance().getValue(SettingsUtils.BranchId, ""));
        requestData.setUrl(RequestParam.OpenCloseCaseListRequestParams(requestData));
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));

        Log.e(TAG, "InitiateOpenClosedCaseListTask: " + SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));

        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(HomeActivity.this, requestData, SettingsUtils.GET_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {

                System.out.println(requestData.getResponse());
                parseOpenCloseCaseListResponse(requestData.getResponse(), requestData.getResponseCode(), requestData.isSuccessful());
            }
        });
        webserviceTask.execute();
    }

    private void parseOpenCloseCaseListResponse(String response, int responseCode, boolean isSuccessful) {

        DataResponse dataResponse = ResponseParser.parseOpenCloseCaseListResponse(response);
        String result = "";
        if (response != null) {
            result = dataResponse.status;
            msg = dataResponse.msg;
            info = dataResponse.info;
            openCaseDataModels = dataResponse.openCaseList;
            closeCaseDataModels = dataResponse.closeCaseList;
            offlineCaseDataModels = dataResponse.offlineCaseList;
            Singleton.getInstance().openCaseList = dataResponse.openCaseList;
            Singleton.getInstance().closeCaseList = dataResponse.closeCaseList;
            Singleton.getInstance().offlineCaseList = dataResponse.offlineCaseList;
        }
        if (!isSuccessful && responseCode == 400 || responseCode == 401) {
            //general.hideloading();
            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
            General.sessionDialog(HomeActivity.this);
        } else {
            if (result != null) {
                if (result.equals("1")) {
                    /*****open recylerview set adapter******/
                    loadOpenCaseAdapter(Singleton.getInstance().openCaseList);
                    /*****close recylerview set adapter******/
                    loadCloseCaseAdapter(Singleton.getInstance().closeCaseList);
                    /*****offline recylerview set adapter******/
                    IncludeInitialOfflineCases();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //general.hideloading();
                            shimmerHomeView.stopShimmer();
                            shimmerHomeView.setVisibility(View.GONE);
                            parentLayout.setVisibility(View.VISIBLE);
                        }
                    }, 100);

                } else if (result.equals("2")) {
                    general.CustomToast(msg);
                    //general.hideloading();
                    shimmerHomeView.stopShimmer();
                    shimmerHomeView.setVisibility(View.GONE);
                    parentLayout.setVisibility(View.VISIBLE);
                } else if (result.equals("0")) {
                    general.CustomToast(msg);
                    //general.hideloading();
                    shimmerHomeView.stopShimmer();
                    shimmerHomeView.setVisibility(View.GONE);
                    parentLayout.setVisibility(View.VISIBLE);
                }
            } else {
                general.CustomToast(getResources().getString(R.string.serverProblem));
                //general.hideloading();
                shimmerHomeView.stopShimmer();
                shimmerHomeView.setVisibility(View.GONE);
                parentLayout.setVisibility(View.VISIBLE);
            }
        }

    }

    private void getStartEndDate() {
      /*  Calendar now = Calendar.getInstance();
        Calendar prev = Calendar.getInstance();
        System.out.println("Current date : " + now.get(Calendar.DATE) + "/" +
                (now.get(Calendar.MONTH) + 1) + "/" +
                now.get(Calendar.YEAR));
        System.out.println("Previous month date : " + now.get(Calendar.DATE) + "/" +
                (now.get(Calendar.MONTH) - 1) + "/" +
                now.get(Calendar.YEAR));

        *//*SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
        prev.add(Calendar.DATE, -15);
        startDate = sdf.format(prev.getTime());*//*

        String stdate = now.get((Calendar.DATE)) + "/" +
                (now.get(Calendar.MONTH)) + "/" +
                now.get(Calendar.YEAR);
        startDate = general.ConvertDate(stdate);*/

        startDate = General.getDateAfterAMonthAgo();
        endDate = General.getTodayDate();
        Log.e(TAG, "getStartEndDate: Start: " + startDate + "\nend: " + endDate);
/*
        String enddate = now.get(Calendar.DATE) + "/" +
                (now.get(Calendar.MONTH) + 1) + "/" +
                now.get(Calendar.YEAR);

       *//* String enddate = now.get(Calendar.DAY_OF_MONTH +1) + "/" +
                (now.get(Calendar.MONTH) + 1) + "/" +
                now.get(Calendar.YEAR);*//*
        endDate = general.ConvertDate(enddate);*/

    }

    public void getInputParams() {
        getStartEndDate();
        empId = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGIN_ID, "");
        /*empId = "22";
        startDate = "07/Dec/2017";
        endDate = "18/Dec/2017";*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.logout);
        MenuItem item1 = menu.findItem(R.id.refresh);
        MenuItem item2 = menu.findItem(R.id.ic_home);
        MenuItem item3 = menu.findItem(R.id.versionname);
        MenuItem item4 = menu.findItem(R.id.noncaseactivity);
        MenuItem item5 = menu.findItem(R.id.convyencereport);
        MenuItem item6 = menu.findItem(R.id.raiseticketsystem);
        MenuItem item7 = menu.findItem(R.id.viewticket);
        MenuItem item8 = menu.findItem(R.id.filter);

        item.setVisible(true);
        item1.setVisible(true);
        item3.setVisible(true);
        item4.setVisible(true);
        item5.setVisible(true);
        item2.setVisible(false);
        item8.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                if (Connectivity.isConnected(this)) {
                    if (general.checkPermissions()) {
                        // getCurrentLocation(this);
                        if (!general.isLogoutClicked) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                LogOutScheduler.cancelAlarm();
                            }
                            general.LogoutDialog(this, SettingsUtils.Latitudes, SettingsUtils.Longitudes);
                        }
                    } else Log.e(TAG, "permission denied");
                } else {
                    Connectivity.showNoConnectionDialog(this);
                }
                break;

            case R.id.refresh:
                OpenCloseAPIWebserviceCall();
                /*OfflineCaseList();*/
                break;

            case R.id.versionname:
                VersionNamePopup();
                break;

            case R.id.gooffline_case:
                goOffline();
                break;

            case R.id.create:
                getFreshCaseSelections();
                break;

            case R.id.noncaseactivity:
                getNonCaseActivity();
                break;
            case R.id.convyencereport:
                getConvyenceReport();
                break;

            case R.id.raiseticketsystem:
                raiseTickerPopup();
                break;

            case R.id.viewticket:
                getViewTicketSystem();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getFreshCaseSelections() {

        if (Connectivity.isConnected(this)) {
            // general.showloading(this);
            shimmerHomeView.startShimmer();
            shimmerHomeView.setVisibility(View.VISIBLE);
            parentLayout.setVisibility(View.GONE);
            String url = general.ApiBaseUrl() + SettingsUtils.GetFreshCaseSelections;
            JsonRequestData requestData = new JsonRequestData();
            requestData.setInitQueryUrl(url);

            Log.e(TAG, "getFreshCaseSelections: " + SettingsUtils.getInstance().getValue(SettingsUtils.BranchId, "") + "==>" + "<===" + SettingsUtils.getInstance().getValue(SettingsUtils.AgencyId, ""));

            requestData.setAgencyBranchId(SettingsUtils.getInstance().getValue(SettingsUtils.BranchId, ""));
            requestData.setAgencyId(SettingsUtils.getInstance().getValue(SettingsUtils.AgencyId, ""));
            requestData.setUrl(RequestParam.GetFreshCaseSelectionRequest(requestData));
            requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(this, requestData, SettingsUtils.GET_TOKEN);
            webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                @Override
                public void onTaskComplete(JsonRequestData requestData) {
                    try {
                        Log.e(TAG, "getFreshCaseSelections: " + requestData.getResponse());
                        System.out.println(new Gson().toJson(requestData.getResponse()));
                        parseCreateCaseResponse(new Gson().fromJson(requestData.getResponse(), CaseSelection.class), requestData.getResponseCode(), requestData.isSuccessful());
                    } catch (Exception e) {
                        //general.hideloading();
                        shimmerHomeView.stopShimmer();
                        shimmerHomeView.setVisibility(View.GONE);
                        parentLayout.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                }
            });
            webserviceTask.execute();
        } else {
            Connectivity.showNoConnectionDialog(this);
            //general.hideloading();
            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
        }
    }

    private void parseCreateCaseResponse(CaseSelection fromJson, int responseCode, boolean isSuccessful) {
        // general.hideloading();
        shimmerHomeView.stopShimmer();
        shimmerHomeView.setVisibility(View.GONE);
        parentLayout.setVisibility(View.VISIBLE);
        caseSelectionsProperty = new ArrayList<>();
        caseSelectionsProperty.add(0, new CaseSelection.Data.PropertyType("Select Property Type"));
        caseSelectionsCaseAdmin = new ArrayList<>();
        //caseSelectionsCaseAdmin.add(0, new CaseSelection.Data.CaseAdmin("Select Case Admin"));
        caseSelectionsReportMaker = new ArrayList<>();
        //caseSelectionsReportMaker.add(0, new CaseSelection.Data.ReportMaker("Select Report Maker"));
        caseSelectionsCaseAdmin.addAll(fromJson.getData().getCaseAdmin());
        caseSelectionsProperty.addAll(fromJson.getData().getPropertyType());
        caseSelectionsReportMaker.addAll(fromJson.getData().getReportMaker());
        //general.hideloading();
        shimmerHomeView.stopShimmer();
        shimmerHomeView.setVisibility(View.GONE);
        parentLayout.setVisibility(View.VISIBLE);
        if (isSuccessful) getSubBranches();
//            createCasePopup();
        else {
            if (responseCode == 400 || responseCode == 401)
                General.sessionDialog(HomeActivity.this);
            else General.customToast(getString(R.string.something_wrong), HomeActivity.this);
        }

    }

    private void getSubBranches() {
        if (Connectivity.isConnected(this)) {
            //general.showloading(this);
            shimmerHomeView.startShimmer();
            shimmerHomeView.setVisibility(View.VISIBLE);
            parentLayout.setVisibility(View.GONE);

            String url = general.ApiBaseUrl() + SettingsUtils.AgencySubBranches;
            JsonRequestData requestData = new JsonRequestData();
            requestData.setInitQueryUrl(url);
            requestData.setAgencyId(SettingsUtils.getInstance().getValue(SettingsUtils.BranchId, ""));
            requestData.setUrl(RequestParam.GetSubBranches(requestData));
            requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));

            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(this, requestData, SettingsUtils.GET_TOKEN);
            webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                @Override
                public void onTaskComplete(JsonRequestData requestData) {
                    try {
                        Log.e(TAG, "onTaskComplete: " + requestData.getResponse());
                        subBranchResponse(new Gson().fromJson(requestData.getResponse(), SubBranchModel.class), requestData.getResponseCode(), requestData.isSuccessful());
                    } catch (Exception e) {
                        // general.hideloading();
                        shimmerHomeView.stopShimmer();
                        shimmerHomeView.setVisibility(View.GONE);
                        parentLayout.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                }
            });
            webserviceTask.execute();


        } else {
            Connectivity.showNoConnectionDialog(this);
            // general.hideloading();
            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
        }
    }

    private void subBranchResponse(SubBranchModel fromJson, int responseCode, boolean isSuccessful) {
        if (isSuccessful) {
            subBranchList = fromJson.getData();
            //subBranchList.add(0, new SubBranchModel.Datum(0, 0, "Select Sub Branch"));
            //general.hideloading();
            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
            createCasePopup();
        } else {
            //general.hideloading();
            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
            if (responseCode == 400 || responseCode == 401)
                General.sessionDialog(HomeActivity.this);
            else General.customToast(getString(R.string.something_wrong), HomeActivity.this);
        }
    }


    private void VersionNamePopup() {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.versionname_popup);

        TextView versionname = (TextView) dialog.findViewById(R.id.versionname);
        TextView versionnametext = (TextView) dialog.findViewById(R.id.versionnametext);
        TextView releasedate = (TextView) dialog.findViewById(R.id.releasedate);
        TextView releasedatetext = (TextView) dialog.findViewById(R.id.releasedatetext);
        TextView okBtn = (TextView) dialog.findViewById(R.id.okBtn);
        versionname.setTypeface(general.mediumtypeface());
        versionname.setText(BuildConfig.VERSION_NAME);
        releasedate.setText(BuildConfig.DATE);
        versionnametext.setTypeface(general.mediumtypeface());
        releasedate.setTypeface(general.mediumtypeface());
        releasedatetext.setTypeface(general.mediumtypeface());
        okBtn.setTypeface(general.mediumtypeface());

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private ArrayList<OfflineDataModel> getOfflineDisplayList() {

        ArrayList<OfflineDataModel> dataModel_offline = new ArrayList<>();
        if (appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(false).size() > 0) {
            dataModel_offline = (ArrayList<OfflineDataModel>) appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(false);
        }
        return dataModel_offline;
    }

    private void setSelectOfflinefromList() {
        /*******get the Datamodel Offline checkbox lists******/
        ArrayList<OfflineDataModel> dataModel_offline = new ArrayList<>();
        if (appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(false).size() > 0) {
            // general.showloading(HomeActivity.this);
            shimmerHomeView.startShimmer();
            shimmerHomeView.setVisibility(View.VISIBLE);
            parentLayout.setVisibility(View.GONE);
            dataModel_offline = (ArrayList<OfflineDataModel>) appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(false);
            //dataModel_offline = (ArrayList<OfflineDataModel>) appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(true);
        }

        /*******Make the OpenCase Offline moved initially as false******/
        if (MyApplication.getAppContext() != null) {
            AppDatabase appDatabase = AppDatabase.getAppDatabase(MyApplication.getAppContext());
            for (int i = 0; i < dataModel_offline.size(); i++) {
                if (appDatabase.interfaceDataModelQuery().getDataModels().size() > 0) {
                    //long value = appDatabase.interfaceDataModelQuery().updateOfflineDataModel(dataModel_offline.get(i).getCaseId(), false);
                    //Log.e("update", value + "");
                }
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // general.hideloading();
                shimmerHomeView.stopShimmer();
                shimmerHomeView.setVisibility(View.GONE);
                parentLayout.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    private void createCasePopup() {
        final Dialog dialog = new Dialog(HomeActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.creatcase_popup);

        Log.e(TAG, "createCasePopup: ");


        final Spinner spSalution = dialog.findViewById(R.id.spSalution);
        final Spinner spPropertyType = dialog.findViewById(R.id.spPropertyType);
        final Spinner spBankName = dialog.findViewById(R.id.spBankName);
        //final Spinner spCaseAdmin = dialog.findViewById(R.id.spCaseAdmin);
        //final Spinner spReportMaker = dialog.findViewById(R.id.spReportMaker);
        // final Spinner spSubBranch = dialog.findViewById(R.id.spSubBranch);

        final TextView spCaseAdmin = dialog.findViewById(R.id.caseAdmin);
        final TextView spReportMaker = dialog.findViewById(R.id.reportMaker);

        final TextView tSubBranch = dialog.findViewById(R.id.subBranch);
        final EditText etName = dialog.findViewById(R.id.etName);
        final EditText etBankNo = dialog.findViewById(R.id.etBankNo);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.row_spinner_item_, getResources().getStringArray(R.array.salution));
        arrayAdapter.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spSalution.setAdapter(arrayAdapter);

       /* ArrayAdapter<CaseSelection.Data.CaseAdmin> arrayAdapter1 = new ArrayAdapter<>(this,
                R.layout.row_spinner_item_, caseSelectionsCaseAdmin);
        arrayAdapter1.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spCaseAdmin.setAdapter(arrayAdapter1);
*/
        ArrayAdapter<CaseSelection.Data.PropertyType> arrayAdapter2 = new ArrayAdapter<>(this, R.layout.row_spinner_item_, caseSelectionsProperty);
        arrayAdapter2.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spPropertyType.setAdapter(arrayAdapter2);

       /* ArrayAdapter<CaseSelection.Data.ReportMaker> arrayAdapter3 = new ArrayAdapter<>(this,
                R.layout.row_spinner_item_, caseSelectionsReportMaker);
        arrayAdapter3.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spReportMaker.setAdapter(arrayAdapter3);*/

       /* ArrayAdapter<SubBranchModel.Datum> arrayAdapter4 = new ArrayAdapter<>(this,
                R.layout.row_spinner_item_, subBranchList);
        arrayAdapter4.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spSubBranch.setAdapter(arrayAdapter4);*/


        // initSubBranch(tSubBranch);

        initCaseAdimn(spCaseAdmin);

        initReportMaker(spReportMaker);


        spPropertyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    propertyId = caseSelectionsProperty.get(position).getTypeOfPropertyId();
                    getBankSelection(spBankName, propertyId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCreateCase(spSalution, etName, spPropertyType, spBankName, spCaseAdminName, spReportMakerName, subBranchName, etBankNo, dialog);
            }
        });
        dialog.show();
    }

    private void validateCreateCase(Spinner spSalution, EditText etName, Spinner spPropertyType, Spinner spBankName, String spCaseAdmin, String spReportMaker, String spSubBranch, EditText etBankNo, Dialog dialog) {
        if (etName.getText().toString().length() < 3)
            general.customToast("Enter applicant name!", HomeActivity.this);
        else if (spPropertyType.getSelectedItemPosition() == 0)
            general.customToast("Choose property type!", HomeActivity.this);
        else if (spBankName.getSelectedItemPosition() == 0)
            general.customToast("Choose bank!", HomeActivity.this);
        else if (etBankNo.getText().toString() == null || etBankNo.getText().toString().isEmpty() || etBankNo.getText().toString().length() < 1)
            general.customToast("Enter Finnone ID!", HomeActivity.this);
        /*else if (spSubBranch.isEmpty())
            general.customToast("Choose the sub-branch!", HomeActivity.this);*/
        else if (spCaseAdmin.isEmpty())
            general.customToast("Choose case admin!", HomeActivity.this);
        else if (spReportMaker.isEmpty())
            general.customToast("Choose report maker!", HomeActivity.this);
        else if (etName.getText().toString().length() > 3 && spPropertyType.getSelectedItemPosition() > 0 && spBankName.getSelectedItemPosition() > 0 && !spCaseAdmin.isEmpty() && !spReportMaker.isEmpty()) {

            String applicantName = spSalution.getSelectedItem() + " " + etName.getText().toString();
            propertyId = caseSelectionsProperty.get(spPropertyType.getSelectedItemPosition()).getTypeOfPropertyId();
            bankId = caseSelectionsBankName.get(spBankName.getSelectedItemPosition()).getBankId();
            // caseAdminId = caseSelectionsCaseAdmin.get(spCaseAdmin.getSelectedItemPosition()).getEmployeeId();
            //reportMakerId = caseSelectionsReportMaker.get(spReportMaker.getSelectedItemPosition()).getEmployeeId();

            caseAdminId = spCaseAdminId;
            reportMakerId = spReportMakerId;

            int rmAgencyBranchId = reportMakerAgentId;
            int caAgencyBranchId = caseAdminAgentId;


            int subBranchId = 0;

           /* if (subBranchNameId != -1) {
                subBranchId = subBranchNameId;
            }*/

            String bankRefNo = "";

            if (!TextUtils.isEmpty(etBankNo.getText())) bankRefNo = etBankNo.getText().toString();

            createCase(applicantName, propertyId, bankId, caseAdminId, reportMakerId, SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGIN_ID, ""), subBranchId, bankRefNo, rmAgencyBranchId, caAgencyBranchId);
            dialog.dismiss();

        }
    }

    private void createCase(String applicantName, int propertyId, int bankId, int caseAdminId, int reportMakerId, String loginId, int subBranchId, String bankRef, int rmAgencyBranchId, int caAgencyBranchId) {

        if (Connectivity.isConnected(this)) {
            // general.showloading(this);
            shimmerHomeView.startShimmer();
            shimmerHomeView.setVisibility(View.VISIBLE);
            parentLayout.setVisibility(View.GONE);

            String url = general.ApiBaseUrl() + SettingsUtils.CreateCase;
            JsonRequestData requestData = new JsonRequestData();
            requestData.setUrl(url);
            requestData.setApplicantName(applicantName);
            requestData.setPropertyType("" + propertyId);
            requestData.setBankId("" + bankId);
            requestData.setModifiedBy(loginId);
            requestData.setCaseAdminId("" + caseAdminId);
            requestData.setReportMakerId("" + reportMakerId);
            requestData.setCaseAdminId("" + caseAdminId);
            requestData.setRmBranchId("" + rmAgencyBranchId);
            requestData.setCaBranchId("" + caAgencyBranchId);
            requestData.setAgencyBranchId(SettingsUtils.getInstance().getValue(SettingsUtils.BranchId, ""));

           /* if (subBranchId > 0) requestData.setSubBranchId("" + subBranchId);
            else requestData.setSubBranchId("");*/
            requestData.setSubBranchId("");

            requestData.setBankRefNo(bankRef);
            requestData.setRequestBody(RequestParam.CreateCaseNewRequestParams(requestData));
            requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));

            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(this, requestData, SettingsUtils.POST_TOKEN);
            webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                @Override
                public void onTaskComplete(JsonRequestData requestData) {
                    OpenWebserviceCall(requestData);
                }
            });
            webserviceTask.execute();
        } else {
            //general.hideloading();
            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
        }
    }

    private void OpenWebserviceCall(JsonRequestData requestData) {
        if (requestData.isSuccessful()) {
            StringBuilder sb = new StringBuilder();
            sb.append("createCase onTaskComplete: ");
            sb.append(requestData.getResponse());
            Log.e(TAG, sb.toString());
            //general.hideloading();
            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
            OpenCloseAPIWebserviceCall();

            general.customToast("Case created successfully", HomeActivity.this);
        } else if (!requestData.isSuccessful() && (requestData.getResponseCode() == 400 || requestData.getResponseCode() == 401)) {
            General.sessionDialog(HomeActivity.this);
        } else General.customToast(getString(R.string.something_wrong), HomeActivity.this);
    }

    private void getBankSelection(final Spinner spBankName, Integer typeOfPropertyId) {

        if (Connectivity.isConnected(this)) {
            //general.showloading(this);
            shimmerHomeView.startShimmer();
            shimmerHomeView.setVisibility(View.VISIBLE);
            parentLayout.setVisibility(View.GONE);
            String url = general.ApiBaseUrl() + SettingsUtils.GetBankSelections;
            JsonRequestData requestData = new JsonRequestData();
            requestData.setInitQueryUrl(url);
            Log.e(TAG, "getFreshCaseSelections: " + SettingsUtils.getInstance().getValue(SettingsUtils.BranchId, "") + "==>" + "<===" + SettingsUtils.getInstance().getValue(SettingsUtils.AgencyId, ""));
            requestData.setPropertyId("" + propertyId);
            requestData.setAgencyBranchId(SettingsUtils.getInstance().getValue(SettingsUtils.BranchId, ""));
            requestData.setAgencyId(SettingsUtils.getInstance().getValue(SettingsUtils.AgencyId, ""));
            requestData.setUrl(RequestParam.GetBankSelection(requestData));
            requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(this, requestData, SettingsUtils.GET_TOKEN);
            webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                @Override
                public void onTaskComplete(JsonRequestData requestData) {
                    try {

                        prepareBankList(spBankName, new Gson().fromJson(requestData.getResponse(), BankSelection.class), requestData.getResponseCode(), requestData.isSuccessful());
                    } catch (Exception e) {
                        // general.hideloading();
                        shimmerHomeView.stopShimmer();
                        shimmerHomeView.setVisibility(View.GONE);
                        parentLayout.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                }
            });
            webserviceTask.execute();
        } else {
            Connectivity.showNoConnectionDialog(this);
            // general.hideloading();
            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
        }
    }

    private void prepareBankList(Spinner spBankName, BankSelection fromJson, int responseCode, boolean successful) {
        if (successful) {
            caseSelectionsBankName = new ArrayList<>();
            caseSelectionsBankName.add(new BankSelection.Datum("Select bank"));
            caseSelectionsBankName.addAll(fromJson.getData());

            ArrayAdapter<BankSelection.Datum> arrayAdapter3 = new ArrayAdapter<>(this, R.layout.row_spinner_item_, caseSelectionsBankName);
            arrayAdapter3.setDropDownViewResource(R.layout.row_spinner_item_popup);
            spBankName.setAdapter(arrayAdapter3);

            // general.hideloading();
            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);

            spBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        bankId = caseSelectionsBankName.get(position).getBankId();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else if (!successful && (responseCode == 400 || responseCode == 401)) {
            General.sessionDialog(HomeActivity.this);
        } else {
            General.customToast(getString(R.string.something_wrong), HomeActivity.this);
        }
    }


    private void OfflineCasePopup() {
        Singleton.getInstance().AddSelectValue.clear();
        Singleton.getInstance().mCheckPosition.clear();
        final ArrayList<OfflineDataModel> offlineDataModels = getOfflineDisplayList();
        setSelectOfflinefromList();

        final Dialog dialog = new Dialog(HomeActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.offline_cases_popup);

        TextView popuptitle = (TextView) dialog.findViewById(R.id.title);
        TextView noOfflineCasestitle = (TextView) dialog.findViewById(R.id.noOfflineCasestitle);
        Button yesBtn = (Button) dialog.findViewById(R.id.yesBtn);
        Button noBtn = (Button) dialog.findViewById(R.id.noBtn);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.offlinecaserecyclerview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String selectedId = "";//Singleton.getInstance().indProperty.getIntFloorId();

        Collections.sort(offlineDataModels, new Comparator<OfflineDataModel>() {
            public int compare(OfflineDataModel obj1, OfflineDataModel obj2) {
                // ## Descending order
                return obj2.getCaseId().compareToIgnoreCase(obj1.getCaseId()); // To compare string values
            }
        });

        OfflineCaseCheckboxAdapter caseAdapter = null;
      /*  if (Singleton.getInstance().offlineCaseList.size() > 0) {
            caseAdapter = new OfflineCaseCheckboxAdapter(this, Singleton.getInstance().offlineCaseList, selectedId);
        } else {*/
        caseAdapter = new OfflineCaseCheckboxAdapter(this, offlineDataModels, selectedId);
        /* }*/

        if (offlineDataModels.size() > 0) {
            noOfflineCasestitle.setVisibility(View.GONE);
        } else {
            noOfflineCasestitle.setVisibility(View.VISIBLE);
        }
        recyclerView.setAdapter(caseAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        setRecyclerViewCheckboxClickListner(recyclerView);

        yesBtn.setTypeface(general.mediumtypeface());
        noBtn.setTypeface(general.mediumtypeface());
        popuptitle.setTypeface(general.mediumtypeface());

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = Singleton.getInstance().AddSelectValue.size();
                if (size > 0) {
                    LoadOfflineCaseAdapter(dialog, offlineDataModels);
                } else {
                    general.CustomToast(getResources().getString(R.string.select_one_case));
                }
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
      /*  Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);*/
    }

    private void LoadOfflineCaseAdapter(Dialog dialog, ArrayList<OfflineDataModel> offlineDataModels) {
        if (Connectivity.isConnected(HomeActivity.this)) {

            int offlineapicount = 0, offlineadaptercount = 0;
            if (!general.isEmpty(offlinecase_count)) {
                offlineapicount = Integer.parseInt(offlinecase_count);
                offlineadaptercount = offlineDataModelAdaptercount();
            }

            if (offlineadaptercount <= offlineapicount) {
                OfflineCaseList(offlineDataModels);
            } else {
                general.CustomToast(getResources().getString(R.string.alreadyexceeded));
            }
            dialog.dismiss();
        } else {
            Connectivity.showNoConnectionDialog(HomeActivity.this);
            dialog.dismiss();
        }
    }

    private int offlineDataModelAdaptercount() {
        ArrayList<OfflineDataModel> dataModel_offline = new ArrayList<>();
        if (appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(true).size() > 0) {
            dataModel_offline = (ArrayList<OfflineDataModel>) appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(true);
        }
        int datacount = dataModel_offline.size();
        return datacount;
    }


    private void checkBoxsaveRoomDB(ArrayList<OfflineDataModel> offlineDataModels) {
        int size = Singleton.getInstance().AddSelectValue.size();
        if (Singleton.getInstance().AddSelectValue.size() > 0) {
            OfflineCasesPipeSymbol();
            for (int i = 0; i < Singleton.getInstance().AddSelectValue.size(); i++) {
                String caseID = Singleton.getInstance().AddSelectValue.get(i);
                for (int j = 0; j < offlineDataModels.size(); j++) {
                    String offlineCaseID = offlineDataModels.get(j).getCaseId();
                    if (offlineCaseID.equalsIgnoreCase(caseID)) {
                        // selected value from the popup
                        appDatabase.interfaceOfflineDataModelQuery().updateOfflineDataModel(offlineDataModels.get(j).getCaseId(), true);
                        // updatecurrent_offlineDataModel as true
                        appDatabase.interfaceOfflineDataModelQuery().updatecurrent_offlineDataModel(offlineDataModels.get(j).getCaseId(), true);
                    }
                }
            }
        }
    }

    private void OfflineCasesPipeSymbol() {
        StringBuilder strbul = new StringBuilder();
        Iterator<String> iterator = Singleton.getInstance().AddSelectValue.iterator();
        //Iterator<String> iterator = AddSelectValue.iterator();
        while (iterator.hasNext()) {
            strbul.append(iterator.next());
            if (iterator.hasNext()) {
                strbul.append("|");
            }
        }
        String complaints = strbul.toString();
        Log.d("test OMG------>", complaints);
        SharedPreferences preferences = getSharedPreferences("SelectOfflineCase", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("checkboxitem", complaints); // Storing string
        editor.apply(); // commit changes
        Singleton.getInstance().CheckboxSelection.add(complaints);

    }


    // TODO onback function
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(parentLay, getResources().getString(R.string.exit_back), Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    /* circle Loading */
    public void call_circle_loading() {
        connectionDialog_circle = new Dialog(HomeActivity.this, R.style.CustomAlertDialog);
        connectionDialog_circle.setContentView(R.layout.progress_loading);
        connectionDialog_circle.setCancelable(false);
        connectionDialog_circle.show();

        mWaveLoadingView = (WaveLoadingView) connectionDialog_circle.findViewById(R.id.waveLoadingView);
        mWaveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
        mWaveLoadingView.setTopTitle("Offline...");
        mWaveLoadingView.setTopTitleColor(getResources().getColor(R.color.font_heading));
        mWaveLoadingView.setTopTitleSize(13);
        mWaveLoadingView.setCenterTitle("Moving of 1 / " + oflineCases_list.size());
        mWaveLoadingView.setCenterTitleColor(getResources().getColor(R.color.font_heading));
        mWaveLoadingView.setCenterTitleSize(13);
        /*int percentage_level_int = Math.round(percentage_level);
        Log.e("percentage_level", "percentage_level_first: " + percentage_level_int);
        mWaveLoadingView.setProgressValue(percentage_level_int);*/
        Log.e("percentage_level", "percentage_level_startcase: 10");
        mWaveLoadingView.setProgressValue(10);
        mWaveLoadingView.setBorderWidth(5);
        mWaveLoadingView.setAmplitudeRatio(60);
        mWaveLoadingView.setWaveBgColor(getResources().getColor(R.color.White));
        mWaveLoadingView.setWaveColor(getResources().getColor(R.color.colorPrimary));
        mWaveLoadingView.setBorderColor(getResources().getColor(R.color.colorPrimary));
        mWaveLoadingView.setAnimDuration(3000);
        mWaveLoadingView.startAnimation();
    }

    // TODO - If case move to Offline Api we want to hit these api for first time and move to offline tab
    // TODO - 1)Get Docs... 2)Start Inspection... 3) Get Photo...
    public void hit_all_offline_api(int case_id, int property_id) {
        if (Connectivity.isConnected(this)) {
            //general.showloading(mContext);
            Offline_Docs_Api(case_id, property_id);
        } else {
            Connectivity.showNoConnectionDialog(this);
            connectionDialog_circle.hide();
            // general.hideloading();
            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
        }
    }

    public void Offline_Docs_Api(final int case_id, final int property_id) {
        if (Connectivity.isConnected(this)) {
            // caseID declared
            SettingsUtils.getInstance().putValue(SettingsUtils.CASE_ID, "" + case_id);
            String url = general.ApiBaseUrl() + SettingsUtils.DocumentRead;
            JsonRequestData requestData = new JsonRequestData();
            requestData.setInitQueryUrl(url);
            requestData.setCaseId("" + case_id);//CaseId
            requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
            requestData.setUrl(RequestParam.DocumentReadRequestParams(requestData));
            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(this, requestData, SettingsUtils.GET_TOKEN);
            webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                @Override
                public void onTaskComplete(JsonRequestData requestData) {
                    parseDataResponse(requestData.getResponse(), requestData.getResponseCode(), requestData.isSuccessful(), case_id, property_id);

                }
            });
            webserviceTask.execute();
        } else {
            Connectivity.showNoConnectionDialog(this);
            connectionDialog_circle.hide();
            //general.hideloading();
            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
        }
    }

    private void parseDataResponse(String response, int responseCode, boolean successful, int case_id, int property_id) {

        if (successful) {
            DataResponse dataResponse = ResponseParser.parseDocumentReadResponse_offline(response);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("offline_api", "Offline_Docs_Api");
                    if ((oflineCases_list.size() - 1) == start_ofline_case) {
                        // apply for only last case is going > set progress bar as 95
                        Log.e("percentage_level", "percentage_level_lastcase: 95");
                        mWaveLoadingView.setProgressValue(95);
                    } else {
                        // apply for all cases
                        float percentage_increase = percentage_level_half + percentage_level_total;
                        int percentage_level_int = Math.round(percentage_increase);
                        Log.e("percentage_level", "percentage_level_first: " + percentage_level_int);
                        mWaveLoadingView.setProgressValue(percentage_level_int);
                    }
                    Offline_StartInspection_Api(case_id, property_id);
                }
            }, 500);
        } else if (!successful && (responseCode == 400 || responseCode == 401)) {
            General.sessionDialog(HomeActivity.this);
        } else {
            General.customToast(getString(R.string.something_wrong), HomeActivity.this);
        }
    }

    public void Offline_StartInspection_Api(final int case_id, final int property_id) {
        if (Connectivity.isConnected(this)) {
            // caseID declared
            SettingsUtils.getInstance().putValue(SettingsUtils.CASE_ID, "" + case_id);
            String url = general.ApiBaseUrl() + SettingsUtils.EditCaseInspection;
            JsonRequestData requestData = new JsonRequestData();
            requestData.setInitQueryUrl(url);
            requestData.setId("" + case_id); // id
            requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
            requestData.setUrl(RequestParam.EditInspectionRequestParams(requestData));
            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(this, requestData, SettingsUtils.GET_TOKEN);
            webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                @Override
                public void onTaskComplete(JsonRequestData requestData) {
                    passDataResponse(requestData.getResponse(), requestData.getResponseCode(), requestData.isSuccessful(), property_id);
                }
            });
            webserviceTask.execute();
        } else {
            Connectivity.showNoConnectionDialog(this);
            connectionDialog_circle.hide();
            //general.hideloading();
            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
        }
    }

    private void passDataResponse(String response, int responseCode, boolean successful, int property_id) {
        if (successful) {
            DataResponse dataResponse = ResponseParser.parseEditInspectionResponse_offline(response);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("offline_api", "Offline_StartInspection_Api");
                    Offline_Photo_Api(property_id);
                }
            }, 500);
        } else if (!successful && (responseCode == 400 || responseCode == 401)) {
            General.sessionDialog(HomeActivity.this);
        } else {
            General.customToast(getString(R.string.something_wrong), HomeActivity.this);
        }
    }

    public void Offline_Photo_Api(final int property_id) {
        if (Connectivity.isConnected(this)) {
            JsonRequestData data = new JsonRequestData();
            String s = SettingsUtils.GETIMAGE;
            data.setUrl(general.ApiBaseUrl() + SettingsUtils.GETIMAGE + property_id);
            data.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(this, data, SettingsUtils.GET_TOKEN);
            webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                @Override
                public void onTaskComplete(JsonRequestData requestData) {

                    offLineDataResponse(requestData.getResponse(), requestData.getResponseCode(), requestData.isSuccessful(), property_id);

                    Log.e("offline_api", "Offline_Photo_Api");

                    String response = requestData.getResponse();
                    //int PropertyId_is = Integer.parseInt(property_id);
                    int PropertyId_is = property_id;
                    ArrayList<GetPhoto> GetPhoto_list_response = new ArrayList<>();

                    // Room Delete
                    appDatabase.interfaceGetPhotoQuery().deleteRow(PropertyId_is);

                    GetPhoto_list_response = new ArrayList<>();
                    GetPhoto dummy_cameraImage = new GetPhoto();
                    dummy_cameraImage.setDefaultimage(true);
                    dummy_cameraImage.setPropertyId(PropertyId_is);
                    Gson gson_dummy_camera = new Gson();
                    gson_dummy_camera.toJson(dummy_cameraImage);
                    GetPhoto_list_response.add(dummy_cameraImage);
                    // Room Add
                    appDatabase.interfaceGetPhotoQuery().insertAll(dummy_cameraImage);

                    GetPhoto dummy_galleryImage = new GetPhoto();
                    dummy_galleryImage.setDefaultimage(true);
                    dummy_galleryImage.setPropertyId(PropertyId_is);
                    Gson gson_dummy_gallery = new Gson();
                    gson_dummy_gallery.toJson(dummy_galleryImage);
                    GetPhoto_list_response.add(dummy_galleryImage);
                    // Room Add
                    appDatabase.interfaceGetPhotoQuery().insertAll(dummy_galleryImage);

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
                                        appDatabase.interfaceGetPhotoQuery().insertAll(getPhoto);
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if ((oflineCases_list.size() - 1) == start_ofline_case) {
                                // hide loading

                                general.CustomToast(oflineCases_list.size() + " cases moved to offline");

                                ArrayList<OfflineDataModel> dataModel_offline = new ArrayList<>();
                                dataModel_offline = (ArrayList<OfflineDataModel>) appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(true);

                                Collections.sort(dataModel_offline, new Comparator<OfflineDataModel>() {
                                    public int compare(OfflineDataModel obj1, OfflineDataModel obj2) {
                                        // ## Descending order
                                        return obj2.getCaseId().compareToIgnoreCase(obj1.getCaseId()); // To compare string values
                                    }
                                });

                                loadOfflineCaseAdapter(dataModel_offline);
                                if (dataModel_offline.size() > 0) {
                                    OfflineLayoutClick();
                                }
//                                GoOfflineLayout.setBackground(getResources().getDrawable(R.drawable.buttonshape));

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        check_offline_button_function();
                                        //general.hideloading();
                                        shimmerHomeView.stopShimmer();
                                        shimmerHomeView.setVisibility(View.GONE);
                                        parentLayout.setVisibility(View.VISIBLE);
                                        connectionDialog_circle.hide();
                                    }
                                }, 1000);


                            } else {
                                percentage_level_total = percentage_level_total + percentage_level;
                                int percentage_level_int = Math.round(percentage_level_total);
                                Log.e("percentage_level", "percentage_level_first: " + percentage_level_int);
                                mWaveLoadingView.setProgressValue(percentage_level_int);
                                // set a one as default value
                                start_ofline_case = start_ofline_case + 1;
                                mWaveLoadingView.setCenterTitle("Moving of " + (start_ofline_case + 1) + " / " + oflineCases_list.size());
                                // Hit all offline api and save it in local
                                hit_all_offline_api(oflineCases_list.get(start_ofline_case).getCaseId(), oflineCases_list.get(start_ofline_case).getPropertyId());
                            }
                        }
                    }, 500);

                }
            });
            webserviceTask.execute();
        } else {
            Connectivity.showNoConnectionDialog(this);
            connectionDialog_circle.hide();
            //general.hideloading();
            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
        }
    }

    private void offLineDataResponse(String response, int responseCode, boolean successful, int property_id) {


        Log.e("offline_api", "Offline_Photo_Api");
        //int PropertyId_is = Integer.parseInt(property_id);
        int PropertyId_is = property_id;
        ArrayList<GetPhoto> GetPhoto_list_response = new ArrayList<>();

        // Room Delete
        appDatabase.interfaceGetPhotoQuery().deleteRow(PropertyId_is);

        GetPhoto_list_response = new ArrayList<>();
        GetPhoto dummy_cameraImage = new GetPhoto();
        dummy_cameraImage.setDefaultimage(true);
        dummy_cameraImage.setPropertyId(PropertyId_is);
        Gson gson_dummy_camera = new Gson();
        gson_dummy_camera.toJson(dummy_cameraImage);
        GetPhoto_list_response.add(dummy_cameraImage);
        // Room Add
        appDatabase.interfaceGetPhotoQuery().insertAll(dummy_cameraImage);

        GetPhoto dummy_galleryImage = new GetPhoto();
        dummy_galleryImage.setDefaultimage(true);
        dummy_galleryImage.setPropertyId(PropertyId_is);
        Gson gson_dummy_gallery = new Gson();
        gson_dummy_gallery.toJson(dummy_galleryImage);
        GetPhoto_list_response.add(dummy_galleryImage);
        // Room Add
        appDatabase.interfaceGetPhotoQuery().insertAll(dummy_galleryImage);

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
                            appDatabase.interfaceGetPhotoQuery().insertAll(getPhoto);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ((oflineCases_list.size() - 1) == start_ofline_case) {
                    // hide loading

                    general.CustomToast(oflineCases_list.size() + " cases moved to offline");

                    ArrayList<OfflineDataModel> dataModel_offline = new ArrayList<>();
                    dataModel_offline = (ArrayList<OfflineDataModel>) appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(true);

                    Collections.sort(dataModel_offline, new Comparator<OfflineDataModel>() {
                        public int compare(OfflineDataModel obj1, OfflineDataModel obj2) {
                            // ## Descending order
                            return obj2.getCaseId().compareToIgnoreCase(obj1.getCaseId()); // To compare string values
                        }
                    });

                    loadOfflineCaseAdapter(dataModel_offline);
                    if (dataModel_offline.size() > 0) {
                        OfflineLayoutClick();
                    }
//                                GoOfflineLayout.setBackground(getResources().getDrawable(R.drawable.buttonshape));

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            check_offline_button_function();
                            //general.hideloading();
                            shimmerHomeView.stopShimmer();
                            shimmerHomeView.setVisibility(View.GONE);
                            parentLayout.setVisibility(View.VISIBLE);
                            connectionDialog_circle.hide();
                        }
                    }, 1000);


                } else {
                    percentage_level_total = percentage_level_total + percentage_level;
                    int percentage_level_int = Math.round(percentage_level_total);
                    Log.e("percentage_level", "percentage_level_first: " + percentage_level_int);
                    mWaveLoadingView.setProgressValue(percentage_level_int);
                    // set a one as default value
                    start_ofline_case = start_ofline_case + 1;
                    mWaveLoadingView.setCenterTitle("Moving of " + (start_ofline_case + 1) + " / " + oflineCases_list.size());
                    // Hit all offline api and save it in local
                    hit_all_offline_api(oflineCases_list.get(start_ofline_case).getCaseId(), oflineCases_list.get(start_ofline_case).getPropertyId());
                }
            }
        }, 500);

        if (!successful && (responseCode == 400 || responseCode == 401)) {
            General.sessionDialog(HomeActivity.this);
        } else {
            // General.customToast(getString(R.string.something_wrong), HomeActivity.this);
        }

    }

    private void check_offline_button_function() {
//        GoOfflineLayout.setBackground(getResources().getDrawable(R.drawable.buttonshape));
        int offlineapicount = 0, offlineadaptercount = 0;
        if (!general.isEmpty(offlinecase_count)) {
            // Offline count from api
            offlineapicount = Integer.parseInt(offlinecase_count);
            Log.e("offline_check;", "1: " + offlineapicount);
            // offline case in offline tab
            offlineadaptercount = offlineDataModelAdaptercount();
            Log.e("offline_check", "2: " + offlineadaptercount);
            if (offlineadaptercount <= offlineapicount) {
                final ArrayList<OfflineDataModel> offlineDataModels = getOfflineDisplayList();
                // No of case in offline list
                Log.e("offline_check", "3: " + offlineDataModels.size());
                if (offlineDataModels.size() == 0) {
//                    GoOfflineLayout.setBackground(getResources().getDrawable(R.drawable.buttonshape_grey));
                }
            } else {
                Log.e("offline_check", "4: ");
//                GoOfflineLayout.setBackground(getResources().getDrawable(R.drawable.buttonshape_grey));
            }
        }
    }

    @Override
    public void transfer(String caseId, String applicantName) {
        Log.e(TAG, "transfer: caseId==>" + caseId + "applicantName==>" + applicantName);
        getTransferCaseSelection(caseId, applicantName);
    }

    private void getTransferCaseSelection(String caseId, String applicantName) {

        if (Connectivity.isConnected(this)) {
            // general.showloading(this);
            shimmerHomeView.startShimmer();
            shimmerHomeView.setVisibility(View.VISIBLE);
            parentLayout.setVisibility(View.GONE);
            String url = general.ApiBaseUrl() + SettingsUtils.GetByFieldStaffByCity;
            JsonRequestData requestData = new JsonRequestData();
            requestData.setInitQueryUrl(url);
            requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
            Log.e(TAG, "getFreshCaseSelections: " + SettingsUtils.getInstance().getValue(SettingsUtils.BranchId, "") + "==>" + "<===" + SettingsUtils.getInstance().getValue(SettingsUtils.AgencyId, ""));
            requestData.setAgencyId(SettingsUtils.getInstance().getValue(SettingsUtils.AgencyId, ""));
            requestData.setRoleId("5");
            requestData.setAgencyBranchId(SettingsUtils.getInstance().getValue(SettingsUtils.BranchId, ""));
            requestData.setUrl(RequestParam.GetByFieldStaffByCity(requestData));
            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(this, requestData, SettingsUtils.GET_TOKEN);
            webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                @Override
                public void onTaskComplete(JsonRequestData requestData) {
                    try {
                        showCaseTransferPopup(requestData.getResponse(), requestData.getResponseCode(), requestData.isSuccessful(), caseId, applicantName);
                    } catch (Exception e) {
                        //general.hideloading();
                        shimmerHomeView.stopShimmer();
                        shimmerHomeView.setVisibility(View.GONE);
                        parentLayout.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                }
            });
            webserviceTask.execute();
        } else {
            Connectivity.showNoConnectionDialog(this);
        }
    }

    private void showCaseTransferPopup(String requestData, int responseCode, boolean successful, String caseId, String applicantName) {
        // general.hideloading();
        shimmerHomeView.stopShimmer();
        shimmerHomeView.setVisibility(View.GONE);
        parentLayout.setVisibility(View.VISIBLE);
        if (successful) {
            FieldStaff fromJson = new Gson().fromJson(requestData, FieldStaff.class);
            final ArrayList<FieldStaff.Datum> arrayListFieldStaff = new ArrayList<>();
            arrayListFieldStaff.add(0, new FieldStaff.Datum("Select staff"));
            arrayListFieldStaff.addAll(fromJson.getData());
            final Dialog dialog = new Dialog(HomeActivity.this, R.style.CustomDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.popup_transfercase);
            TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
            TextView tvFieldStaff = (TextView) dialog.findViewById(R.id.tvFieldStaff);
            final Spinner spFieldStaff = (Spinner) dialog.findViewById(R.id.spinner_FieldStaff);
            Button btnYes = (Button) dialog.findViewById(R.id.yesBtn);
            Button btnNo = (Button) dialog.findViewById(R.id.noBtn);
            btnYes.setTypeface(general.mediumtypeface());
            btnNo.setTypeface(general.mediumtypeface());
            tvFieldStaff.setTypeface(General.regularTypeface());
            tvMessage.setTypeface(General.regularTypeface());
            tvMessage.setText("You want to transfer " + applicantName + " case to");

            ArrayAdapter<FieldStaff.Datum> arrayAdapter = new ArrayAdapter<>(this, R.layout.row_spinner_item_, arrayListFieldStaff);
            arrayAdapter.setDropDownViewResource(R.layout.row_spinner_item_popup);
            spFieldStaff.setAdapter(arrayAdapter);
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (spFieldStaff.getSelectedItemPosition() > 0) {
                        transferCase(caseId, arrayListFieldStaff.get(spFieldStaff.getSelectedItemPosition()).getEmployeeId(), SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGIN_ID, ""), arrayListFieldStaff.get(spFieldStaff.getSelectedItemPosition()).getAgencyBranchId());
                        dialog.dismiss();
                    } else {
                        general.customToast("Select field staff for transfer your case!", HomeActivity.this);
                    }
                }
            });

            dialog.show();
        } else if (!successful && (responseCode == 400 || responseCode == 401)) {
            General.sessionDialog(HomeActivity.this);
        } else {
            General.customToast(getString(R.string.something_wrong), HomeActivity.this);
        }
    }

    private void transferCase(String caseId, Integer empId, String loginId, Integer agencyBranchId) {

        if (Connectivity.isConnected(this)) {
            // General.showloading(this);
            shimmerHomeView.startShimmer();
            shimmerHomeView.setVisibility(View.VISIBLE);
            parentLayout.setVisibility(View.GONE);
            JsonRequestData requestData = new JsonRequestData();
            String url = general.ApiBaseUrl() + SettingsUtils.TransferCase;
            requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
            requestData.setUrl(url);
            requestData.setCaseId(caseId);
            requestData.setAgencyBranchId("" + agencyBranchId);
            requestData.setAssignedTo(String.valueOf(empId));
            requestData.setModifiedBy(loginId);
            requestData.setRequestBody(RequestParam.TransferCase(requestData));
            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(this, requestData, SettingsUtils.POST_TOKEN);
            webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                public void onTaskComplete(JsonRequestData requestData) {
                    transferResponse(requestData.getResponse(), requestData.getResponseCode(), requestData.isSuccessful(), caseId);
                }
            });
            webserviceTask.execute();
        } else {
            Connectivity.showNoConnectionDialog(this);
        }
    }

    private void transferResponse(String response, int responseCode, boolean successful, String caseId) {
        if (successful) {
            StringBuilder sb = new StringBuilder();
            sb.append("tranferCase onTaskComplete: ");
            sb.append(response);
            Log.e("HomeActivity", sb.toString());
            DeleteOfflineDatabyCaseID(caseId);
            //general.hideloading();
            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
            OpenCloseAPIWebserviceCall();
        } else if (!successful && (responseCode == 400 || responseCode == 401)) {
            //general.hideloading();
            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
            General.sessionDialog(HomeActivity.this);
        } else {
            //general.hideloading();
            shimmerHomeView.stopShimmer();
            shimmerHomeView.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
            General.customToast(getString(R.string.something_wrong), HomeActivity.this);
        }
    }

    private void DeleteOfflineDatabyCaseID(String caseId) {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(this);
        StringBuilder sb = new StringBuilder();
        sb.append("DeleteOfflineDatabyCaseID: ");
        sb.append(caseId);
        String str = "HomeActivity";
        Log.e(str, sb.toString());
        if (MyApplication.getAppContext() != null && appDatabase.interfaceOfflineDataModelQuery().getDataModels().size() > 0) {
            int value = appDatabase.interfaceOfflineDataModelQuery().DeleteOfflineDatabyCaseid(caseId);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("delete offline case row");
            sb2.append(value);
            sb2.append("");
            Log.e(str, sb2.toString());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            general.customToast("Turn ON location to access this application", this);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isGPS) {
                        new GpsUtils(HomeActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
                            public final void gpsStatus(boolean isGPSEnable) {
                                isGPS = isGPSEnable;
                            }
                        });
                    }
                }
            }, 1000);
        } else if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SettingsUtils.GPS_REQUEST) {
                isGPS = true;
                makeLocationUpadte();
            }else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

                try {
                    File imgFile = new File(SettingsUtils.mPhotoPath);
                    Uri.fromFile(imgFile);
                    Log.e("PathNew :", SettingsUtils.mPhotoPath);
                    try {
                        File compressedImageFile = new Compressor(this).compressToFile(imgFile);
                        if (!general.isEmpty(SettingsUtils.mPhotoPath)) {
                            Log.e(TAG, "onActivityResult: " + compressedImageFile.getAbsolutePath());
                            convertToBase64(compressedImageFile.getAbsolutePath());
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {


                /***** From Image Adapter *****/
                ImageAdapter imageAdapter = TicketRaisePhotoPickerActivity.imageAdapter;
                Log.e(TAG, "show 1538");
                for (int i = 0; i < imageAdapter.getCheckedItems().size(); i++) {
                    Log.e(TAG, "onActivityResult: " + imageAdapter.getCheckedItems().get(i));
                    try {

                        File imgFile = new File(imageAdapter.getCheckedItems().get(i));

                        Log.d(TAG, "onActivityResult: " + imgFile.getAbsolutePath());

                        File compressedImageFile = new Compressor(this).compressToFile(imgFile);
                        convertToBase64(compressedImageFile.getAbsolutePath());

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    if (i == imageAdapter.getCheckedItems().size() - 1) {
                        Log.e(TAG, "Hide 1564");
                    }
                }
                //noinspection StatementWithEmptyBody
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                }

            }

        }


    }

    @Override
    public void sendLocationUpdate(double latitude, double longitude) {
        StringBuilder sb = new StringBuilder();
        sb.append("sendLocationUpdate: ");
        sb.append(latitude);
        Log.e("HomeActivity", sb.toString());
        locationTrackerApi.shareLocation("", SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGIN_ID, ""), "Interval",
                Double.parseDouble(SettingsUtils.getInstance().getValue("lat", "")),
                Double.parseDouble(SettingsUtils.getInstance().getValue("long", "")), "", 0);
        workManager.startWorker();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted

                    if (checkGps()) {
                        makeLocationUpadte();
                    }
//                    settingAndFireRemote();

                } else {
                    general.customToast("Please enable all permissions to complete access of this application", this);

                  /*  new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            general.checkPermissions();
                        }
                    }, 500);*/

                }


        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        if (e instanceof ApiException) {
            // An error with the Google Play Services API contains some additional details.
            ApiException apiException = (ApiException) e;
            Log.d(TAG, "Error: " + CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()) + ": " + apiException.getStatusMessage());
        } else {
            // A different, unknown type of error occurred.
            Log.d(TAG, "ERROR! " + e.getMessage());
        }
    }

    @Override
    public void onSuccess(@NonNull SafetyNetApi.AttestationResponse attestationResponse) {
        SafeNetModel sNet = SafetyNetChecker.decodeJWS(attestationResponse.getJwsResult());
        if (sNet.getBasicIntegrity() && sNet.getCtsProfileMatch()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initiateViewsAndData();
                }
            });

        } else {
            General.exitDialog(HomeActivity.this, "Your phone is rooted,\nPlease unroot to use " + "app");
        }
    }


    private void initSubBranch(TextView tSubBranch) {
        tSubBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> arrayList;
                TextView textViewSpinner;
                Dialog subBranchDialog = new Dialog(HomeActivity.this, R.style.CustomDialog);
                subBranchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                subBranchDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                subBranchDialog.setContentView(R.layout.search_drop_down);

                subBranchDialog.show();

                EditText editText = subBranchDialog.findViewById(R.id.editText_of_searchableSpinner);
                ListView listView = subBranchDialog.findViewById(R.id.listView_of_searchableSpinner);
                //array adapter
                ArrayAdapter<SubBranchModel.Datum> arrayAdapter = new ArrayAdapter<SubBranchModel.Datum>(HomeActivity.this, R.layout.row_spinner_item_popup, subBranchList);
                listView.setAdapter(arrayAdapter);

                ImageView closeBtn = subBranchDialog.findViewById(R.id.close);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subBranchDialog.dismiss();
                    }
                });

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        //filter arraylist
                        arrayAdapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        tSubBranch.setText(arrayAdapter.getItem(i).getSubBranch());
                        subBranchName = arrayAdapter.getItem(i).getSubBranch();
                        subBranchNameId = arrayAdapter.getItem(i).getId();
                        subBranchDialog.dismiss();
                    }
                });
            }
        });
    }

    private void initCaseAdimn(TextView spCaseAdmin) {
        spCaseAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> arrayList;
                TextView textViewSpinner;
                Dialog spCaseAdminDialog = new Dialog(HomeActivity.this, R.style.CustomDialog);
                spCaseAdminDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                spCaseAdminDialog.setContentView(R.layout.search_drop_down);

                spCaseAdminDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                spCaseAdminDialog.show();

                EditText editText = spCaseAdminDialog.findViewById(R.id.editText_of_searchableSpinner);
                ListView listView = spCaseAdminDialog.findViewById(R.id.listView_of_searchableSpinner);

                editText.setHint("Search Case Admin");
                TextView title = spCaseAdminDialog.findViewById(R.id.title);
                title.setText("Select Case Admin");


                ImageView closeBtn = spCaseAdminDialog.findViewById(R.id.close);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spCaseAdminDialog.dismiss();
                    }
                });


                //array adapter
                ArrayAdapter<CaseSelection.Data.CaseAdmin> spCaseAdminAdapter = new ArrayAdapter<CaseSelection.Data.CaseAdmin>(HomeActivity.this, R.layout.row_spinner_item_popup, caseSelectionsCaseAdmin);
                listView.setAdapter(spCaseAdminAdapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        //filter arraylist
                        spCaseAdminAdapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        spCaseAdminName = spCaseAdminAdapter.getItem(i).getFullName();
                        spCaseAdmin.setText(spCaseAdminAdapter.getItem(i).getFullName());
                        spCaseAdminId = spCaseAdminAdapter.getItem(i).getEmployeeId();
                        caseAdminAgentId = spCaseAdminAdapter.getItem(i).getAgencyBranchId();
                        spCaseAdminDialog.dismiss();
                    }
                });
            }
        });
    }


    private void initReportMaker(TextView spReportMaker) {
        spReportMaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> arrayList;
                TextView textViewSpinner;
                Dialog spReportMakerDialog = new Dialog(HomeActivity.this, R.style.CustomDialog);
                spReportMakerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                spReportMakerDialog.setContentView(R.layout.search_drop_down);

                spReportMakerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                spReportMakerDialog.show();

                EditText editText = spReportMakerDialog.findViewById(R.id.editText_of_searchableSpinner);
                editText.setHint("Search Report Maker");
                TextView title = spReportMakerDialog.findViewById(R.id.title);
                title.setText("Select Report Maker");


                ImageView closeBtn = spReportMakerDialog.findViewById(R.id.close);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spReportMakerDialog.dismiss();
                    }
                });

                ListView listView = spReportMakerDialog.findViewById(R.id.listView_of_searchableSpinner);
                //array adapter
                ArrayAdapter<CaseSelection.Data.ReportMaker> spReportMakerAdapter = new ArrayAdapter<CaseSelection.Data.ReportMaker>(HomeActivity.this, R.layout.row_spinner_item_popup, caseSelectionsReportMaker);
                listView.setAdapter(spReportMakerAdapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        //filter arraylist
                        spReportMakerAdapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        spReportMakerName = spReportMakerAdapter.getItem(i).getFullName();
                        spReportMaker.setText(spReportMakerAdapter.getItem(i).getFullName());
                        reportMakerAgentId = spReportMakerAdapter.getItem(i).getAgencyBranchId();
                        spReportMakerId = spReportMakerAdapter.getItem(i).getEmployeeId();
                        spReportMakerDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        general.isShowDialog();
        super.onDestroy();
    }


    private void getCurrentLocation(Activity activity) {

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

                            general.LogoutDialog(HomeActivity.this, SettingsUtils.Longitudes, SettingsUtils.Latitudes);
                        }
                    }
                }, 1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getNonCaseActivity() {
        Intent intent = new Intent(HomeActivity.this, NonCaseActivity.class);
        startActivity(intent);
    }

    private void getConvyenceReport() {
        Intent intent = new Intent(HomeActivity.this, ConvenyanceReport.class);
        startActivity(intent);
    }

    private void raiseTickerPopup() {
        if (Connectivity.isConnected(HomeActivity.this)) {
            String ticketQueryResponse = SettingsUtils.getInstance().getValue(SettingsUtils.TicketQuery, "");
            if (ticketQueryResponse == null || ticketQueryResponse.isEmpty()) {
                InitiateGetTicketQueryDropDownTask();
            } else {
                initiateTicketQueryPopup();
            }
        } else {
            General.customToast("Internet Connection Is Required", HomeActivity.this);
        }
    }


    private void getViewTicketSystem() {
        Intent intent = new Intent(HomeActivity.this, ViewRaisedTicketsActivity.class);
        startActivity(intent);
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.home_screen;
    }


    public void LogoutDialog(Activity activity, double longitudes, double latitudes) {
        View view = activity.getLayoutInflater().inflate(R.layout.save_pop_up, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomDialog);
        builder.setView(view);

        savePopup = builder.create();
        savePopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = view.findViewById(R.id.title);
        title.setText("Are you sure you want to Logout?");
        Button btnSubmit = view.findViewById(R.id.btn_save);
        btnSubmit.setText("YES");
        Button btnCancel = view.findViewById(R.id.btn_no);
        btnCancel.setText("NO");
        savePopup.setCancelable(false);
        savePopup.setCanceledOnTouchOutside(false);
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            savePopup.show();
        }
        btnSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         /*Singleton.getInstance().longitude = 0.0;
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
//                        SettingsUtils.getInstance().putValue(SettingsUtils.KEY_LOGGED_IN, false);
                         AppDatabase appDatabase = AppDatabase.getAppDatabase(MyApplication.getAppContext());
                         ArrayList<OfflineDataModel> oflineData = (ArrayList) appDatabase.interfaceOfflineDataModelQuery().getDataModal_offlinecase(true);
                         if (appDatabase == null) {
                             return;
                         }
                         if (oflineData.size() > 0) {
                             General.customToast("Please sync your offline documents before logout!", activity);
                             return;
                         } else
                             SettingsUtils.getInstance().putValue(SettingsUtils.KEY_LOGGED_IN, false);

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


                         if (new LocationTrackerApi(activity).shareLocation("",
                                 SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGIN_ID, ""),
                                 "Logout", latitudes, longitudes, "", 4)) {
                             if (Build.VERSION.SDK_INT < 26) {
                                 activity.stopService(new Intent(activity, GeoUpdate.class));
                             } else {
                                 new OreoLocation(activity).stopOreoLocationUpdates();
                             }
                             new WorkerManager(activity).stopWorker();

                             Intent intent = new Intent(mContext, LoginActivity.class);
                             intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                             intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                             mContext.startActivity(intent);
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

                        General.showloading(HomeActivity.this);
                        general.getCurrentLocationOfUser(activity);
                    }
                });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePopup.cancel();
            }
        });

    }


    private void InitiateGetTicketQueryDropDownTask() {
        String url = general.ApiBaseUrl() + SettingsUtils.getTicketQuery;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(HomeActivity.this,
                requestData, SettingsUtils.GET);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {

                if (requestData.isSuccessful()) {
                    TicketQueryDataModel ticketQueryDataModel = new Gson().fromJson(requestData.getResponse(), TicketQueryDataModel.class);
                    String result = "";
                    if (ticketQueryDataModel != null) {
                        SettingsUtils.getInstance().putValue(SettingsUtils.TicketQuery, requestData.getResponse());
                        initiateTicketQueryPopup();
                    }
                } else {

                }
            }
        });
        webserviceTask.execute();
    }


    public static interface HomeClickListener {
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }

    private void initiateTicketQueryPopup() {


        final Dialog dialog = new Dialog(HomeActivity.this, R.style.raiseTicket);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.activity_ticket_raise_system);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        Button btn = dialog.findViewById(R.id.btnSubmit);

        final EditText etOther = dialog.findViewById(R.id.et_other);
        final Spinner spTicketQuery = dialog.findViewById(R.id.spinnerTicketQuery);
        final EditText etDescritpion = dialog.findViewById(R.id.etDescritpion);
        final EditText etEmail = dialog.findViewById(R.id.et_email);
        final EditText etSapID = dialog.findViewById(R.id.et_sapID);
        final EditText etContact = dialog.findViewById(R.id.et_contactNumber);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValidate = general.createTicketValidation(querySpinnerPosition, etOther, etDescritpion, etEmail, etSapID, etContact
                        , GetPhoto_list_response, HomeActivity.this
                );
                if (isValidate) {
                    General.showloading(HomeActivity.this);
                    TicketRaisePhoto ticketRaisePhoto = new TicketRaisePhoto();
                    ticketRaisePhoto.setQueryType(querySpinnerPosition);
                    ticketRaisePhoto.setTicketStatus("1");
                    if(etOther.getVisibility() == View.VISIBLE){
                        ticketRaisePhoto.setOtherQueries(etOther.getText().toString().trim());
                    }
                    ticketRaisePhoto.setDescription(etDescritpion.getText().toString().trim());
                    ticketRaisePhoto.setEmailId(etEmail.getText().toString().trim());
                    ticketRaisePhoto.setSAPID(etSapID.getText().toString().trim());
                    ticketRaisePhoto.setContactNumber(etContact.getText().toString().trim());
                    ArrayList<TicketRaisePhoto.Datum> data = new ArrayList<>();
                    ArrayList<GetPhoto> photoResposne = new ArrayList<>();

                    photoResposne = GetPhoto_list_response;

                    if (photoResposne.size() > 2) {
                        for (int i = 2; i < photoResposne.size(); i++) {
                            TicketRaisePhoto.Datum datum = new TicketRaisePhoto.Datum();
                            datum.setFileName(photoResposne.get(i).getFileName());
                            datum.setId(photoResposne.get(i).getId());
                            datum.setImage(photoResposne.get(i).getLogo());
                            datum.setTitle(photoResposne.get(i).getTitle());
                            data.add(datum);

                        }
                        ticketRaisePhoto.setTicketImages(data);
                        createTicketApi(ticketRaisePhoto, dialog);
                    }


                }

            }
        });


        GetPhoto_list_response = general.createStaticImage();

        ticketRaiseImageAdapter = new TicketRaiseImageAdapter(general, this, GetPhoto_list_response);


        final RecyclerView recyclerView = dialog.findViewById(R.id.rv_image);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(ticketRaiseImageAdapter);

        recyclerView.addOnItemTouchListener(new HomeRecyclerTouchListener(this, recyclerView, new HomeClickListener() {

            @Override
            public void onClick(View view, int position) {
                if (getAvailableMemory()) {
                    Log.e("can pick image :", getAvailableMemory() + "");
                    if (position == 0) {
                        if (checkPermissions())
                            if (GetPhoto_list_response.size() <= 3) {
                                TakePicture();
                            } else {
                                general.CustomToast(getResources().getString(R.string.tiket_raise_image_size_limit));
                            }
                    } else if (position == 1) {
                        if (checkPermissions()) {
                                /*if (GetPhoto_list_response.size() <= 7) {
                                            Intent gallery_select = new Intent(LoginActivity.this, MultiPhotoSelectActivity.class);
                                   // gallery_select.putExtra("available_photo_size",GetPhoto_list_response.size()-2);
                                    gallery_select.putExtra("TicketRaiseImage",true);
                                    startActivityForResult(gallery_select, GALLERY_REQUEST);
                                } else {
                                    general.CustomToast(getResources().getString(R.string.tiket_raise_image_size_limit));
                                }*/
                            if (GetPhoto_list_response.size() <= 3) {
                                Intent gallery_select = new Intent(HomeActivity.this, TicketRaisePhotoPickerActivity.class);
                                gallery_select.putExtra("available_photo_size", GetPhoto_list_response.size() - 2);
                                startActivityForResult(gallery_select, GALLERY_REQUEST);
                            } else {
                                general.CustomToast(getResources().getString(R.string.tiket_raise_image_size_limit));
                            }


                        }
                    }
                } else {
                    general.customToast("Please delete some images in your device, to add new image", HomeActivity.this);
                    Log.e("cannot pick image:", getAvailableMemory() + "");
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        String ticketQueryDropDown = SettingsUtils.getInstance().getValue(SettingsUtils.TicketQuery, "");
        TicketQueryDataModel ticketQueryDataModel = new Gson().fromJson(ticketQueryDropDown, TicketQueryDataModel.class);

        ArrayList<TicketQueryDataModel.Data> ticketData = new ArrayList();
        ticketData.add(new TicketQueryDataModel.Data("Select TicketQuery"));
        ticketData.addAll(ticketQueryDataModel.getData());

        ArrayAdapter<TicketQueryDataModel.Data> arrayAdapter3 = new ArrayAdapter<>(this, R.layout.row_spinner_item_, ticketData);
        arrayAdapter3.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spTicketQuery.setAdapter(arrayAdapter3);

        spTicketQuery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                querySpinnerPosition = ticketData.get(i).getID();


                String other = ticketData.get(i).getName();

                if (other != null && !other.isEmpty() && other.equalsIgnoreCase("Others")) {
                    etOther.setVisibility(View.VISIBLE);
                } else {
                    etOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (Build.VERSION.SDK_INT < 33) {
            for (String p : permissions) {
                result = ContextCompat.checkSelfPermission(this, p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
        } else {
            for (String p : androidHigherVersionPermission) {
                result = ContextCompat.checkSelfPermission(this, p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
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
        return available;
    }


    private void createTicketApi(TicketRaisePhoto ticketRaisePhoto, Dialog dialog) {
        String url = general.ApiBaseUrl() + SettingsUtils.createQuery;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);
        requestData.setMainJson(new Gson().toJson(ticketRaisePhoto));
        requestData.setRequestBody(RequestParam.SaveCaseInspectionRequestParams(requestData));
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(HomeActivity.this,
                requestData, SettingsUtils.POST);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {

                try {
                    TicketCreationResponse ticketCreationResponse = new Gson().fromJson(requestData.getResponse(), TicketCreationResponse.class);
                    if (ticketCreationResponse.getStatus() == 1) {
                        if (ticketCreationResponse.getData() != null) {
                            General.customToast("Ticket ID " + ticketCreationResponse.getData().getTicketIDVal() + " was created", HomeActivity.this);
                            if (dialog != null)
                                dialog.cancel();
                        }
                    }
                    General.hideloading();
                } catch (Exception e) {
                    e.getMessage();
                    General.hideloading();
                }
            }
        });
        webserviceTask.execute();
    }
    private void TakePicture() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = SettingsUtils.createImageFile(this);
        } catch (IOException ex) {
            StringBuilder sb = new StringBuilder();
            sb.append("takeCameraPicture:");
            sb.append(ex);
            Log.e(TAG, sb.toString());
        }
        if (photoFile != null) {
            takePictureIntent.putExtra("output", FileProvider.getUriForFile(this,
                    SettingsUtils.FILE_PROVIDER, photoFile));
            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        }
//        }
    }

    class HomeRecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private HomeClickListener clicklistener;
        private GestureDetector gestureDetector;

        public  HomeRecyclerTouchListener(Context context, final RecyclerView recycleView, final HomeClickListener clicklistener) {

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


    private String convertToBase64(String imagePath) {
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodedImage = null;
        try {
            bmp = BitmapFactory.decodeFile(imagePath);
            bos = new ByteArrayOutputStream();
            long current_time_cam_image = Calendar.getInstance().getTimeInMillis();
            String fileName = "RA_" + current_time_cam_image + ".jpg";

            if (bmp != null) {
                bmp = printLatLong(fileName, bmp);
            }
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodedImage = Base64.encodeToString(bt, Base64.DEFAULT);
            String mBase64 = "";
            mBase64 = encodedImage;
            if (mBase64 != null) {
                //Log.e("encode_is", "encode_is: " + mBase64);
                GetPhoto getPhoto_new_image = new GetPhoto();
                getPhoto_new_image.setNewimage(true);
                getPhoto_new_image.setLogo(mBase64);
                getPhoto_new_image.setId(0);
                getPhoto_new_image.setFileName(fileName);
                getPhoto_new_image.setPropertyId(0);
                GetPhoto_list_response.add(getPhoto_new_image);
                ticketRaiseImageAdapter.setphoto_adapter(GetPhoto_list_response);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return encodedImage;
    }

    private Bitmap printLatLong(String fileName, Bitmap toEdit) {
        try {
            Bitmap dest = Bitmap.createBitmap(toEdit.getWidth(), toEdit.getHeight(), toEdit.getConfig());
            Canvas cs = new Canvas(dest);
            cs.drawBitmap(toEdit, 0, 0, null);
            Paint tPaint = new Paint();
            tPaint.setTextSize(20.0f);
            tPaint.setColor(-16776961);
            tPaint.setTextAlign(Paint.Align.CENTER);
            tPaint.setStyle(Paint.Style.FILL);
            float height = tPaint.measureText("yY");

            Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
            StringBuilder sb2 = new StringBuilder();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                sb2.append(Environment.getExternalStorageDirectory());

            } else {
                sb2.append(this.getExternalFilesDir(""));
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
}
