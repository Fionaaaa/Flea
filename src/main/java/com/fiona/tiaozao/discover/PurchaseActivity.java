package com.fiona.tiaozao.discover;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fiona.tiaozao.R;

public class PurchaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase2);
    }

    public void clickBackPurchaseActivity(View view) {
        finish();
    }
}
