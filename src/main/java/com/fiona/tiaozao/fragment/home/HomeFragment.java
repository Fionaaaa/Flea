package com.fiona.tiaozao.fragment.home;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fiona.tiaozao.App;
import com.fiona.tiaozao.MainActivity;
import com.fiona.tiaozao.ProductPagerActivity;
import com.fiona.tiaozao.R;
import com.fiona.tiaozao.bean.Goods;
import com.fiona.tiaozao.interactor.Interactor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    ViewPager viewPager;

    View viewDo1;
    View viewDo2;
    View viewDo3;

    int count = 0;          //viewpager当前页
    boolean isAuto = false; //判断是否自动滑动

    RvAdapter adapter;
    ArrayList<Goods> data = new ArrayList<>();

//    public Handler handler = new MyHandler();

    public HomeFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    //本地加载
    private void loadLocalSource() {

        new AsyncTask<String, String, ArrayList<Goods>>() {

            @Override
            protected ArrayList<Goods> doInBackground(String... params) {
                ArrayList<Goods> list;
                list = (ArrayList<Goods>) Goods.find(Goods.class, "flag = ?", "1");
                return list;
            }

            @Override
            protected void onPostExecute(ArrayList<Goods> list) {
                data.clear();
                for(Goods goods:list){
                    data.add(goods);
                }
                adapter.notifyItemRangeChanged(0, data.size());
            }
        }.execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        EventBus.getDefault().register(this);

        viewDo1 = view.findViewById(R.id.dot_1);
        viewDo2 = view.findViewById(R.id.dot_2);
        viewDo3 = view.findViewById(R.id.dot_3);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager_home);

        /**
         * viewpager的适配器
         */
        viewPager.setAdapter(new VpAdapter());

        /**
         * viewpager页面改变监听器
         */
        viewPager.addOnPageChangeListener(new VpListener());

        /**
         * viewpager自动切换
         */
        pagerAutoChange();

        viewPager.setCurrentItem(2);

        /**
         * recycler设置
         */
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_home);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL));

        adapter = new RvAdapter(getActivity(), data);
        recyclerView.setAdapter(adapter);

        loadLocalSource();

