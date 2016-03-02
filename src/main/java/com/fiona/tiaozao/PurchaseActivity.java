package com.fiona.tiaozao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class PurchaseActivity extends AppCompatActivity {

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        spinner = (Spinner) findViewById(R.id.spinner);

    }

    /**
     * 点击提交
     *
     * @param view
     */
    public void clickSure(View view) {
        Toast.makeText(this, "暂未实现该功能", Toast.LENGTH_SHORT).show();
    }

    /**
     * 点击返回
     *
     * @param view
     */
    public void returnActivity(View view) {
        finish();
    }

}
