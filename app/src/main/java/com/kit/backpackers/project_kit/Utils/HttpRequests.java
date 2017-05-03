package com.kit.backpackers.project_kit.Utils;

import java.io.IOException;

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

    public static String UserLogin(String useremail , String userpassword) throws IOException
    {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
       // RequestBody body = RequestBody.create(mediaType, "{\n\t\"Email\" : \""+useremail+"\",\n\t\"Password\" : \""+userpassword+"\"\n}");
        RequestBody body = RequestBody.create(mediaType, "{\n\t\"Email\" : \"hahmed412@gmail.com\",\n\t\"Password\" : \"Qwerty12!@\"\n}");
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
                .url("https://backpackersapp.azurewebsites.net/api/Users/Upcoming/"+userid)
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
                .url("http://code-dataserver.cloudapp.net/backpacker_api/index.php/user/getexpbyid/"+expid)
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "c8c32ef8-09d2-6552-9071-b5e33546791e")
                .build();

        Response response = client.newCall(request).execute();
        return  response.body().string();
    }
}
