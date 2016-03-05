package com.fiona.tiaozao.fragment.myself;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fiona.tiaozao.R;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }

    /**
     * 点击返回
     *
     * @param view
     */
    public void returnFeedbackActivity(View view) {
        finish();
    }

    /**
     * 点击提交
     *
     * @param view
     */
    public void clickCommit(View view) {
        //auto
    }
}
