package com.realappraiser.gharvalue.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.adapter.DocumentAdapter;
import com.realappraiser.gharvalue.adapter.ImageAdapter;
import com.realappraiser.gharvalue.adapter.PropertyDocumentNewFileAdapter;
import com.realappraiser.gharvalue.communicator.DataResponse;
import com.realappraiser.gharvalue.communicator.JsonRequestData;
import com.realappraiser.gharvalue.communicator.RequestParam;
import com.realappraiser.gharvalue.communicator.ResponseParser;
import com.realappraiser.gharvalue.communicator.TaskCompleteListener;
import com.realappraiser.gharvalue.communicator.WebserviceCommunicator;
import com.realappraiser.gharvalue.model.Document_list;
import com.realappraiser.gharvalue.model.LoadQcModel;
import com.realappraiser.gharvalue.model.PropertyDocModel;
import com.realappraiser.gharvalue.property.DeleteItemInterface;
import com.realappraiser.gharvalue.utils.Connectivity;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.SettingsUtils;
import com.realappraiser.gharvalue.utils.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;

public class PropertyDocumentsActivity extends AppCompatActivity implements View.OnClickListener, DeleteItemInterface {

    private RecyclerView rvExistingFiles, rvNewFiles;

    private DocumentAdapter documentAdapter;

