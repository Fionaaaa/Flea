package com.fiona.tiaozao.discover;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.fiona.tiaozao.ProductActivity;
import com.fiona.tiaozao.R;

public class StallActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stall);

        listView = (ListView) findViewById(R.id.listView_discover_stall_activity);

        listView.setAdapter(new ListViewAdapter(this));

        listView.setOnItemClickListener(new ListViewListener());
    }

    public void clickBackStallActivity(View view) {
        finish();
    }

    /**
     * listView的适配器
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
            return 5;
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

            Intent intent = new Intent(StallActivity.this, ProductActivity.class);
            startActivity(intent);

        }
    }
}
