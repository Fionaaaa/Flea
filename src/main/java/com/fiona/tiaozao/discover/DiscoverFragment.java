package com.fiona.tiaozao.discover;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fiona.tiaozao.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {

    ViewPager viewPager;

    Button buttonLeft;
    Button buttonRight;

    boolean isLeftDiscover = true;

    public DiscoverFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        buttonLeft = (Button) view.findViewById(R.id.button_left_discover);
        buttonRight = (Button) view.findViewById(R.id.button_right_discover);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager_discover);
        viewPager.setAdapter(new VpAdapter(getChildFragmentManager()));
        viewPager.addOnPageChangeListener(new PagerChangeListener());

        initButtonState();  //初始化button状态

        return view;
    }

    /**
     * 初始化 button 的状态
     */
    private void initButtonState() {
        if (!isLeftDiscover) {
            buttonLeft.setBackground(getResources().getDrawable(R.drawable.discover_tab_left_blue));
            buttonLeft.setTextColor(getResources().getColor(R.color.white));

            buttonRight.setBackground(getResources().getDrawable(R.drawable.discover_tab_right_white));
            buttonRight.setTextColor(getResources().getColor(R.color.blue_m));
        }
    }

    /**
     * ViewPager 适配器
     */
    public class VpAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> data = new ArrayList<>();

        DiscoverLeftFragment fgLeft;
        DiscoverRightFragment fgRight;

        public VpAdapter(FragmentManager fm) {
            super(fm);
            fgLeft = new DiscoverLeftFragment();
            fgRight = new DiscoverRightFragment();

            data.add(fgLeft);
            data.add(fgRight);
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    /**
     * viewpager页面改变监听
     */
    public class PagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //button选中状态改变
            if (position == 0) {
                clickButton(buttonLeft);
            } else {
                clickButton(buttonRight);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 点击tab滑动viewpager
     *
     * @param position
     */
    public void pagerScrollTo(int position) {

        if (viewPager != null) {
            viewPager.setCurrentItem(position, true);
        }
    }

    /**
     * 点击tab_Button
     *
     * @param view
     */
    public void clickButton(View view) {

        //点击 摊位
        if (view.getId() == R.id.button_left_discover && !isLeftDiscover) {
            buttonLeft.setBackground(getResources().getDrawable(R.drawable.discover_tab_left_white));
            buttonLeft.setTextColor(getResources().getColor(R.color.blue_m));

            buttonRight.setBackground(getResources().getDrawable(R.drawable.discover_tab_right_blue));
            buttonRight.setTextColor(getResources().getColor(R.color.white));

            pagerScrollTo(0);
            isLeftDiscover = true;
        }

        //点击 求购
        if (view.getId() == R.id.button_right_discover && isLeftDiscover) {
            buttonLeft.setBackground(getResources().getDrawable(R.drawable.discover_tab_left_blue));
            buttonLeft.setTextColor(getResources().getColor(R.color.white));

            buttonRight.setBackground(getResources().getDrawable(R.drawable.discover_tab_right_white));
            buttonRight.setTextColor(getResources().getColor(R.color.blue_m));

            pagerScrollTo(1);
            isLeftDiscover = false;
        }
    }

}
