package com.fiona.tiaozao.discover;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiona.tiaozao.MainActivity;
import com.fiona.tiaozao.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverRightFragment extends Fragment {


    public DiscoverRightFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover_right, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_discover_right);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new Rvadapter(getActivity()));
        return view;
    }

    /**
     * recycle适配器
     */
    private class Rvadapter extends RecyclerView.Adapter<Holder> implements View.OnClickListener {
        Context context;

        public Rvadapter(Context context) {
            this.context = context;
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
             * 加载网络（缓存）数据
             */
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        @Override
        public void onClick(View v) {

            ((MainActivity) getActivity()).clickChangeBackgroundColor(v);

            Intent intent = new Intent(getActivity(), PurchaseActivity.class);
            startActivity(intent);
        }
    }

    /**
     * holder类
     */
    private class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewClassify;
        TextView textViewDescription;
        TextView textViewAuthor;
        TextView textViewTime;

        View view;

        public Holder(View v) {
            super(v);
            this.view = v;

            imageView = (ImageView) v.findViewById(R.id.imageView_discover_right);
            textViewClassify = (TextView) v.findViewById(R.id.textView_discover_classify);
            textViewDescription = (TextView) v.findViewById(R.id.textView_discover_theme);
            textViewAuthor = (TextView) v.findViewById(R.id.textView_discover_author);
            textViewTime = (TextView) v.findViewById(R.id.textView_discover_right_time);
        }
    }

}
