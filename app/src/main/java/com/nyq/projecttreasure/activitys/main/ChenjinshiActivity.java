package com.nyq.projecttreasure.activitys.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nyq.projecttreasure.DBLitePal.LitePalManage;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.base.BaseActivity;
import com.nyq.projecttreasure.models.AreaInfo;
import com.nyq.projecttreasure.models.HealthInfo;
import com.nyq.projecttreasure.utils.AGCache;
import com.nyq.projecttreasure.utils.BannerGlideImageLoader;
import com.nyq.projecttreasure.utils.ColorUtil;
import com.nyq.projecttreasure.utils.DensityUtil;
import com.nyq.projecttreasure.utils.StatusBarUtil;
import com.nyq.projecttreasure.utils.StringHelper;
import com.nyq.projecttreasure.utils.TimeHelper;
import com.nyq.projecttreasure.views.DividerItemDecoration;
import com.nyq.projecttreasure.views.MLImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChenjinshiActivity extends BaseActivity {

    @BindView(R.id.top_left)
    LinearLayout topLeft;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.toolbar)
    LinearLayout toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.top_middle)
    LinearLayout topMiddle;
    @BindView(R.id.toobar_layout)
    LinearLayout toobarLayout;
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.top_right)
    LinearLayout topRight;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    //    private ClassicsHeader mClassicsHeader;
//    private Drawable mDrawableProgress;
    private List<HealthInfo> healthInfos;
    private JkzxAdapter mAdapter;
    private int pageNumber = 1;

    private int adViewHeight = 180; // 广告视图的高度
    private int adViewTopSpace; // 广告视图距离顶部的距离
    private boolean isScrollIdle = true; // recycleView是否在滑动
    private int titleViewHeight = 40; // 标题栏的高度
    private List<String> BANNER_ITEMS = new ArrayList<>();

    private AreaInfo areaInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chenjinshi);
        ButterKnife.bind(this);
        initToobarView();
        initSmartRefreshLayout();
        getInfoList();
    }

    public void initToobarView() {
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, toolbar);
        toolbar.setBackgroundColor(0);
        topTitle.setText("沉浸式");
        topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                activity.overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
            }
        });
        tvAddress.setText(AGCache.CITY_NAME);
        Log.e("areaInfo", AGCache.CITY_CODE);
        Log.e("areaInfo", LitePalManage.getIntance().getAreaInfo(AGCache.CITY_CODE).toString());
    }


    public void initSmartRefreshLayout() {
        getInfoList();
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true); //设置是否在全部加载结束之后Footer跟随内容
        refreshLayout.setEnableHeaderTranslationContent(false);
        refreshLayout.setEnableLoadMore(false);
        // <!--金典刷新-->
//        mClassicsHeader = (ClassicsHeader) refreshLayout.getRefreshHeader();
//        mClassicsHeader.setSpinnerStyle(SpinnerStyle.FixedBehind);
//        mDrawableProgress = ((ImageView) mClassicsHeader.findViewById(ClassicsHeader.ID_IMAGE_PROGRESS)).getDrawable();
//        if (mDrawableProgress instanceof LayerDrawable) {
//            mDrawableProgress = ((LayerDrawable) mDrawableProgress).getDrawable(0);
//        }
//        mClassicsHeader.setAccentColor(getResources().getColor(R.color.colorPrimary));

        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new JkzxAdapter(activity, R.layout.item_listview_jkzx, healthInfos);
