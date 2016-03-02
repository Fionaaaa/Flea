package com.fiona.tiaozao.classify;

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

public class ClassifyActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);

        TextView textView = (TextView) findViewById(R.id.textView_classify_activity_title);
        textView.append(getIntent().getStringExtra("classify"));

        listView = (ListView) findViewById(R.id.listView_classify_activity);

        listView.setAdapter(new ListViewAdapter(this));

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

        public ListViewAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 50;
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

            convertView = inflater.inflate(R.layout.list_listview_product, parent, false);

            return convertView;
        }

        /**
         * Holder类
         */
        class Holder {

        }
    }

    /**
     * listView 的监听器
     */
    class ListViewListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(ClassifyActivity.this, ProductActivity.class);
            startActivity(intent);

        }
    }
}
