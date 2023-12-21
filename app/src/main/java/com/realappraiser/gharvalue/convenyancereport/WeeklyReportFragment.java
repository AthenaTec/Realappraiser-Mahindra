package com.realappraiser.gharvalue.convenyancereport;

import static android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION;

import static com.realappraiser.gharvalue.utils.General.REQUEST_ID_MULTIPLE_PERMISSIONS;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.activities.PropertyDocumentsActivity;
import com.realappraiser.gharvalue.communicator.JsonRequestData;
import com.realappraiser.gharvalue.communicator.RequestParam;
import com.realappraiser.gharvalue.communicator.TaskCompleteListener;
import com.realappraiser.gharvalue.communicator.WebserviceCommunicator;
import com.realappraiser.gharvalue.model.DailyActivityResponse;
import com.realappraiser.gharvalue.model.SubBranchModel;
import com.realappraiser.gharvalue.utils.Connectivity;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.SettingsUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WeeklyReportFragment extends Fragment implements  PDFUtility.OnDocumentClose{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.etFromDate)
    EditText etFromDate;

    @BindView(R.id.etToDate)
    EditText etToDate;

    @BindView(R.id.btnSubmit)
    Button download;


    private General general;

    private String TAG = DialyReportFragment.class.getName();

    private String fromdate = "";

    private String toDate = "";

    ArrayList<DailyActivityResponse.Datum> data;

    private AppCompatEditText rowCount;

    private boolean isShowButton = false;

    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 3;

    private String[] androidHigherVersionPermission = new String[]{
            Manifest.permission.READ_MEDIA_IMAGES};




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly_report, container, false);
        ButterKnife.bind(this, view);
        etFromDate = view.findViewById(R.id.etFromDate);
        etToDate = view.findViewById(R.id.etToDate);
       // rowCount = view.findViewById(R.id.rowCount);

        general = new General(getActivity());

        etFromDate.setOnClickListener(view1 -> showCalender(false));
        etToDate.setOnClickListener(view1 -> showCalender(true));


        download.setOnClickListener(view3 -> {



                   // downloadPdf();






        /*  File file  = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/sabesh.pdf");

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

          String path = file.getAbsolutePath();

            try
            {
                PDFUtility.createPdf(view3.getContext(),this,getSampleData(),path,true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.e(TAG,"Error Creating Pdf");
                Toast.makeText(view3.getContext(),"Error Creating Pdf",Toast.LENGTH_SHORT).show();
            }



*/


           /* File f = new File(Environment.getExternalStorageDirectory(), "RealAppraiser");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    Files.createDirectory(Paths.get(f.getAbsolutePath()));
                } catch (IOException e) {
                    e.printStackTrace();
                   // Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                f.mkdir();
                f.mkdirs();
                Toast.makeText(getActivity(), f.getPath(), Toast.LENGTH_LONG).show();
            }*/



           // downloadPdf();

                    if (fromdate != null && !fromdate.isEmpty() && toDate != null && !toDate.isEmpty()) {
                        General.showloading(getActivity());
                        downloadPdf();
                    }else{
                        general.CustomToast("Please Select Date");
                    }
                }
        );
        return view;
    }

    private void downloadPdf() {

        if (Connectivity.isConnected(getActivity())) {

          //  General.showloading(getActivity());
            String url = general.ApiBaseUrl() + SettingsUtils.WEEKLY_REPORT;
            JsonRequestData requestData = new JsonRequestData();
            requestData.setInitQueryUrl(url);
            requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
            //requestData.setAgencyBranchId("378");
             requestData.setAgencyBranchId(SettingsUtils.getInstance().getValue(SettingsUtils.BranchId, ""));
            requestData.setFieldStaff(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGIN_ID, ""));
            //requestData.setFieldStaff("539");
             requestData.setFromDate(fromdate + " 00:00:00");
            //requestData.setFromDate("2023-10-01 00:00:00");
           // requestData.setToDate("2023-10-30 23:59:00");
            requestData.setToDate(toDate + " 23:59:00");

            requestData.setUrl(RequestParam.GetConvenyanceTypeRequestParams(requestData));

            Log.e(TAG, "Request Params..WeekLy Report" + new Gson().toJson(requestData));

            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(getActivity(),
                    requestData, SettingsUtils.GET_TOKEN);
            webserviceTask.setFetchMyData((TaskCompleteListener<JsonRequestData>) dailyActivityResponse -> {


                Log.e(TAG, dailyActivityResponse.getResponse());
                try{
                    parseDailyActivityResponse(new Gson().fromJson(dailyActivityResponse.getResponse(), DailyActivityResponse.class), dailyActivityResponse.getResponseCode(), dailyActivityResponse.isSuccessful());
                }catch (Exception e){
                    General.customToastLong("Something went wrong", getActivity());
                    General.hideloading();
                    e.getMessage();
                }

            });
            webserviceTask.execute();
        } else {
            General.hideloading();
            Connectivity.showNoConnectionDialog(getActivity());
        }
    }

    private void parseDailyActivityResponse(DailyActivityResponse fromJson, int responseCode, boolean isSuccessful) {
        Log.e(TAG, fromJson.getData().toString());

         data = new ArrayList<>();
         data = fromJson.getData();

         if(data.size()>0){
             createPdf();
             General.hideloading();
         }else{
             General.customToast("There is no data between these two dates!",getActivity());
             General.hideloading();
         }





    }

    private void createPdf(){



        String firstname = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_FIRSTNAME, "");
        String lastname = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LASTNAME, "");

        String fileName = "";
        if(firstname != null){
            fileName = firstname;
        }

        if(lastname != null && !lastname.equalsIgnoreCase("null")){
            fileName = fileName + " "+ lastname;
        }

        fileName = fileName + " Conveyance Report";

        if(checkPermissions()) {
            File file = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/"+fileName+"_"+System.currentTimeMillis()+".pdf");
            if (!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    General.hideloading();
                    throw new RuntimeException(e);
                }
            }else{
                file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    General.hideloading();
                    throw new RuntimeException(e);
                }
            }

            try
            {
                PDFUtility.createPdf(Objects.requireNonNull(getContext()),this,getSampleData(),file.getAbsolutePath(),true,data,etFromDate.getText().toString(),etToDate.getText().toString());
                General.hideloading();
            }
            catch (Exception e)
            {
                General.hideloading();
                e.printStackTrace();
                Toast.makeText(getContext(),"Unable to create Pdf, Please try again...",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Log.e("custome report","permission denied");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(Build.VERSION.SDK_INT > 33){
            int media = ContextCompat.checkSelfPermission(getActivity(),
                    ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);

            if(media == PackageManager.PERMISSION_GRANTED && requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS){
             General.customToast("Permission Granted",getActivity());
            }

        }
    }

    private void showCalender(boolean isFromDate) {

        download.setVisibility(View.VISIBLE);

        final Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog;
        int mYear = newCalendar.get(Calendar.YEAR);
        int mMonth = newCalendar.get(Calendar.MONTH);
        int mDay = newCalendar.get(Calendar.DAY_OF_MONTH);

        newCalendar.add(Calendar.YEAR, 0);
        long upperLimit = newCalendar.getTimeInMillis();

        datePickerDialog = new DatePickerDialog(getActivity(), new
                DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        newCalendar.set(Calendar.YEAR, year);
                        newCalendar.set(Calendar.MONTH, month);
                        newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale
                                .getDefault());
                        SimpleDateFormat sdfWeekReport = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                        if (!isFromDate) {
                            fromdate = sdfWeekReport.format(newCalendar.getTime());
                            etFromDate.setText(sdf.format(newCalendar.getTime()));
                        } else {
                            toDate = sdfWeekReport.format(newCalendar.getTime());
                            etToDate.setText(sdf.format(newCalendar.getTime()));
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().getTouchables().get(0).performClick();
        datePickerDialog.getDatePicker().setMaxDate(upperLimit);
        datePickerDialog.show();
    }

    @Override
    public void onPDFDocumentClose(File file) {
        Toast.makeText(getActivity(),"Conveyance Report Downloaded",Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }
    private List<String[]> getSampleData()
    {
        int count = 20;
       /* if(!TextUtils.isEmpty(rowCount.getText()))
        {
            count = 20;
        }*/

        List<String[]> temp = new ArrayList<>();
        for (int i = 0; i < count; i++)
        {
            temp.add(new String[] {"C1-R"+ (i+1),"C2-R"+ (i+1)});
        }
        return  temp;
    }

    public boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (Build.VERSION.SDK_INT < 33) {
            for (String p : permissions) {
                result = ContextCompat.checkSelfPermission(getActivity(), p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
        } else {
            for (String p : androidHigherVersionPermission) {
                result = ContextCompat.checkSelfPermission(getActivity(), p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}