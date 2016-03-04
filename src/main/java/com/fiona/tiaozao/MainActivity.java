package com.fiona.tiaozao;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fiona.tiaozao.account.LoginActivity;
import com.fiona.tiaozao.classify.ClassifyFragment;
import com.fiona.tiaozao.discover.DiscoverFragment;
import com.fiona.tiaozao.home.HomeFragment;
import com.fiona.tiaozao.model.Goods;
import com.fiona.tiaozao.myself.AboutActivity;
import com.fiona.tiaozao.myself.MyCollectionActivity;
import com.fiona.tiaozao.myself.MyPurchaseActivity;
import com.fiona.tiaozao.myself.MyStallActivity;
import com.fiona.tiaozao.myself.MyselfFragment;
import com.fiona.tiaozao.myself.SettingActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm;     //  布局管理器

    GridView gridView;

    /**
     * 四个Fragment实例
     */
    HomeFragment homeFragment;
    ClassifyFragment classifyFragment;
    DiscoverFragment discoverFragment;
    MyselfFragment myselfFragment;

    /**
     * Add的字段
     */
    boolean isAdd = false;
    int fromDegrees = 0;
    View viewAdd;

    AlertDialog dialog; //登陆对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //片段实例化
        homeFragment = new HomeFragment();
        classifyFragment = new ClassifyFragment();
        discoverFragment = new DiscoverFragment();
        myselfFragment = new MyselfFragment();

        //布局管理器开始业务
        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.fragment_container, homeFragment).commit();

        //一级选项 Tab
        gridView = (GridView) findViewById(R.id.gridView_main);
        final MyGridViewAdapter gridViewAdapter = new MyGridViewAdapter(this);
        gridView.setAdapter(gridViewAdapter);

        /**
         * gridView监听
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                changeFragment(position);

                gridViewAdapter.setCurrent(position);
            }

            /**
             * 切换界面
             * @param position
             */
            public void changeFragment(int position) {
                switch (position) {
                    case 0:
                        fm.beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                        break;
                    case 1:
                        fm.beginTransaction().replace(R.id.fragment_container, classifyFragment).commit();
                        break;
                    case 2:
                        fm.beginTransaction().replace(R.id.fragment_container, discoverFragment).commit();
                        break;
                    case 3:
                        fm.beginTransaction().replace(R.id.fragment_container, myselfFragment).commit();
                        break;
                }
            }
        });

    }

    /**
     * 点击：MyselfFragment的选项
     *
     * @param view
     */
    public void onClick(final View view) {

        /*ArrayList<Integer> ids = new ArrayList<>();
        ids.add(R.id.relativeLayout_myself_2);  //我的摊位
        ids.add(R.id.relativeLayout_myself_3);  //我的求购
        ids.add(R.id.relativeLayout_myself_4);  //收藏
        ids.add(R.id.relativeLayout_myself_5);  //关于我们
        ids.add(R.id.relativeLayout_myself_6);  //设置*/

        //点击背景变色
        clickChangeBackgroundColor(view);

        switch (view.getId()) {
            case R.id.relativeLayout_myself_1:
                choiceLoginMethod();
                break;
            case R.id.relativeLayout_myself_2:
                startActivity(new Intent(this, MyStallActivity.class));
                break;
            case R.id.relativeLayout_myself_3:
                startActivity(new Intent(this, MyPurchaseActivity.class));
                break;
            case R.id.relativeLayout_myself_4:
                startActivity(new Intent(this, MyCollectionActivity.class));
                break;
            case R.id.relativeLayout_myself_5:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.relativeLayout_myself_6:
                startActivity(new Intent(this, SettingActivity.class));
                break;

            //登陆方式
            case R.id.relativeLayout_login_qq:
                login(R.id.relativeLayout_login_qq);
                break;
            case R.id.relativeLayout_login_weibo:
                login(R.id.relativeLayout_login_weibo);
                break;
        }
    }

    /**
     * 选择登陆方式
     */
    private void choiceLoginMethod() {

        View view = LayoutInflater.from(this).inflate(R.layout.item_login_method, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(view);

        builder.create();
        dialog = builder.show();
    }

    /**
     * 登陆
     *
     * @param id
     */
    private void login(int id) {
        dialog.cancel();    //  对话框取消

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 点击背景变色
     *
     * @param view
     */
    public void clickChangeBackgroundColor(final View view) {
        view.setBackgroundColor(Color.argb(150, 0x66, 0x99, 0xFF));
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setBackgroundColor(Color.WHITE);
            }
        }, 100);
    }

    /**
     * 点击：DiscoverFragment的Tab
     *
     * @param view
     */
    public void clickButtonDiscover(View view) {
        discoverFragment.clickButton(view);
    }

    /**
     * 点击所有的 +
     *
     * @param viewAdd
     */
    public void clickAdd(View viewAdd) {
        this.viewAdd = viewAdd;
        isAdd = !isAdd;
        View view = (View) viewAdd.getParent();
        View v = view.findViewById(R.id.add);
        if (isAdd) {
            v.setVisibility(View.VISIBLE);  //  选项可见
            Animation animation = new AlphaAnimation(0f, 1.0f);
            animation.setDuration(500);
            v.startAnimation(animation);
        } else {
            v.setVisibility(View.INVISIBLE);//  选项不可见
            Animation animation = new AlphaAnimation(1.0f, 0f);
            animation.setDuration(500);
            v.startAnimation(animation);
        }

        //Add旋转
        RotateAnimation animation = new RotateAnimation(fromDegrees,
                fromDegrees + 45,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        fromDegrees += 45;
        animation.setDuration(500);
        animation.setFillAfter(true);
        viewAdd.startAnimation(animation);

    }

    /**
     * 点击搜索
     *
     * @param view
     */
    public void clickSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    /**
     * 点击发布，求购
     *
     * @param view
     */
    public void clickAddSelect(View view) {
        if (view.getId() == R.id.textView_add_sale) {
            startActivity(new Intent(this, SaleActivity.class));
        } else {
            startActivity(new Intent(this, PurchaseActivity.class));
        }
    }

    /**
     * 活动触摸事件分发
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //取消添加的视图
        if (isAdd && ev.getY() > 300) {
            clickAdd(viewAdd);
        }
        return super.dispatchTouchEvent(ev);
    }

}
