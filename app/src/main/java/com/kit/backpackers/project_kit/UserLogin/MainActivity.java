package com.kit.backpackers.project_kit.UserLogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kit.backpackers.project_kit.Home.HomeActivity;
import com.kit.backpackers.project_kit.Home.JSONParser;
import com.kit.backpackers.project_kit.R;
import com.kit.backpackers.project_kit.Utils.UserLoginSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Button ok, back, exit;
    TextView result;
    EditText username, password;
    String mUsername;
    String mPassword;
    UserLoginSession user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Login button clicked
        ok = (Button) findViewById(R.id.btn_login);
        username = (EditText) findViewById(R.id.txt_username);
        password = (EditText) findViewById(R.id.txt_password);
        user = new UserLoginSession(this);

        ok.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                mUsername = username.getText().toString();
                mPassword = password.getText().toString();
                new PostAsync().execute(mUsername, mPassword);


            }

        });
    }

    class PostAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private ProgressDialog pDialog;

        private static final String LOGIN_URL = "https://backpackersapp.azurewebsites.net/api/Users/Login";

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("Email", args[0]);
                params.put("Password", args[1]);

                Log.d("request", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                if (json != null) {
                    Log.d("JSON result", json.toString());

                    return json;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(JSONObject json) {

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (json != null) {
               // Log.e("json answer", json.toString());
                Toast.makeText(MainActivity.this, "Login was ....Successful !!!Begin exploring now", Toast.LENGTH_LONG).show();
                try {
                  Log.e("json parse result " , json.getString("BackpackerID"));
                    String userid = json.getString("BackpackerID");
                    String username = json.getString("Name");
                    String picture = json.getString("Picture");

                    user.createLoginSession(userid , username , picture);
                    startActivity(new Intent(MainActivity.this , HomeActivity.class));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();}

            }else{
                Toast.makeText(MainActivity.this, "Login was not....Successful!!!", Toast.LENGTH_LONG).show();

            }



        }

    }
}
