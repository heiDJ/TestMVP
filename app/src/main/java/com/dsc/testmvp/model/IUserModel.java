package com.dsc.testmvp.model;

/**
 * Created by staff on 2016/9/5.
 *  规范UserModel业务
 */
public interface IUserModel {
    void login(String userName, String usePsw, OnLoginListener onLoginListener);
}
