package com.kit.backpackers.project_kit.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

/**
 *  on 4/21/2017.
 */

public class UserLoginSession {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 1;
    private static final String PREF_NAME = "user_login";
    private static final String IS_LOGIN = "IsUserLoggedIn";

    //for user auto login feature...
    public static final String username = "username";
    public static final String picture = "userpicture";
    public static  final String userid = "userid";


    // Constructor
    public UserLoginSession(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //store the data
    public void createLoginSession(String _id, String _name , String _picture){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(userid, _id);
        editor.putString(username, _name);
        editor.putString(picture, _picture);
        editor.commit();
    }

    //get the stored data
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(userid, pref.getString(userid, null));
        user.put(username, pref.getString(username, null));
        user.put(picture, pref.getString(picture, null));

        Log.e("******", "Saved");
        Const.BpId = pref.getString(userid, "");
        Const.Name = pref.getString(username, "");

        // return user
        return user;
    }

    //delete the store user info
    public void logoutUser(){
        editor.clear();
        editor.commit();
    }
}
