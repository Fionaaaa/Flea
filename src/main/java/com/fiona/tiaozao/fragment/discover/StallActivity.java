package com.fiona.tiaozao.fragment.discover;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fiona.tiaozao.App;
import com.fiona.tiaozao.ProductPagerActivity;
import com.fiona.tiaozao.R;
import com.fiona.tiaozao.bean.Goods;
import com.fiona.tiaozao.bean.User;
import com.fiona.tiaozao.interactor.Interactor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class StallActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ListView listView;

    ArrayList<Goods> data = new ArrayList<>();
    User user;
    ImageView icon;
    SwipeRefreshLayout mSwipeLayout;
    TextView textViewWho;

    ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stall);

        EventBus.getDefault().register(this);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.srl_dis_stall);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.colorAccent, R.color.green_m, R.color.red_m);

        icon = (ImageView) findViewById(R.id.icon_stall_collect);
        user = (User) getIntent().getExtras().get(App.ACTION_USER);

        data = user.getListSale();

        listView = (ListView) findViewById(R.id.listView_discover_stall_activity);
        textViewWho= (TextView) findViewById(R.id.who_stall);
        textViewWho.setText(user.getName()+"的摊位");

        adapter = new ListViewAdapter(this, data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new ListViewListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //判断是否已收藏此摊位
        if (Interactor.isCollected(this, user.getUser_id(), 0)) {
            icon.setImageResource(R.drawable.icon_add_collection);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void clickBackStallActivity(View view) {
        finish();
    }

    //收藏操作
    public void doCollect(View view) {
        if (getSharedPreferences("user", MODE_PRIVATE).getBoolean("isLoad", false)) {
            if (Interactor.isCollected(this, user.getUser_id(), 0)) {
                icon.setImageResource(R.drawable.icon_remove_collecdtion);
                Interactor.deletCollection(this, user.getUser_id(), 0);

                setResult(8080);    //如果从收藏表删除
            } else {
                icon.setImageResource(R.drawable.icon_add_collection);
                Interactor.addCollection(this, user.getUser_id(), 0);
            }
        } else {
            Toast.makeText(StallActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {
        //开始网络请求
        Interactor.getUserGoods(this, user.getUser_id());
    }

    /**
     * listView的适配器
     */
    class ListViewAdapter extends BaseAdapter {

        ArrayList<Goods> data;

        Context context;
        LayoutInflater inflater;

        public ListViewAdapter(Context context, ArrayList<Goods> data) {
            this.data = data;
            this.context = context;
            inflater = LayoutInflater.from(context);
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
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Holder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_listview_product, parent, false);
                holder = new Holder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            Goods goods = data.get(position);
            if (!Interactor.onlyWifi(StallActivity.this)) {
                holder.imageView.setImageURI(Uri.parse(App.URL + goods.getPic_location()));
            }
            holder.tvTitle.setText(goods.getTitle());
            holder.tvPrice.setText(String.valueOf(goods.getPrice()) + "￥");
            holder.tvDescribe.setText(goods.getDescribe());

            return convertView;
        }

        /**
         * Holder类
         */
        class Holder {
            SimpleDraweeView imageView;
            TextView tvTitle;
            TextView tvPrice;
            TextView tvDescribe;

            Holder(View view) {
                imageView = (SimpleDraweeView) view.findViewById(R.id.textView30_classify_product_picture);
                tvTitle = (TextView) view.findViewById(R.id.textView30_classify_product_title);
                tvPrice = (TextView) view.findViewById(R.id.textView30_classify_product_price);
                tvDescribe = (TextView) view.findViewById(R.id.textView31_classify_product_describe);
            }
        }
    }

    /**
     * listView 的监听器
     */
    class ListViewListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(StallActivity.this, ProductPagerActivity.class);
            intent.putExtra(App.ACTION_GOODS, data);
            intent.putExtra("position", position);
            intent.putExtra("where", "stall");
            intent.putExtra("msg",user.getName());
            startActivity(intent);
        }
    }

    //本地请求
    private ArrayList<Goods> getData() {
        ArrayList<Goods> list = (ArrayList<Goods>) Goods.find(Goods.class, "user_id=? and flag=?", user.getUser_id(), "1");
        return list;
    }

    //网络请求完成
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void netTaskCompleted(String msg) {
        if (msg.equals(App.QUERY_USER_GOODS)) {
            data=getData();
            adapter.notifyDataSetChanged();     //数据集改变
            mSwipeLayout.setRefreshing(false);  //刷新状态取消
        }
    }
}
