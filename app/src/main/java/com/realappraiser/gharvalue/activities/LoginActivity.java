package com.realappraiser.gharvalue.activities;

import static com.realappraiser.gharvalue.utils.General.REQUEST_ID_MULTIPLE_PERMISSIONS;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.communicator.DataModel;
import com.realappraiser.gharvalue.communicator.DataResponse;
import com.realappraiser.gharvalue.communicator.JsonRequestData;
import com.realappraiser.gharvalue.communicator.RequestParam;
import com.realappraiser.gharvalue.communicator.ResponseParser;
import com.realappraiser.gharvalue.communicator.TaskCompleteListener;
import com.realappraiser.gharvalue.communicator.WebserviceCommunicator;
import com.realappraiser.gharvalue.model.ConfigData;
import com.realappraiser.gharvalue.model.GetStoreModel;
import com.realappraiser.gharvalue.model.MultiBranchModel;
import com.realappraiser.gharvalue.model.SafeNetModel;
import com.realappraiser.gharvalue.model.SecurityToken;
import com.realappraiser.gharvalue.utils.Connectivity;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.GpsUtils;
import com.realappraiser.gharvalue.utils.NetworkPolicyTranslucent;
import com.realappraiser.gharvalue.utils.OfflineLocationInterface;
import com.realappraiser.gharvalue.utils.OfflineLocationReceiver;
import com.realappraiser.gharvalue.utils.SettingsUtils;
import com.realappraiser.gharvalue.utils.security.EncryptionKeyGenerator;
import com.realappraiser.gharvalue.utils.security.SafetyNetChecker;
import com.realappraiser.gharvalue.worker.LocationTrackerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaptas on 15/12/17.
 */

@SuppressWarnings("ALL")
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        OfflineLocationInterface, OnFailureListener,
        OnSuccessListener<SafetyNetApi.AttestationResponse> {

    private General general;
    @BindView(R.id.forgotpassword)
    TextView forgotpassword;
    @BindView(R.id.seturl)
    TextView seturl;
    @BindView(R.id.txt)
    TextView txt;
    @BindView(R.id.email_input)
    TextView email_input;
    @BindView(R.id.pwd_input)
    EditText pwd_input;
    @BindView(R.id.loginBtn)
    TextView loginBtn;

    private EditText addkeys;
    private Dialog dialog;

    private AlertDialog urlAlertDialog;

    private String msg = "", info = "";
    private String lemail = "", lpwd = "";
    private ArrayList<DataModel> loginModel;
    String encrptPass;

    // Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    private boolean isGPS = false;

    private double latt = 0.0, longi = 0.0;


    private static final String TAG = "LoginActivity";
    private OfflineLocationReceiver offlineLocationReceiver;
    private LocationTrackerApi locationTrackerApi;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };

    /**
     * Password hide Imageview instance
     */
    @BindView(R.id.password_hide)
    ImageView passwordHide;

    /**
     * Password show boolean value
     */
    private boolean isPasswordShow = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO - > For Bug and error report send to api
        General.report_bug(LoginActivity.this);
        setContentView(R.layout.login);
        ButterKnife.bind(this);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "2");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Login");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        mFirebaseAnalytics.logEvent("Login", bundle);
        general = new General(LoginActivity.this);


       /* if (Connectivity.isConnected(this)) {
            SafetyNetChecker safetyNetChecker = new SafetyNetChecker(this, this, this);
        } else if (General.rootAndEmulatorChecker(LoginActivity.this) == false) {
            initValues();
        }*/

        initValues();


        passwordHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowPassword();
            }
        });

    }

    private void onShowPassword() {

        if (isPasswordShow) {
            pwd_input.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
            passwordHide.setImageResource(R.drawable.ic_password_hide);
            pwd_input.setSelection(pwd_input.getText
                    ().length());
            isPasswordShow = false;
        } else {
            pwd_input.setTransformationMethod
                    (HideReturnsTransformationMethod.getInstance());
            passwordHide.setImageResource(R.drawable.ic_password_show);
            pwd_input.setSelection(pwd_input.getText
                    ().length());
            isPasswordShow = true;
        }
    }


    private void settingAndFireRemote() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5)
                .build();
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
                                if (configData.getUser() == null || configData.getUser().isEmpty()
                                        || configData.getUser().equals("") || configData.getUser().equals("null"))
                                    if (general.checkPermissions())
