package com.realappraiser.gharvalue.convenyancereport;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.communicator.JsonRequestData;
import com.realappraiser.gharvalue.communicator.RequestParam;
import com.realappraiser.gharvalue.communicator.TaskCompleteListener;
import com.realappraiser.gharvalue.communicator.WebserviceCommunicator;
import com.realappraiser.gharvalue.convenyancereport.adapter.DailyReportAdapter;
import com.realappraiser.gharvalue.communicator.TodayActivityResponse;
import com.realappraiser.gharvalue.utils.Connectivity;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.SettingsUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DialyReportFragment extends Fragment {


    private General general;

    private String TAG = DialyReportFragment.class.getName();

    @BindView(R.id.rv_dailyReport)
    RecyclerView rvDailyReport;

    @BindView(R.id.no_activity)
    TextView noActivity;

    @BindView(R.id.ll_dailyreport)
    LinearLayout llDailyReport;

    @BindView(R.id.total_distance_travelled)
    TextView totalDistanceTravelled;

    @BindView(R.id.total_activity_count)
    TextView totalActivityCount;

    @BindView(R.id.retry)
    Button retry;

    @BindView(R.id.ll_no_internet)
    RelativeLayout rl_no_internet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialy_report, container, false);
        ButterKnife.bind(this, view);
        general = new General(getActivity());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());


        Log.e(TAG, "Today Date...." + currentDateandTime);

        initDesign();

        getDailyConvenyanceReport();

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDailyConvenyanceReport();
            }
        });

        return view;
    }


    private void initDesign() {
        rvDailyReport.setHasFixedSize(true);
    }

    private void getDailyConvenyanceReport() {
        rl_no_internet.setVisibility(View.GONE);
        noActivity.setVisibility(View.GONE);
        if (Connectivity.isConnected(getActivity())) {

            General.showloading(getActivity());
            String url = general.ApiBaseUrl() + SettingsUtils.CONVEYANCE_REPORT;
            JsonRequestData requestData = new JsonRequestData();
            requestData.setInitQueryUrl(url);
            requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
            requestData.setAgencyBranchId(SettingsUtils.getInstance().getValue(SettingsUtils.BranchId, ""));
            requestData.setFieldStaff(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGIN_ID, ""));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            requestData.setFromDate(currentDateandTime + " 00:00:00");
            /*requestData.setFromDate("2023-10-30 00:00:00");
            requestData.setToDate("2023-10-30 23:59:00");*/
            requestData.setToDate(currentDateandTime + " 23:59:00");
            requestData.setUrl(RequestParam.GetConvenyanceTypeRequestParams(requestData));


            Log.e(TAG, "Request Params.." + new Gson().toJson(requestData));

            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(getActivity(),
                    requestData, SettingsUtils.GET_TOKEN);
            webserviceTask.setFetchMyData((TaskCompleteListener<JsonRequestData>) dailyActivityResponse -> {

                Log.e(TAG, new Gson().toJson(dailyActivityResponse));
                parseDailyActivityResponse(new Gson().fromJson(dailyActivityResponse.getResponse(), TodayActivityResponse.class), dailyActivityResponse.getResponseCode(), dailyActivityResponse.isSuccessful());


            });
            webserviceTask.execute();
        } else {
            rl_no_internet.setVisibility(View.VISIBLE);
            noActivity.setVisibility(View.GONE);
            llDailyReport.setVisibility(View.GONE);
            Connectivity.showNoConnectionDialog(getActivity());
        }
    }

    private void parseDailyActivityResponse(TodayActivityResponse fromJson, int responseCode, boolean isSuccessful) {

        if (fromJson != null && fromJson.getData() != null ) {
            noActivity.setVisibility(View.GONE);
            rl_no_internet.setVisibility(View.GONE);
            llDailyReport.setVisibility(View.VISIBLE);
            totalDistanceTravelled.setText("Total Distance Travelled : " + fromJson.getData().getTotalDistance() + " Km");
            totalActivityCount.setText("Total Activity count : " + fromJson.getData().getNoOfActivities());
            if (fromJson.getData().getActivities() != null) {
                DailyReportAdapter dailyReportAdapter = new DailyReportAdapter(getActivity(), fromJson.getData().getActivities());
                rvDailyReport.setAdapter(dailyReportAdapter);
                rvDailyReport.setItemAnimator(new DefaultItemAnimator());
                rvDailyReport.setNestedScrollingEnabled(false);
            }else {
                noActivity.setVisibility(View.VISIBLE);
                rl_no_internet.setVisibility(View.GONE);
                llDailyReport.setVisibility(View.GONE);
            }
        } else {
            noActivity.setVisibility(View.VISIBLE);
            rl_no_internet.setVisibility(View.GONE);
            llDailyReport.setVisibility(View.GONE);
        }

        General.hideloading();

    }
}