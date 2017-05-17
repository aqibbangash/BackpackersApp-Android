package com.kit.backpackers.project_kit.Utils;

import android.util.Log;

import com.kit.backpackers.project_kit.Models.ExpLocations;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Naila on 29/04/2017.
 */


//all if the API calls will be managed here.
public class HttpRequests {

    OkHttpClient client = new OkHttpClient();

    public static String UserLogin(String useremail , String userpassword) throws IOException
    {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n\t\"Email\" : \""+useremail+"\",\n\t\"Password\" : \""+userpassword+"\"\n}");
       // RequestBody body = RequestBody.create(mediaType, "{\n\t\"Email\" : \"hahmed412@gmail.com\",\n\t\"Password\" : \"Qwerty12!@\"\n}");
        Request request = new Request.Builder()
                .url("https://backpackersapp.azurewebsites.net/api/Users/Login")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "6ff050b9-1b3e-5ee7-3e88-5318d681a972")
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    //get my expedition api call
    public  static String GetMyExpeditoin(String userid) throws IOException
    {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://backpackersapp.azurewebsites.net/api/Users/Expeditions/" + userid)
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "51de8709-cfd9-c296-9283-197ceae909e7")
                .build();

        Response response = client.newCall(request).execute();
        return  response.body().string();
    }

    //get other expeditions
    public static String GetAllExpeditions(String userid) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://backpackersapp.azurewebsites.net/api/Users/Upcoming/" + userid)
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "18a5a708-35b4-319f-3ebf-6095f226b095")
                .build();

        Response response = client.newCall(request).execute();
        return  response.body().string();
    }

    public static String GetExpById(String expid) throws IOException{

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://backpackersapp.azurewebsites.net/api/Users/Expeditions/Details/" + expid)
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "02ef4c82-e0af-fbe6-878d-d6acb1e392d0")
                .build();

        Response response = client.newCall(request).execute();
        return  response.body().string();
    }

    public static String UpdateExpStatus(String expidd, String state) throws IOException
    {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, " {\n    \"IdExpedition\": \""+expidd+"\",\n    \"State\": \""+state+"\"\n  }");
        Request request = new Request.Builder()
                .url("https://backpackersapp.azurewebsites.net/api/Users/Expeditions/State/"+expidd)
                .put(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "f848a2cb-0a86-8a8d-8897-81852c81bf1a")
                .build();

        Response response = client.newCall(request).execute();
        return  response.body().string();
    }

    public static String GetJoinedExpd(String useid) throws IOException
    {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://backpackersapp.azurewebsites.net/api/Users/Events/" + useid)
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "3a4a8292-29db-8f35-3b36-31ca447cb534")
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String UpdateUserLocation(String BackpackerId, String ExpeditionId, String Name, String Lat, String Lon) throws IOException {

        OkHttpClient client = new OkHttpClient();

        String url = "https://backpackersapp.azurewebsites.net/api/Tracks";

        RequestBody requestBody = new FormBody.Builder()
                .add("BackpackerId", BackpackerId)
                .add("ExpeditionId", ExpeditionId)
                .add("Name", ExpeditionId)
                .add("Lat", ExpeditionId)
                .add("Lng", ExpeditionId).build();

        Log.e("Req", requestBody.toString());

        Request request = new Request.Builder().url(url)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "f848a2cb-0a86-8a8d-8897-81852c81bf1a")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        Log.e("Resp", response.body().string());

        return response.body().string();


    }


    public static String UpdateExpedition(String IdExpedition, String State) throws IOException {

        OkHttpClient client = new OkHttpClient();

        String url = "https://backpackersapp.azurewebsites.net/api/Users/Expeditions/State/" + State;

        RequestBody requestBody = new FormBody.Builder()
                .add("IdExpedition", IdExpedition)
                .add("State", State)
                .build();

        Log.e("Req", requestBody.toString());

        Request request = new Request.Builder().url(url)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "f848a2cb-0a86-8a8d-8897-81852c81bf1a")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        Log.e("Resp", response.body().string());

        return response.body().string();


    }

    public static JSONArray getAllPins(String track) throws IOException {

        OkHttpClient client = new OkHttpClient();

        String url = "https://backpackersapp.azurewebsites.net/api/Tracks/" + track;

        Request request = new Request.Builder().url(url)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "f848a2cb-0a86-8a8d-8897-81852c81bf1a")
                .build();

        Response response = client.newCall(request).execute();

        String res = response.body().string();

        try {
            JSONArray resJSON = new JSONArray(res);

            return resJSON;

        } catch (JSONException e) {
            e.printStackTrace();

            return new JSONArray();
        }


    }





}