//        loadLocalSource();

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 设置适配器
     */
    private void setAdapter(ArrayList<Goods> list) {
        if (adapter != null) {
            adapter.notifyItemRangeChanged(0, adapter.data.size());
            adapter.notifyItemRangeRemoved(0, adapter.data.size());
        }
        adapter = new RvAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
    }

    /**
     * RecyclerView适配器
     */
    public class RvAdapter extends RecyclerView.Adapter<RvAdapter.Holder> implements View.OnClickListener {

        Context context;
        ArrayList<Goods> data;

        public RvAdapter(Context context, ArrayList<Goods> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.list_home, parent, false);
            Holder holder = new Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {

            if (!new Interactor().onlyWifi(getActivity())) {
                holder.imageView.setImageURI(Uri.parse(App.URL + data.get(position).getPic_location() + "small"));
            }

            holder.textViewName.setText(data.get(position).getTitle());
            holder.textViewPrice.setText(String.valueOf(data.get(position).getPrice()) + "￥");

            holder.view.setId(position);
            holder.view.setOnClickListener(this);

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        /**
         * 点击某件物品进行浏览
         *
         * @param v
         */
        @Override
        public void onClick(View v) {

            ((MainActivity) getActivity()).clickChangeBackgroundColor(v);

            Intent intent = new Intent(getActivity(), ProductPagerActivity.class);
            intent.putExtra(App.ACTION_GOODS, data);
            intent.putExtra("position", v.getId());
            intent.putExtra("where", "home");
            startActivity(intent);
        }

        /**
         * Holder类
         */
        public class Holder extends RecyclerView.ViewHolder {

            public SimpleDraweeView imageView;
            public TextView textViewName;
            public TextView textViewPrice;
            View view;

            public Holder(View view) {
                super(view);
                this.view = view;
                imageView = (SimpleDraweeView) view.findViewById(R.id.imageView_home);
                textViewName = (TextView) view.findViewById(R.id.textView_name);
                textViewPrice = (TextView) view.findViewById(R.id.textView_price);
            }

        }
    }

    /**
     * viewpager自动换页
     */
    private void pagerAutoChange() {
        new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(3500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isAuto = true;
                                viewPager.setCurrentItem(++count);
                            }
                        });
                    } else {
                        break;
                    }
                }
            }
        }.start();
    }

    /**
     * viewpager的监听器
     */
    public class VpListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            count = position;     //自动滑动的位置需要改变

            if (count == 6) {
                if (isAuto) {
                    viewPager.setCurrentItem(2, false);
                    count = 2;
                } else {
                    viewPager.setCurrentItem(3, false);
                    count = 3;
                }
            }
            if (count == 0) {
                if (isAuto) {
                    viewPager.setCurrentItem(4, false);
                    count = 4;
                } else {
                    viewPager.setCurrentItem(3, false);
                    count = 3;
                }
            }

            isAuto = false;

            //小点变化
            dotChange(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    /**
     * 切换小不点
     *
     * @param position
     */
    private void dotChange(int position) {
        if (getActivity() != null) {
            if (position == 2 || position == 5) {
                viewDo1.setBackground(getActivity().getResources().getDrawable(R.drawable.dot_focused));
                viewDo2.setBackground(getActivity().getResources().getDrawable(R.drawable.dot_normal));
                viewDo3.setBackground(getActivity().getResources().getDrawable(R.drawable.dot_normal));
            }
            if (position == 3) {
                viewDo1.setBackground(getActivity().getResources().getDrawable(R.drawable.dot_normal));
                viewDo2.setBackground(getActivity().getResources().getDrawable(R.drawable.dot_focused));
                viewDo3.setBackground(getActivity().getResources().getDrawable(R.drawable.dot_normal));
            }
            if (position == 1 || position == 4) {
                viewDo1.setBackground(getActivity().getResources().getDrawable(R.drawable.dot_normal));
                viewDo2.setBackground(getActivity().getResources().getDrawable(R.drawable.dot_normal));
                viewDo3.setBackground(getActivity().getResources().getDrawable(R.drawable.dot_focused));
            }
        }
    }


    /**
     * viewpager的适配器
     */
    class VpAdapter extends PagerAdapter implements View.OnClickListener {

        ImageView imageView0;
        ImageView imageView1;
        ImageView imageView2;
        ImageView imageView3;
        ImageView imageView4;
        ImageView imageView5;
        ImageView imageView6;

        ArrayList<ImageView> viewList;

        public VpAdapter() {
            viewList = new ArrayList<>();

            imageView0 = new ImageView(getActivity());
            imageView0.setBackgroundColor(getResources().getColor(R.color.purple));

            imageView1 = new ImageView(getActivity());
            imageView1.setBackgroundColor(getResources().getColor(R.color.red_m));

            imageView2 = new ImageView(getActivity());
            imageView2.setBackgroundColor(getResources().getColor(R.color.green_m));

            imageView3 = new ImageView(getActivity());
            imageView3.setBackgroundColor(getResources().getColor(R.color.purple));

            imageView4 = new ImageView(getActivity());
            imageView4.setBackgroundColor(getResources().getColor(R.color.red_m));

            imageView5 = new ImageView(getActivity());
            imageView5.setBackgroundColor(getResources().getColor(R.color.green_m));

            imageView6 = new ImageView(getActivity());
            imageView6.setBackgroundColor(getResources().getColor(R.color.purple));

            viewList.add(imageView0);
            viewList.add(imageView1);
            viewList.add(imageView2);
            viewList.add(imageView3);
            viewList.add(imageView4);
            viewList.add(imageView5);
            viewList.add(imageView6);

        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ViewPager viewPager = (ViewPager) container;
            viewPager.removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ViewPager viewPager = (ViewPager) container;
            ImageView imageView = viewList.get(position);

            viewPager.addView(imageView);
            imageView.setOnClickListener(this);

            return imageView;
        }


        /**
         * viewpager点击监听
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            int item = viewPager.getCurrentItem();    //获得当前页

            Intent intent = new Intent(getActivity(), AfficheActivity.class);
            startActivity(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void netTaskCompleted(String msg) {
        if (msg.equals(App.QUERY_SALE)) {
            data.clear();
            List<Goods>list =Goods.find(Goods.class, "flag=?", "1");
            for(Goods goods:list){
                data.add(goods);
            }
            adapter.notifyItemRangeChanged(0,data.size());
        }
    }
}

