package com.nyq.projecttreasure.activitys.youyaadapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liufx.delegationadapter.DelegationAdapter;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.base.BaseActivity;
import com.nyq.projecttreasure.models.HealthInfo;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @package: com.nyq.projecttreasure.activitys.youyaadapter
 * @author: niuyq
 * @date: 2019/2/14
 * Copyright © 2019 某某某公司. All rights reserved.
 * @description: <使得RecyclerView各种情况的多类型条目更简单>
 */

public class DelegationAdapterActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.menu)
    TextView menu;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.rv_list)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private DelegationAdapter mDelegationAdapter;
    private HeardAdapter heardAdapter;
    private MiddleAdapter middleAdapter;
    private List<HealthInfo> healthInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegation_adapter);
        ButterKnife.bind(this);
        setStatusBarColor(R.color.colorPrimary);
        initView();
        getInfoList();
    }

    public void initView() {
        back.setVisibility(View.VISIBLE);
        title.setText("RecyclerView多类型item");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                activity.overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
            }
        });

        mDelegationAdapter = new DelegationAdapter();
        heardAdapter = new HeardAdapter();
        middleAdapter = new MiddleAdapter(activity);
        mDelegationAdapter.addDelegate(heardAdapter);
        mDelegationAdapter.addDelegate(middleAdapter);
        recyclerView.setAdapter(mDelegationAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        DividerItemDecoration divider = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(activity, R.drawable.custom_divider_6));
        recyclerView.addItemDecoration(divider);
        // 是否开启下拉刷新功能（默认true）
        refreshLayout.setEnableRefresh(true);

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
            }
        });
    }

    /**
     * 从网络获取数据
     */
    private void getInfoList() {
        PeopleInfoListVo vo = new PeopleInfoListVo();
        vo.setUserName("张飞");
        vo.setFinishTime("2019-02-14");
        vo.setScore(100);
        healthInfos = new ArrayList<>();
        healthInfos.add(new HealthInfo("Activity沉浸式", new Date().toString(), "Activity内容入嵌沉浸式", "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1607779262,1632598626&fm=26&gp=0.jpg"));
        healthInfos.add(new HealthInfo("默认主题", new Date().toString(), getResources().getString(R.string.item_style_theme_default_abstract), "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=4017085655,264997454&fm=26&gp=0.jpg"));
        healthInfos.add(new HealthInfo("橙色主题", new Date().toString(), getResources().getString(R.string.item_style_theme_orange_abstract), "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3182596561,138363622&fm=26&gp=0.jpg"));
        healthInfos.add(new HealthInfo("红色主题", new Date().toString(), getResources().getString(R.string.item_style_theme_red_abstract), "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=659044909,4042106041&fm=26&gp=0.jpg"));
        healthInfos.add(new HealthInfo("绿色主题", new Date().toString(), getResources().getString(R.string.item_style_theme_green_abstract), "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=4111176407,2620705746&fm=26&gp=0.jpg"));
        healthInfos.add(new HealthInfo("蓝色主题", new Date().toString(), getResources().getString(R.string.item_style_theme_blue_abstract), "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2739505509,237691169&fm=26&gp=0.jpg"));
        // 设置数据
        mDelegationAdapter.setHeaderItem(vo);
        mDelegationAdapter.setDataItems(healthInfos);
    }

    /**
     * 为状态栏着色
     * @param colorRes
     */
    public void setStatusBarColor(int colorRes) {
        toolbar.setBackgroundResource(colorRes);
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(colorRes);
    }

}
