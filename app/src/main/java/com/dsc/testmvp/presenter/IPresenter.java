package com.dsc.testmvp.presenter;

import com.dsc.testmvp.ui.IView;

/**
 * Created by staff on 2016/9/5.
 */
public interface IPresenter {
    void setView(IView view);
    void login();
    void clear();
}
