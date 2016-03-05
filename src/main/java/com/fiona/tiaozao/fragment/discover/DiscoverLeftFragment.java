package com.fiona.tiaozao.fragment.discover;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fiona.tiaozao.App;
import com.fiona.tiaozao.MainActivity;
import com.fiona.tiaozao.R;
import com.fiona.tiaozao.model.User;
import com.fiona.tiaozao.net.NetQuery;
import com.fiona.tiaozao.net.NetQueryImpl;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DiscoverLeftFragment extends Fragment {
    RecyclerView recyclerView;

    ArrayList<User> data=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getActivity());

        View view = inflater.inflate(R.layout.fragment_discover_left, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_discover_left);

        Handler handler=new MyHandler();
        NetQuery query=NetQueryImpl.getInstance(getActivity());
        query.getUsers(handler);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new RvAdapter(getActivity(), data));
    }

    /**
     * recycle适配器
     */
    public class RvAdapter extends RecyclerView.Adapter<RvAdapter.Holder> implements View.OnClickListener {

        Context context;
        ArrayList<User> data=new ArrayList<>();

        public RvAdapter(Context context, ArrayList<User> data) {
            this.context = context;
            this.data = data;
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
            holder.view.setId(position);

            /**
             * 从网络（缓存）取数据显示
             */
            User user = data.get(position);

            Uri uri=Uri.parse(App.URL+user.getIcon());
            holder.imageView.setImageURI(uri);

            holder.textViewSale.setText(user.getName());

            holder.textViewDescription.setText("描述:"+user.getDescribe());
        }

        @Override
        public int getItemCount() {
            return data.size();
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
            intent.putExtra(App.ACTION_USER, data.get(v.getId()));
            startActivity(intent);
        }

        /**
         * Holder类
         */
        class Holder extends RecyclerView.ViewHolder {

            public SimpleDraweeView imageView;
            public TextView textViewSale;
            public TextView textViewDescription;
            public TextView textViewTime;

            View view;

            public Holder(View view) {
                super(view);
                this.view = view;

                imageView = (SimpleDraweeView) view.findViewById(R.id.imageView_discover_left);
                textViewSale = (TextView) view.findViewById(R.id.textView_discover_left_author);
                textViewDescription = (TextView) view.findViewById(R.id.textView_discover_left_description);
                textViewTime = (TextView) view.findViewById(R.id.textView_discover_left_time);
            }
        }
    }

    /**
     * 请求网络数据
     */
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            data= (ArrayList<User>) msg.obj;
            recyclerView.setAdapter(new RvAdapter(getActivity(),data));
        }
    }
}
