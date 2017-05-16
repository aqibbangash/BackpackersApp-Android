package com.kit.backpackers.project_kit.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kit.backpackers.project_kit.Home.HomeFragments.FragmentActivity;
import com.kit.backpackers.project_kit.R;
import com.kit.backpackers.project_kit.UserLogin.MainActivity;
import com.kit.backpackers.project_kit.Utils.NotificationService;
import com.kit.backpackers.project_kit.Utils.UserLoginSession;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String PREFS_NAME = "LoginPrefs";
    UserLoginSession userLoginSession;
    String _username, _picture;
    TextView displayName;
    ImageView displayImage;


    private Button btnGroups;
    private Button btnExpedition;
    private Button btnTrack;
    private Button btnlogout;
    ImageView lg_button;

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
        lg_button = (ImageView) findViewById( R.id.lg_button );

        btnGroups.setOnClickListener(this);
        btnExpedition.setOnClickListener( this );
        btnTrack.setOnClickListener( this );
        lg_button.setOnClickListener( this );
    }


    @Override
    public void onClick(View v) {
        if ( v == btnGroups ) {
            startActivity(new Intent(HomeActivity.this, FragmentActivity.class));

        } else if ( v == btnExpedition ) {
            // Handle clicks for btnExpedition
        } else if ( v == btnTrack ) {
            // Handle clicks for btnTrack
        }
        else if ( v == lg_button ) {
            // Handle clicks for btnTrack
            userLoginSession.logoutUser();
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        displayName =(TextView)findViewById(R.id.name_display_user);
        displayImage=(ImageView)findViewById(R.id.img_user);

        findViews();
        userLoginSession = new UserLoginSession(this);

        HashMap<String, String > userDetails = userLoginSession.getUserDetails();
        _username = userDetails.get(UserLoginSession.username);
        _picture = userDetails.get(UserLoginSession.picture);
        displayName.setText(_username);

       // startService(new Intent(HomeActivity.this, NotificationService.class));
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
