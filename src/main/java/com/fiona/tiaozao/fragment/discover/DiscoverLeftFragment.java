package com.fiona.tiaozao.fragment.discover;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fiona.tiaozao.App;
import com.fiona.tiaozao.MainActivity;
import com.fiona.tiaozao.R;
import com.fiona.tiaozao.bean.User;
import com.fiona.tiaozao.interactor.Interactor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class DiscoverLeftFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeLayout;
    ArrayList<User> data = new ArrayList<>();
    RvAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        EventBus.getDefault().register(this);

        View view = inflater.inflate(R.layout.fragment_discover_left, container, false);

        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_dis_left);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.colorAccent, R.color.green_m, R.color.red_m);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_discover_left);

        initView();

        return view;
    }

    void initView() {
        data = getData();
        adapter = new RvAdapter(getActivity(), data);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        //开始网络请求
        Interactor.getUsers(getActivity());
    }

    /**
     * recycle适配器
     */
    public class RvAdapter extends RecyclerView.Adapter<RvAdapter.Holder> implements View.OnClickListener {

        Context context;
        ArrayList<User> data = new ArrayList<>();

        public RvAdapter(Context context, ArrayList<User> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_discover_sale, parent, false);
            Holder holder = new Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {

            holder.view.setOnClickListener(this);
            holder.view.setId(position);

            /**
             * 从（缓存）取数据显示
             */
            User user = data.get(position);

            if (!Interactor.onlyWifi(getActivity())) {
                holder.imageView.setImageURI(Uri.parse(user.getIcon()));
            } else {
                holder.imageView.setImageURI(Uri.parse(App.DEFAULT_PIC));
            }
            holder.textViewSale.setText(user.getName());
            holder.textViewDescription.setText("描述:" + user.getDescribe());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        /**
         * 点击事件
         *
         * @param v
         */
        @Override
        public void onClick(View v) {

            ((MainActivity) getActivity()).clickChangeBackgroundColor(v);

            Intent intent = new Intent(getActivity(), StallActivity.class);
            intent.putExtra(App.ACTION_USER, data.get(v.getId()));
            startActivity(intent);
        }

        /**
         * Holder类
         */
        class Holder extends RecyclerView.ViewHolder {

            public SimpleDraweeView imageView;
            public TextView textViewSale;
            public TextView textViewDescription;
            public TextView textViewTime;

            View view;

            public Holder(View view) {
                super(view);
                this.view = view;

                imageView = (SimpleDraweeView) view.findViewById(R.id.imageView_discover_left);

                textViewSale = (TextView) view.findViewById(R.id.textView_discover_left_author);
                textViewDescription = (TextView) view.findViewById(R.id.textView_discover_left_description);
                textViewTime = (TextView) view.findViewById(R.id.textView_discover_left_time);
            }
        }
    }

    //从本地取得数据
    private ArrayList<User> getData() {
        ArrayList<User> userList = (ArrayList<User>) User.listAll(User.class);
        return Interactor.getGoodsToUser(userList);
    }

    //收到网络加载完毕通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loadNetSource(String msg) {
        if (msg.equals(App.QUERY_USER)) {
            data = getData();      //加载本地数据
            adapter.notifyItemRangeChanged(0, data.size());//刷新列表
            mSwipeLayout.setRefreshing(false);  //取消刷新状态
        }
    }
}
