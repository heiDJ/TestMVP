package com.dsc.testmvp.model;

import android.os.SystemClock;
import android.util.TimeUtils;

import com.dsc.testmvp.bean.User;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * Created by staff on 2016/9/5.
 */
public class UserModel implements IUserModel {
    @Override public void login(final String userName, final String usePsw, final OnLoginListener onLoginListener) {
        new Thread(){
            @Override public void run() {
                //耗时操作
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(userName.equals("dsc") && usePsw.equals("123") && onLoginListener!=null){
                    User user = new User();
                    user.setName(userName);
                    user.setPsw(usePsw);
                    onLoginListener.loginSuccess(user);
                }else {
                    onLoginListener.loginFaild();
                }
            }
        }.start();
    }
}
