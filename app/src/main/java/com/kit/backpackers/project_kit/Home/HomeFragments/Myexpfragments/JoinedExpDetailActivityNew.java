package com.kit.backpackers.project_kit.Home.HomeFragments.Myexpfragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kit.backpackers.project_kit.R;
import com.kit.backpackers.project_kit.Utils.ExpeditionSession;
import com.kit.backpackers.project_kit.Utils.HttpRequests;
import com.kit.backpackers.project_kit.Utils.MapsActivity;
import com.kit.backpackers.project_kit.Utils.MapsActivityNew;

import org.json.JSONObject;

import java.io.IOException;

public class JoinedExpDetailActivityNew extends AppCompatActivity {


    TextView exploc, expdes;
    String _exploc, _expdes;
    String expid;
    Button go_event;
    ExpeditionSession expeditionSession;
    String _expidSession;       //exp id that we get form sesion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_exp_detail);

        go_event = (Button) findViewById(R.id.event_btnA);
        exploc = (TextView) findViewById(R.id.textLocationA);
        expdes = (TextView) findViewById(R.id.textDescA);
        expeditionSession = new ExpeditionSession(this);
        Intent a = getIntent();
        expid = a.getStringExtra("expid");
        go_event.setEnabled(false);


        new LoadMyExpedition().execute();

    }


    public class LoadMyExpedition extends AsyncTask<Void, Void, String> {
        private MaterialDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new MaterialDialog.Builder(JoinedExpDetailActivityNew.this)
                    .content("Getting Your Expeditions...")
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
                responseString = HttpRequests.GetExpById(expid);
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

            //parsing the json data to display on the list view
            try {
                JSONObject jsonObj = new JSONObject(s);

                JSONObject ob = jsonObj.getJSONObject("Expedition");
                // JSONArray getData = jsonObj.getJSONArray("result");

                //setting array size to the total data length


              /*  if (getData.length() > 0) {
                    for (int i = 0; i < getData.length(); i++) {
                        JSONObject c = getData.getJSONObject(i);
                        _exploc = c.getString("exp_location");
                        _expdes = c.getString("exp_descrption");

                    }

                    exploc.setText(_exploc);
                    expdes.setText(_expdes);*/
                //s Toast.makeText(getActivity(), ob.getString("IdExpedition"), Toast.LENGTH_LONG).show();
                expdes.setText(ob.getString("Description"));
                exploc.setText(ob.getString("Place"));

                if (ob.getInt("State") == 1) {
                    Toast.makeText(JoinedExpDetailActivityNew.this, "Theek haai", Toast.LENGTH_LONG).show();
                    go_event.setEnabled(true);
                    go_event.setText("Continue Event");

                    go_event.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(JoinedExpDetailActivity.this, "Click how gya....", Toast.LENGTH_LONG).show();
                            expeditionSession.createExpSession(expid);
                            startActivity(new Intent(JoinedExpDetailActivityNew.this, MapsActivity.class));
                        }
                    });

                } else {
                    Toast.makeText(JoinedExpDetailActivityNew.this, "Trip not started Yet", Toast.LENGTH_LONG).show();
                }


            } catch (Exception e) {

            }


        }
    }
}
