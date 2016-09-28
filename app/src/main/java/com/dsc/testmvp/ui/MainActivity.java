package com.dsc.testmvp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dsc.testmvp.R;
import com.dsc.testmvp.bean.User;
import com.dsc.testmvp.presenter.ILoginPresenter;
import com.dsc.testmvp.presenter.LoginPresenter;
import com.dsc.testmvp.util.LocationUtil;

public class MainActivity extends AppCompatActivity implements IView {
    private final static String TAG = "MainActivity";
    private EditText editUserName,editUserPsw;
    private Button btnLogin,btnClear;
    private ProgressBar pb;
    private ILoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
