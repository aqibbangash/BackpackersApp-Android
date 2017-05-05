package com.kit.backpackers.project_kit.Home.HomeFragments;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kit.backpackers.project_kit.Home.HomeFragments.Adapters.AllExpeditionAdapter;
import com.kit.backpackers.project_kit.Home.HomeFragments.Adapters.MyExpeditionAdapter;
import com.kit.backpackers.project_kit.R;
import com.kit.backpackers.project_kit.Utils.HttpRequests;
import com.kit.backpackers.project_kit.Utils.UserLoginSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


public class ThreeFragment extends Fragment {

    String[] exp_id;
    String[] exp_location;
    String[] exp_name;
    String[] created_by;
    String userid;
    UserLoginSession userLoginSession;


    ListView allexp;

    //myExpListView

    int fragNum;
    static ThreeFragment init(int val) {
        ThreeFragment truitonList = new ThreeFragment();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        truitonList.setArguments(args);
        return truitonList;
    }
    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragNum = getArguments() != null ? getArguments().getInt("val") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.fragment_three,
                container, false);

        //initlizing user session in order to get logged in user id
        userLoginSession = new UserLoginSession(getActivity());
        HashMap<String , String> getUserDetaiils = userLoginSession.getUserDetails();
        userid = getUserDetaiils.get(UserLoginSession.userid);

        //setting up list view
        allexp = (ListView) layoutView.findViewById(R.id.allExpListView);
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
                responseString = HttpRequests.GetAllExpeditions(userid);
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
                JSONArray getData = new JSONArray(s);

                //setting array size to the total data length
                exp_id = new String[getData.length()];
                exp_location = new String[getData.length()];
                exp_name = new String[getData.length()];
                created_by = new String[getData.length()];

                for(int i=0; i < getData.length(); i++) {
                    JSONObject object = getData.getJSONObject(i);
                  //  Toast.makeText(getActivity(), object.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("Result is here;;;...", object.toString());

                    //geeting the back packer object here...
                    JSONObject ob = object.getJSONObject("Backpacker");
                    //Toast.makeText(getActivity(), ob.getString("Name"), Toast.LENGTH_SHORT).show();
                    Log.d("for backpackers..", ob.toString());


                    //gtting info for expedition here..
                    JSONObject oob = object.getJSONObject("Expedition");
                 //   Toast.makeText(getActivity(), oob.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("for Expedition..", oob.toString());

                    //show on the list view...
                    created_by[i] = ob.getString("Name");
                    exp_id[i]  = oob.getString("IdExpedition");
                    exp_name[i] = oob.getString("Name");
                    exp_location[i] = oob.getString("Place");
                }

                AllExpeditionAdapter adapter = new AllExpeditionAdapter(getActivity() , exp_id, exp_location, exp_name,created_by);
                allexp.setAdapter(adapter);
                } catch (JSONException e1) {
                e1.printStackTrace();
            }



        }
    }

}