package com.fiona.tiaozao.fragment.myself;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fiona.tiaozao.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    /**
     * 点击返回
     *
     * @param view
     */
    public void returnAboutActivity(View view) {
        finish();
    }

    /**
     * 点击反馈
     *
     * @param view
     */
    public void clickToFeedback(View view) {
        Intent intent=new Intent(this,FeedbackActivity.class);
        startActivity(intent);
    }
}
