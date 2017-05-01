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
        RequestBody body = RequestBody.create(mediaType, "{\n\t\"email\":\""+useremail+"\",\n\t\"password\":\""+userpassword+"\"\n}");
        Request request = new Request.Builder()
                .url("http://code-dataserver.cloudapp.net/backpacker_api/index.php/user/login")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "c7ae283f-785d-9672-99b1-2797ddcfcb4a")
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    //get my expedition api call
    public  static String GetMyExpeditoin(String userid) throws IOException
    {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://code-dataserver.cloudapp.net/backpacker_api/index.php/user/myexpedition/" + userid)
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "5e927ea0-aebe-b58b-6be7-9c799172a24d")
                .build();

        Response response = client.newCall(request).execute();
        return  response.body().string();
    }

    //get other expeditions
    public static String GetAllExpeditions(String userid) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://code-dataserver.cloudapp.net/backpacker_api/index.php/user/otherexpedition/"+userid)
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "0ac701b1-270f-659d-16bf-fd65c0968a3d")
                .build();

        Response response = client.newCall(request).execute();
        return  response.body().string();
    }
}
