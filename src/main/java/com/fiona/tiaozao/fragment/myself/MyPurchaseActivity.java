package com.fiona.tiaozao.fragment.myself;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fiona.tiaozao.App;
import com.fiona.tiaozao.R;
import com.fiona.tiaozao.bean.User;
import com.fiona.tiaozao.fragment.discover.PurchaseActivity;
import com.fiona.tiaozao.bean.Goods;
import com.fiona.tiaozao.fragment.myself.edit.AddEmptionView;
import com.fiona.tiaozao.fragment.myself.edit.DeleteView;
import com.fiona.tiaozao.fragment.myself.edit.SureCircleView;
import com.fiona.tiaozao.interactor.Interactor;
import com.fiona.tiaozao.net.NetQuery;
import com.fiona.tiaozao.net.NetQueryImpl;

import java.util.ArrayList;
import java.util.List;

public class MyPurchaseActivity extends AppCompatActivity {

    ListView listView;

    private String userID;

    ArrayList<Goods> data = new ArrayList<>();
    TextView textViewEdit;

    ListViewAdapter adapter;

    boolean isSelectMode = false;   //选择模式
    DeleteView deleteView;
    float y;

    int[] listSelectec;
    ProgressBar progressBar;
    AddEmptionView addEmptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchase);

        textViewEdit = (TextView) findViewById(R.id.my_collection_emption_edit);
        deleteView = (DeleteView) findViewById(R.id.deleteView_emption);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        addEmptionView= (AddEmptionView) findViewById(R.id.newNoteView_emption);

        userID = new Interactor().getId(this);

        listView = (ListView) findViewById(R.id.listView_my_purchase);

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

            //选择模式
            if (isSelectMode) {
                holder.tvTime.setVisibility(View.INVISIBLE);
                holder.sureCircleView.setVisibility(View.VISIBLE);
                if (listSelectec[position] == 1) {
                    holder.sureCircleView.setStyle(SureCircleView.FILL);
                } else {
                    holder.sureCircleView.setStyle(SureCircleView.STROKE);
                }
            } else {
                holder.tvTime.setVisibility(View.VISIBLE);
                holder.sureCircleView.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }

        /**
         * Holder类
         */
        class Holder {
            TextView tvClassify;
            TextView tvDescribe;
            TextView tvTime;
            SureCircleView sureCircleView;

            public Holder(View view) {
                tvClassify = (TextView) view.findViewById(R.id.textView32_my_emption_classify);
                tvDescribe = (TextView) view.findViewById(R.id.textView33_my_emption_describe);
                tvTime = (TextView) view.findViewById(R.id.textView34_my_emption_time);
                sureCircleView = (SureCircleView) view.findViewById(R.id.sureCirleView_emption);
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
                Intent intent = new Intent(MyPurchaseActivity.this, PurchaseActivity.class);
                intent.putExtra(App.ACTION_GOODS, data.get(position));
                intent.putExtra("from","mine");
                startActivity(intent);
            } else {
                if (listSelectec[position] == 1) {
                    listSelectec[position] = 0;
                } else {
                    listSelectec[position] = 1;
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 读取本地 数据
     */
    private class SetDataSource extends AsyncTask<String, Void, ArrayList<Goods>> {

        @Override
        protected ArrayList<Goods> doInBackground(String... params) {
            String userID = params[0];
            ArrayList<Goods> goodsList = (ArrayList<Goods>) Goods.find(Goods.class, "user_id= ? and flag= ?", userID, "0");
            return goodsList;
        }

        @Override
        protected void onPostExecute(ArrayList<Goods> list) {
            data.addAll(list);
            listSelectec = new int[data.size()];  // 初始化选择列表
            adapter.notifyDataSetChanged();
        }
    }

    //点击编辑
    public void onClickEdit(View view) {
        if (isSelectMode) {
            //点击取消
            textViewEdit.setText("编辑");
            clearSelect();                  //清空选择
            removeDelete();
            addEmptionView.setVisibility(View.VISIBLE);
        } else {
            textViewEdit.setText("取消");
            initDelete();
            addEmptionView.setVisibility(View.INVISIBLE);
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
                removeDelete();
                onClickEdit(null);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //点击删除
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
                    new Interactor().deleteGoods(MyPurchaseActivity.this, list);

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
        onClickEdit(null);
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
