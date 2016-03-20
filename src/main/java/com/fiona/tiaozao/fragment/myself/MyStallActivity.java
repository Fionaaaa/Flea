package com.fiona.tiaozao.fragment.myself;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fiona.tiaozao.App;
import com.fiona.tiaozao.ProductActivity;
import com.fiona.tiaozao.R;
import com.fiona.tiaozao.bean.Goods;
import com.fiona.tiaozao.bean.User;
import com.fiona.tiaozao.fragment.myself.edit.DeleteView;
import com.fiona.tiaozao.fragment.myself.edit.AddProductView;
import com.fiona.tiaozao.fragment.myself.edit.SureCircleView;
import com.fiona.tiaozao.interactor.Interactor;

import java.util.ArrayList;
import java.util.List;

public class MyStallActivity extends AppCompatActivity {

    ListView listView;
    ListViewAdapter adapter;

    private String userID;

    ArrayList<Goods> data = new ArrayList<>();
    DeleteView deleteView;
    float y;
    boolean isSelectMode = false;
    TextView editText;

    int[] listSelectec;
    ProgressBar progressBar;

    AddProductView addProductView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stall);
        deleteView = (DeleteView) findViewById(R.id.deleteView_product);
        editText = (TextView) findViewById(R.id.text_my_stall);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        addProductView = (AddProductView) findViewById(R.id.newNoteView_stall);

        String account = getSharedPreferences("user", MODE_PRIVATE).getString("account", "000");
        List<User> userList = User.find(User.class, "account=?", account);
        if (userList.size() > 0) {
            userID = String.valueOf(userList.get(0).getId());
        }

        listView = (ListView) findViewById(R.id.listView_my_stall_activity);

        adapter = new ListViewAdapter(this, data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new ListViewListener());

        new SetDataSource().execute(userID);
    }

    /**
     * 点击结束活动
     *
     * @param view
     */
    public void returnMyStallActivity(View view) {
        finish();
    }

    /**
     * listview的适配器
     */
    class ListViewAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        ArrayList<Goods> data = new ArrayList<>();

        public ListViewAdapter(Context context, ArrayList<Goods> listSale) {
            if (listSale != null) {
                data = listSale;
            }
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
                convertView = inflater.inflate(R.layout.list_my_collection_product, parent, false);
                holder = new Holder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            Goods goods = data.get(position);
            if (!new Interactor().onlyWifi(MyStallActivity.this)) {
                holder.imageView.setImageURI(Uri.parse(App.URL + goods.getPic_location()));
            }
            holder.tvTitle.setText(goods.getTitle());
            holder.tvPrice.setText(String.valueOf(goods.getPrice()) + "￥");
            holder.tvDescribe.setText(goods.getDescribe());

            if (isSelectMode) {
                holder.circleView.setVisibility(View.VISIBLE);  //可见
                if (listSelectec[position] == 1) {
                    holder.circleView.setStyle(SureCircleView.FILL);
                } else {
                    holder.circleView.setStyle(SureCircleView.STROKE);
                }
            } else {
                holder.circleView.setVisibility(View.INVISIBLE);
            }

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
            SureCircleView circleView;

            Holder(View view) {
                imageView = (SimpleDraweeView) view.findViewById(R.id.list_my_collection_product_picture);
                tvTitle = (TextView) view.findViewById(R.id.list_my_collection_product_title);
                tvPrice = (TextView) view.findViewById(R.id.list_my_collection_product_price);
                tvDescribe = (TextView) view.findViewById(R.id.list_my_collection_product_describe);
                circleView = (SureCircleView) view.findViewById(R.id.sureCirleView_product);
            }
        }
    }

    /**
     * listView 的监听器
     */
    class ListViewListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (!isSelectMode) {
                Intent intent = new Intent(MyStallActivity.this, ProductActivity.class);
                intent.putExtra(App.ACTION_GOODS, data.get(position));
                intent.putExtra("fromFionaaaa",true);
                startActivity(intent);
            } else {
                if (listSelectec[position] != 0) {
                    listSelectec[position] = 0;
                } else {
                    listSelectec[position] = 1;
                }
                adapter.notifyDataSetChanged(); // 数据集改变
            }
        }
    }

    /**
     * 本地数据
     */

    private class SetDataSource extends AsyncTask<String, Void, ArrayList<Goods>> {

        @Override
        protected ArrayList<Goods> doInBackground(String... params) {
            String userID = params[0];
            ArrayList<Goods> goodsList = new ArrayList<>();
            if (userID != null) {
                goodsList = (ArrayList<Goods>) Goods.find(Goods.class, "user_id= ? and flag= ?", userID, "1");
            }
            return goodsList;
        }

        @Override
        protected void onPostExecute(ArrayList<Goods> list) {
            data.addAll(list);
            listSelectec = new int[data.size()];  //初始化选择记录
            adapter.notifyDataSetChanged();
        }
    }


    //点击编辑（取消）
    public void clickEdit(View view) {
        if (isSelectMode) {
            //点击取消
            editText.setText("编辑");
            clearSelect();              // 清空选择记录
            removeDelete();
            addProductView.setVisibility(View.VISIBLE);
        } else {
            editText.setText("取消");
            initDelete();
            addProductView.setVisibility(View.INVISIBLE);
        }

        isSelectMode = !isSelectMode;
        adapter.notifyDataSetChanged();
    }


    //删除按钮出现
    private void initDelete() {
        y = deleteView.getY();

        deleteView.setY(deleteView.getHeight() + y);
        deleteView.setVisibility(View.VISIBLE);

        new Thread() {
            @Override
            public void run() {
                while (deleteView.getY() > y) {
                    deleteView.post(new Runnable() {
                        @Override
                        public void run() {
                            deleteView.setY(deleteView.getY() - 20);
                        }
                    });
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (deleteView.getY() < y) {
                    deleteView.post(new Runnable() {
                        @Override
                        public void run() {
                            deleteView.setY(y);
                        }
                    });
                }
            }
        }.start();
    }

    //删除按钮消失
    private void removeDelete() {
        new Thread() {
            @Override
            public void run() {
                while (deleteView.getY() <= y + deleteView.getHeight()) {
                    deleteView.post(new Runnable() {
                        @Override
                        public void run() {
                            deleteView.setY(deleteView.getY() + 20);
                        }
                    });
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                deleteView.post(new Runnable() {
                    @Override
                    public void run() {
                        deleteView.setVisibility(View.INVISIBLE);//删除按钮不可见
                        deleteView.setY(y);
                    }
                });
            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isSelectMode) {
                clickEdit(null);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //删除
    public void doDelete(View view) {
        removeDelete();

        List<Goods> list = new ArrayList();

        for (int i = 0; i < data.size(); i++) {
            if (listSelectec[i] == 1) {
                list.add(data.get(i));
            }
        }

        if (list.size() > 0) {
            progressBar.setVisibility(View.VISIBLE);

            //删除内存
            deleteMemery();

            new AsyncTask<List, Void, Void>() {

                @Override
                protected Void doInBackground(List... params) {
                    List list = params[0];
                    //删除本地
                    Goods.deleteInTx(list);

                    //删除服务器
                    new Interactor().deleteGoods(MyStallActivity.this, list);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    //删除完成
                    progressBar.setVisibility(View.INVISIBLE);  //  进度条不可见
                }
            }.execute(list);
        }
        clickEdit(null);
    }

    //删除内存
    private void deleteMemery() {
        for (int i = data.size() - 1; i >= 0; i--) {
            if (listSelectec[i] == 1) {
                data.remove(i);
            }
        }
    }

    //清空选择记录
    private void clearSelect() {
        for (int i = 0; i < listSelectec.length; i++) {
            listSelectec[i] = 0;
        }
    }

}
