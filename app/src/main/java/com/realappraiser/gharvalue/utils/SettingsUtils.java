package com.realappraiser.gharvalue.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.text.TextUtils;
import android.util.Log;

import com.realappraiser.gharvalue.MyApplication;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.utils.security.EncryptionUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*import io.fabric.sdk.android.services.events.EventsFilesManager;*/

/**
 * Created by kaptas on dec17.
 * SettingsUtils class is the Webservice API's assigned class and temporary storage shared preference
 * has been used overall in all activities
 */

@SuppressWarnings("ALL")
public class SettingsUtils {

    private static final String TAG = SettingsUtils.class.getSimpleName();
    public static String mPhotoPath = null;

    //public static final String BASE_URL = "http://113.193.30.132:8003";
    public static final String API_BASE_URL = "API_BASE_URL";
    public static String image_destination_path = Environment.getExternalStorageDirectory() + File.separator + "copy" + ".jpg";
    public static final String SecurityToken = "/api/security/token";
    public static final String EmployeeBranches = "/Api/Login/app_GetEmployeeBranches";
    public static final String AgencySubBranches = "/Api/BankBranch/app_GetSubBranches";

    public static final String CurrentServer = "/api/Utililty/app_AuthenticateAPI";
    public static final String Login = "/api/Login/app_AuthenticateUser"; // for kakode
    public static final String Login_STORE = "/api/Login/app_AuthenticateUser_Store"; // for kakode
    public static final String GETSTORE_PASSWORD_KEY = "/api/Login/GetStore"; // for kakode
    //public static final String Login = "/api/Login/AuthenticateUserAPP"; // for jaipur
    public static final String OpenClosedCaseList = "/api/Case/app_GetCaseByEmployeeID/";
    public static final String UpdateCaseStatusById = "/api/Case/app_UpdateCaseStatusById";
    public static final String deleteImage = "/api/Case/Removeimage";
    public static final String RejectCaseStatusById = "/api/Case/app_RejectCaseStatusById";
    public static final String LoadPropertyType = "/api/Property/app_LoadPropertyTypeByBankId/";
    public static final String UpdatePropertyType = "/api/Case/app_UpdatePropertyTypeByCaseId";
    public static final String DocumentRead = "/api/ValuationMethod/app_GetAgreementDocByVisibleandcaseId";
    public static final String GetFieldDropDowns = "/api/FieldManagement/app_GetFieldFormDropDowns";
    public static final String SaveCaseInspection = "/api/FieldManagement/app_InsertFieldEntryData";
    public static final String EditCaseInspection = "/api/FieldManagement/app_GetFieldEntryData";
    public static final String GetCaseInspection = "/api/Case/app_GetById";
    public static final String RemoveProximity = "/api/ValuationMethod/app_Removeproxi";
    public static final String GetReportTypeProperty = "/api/ReportType/app_GetReportTypeByAgencyBranchBank";
    public static final String GetFreshCaseSelections = "/api/Case/app_GetFreshCaseSelections";
    public static final String GetBankSelections = "/api/AgencyBank/app_GetByAgencyBranchAndPropertyCategoryId";
    public static final String GetByFieldStaffByCity = "/api/Employee/app_GetByFieldStaffByCity";
    public static final String TransferCase = "/api/Case/app_ReassignFieldStaff";
    public static final String LocationTracker = "/api/Case/app_InsertFSTrackerDetails";
    public static final String CreateCase = "/api/Case/app_InsertCaseFromFS";
    public static final String UpdatePropertytype = "/api/Case/app_UpdatePropertyTypeByCaseId";
    public static final String UploadPropertyDocuments = "/api/FieldManagement/app_UploadPropertyDocuments";
    public static final String GetPropertyDetails = "/api/Property/GetPropertyDetails";
    public static final String GetPropertyCompareDetails = "/api/Case/GetPropertyCompareDetails";
    //public static final String OfflineCaseCount = "/api/case/GetOfflineCaseCount";
    public static final String OfflineCaseCount = "/api/Application/app_GetAppSettings";
    public static final String InsertQC = "/api/FieldManagement/app_InsertQC";
    public static final String LoadQC = "/api/FieldManagement/app_LoadQCData";
    public static final String is_offline = "is_offline";
    public static final String is_local = "is_local";

    public static final String getRatePopup = "/api/Case/app_GetProfertyFlatValueDetails";

    public static final String FLAG_URL = "https://stage.real-appraiser" + ".com/api/FieldManagement/GetAllHostNames";


    public static final String IMAGE = "/api/FieldManagement/app_UploadPropertyImages";
    public static final String GETIMAGE = "/api/FieldManagement/app_GetBLOBImageById/";
    public static final String GETIMAGE_ONE = "/api/FieldManagement/app_GetMeasurmentImageByPropertyId/";
    public static final String DELETEIMAGE = "/api/FieldManagement/app_Removeimage";
    public static final String DeleteFloors = "/api/FieldManagement/app_DeleteDynamicFloors";

