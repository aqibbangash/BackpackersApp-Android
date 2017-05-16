package com.kit.backpackers.project_kit.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.kit.backpackers.project_kit.Home.HomeActivity;
import com.kit.backpackers.project_kit.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NotificationService extends Service {
    private static final String TAG = "NotificationService";

    String userId,expId;

    String connectionURL;
    String crrentAct;
    UserLoginSession userLoginSession; //get ids of all logged in users
     ExpeditionSession expeditionSession; //get id of started exp

    private boolean isRunning = false;
    String status = "Waiting for ";



    public NotificationService() {
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
        isRunning = true;
        userLoginSession = new UserLoginSession(getApplicationContext());
        expeditionSession = new ExpeditionSession(getApplicationContext());

        HashMap<String, String> user = userLoginSession.getUserDetails();
        HashMap<String, String> expedition = expeditionSession.getExpDetails();

        userId = user.get(UserLoginSession.userid);
        expId= expedition.get(ExpeditionSession.expeditionid);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Log.i(TAG, "Service onStartCommand");
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Your logic that service will perform will be placed here
                // In this example we are just looping and waits for 1000
                // milliseconds in each loop.
                while (true) {


                    try {
                        Thread.sleep(13000);
                    } catch (Exception e) {
                    }

                    if (isRunning) {
                        Log.i(TAG, "Service running" + connectionURL);
                        try {
                            if (expId == null) {
                                OkHttpClient client = new OkHttpClient();

                                Request request = new Request.Builder()
                                        .url("https://backpackersapp.azurewebsites.net/api/Users/Events/State/5")
                                        .get()
                                        .addHeader("cache-control", "no-cache")
                                        .addHeader("postman-token", "083b8272-3acf-8333-9a35-78c00956cbf4")
                                        .build();

                                Response response = client.newCall(request).execute();
                                String myResponse = response.body().string();


                                JSONArray jsonArray = new JSONArray(myResponse);
                                if(jsonArray.length() > 0){
                                    String[] expid;
                                    expid = new String[jsonArray.length()];

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        expid[i] = jsonObject.getString("ExpeditionId");
                                    }
                                    expeditionSession.createExpSession(expid[0]);
                                }else{

                                }



                                Log.i(TAG, crrentAct + "Inside");
                                // define sound URI, the sound to be played when there's a notification
                                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                // intent triggered, you can add other intent for other actions
                                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                                // this is it, we'll build the notification!
                                // in the addAction method, if you don't want any icon, just set the first param to 0

                                Notification mNotification = new Notification.Builder(getApplicationContext()) //gets an instance of notification service

                                        .setContentTitle("Get Ready !")
                                        .setContentText("Your expedition has been started")
                                        .setSmallIcon(R.drawable.trip)
                                        .setContentIntent(pIntent)
                                        .setSound(soundUri)
                                        .addAction(0, "View Trip", pIntent)
                                        .addAction(0, "Ok", pIntent)
                                        .build();

                                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                                // If you want to hide the notification after it was selected, do the code below
                                // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

                                notificationManager.notify(0, mNotification);
                                // jb.createLoginSession(clLats, clLongs, dlLats,
                                // dlLongs, clientid, crrentAct, destinationId);
                            }
                            else{
                                //do nothing :D
                            }


                            }catch(Exception ee){

                            }

                            //  Log.e("driver location", jsonObject.toString());

                        }


                    }
                }



    }).start();

    return Service.START_STICKY;
}


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
