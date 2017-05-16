package com.kit.backpackers.project_kit.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 *  on 4/21/2017.
 */

public class ExpeditionSession {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 1;
    private static final String PREF_NAME = "exppref";
    private static final String IS_LOGIN = "isexxpstarted";

    //for user auto login feature...
    public static final String expeditionid = "expeditionid";



    // Constructor
    public ExpeditionSession(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //store the data
    public void createExpSession(String _id){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(expeditionid, _id);


        editor.commit();
    }

    //get the stored data
    public HashMap<String, String> getExpDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(expeditionid, pref.getString(expeditionid, null));

        // return user
        return user;
    }

    //delete the store user info
    public void logoutUser(){
        editor.clear();
        editor.commit();
    }
}