    public static final String TYPE_OF_MASONRY = "/api/TypeOfMasonry/GetAll";

    public static final String TYPE_OF_NON_CASE_VISIT = "/api/NonCaseActivities/LoadNonCaseActivities";
    public static final String TYPE_OF_MORTAR = "/api/TypeOfMortar/GetAll";
    public static final String TYPE_OF_FOOTING = "/api/TypeOfFootingFoundation/GetAll";
    public static final String TYPE_OF_STEEL = "/api/TypeOfSteelGrade/GetAll";

    public static final String CONVEYANCE_REPORT = "/api/FsConveyance/app_DailyConveyanceReportAsync";

    public static final String WEEKLY_REPORT = "/api/FsConveyance/app_CustomConveyanceReport";


    //public static final String Logout = API_BASE_URL + "login/logout";

    public static final String KEY_LOGIN_ID = "KEY_LOGIN_ID";
    public static final String KEY_FIRSTNAME = "KEY_FIRSTNAME";
    public static final String KEY_LASTNAME = "KEY_LASTNAME";
    public static final String KEY_EMAIL = "KEY_EMAIL";
    public static final String DropDownSave = "DropDownSave";
    public static final String KEY_DOCUMENT = "KEY_DOCUMENT";
    public static final String KEY_OFFLINECASE_COUNT = "KEY_OFFLINECASE_COUNT";
    public static final String KEY_ENABLE_OFFLINE = "KEY_ENABLE_OFFLINE";
    public static final String KEY_INTERNAL_COMPOSTION_NOT_COPY = "KEY_INTERNAL_COMPOSTION_NOT_COPY ";
    public static final String KEY_FLAG_URL = "FLAG_URL";

    public static final String ReportName = "ReportName";

    public static final String KEY_URL_TYPE = "KEY_URL";

    public static final String KEY_LOGGED_IN = "KEY_LOGGED_IN";
    public static final String StatusId = "StatusId";
    public static final String KEY_EMPID = "KEY_EMPID";
    public static final String CASE_ID = "CASE_ID";
    public static final String Case_Title = "Case_Title";
    public static final String Case_Bank = "Case_Bank";
    public static final String Case_BankBranch = "Case_BankBranch";
    public static final String PropertyId = "PropertyId";
    public static final String PropertyCategoryId = "PropertyCategoryId";
    public static final String PropertyType = "PropertyType";
    public static final String BranchId = "BranchId";
    public static final String AgencyId = "AgencyId";

    public static final String loginRoles = "loginRoles";
    public static final String KEY_LOGIN = "KEY_LOGIN";
    public static final String KEY_IMEI = "KEY_IMEI";
    public static final String KEY_TOKEN = "KEY_TOKEN";

    public static final String aCase = "aCase";
    public static final String property = "property";
    public static final String indProperty = "indProperty";
    public static final String indPropertyValuation = "indPropertyValuation";
    public static final String indPropertyFloors = "indPropertyFloors";
    public static final String indPropertyFloorsValuations = "indPropertyFloorsValuations";
    public static final String proximities = "proximities";
    public static final String insertedit_casedetail = "insertedit_casedetail";

    public static final String real_appraiser_jaipur = "real_appraiser_jaipur";
    public static final String CURRENT_CAMERA_IMAGE = "CURRENT_CAMERA_IMAGE";
    public static final String FILE_PROVIDER = "com.realappraiser.gharvalue.fileprovider";
    public static double Longitudes = 0.0d;
    public static double Latitudes = 0.0d;
    public static final long FASTEST_UPDATE_INTERVAL = 30000;
    public static final long UPDATE_INTERVAL = 60000;
    public static final int GPS_REQUEST = 1001;
    public static final int GET = 1;
    public static final int POST = 2;
    public static final int PUT = 3;
    public static final int DELETE = 4;
    public static final int GET_TOKEN = 5;
    public static final int POST_TOKEN = 6;
    public static final int PUT_TOKEN = 7;
    public static final int DELETE_TOKEN = 8;

    public static final int DELETE_DOCUMENT = 9;

    public static final String APP_DATA = "app_data";

    private static SettingsUtils instance;
    private SharedPreferences prefs;

    private EncryptionUtils encryptionUtil;

