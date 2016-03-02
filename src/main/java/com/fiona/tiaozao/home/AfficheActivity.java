package com.fiona.tiaozao.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fiona.tiaozao.R;

public class AfficheActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiche);
    }

    /**
     * 点击返回
     *
     * @param view
     */
    public void clickBackAfficheActivity(View view) {
        finish();
    }
}
