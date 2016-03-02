package com.fiona.tiaozao;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 自定义适配器
 * Created by fiona on 16-2-25.
 */
public class MyGridViewAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;

    ArrayList<String> title;
    ArrayList<Integer> images;
    ArrayList<Integer> imagesSelect;

    int current = 0;      //  选中的tab

    public MyGridViewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);

        title = new ArrayList<>();
        title.add("首页");
        title.add("分类");
        title.add("发现");
        title.add("我");

        images = new ArrayList<>();
        images.add(R.drawable.tab_home);
        images.add(R.drawable.tab_classify);
        images.add(R.drawable.tab_discover);
        images.add(R.drawable.tab_mine);

        imagesSelect=new ArrayList<>();
        imagesSelect.add(R.drawable.tab_home_select);
        imagesSelect.add(R.drawable.tab_classify_select);
        imagesSelect.add(R.drawable.tab_discover_select);
        imagesSelect.add(R.drawable.tab_mine_select);
    }

    @Override
    public int getCount() {
        return 4;
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
        convertView = inflater.inflate(R.layout.list_gridview_main, parent, false);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView_grid_view);
        TextView textView = (TextView) convertView.findViewById(R.id.textView_grid_view);

        if (position == current) {
            imageView.setImageResource(imagesSelect.get(position));
            textView.setTextColor(Color.rgb(0xea,0x80,0x10));
        } else {
            imageView.setImageResource(images.get(position));
        }
        textView.setText(title.get(position));

        return convertView;
    }

    /**
     * 选中tab
     * @param position
     */
    public void setCurrent(int position){
        current=position;
        notifyDataSetChanged();     //通知数据改变
    }

}