    public SettingsUtils(Context context) {
        if (context != null) {
            this.prefs = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        } else {
            if (MyApplication.getAppContext() != null) {
                //this.prefs = context.getSharedPreferences(MyApplication.getAppContext().getString(R.string.app_name), Context.MODE_PRIVATE);
                // 11/09/2018 - v3.1 changed
                this.prefs = MyApplication.getAppContext().getSharedPreferences(MyApplication.getAppContext().getString(R.string.app_name), MyApplication.getAppContext().MODE_PRIVATE);
            }
        }

        encryptionUtil = new EncryptionUtils(context);
    }

    public static SettingsUtils getInstance() {
        if (instance == null) {
            throw new IllegalStateException("should use SettingsUtils.init(context) first");
//            Log.e(TAG, "should use SettingsUtils.init(context) first");
        }
        return instance;
    }

    public static void init(Context context) {
        if (context != null) instance = new SettingsUtils(context);
    }

    public static File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH_mm_ss").format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append("RA_");
        sb.append(timeStamp);
        /*sb.append(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR);*/
        File image = File.createTempFile(sb.toString(), ".jpg", context.getExternalFilesDir(Environment.DIRECTORY_DCIM));
        mPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void putValue(String key, String value) {
//        Log.e(TAG, "putValue:"+encryptionUtil.encrypt(value));
        prefs.edit().putString(key, encryptionUtil.encrypt(value)).apply();
    }

    public void putValue(String key, int value) {


        prefs.edit().putInt(key, value).apply();
    }

    public void putValue(String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    public void putValue(String key, long value) {
        prefs.edit().putLong(key, value).apply();
    }

    public void putValue(String key, float value) {
        prefs.edit().putFloat(key, value).apply();
    }

    public String getValue(String key, String defaultValue) {

        if (!prefs.getString(key, defaultValue).isEmpty()) {
//            Log.e(TAG, "getValue: "+encryptionUtil.decrypt(prefs.getString(key, defaultValue)));
            return encryptionUtil.decrypt(prefs.getString(key, defaultValue));
        } else {
            return prefs.getString(key, defaultValue);
        }


    }

    public int getValue(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    public boolean getValue(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }

    public long getValue(String key, long defaultValue) {
        return prefs.getLong(key, defaultValue);
    }

    public float getValue(String key, float defaultValue) {
        return prefs.getFloat(key, defaultValue);
    }

    public boolean contains(String key) {
        return prefs.contains(key);
    }

    public void clear() {
        prefs.edit().clear().apply();
    }


    public void putServiceValues(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    public String getServiceValue(String key) {
        Log.d("ID_INGet", prefs.getString(key, ""));
        return prefs.getString(key, "");
    }

    public void removeService(ArrayList<String> batchList) {

        for (String id : batchList) {
            Log.d("RemoveId", id);
            prefs.edit().putString(id, "").apply();
        }
    }

    /**
     * Put ArrayList of String into SharedPreferences with 'key' and save
     *
     * @param key        SharedPreferences key
     * @param stringList ArrayList of String to be added
     */
    public void putListString(String key, ArrayList<String> stringList) {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        prefs.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    /**
     * Get parsed ArrayList of String from SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @return ArrayList of String
     */
    public ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(prefs.getString(key, ""), "‚‗‚")));
    }

    /**
     * null keys would corrupt the shared pref file and make them unreadable this is a preventive measure
     *
     * @param //the pref key
     */
    public void checkForNullKey(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
    }

    /**
     * null keys would corrupt the shared pref file and make them unreadable this is a preventive measure
     *
     * @param //the pref key
     */
    public void checkForNullValue(String value) {
        if (value == null) {
            throw new NullPointerException();
        }
    }

    public void clearValue(String key, String value) {

        prefs.edit().remove(key).apply();

    }

    public static boolean hasMultiplePermissions(Context context, String permissions) {
        return ContextCompat.checkSelfPermission(context, permissions) == 0;
    }


    public static String convertLatLngToAddress(Context context, double latt, double longi) {
        String address = "Address not found";
        Context context2 = context;
        try {
            List<Address> addresses = new Geocoder(context, Locale.getDefault()).getFromLocation(latt, longi, 1);
            if (addresses.size() <= 0) {
                return address;
            }
            String addres = ((Address) addresses.get(0)).getAddressLine(0);
            String locality = ((Address) addresses.get(0)).getLocality();
            String adminArea = ((Address) addresses.get(0)).getAdminArea();
            String countryName = ((Address) addresses.get(0)).getCountryName();
            String postalCode = ((Address) addresses.get(0)).getPostalCode();
            String featureName = ((Address) addresses.get(0)).getFeatureName();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("convertLatLngToAddress: ");
            sb.append(addres);
            Log.e(str, sb.toString());
            return addres.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "onRunJob: Could not get address..!");
            return address;
        }
    }

    public static Uri checkAndReturnUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, "com.realappraiser.gharvalue.fileprovider", file);
        } else {
            return Uri.fromFile(file);
        }
    }


}
