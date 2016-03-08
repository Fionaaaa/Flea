package com.fiona.tiaozao;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiona.tiaozao.bean.Goods;
import com.fiona.tiaozao.bean.User;
import com.fiona.tiaozao.net.NetQuery;
import com.fiona.tiaozao.net.NetQueryImpl;
import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        initView();

    }

    /**
     * 初始化视图
     */
    private void initView() {
        Goods goods = (Goods) getIntent().getExtras().get(App.ACTION_GOODS);

        Picasso.with(this).load(App.URL + goods.getPic_location()).into((ImageView) findViewById(R.id.imageView_product_picture));
        ((TextView) findViewById(R.id.textView21_product_title)).setText(goods.getTitle());
        ((TextView) findViewById(R.id.textView23_product_price)).setText(String.valueOf(goods.getPrice()) + "￥");
        ((TextView) findViewById(R.id.textView25_product_describe)).setText(goods.getDescribe());
        ((TextView) findViewById(R.id.textView27_product_saler)).setText(goods.getUserName());
        ((TextView) findViewById(R.id.textView29_product_contact)).setText(goods.getContact());

    }

    /**
     * 点击返回
     *
     * @param view
     */
    public void clickBackProductActivity(View view) {
        finish();
    }
}
