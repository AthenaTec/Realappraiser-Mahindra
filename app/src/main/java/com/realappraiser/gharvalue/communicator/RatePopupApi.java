package com.realappraiser.gharvalue.communicator;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.realappraiser.gharvalue.model.RatePopup;
import com.realappraiser.gharvalue.utils.Connectivity;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.SettingsUtils;

public class RatePopupApi {
    private RatePopupupInterface ratePopupupInterface;
    private Activity context;
    private static final String TAG = "RatePopupApi";

    public RatePopupApi(Activity context, RatePopupupInterface ratePopupupInterface) {
        this.ratePopupupInterface = ratePopupupInterface;
        this.context = context;
    }

    public void getRatePopup(String caseId, String latitude, String longitude) {

        Log.e(TAG, "getRatePopup: caseId" + caseId + "=latitude=" + latitude + "=longitude=" + longitude);

        if (Connectivity.isConnected(context)) {
            String url = SettingsUtils.getInstance().getValue(SettingsUtils.API_BASE_URL,
                    "") + SettingsUtils.getRatePopup;
            JsonRequestData requestData = new JsonRequestData();
            requestData.setInitQueryUrl(url);
            requestData.setCaseId(caseId);
            requestData.setLatitude(latitude);
            requestData.setLongitude(longitude);
            requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN
                    , ""));
            requestData.setUrl(RequestParam.GetRatePopupDetails(requestData));
            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(context, requestData, SettingsUtils.GET_TOKEN);
            webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                @Override
                public void onTaskComplete(JsonRequestData requestData) {
                    if (requestData.isSuccessful()) {
                        try {
                            String sb = "RatePopup get sucessfully" +
                                    requestData.getResponse();
                            Log.e(TAG, sb);
                            ratePopupupInterface.onRatePopupSucess(new Gson().fromJson(requestData.getResponse(), RatePopup.class));
                        } catch (Exception e) {
                            ratePopupupInterface.onRatePopupFailed("Poor Internet connectivity!");
                            e.printStackTrace();
                        }
                    } else {
                        ratePopupupInterface.onRatePopupFailed("Poor Internet connectivity!");
                    }
                }
            });
            webserviceTask.execute();
        } else {
            Connectivity.showNoConnectionDialog(context);
        }
    }
}
