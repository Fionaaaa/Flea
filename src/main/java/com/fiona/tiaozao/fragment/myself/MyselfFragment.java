package com.fiona.tiaozao.fragment.myself;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fiona.tiaozao.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyselfFragment extends Fragment {
    SimpleDraweeView draweeView;
    TextView textViewName;
    TextView textViewDescribe;
    TextView textViewLogin;
    RelativeLayout relativeLayout2;
    TextView textViewSale;
    RelativeLayout relativeLayout3;
    TextView textViewEmption;
    RelativeLayout relativeLayout4;
    TextView textViewCollection;


    public MyselfFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View view = inflater.inflate(R.layout.fragment_myself, container, false);

        draweeView = (SimpleDraweeView) view.findViewById(R.id.imageView_myself);
        textViewName = (TextView) view.findViewById(R.id.textView_myself_account_name);
        textViewDescribe = (TextView) view.findViewById(R.id.textView_myself_email);
        textViewLogin = (TextView) view.findViewById(R.id.textView2_click_login);
        relativeLayout2 = (RelativeLayout) view.findViewById(R.id.relativeLayout_myself_2);
        relativeLayout3 = (RelativeLayout) view.findViewById(R.id.relativeLayout_myself_3);
        relativeLayout4 = (RelativeLayout) view.findViewById(R.id.relativeLayout_myself_4);
        textViewSale = (TextView) view.findViewById(R.id.textView_myself_sale);
        textViewEmption = (TextView) view.findViewById(R.id.textView2_myself_emption);
        textViewCollection = (TextView) view.findViewById(R.id.textView3_myself_collection);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        //如果用户已登陆
        SharedPreferences pf = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
        if (pf.getBoolean("isLoad", false)) {
            //已经登陆
            String name = pf.getString("name", null);
            String icon = pf.getString("icon", null);

            draweeView.setImageURI(Uri.parse(icon));
            textViewName.setText(name);

            textViewName.setVisibility(View.VISIBLE);
            textViewDescribe.setVisibility(View.VISIBLE);
            textViewLogin.setVisibility(View.INVISIBLE);

            relativeLayout2.setEnabled(true);
            relativeLayout3.setEnabled(true);
            relativeLayout4.setEnabled(true);

            textViewSale.setTextColor(Color.rgb(0x80, 0x80, 0x80));
            textViewEmption.setTextColor(Color.rgb(0x80, 0x80, 0x80));
            textViewCollection.setTextColor(Color.rgb(0x80, 0x80, 0x80));

        } else {
            //未登录
            textViewName.setText("点击登陆");
            textViewName.setVisibility(View.INVISIBLE);
            textViewDescribe.setVisibility(View.INVISIBLE);
            textViewLogin.setVisibility(View.VISIBLE);

            relativeLayout2.setEnabled(false);
            relativeLayout3.setEnabled(false);
            relativeLayout4.setEnabled(false);

            textViewSale.setTextColor(Color.rgb(0xBF, 0xBF, 0xBF));
            textViewEmption.setTextColor(Color.rgb(0xBF, 0xBF, 0xBF));
            textViewCollection.setTextColor(Color.rgb(0xBF, 0xBF, 0xBF));
        }
    }

}
