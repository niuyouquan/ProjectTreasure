package com.nyq.projecttreasure.activitys.baidumap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.adapter.AppAdapter;
import com.nyq.projecttreasure.adapter.BaiDuAboutSearchAdapter;
import com.nyq.projecttreasure.base.BaseActivity;
import com.nyq.projecttreasure.models.AppInfo;
import com.nyq.projecttreasure.models.HealthInfo;
import com.nyq.projecttreasure.utils.AGCache;
import com.nyq.projecttreasure.utils.ColorUtil;
import com.nyq.projecttreasure.utils.DensityUtil;
import com.nyq.projecttreasure.utils.StatusBarUtil;
import com.nyq.projecttreasure.views.FixedGridView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaiDuAboutSearchActivity extends BaseActivity {

    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.tv_address_left)
    TextView tvAddressLeft;
    @BindView(R.id.top_left)
    LinearLayout topLeft;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_middle)
    LinearLayout topMiddle;
    @BindView(R.id.toolbar)
    LinearLayout toolbar;
    @BindView(R.id.toobar_layout)
    LinearLayout toobarLayout;
    @BindView(R.id.recyclerView)
    LRecyclerView mRecyclerView;

    private LinearLayout head_img;
    private FixedGridView fixedGridView;
    private CardView cardView;

    private BaiDuAboutSearchAdapter mAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private AppAdapter appAdapter;
    private List<AppInfo> appList;
    private List<HealthInfo> healthInfosAll = new ArrayList<>();
    private boolean isScrollIdle = true; // recycleView是否在滑动
    private int adViewTopSpace; // 广告视图距离顶部的距离
    private int adViewHeight = 180; // 广告视图的高度
    private int titleViewHeight = 40; // 标题栏的高度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_du_about_search);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, toolbar);
        topTitle.setText(AGCache.CITY_NAME);
        tvAddressLeft.setText(AGCache.CITY_NAME);
        topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                activity.overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
            }
        });

        mAdapter = new BaiDuAboutSearchAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.colorAccent, R.color.colorAccent, android.R.color.white);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadMoreEnabled(true);

        //        //添加Header
        View header = LayoutInflater.from(activity).inflate(R.layout.about_search_heard_v, mRecyclerView, false);
        mLRecyclerViewAdapter.addHeaderView(header);
        head_img = header.findViewById(R.id.head_img);
        fixedGridView = header.findViewById(R.id.fixedGridView);
        cardView = header.findViewById(R.id.cd_about_search);

        appAdapter = new AppAdapter(activity, getGridData());
        fixedGridView.setAdapter(appAdapter);

        fixedGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity,BaiDuAboutSearchMapActivity.class);
                intent.putExtra("keyWords",appAdapter.getData().get(position).getName());
                startActivity(intent);
                activity.overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getInfoList();
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isScrollIdle = (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isScrollIdle && adViewTopSpace < 0) return;
                adViewTopSpace = DensityUtil.px2dip(activity, header.getTop());
                adViewHeight = DensityUtil.px2dip(activity, head_img.getHeight());
                if (-adViewTopSpace <= 50) {
                    topMiddle.setVisibility(View.INVISIBLE);
                    tvAddressLeft.setVisibility(View.VISIBLE);
                } else {
                    topMiddle.setVisibility(View.VISIBLE);
                    tvAddressLeft.setVisibility(View.INVISIBLE);
                }
                handleTitleBarColorEvaluate();
            }
        });

        getInfoList();
    }

    public List<AppInfo> getGridData() {
        appList = new ArrayList<>();
        appList.add(new AppInfo("美食", R.mipmap.image_practice_repast_1));
        appList.add(new AppInfo("酒店", R.mipmap.image_practice_repast_2));
        appList.add(new AppInfo("银行", R.mipmap.image_practice_repast_3));
        appList.add(new AppInfo("网吧", R.mipmap.image_practice_repast_4));
        appList.add(new AppInfo("超市", R.mipmap.image_practice_repast_1));
        appList.add(new AppInfo("公交站", R.mipmap.image_practice_repast_2));
        appList.add(new AppInfo("厕所", R.mipmap.image_practice_repast_3));
        appList.add(new AppInfo("景点", R.mipmap.image_practice_repast_4));
        appList.add(new AppInfo("更多", R.mipmap.image_practice_repast_1));
        return appList;
    }

    //从网络获取数据
    private void getInfoList() {
        List<HealthInfo> healthInfos;healthInfos = new ArrayList<>();
        healthInfos.add(new HealthInfo("Activity沉浸式", new Date().toString(),"Activity内容入嵌沉浸式","https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1607779262,1632598626&fm=26&gp=0.jpg"));
        healthInfos.add(new HealthInfo("默认主题", new Date().toString(),getResources().getString(R.string.item_style_theme_default_abstract),"https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=4017085655,264997454&fm=26&gp=0.jpg"));
        healthInfos.add(new HealthInfo("橙色主题", new Date().toString(),getResources().getString(R.string.item_style_theme_orange_abstract),"https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3182596561,138363622&fm=26&gp=0.jpg"));
        healthInfos.add(new HealthInfo("红色主题", new Date().toString(),getResources().getString(R.string.item_style_theme_red_abstract),"https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=659044909,4042106041&fm=26&gp=0.jpg"));
        healthInfos.add(new HealthInfo("绿色主题", new Date().toString(),getResources().getString(R.string.item_style_theme_green_abstract),"https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=4111176407,2620705746&fm=26&gp=0.jpg"));
        healthInfos.add(new HealthInfo("蓝色主题", new Date().toString(),getResources().getString(R.string.item_style_theme_blue_abstract),"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2739505509,237691169&fm=26&gp=0.jpg"));
        healthInfosAll.addAll(healthInfos);
        mAdapter.addAll(healthInfos);
        mRecyclerView.refreshComplete(6);
        if (healthInfosAll.size() >= 18) {
            mRecyclerView.setNoMore(true);
        } else {
            mRecyclerView.setNoMore(false);
        }
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    // 处理标题栏颜色渐变
    private void handleTitleBarColorEvaluate() {
        float fraction;
        toolbar.setAlpha(1f);
        if (adViewTopSpace > 5) {
            fraction = 1f - adViewTopSpace * 1f / 60;
            if (fraction < 0f) fraction = 0f;
            toolbar.setAlpha(fraction);
            return;
        }
        float space = Math.abs(adViewTopSpace) * 1f;
        fraction = space / (adViewHeight - titleViewHeight);
        if (fraction < 0f) fraction = 0f;
        if (fraction > 1f) fraction = 1f;
        toolbar.setAlpha(1f);
        if (fraction >= 1f) {
            fakeStatusBar.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
            toolbar.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
        } else {
            fakeStatusBar.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(activity, fraction, R.color.transparent, R.color.colorPrimary));
            toolbar.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(activity, fraction, R.color.transparent, R.color.colorPrimary));
        }
    }
}
