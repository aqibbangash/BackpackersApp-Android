package com.kit.backpackers.project_kit.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.kit.backpackers.project_kit.Home.HomeFragments.FragmentActivity;
import com.kit.backpackers.project_kit.R;
import com.kit.backpackers.project_kit.UserLogin.MainActivity;
import com.kit.backpackers.project_kit.Utils.Const;
import com.kit.backpackers.project_kit.Utils.NotificationService;
import com.kit.backpackers.project_kit.Utils.UserLoginSession;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String PREFS_NAME = "LoginPrefs";
    UserLoginSession userLoginSession;
    String _username, _picture;
    TextView displayName;
    ImageView displayImage;

    RelativeLayout gp, exp, track, logout;

    private Button btnGroups;
    private Button btnExpedition;
    private Button btnTrack;
    private Button btnlogout;
    ImageView lg_button;

    private Toolbar toolbar;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-01 09:25:21 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        btnGroups = (Button)findViewById( R.id.btn_groups );
        btnExpedition = (Button)findViewById( R.id.btn_expedition );
        btnTrack = (Button)findViewById( R.id.btn_track );


        gp = (RelativeLayout) findViewById(R.id.rl_groups);
        exp = (RelativeLayout) findViewById(R.id.rl_exp);
        track = (RelativeLayout) findViewById(R.id.rl_track);
        logout = (RelativeLayout) findViewById(R.id.rl_logout);


        gp.setOnClickListener(this);
        exp.setOnClickListener( this );
        track.setOnClickListener( this );
        logout.setOnClickListener( this );
    }


    void getMyCurrentLocation() throws SecurityException
    {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        sendPush(latitude, longitude);

        final LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                //longitude = location.getLongitude();
                //latitude = location.getLatitude();
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

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

    }

    private void sendPush(double latitude, double longitude) {

//        RemoteMessage rm = new RemoteMessage.Builder("/topics/sos").addData("lat",String.valueOf(latitude)).addData("lon", String.valueOf(longitude)).addData("type","sos").build();
//        FirebaseMessaging.getInstance().send(rm);


        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";

        MediaType mediaType = MediaType.parse("application/json");
        Log.e("SOS", " {\n    \"to\": \"" + "/topics/sos" + "\",\n    \"data\": { \"message\": \"SOS Message. Lon: "+longitude + " , Lat: " + latitude + "\"} }");
        RequestBody body = RequestBody.create(mediaType, " {\n    \"to\": \"" + "/topics/sos" + "\",\n    \"notification\": { \"title\": \"SOS Message.\",\"title\":\" Lon: "+longitude + " , Lat: " + latitude + "\"} }");

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "key=" + Const.FirebaseToken)
                .build();


        Log.e("Token", Const.FirebaseToken);

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("SOS", "Failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.e("SOS", "Sent");
                Log.e("SOSResp", response.toString());

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.rl_groups:
                startActivity(new Intent(HomeActivity.this, FragmentActivity.class));
                break;
            case R.id.rl_exp:
                startActivity(new Intent(HomeActivity.this, ExpeditionActivity.class));
                break;
            case R.id.rl_track:
                getMyCurrentLocation();
                break;
            case R.id.rl_logout:
                userLoginSession.logoutUser();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                break;
//            case R.id.lg_button:
//                userLoginSession.logoutUser();
//                startActivity(new Intent(HomeActivity.this, MainActivity.class));
//                break;

            default:



        }

//        if ( v == btnGroups ) {
//            startActivity(new Intent(HomeActivity.this, FragmentActivity.class));
//
//        } else if ( v == btnExpedition ) {
//            // Handle clicks for btnExpedition
//        } else if ( v == btnTrack ) {
//            // Handle clicks for btnTrack
//        }
//        else if ( v == lg_button ) {
//            // Handle clicks for btnTrack
//            userLoginSession.logoutUser();
//            startActivity(new Intent(HomeActivity.this, MainActivity.class));
//        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //------GET USER TOKEN-------

        FirebaseInstanceId.getInstance().getToken();




        displayName =(TextView)findViewById(R.id.name_display_user);
        displayImage=(ImageView)findViewById(R.id.img_user);

        findViews();
        userLoginSession = new UserLoginSession(this);

        HashMap<String, String > userDetails = userLoginSession.getUserDetails();
        _username = userDetails.get(UserLoginSession.username);
        _picture = userDetails.get(UserLoginSession.picture);
        displayName.setText(_username);

       // startService(new Intent(HomeActivity.this, NotificationService.class));
        FirebaseMessaging.getInstance().subscribeToTopic(Const.BpId);

        FirebaseMessaging.getInstance().subscribeToTopic("sos");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.menu_main, menu);
       // return true;
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_logout:
                //your code here
                SharedPreferences preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
