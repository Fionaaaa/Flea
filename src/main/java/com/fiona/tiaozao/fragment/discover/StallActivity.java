package com.fiona.tiaozao.fragment.discover;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.fiona.tiaozao.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StallActivity extends AppCompatActivity {
    ListView listView;

    ArrayList<Goods> data=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stall);

        User user= (User) getIntent().getExtras().get(App.ACTION_USER);
        data=user.getListSale();

        listView = (ListView) findViewById(R.id.listView_discover_stall_activity);

        listView.setAdapter(new ListViewAdapter(this, data));

        listView.setOnItemClickListener(new ListViewListener());
    }

    public void clickBackStallActivity(View view) {
        finish();
    }

    /**
     * listView的适配器
     */
    class ListViewAdapter extends BaseAdapter {

        ArrayList<Goods> data=new ArrayList<>();

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
            return data.get(position).getId();
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

            Goods goods=data.get(position);
            Picasso.with(context).load(App.URL+goods.getPic_location()).into(holder.imageView);
            holder.tvTitle.setText(goods.getTitle());
            holder.tvPrice.setText(String.valueOf(goods.getPrice())+"￥");
            holder.tvDescribe.setText(goods.getDescribe());

            return convertView;
        }

        /**
         * Holder类
         */
        class Holder {
            ImageView imageView;
            TextView tvTitle;
            TextView tvPrice;
            TextView tvDescribe;

            Holder(View view) {
                imageView = (ImageView) view.findViewById(R.id.imageView8_classify_product_pic);
                tvTitle = (TextView) view.findViewById(R.id.textView22_classify_product_title);
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

            Intent intent = new Intent(StallActivity.this, ProductActivity.class);
            intent.putExtra(App.ACTION_GOODS,data.get(position));
            startActivity(intent);
        }
    }
}
