package com.realappraiser.gharvalue.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ViewAnimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.LocationBias;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.adapter.LatLngAdapter;
import com.realappraiser.gharvalue.adapter.PlacePredictionAdapter;
import com.realappraiser.gharvalue.model.places.GeocodingResult;
import com.realappraiser.gharvalue.model.places.InfoWindowData;
import com.realappraiser.gharvalue.utils.CustomInfoWindowGoogleMap;
import com.realappraiser.gharvalue.utils.General;
import com.realappraiser.gharvalue.utils.SettingsUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlacePickerActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.view_animator)
    ViewAnimator viewAnimator;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.okBtn)
    TextView okBtn;
    @BindView(R.id.cancelBtn)
    TextView cancelBtn;

    private GoogleMap map;
    double mLatitude, mLongitude;
    private static final String TAG = PlacePickerActivity.class.getSimpleName();

    private PlacesClient placesClient;
    private Handler handler = new Handler();
    private AutocompleteSessionToken sessionToken;
    private PlacePredictionAdapter adapter = new PlacePredictionAdapter();
    private Gson gson = new GsonBuilder().registerTypeAdapter(LatLng.class, new LatLngAdapter())
            .create();
    private RequestQueue queue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);
        ButterKnife.bind(this);

        initiateToolbar();

        Places.initialize(this, getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        queue = Volley.newRequestQueue(this);
        initRecyclerView();
    }

    private void initiateToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Property Location");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initRecyclerView() {
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView
                .addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
       /* adapter.setPlaceClickListener(new PlacePredictionAdapter.OnPlaceClickListener() {
            @Override
            public void onPlaceClicked(com.google.android.gms.location.places.AutocompletePrediction place) {
                geocodePlaceAndDisplay(place);
            }
        });*/

        adapter.setPlaceClickListener(new PlacePredictionAdapter.OnPlaceClickListener() {
            @Override
            public void onPlaceClicked(AutocompletePrediction place) {
                geocodePlaceAndDisplay(place);
            }
        });
    }

    private void geocodePlaceAndDisplay(AutocompletePrediction autocompletePrediction) {

        final String apiKey = getString(R.string.google_maps_key);
        final String url = "https://maps.googleapis.com/maps/api/geocode/json?place_id=%s&key=%s";
        final String requestURL = String.format(url, autocompletePrediction.getPlaceId(), apiKey);


        Log.e(TAG, "geocodePlaceAndDisplay: "+requestURL );

        // Use the HTTP request URL for Geocoding API to get geographic coordinates for the place
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestURL, null,
                response -> {
                    try {
                        // Inspect the value of "results" and make sure it's not empty
                        JSONArray results = response.getJSONArray("results");
                        if (results.length() == 0) {
                            Log.w(TAG, "No results from geocoding request.");
                            return;
                        }

                        // Use Gson to convert the response JSON object to a POJO
                        GeocodingResult result = gson.fromJson(
                                results.getString(0), GeocodingResult.class);
                        displayDialog(autocompletePrediction, result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e(TAG, "Request failed"));

        queue.add(request);
    }

/*
    private void geocodePlaceAndDisplay(AutocompletePrediction placePrediction) {

        final String apiKey = getString(R.string.google_maps_key);
        final String url = "https://maps.googleapis.com/maps/api/geocode/json?place_id=%s&key=%s";
        final String requestURL = String.format(url, placePrediction.getPlaceId(), apiKey);


        Log.e(TAG, "geocodePlaceAndDisplay: "+requestURL );

        // Use the HTTP request URL for Geocoding API to get geographic coordinates for the place
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestURL, null,
                response -> {
                    try {
                        // Inspect the value of "results" and make sure it's not empty
                        JSONArray results = response.getJSONArray("results");
                        if (results.length() == 0) {
                            Log.w(TAG, "No results from geocoding request.");
                            return;
                        }

                        // Use Gson to convert the response JSON object to a POJO
                        GeocodingResult result = gson.fromJson(
                                results.getString(0), GeocodingResult.class);
                        displayDialog(placePrediction, result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e(TAG, "Request failed"));

        queue.add(request);
    }
*/


    private void displayDialog(AutocompletePrediction place, GeocodingResult result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        String msg = "Would you like change the location of the property to below address ?\n\nAddress:\n"
                + place.getPrimaryText(null) + "\n" + place.getSecondaryText(null);
        builder.setTitle("Warning").setMessage(msg)
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (viewAnimator.getVisibility() == View.VISIBLE)
                        viewAnimator.setVisibility(View.GONE);
                    moveToNewLocation(result.geometry.location.latitude, result.geometry.location.longitude);
                }).setNegativeButton("No", (dialog, which) -> {
            if (viewAnimator.getVisibility() == View.VISIBLE)
                viewAnimator.setVisibility(View.GONE);
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @OnClick(R.id.fab)
    public void setCurrentLocationMark() {
        showCautionDialog();
    }

    @OnClick(R.id.okBtn)
    public void submit() {
        setResult(Activity.RESULT_OK, new Intent().putExtra("LATLNG", mLatitude + "," + mLongitude));
        finish();
    }

    @OnClick(R.id.cancelBtn)
    public void closeActivity() {
        setResult(Activity.RESULT_CANCELED, new Intent());
        finish();
    }


    private void moveToNewLocation(double latt, double longg) {
        mLatitude = latt;
        mLongitude = longg;
        map.clear();
        LatLng latLng = new LatLng(latt, longg);
        InfoWindowData info = getAddress(latt, longg);
        Drawable circleDrawable = getResources().getDrawable(R.drawable.ic__building_pin);

        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title(info.getTitle()).snippet(info.getAddress()).icon(General.getMarkerIconFromDrawable(circleDrawable));


        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        map.setInfoWindowAdapter(customInfoWindow);

        Marker m = map.addMarker(markerOptions);
        assert m != null;
        m.setTag(info);
        m.showInfoWindow();
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    private void showCautionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));

        builder.setTitle("Warning")
                .setMessage("Would you like change the location of the property to your current location?")
                .setPositiveButton("Yes",
                        (dialog, which) ->
                                moveToNewLocation(SettingsUtils.Latitudes, SettingsUtils.Longitudes))
                .setNegativeButton("No", (dialog, which) -> {
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(SettingsUtils.Latitudes, SettingsUtils.Longitudes);
        if (getIntent().hasExtra("LATLNG")) {
            String latlng = getIntent().getStringExtra("LATLNG");
            String[] ltlg = latlng.split(",");
            Log.e(TAG, "onMapReady: " + latlng);
            latLng = new LatLng(Double.parseDouble(ltlg[0]), Double.parseDouble(ltlg[1]));
        }
        moveToNewLocation(latLng.latitude, latLng.longitude);


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                moveToNewLocation(latLng.latitude, latLng.longitude);
            }
        });
    }

    public InfoWindowData getAddress(double latt, double longg) {
        InfoWindowData info = new InfoWindowData();

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses;
            addresses = geocoder.getFromLocation(latt, longg, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String featureName = addresses.get(0).getFeatureName();
            String subLocal = addresses.get(0).getSubLocality();
            Log.e(TAG, "getAddress: " + new Gson().toJson(addresses));

            String title = "Address";

            if (!featureName.isEmpty())
                title = featureName;
            else if (!subLocal.isEmpty())
                title = subLocal;
            else
                title = city;

            info.setTitle(title);
            info.setAddress(address);
            return info;

        } catch (IOException exception) {
            exception.printStackTrace();
            info.setTitle("");
            info.setAddress("");
            return info;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        initSearchView(menu, searchView);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search) {

            if (viewAnimator.getVisibility() == View.GONE) {
                viewAnimator.setVisibility(View.VISIBLE);
            }

            sessionToken = AutocompleteSessionToken.newInstance();
            return false;
        }

        if (id == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED, new Intent());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSearchView(Menu menu, SearchView searchView) {

        searchView.setQueryHint(getString(R.string.search_a_place));
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (viewAnimator.getVisibility() == View.GONE)
                    viewAnimator.setVisibility(View.VISIBLE);


                progressBar.setIndeterminate(true);

                // Cancel any previous place prediction requests
                handler.removeCallbacksAndMessages(null);

                // Start a new place prediction request in 300 ms
                handler.postDelayed(() -> {
                    getPlacePredictions(newText);
                }, 300);
                return true;
            }
        });

    }

    private void getPlacePredictions(String query) {
        // The value of 'bias' biases prediction results to the rectangular region provided
        // (currently Kolkata). Modify these values to get results for another area. Make sure to
        // pass in the appropriate value/s for .setCountries() in the
        // FindAutocompletePredictionsRequest.Builder object as well.
        final LocationBias bias = RectangularBounds.newInstance(
                new LatLng(22.458744, 88.208162), // SW lat, lng
                new LatLng(22.730671, 88.524896) // NE lat, lng
        );

        // Create a new programmatic Place Autocomplete request in Places SDK for Android
        final FindAutocompletePredictionsRequest newRequest = FindAutocompletePredictionsRequest
                .builder()
                .setSessionToken(sessionToken)
                .setLocationBias(bias)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setQuery(query)
                .setCountries("IN")
                .build();

        // Perform autocomplete predictions request
        placesClient.findAutocompletePredictions(newRequest).addOnSuccessListener((response) -> {
            List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
            adapter.setPredictions(predictions);

            progressBar.setIndeterminate(false);
            viewAnimator.setDisplayedChild(predictions.isEmpty() ? 0 : 1);
        }).addOnFailureListener((exception) -> {
            progressBar.setIndeterminate(false);
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });
    }

}
