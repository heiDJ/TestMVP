package com.dsc.testmvp.ui;

import com.dsc.testmvp.bean.User;

/**
 * Created by staff on 2016/9/5.
 * view层
 */
public interface IView {
    String getUserName();

    String getPassword();

    void clearUserName();

    void clearPassword();

    void showLoading();

    void hideLoading();

    void toNextActivity(User user);

    void showFailedError();
}
