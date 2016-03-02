package com.fiona.tiaozao.account;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fiona.tiaozao.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * 点击返回
     *
     * @param view
     */
    public void clickLoginActivity(View view) {
        finish();
    }
}
