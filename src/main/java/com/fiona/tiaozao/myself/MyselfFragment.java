package com.fiona.tiaozao.myself;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiona.tiaozao.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyselfFragment extends Fragment {


    public MyselfFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_myself, container, false);
        return view;
    }

}
