package com.dsc.testmvp;

import android.app.Application;
import android.content.Context;

import com.dsc.testmvp.util.LocationUtil;

/**
 * Created by staff on 2016/9/28.
 */
public class APP extends Application {
    private static Context mApp = null;
    /**
     * 长途汽车信息API的Key
     */
    private static final String COACH_API_KEY = "35f588a3199402de770c273f2b6354bf";
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = getApplicationContext();
        //定位,随便玩玩
        LocationUtil.startLocation(this);
    }
    public static Context getApp(){
        return mApp;
    }
}
