package com.dsc.testmvp.model;

import com.dsc.testmvp.bean.User;

/**
 * Created by staff on 2016/9/5.
 */
public interface OnLoginListener {
    void loginSuccess(User user);
    void loginFaild();
}
