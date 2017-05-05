package com.kit.backpackers.project_kit.Home.HomeFragments.Myexpfragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kit.backpackers.project_kit.Home.HomeFragments.Adapters.MyExpeditionAdapter;
import com.kit.backpackers.project_kit.Models.Expedition;
import com.kit.backpackers.project_kit.R;
import com.kit.backpackers.project_kit.Utils.ExpeditionSession;
import com.kit.backpackers.project_kit.Utils.HttpRequests;
import com.kit.backpackers.project_kit.Utils.MapsActivity;
import com.kit.backpackers.project_kit.Utils.UserLoginSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


public class myExpDetailFragment extends Fragment{

    TextView exploc, expdes;
    String _exploc, _expdes;
    String expid;
    Button poi_create,activity_add;
    ExpeditionSession expeditionSession;
    String _expidSession;       //exp id that we get form sesion


    public myExpDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.my_exp_detail_fragment,
                container, false);

        exploc = (TextView) layoutView.findViewById(R.id.textLocation);
        expdes = (TextView) layoutView.findViewById(R.id.textDesc);
<<<<<<< HEAD
<<<<<<< HEAD
       // poi_create = (Button) layoutView.findViewById(R.id.create_poi);
       // activity_add = (Button) layoutView.findViewById(R.id.create_activity);
=======
        poi_create = (Button) layoutView.findViewById(R.id.create_poi);
=======
        poi_create = (Button) layoutView.findViewById(R.id.event_btn);




       //
>>>>>>> 9f1c8e605fcfb5ad7a5c729c1559eecdbfde8bb6
        //activity_add = (Button) layoutView.findViewById(R.id.create_activity);
>>>>>>> c1392feae6e13a2852febcc89a90938717c01dca

        Intent a = getActivity().getIntent();
        expid = a.getStringExtra("expid");

        expeditionSession = new ExpeditionSession(getActivity());
        HashMap<String , String> getExp = expeditionSession.getExpDetails();
        _expidSession = getExp.get(ExpeditionSession.expeditionid);

        if( _expidSession != null){
            poi_create.setText("Continue Event");
        }

        poi_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_expidSession == null){
                    //YOU WILL CALL THE ASYN TASK HERE.
                    new UpdateMyExpedition().execute();
                }
                else{
                    //WILL DIRECTLY GO THE MAPS
                     Intent getMpas = new Intent(getActivity(), MapsActivity.class);
                     getMpas.putExtra("expid" , expid);
                    startActivity(getMpas);
                }



               // startActivity(new Intent(getActivity(), MapsActivity.class));
            }
        });



        return layoutView;
    }
    //once activity is created we call the asyc task in this method
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new LoadMyExpedition().execute();
    }


    //server api call goes here...
    //get my expediton list
    public class LoadMyExpedition extends AsyncTask<Void, Void, String> {
        private MaterialDialog nDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog =  new MaterialDialog.Builder(getActivity())
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
            if(nDialog.isShowing())
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
                Toast.makeText(getActivity() , ob.getString("IdExpedition"), Toast.LENGTH_LONG).show();
                expdes.setText(ob.getString("Description"));
                exploc.setText(ob.getString("Place"));



            } catch (Exception e) {

            }


        }
    }

    public class UpdateMyExpedition extends AsyncTask<Void, Void, String> {
        private MaterialDialog nDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog =  new MaterialDialog.Builder(getActivity())
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
                responseString = HttpRequests.UpdateExpStatus(expid, "1");
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

            expeditionSession.createExpSession(expid);
            Intent getMpas = new Intent(getActivity(), MapsActivity.class);
            getMpas.putExtra("expid", expid);
            startActivity(getMpas);



        }
    }
}