//        //打开或关闭加载
        mAdapter.setEnableLoadMore(true);
        mAdapter.openLoadAnimation();
        recyclerView.setAdapter(mAdapter);
        mAdapter.replaceData(healthInfos);


        BANNER_ITEMS.clear();
        BANNER_ITEMS.add("https://image.baidu.com/search/down?tn=download&ipn=dwnl&word=download&ie=utf8&fr=result&url=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2F00%2F00%2F69%2F40%2F0f88dcc2a682f87d6537fd630ef93db3.jpg&thumburl=https%3A%2F%2Fss1.bdstatic.com%2F70cFvXSh_Q1YnxGkpoWK1HF6hhy%2Fit%2Fu%3D787334399%2C1030936190%26fm%3D26%26gp%3D0.jpg");
        BANNER_ITEMS.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1181694068,3468684352&fm=26&gp=0.jpg");
        BANNER_ITEMS.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1337913,139449909&fm=26&gp=0.jpg");
        BANNER_ITEMS.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1771357690,1657880027&fm=26&gp=0.jpg");
        BANNER_ITEMS.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=612621339,1757807680&fm=26&gp=0.jpg");
        //添加Header
        View header = LayoutInflater.from(activity).inflate(R.layout.listitem_movie_header, recyclerView, false);
        Banner banner = (Banner) header;
        // 设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        // 设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        banner.setImageLoader(BannerGlideImageLoader.init());
        banner.setImages(BANNER_ITEMS);
        banner.setDelayTime(3000);
        banner.start();
        mAdapter.addHeaderView(banner);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageNumber = 1;
                        getInfoList();
                        mAdapter.replaceData(healthInfos);
                        refreshLayout.finishRefresh();
                        refreshLayout.setNoMoreData(false);
                        mAdapter.setEnableLoadMore(true);
                    }
                }, 2000);
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageNumber++;
                        if (pageNumber > 4) {
                            Toast.makeText(activity, "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            mAdapter.setEnableLoadMore(false);
                        } else {
                            getInfoList();
                            mAdapter.addData(healthInfos);
                            mAdapter.setEnableLoadMore(true);
                            mAdapter.loadMoreComplete();
                        }
                    }
                }, 2000);
            }
        }, recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isScrollIdle = (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isScrollIdle && adViewTopSpace < 0) {
                    return;
                }
                super.onScrolled(recyclerView, dx, dy);
                adViewTopSpace = DensityUtil.px2dip(activity, mAdapter.getHeaderLayout().getTop());
                adViewHeight = DensityUtil.px2dip(activity, mAdapter.getHeaderLayout().getHeight());
                if (-adViewTopSpace <= 50) {
                    topMiddle.setVisibility(View.INVISIBLE);
                } else {
                    topMiddle.setVisibility(View.VISIBLE);
                }
                handleTitleBarColorEvaluate();
            }
        });

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

    //从网络获取数据
    private void getInfoList() {
        healthInfos = new ArrayList<>();
        healthInfos.add(new HealthInfo("Activity沉浸式", new Date().toString(), "Activity内容入嵌沉浸式", "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1607779262,1632598626&fm=26&gp=0.jpg"));
        healthInfos.add(new HealthInfo("默认主题", new Date().toString(), getResources().getString(R.string.item_style_theme_default_abstract), "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=4017085655,264997454&fm=26&gp=0.jpg"));
        healthInfos.add(new HealthInfo("橙色主题", new Date().toString(), getResources().getString(R.string.item_style_theme_orange_abstract), "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3182596561,138363622&fm=26&gp=0.jpg"));
        healthInfos.add(new HealthInfo("红色主题", new Date().toString(), getResources().getString(R.string.item_style_theme_red_abstract), "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=659044909,4042106041&fm=26&gp=0.jpg"));
        healthInfos.add(new HealthInfo("绿色主题", new Date().toString(), getResources().getString(R.string.item_style_theme_green_abstract), "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=4111176407,2620705746&fm=26&gp=0.jpg"));
        healthInfos.add(new HealthInfo("蓝色主题", new Date().toString(), getResources().getString(R.string.item_style_theme_blue_abstract), "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2739505509,237691169&fm=26&gp=0.jpg"));
    }

    // 处理标题栏颜色渐变
    private void handleTitleBarColorEvaluate() {
        float fraction;
        toolbar.setAlpha(1f);
        if (adViewTopSpace > 5) {
            fraction = 1f - adViewTopSpace * 1f / 60;
            if (fraction < 0f) {
                fraction = 0f;
            }
            toolbar.setAlpha(fraction);
            return;
        }
        float space = Math.abs(adViewTopSpace) * 1f;
        fraction = space / (adViewHeight - titleViewHeight);
        if (fraction < 0f) {
            fraction = 0f;
        }
        if (fraction > 1f) {
            fraction = 1f;
        }
        toolbar.setAlpha(1f);
        if (fraction >= 1f) {
            fakeStatusBar.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
            toolbar.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
        } else {
            fakeStatusBar.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(activity, fraction, R.color.transparent, R.color.colorPrimary));
            toolbar.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(activity, fraction, R.color.transparent, R.color.colorPrimary));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
