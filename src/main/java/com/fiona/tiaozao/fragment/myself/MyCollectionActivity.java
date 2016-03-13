package com.fiona.tiaozao.fragment.myself;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fiona.tiaozao.App;
import com.fiona.tiaozao.ProductActivity;
import com.fiona.tiaozao.R;
import com.fiona.tiaozao.bean.Goods;
import com.fiona.tiaozao.bean.User;
import com.fiona.tiaozao.fragment.discover.StallActivity;
import com.fiona.tiaozao.interactor.Interactor;

import java.util.ArrayList;

public class MyCollectionActivity extends AppCompatActivity {

    MyListView listView;
    String userId;
    ArrayList<User> userList;
    ArrayList<Goods> goodsList;

    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);

        listView = (MyListView) findViewById(R.id.myListView);

        listView.setOnItemClickListener(new MyListener());

        //从本地取数据
        userId = Interactor.getId(this);

        // TODO: 16-3-8 获得收藏的用户和物品
        initView();

    }

    private void initView() {
        userList = Interactor.getCollectUser(this);
        goodsList = Interactor.getCollectGoods(this);

        adapter = new MyAdapter(this, userList, goodsList);
        listView.setAdapter(adapter);
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
        ArrayList<User> userList;
        ArrayList<Goods> goodsList;

        public MyAdapter(Context context, ArrayList<User> userList, ArrayList<Goods> goodsList) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.userList = userList;
            this.goodsList = goodsList;
        }

        @Override
        public int getCount() {
            return userList.size() + goodsList.size() + 2;
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
            HolderGoods holderGoods;
            HolderUser holderUser;
            if (position == 0) {
                convertView = inflater.inflate(R.layout.item_my_collection, parent, false);
                TextView textView = (TextView) convertView.findViewById(R.id.textView_my_collection_01);
                if (goodsList.size() == 0 && userList.size() == 0) {
                    textView.setText("无");
                }
                if (userList.size() == 0 && goodsList.size() > 0) {
                    textView.setVisibility(View.INVISIBLE);
                }

            } else if (position > 0 && position <= userList.size()) {

                convertView = inflater.inflate(R.layout.list_discover_sale, parent, false);
                convertView.findViewById(R.id.view_line).setVisibility(View.INVISIBLE);
                convertView.findViewById(R.id.textView_discover_left_time).setVisibility(View.INVISIBLE);
                holderUser = new HolderUser(convertView);

                if (!Interactor.onlyWifi(MyCollectionActivity.this)) {
                    holderUser.imageView.setImageURI(Uri.parse(userList.get(position - 1).getIcon()));
                }else{
                    holderUser.imageView.setImageURI(Uri.parse(App.DEFAULT_PIC));
                }
                holderUser.textViewSale.setText(userList.get(position - 1).getName());
                holderUser.textViewDescription.setText(userList.get(position - 1).getDescribe());

            } else if (position == userList.size() + 1) {

                convertView = inflater.inflate(R.layout.item_my_collection, parent, false);
                TextView textView = (TextView) convertView.findViewById(R.id.textView_my_collection_01);
                textView.setText("商品");
                if (goodsList.size() == 0) {
                    textView.setVisibility(View.INVISIBLE);
                }

            } else {

                convertView = inflater.inflate(R.layout.list_listview_product, parent, false);
                holderGoods = new HolderGoods(convertView);

                if (!Interactor.onlyWifi(MyCollectionActivity.this)) {
                    holderGoods.imageView.setImageURI(Uri.parse(App.URL + goodsList.get(position - userList.size() - 2).getPic_location()));
                }
                holderGoods.textViewTitle.setText(goodsList.get(position - userList.size() - 2).getTitle());
                holderGoods.textViewPrice.setText(String.valueOf(goodsList.get(position - userList.size() - 2).getPrice()));
                holderGoods.textViewDescribe.setText(goodsList.get(position - userList.size() - 2).getDescribe());

            }
            return convertView;
        }

        /**
         * Holder:GOODS
         */
        class HolderGoods {
            SimpleDraweeView imageView;
            TextView textViewTitle;
            TextView textViewPrice;
            TextView textViewDescribe;

            HolderGoods(View view) {
                imageView = (SimpleDraweeView) view.findViewById(R.id.textView30_classify_product_picture);
                textViewTitle = (TextView) view.findViewById(R.id.textView30_classify_product_title);
                textViewPrice = (TextView) view.findViewById(R.id.textView30_classify_product_price);
                textViewDescribe = (TextView) view.findViewById(R.id.textView31_classify_product_describe);
            }
        }

        /**
         * Holder:USER
         */
        class HolderUser {
            public SimpleDraweeView imageView;
            public TextView textViewSale;
            public TextView textViewDescription;
            public TextView textViewTime;

            public HolderUser(View view) {
                imageView = (SimpleDraweeView) view.findViewById(R.id.imageView_discover_left);
                textViewSale = (TextView) view.findViewById(R.id.textView_discover_left_author);
                textViewDescription = (TextView) view.findViewById(R.id.textView_discover_left_description);
                textViewTime = (TextView) view.findViewById(R.id.textView_discover_left_time);
            }
        }
    }

    /**
     * 监听器
     */
    class MyListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0 && position <= userList.size()) {
                userList = Interactor.getGoodsToUser(userList);
                Log.d("debug","userlist:"+userList.size());
                Intent intent = new Intent(MyCollectionActivity.this, StallActivity.class);
                intent.putExtra(App.ACTION_USER, userList.get(position - 1));
                startActivityForResult(intent, 8080);
            }
            if (position > userList.size() + 1) {
                Intent intent = new Intent(MyCollectionActivity.this, ProductActivity.class);
                intent.putExtra(App.ACTION_GOODS, goodsList.get(position - userList.size() - 2));
                startActivityForResult(intent, 8080);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 8080) {
            initView();
        }
    }
}
