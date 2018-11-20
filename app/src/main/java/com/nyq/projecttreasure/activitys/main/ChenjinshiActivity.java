package com.nyq.projecttreasure.activitys.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.base.BaseActivity;
import com.nyq.projecttreasure.models.HealthInfo;
import com.nyq.projecttreasure.utils.ColorUtil;
import com.nyq.projecttreasure.utils.DensityUtil;
import com.nyq.projecttreasure.utils.GlideImageLoader;
import com.nyq.projecttreasure.utils.StatusBarUtil;
import com.nyq.projecttreasure.utils.StringHelper;
import com.nyq.projecttreasure.utils.TimeHelper;
import com.nyq.projecttreasure.views.DividerItemDecoration;
import com.nyq.projecttreasure.views.MLImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
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
//    private ClassicsHeader mClassicsHeader;
//    private Drawable mDrawableProgress;
    private List<HealthInfo> healthInfos;
    private List<HealthInfo> healthInfosMore = new ArrayList<>();
    private JkzxAdapter mAdapter;
    private int mOffset = 0;
    private int mScrollY = 0;


    private int adViewHeight = 180; // 广告视图的高度
    private int adViewTopSpace; // 广告视图距离顶部的距离
    private boolean isScrollIdle = true; // recycleView是否在滑动
    private int titleViewHeight = 40; // 标题栏的高度
    private List<String> BANNER_ITEMS = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chenjinshi);
        ButterKnife.bind(this);
        initToobarView();
        initSmartRefreshLayout();
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
    }


    public void initSmartRefreshLayout() {
        getInfoList();
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true); //设置是否在全部加载结束之后Footer跟随内容
        refreshLayout.setEnableHeaderTranslationContent(false);
        // <!--金典刷新-->
//        mClassicsHeader = (ClassicsHeader) refreshLayout.getRefreshHeader();
//        mClassicsHeader.setSpinnerStyle(SpinnerStyle.FixedBehind);
//        mDrawableProgress = ((ImageView) mClassicsHeader.findViewById(ClassicsHeader.ID_IMAGE_PROGRESS)).getDrawable();
//        if (mDrawableProgress instanceof LayerDrawable) {
//            mDrawableProgress = ((LayerDrawable) mDrawableProgress).getDrawable(0);
//        }
//        mClassicsHeader.setAccentColor(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setEnableLoadMore(false);

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
        BANNER_ITEMS.add("http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg");
        BANNER_ITEMS.add("http://app.infunpw.com/commons/images/cinema/cinema_films/3566.jpg");
        BANNER_ITEMS.add("http://app.infunpw.com/commons/images/cinema/cinema_films/3750.jpg");
        BANNER_ITEMS.add("http://app.infunpw.com/commons/images/cinema/cinema_films/3757.jpg");
        //添加Header
        View header = LayoutInflater.from(activity).inflate(R.layout.listitem_movie_header, recyclerView, false);
        Banner banner = (Banner) header;
        // 设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        // 设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(BANNER_ITEMS);
        banner.setDelayTime(3000);
        banner.start();
        mAdapter.addHeaderView(banner);
        mAdapter.openLoadAnimation();

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getInfoList();
                        mAdapter.replaceData(healthInfos);
                        refreshLayout.finishRefresh();
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                        mAdapter.setEnableLoadMore(true);
                    }
                }, 2000);
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mAdapter.getData().size() > 15) {
                    Toast.makeText(activity, "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                    mAdapter.setEnableLoadMore(false);
                } else {
                    getInfoList();
                    mAdapter.addData(healthInfos);
                    mAdapter.setEnableLoadMore(true);
                    mAdapter.loadMoreComplete();
                }
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
                super.onScrolled(recyclerView, dx, dy);
                if (isScrollIdle && adViewTopSpace < 0) return;
                adViewTopSpace = DensityUtil.px2dip(activity, mAdapter.getHeaderLayout().getTop());
                adViewHeight = DensityUtil.px2dip(activity, mAdapter.getHeaderLayout().getHeight());
                if (-adViewTopSpace <= 50) {
                    topMiddle.setVisibility(View.GONE);
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
        healthInfos.add(new HealthInfo("默认主题", new Date().toString(), getResources().getString(R.string.item_style_theme_default_abstract), "http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfos.add(new HealthInfo("橙色主题", new Date().toString(), getResources().getString(R.string.item_style_theme_orange_abstract), "http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfos.add(new HealthInfo("红色主题", new Date().toString(), getResources().getString(R.string.item_style_theme_red_abstract), "http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfos.add(new HealthInfo("绿色主题", new Date().toString(), getResources().getString(R.string.item_style_theme_green_abstract), "http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfos.add(new HealthInfo("蓝色主题", new Date().toString(), getResources().getString(R.string.item_style_theme_blue_abstract), "http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfos.add(new HealthInfo("Activity沉浸式", new Date().toString(), getResources().getString(R.string.item_style_theme_blue_abstract), "http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfos.add(new HealthInfo("默认主题", new Date().toString(), getResources().getString(R.string.item_style_theme_default_abstract), "http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfos.add(new HealthInfo("橙色主题", new Date().toString(), getResources().getString(R.string.item_style_theme_orange_abstract), "http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfos.add(new HealthInfo("红色主题", new Date().toString(), getResources().getString(R.string.item_style_theme_red_abstract), "http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfos.add(new HealthInfo("绿色主题", new Date().toString(), getResources().getString(R.string.item_style_theme_green_abstract), "http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfos.add(new HealthInfo("蓝色主题", new Date().toString(), getResources().getString(R.string.item_style_theme_blue_abstract), "http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfos.add(new HealthInfo("Activity沉浸式", new Date().toString(), getResources().getString(R.string.item_style_theme_blue_abstract), "http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg"));
        healthInfosMore.addAll(healthInfos);
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
