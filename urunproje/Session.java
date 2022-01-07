package com.urunproje.urunproje;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {


    private SharedPreferences prefs;

    public Session(Context cntx) {

        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusename(String usename) {
        prefs.edit().putString("usename", usename).commit();
    }

    public String getusename() {
        String usename = prefs.getString("usename","");
        return usename;
    }

    public void setkayitturu(String kayitturu) {
        prefs.edit().putString("kayitturu", kayitturu).commit();
    }

    public String getkayitturu() {
        String kayitturu = prefs.getString("kayitturu","");
        return kayitturu;
    }


}
