package com.fiona.tiaozao.myself;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fiona.tiaozao.ProductActivity;
import com.fiona.tiaozao.R;
import com.fiona.tiaozao.discover.StallActivity;

public class MyCollectionActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);

        listView = (ListView) findViewById(R.id.listView_collection);

        listView.setAdapter(new MyAdapter(this));

        listView.setOnItemClickListener(new MyListener());

    }

    /**
     * 点击返回
     *
     * @param view
     */
    public void returnMyCollectionActivity(View view) {
        finish();
    }

    /**
     * 适配器
     */
    class MyAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;

        public MyAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 12;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                convertView = inflater.inflate(R.layout.item_my_collection, parent, false);
            } else if (position > 0 && position < 6) {
                convertView = inflater.inflate(R.layout.list_discover_sale, parent, false);

                convertView.findViewById(R.id.view_line).setVisibility(View.INVISIBLE);
                convertView.findViewById(R.id.textView_discover_time).setVisibility(View.INVISIBLE);

            } else if (position == 6) {
                convertView = inflater.inflate(R.layout.item_my_collection, parent, false);

                TextView textView = (TextView) convertView.findViewById(R.id.textView_my_collection);
                textView.setText("商品");
            } else {
                convertView = inflater.inflate(R.layout.list_listview_product, parent, false);
            }
            return convertView;
        }

        /**
         * Holder类
         */
        class Holder {

            public Holder(View view) {
            }
        }
    }

    /**
     * 监听器
     */
    class MyListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0 && position < 6) {
                Intent intent = new Intent(MyCollectionActivity.this, StallActivity.class);
                startActivity(intent);
            }
            if (position > 6) {
                Intent intent = new Intent(MyCollectionActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        }
    }
}
