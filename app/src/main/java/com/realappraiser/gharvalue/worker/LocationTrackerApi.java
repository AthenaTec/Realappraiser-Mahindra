package com.realappraiser.gharvalue.worker;

import static com.realappraiser.gharvalue.utils.General.GPS_status;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.realappraiser.gharvalue.communicator.JsonRequestData;
import com.realappraiser.gharvalue.communicator.RequestParam;
import com.realappraiser.gharvalue.communicator.TaskCompleteListener;
import com.realappraiser.gharvalue.communicator.WebserviceCommunicator;
import com.realappraiser.gharvalue.utils.Connectivity;
import com.realappraiser.gharvalue.utils.GPSService;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.SettingsUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LocationTrackerApi {
    private static final String TAG = "LocationTrackerApi";
    private Context context;
    private boolean success = false;
    String address = "";
    public LocationTrackerApi(Context context) {
        this.context = context;
    }


    public boolean shareLocation(String caseId, String fieldStaffId, String interval, double latitudes, double longitudes
    ,String comments,Integer ActivityType) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                address = SettingsUtils.convertLatLngToAddress(context);
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

        if (Connectivity.isConnected(context)) {
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
                address = SettingsUtils.convertLatLngToAddress(context);
                requestData.setAddress(address);
            }


            requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
//            requestData.setAuthToken("Bearer FFcxMfxHjm79qtRcMFNbp4Ydf7l_3jGiLSeSuY2tC3QJmiurkOSfEQGtbN-M6S3kF13VMSM5CALbIJNnT37zMi81gCRCz6YWZD7Usqs9i73kIgJGoHdDsPJdHkWyzD52JuORASt5p-jEB5jN2abX2HXdcIDrZD_YxVHWlFVn4uITc1SA8nk5OPCy5-xmpSq4VrHoUPsRrRMPx411C8gfcJvdaOCTodGRKFVwzVffHRC2cTRi-");
            requestData.setRequestBody(RequestParam.LocationTracker(requestData));

            Log.e("Location Params", new Gson().toJson(requestData));

            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(context,
                    requestData, SettingsUtils.POST_TOKEN);
            webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                public void onTaskComplete(JsonRequestData requestData) {
                    String sb = "Location updated sucessfully" +
                            requestData.getResponse();
                    Log.e(TAG, sb);
                    success = true;
                }
            });
            webserviceTask.execute();




        } else {
           /* Toast.makeText(context, "Please check your Internet Connection!",
                    Toast.LENGTH_SHORT).show();*/
            success = false;
        }
        return success;
    }
}
