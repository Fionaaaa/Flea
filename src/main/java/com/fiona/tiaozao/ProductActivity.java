package com.fiona.tiaozao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiona.tiaozao.bean.Goods;
import com.fiona.tiaozao.interactor.Interactor;
import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity {

    ImageView imageViewAddColelct;
    Goods goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        goods = (Goods) getIntent().getExtras().get(App.ACTION_GOODS);

        imageViewAddColelct= (ImageView) findViewById(R.id.icon_add_collection);

        initView();

    }

    /**
     * 初始化视图
     */
    private void initView() {
        if(Interactor.isCollected(this, goods.getGoods_id())){
            imageViewAddColelct.setImageResource(R.drawable.icon_add_collection);
            Log.d("debug","用户已收藏");
        }

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

    //收藏物品（或取消）:本地+服务器
    public void doCollect(View view) {
        if(Interactor.isCollected(this, goods.getGoods_id())){
            //已收藏
            imageViewAddColelct.setImageResource(R.drawable.icon_remove_collecdtion);
            //从本地以及服务器删除
            Interactor.deletCollection(this, goods.getGoods_id(),1);
        }else{
            imageViewAddColelct.setImageResource(R.drawable.icon_add_collection);
            //从本地以及服务器添加
            Interactor.addCollection(this,goods.getGoods_id(),1);
        }
    }
}
