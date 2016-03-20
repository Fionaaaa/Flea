package com.fiona.tiaozao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fiona.tiaozao.bean.Goods;
import com.fiona.tiaozao.interactor.Interactor;

import java.util.ArrayList;

public class ProductPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    ImageView imageViewCollect;
    TextView textView;
    ArrayList<Goods> goodsList;
    Goods goods;
    int current;

    ImageView toLeft;
    ImageView toRight;
    Animation anLeft;
    Animation anRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product2);
        imageViewCollect = (ImageView) findViewById(R.id.icon_add_collection2);
        viewPager = (ViewPager) findViewById(R.id.view15);
        textView = (TextView) findViewById(R.id.textView13);

        initTitle(textView);    //初始化标题

        if (getPreferences(MODE_PRIVATE).getBoolean("isFirst", true)) {
            getPreferences(MODE_PRIVATE).edit().putBoolean("isFirst", false).commit();
            toLeft = (ImageView) findViewById(R.id.imageView_to_left);
            toRight = (ImageView) findViewById(R.id.imageView_to_right);
            toLeft.setVisibility(View.VISIBLE);
            toRight.setVisibility(View.VISIBLE);
            //滑动
            keepMove();
        }

        //从意图获得数据（物品列表）
        Intent intent = getIntent();
        goodsList = (ArrayList<Goods>) intent.getSerializableExtra(App.ACTION_GOODS);
        current = intent.getIntExtra("position", 0);


        if (goodsList.size() > 0) {
            viewPager.setAdapter(new VpAdapter(getSupportFragmentManager(), goodsList));
            viewPager.setCurrentItem(current);
            viewPager.addOnPageChangeListener(new Mylistener());

            if (new Interactor().isCollected(this, goodsList.get(current).getGoods_id(), 1)) {
                imageViewCollect.setImageResource(R.drawable.icon_add_collection);
            }
        }
    }

    private void initTitle(TextView textView) {
        String title = getIntent().getStringExtra("where");
        switch (title) {
            case "home":
                textView.setText("推 荐");
                break;
            case "classify":
                textView.setText(getIntent().getStringExtra("msg"));
                break;
            case "stall":
                textView.setText(getIntent().getStringExtra("msg"));
                break;
        }
    }

    public void clickBackActivity(View view) {
        finish();
    }

    //监听器
    private class Mylistener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (new Interactor().isCollected(ProductPagerActivity.this, goodsList.get(viewPager.getCurrentItem()).getGoods_id(), 1)) {
                imageViewCollect.setImageResource(R.drawable.icon_add_collection);
                Log.d("debug", "用户已收藏");
            } else {
                imageViewCollect.setImageResource(R.drawable.icon_remove_collecdtion);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    //viewpager适配器
    private class VpAdapter extends FragmentPagerAdapter {

        ArrayList<Goods> data;

        public VpAdapter(FragmentManager fm, ArrayList<Goods> goodsList) {
            super(fm);
            data = goodsList;
        }

        @Override
        public Fragment getItem(int position) {
            ProductFragment fragment = ProductFragment.getInstance(data.get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            return data.size();
        }
    }

    //引导用户左右滑动
    private void keepMove() {
        float x = toLeft.getX();
        float y = toLeft.getY();
        float x2 = toRight.getX();
        float y2 = toRight.getY();

        anLeft = new TranslateAnimation(x, x - 50, y, y);
        anLeft.setDuration(900);
        anLeft.setRepeatCount(1000);
        toLeft.setAnimation(anLeft);

        anRight = new TranslateAnimation(x2, x2 + 50, y2, y2);
        anRight.setDuration(900);
        anRight.setRepeatCount(1000);
        toRight.setAnimation(anRight);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (anLeft != null) {
            anLeft.cancel();
            anRight.cancel();
            toLeft.clearAnimation();
            toRight.clearAnimation();
            toLeft.setVisibility(View.INVISIBLE);
            toRight.setVisibility(View.INVISIBLE);
        }
        return super.dispatchTouchEvent(ev);
    }

    //收藏物品（或取消）:本地+服务器
    public void doCollect(View view) {
        goods = goodsList.get(viewPager.getCurrentItem());
        if (getSharedPreferences("user", MODE_PRIVATE).getBoolean("isLoad", false)) {
            if (new Interactor().isCollected(this, goods.getGoods_id(), 1)) {
                //已收藏
                imageViewCollect.setImageResource(R.drawable.icon_remove_collecdtion);
                //从本地以及服务器删除
                new Interactor().deletCollection(this, goods.getGoods_id(), 1);

                setResult(8080);    //如果从收藏表删除
            } else {
                imageViewCollect.setImageResource(R.drawable.icon_add_collection);
                //从本地以及服务器添加
                new Interactor().addCollection(this, goods.getGoods_id(), 1);

            }
        } else {
            Toast.makeText(ProductPagerActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }
}