//                                        checkUpdate(configData);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });*/
    }

    private void checkUpdate(ConfigData configData) throws PackageManager.NameNotFoundException {
       /* ApkDownloader apkDownloader = new ApkDownloader();
        PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
        String version = pInfo.versionName;
        if (!configData.getVersion().equals(version)) {
            File file = new File(Environment.getExternalStorageDirectory() + "/RealAppraiser/" + configData.getFileName());
            Log.e(TAG, "checkUpdate: " + file.getAbsolutePath());
            if (!file.exists()) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        apkDownloader.Download(MyApplication.getAppContext(), configData.getUrl(), configData.getFileName());
                    }
                });

            } else {
                showUpdateDialog(file, configData.getVersion());
            }
        }*/
    }

    private void showUpdateDialog(File file, String newVersion) throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
        String version = pInfo.versionName;

        AlertDialog.Builder alert_build = new AlertDialog.Builder(this);
        alert_build.setTitle("Update Available!");
        alert_build.setMessage("You have older version(" + version + ").\nPlease update to new version(" + newVersion + ") which contains lot new features.");
        alert_build.setCancelable(false);
        alert_build.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openInstall(file);
                dialog.dismiss();
            }
        });
        AlertDialog alert_show = alert_build.create();
        alert_show.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alert_show.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorPrimaryDark));
            }
        });
        alert_show.show();
    }

    private void openInstall(File file) {
        Uri uri = FileProvider.getUriForFile(this, "com.realappraiser.gharvalue.fileprovider", file);
        Log.e(TAG, "checkUpdate: install update =>" + uri);
        Intent promptInstall = new Intent(Intent.ACTION_VIEW);
        promptInstall.setDataAndType(SettingsUtils.checkAndReturnUri(this, file), "application/vnd.android" + ".package-archive");
        promptInstall.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
        promptInstall.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        promptInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getApplicationContext().startActivity(promptInstall);
    }


    /*******
     * Initiate View
     * *******/
    private void initValues() {

        NetworkPolicyTranslucent ntwthread = new NetworkPolicyTranslucent(LoginActivity.this);
        ntwthread.MainThreadTranslucent();
        SettingsUtils.init(this);
        locationTrackerApi = new LocationTrackerApi(this);
        forgotpassword.setTypeface(general.regulartypeface());
        seturl.setTypeface(general.regulartypeface());
        //login.setTypeface(general.mediumtypeface());
        txt.setTypeface(general.regulartypeface());
        loginBtn.setOnClickListener(this);
        seturl.setOnClickListener(this);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        txt.setText(year + " " + getResources().getString(R.string.realappraiser_brand));

        String baseurl = general.ApiBaseUrl();
        if (baseurl.equalsIgnoreCase("")) {
            Serverurldialog();
        }

        if (general.checkPermissions() && checkGps()) {
            makeLocationUpadte();
        }
    }

    private boolean checkGps() {
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            public final void gpsStatus(boolean isGPSEnable) {
                isGPS = isGPSEnable;
            }
        });
        return isGPS;
    }

    /*******
     * Onclick listener for click event widgets
     * *******/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                submitLoginForm();
                break;

            case R.id.seturl:
                Serverurldialog();
                break;
        }
    }


    /*******
     * Submit Login Form
     * *******/
    private void submitLoginForm() {
        if (!validateEmailPassword()) {
            return;
        } else {
            InitiateLoginWebservice();
        }
    }

    /*******
     * Submit Popup Form
     * *******/
    private void submitPopupForm() {
        if (!validatePopupInputParams()) {
            return;
        } else {
            CurrentServerUrlWebservice();
        }
    }


    /*******
     * Server Url Dialog to force on Login Screen
     * first view for server authentication
     * *******/
    private void Serverurldialog() {
        /*dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.loginpopup);


        TextView popuptitle = (TextView) dialog.findViewById(R.id.title);
        addkeys = (EditText) dialog.findViewById(R.id.layout_url);
        ImageView Cancel = (ImageView) dialog.findViewById(R.id.close);
        Button submit = (Button) dialog.findViewById(R.id.button);
        submit.setTypeface(general.mediumtypeface());
        popuptitle.setTypeface(general.mediumtypeface());*/

        View view  = getLayoutInflater().inflate(R.layout.loginpopup,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view);
        urlAlertDialog = builder.create();
        urlAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        urlAlertDialog.show();

        TextView popuptitle = (TextView) view.findViewById(R.id.title);
        addkeys = (EditText) view.findViewById(R.id.layout_url);
        ImageView Cancel = (ImageView) view.findViewById(R.id.close);
        Button submit = (Button) view.findViewById(R.id.button);
        submit.setTypeface(general.mediumtypeface());
        popuptitle.setTypeface(general.mediumtypeface());


        String baseurl = general.ApiBaseUrl();
        if (baseurl != null && !baseurl.equalsIgnoreCase("")) {
            addkeys.setText(baseurl);
        }

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(addkeys);
                urlAlertDialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(addkeys);
                submitPopupForm();
            }
        });









       // dialog.show();
       /* addkeys.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        Window windo = dialog.getWindow();
        windo.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
   */ }

    private void hideSoftKeyboard(View addkeys) {
        if (addkeys != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(addkeys.getWindowToken(), 0);
        }
    }


    /*******
     * Login Webservice API call
     * *******/
    private void InitiateLoginWebservice() {
        if (Connectivity.isConnected(this)) {
            general.showloading(LoginActivity.this);
            getUserInfo();
            InitiategetStoreUserLoginTask(0, false, null);
        } else {
            Connectivity.showNoConnectionDialog(this);
            general.hideloading();
        }
    }

    public void getUserInfo() {
        lemail = email_input.getText().toString().trim();
        lpwd = pwd_input.getText().toString().trim();
    }

    private void InitiateUserLoginTask() {
        getUserInfo();
        String url = general.ApiBaseUrl() + SettingsUtils.Login_STORE;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);
        requestData.setEmail(lemail);
        requestData.setPwd(encrptPass);
        requestData.setRequestBody(RequestParam.LoginRequestParams(requestData));

        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(LoginActivity.this, requestData, SettingsUtils.POST);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                parseLoginDataResponse(requestData.getResponse());
            }
        });
        webserviceTask.execute();
    }

    private void InitiategetStoreUserLoginTask(Integer agencyBranchId, boolean branchChoosen, Dialog dialog) {
        getUserInfo();
        String url = general.ApiBaseUrl() + SettingsUtils.GETSTORE_PASSWORD_KEY;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);
        requestData.setEmail(lemail);
