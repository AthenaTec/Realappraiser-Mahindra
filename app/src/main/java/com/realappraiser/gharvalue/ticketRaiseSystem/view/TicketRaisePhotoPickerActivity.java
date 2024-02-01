package com.realappraiser.gharvalue.ticketRaiseSystem.view;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.adapter.ImageAdapter;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.ItemOffsetDecoration;
import com.realappraiser.gharvalue.utils.SettingsUtils;
import java.util.ArrayList;

public class TicketRaisePhotoPickerActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static ImageAdapter imageAdapter;
    private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;
    private General general;
    private String mBase64 = "", page = null;
    private ArrayList<String> selectedItems;
    boolean is_fragment_flat_ka = false;
    boolean real_appraiser_jaipur = false;
    private static final String TAG = "TicketRaisePhotoPicker";
    private int selectedPhotoSize;
    private boolean photolanlat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_raise_photo_picker);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !getIntent().hasExtra("photolanlat")) {
            String page_is = bundle.getString("page_is");
            is_fragment_flat_ka = true;
            Snackbar.make(findViewById(R.id.button1), getResources().getString(R.string.one_photo), Snackbar.LENGTH_SHORT).show();
            if (getIntent().hasExtra("page"))
                page = getIntent().getStringExtra("page");
        } else {
            if (getIntent().hasExtra("available_photo_size"))
                selectedPhotoSize = getIntent().getIntExtra("available_photo_size", 0);

            if (getIntent().hasExtra("photolanlat")) {
                photolanlat = getIntent().getBooleanExtra("photolanlat", false);
            }
            is_fragment_flat_ka = false;
        }
        real_appraiser_jaipur = SettingsUtils.getInstance().getValue(SettingsUtils.real_appraiser_jaipur, false);
        general = new General(TicketRaisePhotoPickerActivity.this);
        populateImagesFromGallery();
    }

    public void btnChoosePhotosClick(final View v) {
        v.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
                Log.e(TAG, "run: " + new Gson().toJson(selectedItems));
                if (selectedItems != null && selectedItems.size() > 0) {
                    v.setVisibility(View.GONE);
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("multiple_photo", selectedItems);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    general.hideloading();
                    v.setVisibility(View.VISIBLE);
                    general.CustomToast(getResources().getString(R.string.selectmsg));
                }
            }
        }, 1000);
    }

    private void populateImagesFromGallery() {
        if (!mayRequestGalleryImages()) {
            return;
        }

        ArrayList<String> imageUrls = new ArrayList<>();
        if (page != null && page.contains(TicketRaisePhotoPickerActivity.class.getSimpleName()))
            imageUrls = loadAllFiles();
        else
            imageUrls = loadPhotosFromNativeGallery();
        initializeRecyclerView(imageUrls);
    }

    private boolean mayRequestGalleryImages() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (Build.VERSION.SDK_INT < 33) {
            if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        } else {
            if (checkSelfPermission(READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }


        if (Build.VERSION.SDK_INT < 33) {
            if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                //promptStoragePermission();
                showPermissionRationaleSnackBar();
            } else {
                requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, REQUEST_FOR_STORAGE_PERMISSION);
            }
        } else {
            if (shouldShowRequestPermissionRationale(READ_MEDIA_IMAGES)) {
                showPermissionRationaleSnackBar();
            } else {
                requestPermissions(new String[]{READ_MEDIA_IMAGES}, REQUEST_FOR_STORAGE_PERMISSION);
            }
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_FOR_STORAGE_PERMISSION: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        populateImagesFromGallery();
                    } else {
                        if (Build.VERSION.SDK_INT < 33) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                                showPermissionRationaleSnackBar();
                            } else {
                                Toast.makeText(this, "Go to settings and enable permission", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_MEDIA_IMAGES)) {
                                showPermissionRationaleSnackBar();
                            } else {
                                Toast.makeText(this, "Go to settings and enable permission", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                }
                break;
            }
        }
    }

    private ArrayList<String> loadPhotosFromNativeGallery() {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        //noinspection deprecation
        @SuppressWarnings("deprecation")
        Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");

        ArrayList<String> imageUrls = new ArrayList<String>();

        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));
            System.out.println("=====> Array path => " + imageUrls.get(i));
        }
        return imageUrls;
    }


    private ArrayList<String> loadDOCX() {
        ContentResolver cr = getContentResolver();
        Uri uri = MediaStore.Files.getContentUri("external");
        String[] projection = null;
        final String orderBy = MediaStore.Files.FileColumns.DATE_TAKEN;
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE;
        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx");
        Log.e("loadDOCX", "loadDOCX: " + mimeType);
        String[] selectionArgsPdf = new String[]{"application/vnd.openxmlformats-officedocument.wordprocessingml.document"};
        Cursor cursor = cr.query(uri, projection, selectionMimeType, selectionArgsPdf, orderBy + " DESC");
        assert cursor != null;
        ArrayList<String> imageUrls = new ArrayList<String>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int dataColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            String filePath = cursor.getString(dataColumn);
            imageUrls.add(filePath);
        }
        cursor.close();
        return imageUrls;
    }

    private ArrayList<String> loadDOC() {
        ContentResolver cr = getContentResolver();
        Uri uri = MediaStore.Files.getContentUri("external");
        String[] projection = null;
        final String orderBy = MediaStore.Files.FileColumns.DATE_TAKEN;
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE;
        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("msword");
        Log.e("loadDOC", "loadDOC: " + mimeType);

        String[] selectionArgsPdf = new String[]{"application/msword"};
        Cursor cursor = cr.query(uri, projection, selectionMimeType, selectionArgsPdf, orderBy + " DESC");
        assert cursor != null;
        ArrayList<String> imageUrls = new ArrayList<String>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int dataColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            String filePath = cursor.getString(dataColumn);
            imageUrls.add(filePath);
        }
        cursor.close();
        return imageUrls;
    }


    private ArrayList<String> loadPDF() {
        ContentResolver cr = getContentResolver();
        Uri uri = MediaStore.Files.getContentUri("external");
        String[] projection = null;
        final String orderBy = MediaStore.Files.FileColumns.DATE_TAKEN;
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE;
        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        Log.e("loadPDF", "loadPDF: " + mimeType);
        String[] selectionArgsPdf = new String[]{"application/pdf"};
        Cursor cursor = cr.query(uri, projection, selectionMimeType, selectionArgsPdf, orderBy + " DESC");
        assert cursor != null;
        ArrayList<String> imageUrls = new ArrayList<String>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int dataColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            String filePath = cursor.getString(dataColumn);
            imageUrls.add(filePath);
        }
        System.out.println("loadPDF" + imageUrls);
        cursor.close();
        return imageUrls;
    }

    private ArrayList<String> loadAllFiles() {

        ArrayList<String> list = loadPDF();

        for (String str : loadDOC()) {
            list.add(str);
        }

        for (String str : loadDOCX()) {
            list.add(str);
        }

        for (String str : loadPhotosFromNativeGallery()) {
            list.add(str);
        }

        return list;
    }

    private void initializeRecyclerView(ArrayList<String> imageUrls) {
        imageAdapter = new ImageAdapter(this, imageUrls, selectedPhotoSize, photolanlat, true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemOffsetDecoration(this, R.dimen.item_offset));
        recyclerView.setAdapter(imageAdapter);
    }

    private void showPermissionRationaleSnackBar() {
        Snackbar.make(findViewById(R.id.button1), getString(R.string.permission_rationale),
                Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Request the permission
                if (Build.VERSION.SDK_INT < 33) {
                    ActivityCompat.requestPermissions(TicketRaisePhotoPickerActivity.this,
                            new String[]{READ_EXTERNAL_STORAGE},
                            REQUEST_FOR_STORAGE_PERMISSION);
                } else {
                    ActivityCompat.requestPermissions(TicketRaisePhotoPickerActivity.this,
                            new String[]{READ_MEDIA_IMAGES},
                            REQUEST_FOR_STORAGE_PERMISSION);
                }
            }
        }).show();
    }
}