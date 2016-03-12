package com.fiona.tiaozao.fragment.discover;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fiona.tiaozao.App;
import com.fiona.tiaozao.MainActivity;
import com.fiona.tiaozao.R;
import com.fiona.tiaozao.bean.Goods;
import com.fiona.tiaozao.interactor.Interactor;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverRightFragment extends Fragment {

    ArrayList<Goods> data = new ArrayList<>();

    RecyclerView recyclerView;

//    Handler handler;

    public DiscoverRightFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover_right, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_discover_right);

        initView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    void initView() {

        new SetDataSource().execute();

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new Rvadapter(getActivity(), data));
    }

    /**
     * recycle适配器
     */
    private class Rvadapter extends RecyclerView.Adapter<Holder> implements View.OnClickListener {
        Context context;
        ArrayList<Goods> data;

        public Rvadapter(Context context, ArrayList<Goods> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_discover_emption, parent, false);
            Holder holder = new Holder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {

            holder.view.setOnClickListener(this);

            /**
             * 加载网（缓存）数据
             */
            Goods goods = data.get(position);
            //用户头像
            String icon = Interactor.getIcon(goods.getGoods_id());
            if (icon != null) {
                if (!Interactor.onlyWifi(getActivity())) {
                    holder.imageView.setImageURI(Uri.parse(icon));
                }else{
                    holder.imageView.setImageURI(Uri.parse(App.DEFAULT_PIC));
                }
            }

            holder.textViewAuthor.setText(goods.getUserName());

            holder.textViewClassify.setText(goods.getClassify());
            holder.textViewDescription.setText(goods.getDescribe());
            holder.textViewTime.setText(goods.getTime());


            holder.view.setId(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public void onClick(View v) {

            ((MainActivity) getActivity()).clickChangeBackgroundColor(v);

            Intent intent = new Intent(getActivity(), PurchaseActivity.class);
            intent.putExtra(App.ACTION_GOODS, data.get(v.getId()));
            startActivity(intent);
        }
    }

    /**
     * holder类
     */
    private class Holder extends RecyclerView.ViewHolder {
        SimpleDraweeView imageView;
        TextView textViewClassify;
        TextView textViewDescription;
        TextView textViewAuthor;
        TextView textViewTime;

        View view;

        public Holder(View v) {
            super(v);
            this.view = v;

            imageView = (SimpleDraweeView) v.findViewById(R.id.dis_right_sd);
            textViewClassify = (TextView) v.findViewById(R.id.dis_right_classify);
            textViewDescription = (TextView) v.findViewById(R.id.dis_right_describe);
            textViewAuthor = (TextView) v.findViewById(R.id.dis_right_author);


            textViewTime = (TextView) v.findViewById(R.id.textView_purchase_time);
        }
    }

    /**
     * 本地缓存取数据
     */
    private class SetDataSource extends AsyncTask<Void, Void, ArrayList<Goods>> {

        @Override
        protected ArrayList<Goods> doInBackground(Void... params) {
            ArrayList<Goods> goodsList = (ArrayList<Goods>) Goods.find(Goods.class, "flag = ?", "0");

            return goodsList;
        }

        @Override
        protected void onPostExecute(ArrayList<Goods> data) {
            setAdapter(data);
        }
    }

    //设置适配器
    private void setAdapter(ArrayList<Goods> data) {
        recyclerView.setAdapter(new Rvadapter(getActivity(), data));
    }
}
