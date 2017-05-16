package com.kit.backpackers.project_kit.Home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kit.backpackers.project_kit.Adapters.ExpeditionListAdapter;
import com.kit.backpackers.project_kit.Models.Expedition;
import com.kit.backpackers.project_kit.R;
import com.kit.backpackers.project_kit.Utils.UserLoginSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExpeditionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    UserLoginSession userLoginSession;
    String _id;
    ListView expeditionListView;
    Context context;
    ArrayList<Expedition> expeditionList;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expeditions);
        //list view
        expeditionListView = (ListView)findViewById(R.id.expedition_ListView);
        expeditionList= new ArrayList<Expedition>();

        //logged user details
        userLoginSession = new UserLoginSession(this);
        HashMap<String, String > userDetails = userLoginSession.getUserDetails();
         _id = userDetails.get(UserLoginSession.userid);
        url= "https://backpackersapp.azurewebsites.net/api/Users/Expeditions/"+_id;

        try {
            Log.e("url", url);
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
        expeditionListView.setOnItemClickListener(this);
        //make a seprate method
        //get all json from api/Users/Expeditions/"userId"
//        Expedition exp1 = new Expedition(1,"exp1","fnkdnfkjd","flex","public","3","09/02/2012","08","02/21/2017","10","Muzafrabad");
//        Expedition exp2 = new Expedition(2,"exp2","fnkdnfkjd","flex","public","3","09/02/2012","08","02/21/2017","10","Muzafrabad");
//        Expedition exp3 = new Expedition(3,"exp3","fnkdnfkjd","flex","public","3","09/02/2012","08","02/21/2017","10","Muzafrabad");
//        Expedition exp4 = new Expedition(4,"exp4","fnkdnfkjd","flex","public","3","09/02/2012","08","02/21/2017","10","Muzafrabad");
//        Expedition exp5 = new Expedition(5,"exp5","fnkdnfkjd","flex","public","3","09/02/2012","08","02/21/2017","10","Muzafrabad");
//        Expedition exp6 = new Expedition(6,"exp6","fnkdnfkjd","flex","public","3","09/02/2012","08","02/21/2017","10","Muzafrabad");
//        expeditionList.add(exp1);
//        expeditionList.add(exp2);
//        expeditionList.add(exp3);
//        expeditionList.add(exp4);
//        expeditionList.add(exp5);
//        expeditionList.add(exp6);
//        ExpeditionListAdapter adapter = new ExpeditionListAdapter(ExpeditionActivity.this,R.layout.expedition_list_layout, expeditionList);
//        expeditionListView.setAdapter(adapter);

    }

    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final SimpleDateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd");
                final SimpleDateFormat outputDate = new SimpleDateFormat("MMM dd, yy");
                final SimpleDateFormat inputTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                final SimpleDateFormat outputTime = new SimpleDateFormat("HH:mm");
                final String myResponse = response.body().string();

                ExpeditionActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
               //         txtString.setText(myResponse);
                        if(myResponse!=null){
                            try {
                                Log.e("json answer", myResponse);
                                JSONArray expeditions = new JSONArray(myResponse);
                                for(int i=0; i<expeditions.length();i++){
                                    String startDate;
                                    String endDate;
                                    String startTime;
                                    String endTime;
                                    JSONObject exp = expeditions.getJSONObject(i);

                                    Date date = inputDate.parse(exp.getString("StartDate"));
                                    startDate=outputDate.format(date);
                                    date = inputDate.parse(exp.getString("EndDate"));
                                    endDate=outputDate.format(date);

                                   // Date startT = inputTime.parse(exp.getString("StartTime"));
                                    //Log.e("Time",outputTime.format(startT));
                                    Expedition expedition = new Expedition(exp.getString("IdExpedition"),exp.getString("Name"),exp.getString("Description"),exp.getString("Type"),exp.getString("Status"),exp.getString("UserId"),startDate,exp.getString("StartTime"),endDate,exp.getString("EndTime"),exp.getString("Place"));
                                   expeditionList.add(expedition);
                                }
                                ExpeditionListAdapter adapter = new ExpeditionListAdapter(ExpeditionActivity.this,R.layout.expedition_list_layout, expeditionList);
                                expeditionListView.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Expedition exp= (Expedition)(parent.getItemAtPosition(position));
        Toast.makeText(ExpeditionActivity.this,exp.getDescription().toString(),Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ExpeditionActivity.this, DetailActivity.class);
      //  intent.putExtra("Expedition",exp);
        startActivity(intent);

    }
}
