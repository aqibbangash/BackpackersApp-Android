package com.kit.backpackers.project_kit.Utils;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.kit.backpackers.project_kit.Home.HomeActivity;
import com.kit.backpackers.project_kit.Home.HomeFragments.Adapters.JoinedExpeditionAdapter;
import com.kit.backpackers.project_kit.Home.HomeFragments.FragmentActivity;
import com.kit.backpackers.project_kit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.SSLEngineResult;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMapLongClickListener, LocationListener {

    String[] active_id;
    String[] backpacker_id;
    String[] lat;
    String[] expidd;
    String[] username;

    ArrayList<Marker> allPersons = new ArrayList<>();

    String currentExpId;

    GPSTracker gps;

    Handler handler = new Handler();

    Location location;

    LocationManager mLocationManager;

    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    String mLastUpdateTime;

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    Location location_l = null;
    Button stop_track;
    Timer timer;
    UserLoginSession userLoginSession;
    String userid,expid;
    ExpeditionSession expeditionSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        stop_track=(Button)findViewById(R.id.stop_track);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapdetails);
        mapFragment.getMapAsync(this);


        userLoginSession = new UserLoginSession(this);
        expeditionSession = new ExpeditionSession(this);

        HashMap<String , String> getuserid = userLoginSession.getUserDetails();
        userid = getuserid.get(UserLoginSession.userid);


       //this is how you get the id from session now remove the code and dot it your self once.... ok :(
        HashMap <String,String> getexpid=expeditionSession.getExpDetails();
        expid=getexpid.get(ExpeditionSession.expeditionid);

        currentExpId = getIntent().getStringExtra("expid");
        try {
            Log.e("CurrentExp", currentExpId);
            Const.ExId = currentExpId;
        } catch (NullPointerException ne)
        {
            Log.e("Null", "Exception");
        }

        //startService(new Intent(MapsActivity.this, LocationUpdateService.class));

        stop_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO update these values
                String IdExpedition = Const.ExId; //currentExpId;
                String State = "2";
                try {
                    Log.e("End Exp", "TRY BRACKET");
                    HttpRequests.UpdateExpedition(IdExpedition, State);
                    MapsActivity.this.finish();
                } catch (IOException e) {
                    Log.e("End Exp", "ERROR");
                    e.printStackTrace();
                }
                //new UpdateMyExpedition().execute(); //just call the class bs....rgihty
            }
        });

        //sending object to your service class in onCreate Method
        try {
            TimerTask timerTask = new TimerTask() {

                @Override
                public void run() {

                    Intent updateDriver = new Intent(MapsActivity.this,
                            UIService.class);

                    //your whats app down
                    updateDriver.putExtra("expid", expid);

                    startService(updateDriver);

                }
            };

            //timer = new Timer();
            //timer.schedule(timerTask, 5000, 7000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        registerReceiver(receiver, new IntentFilter(UIService.NOTIFICATION));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(UIService.NOTIFICATION));
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        try {
            mMap.setMyLocationEnabled(true);


            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMapLongClickListener(this);

            enableMyLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }


            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            final LocationListener mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {
                    //your code here

                    Log.e("Location", "Changed");


                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        LatLng latLng = new LatLng(latitude, longitude);
                        float zoomLevel = 16; //This goes up to 21
                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                    10, mLocationListener);


            location = mMap.getMyLocation();


            gps = new GPSTracker(getApplicationContext());

            if (gps.canGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();


                //TODO update these values
                String IdExpedition = Const.ExId; //currentExpId;
                String State = "1";
                try {
                    HttpRequests.UpdateExpedition(IdExpedition, State);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // \n is for new line
                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                try {
                    getOtherPins(Const.ExId);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                LatLng latLng = new LatLng(latitude, longitude);
                float zoomLevel = 16; //This goes up to 21
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

                startLocationChangedListener();


            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }


//            if (location != null) {
//                double latitude = location.getLatitude();
//                double longitude = location.getLongitude();
//                LatLng latLng = new LatLng(latitude, longitude);
//                float zoomLevel = 16; //This goes up to 21
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
//            }
//            else {
//                Toast.makeText(getApplicationContext() , "Location Not Found " , Toast.LENGTH_LONG).show();
//            }


        } catch (SecurityException se) {
            Toast.makeText(getApplication(), "Allow Location Access first", Toast.LENGTH_SHORT).show();
            mPermissionDenied = true;
        }


    }


    //Enables the My Location layer if the fine location permission has been granted.
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }



    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }



    //Displays a dialog with error message explaining that the location permission is missing.
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    @Override
    public void onMapLongClick(LatLng latLng) {

    }




    //broad cast receiver
    BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int resultCode = bundle.getInt(UIService.RESULT);
                if (resultCode == RESULT_OK) {
                    //get all the results from your services over here...
                    String locationResult = bundle.getString("userlocations");
                  //  Toast.makeText(getApplicationContext(), locationResult , Toast.LENGTH_LONG).show();
                    try {
                        //mMap.clear();



                        //clearing all persons markers


//                        try {
//                            getOtherPins(Const.ExId);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                        JSONArray jsonArray=new JSONArray(locationResult);

                       // if(jsonArray.length() > 0 ) {

                            String[] latitiude, longitude;

                            latitiude = new String[jsonArray.length()];
                            longitude = new String[jsonArray.length()];

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                latitiude[i] = jsonObject.getString("Lat");
                                longitude[i] = jsonObject.getString("Lng");

//                                MarkerOptions markerOptions = new MarkerOptions();
//                                markerOptions.position(new LatLng(Double.parseDouble(latitiude[i]), Double.parseDouble(longitude[i])));
//                                //here?
//                                mMap.addMarker(markerOptions);
                            }


                          //just trying
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }


    };//end of broadcast receiver

    @Override
    public void onLocationChanged(Location location) {

        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        Log.e("Location Upate: ", mLastUpdateTime);
        Log.e("Location : ", Double.toString(mCurrentLocation.getLatitude()) + ", " + Double.toString(mCurrentLocation.getLongitude()));


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    //async class to update the state of the exp
    public class UpdateMyExpedition extends AsyncTask<Void, Void, String> {
        private MaterialDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new MaterialDialog.Builder(MapsActivity.this)
                    .content("Starting Your Expeditions...")
                    .progress(true, 0)
                    .backgroundColor(Color.WHITE)
                    .contentColor(Color.BLACK)
                    .titleColor(Color.BLACK)
                    .dividerColor(Color.BLACK)
                    .widgetColor(Color.parseColor("#55B0CF"))
                    .show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String responseString = null;
            try {
                //defined in HttpRequest Class. calling that method
                responseString = HttpRequests.UpdateExpStatus(expid, "2");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (nDialog.isShowing())
                nDialog.dismiss();
            expeditionSession.logoutUser();
            startActivity(new Intent(MapsActivity.this, HomeActivity.class));

        }
    }


    void startLocationChangedListener() {

        SmartLocation.with(getApplicationContext()).location().start(locationListener);

    }


    OnLocationUpdatedListener locationListener = new OnLocationUpdatedListener() {
        @Override
        public void onLocationUpdated(Location location) {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            //Toast.makeText(getApplicationContext(), "New Location!", Toast.LENGTH_SHORT).show();

            LatLng latLng = new LatLng(latitude, longitude);
            float zoomLevel = 16; //This goes up to 21
            if (mMap != null) {
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

                updateLocationOnServer(latitude, longitude);
            }


            handler.postDelayed(locationRunnable, 20000);
        }
    };


    Runnable locationRunnable = new Runnable() {
        @Override
        public void run() {

            Log.e("Updating Your Loc:", "SmartLocation");
            SmartLocation.with(getApplicationContext()).location().start(locationListener);

        }
    };


    @Override
    public void onStop() {
        SmartLocation.with(getApplicationContext()).location().stop();
        super.onStop();
    }

    void updateLocationOnServer(Double lat, Double lon) {


        //TODO update these values
        String BackpackerId = Const.BpId; //""; //actual BackpackerId here
        String ExpeditionId = Const.ExId; //actual ExpeditionId here
        String Name = Const.Name;            //actual Name here
        String Lat = lat.toString();  //actual Lat here
        String Lon = lon.toString();  //actual Lon here

        try {

            HttpRequests.UpdateUserLocation(BackpackerId, ExpeditionId, Name, Lat, Lon);

            //TODO update these values
            String trackNumber = ExpeditionId; //""; //actual trackNumber here

            getAndMapPins(trackNumber);

        } catch (IOException e)
        {
            e.printStackTrace();
            Log.e("UpdateLoc", "ERROR");
        }

    }

    void getAndMapPins(String trackId) {


        try {

            Log.e("Pins", "Clean");


            getAllPins(trackId, mMap);




        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getAllPins(String track, final GoogleMap mMap) throws IOException {

        OkHttpClient client = new OkHttpClient();

        String url = "https://backpackersapp.azurewebsites.net/api/Tracks/" + track;
        Log.e("GET ALL PINS", url);

        Request request = new Request.Builder().url(url)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "f848a2cb-0a86-8a8d-8897-81852c81bf1a")
                .build();

//        Response response = client.newCall(request).execute();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String res = response.body().string();

                try {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mMap.clear();

                        }
                    });

                    getOtherPins(Const.ExId);

//                    for (final Marker m: allPersons)
//                    {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                m.remove();
//                            }
//                        });
//                        allPersons.remove(m);
//                        Log.e("RemaingPersons", String.valueOf(allPersons.size()));
//                    }



                    JSONArray allPins = new JSONArray(res);


                    for (int i = 0; i < allPins.length(); i++) {

                        JSONObject jj = allPins.getJSONObject(i);

                        if (jj.getString("BackpackerId") != Const.BpId)
                        {

                            final MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(jj.getDouble("Lat"), jj.getDouble("Lng")));
                            markerOptions.title(jj.getString("Name"));



                            int height = 150;
                            int width = 150;
                            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.person);
                            Bitmap b=bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));



                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    allPersons.add(mMap.addMarker(markerOptions));



                                }
                            });


                        }
                    }

                    Log.e("Pins", "Planted : " + allPins.length());


                } catch (JSONException e) {
                    e.printStackTrace();


                }

            }
        });




    }


    void getOtherPins(String Expid) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        String url = "https://backpackersapp.azurewebsites.net/api/Users/Expeditions/details/" + Expid;

        Log.e("f-url", url);
        Request request = new Request.Builder().url(url)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "f848a2cb-0a86-8a8d-8897-81852c81bf1a")
                .build();

        //Response response = client.newCall(request).execute();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                String res = response.body().string();

                JSONObject jj = null;
                try {
                    jj = new JSONObject(res);

                    JSONArray markers = jj.getJSONArray("Markers");


                    for (int i = 0; i < markers.length(); i++) {

                        JSONObject jj1 = markers.getJSONObject(i);
                        final MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(new LatLng(jj1.getDouble("Lat"), jj1.getDouble("Lng")));
                        markerOptions.title(jj1.getString("Name"));
                        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                        int height = 150;
                        int width = 150;
                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.location);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mMap.addMarker(markerOptions);
                            }
                        });

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });





    }


}
