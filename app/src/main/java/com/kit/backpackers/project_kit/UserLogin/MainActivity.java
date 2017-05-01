package com.kit.backpackers.project_kit.UserLogin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kit.backpackers.project_kit.Home.HomeActivity;
import com.kit.backpackers.project_kit.R;
import com.kit.backpackers.project_kit.Utils.HttpRequests;
import com.kit.backpackers.project_kit.Utils.UserLoginSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;


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

                if(!_validate()){
                    return;
                }else{
                    new LoginTask().execute();
                }
            }

        });
    }


    boolean _validate()
    {
        boolean valid = true;
        // _name = name.getText().toString();
        mUsername = username.getText().toString();
        mPassword = password.getText().toString();

        if (mUsername.isEmpty()) {
            username.setError("please provide your email");
            valid = false;
        } else {
            username.setError(null);
        }


        if (mPassword.isEmpty()) {
            password.setError("please provide your password");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }


    //login task goes here.
    public class LoginTask extends AsyncTask<Void, Void, String> {


        private MaterialDialog nDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog =  new MaterialDialog.Builder(MainActivity.this)
                    .title("Please Wait")
                    .content("Logging you in")
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
                responseString = HttpRequests.UserLogin(mUsername, mPassword);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(nDialog.isShowing())
                nDialog.dismiss();
            try
            {
                JSONObject jsonObj = new JSONObject(s);
                JSONArray getData = jsonObj.getJSONArray("result");

                if(getData.length() > 0) {
                    for (int i = 0; i < getData.length(); i++) {
                        JSONObject c = getData.getJSONObject(i);
                        String user_id = c.getString("user_id");
                        String username = c.getString("firstname");
                        String email = c.getString("email");

                        Log.d("userdetais  ", user_id + "-------" + username + "-----" + email);
                    }


                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Your Login details are incorrect. Please try again", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
//            alertDialogBuilder.setMessage(s);
//
//            alertDialogBuilder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface arg0, int arg1) {
//
//                }
//            });
//
//            AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
                }

            }catch (Exception e)
            {

            }





        }
    }


}
