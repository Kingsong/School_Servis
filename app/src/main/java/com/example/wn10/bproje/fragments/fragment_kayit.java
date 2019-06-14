package com.example.wn10.bproje.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wn10.bproje.BuildConfig;
import com.example.wn10.bproje.Kayit;
import com.example.wn10.bproje.MainActivity;
import com.example.wn10.bproje.R;
import com.example.wn10.bproje.Retrofit.api.RetrofitClient;
import com.example.wn10.bproje.Retrofit.models.DefaultResponse;
import com.example.wn10.bproje.Storage.SharedPrefManager;
import com.example.wn10.bproje.Storage.veri;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_kayit extends Fragment {

    //DatabaseReference HumanDriver;
    //DatabaseReference Student;
    veri DriverLoc = new veri();
    veri StudentLoc = new veri();
    TextView tv;
    EditText OgrOkul,OgrVakit,OgrTelefon,OgrAd,OgrSoyad;
    Button btnStartUpdates;
    Button btnStopUpdates;
    Button btnSafeStudentLocation;
    TextView txtUpdatedOn;
    TextView txtLocationResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.kayitfragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv=view.findViewById(R.id.test);
        OgrOkul = view.findViewById(R.id.Okul);
        OgrVakit = view.findViewById(R.id.Vakit);
        OgrAd = view.findViewById(R.id.Ad);
        OgrSoyad = view.findViewById(R.id.Soyad);
        OgrTelefon = view.findViewById(R.id.Telefon);
        btnStartUpdates = view.findViewById(R.id.btn_start_location_updates);
        btnStopUpdates = view.findViewById(R.id.btn_stop_location_updates);
        btnSafeStudentLocation = view.findViewById(R.id.btn_Safe_Student_Location);
        txtUpdatedOn = view.findViewById(R.id.updated_on);
        txtLocationResult = view.findViewById(R.id.location_result);




        ButterKnife.bind(getActivity());

        // initialize the necessary libraries
        init();

        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState);
        /*
        HumanDriver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    DriverLoc = dataSnapshot.getValue(veri.class);
                    double sonuc = Havasine(StudentLoc.lat,StudentLoc.lng,DriverLoc.lat,DriverLoc.lng);
                    if((sonuc*1000) <1000.0)
                    {
                        Toast.makeText(getApplicationContext(),"Distance between 2 coordinats is -> "+Double.toString(Math.round(sonuc*1000))+" Meter",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Distance between 2 coordinats is -> "+Double.toString(Math.round(sonuc))+" km",Toast.LENGTH_SHORT).show();
                    }

                    //lat:37.8734621 lng:32.4523525
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Student.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                 StudentLoc = dataSnapshot.getValue(veri.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
        btnStartUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRequestingLocationUpdates = true;
                startLocationUpdates();
            }
        });

        btnStopUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRequestingLocationUpdates = false;
                stopLocationUpdates();
            }
        });

        btnSafeStudentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double lat = mCurrentLocation.getLatitude();
                double lng = mCurrentLocation.getLongitude();
                String okul = OgrOkul.getText().toString().trim();
                String vakit = OgrVakit.getText().toString().trim();
                String ad = OgrAd.getText().toString().trim();
                String soyad = OgrSoyad.getText().toString().trim();
                String telefon = OgrTelefon.getText().toString().trim();

                if(okul.isEmpty())
                {
                    OgrOkul.setError("Okul lazım");
                    OgrOkul.requestFocus();
                    return;
                }
                if(vakit.isEmpty())
                {
                    OgrVakit.setError("Vakit lazım");
                    OgrVakit.requestFocus();
                    return;
                }
                if(ad.isEmpty())
                {
                    OgrAd.setError("Ad lazım");
                    OgrAd.requestFocus();
                    return;
                }
                if(soyad.isEmpty())
                {
                    OgrSoyad.setError("Soyad lazım");
                    OgrSoyad.requestFocus();
                    return;
                }
                if(telefon.isEmpty())
                {
                    OgrTelefon.setError("Telefon lazım");
                    OgrTelefon.requestFocus();
                    return;
                }

                Call<DefaultResponse> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .createUser(lat,lng,okul,vakit,ad,soyad,telefon, SharedPrefManager.getInstance(getActivity()).getPlaka());

                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if (response.code() == 201) {

                            DefaultResponse dr = response.body();
                            Toast.makeText(getActivity(), dr.getMsg(), Toast.LENGTH_LONG).show();

                        } else if (response.code() == 422) {
                            Toast.makeText(getActivity(), "User already exist", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });



            }
        });

    }

    private static final String TAG = MainActivity.class.getSimpleName();



    // location last updated time
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Restoring values from saved instance state
     */
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        updateLocationUI();
    }


    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            txtLocationResult.setText(
                    "Lat: " + mCurrentLocation.getLatitude() + ", " +
                            "Lng: " + mCurrentLocation.getLongitude()
            );


            // giving a blink animation on TextView
            txtLocationResult.setAlpha(0);
            txtLocationResult.animate().alpha(1).setDuration(300);

            // location last updated time
            txtUpdatedOn.setText("Last updated on: " + mLastUpdateTime);
            DriverLoc.setLat(mCurrentLocation.getLatitude());
            DriverLoc.setLng(mCurrentLocation.getLongitude());
            //HumanDriver.setValue(DriverLoc);


        }

        toggleButtons();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);

    }

    private void toggleButtons() {
        if (mRequestingLocationUpdates) {
            btnStartUpdates.setEnabled(false);
            btnStopUpdates.setEnabled(true);
            btnSafeStudentLocation.setEnabled(true);
        } else {
            btnStartUpdates.setEnabled(true);
            btnStopUpdates.setEnabled(false);
            btnSafeStudentLocation.setEnabled(false);
        }
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        Toast.makeText(getActivity(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }
    /*
    @OnClick(R.id.btn_start_location_updates)
    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(getActivity())
                .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @OnClick(R.id.btn_stop_location_updates)
    public void stopLocationButtonClick() {
        mRequestingLocationUpdates = false;
        stopLocationUpdates();
    }
    */
    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                        toggleButtons();
                    }
                });
    }
    /*
    @OnClick(R.id.btn_Safe_Student_Location)
    public void showLastKnownLocation() {
        double lat = mCurrentLocation.getLatitude();
        double lng = mCurrentLocation.getLongitude();
        String okul = OgrOkul.getText().toString().trim();
        String vakit = OgrVakit.getText().toString().trim();
        String ad = OgrAd.getText().toString().trim();
        String soyad = OgrSoyad.getText().toString().trim();
        String telefon = OgrTelefon.getText().toString().trim();

        if(okul.isEmpty())
        {
            OgrOkul.setError("Okul lazım");
            OgrOkul.requestFocus();
            return;
        }
        if(vakit.isEmpty())
        {
            OgrVakit.setError("Vakit lazım");
            OgrVakit.requestFocus();
            return;
        }
        if(ad.isEmpty())
        {
            OgrAd.setError("Ad lazım");
            OgrAd.requestFocus();
            return;
        }
        if(soyad.isEmpty())
        {
            OgrSoyad.setError("Soyad lazım");
            OgrSoyad.requestFocus();
            return;
        }
        if(telefon.isEmpty())
        {
            OgrTelefon.setError("Telefon lazım");
            OgrTelefon.requestFocus();
            return;
        }

        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(lat,lng,okul,vakit,ad,soyad,telefon, SharedPrefManager.getInstance(getActivity()).getPlaka());

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.code() == 201) {

                    DefaultResponse dr = response.body();
                    Toast.makeText(getActivity(), dr.getMsg(), Toast.LENGTH_LONG).show();

                } else if (response.code() == 422) {
                    Toast.makeText(getActivity(), "User already exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }
    */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onPause() {
        super.onPause();

        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }

    public double deg2rad(double deg)
    {
        return (deg * Math.PI / 180  );
    }

    public double Havasine(double lat1d, double lon1d, double lat2d, double lon2d)
    {
        double lat1r, lon1r, lat2r, lon2r, u, v;
        lat1r = deg2rad(lat1d);
        lon1r = deg2rad(lon1d);
        lat2r = deg2rad(lat2d);
        lon2r = deg2rad(lon2d);
        u = Math.sin((lat2r - lat1r)/2);
        v = Math.sin((lon2r - lon1r)/2);
        return 2.0 * 6371.0 * Math.asin(Math.sqrt(u * u + Math.cos(lat1r) * Math.cos(lat2r) * v * v));
    }

}
