package com.fiona.tiaozao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

    }

    /**
     * 点击返回
     * @param view
     */
    public void clickBackProductActivity(View view) {
        finish();
    }
}
