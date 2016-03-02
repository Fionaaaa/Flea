package com.fiona.tiaozao.discover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiona.tiaozao.MainActivity;
import com.fiona.tiaozao.R;

public class DiscoverLeftFragment extends Fragment {
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover_left, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_discover_left);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new RvAdapter(getActivity()));
    }

    /**
     * recycle适配器
     */
    public class RvAdapter extends RecyclerView.Adapter<RvAdapter.Holder> implements View.OnClickListener {

        Context context;

        public RvAdapter(Context context) {
            this.context = context;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_discover_sale, parent, false);
            Holder holder = new Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {

            holder.view.setOnClickListener(this);

            /**
             * 从网络（缓存）取数据显示
             */
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        /**
         * 点击事件
         *
         * @param v
         */
        @Override
        public void onClick(View v) {

            ((MainActivity) getActivity()).clickChangeBackgroundColor(v);

            Intent intent = new Intent(getActivity(), StallActivity.class);
            startActivity(intent);
        }

        /**
         * Holder类
         */
        class Holder extends RecyclerView.ViewHolder {

            public ImageView imageView;
            public TextView textViewSale;
            public TextView textViewDescription;

            View view;

            public Holder(View view) {
                super(view);
                this.view = view;

                imageView = (ImageView) view.findViewById(R.id.imageView_discover_left);
                textViewSale = (TextView) view.findViewById(R.id.textView_saler);
                textViewDescription = (TextView) view.findViewById(R.id.textView_description_saler);
            }
        }
    }

    public void resume() {

    }
}
