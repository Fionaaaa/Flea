package com.fiona.tiaozao.fragment.classify;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fiona.tiaozao.App;
import com.fiona.tiaozao.ProductActivity;
import com.fiona.tiaozao.R;
import com.fiona.tiaozao.model.Goods;
import com.fiona.tiaozao.net.NetQuery;
import com.fiona.tiaozao.net.NetQueryImpl;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ClassifyActivity extends AppCompatActivity {

    ListView listView;
    String classify;

    ListViewAdapter adapter;

    ArrayList<Goods> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);

        classify = getIntent().getStringExtra("classify");

        Handler handler = new MyHandler();
        NetQuery query = NetQueryImpl.getInstance(this);
        query.getClassifyGoods(classify,handler);


        TextView textView = (TextView) findViewById(R.id.textView_classify_activity_title);
        textView.append(classify);

        listView = (ListView) findViewById(R.id.listView_classify_activity);

        adapter = new ListViewAdapter(this,data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new ListViewListener());
    }

    /**
     * 点击结束活动
     *
     * @param view
     */
    public void clickBackClassifyActivity(View view) {
        finish();
    }

    /**
     * listView 的适配器
     */
    class ListViewAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        ArrayList<Goods> data;

        public ListViewAdapter(Context context,ArrayList<Goods> data) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data=data;
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
            if(convertView==null){
                convertView = inflater.inflate(R.layout.list_listview_product, parent, false);
                holder=new Holder(convertView);
                convertView.setTag(holder);
            }else{
                holder= (Holder) convertView.getTag();
            }

            Picasso.with(context).load(App.URL+data.get(position).getPic_location()).into(holder.imageView);
            holder.textViewTitle.setText(data.get(position).getTitle());
            holder.textViewPrice.setText(String.valueOf(data.get(position).getPrice()));
            holder.textViewDescribe.setText(data.get(position).getDescribe());

            return convertView;
        }

        /**
         * Holder类
         */
        class Holder {
            ImageView imageView;
            TextView textViewTitle;
            TextView textViewPrice;
            TextView textViewDescribe;

            Holder(View view){
                imageView= (ImageView) view.findViewById(R.id.imageView8_classify_product_pic);
                textViewTitle= (TextView) view.findViewById(R.id.textView22_classify_product_title);
                textViewPrice= (TextView) view.findViewById(R.id.textView30_classify_product_price);
                textViewDescribe= (TextView) view.findViewById(R.id.textView31_classify_product_describe);
            }
        }
    }

    /**
     * listView 的监听器
     */
    class ListViewListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(ClassifyActivity.this, ProductActivity.class);
            intent.putExtra(App.ACTION_GOODS,data.get(position));
            startActivity(intent);

        }
    }

    /**
     * 网络加载结束
     */
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            data = (ArrayList<Goods>) msg.obj;
            adapter=new ListViewAdapter(ClassifyActivity.this,data);
            listView.setAdapter(adapter);
        }
    }
}
