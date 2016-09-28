package com.dsc.testmvp.presenter;

import android.os.Handler;
import android.util.Log;

import com.dsc.testmvp.bean.User;
import com.dsc.testmvp.model.IUserModel;
import com.dsc.testmvp.model.OnLoginListener;
import com.dsc.testmvp.model.UserModel;
import com.dsc.testmvp.ui.IView;

/**
 * Created by staff on 2016/9/5.
 * 完成View于Model间的交互
 */
public class LoginPresenter implements ILoginPresenter {
    private final static String TAG = "LoginPresenter";
    private IView mView;
    private IUserModel userModel;
    private final static Handler mHandler = new Handler();
    @Override
    public void setView(IView view) {
        mView = view;
        userModel = new UserModel();
    }

    @Override
    public void login() {
        mView.showLoading();
        userModel.login(mView.getUserName(), mView.getPassword(), new OnLoginListener() {
            @Override
            public void loginSuccess(final User user) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,user.toString());
                        mView.hideLoading();
                        mView.toNextActivity(user);
                    }
                });
            }

            @Override
            public void loginFaild() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"loginFaild");
                        mView.hideLoading();
                        mView.showFailedError();
                    }
                });
            }
        });
    }

    @Override
    public void clear() {
        mView.clearPassword();
        mView.clearUserName();
    }
}
