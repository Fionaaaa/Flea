package com.fiona.tiaozao.fragment.myself;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fiona.tiaozao.App;
import com.fiona.tiaozao.R;
import com.fiona.tiaozao.fragment.discover.PurchaseActivity;
import com.fiona.tiaozao.model.Goods;
import com.fiona.tiaozao.net.NetQuery;
import com.fiona.tiaozao.net.NetQueryImpl;

import java.util.ArrayList;

public class MyPurchaseActivity extends AppCompatActivity {

    ListView listView;

    private String userID = "1";  //用户id，暂时用1

    ArrayList<Goods> listEmption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchase);

        //开始请求网络
        Handler handler = new MyHandler();
        NetQuery query = NetQueryImpl.getInstance(this);
        query.getUserEmption(userID,handler);

        listView = (ListView) findViewById(R.id.listView_my_purchase);

        listView.setOnItemClickListener(new ListViewListener());
    }

    /**
     * 点击结束活动
     *
     * @param view
     */
    public void clickBackMyPurchaseActivity(View view) {
        finish();
    }

    /**
     * listview的适配器
     */
    class ListViewAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;

        ArrayList<Goods> data = new ArrayList<>();

        public ListViewAdapter(Context context, ArrayList<Goods> listEmption) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            if (listEmption != null) {
                data = listEmption;
            }
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
                convertView = inflater.inflate(R.layout.list_listview_my_purchase, parent, false);
                holder = new Holder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            Goods goods = data.get(position);
            holder.tvClassify.setText("求购·" + goods.getClassify());
            holder.tvDescribe.setText(goods.getDescribe());

            return convertView;
        }

        /**
         * Holder类
         */
        class Holder {
            TextView tvClassify;
            TextView tvDescribe;
            TextView tvTime;

            public Holder(View view) {
                tvClassify = (TextView) view.findViewById(R.id.textView32_my_emption_classify);
                tvDescribe = (TextView) view.findViewById(R.id.textView33_my_emption_describe);
                tvTime = (TextView) view.findViewById(R.id.textView34_my_emption_time);
            }
        }
    }

    /**
     * listView 的监听器
     */
    class ListViewListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(MyPurchaseActivity.this, PurchaseActivity.class);
            intent.putExtra(App.ACTION_GOODS, listEmption.get(position));
            startActivity(intent);

        }
    }

    /**
     * 网络请求成功
     */
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            listEmption = (ArrayList<Goods>) msg.obj;
            listView.setAdapter(new ListViewAdapter(MyPurchaseActivity.this, listEmption));
        }
    }
}
