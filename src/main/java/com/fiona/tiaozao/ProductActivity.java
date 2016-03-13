package com.fiona.tiaozao;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fiona.tiaozao.bean.Goods;
import com.fiona.tiaozao.interactor.Interactor;
import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity {

    ImageView imageViewAddColelct;
    Goods goods;

    boolean flag;
    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        goods = (Goods) getIntent().getExtras().get(App.ACTION_GOODS);
        flag = getIntent().getBooleanExtra("fromFionaaaa", false);

        editText = (EditText) findViewById(R.id.editText2);
        textView= (TextView) findViewById(R.id.textView13);

        imageViewAddColelct = (ImageView) findViewById(R.id.icon_add_collection);

        initView();

    }

    /**
     * 初始化视图
     */
    private void initView() {
        if (Interactor.isCollected(this, goods.getGoods_id(), 1)) {
            imageViewAddColelct.setImageResource(R.drawable.icon_add_collection);
            Log.d("debug", "用户已收藏");
        }

        if (!Interactor.onlyWifi(this)) {
            ((SimpleDraweeView) findViewById(R.id.imageView_product_picture)).setImageURI(Uri.parse(App.URL + goods.getPic_location()));
        }
        ((TextView) findViewById(R.id.textView21_product_title)).setText(goods.getTitle());
        ((TextView) findViewById(R.id.textView23_product_price)).setText(String.valueOf(goods.getPrice()) + "￥");
        ((TextView) findViewById(R.id.textView25_product_describe)).setText(goods.getDescribe());
        ((TextView) findViewById(R.id.textView27_product_saler)).setText(goods.getUserName());
        ((TextView) findViewById(R.id.textView29_product_contact)).setText(goods.getContact());

        if (flag) {
            textView.setText("我的物品");
            editText.setVisibility(View.VISIBLE);
            editText.setHint(String.valueOf(goods.getPrice()));
            findViewById(R.id.textView23_product_price).setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 点击返回
     *
     * @param view
     */
    public void clickBackProductActivity(View view) {
        if (!String.valueOf(editText.getText()).equals(String.valueOf(goods.getPrice()))) {
            sendNetTask();
        }
        finish();
    }

    //收藏物品（或取消）:本地+服务器
    public void doCollect(View view) {
        if (getSharedPreferences("user", MODE_PRIVATE).getBoolean("isLoad", false)) {
            if (Interactor.isCollected(this, goods.getGoods_id(), 1)) {
                //已收藏
                imageViewAddColelct.setImageResource(R.drawable.icon_remove_collecdtion);
                //从本地以及服务器删除
                Interactor.deletCollection(this, goods.getGoods_id(), 1);

                setResult(8080);    //如果从收藏表删除
            } else {
                imageViewAddColelct.setImageResource(R.drawable.icon_add_collection);
                //从本地以及服务器添加
                Interactor.addCollection(this, goods.getGoods_id(), 1);

            }
        } else {
            Toast.makeText(ProductActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!String.valueOf(editText.getText()).equals(String.valueOf(goods.getPrice()))) {
                //发网络请求：价格变化
                sendNetTask();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //网络请求
    private void sendNetTask() {
        Interactor.updateGoods(this,editText.getText(),goods.getGoods_id());
    }
}