//        requestData.setPwd(lpwd);
        requestData.setRequestBody(RequestParam.StoreLoginRequestParams(requestData));

        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(LoginActivity.this, requestData, SettingsUtils.POST);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
//                Log.e("Get Store", requestData.getResponse());
                parseGetStoreDataResponse(requestData.getResponse(), agencyBranchId, branchChoosen, dialog);
            }
        });
        webserviceTask.execute();
    }

    private void parseGetStoreDataResponse(String response, Integer agencyBranchId, boolean branchChoosen, Dialog dialog) {
        String result = "";
        if (response != null) {
            GetStoreModel storeModel = new Gson().fromJson(response, GetStoreModel.class);
            msg = storeModel.getMsg();
            String data = storeModel.getData();
//                    data="o1sSD6lHTpRrI581uB=8yAaWD0E7TvIz";
            String s = data.substring(0, 16);
            encrptPass = EncryptionKeyGenerator.encrypt(data, s, lpwd);
            Log.e(TAG, "encryptedPassword => " + encrptPass);
            String decryptedPassword = EncryptionKeyGenerator.decrypt(data, s, encrptPass);
            Log.e(TAG, "decryptedPassword => " + decryptedPassword);


            if (!branchChoosen)
                InitiateUserLoginTask();
            else {
                general.hideloading();
                generateToken(agencyBranchId, true, dialog);
            }
           /* try {
                JSONObject object = new JSONObject(response);
                if (object.has("data")) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }

    }

    private void parseLoginDataResponse(String response) {

        DataResponse dataResponse = ResponseParser.parseLoginResponse(response);
        String result = "";
        if (response != null) {
            result = dataResponse.status;
            msg = dataResponse.msg;
            info = dataResponse.info;
            loginModel = dataResponse.loginModel;

            try {
                JSONObject object = new JSONObject(response);
                if (object.has("data")) {
                    JSONObject objects = object.getJSONObject("data");
                    String agencyId = objects.getString("AgencyId");
                    String branchId = objects.getString("BranchId");

                    Log.e(TAG, "parseLoginDataResponse: ==>" + agencyId + "\n" + branchId);

                    SettingsUtils.getInstance().putValue(SettingsUtils.BranchId, branchId);
                    SettingsUtils.getInstance().putValue(SettingsUtils.AgencyId, agencyId);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        if (result != null) {
            Log.e("result_is", "::: " + result);
            if (result.equals("1")) {
                general.hideloading();
                generateToken(0, false, null);
            } else if (result.equals("2")) {
                general.hideloading();
                general.CustomToast(getResources().getString(R.string.not_validuser));
            } else if (result.equals("0")) {
                general.hideloading();
                general.CustomToast(getResources().getString(R.string.wrong_password));
            }
        } else {
            general.hideloading();
            general.CustomToast(getResources().getString(R.string.serverProblem));
        }
    }

    private void generateToken(Integer branchId, boolean branchChoose, Dialog dialog) {

        Log.e(TAG, "generateToken: " + branchId + "==" + branchChoose);
        general.showloading(LoginActivity.this);
        String url = general.ApiBaseUrl() + SettingsUtils.SecurityToken;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);
        if (branchChoose)
            requestData.setEmail(lemail + "," + branchId);
        else
            requestData.setEmail(lemail);
        requestData.setPwd(encrptPass);
        requestData.setGrantType("password");
        requestData.setRequestBody(RequestParam.TokenRequestParams(requestData));

        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(LoginActivity.this, requestData, SettingsUtils.POST);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                parseTokenResponse(requestData.getResponse(), branchChoose, dialog);
            }
        });
        webserviceTask.execute();
    }

    private void parseTokenResponse(String response, boolean branchChoose, Dialog dialog) {
        SecurityToken token = new Gson().fromJson(response, SecurityToken.class);

        if (token.getError() != null && !token.getError().isEmpty()) {
            general.hideloading();
            general.CustomToast(getResources().getString(R.string.not_validuser));
        } else {
            Log.e(TAG, "parseTokenResponse: " + token.getAccessToken());
            SettingsUtils.getInstance().putValue(SettingsUtils.KEY_TOKEN,
                    "Bearer " + token.getAccessToken());
            // Get offline count
            if (branchChoose) {
                if (dialog != null)
                    dialog.dismiss();
                InitiateOfflineCaseCount();
            } else
                initaiteGetEmployeeBranch();

//
        }
    }

    private void initaiteGetEmployeeBranch() {
        general.hideloading();
        general.showloading(LoginActivity.this);
        String url = general.ApiBaseUrl() + SettingsUtils.EmployeeBranches;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setInitQueryUrl(url);
        requestData.setEmpId(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGIN_ID, ""));
        requestData.setUrl(RequestParam.GetEmployeeBranchs(requestData));
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));


        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(LoginActivity.this,
                requestData, SettingsUtils.GET_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                parseBranchResponse(requestData.getResponse(),
                        requestData.getResponseCode(), requestData.isSuccessful());
            }
        });
        webserviceTask.execute();
    }

    private void parseBranchResponse(String response, int responseCode, boolean isSuccessful) {


        Log.e(TAG, "parseBranchResponse: " + response);
        MultiBranchModel multiModel = new Gson().fromJson(response, MultiBranchModel.class);
        if (!isSuccessful && responseCode == 400 || responseCode == 401) {
            general.hideloading();
            General.sessionDialog(LoginActivity.this);
        } else {
            if (multiModel != null) {
                if (multiModel.getStatus() == 1) {
                    general.hideloading();
                    showMultiBranchPopup(multiModel);
                } else if (multiModel.getStatus() == 2) {
                    general.CustomToast(msg);
                    general.hideloading();
                } else if (multiModel.getStatus() == 0) {
                    general.CustomToast(msg);
                    general.hideloading();
                }
            } else {
                general.CustomToast(getResources().getString(R.string.serverProblem));
                general.hideloading();
            }
        }
    }

    private void showMultiBranchPopup(MultiBranchModel multiModel) {
        general.hideloading();
        /*final Dialog dialog = new Dialog(LoginActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.branch_popup);*/


        View view  = getLayoutInflater().inflate(R.layout.branch_popup,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view);
        AlertDialog branchPopup = builder.create();
        branchPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Spinner spSalution = view.findViewById(R.id.spBranch);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        branchPopup.setCancelable(false);
        branchPopup.setCanceledOnTouchOutside(false);

        multiModel.getData().add(0, new MultiBranchModel.Datum("Select Branch", 0));

        ArrayAdapter<MultiBranchModel.Datum> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.row_spinner_item_, multiModel.getData());
        arrayAdapter.setDropDownViewResource(R.layout.row_spinner_item_popup);
        spSalution.setAdapter(arrayAdapter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = spSalution.getSelectedItemPosition();

                if (pos > 0) {
                    SettingsUtils.getInstance().putValue(SettingsUtils.BranchId, multiModel.getData().get(pos).getAgencyBranchId().toString());
//                    SettingsUtils.getInstance().putValue(SettingsUtils.AgencyId, multiModel.getData().get(pos).getAgencyBranchId().toString());

                    InitiategetStoreUserLoginTask(multiModel.getData().get(pos).getAgencyBranchId(), true, dialog);
                } else {
                    General.customToast("Choose the branch", LoginActivity.this);
                }

//                validateCreateCase(spSalution, etName, spPropertyType, spBankName, spCaseAdmin, spReportMaker, dialog);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                branchPopup.dismiss();
            }
        });

        branchPopup.show();
    }


    /*******
     * Current Server Url Webservice API call
     * *******/
    private void CurrentServerUrlWebservice() {
        if (Connectivity.isConnected(this)) {
            general.showloading(LoginActivity.this);
            InitiateCurrentServerTask();
        } else {
            Connectivity.showNoConnectionDialog(this);
        }
    }

    private void InitiateCurrentServerTask() {

        String url = addkeys.getText().toString().trim() + SettingsUtils.CurrentServer;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);
        requestData.setValidUrl(true);

        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(LoginActivity.this, requestData, SettingsUtils.GET);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                parseCurrentServerData(requestData.getResponse(), requestData.getResponseCode(),
                        requestData.isSuccessful());
            }
        });
        webserviceTask.execute();
    }

    private void parseCurrentServerData(String response, int responseCode, boolean successful) {


        if (successful) {
            DataResponse dataResponse = ResponseParser.parseCurrentServerResponse(response);
            String result = "";
            if (response != null) {
                Log.e(TAG, "parseCurrentServerData: " + response.toString());
                result = dataResponse.status;
                msg = dataResponse.msg;
                info = dataResponse.info;
            } else {
                general.CustomToast(getResources().getString(R.string.validurl));
            }

            if (result != null) {
                if (result.equals("1")) {
                    // general.CustomToast(msg);
                    String API_BASE_URL = addkeys.getText().toString().trim();
                    SettingsUtils.getInstance().putValue(SettingsUtils.API_BASE_URL, API_BASE_URL);
                   if(dialog!=null)
                    dialog.dismiss();

                   if(urlAlertDialog!=null)
                       urlAlertDialog.dismiss();


                } else if (result.equals("2")) {
                    general.CustomToast(msg);
                } else if (result.equals("0")) {
                    general.CustomToast(msg);
                }
            } else {
                general.CustomToast(getResources().getString(R.string.serverProblem));
            }
        } else if (!successful && (responseCode == 400 || responseCode == 401)) {
            General.sessionDialog(LoginActivity.this);
        } else {
            if(Connectivity.isConnected(this) && !successful && responseCode == 0){
              General.customToastLong(getString(R.string.validurl),LoginActivity.this);
            }else{
                General.customToast(getString(R.string.something_wrong), LoginActivity.this);

            }
             }
        general.hideloading();
    }


    /*******
     *  Validating Call Server Input params
     * *******/
    private boolean validatePopupInputParams() {
        String currentserver = addkeys.getText().toString().trim();
        if (currentserver.isEmpty()) {
            addkeys.setError(getString(R.string.err_msg_serverurl));
            requestFocus(addkeys);
            return false;
        }
        return true;
    }

    /*******
     * Validating Login Input params
     * *******/
    private boolean validateEmailPassword() {
        String emailreg = email_input.getText().toString().trim();
        if (emailreg.isEmpty() || !isValidEmail(emailreg)) {
            email_input.setError(getString(R.string.err_msg_email));
            requestFocus(email_input);
            return false;
        } else if (pwd_input.getText().toString().trim().isEmpty()) {
            pwd_input.setError(getString(R.string.err_msg_password));
            requestFocus(pwd_input);
            return false;
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(LoginActivity.this,
                requestData, SettingsUtils.GET_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                parseOfflineCaseCountLimit(requestData.getResponse(),
                        requestData.getResponseCode(), requestData.isSuccessful());
            }
        });
        webserviceTask.execute();
    }

    private void parseOfflineCaseCountLimit(String response, int responseCode, boolean successful) {
        if (successful) {
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
                            // Hide offline button
                            SettingsUtils.getInstance().putValue(SettingsUtils.KEY_INTERNAL_COMPOSTION_NOT_COPY, false);
                        }
                    } else {
                        // Hide offline button
                        SettingsUtils.getInstance().putValue(SettingsUtils.KEY_INTERNAL_COMPOSTION_NOT_COPY, false);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Get Drop down API
            InitiateGetFieldDropDownTask();

        } else if (!successful && (responseCode == 400 || responseCode == 401)) {
            General.sessionDialog(LoginActivity.this);
        } else {
            General.customToast(getString(R.string.something_wrong), LoginActivity.this);
        }
    }

    /*******
     * GetFieldDropDowns Webservice API call
     * *******/
    private void InitiateGetFieldDropDownTask() {
        String url = general.ApiBaseUrl() + SettingsUtils.GetFieldDropDowns;
        // String url = SettingsUtils.BASE_URL + SettingsUtils.GetFieldDropDowns;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(LoginActivity.this,
                requestData, SettingsUtils.GET_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                dropDownResponse(requestData.getResponse(), requestData.getResponseCode(),
                        requestData.isSuccessful());
            }
        });
        webserviceTask.execute();
    }

    private void dropDownResponse(String response, int responseCode, boolean successful) {
        if (successful) {
            parseGetDropDownsDataResponse(response);
            SettingsUtils.getInstance().putValue(SettingsUtils.DropDownSave, response);
        } else if (!successful && (responseCode == 400 || responseCode == 401)) {
            General.sessionDialog(LoginActivity.this);
        } else {
            General.customToast(getString(R.string.something_wrong), LoginActivity.this);
        }
    }

    private void parseGetDropDownsDataResponse(String response) {
        DataResponse dataResponse = ResponseParser.parseGetFieldDropDownResponse(response);
        String result = "";
        if (response != null) {
            result = dataResponse.status;
            msg = dataResponse.msg;
            info = dataResponse.info;
        }

        if (result != null) {
            if (result.equals("1")) {
                //
            } else if (result.equals("2")) {
                general.CustomToast(msg);
            } else if (result.equals("0")) {
                general.CustomToast(msg);
            }
        } else {
            general.CustomToast(getResources().getString(R.string.serverProblem));
        }
        general.CustomToast("Login Successful");
        if (latt > 0.0d) {
            String fieldStaffId = SettingsUtils.getInstance().getValue(SettingsUtils.KEY_LOGIN_ID, "");


            if (general.isNetworkAvailable()) {
                locationTrackerApi.shareLocation("", fieldStaffId, "Login", latt, longi);
            } else {
                General general2 = this.general;
                General.customToast("Please check your Internet Connection!", this);
            }
        }


        SettingsUtils.getInstance().putValue(SettingsUtils.KEY_LOGGED_IN, true);
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        if ((dialog != null) && dialog.isShowing()) {
            dialog.dismiss();
        }
        general.hideloading();
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
                        new GpsUtils(LoginActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
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
            }
        }

    }

    private void makeLocationUpadte() {
        offlineLocationReceiver = new OfflineLocationReceiver(this, this);
    }

    @Override
    public void sendLocationUpdate(double latitude, double longitude) {
        latt = latitude;
        longi = longitude;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
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

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            general.checkPermissions();
                        }
                    }, 500);

                }


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

    @Override
    public void onSuccess(@NonNull SafetyNetApi.AttestationResponse attestationResponse) {
        SafeNetModel sNet = SafetyNetChecker.decodeJWS(attestationResponse.getJwsResult());
        if (sNet.getBasicIntegrity() && sNet.getCtsProfileMatch()) {
            initValues();
        } else {
            General.exitDialog(LoginActivity.this, "Your phone is rooted,\nPlease unroot to use " +
                    "app");
        }
    }
}




