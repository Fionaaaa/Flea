package com.fiona.tiaozao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SaleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
    }

    /**
     * 点击提交
     *
     * @param view
     */
    public void clickSure(View view) {
        Toast.makeText(SaleActivity.this, "暂未实现该功能", Toast.LENGTH_SHORT).show();
    }

    /**
     * 点击返回
     *
     * @param view
     */
    public void returnActivity(View view) {
        finish();
    }

    /**
     * 点击添加图片
     *
     * @param view
     */
    public void clickAddPicture(View view) {
    }
}
