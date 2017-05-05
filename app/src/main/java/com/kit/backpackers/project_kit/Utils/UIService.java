package com.kit.backpackers.project_kit.Utils;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class UIService extends IntentService {

    public static final String NOTIFICATION = "";
    private int result = Activity.RESULT_CANCELED;
    public static final String RESULT = "result";
    String userid;
    double value_a;
    double value_b;
    int e = 0;
    String id;
    String locationresults;

    public UIService() {
        super("");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        if (extras != null) {
            try {

                userid = extras.getString("driver_id");
                id = userid;
                System.out.println("the driver id outside is " + id);

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://backpackersapp.azurewebsites.net/api/Tracks/"+userid)
                        .get()
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "5a027277-3670-b49d-c863-ed605799230c")
                        .build();

                Response response = client.newCall(request).execute();
                result = Activity.RESULT_OK;
                locationresults = response.body().string();

                publishResults(locationresults , result);

               /* if (currentActivitiy.equalsIgnoreCase(
                        "Waiting for Payment")) {
                    if (!(fair.equalsIgnoreCase("") || fair.length() == 0)) {

                        publishResults(destinationId,
                                currentActivitiy,
                                fair, result);
                    }
                } else {

                    publishResults(destinationId,
                            currentActivitiy,
                            fair, result);
                }*/


            } catch (Exception e) {

            }
        }


    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    private void publishResults(String userloc,
                                int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra("userlocations", userloc);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }

}