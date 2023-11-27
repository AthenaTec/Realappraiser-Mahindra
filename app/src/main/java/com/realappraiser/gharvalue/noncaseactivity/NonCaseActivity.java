package com.realappraiser.gharvalue.noncaseactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.activities.HomeActivity;
import com.realappraiser.gharvalue.activities.LoginActivity;
import com.realappraiser.gharvalue.communicator.JsonRequestData;
import com.realappraiser.gharvalue.communicator.RequestParam;
import com.realappraiser.gharvalue.communicator.TaskCompleteListener;
import com.realappraiser.gharvalue.communicator.WebserviceCommunicator;
import com.realappraiser.gharvalue.model.TypeOfMasonry;
import com.realappraiser.gharvalue.utils.Connectivity;
import com.realappraiser.gharvalue.utils.GPSService;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.SettingsUtils;
import com.realappraiser.gharvalue.worker.LocationTrackerApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NonCaseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rg_visit_type)
    RadioGroup rgVisitType;

    @BindView(R.id.btnSubmit)
    Button submit;

    @BindView(R.id.bottomLayout)
    LinearLayout bottomLayout;

    @BindView(R.id.etComment)
    EditText comments;

    private LocationTrackerApi locationTrackerApi;
    private General general;

    private List<TypeOfMasonry.Datum> data;

    private String visitType;

    private String fieldStaffId;

    private String address = "";

    private static final String TAG = NonCaseActivity.class.getName();

    private static  boolean isButtonCliecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_case);
        ButterKnife.bind(this);
        initToolBar();
        getTypeVisitList();
    }

    private void initToolBar() {
        general = new General(NonCaseActivity.this);
        locationTrackerApi = new LocationTrackerApi(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Non Case Activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        fieldStaffId = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGIN_ID, "");


        rgVisitType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (bottomLayout.getVisibility() == View.GONE) {
                    bottomLayout.setVisibility(View.VISIBLE);
                }
                visitType = data.get(checkedId).getName();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isButtonCliecked){
                    isButtonCliecked = true;
                    if (general.checkLatLong()) {
                        shareLocation();
                    } else {
                        getCurrentLocation(NonCaseActivity.this);
                    }
                }


            }
        });
    }

    private void initRadioGroup(List<TypeOfMasonry.Datum> data) {
        rgVisitType.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < data.size(); i++) {
            RadioButton rbn = new RadioButton(this);
            rbn.setId(i);
            rbn.setText(" " + data.get(i).getName());
            rbn.setTextAppearance(this, android.R.style.TextAppearance_Small);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            params.setMargins(0, 10, 0, 0);
            rbn.setLayoutParams(params);
            rgVisitType.addView(rbn);
        }
    }


    private void getTypeVisitList() {
        General.showloading(this);
        if (Connectivity.isConnected(this)) {
            String url = general.ApiBaseUrl() + SettingsUtils.TYPE_OF_NON_CASE_VISIT;
            // String url = SettingsUtils.BASE_URL + SettingsUtils.GetFieldDropDowns;
            JsonRequestData requestData = new JsonRequestData();
            requestData.setUrl(url);
            requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(NonCaseActivity.this, requestData, SettingsUtils.GET_TOKEN);
            webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                @Override
                public void onTaskComplete(JsonRequestData requestData) {
                    Log.e("TypeVisitList", "onTaskComplete: " + new Gson().toJson(requestData.getResponse()));

                    typeVisitListResponse(requestData.getResponse(), requestData.getResponseCode(), requestData.isSuccessful());
                }
            });
            webserviceTask.execute();
        } else {
            General.hideloading();
            General.customToast("Please check your Internet Connection!", NonCaseActivity.this);
        }
    }

    private void typeVisitListResponse(String response, int responseCode, boolean successful) {

        if (successful) {
            TypeOfMasonry typeOfMortar = new Gson().fromJson(response, TypeOfMasonry.class);
            data = new ArrayList<>();
            data.addAll(typeOfMortar.getData());
            initRadioGroup(data);
            General.hideloading();
        } else {
            General.customToast("Something went wrong...!", NonCaseActivity.this);
            General.hideloading();
        }

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

                            shareLocation();
                        }
                    }
                }, 1500);
            } catch (Exception e) {
                isButtonCliecked = false;
                e.printStackTrace();
            }
        }
    }

    private void shareLocation() {
        if (general.isNetworkAvailable()) {
            shareLocation("", fieldStaffId, visitType, SettingsUtils.Latitudes, SettingsUtils.Longitudes, comments.getText().toString(), 2);
        } else {
            isButtonCliecked = false;
            General.customToast("Please check your Internet Connection!", NonCaseActivity.this);
        }
    }

    public void shareLocation(String caseId, String fieldStaffId, String interval, double latitudes, double longitudes
            , String comments, Integer ActivityType) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                address = SettingsUtils.convertLatLngToAddress(NonCaseActivity.this);
            }
        });

        @SuppressLint("SimpleDateFormat") String time =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(Calendar.getInstance().getTime());

        String sb2 = "shareLocation:1 caseId=>" +
                caseId +
                "\nfieldStaffId=>" +
                fieldStaffId +
                "\nType=>" +
                interval +
                "\nLatt=>" +
                latitudes +
                "\nLong=>" +
                longitudes +
                "\nTime=>" +
                time +
                "\nAddress=>" +
                address;
        Log.e(TAG, sb2);

        if (Connectivity.isConnected(this)) {
            General.showloading(NonCaseActivity.this);
            JsonRequestData requestData = new JsonRequestData();
            String url = SettingsUtils.getInstance().getValue(SettingsUtils.API_BASE_URL,
                    "") + SettingsUtils.LocationTracker;
            requestData.setUrl(url);
            requestData.setCaseId(caseId);
            requestData.setEmpId(fieldStaffId);
            requestData.setLocationType(interval);
            requestData.setLatitude(SettingsUtils.getInstance().getValue("lat",""));
            requestData.setLongitude(SettingsUtils.getInstance().getValue("long",""));
            requestData.setTrackerTime(time);
            requestData.setActivityType(String.valueOf(ActivityType));
            requestData.setComments(comments);
            requestData.setAgencyBId(SettingsUtils.getInstance().getValue(SettingsUtils.BranchId, ""));
            Log.e("convertLatLngToAddrJson", address);

            if (!address.isEmpty()) {
                requestData.setAddress(address);
            } else {
                address = SettingsUtils.convertLatLngToAddress(NonCaseActivity.this);
                requestData.setAddress(address);
            }


            requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
//            requestData.setAuthToken("Bearer FFcxMfxHjm79qtRcMFNbp4Ydf7l_3jGiLSeSuY2tC3QJmiurkOSfEQGtbN-M6S3kF13VMSM5CALbIJNnT37zMi81gCRCz6YWZD7Usqs9i73kIgJGoHdDsPJdHkWyzD52JuORASt5p-jEB5jN2abX2HXdcIDrZD_YxVHWlFVn4uITc1SA8nk5OPCy5-xmpSq4VrHoUPsRrRMPx411C8gfcJvdaOCTodGRKFVwzVffHRC2cTRi-");
            requestData.setRequestBody(RequestParam.LocationTracker(requestData));

            Log.e("Location Params", new Gson().toJson(requestData));

            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(NonCaseActivity.this,
                    requestData, SettingsUtils.POST_TOKEN);
            webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                public void onTaskComplete(JsonRequestData requestData) {
                    isButtonCliecked = false;
                    General.hideloading();

                    Log.e("Location Response", new Gson().toJson(requestData));

                    if (requestData.isSuccessful()) {
                        String sb = "Location updated sucessfully" +
                                requestData.getResponse();
                        Log.e(TAG, sb);
                        General.customToast("Visit type data updated successfully", NonCaseActivity.this);
                        onBackPressed();
                    }
                }
            });

            webserviceTask.execute();
        } else {
            isButtonCliecked =  false;
            General.customToast("Please check your Internet Connection!", NonCaseActivity.this);
        }
    }
}