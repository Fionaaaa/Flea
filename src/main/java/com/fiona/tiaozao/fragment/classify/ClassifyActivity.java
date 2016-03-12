package com.fiona.tiaozao.fragment.classify;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fiona.tiaozao.App;
import com.fiona.tiaozao.Product2Activity;
import com.fiona.tiaozao.R;
import com.fiona.tiaozao.bean.Goods;
import com.fiona.tiaozao.interactor.Interactor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class ClassifyActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ListView listView;
    String classify;

    ListViewAdapter adapter;
    SwipeRefreshLayout mSwipeLayout;

    ArrayList<Goods> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        EventBus.getDefault().register(this);   //总线注册

        classify = getIntent().getStringExtra("classify");

        TextView textView = (TextView) findViewById(R.id.textView_classify_activity_title);
        textView.append(classify);

        listView = (ListView) findViewById(R.id.listView_classify_activity);

        adapter = new ListViewAdapter(this, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ListViewListener());

        getData();    //从本地取数据

        initRefresh();
    }

    private void initRefresh() {
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.srl_classify);

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.colorAccent, R.color.green_m, R.color.red_m);
    }

    /**
     * 点击结束活动
     *
     * @param view
     */
    public void clickBackClassifyActivity(View view) {
        finish();
    }

    //刷新
    @Override
    public void onRefresh() {
        Interactor.getSales(this);  //请求网络
    }

    //接受总线通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void netTaskCompleted(String msg){
        if(msg.equals(App.QUERY_SALE)){
            getData();                          //获得本地数据
            mSwipeLayout.setRefreshing(false);  //取消刷新状态
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * listView 的适配器
     */
    class ListViewAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        ArrayList<Goods> data;

        public ListViewAdapter(Context context, ArrayList<Goods> data) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return data.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_classify_item, parent, false);
                holder = new Holder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            if (!Interactor.onlyWifi(ClassifyActivity.this)) {
                holder.imageView.setImageURI(Uri.parse(App.URL + data.get(position).getPic_location()));
            }
            holder.textViewTitle.setText(data.get(position).getTitle());
            holder.textViewPrice.setText(String.valueOf(data.get(position).getPrice()) + "￥");
            holder.textViewDescribe.setText(data.get(position).getDescribe());

            return convertView;
        }

        /**
         * Holder类
         */
        class Holder {
            SimpleDraweeView imageView;
            TextView textViewTitle;
            TextView textViewPrice;
            TextView textViewDescribe;

            Holder(View view) {
                imageView = (SimpleDraweeView) view.findViewById(R.id.lci_sd);
                textViewTitle = (TextView) view.findViewById(R.id.lci_name);
                textViewPrice = (TextView) view.findViewById(R.id.lci_price);
                textViewDescribe = (TextView) view.findViewById(R.id.lci_describe);
            }
        }
    }

    /**
     * listView 的监听器
     */
    class ListViewListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(ClassifyActivity.this, Product2Activity.class);
            intent.putExtra(App.ACTION_GOODS, data);
            intent.putExtra("position", position);
            intent.putExtra("where", "classify");
            startActivity(intent);

        }
    }

    /**
     * 从本地取数据,设置listView
     */
    private void getData() {
        ArrayList<Goods> list = (ArrayList<Goods>) Goods.find(Goods.class, "classify = ? and flag=?", classify, "1");
        data.clear();
        if (list.size() > 0) {
            for (Goods goods : list) {
                data.add(goods);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
