package com.fiona.tiaozao.fragment.discover;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fiona.tiaozao.App;
import com.fiona.tiaozao.R;
import com.fiona.tiaozao.bean.Goods;
import com.fiona.tiaozao.interactor.Interactor;

public class PurchaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);

        setContentView(R.layout.activity_purchase2);

        initView();

    }

    private void initView() {
        Intent intent = getIntent();
        Goods goods = (Goods) intent.getExtras().get(App.ACTION_GOODS);

        SimpleDraweeView imageView = (SimpleDraweeView) findViewById(R.id.imageView_discover_right);
        String icon = Interactor.getIcon(goods.getGoods_id());
        if (icon != null) {
            if (!Interactor.onlyWifi(this)) {
                imageView.setImageURI(Uri.parse(icon));
            }else{
                imageView.setImageURI(Uri.parse(App.DEFAULT_PIC));
            }
        }

        TextView tvClassify = (TextView) findViewById(R.id.textView_purchase_classify);
        tvClassify.setText("求购·" + goods.getClassify());

        TextView tvAuthor = (TextView) findViewById(R.id.textView_purchase_author_discover2);
        tvAuthor.setText(goods.getUserName());

        TextView tvTime = (TextView) findViewById(R.id.textView_purchase_time2);
        tvTime.setText(String.valueOf(goods.getTime()));

        TextView tvDescribe = (TextView) findViewById(R.id.textView_purchase_describe);
        tvDescribe.setText(goods.getDescribe());
    }

    public void clickBackPurchaseActivity(View view) {
        finish();
    }
}
