package com.kit.backpackers.project_kit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kit.backpackers.project_kit.Models.Expedition;
import com.kit.backpackers.project_kit.SessionManagment.UserLoginSession;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "LoginPrefs";
    UserLoginSession userLoginSession;
    String _username, _picture;
    TextView displayName;
    ImageView displayImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        displayName =(TextView)findViewById(R.id.name_display_user);
        displayImage=(ImageView)findViewById(R.id.img_user);
        userLoginSession = new UserLoginSession(this);

        HashMap<String, String > userDetails = userLoginSession.getUserDetails();
        _username = userDetails.get(UserLoginSession.username);
        _picture = userDetails.get(UserLoginSession.picture);
      //  displayImage.setImageURI(Uri.parse(_picture));
        displayName.setText(_username);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
    public void gotoExpedition(View view){
        Intent intent = new Intent(HomeActivity.this, ExpeditionActivity.class);
        startActivity(intent);

    }
}
