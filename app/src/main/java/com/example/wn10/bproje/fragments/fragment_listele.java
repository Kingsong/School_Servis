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
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wn10.bproje.BuildConfig;
import com.example.wn10.bproje.Listele;
import com.example.wn10.bproje.MainActivity;
import com.example.wn10.bproje.R;
import com.example.wn10.bproje.Retrofit.api.RetrofitClient;
import com.example.wn10.bproje.Retrofit.models.VeriResponse;
import com.example.wn10.bproje.Storage.SharedPrefManager;
import com.example.wn10.bproje.Storage.veri;
import com.example.wn10.bproje.TinyDB;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_listele extends Fragment implements OnMapReadyCallback {

    private ListView listView;
    private Spinner spinner_okul,spinner_vakit;
    Button btn;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> TelNo = new ArrayList<String>();
    ArrayList<Double> Lat = new ArrayList<Double>();
    ArrayList<Double> Lng = new ArrayList<Double>();
    ArrayList<String> okul_liste = new ArrayList<String>();
    ArrayList<String> vakit_liste = new ArrayList<String>();
    veri DriverLoc = new veri();
    veri StudentLoc = new veri();
    int counter = 0;
    int uyariMesafe = 0;
    private GoogleMap mMap;
    String secilen_okul,secilen_vakit;
    Button btnStartUpdates;
    Button btnStopUpdates;
    Button btnSafeStudentLocation;
    ArrayAdapter<String> adapter;
    int sayac = 0;
    int ilk = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.listelefragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        listView = (ListView) view.findViewById(R.id.listview);
        btn = view.findViewById(R.id.btn_ekle);
        spinner_okul = view.findViewById(R.id.spinner_okul);
        spinner_vakit = view.findViewById(R.id.spinner_vakit);
        btnStartUpdates = view.findViewById(R.id.btn_start_location_updates);
        btnStopUpdates = view.findViewById(R.id.btn_stop_location_updates);
        btnSafeStudentLocation = view.findViewById(R.id.btn_Safe_Student_Location);



        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        ButterKnife.bind(getActivity());

        // initialize the necessary libraries
        init();

        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState);



        // Defined Array values to show in ListView
        /*
        String[] values = new String[] { "Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };
        ArrayList<String> list = new ArrayList<String>();

        for(int i = 0;i<values.length; i++)
        {
            list.add(values[i]);
        }
        */



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //list=dbHandler.loadHandler(Okul.getText().toString().trim(),Vakit.getText().toString().trim());
                //String okul = Okul.getText().toString().trim();
                //String vakit = Vakit.getText().toString().trim();
                final TinyDB tinyDB = new TinyDB(getActivity());
                //sayac = 0;
                list.clear();
                Lat.clear();
                Lng.clear();
                mMap.clear();
                Call<VeriResponse> call = RetrofitClient.getInstance().getApi().getBilgi(secilen_okul, secilen_vakit, SharedPrefManager.getInstance(getActivity()).getPlaka());

                call.enqueue(new Callback<VeriResponse>() {
                    @Override
                    public void onResponse(Call<VeriResponse> call, Response<VeriResponse> response) {
                        ArrayList<veri> dizi = response.body().getVeriler();
                        for (int i = 0; i < dizi.size(); i++) {
                            list.add(dizi.get(i).getAd() + " " + dizi.get(i).getSoyad());
                            Lat.add(dizi.get(i).getLat());
                            Lng.add(dizi.get(i).getLng());
                            /*
                            LatLng sydney = new LatLng(dizi.get(i).getLat(), dizi.get(i).getLng());
                            mMap.addMarker(new MarkerOptions().position(sydney).title(list.get(i)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            */



                        }/*
                        adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, list);
                        listView.setAdapter(adapter);
                        */
                        //Liste Kayitli degil ise olustur
                        if (tinyDB.getListString(secilen_okul+secilen_vakit+"ogr").isEmpty())
                        {
                            tinyDB.putListString(secilen_okul+secilen_vakit+"ogr",list);
                            tinyDB.putListDouble(secilen_okul+secilen_vakit+"lat",Lat);
                            tinyDB.putListDouble(secilen_okul+secilen_vakit+"lng",Lng);
                            Activteguncelle();

                        }
                        // Liste var ama ogr sayisi eksik ise guncelle
                        else if(tinyDB.getListString(secilen_okul+secilen_vakit+"ogr").size()<list.size())
                        {
                            tinyDB.putListString(secilen_okul+secilen_vakit+"ogr",list);
                            tinyDB.putListDouble(secilen_okul+secilen_vakit+"lat",Lat);
                            tinyDB.putListDouble(secilen_okul+secilen_vakit+"lng",Lng);
                            Activteguncelle();

                        }
                        // yoksa kayit olan bilgi getir
                        else
                        {
                            list=tinyDB.getListString(secilen_okul+secilen_vakit+"ogr");
                            Lat=tinyDB.getListDouble(secilen_okul+secilen_vakit+"lat");
                            Lng=tinyDB.getListDouble(secilen_okul+secilen_vakit+"lng");
                            Activteguncelle();
                        }
                    }

                    @Override
                    public void onFailure(Call<VeriResponse> call, Throwable t) {

                    }
                });












                //Lat=dbHandler.KoordinatLatHandler(Okul.getText().toString().trim(),Vakit.getText().toString().trim());
                //Lng=dbHandler.KoordinatLngHandler(Okul.getText().toString().trim(),Vakit.getText().toString().trim());
                //TelNo=dbHandler.TelefonNoHandler(Okul.getText().toString().trim(),Vakit.getText().toString().trim());

            }
        });


        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        //     android.R.layout.simple_list_item_1, android.R.id.text1, list);


        // Assign adapter to ListView
        //listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TinyDB tinyDB = new TinyDB(getActivity());
                /*
                if(ilk == position)
                {
                    list.remove(position);
                    Lat.remove(position);
                    Lng.remove(position);
                    adapter.notifyDataSetChanged();
                    mMap.clear();
                    for(int i=0;i< list.size();i++)
                    {
                        LatLng sydney = new LatLng(Lat.get(i), Lng.get(i));
                        mMap.addMarker(new MarkerOptions().position(sydney).title(list.get(i)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    }

                    sayac = 0;
                }
                */


                if(sayac == 1)
                {
                    Collections.swap(list,ilk,position);
                    Collections.swap(Lat,ilk,position);
                    Collections.swap(Lng,ilk,position);
                    adapter.notifyDataSetChanged();
                    tinyDB.putListString(secilen_okul+secilen_vakit+"ogr",list);
                    tinyDB.putListDouble(secilen_okul+secilen_vakit+"lat",Lat);
                    tinyDB.putListDouble(secilen_okul+secilen_okul+"lng",Lng);

                    sayac = 0 ;
                }
                else if (sayac == 0)
                {
                    ilk = position;
                    sayac++;
                }



                // ListView Clicked item index
                //int itemPosition     = position;

                // ListView Clicked item value
                //String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                /*
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();
                        */

            }

        });

        spinner_vakit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                secilen_vakit = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_okul.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                secilen_okul = adapterView.getItemAtPosition(i).toString();
                vakit_liste.clear();
                Call<VeriResponse> call = RetrofitClient.getInstance().getApi().getVakit(secilen_okul);

                call.enqueue(new Callback<VeriResponse>() {
                    @Override
                    public void onResponse(Call<VeriResponse> call, Response<VeriResponse> response) {
                        ArrayList<veri> dizi = response.body().getVeriler();

                        for(int i = 0; i < dizi.size() ; i++)
                        {
                            vakit_liste.add(dizi.get(i).getVakit().toString());
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_dropdown_item,vakit_liste);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner_vakit.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onFailure(Call<VeriResponse> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnStartUpdates.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mRequestingLocationUpdates = true;
                startLocationUpdates();
                /*
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

                            }

                        }).check();
                        */
            }
        });

        btnStopUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRequestingLocationUpdates = false;
                stopLocationUpdates();
            }
        });
    }

    private void Activteguncelle() {
        for (int i=0; i<Lat.size();i++)
        {
            LatLng sydney = new LatLng(Lat.get(i), Lng.get(i));
            mMap.addMarker(new MarkerOptions().position(sydney).title(list.get(i)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        okul_liste.clear();
        Call<VeriResponse> call_okul = RetrofitClient.getInstance().getApi().getOkul(SharedPrefManager.getInstance(getActivity()).getPlaka());

        call_okul.enqueue(new Callback<VeriResponse>() {
            @Override
            public void onResponse(Call<VeriResponse> call, Response<VeriResponse> response) {
                ArrayList<veri> dizi = response.body().getVeriler();
                for(int i=0; i< dizi.size() ; i++)
                {
                    okul_liste.add(dizi.get(i).getOkul());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,okul_liste);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner_okul.setAdapter(arrayAdapter);
            }

            @Override
            public void onFailure(Call<VeriResponse> call, Throwable t) {

            }
        });


    }

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.location_result)
    TextView txtLocationResult;

    @BindView(R.id.updated_on)
    TextView txtUpdatedOn;









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
                //Buraya Listeden eleman silme ile mesaj atmayı ekle
                updateLocationUI();
                uyariMesafe = 600;
                if (false == list.isEmpty() && (uyariMesafe>Havasine(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude(),Lat.get(counter),Lng.get(counter)))) {
                    list.remove(0);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1, android.R.id.text1, list);
                    listView.setAdapter(adapter);
                    //SmsManager smsManager = SmsManager.getDefault();
                    //smsManager.sendTextMessage(TelNo.get(counter), null, "mesaj", null, null);
                    counter++;
                    mMap.clear();
                    for(int i=0;i< list.size();i++)
                    {
                        LatLng sydney = new LatLng(Lat.get(i), Lng.get(i));
                        mMap.addMarker(new MarkerOptions().position(sydney).title(list.get(i)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    }

                }
                else
                {
                    mMap.clear();
                    for(int i=0;i< list.size();i++)
                    {
                        LatLng sydney = new LatLng(Lat.get(i), Lng.get(i));
                        mMap.addMarker(new MarkerOptions().position(sydney).title(list.get(i)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    }
                }
                LatLng sydney = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(sydney).title("Şoför"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
    public void updateLocationUI() {

        toggleButtons();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);

    }

    public void toggleButtons() {
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
    public void startLocationUpdates() {
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
                .addOnFailureListener((getActivity()), new OnFailureListener() {
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



        if (mCurrentLocation != null) {


        } else {
            Toast.makeText(getActivity(), "Last known location is not available!", Toast.LENGTH_SHORT).show();

        }
        //veri veri = new veri(OgrOkul.getText().toString(),OgrVakit.getText().toString(),OgrAd.getText().toString(),OgrSoyad.getText().toString(),OgrTelefon.getText().toString(),mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());





        //dbHandler.deleteHandler("ibo");
        //tv.setText(vericik.Okul);
        //tv.setText(dbHandler.loadHandler2("MMK","sabah"));
        //tv.setText(dbHandler.KoordinatLatHandler("MMK","sabah").toString());
        //tv.setText(dbHandler.KoordinatLngHandler("MMK","sabah").toString());
        //tv.setText(dbHandler.TelefonNoHandler("MMK","sabah"));

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

    public void openSettings() {
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

    public boolean checkPermissions() {
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
        double lat1r, lon1r, lat2r, lon2r, u, v, sonuc;
        lat1r = deg2rad(lat1d);
        lon1r = deg2rad(lon1d);
        lat2r = deg2rad(lat2d);
        lon2r = deg2rad(lon2d);
        u = Math.sin((lat2r - lat1r)/2);
        v = Math.sin((lon2r - lon1r)/2);
        sonuc =1000 * 2.0 * 6371.0 * Math.asin(Math.sqrt(u * u + Math.cos(lat1r) * Math.cos(lat2r) * v * v));
        return sonuc ;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}
