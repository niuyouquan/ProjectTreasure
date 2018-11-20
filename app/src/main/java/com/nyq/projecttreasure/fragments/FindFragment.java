package com.nyq.projecttreasure.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.views.ObservableScrollView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 发现
 */
public class FindFragment extends Fragment {

    Unbinder unbinder;

    private int imageHeight;

    public static FindFragment newInstance() {
        FindFragment fragment = new FindFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    public void initView(View view) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

}
