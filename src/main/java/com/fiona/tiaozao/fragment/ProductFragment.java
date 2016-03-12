package com.fiona.tiaozao.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fiona.tiaozao.App;
import com.fiona.tiaozao.R;
import com.fiona.tiaozao.bean.Goods;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment {

    private static final String GOODS = "goods";

    SimpleDraweeView sdView;
    TextView tvTitle;
    TextView tvPrice;
    TextView tvDescribe;
    TextView tvAuthor;
    TextView tvContact;

    public ProductFragment() {
        // Required empty public constructor
    }

    //获得片段实例
    public static ProductFragment getInstance(Goods goods) {
        ProductFragment fragment = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GOODS, goods);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        Goods goods= (Goods) getArguments().getSerializable(GOODS);

        sdView= (SimpleDraweeView) view.findViewById(R.id.p2_image);
        tvTitle= (TextView) view.findViewById(R.id.p2_title);
        tvPrice= (TextView) view.findViewById(R.id.p2_price);
        tvDescribe= (TextView) view.findViewById(R.id.p2_describe);
        tvAuthor= (TextView) view.findViewById(R.id.p2_author);
        tvContact= (TextView) view.findViewById(R.id.p2_contact);

        if(!getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getBoolean("wifi",false)){
            sdView.setImageURI(Uri.parse(App.URL+goods.getPic_location()));
        }
        tvTitle.setText(goods.getTitle());
        tvPrice.setText(String.valueOf(goods.getPrice())+"￥");
        tvDescribe.setText(goods.getDescribe());
        tvAuthor.setText(goods.getUserName());
        tvContact.setText(goods.getContact());

        return view;
    }

}
