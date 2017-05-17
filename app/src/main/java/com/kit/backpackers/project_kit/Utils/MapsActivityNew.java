package com.kit.backpackers.project_kit.Utils;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kit.backpackers.project_kit.Home.HomeActivity;
import com.kit.backpackers.project_kit.Home.HomeFragments.FragmentActivity;
import com.kit.backpackers.project_kit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivityNew extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap map;
    OnMapReadyCallback callback;

    String[] active_id;
    String[] backpacker_id;
    String[] lat;
    String[] expidd;
    String[] username;


    private boolean mPermissionDenied = false;

    Location location_l = null;
    Button stop_track;
    Timer timer;
    UserLoginSession userLoginSession;
    String userid, expid;
    ExpeditionSession expeditionSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapdetails);
        mapFragment.getMapAsync(this);

        userLoginSession = new UserLoginSession(this);
        expeditionSession = new ExpeditionSession(this);

        HashMap<String, String> getuserid = userLoginSession.getUserDetails();
        userid = getuserid.get(UserLoginSession.userid);


        //this is how you get the id from session now remove the code and dot it your self once.... ok :(
        HashMap<String, String> getexpid = expeditionSession.getExpDetails();
        expid = getexpid.get(ExpeditionSession.expeditionid);


        stop_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //new MapsActivity.UpdateMyExpedition().execute(); //just call the class bs....rgihty
            }
        });

        startService(new Intent(MapsActivityNew.this, LocationUpdateService.class));


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        try {
            map.setMyLocationEnabled(true);


        } catch (SecurityException se) {
            Toast.makeText(getApplication(), "Allow Location Access first", Toast.LENGTH_SHORT).show();
            mPermissionDenied = true;
        }
    }


}

