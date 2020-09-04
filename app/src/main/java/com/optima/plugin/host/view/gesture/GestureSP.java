package com.optima.plugin.host.view.gesture;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * create by wma
 * on 2020/8/31 0031
 */
public class GestureSP {

    private final String SP_NAME = "GestureName";
    private Context mContext;
    private SharedPreferences mSP;

    public GestureSP(Context mContext) {
        this.mContext = mContext;
        mSP = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
    }


    public void putPassword(String password) {
        mSP.edit().putString("password",password).apply();
    }

    public  String getPassword(){
        return mSP.getString("password","");
    }
}