    private RelativeLayout rlCamera, rlFiles, rlUpload;
    private Toolbar toolbar;
    private ArrayList<PropertyDocModel> selectedDocumentList = new ArrayList<>();
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 4;
    private static final int CAMERA_REQUEST = 123;
    private int PICK_IMAGE_REQUEST = 2;
    private static final int GALLERY_REQUEST = 5;
    private int REQUEST = 0;
    private static final String TAG = PropertyDocumentsActivity.class.getSimpleName();
    private int caseId;

    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
    private String[] androidHigherVersionPermission = new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.CAMERA };

    PropertyDocumentNewFileAdapter newFileAdapter;

    private General general;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propertydocuments);
        general = new General(this);

        toolbar = findViewById(R.id.toolbar);
        rvExistingFiles = findViewById(R.id.rvExistingFiles);
        rvNewFiles = findViewById(R.id.rvNewFiles);
        rlCamera = findViewById(R.id.rlCamera);
        rlFiles = findViewById(R.id.rlFiles);
        rlUpload = findViewById(R.id.rlUpload);

        rlCamera.setOnClickListener(this);
        rlFiles.setOnClickListener(this);
        rlUpload.setOnClickListener(this);

        initiateToolbar();

        if (getIntent().hasExtra("caseId")) {
            String caseid_ = getIntent().getStringExtra("caseId");
            caseId = Integer.parseInt(caseid_);
            InitiateDocumentReadTask(caseId);
        }
        initiateRecyclerView();

    }

    private void initiateRecyclerView() {
        newFileAdapter = new PropertyDocumentNewFileAdapter(selectedDocumentList, this);
        rvNewFiles.setLayoutManager(new LinearLayoutManager(this));
        rvNewFiles.setHasFixedSize(true);
        rvNewFiles.setAdapter(newFileAdapter);
        rvExistingFiles.setItemAnimator(new DefaultItemAnimator());
        rvExistingFiles.setNestedScrollingEnabled(false);
    }

    private void initiateToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Property Documents");
    }

    private void InitiateDocumentReadTask(int CaseId) {
        General.showloading(PropertyDocumentsActivity.this);
        String url = general.ApiBaseUrl() + SettingsUtils.DocumentRead;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setInitQueryUrl(url);
        requestData.setCaseId(String.valueOf(CaseId));//CaseId
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        requestData.setUrl(RequestParam.DocumentReadRequestParams(requestData));

        WebserviceCommunicator webserviceTask =
                new WebserviceCommunicator(PropertyDocumentsActivity.this, requestData,
                        SettingsUtils.GET_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                if (requestData.isSuccessful()) {
                    parseDocumentReadResponse(requestData.getResponse());
                    SettingsUtils.getInstance().putValue(SettingsUtils.KEY_DOCUMENT, requestData.getResponse());
                } else if (!requestData.isSuccessful() && (requestData.getResponseCode() == 400 || requestData.getResponseCode() == 401)) {
                    general.hideloading();
                    General.sessionDialog(PropertyDocumentsActivity.this);
                } else {
                    general.hideloading();
                    General.customToast(PropertyDocumentsActivity.this.getString(R.string.something_wrong),
                            PropertyDocumentsActivity.this);
                }
            }
        });
        webserviceTask.execute();
    }

    private void parseDocumentReadResponse(String response) {
        String msg = "";

        ArrayList<Document_list> documentRead = new ArrayList<>();

        DataResponse dataResponse = ResponseParser.parseDocumentReadResponse(response);
        String result = "";
        if (response != null) {
            result = dataResponse.status;
            msg = dataResponse.msg;
            documentRead = dataResponse.documentRead;
            Singleton.getInstance().documentRead = dataResponse.documentRead;
            general.hideloading();
            if (documentRead.size() > 0) {
                rvExistingFiles.setLayoutManager(new LinearLayoutManager(this));
                rvExistingFiles.setHasFixedSize(true);
                DeleteItemInterface deleteItemInterface = this;
                documentAdapter = new DocumentAdapter(this, documentRead, 1,deleteItemInterface);
                rvExistingFiles.setAdapter(documentAdapter);
                rvExistingFiles.setItemAnimator(new DefaultItemAnimator());
                rvExistingFiles.setNestedScrollingEnabled(false);
            } else {
                rvExistingFiles.setVisibility(View.GONE);
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
                params.weight = 2;
                rvNewFiles.setLayoutParams(params);
            }

        }

        if (result != null) {
            if (result.equals("1")) {
                general.hideloading();
            } else if (result.equals("2")) {
                general.hideloading();
                general.CustomToast(msg);
            } else if (result.equals("0")) {
                general.hideloading();
                general.CustomToast(msg);
            }
        } else {
            general.hideloading();
            general.CustomToast(getResources().getString(R.string.serverProblem));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlFiles:
                REQUEST = 1;
                if (checkPermissions())
                    takeGalleryPicture();
                break;
            case R.id.rlCamera:
                REQUEST = 0;
                if (checkPermissions())
                    takePicture();
                break;
            case R.id.rlUpload:
                if (selectedDocumentList.size() > 0)
                    uploadDocuments();
                else
                    Toast.makeText(PropertyDocumentsActivity.this, "Choose atleast 1 file to make upload property documents", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void uploadDocuments() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < selectedDocumentList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Id", 0);
                jsonObject.put("DocumentName", selectedDocumentList.get(i).getFilleName());
                jsonObject.put("CaseId", selectedDocumentList.get(i).getCaseId());
                jsonObject.put("Document", selectedDocumentList.get(i).getFile());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            Log.e(TAG, "uploadDocuments: " + jsonArray.getJSONObject(0).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(jsonArray.toString());
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(general.ApiBaseUrl() + SettingsUtils.UploadPropertyDocuments);
        requestData.setPropertyDoc(jsonArray.toString());
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        requestData.setRequestBody(RequestParam.UploadPropertyDocumentsRequestParams(requestData));

        if (Connectivity.isConnected(this)) {
            General.showloading(this);
            WebserviceCommunicator webserviceTask = new WebserviceCommunicator(this, requestData,
                    SettingsUtils.POST_TOKEN);
            webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
                public void onTaskComplete(JsonRequestData requestData) {

                    parseResponse(requestData.getResponse(), requestData.getResponseCode(),
                            requestData.isSuccessful());
//                    startActivity(new Intent(PropertyDocumentsActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            });
            webserviceTask.execute();
        } else {
            Connectivity.showNoConnectionDialog(this);
        }
    }

    private void parseResponse(String response, int responseCode, boolean successful) {
        General.hideloading();
        if (successful) {
            String sb = "onTaskComplete: " + response;
            Log.e(TAG, sb);
            General.hideloading();
            General.customToast("Record Updated", PropertyDocumentsActivity.this);
            finish();
        } else if (!successful && (responseCode == 400 || responseCode == 401)) {
            General.sessionDialog(PropertyDocumentsActivity.this);
        } else {
            General.customToast(getString(R.string.something_wrong), PropertyDocumentsActivity.this);
        }

    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = SettingsUtils.createImageFile(this);
            Log.e(TAG, "takePicture: " + photoFile.getAbsolutePath());
        } catch (IOException ex) {
            StringBuilder sb = new StringBuilder();
            sb.append("takeCameraPicture:");
            sb.append(ex);
            Log.e(TAG, sb.toString());
        }
        if (photoFile != null) {
            takePictureIntent.putExtra("output", FileProvider.getUriForFile(PropertyDocumentsActivity.this,
                    SettingsUtils.FILE_PROVIDER, photoFile));
            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        }
    }

    private void takeGalleryPicture() {
        Intent gallery_select = new Intent(this, MultiPhotoSelectActivity.class);
        gallery_select.putExtra("page", PropertyDocumentsActivity.class.getSimpleName());
        startActivityForResult(gallery_select, GALLERY_REQUEST);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.qc) {
            loadQc();
            return true;
        }

        return false;
    }

    private void loadQc() {
        general.showloading(PropertyDocumentsActivity.this);
        String url = general.ApiBaseUrl() + SettingsUtils.LoadQC;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setInitQueryUrl(url);
        requestData.setCaseId("" + caseId);
        requestData.setUrl(RequestParam.LoadQc(requestData));
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(PropertyDocumentsActivity.this,
                requestData, SettingsUtils.GET_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {


                parseLoadQc(requestData.getResponse(),
                        requestData.getResponseCode(), requestData.isSuccessful());
            }
        });
        webserviceTask.execute();
    }

    private void parseLoadQc(String response, int responseCode, boolean successful) {
        Log.e(TAG, "parseInsertResponse: " + response);

        if (successful) {
            general.hideloading();
            LoadQcModel qcModel = new Gson().fromJson(response, LoadQcModel.class);

            showQcPopup(qcModel.getData().getQCStatus(), qcModel.getData().getQCNote());

        } else if ((responseCode == 400 || responseCode == 401) && !successful) {
            General.sessionDialog(PropertyDocumentsActivity.this);
        } else {
            general.CustomToast(getResources().getString(R.string.serverProblem));
            general.hideloading();
        }
    }

    private void showQcPopup(Boolean qcStatus, String qcNote) {
        final Dialog dialog = new Dialog(PropertyDocumentsActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.qc_popup);

        final RadioGroup rqQc = dialog.findViewById(R.id.rgQC);
        final RadioButton rbQcC = dialog.findViewById(R.id.rbQcC);
        final RadioButton rbQcH = dialog.findViewById(R.id.rbQcH);
        final EditText etNotes = dialog.findViewById(R.id.etNotes);
        final Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        final Button btnCancel = dialog.findViewById(R.id.btnCancel);


        if (qcStatus != null) {
            if (qcStatus)
                rbQcC.setChecked(true);
            else
                rbQcH.setChecked(true);

        }

        if (qcNote != null) {
            etNotes.setText(qcNote);
        }

        btnSubmit.setOnClickListener(v -> validateValues(dialog, rbQcC, rbQcH, etNotes));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void validateValues(Dialog dialog, RadioButton rbQcC, RadioButton rbQcH, EditText etNotes) {
        if (!rbQcC.isChecked() && !rbQcH.isChecked()) {
            General.customToast("Select QC completion status!", PropertyDocumentsActivity.this);
            return;
        } else if (TextUtils.isEmpty(etNotes.getText())) {
            General.customToast("Enter QC notes!", PropertyDocumentsActivity.this);
            return;
        } else {
            general.showloading(PropertyDocumentsActivity.this);
            if (rbQcC.isChecked()) {
                insertQc(1, etNotes.getText().toString());
            } else {
                insertQc(0, etNotes.getText().toString());
            }
            dialog.dismiss();
        }
    }

    private void insertQc(int i, String toString) {
        String url = general.ApiBaseUrl() + SettingsUtils.InsertQC;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setUrl(url);
        requestData.setNotes(toString);
        requestData.setStatus("" + i);
        requestData.setCaseId("" + caseId);
        requestData.setRequestBody(RequestParam.InsertQcRequestParams(requestData));
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        WebserviceCommunicator webserviceTask = new WebserviceCommunicator(PropertyDocumentsActivity.this,
                requestData, SettingsUtils.POST_TOKEN);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                parseInsertResponse(requestData.getResponse(),
                        requestData.getResponseCode(), requestData.isSuccessful());
            }
        });
        webserviceTask.execute();

    }

    private void parseInsertResponse(String response, int responseCode, boolean successful) {

        Log.e(TAG, "parseInsertResponse: " + response);

        if (successful) {
            general.hideloading();
        } else if ((responseCode == 400 || responseCode == 401) && !successful) {
            General.sessionDialog(PropertyDocumentsActivity.this);
        } else {
            general.CustomToast(getResources().getString(R.string.serverProblem));
            general.hideloading();
        }
    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        if(Build.VERSION.SDK_INT < 33){
            for (String p : permissions) {
                result = ContextCompat.checkSelfPermission(PropertyDocumentsActivity.this, p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
        }else{
            for (String p : androidHigherVersionPermission) {
                result = ContextCompat.checkSelfPermission(PropertyDocumentsActivity.this, p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(PropertyDocumentsActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if(Build.VERSION.SDK_INT < 33){
            int write = ContextCompat.checkSelfPermission(PropertyDocumentsActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(PropertyDocumentsActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            int camera = ContextCompat.checkSelfPermission(PropertyDocumentsActivity.this,
                    Manifest.permission.CAMERA);
            if (write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED && camera == PackageManager.PERMISSION_GRANTED) {
                if (REQUEST == 1)
                    takeGalleryPicture();
                else
                    takePicture();
            } else {
                Toast.makeText(PropertyDocumentsActivity.this, "Please allow the permissions requests", Toast.LENGTH_SHORT).show();
                checkPermissions();
            }

        }else{

            int media = ContextCompat.checkSelfPermission(PropertyDocumentsActivity.this,
                    Manifest.permission.READ_MEDIA_IMAGES);
            int camera = ContextCompat.checkSelfPermission(PropertyDocumentsActivity.this,
                    Manifest.permission.CAMERA);
            if (media == PackageManager.PERMISSION_GRANTED && camera == PackageManager.PERMISSION_GRANTED) {
                if (REQUEST == 1)
                    takeGalleryPicture();
                else
                    takePicture();
            } else {
                Toast.makeText(PropertyDocumentsActivity.this, "Please allow the permissions requests", Toast.LENGTH_SHORT).show();
                checkPermissions();
            }

        }


      }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                General.showloading(PropertyDocumentsActivity.this);
                File imgFile = new File(SettingsUtils.mPhotoPath);
                Uri.fromFile(imgFile);
                Log.e("PathNew :", SettingsUtils.mPhotoPath);
                try {
                    File compressedImageFile = new Compressor(PropertyDocumentsActivity.this).compressToFile(imgFile);
                    if (!general.isEmpty(SettingsUtils.mPhotoPath)) {
                        String filename = compressedImageFile.getAbsolutePath().substring(compressedImageFile.getAbsolutePath().lastIndexOf("/") + 1);
                        selectedDocumentList.add(new PropertyDocModel(caseId, General.convertImageToBase64(compressedImageFile.getAbsolutePath()), filename, compressedImageFile.getAbsolutePath()));
                        newFileAdapter.notifyDataSetChanged();
                        General.hideloading();
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            } catch (Exception e) {
                Log.e(TAG, "onActivityResult: " + e.getLocalizedMessage());
            }
        } else if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {

            ImageAdapter imageAdapter = MultiPhotoSelectActivity.imageAdapter;
            convertFiles(imageAdapter.getCheckedItems());
        }
    }


    private void convertFiles(ArrayList<String> strList) {
        General.showloading(this);
        for (String str : strList) {
            if (str.contains("docx") || str.contains("doc") || str.contains("docs") || str.contains("pdf")) {
                String filename = str.substring(str.lastIndexOf("/") + 1);
                selectedDocumentList.add(new PropertyDocModel(caseId, General.convertFileToBase64(str), filename, str));
            } else {
                String filename = str.substring(str.lastIndexOf("/") + 1);
                selectedDocumentList.add(new PropertyDocModel(caseId, General.convertImageToBase64(str), filename, str));
            }
        }
        newFileAdapter.notifyDataSetChanged();
        General.hideloading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_qc, menu);
        return true;
    }

    @Override
    public void deleteItem(String ItemId,int position) {

        General.showloading(PropertyDocumentsActivity.this);
        String url = general.ApiBaseUrl() + SettingsUtils.deleteImage;
        JsonRequestData requestData = new JsonRequestData();
        requestData.setInitQueryUrl(url);
        requestData.setId(ItemId);//CaseId
        requestData.setAuthToken(SettingsUtils.getInstance().getValue(SettingsUtils.KEY_TOKEN, ""));
        requestData.setUrl(RequestParam.deleteImageId(requestData));

        WebserviceCommunicator webserviceTask =
                new WebserviceCommunicator(PropertyDocumentsActivity.this, requestData,
                        SettingsUtils.DELETE_DOCUMENT);
        webserviceTask.setFetchMyData(new TaskCompleteListener<JsonRequestData>() {
            @Override
            public void onTaskComplete(JsonRequestData requestData) {
                if (requestData.isSuccessful()) {
                    documentAdapter.removeFile(position);
                    documentAdapter.notifyDataSetChanged();
                    general.hideloading();
                    General.customToast(getResources().getString(R.string.deleted_successfully),
                            PropertyDocumentsActivity.this);
                 } else if (!requestData.isSuccessful() && (requestData.getResponseCode() == 400 || requestData.getResponseCode() == 401)) {

                    general.hideloading();

                    General.customToast(getResources().getString(R.string.unable_to_delete),
                            PropertyDocumentsActivity.this);
                } else {
                    general.hideloading();
                    General.customToast(getResources().getString(R.string.unable_to_delete),
                            PropertyDocumentsActivity.this);
                }
            }
        });
        webserviceTask.execute();

    }




}
