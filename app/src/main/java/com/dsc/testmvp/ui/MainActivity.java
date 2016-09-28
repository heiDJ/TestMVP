package com.dsc.testmvp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.dsc.testmvp.R;
import com.dsc.testmvp.bean.User;
import com.dsc.testmvp.presenter.IPresenter;
import com.dsc.testmvp.presenter.LoginPresenter;
import com.dsc.testmvp.util.LocationUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity implements IView {
    private final static String TAG = "MainActivity";
    private EditText editUserName,editUserPsw;
    private Button btnLogin,btnClear;
    private ProgressBar pb;
    private IPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        LocationUtil.startLocation(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationUtil.stopLocation();
    }

    private void init() {
        editUserName = (EditText) findViewById(R.id.edit_userName);
        editUserPsw = (EditText) findViewById(R.id.edit_userPsw);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnClear = (Button) findViewById(R.id.btn_clear);
        pb = (ProgressBar) findViewById(R.id.pb);
        mPresenter = new LoginPresenter();
        mPresenter.setView(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.login();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.clear();
            }
        });
    }

    @Override
    public String getUserName() {
        return editUserName.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return editUserPsw.getText().toString().trim();
    }

    @Override
    public void clearUserName() {
        editUserName.setText("");
    }

    @Override
    public void clearPassword() {
        editUserPsw.setText("");
    }

    @Override
    public void showLoading() {
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pb.setVisibility(View.GONE);
    }

    @Override
    public void toNextActivity(User user) {
        Toast.makeText(MainActivity.this, "toNextActivity", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFailedError() {
        Toast.makeText(MainActivity.this, "showFailedError", Toast.LENGTH_SHORT).show();
    }

}
