package com.fiona.tiaozao.fragment.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fiona.tiaozao.bean.Goods;

import java.util.ArrayList;

/**
 * Created by fiona on 16-3-13.
 */
public class ListViewAdapter extends BaseAdapter {

    ArrayList<Goods> data;
    LayoutInflater inflater;

    public ListViewAdapter(Context context,ArrayList<Goods> data) {
        this.data = data;
        inflater=LayoutInflater.from(context);
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
        return null;
    }
}
