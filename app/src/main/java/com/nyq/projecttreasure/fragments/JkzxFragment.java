package com.nyq.projecttreasure.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.activitys.main.ChenjinshiActivity;
import com.nyq.projecttreasure.models.HealthInfo;
import com.nyq.projecttreasure.utils.LogUtil;
import com.nyq.projecttreasure.utils.StringHelper;
import com.nyq.projecttreasure.utils.TimeHelper;
import com.nyq.projecttreasure.views.DividerItemDecoration;
import com.nyq.projecttreasure.views.MLImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author 健康资讯fragment
 * @time 2016-07-18 16:49
 */

// JUMP 
@SuppressLint("ValidFragment")
public class JkzxFragment extends Fragment {
    RecyclerView mRecyclerView;
    private static int pageSize = 10;
    private int pageNum = 1;
    JkzxAdapter mAdapter;
    private static final String COLUMN_CODE = "column_code";
    private static final String COLUMN_NAME = "column_name";
    private static final String COLUMN_re= "refreshLayout";

    private List<HealthInfo> healthInfos;
    private List<HealthInfo> healthInfosMore = new ArrayList<>();
    private String columnCode;
    private String columnName;
    String name;

    public static JkzxFragment newInstance(String columnCode, String columnName) {
        Bundle args = new Bundle();
        args.putString(COLUMN_CODE, columnCode);
        args.putString(COLUMN_NAME, columnName);
        JkzxFragment tripFragment = new JkzxFragment();
        tripFragment.setArguments(args);
        return tripFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        healthInfosMore.clear();
        getInfoList();
        columnCode = getArguments().getString(COLUMN_CODE);
        columnName = getArguments().getString(COLUMN_NAME);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jkzx, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new JkzxAdapter(getActivity(), R.layout.item_listview_jkzx, healthInfos);
//        //打开或关闭加载
        mAdapter.setEnableLoadMore(true);
        mAdapter.openLoadAnimation();
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mAdapter.getData().size() > 10) {
                    Toast.makeText(getContext(), "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                    mAdapter.setEnableLoadMore(false);
                } else {
                    getInfoList();
                    mAdapter.addData(healthInfos);
                    mAdapter.setEnableLoadMore(true);
                    mAdapter.loadMoreComplete();
                }
            }
        }, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);//设置adapter
        mAdapter.replaceData(healthInfos);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e("position：",position+"");
                Toast.makeText(getContext(), columnName+"    "+healthInfosMore.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                if (position == 5){
                    startActivity(new Intent(getContext(), ChenjinshiActivity.class));
                }
            }
        });

        return view;

    }

    //从网络获取数据
    private void getInfoList() {
        healthInfos = new ArrayList<>();
        healthInfos.add(new HealthInfo("默认主题", new Date().toString(),getResources().getString(R.string.item_style_theme_default_abstract),"http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfos.add(new HealthInfo("橙色主题", new Date().toString(),getResources().getString(R.string.item_style_theme_orange_abstract),"http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfos.add(new HealthInfo("红色主题", new Date().toString(),getResources().getString(R.string.item_style_theme_red_abstract),"http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfos.add(new HealthInfo("绿色主题", new Date().toString(),getResources().getString(R.string.item_style_theme_green_abstract),"http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfos.add(new HealthInfo("蓝色主题", new Date().toString(),getResources().getString(R.string.item_style_theme_blue_abstract),"http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfos.add(new HealthInfo("Activity沉浸式", new Date().toString(),getResources().getString(R.string.item_style_theme_blue_abstract),"http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfosMore.addAll(healthInfos);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * adapter
     */
    class JkzxAdapter extends BaseQuickAdapter<HealthInfo, BaseViewHolder> {
        Context context;
        String code;

        public JkzxAdapter(Context context, int layoutId, List<HealthInfo> str) {
            super(layoutId, str);
            this.context = context;
        }

        @Override
        public void convert(BaseViewHolder viewHolder, HealthInfo healthInfo) {
            viewHolder.setText(R.id.item_tv1, healthInfo.getTitle());
            viewHolder.setText(R.id.msg_time, TimeHelper.formatStringToDate(new Date().toString(), "yyyy-M-d"));
            viewHolder.setText(R.id.source, StringHelper.isBlank(healthInfo.getSource()) ? "未知" : healthInfo.getSource());
            Glide.with(mContext).load(healthInfo.getImg()).into((MLImageView) viewHolder.getView(R.id.item_image));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
