package com.nyq.projecttreasure.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.activitys.baidumap.BaiDuMapActivity;
import com.nyq.projecttreasure.activitys.erweima.QrCodeActivity;
import com.nyq.projecttreasure.activitys.youyaadapter.DelegationAdapterActivity;
import com.nyq.projecttreasure.adapter.AppAdapter;
import com.nyq.projecttreasure.adapter.ViewPagerFragmentAdapter;
import com.nyq.projecttreasure.cehuacaidan.CeHuaCaiDanActivity;
import com.nyq.projecttreasure.models.AppInfo;
import com.nyq.projecttreasure.models.InfoColumn;
import com.nyq.projecttreasure.utils.AGCache;
import com.nyq.projecttreasure.utils.BannerGlideImageLoader;
import com.nyq.projecttreasure.utils.StringHelper;
import com.nyq.projecttreasure.views.FixedGridView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 首页，嵌套滑动
 */
public class HomeFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.toolbarLayout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.fixedGridView)
    FixedGridView fixedGridView;
    @BindView(R.id.tl_main_tabtop)
    TabLayout tlMainTabtop;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.nsv)
    NestedScrollView nsv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.area_layout)
    LinearLayout areaLayout;

    private ClassicsHeader mClassicsHeader;
    private Drawable mDrawableProgress;
    private List<String> BANNER_ITEMS = new ArrayList<>();
    private AppAdapter appAdapter;
    private List<AppInfo> appList;
    private List<Fragment> fragmentList;
    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private List<InfoColumn> infoColumnList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public void initView() {
        setStatusBarColor(R.color.colorPrimary);
        tvArea.setText(AGCache.CITY_NAME);
        BANNER_ITEMS.clear();
        BANNER_ITEMS.add("https://image.baidu.com/search/down?tn=download&ipn=dwnl&word=download&ie=utf8&fr=result&url=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2F00%2F00%2F69%2F40%2F0f88dcc2a682f87d6537fd630ef93db3.jpg&thumburl=https%3A%2F%2Fss1.bdstatic.com%2F70cFvXSh_Q1YnxGkpoWK1HF6hhy%2Fit%2Fu%3D787334399%2C1030936190%26fm%3D26%26gp%3D0.jpg");
        BANNER_ITEMS.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1181694068,3468684352&fm=26&gp=0.jpg");
        BANNER_ITEMS.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1337913,139449909&fm=26&gp=0.jpg");
        BANNER_ITEMS.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1771357690,1657880027&fm=26&gp=0.jpg");
        BANNER_ITEMS.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=612621339,1757807680&fm=26&gp=0.jpg");
        // 设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        banner.setImageLoader(BannerGlideImageLoader.init());
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImages(BANNER_ITEMS);
        banner.setDelayTime(3000);
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Toast.makeText(getContext(), "banner  " + position, Toast.LENGTH_SHORT).show();
            }
        });

        appAdapter = new AppAdapter(getActivity(), getGridData());
        fixedGridView.setAdapter(appAdapter);
        fixedGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(getContext(), CeHuaCaiDanActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
                } else if (position == 1) {
                    Intent intent = new Intent(getContext(), QrCodeActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
                } else if (position == 2) {
                    Intent intent = new Intent(getContext(), BaiDuMapActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
                } else if (position == 3) {
                    Intent intent = new Intent(getContext(), DelegationAdapterActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
                }else {
                    Toast.makeText(getContext(), "fixedGridView  " + position, Toast.LENGTH_SHORT).show();
                }
            }
        });
        refreshLayout.setEnableFooterFollowWhenLoadFinished(false); //设置是否在全部加载结束之后Footer跟随内容
        mClassicsHeader = (ClassicsHeader) refreshLayout.getRefreshHeader();
        mClassicsHeader.setSpinnerStyle(SpinnerStyle.Scale);
        mDrawableProgress = ((ImageView) mClassicsHeader.findViewById(ClassicsHeader.ID_IMAGE_PROGRESS)).getDrawable();
        if (mDrawableProgress instanceof LayerDrawable) {
            mDrawableProgress = ((LayerDrawable) mDrawableProgress).getDrawable(0);
        }
        mClassicsHeader.setAccentColor(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getFragmentData();
                refreshLayout.finishRefresh();
                refreshLayout.setNoMoreData(true);//恢复上拉状态
            }
        });
        getFragmentData();
    }


    public void getFragmentData() {
        infoColumnList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            InfoColumn infoColumn = new InfoColumn();
            infoColumn.setColumnCode(StringHelper.convertToString("" + i));
            infoColumn.setColumnName(StringHelper.convertToString("菜单" + i));
            infoColumn.setColumnType(StringHelper.convertToString("0" + i));
            infoColumnList.add(infoColumn);
        }

        fragmentList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            fragmentList.add(JkzxFragment.newInstance());
        }
        viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getChildFragmentManager());
        viewpager.setAdapter(viewPagerFragmentAdapter);
        tlMainTabtop.setupWithViewPager(viewpager);
        // 更新适配器数据
        viewPagerFragmentAdapter.setList(infoColumnList);
        viewPagerFragmentAdapter.setListData(fragmentList);
//        app:tabMode="fixed"
        tlMainTabtop.setTabMode(TabLayout.MODE_FIXED);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlMainTabtop));
        tlMainTabtop.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public List<AppInfo> getGridData() {
        appList = new ArrayList<>();
        appList.add(new AppInfo("侧滑菜单", R.mipmap.image_practice_repast_1));
        appList.add(new AppInfo("二维码扫描", R.mipmap.image_practice_repast_2));
        appList.add(new AppInfo("百度地图", R.mipmap.image_practice_repast_3));
        appList.add(new AppInfo("优雅的Adapter", R.mipmap.image_practice_repast_4));
        appList.add(new AppInfo("但家香酥鸭", R.mipmap.image_practice_repast_1));
        appList.add(new AppInfo("香菇蒸鸟蛋", R.mipmap.image_practice_repast_2));
        appList.add(new AppInfo("花溪牛肉粉", R.mipmap.image_practice_repast_3));
        appList.add(new AppInfo("破酥包", R.mipmap.image_practice_repast_4));
        appList.add(new AppInfo("但家香酥鸭", R.mipmap.image_practice_repast_1));
        return appList;
    }

    //为状态栏着色
    public void setStatusBarColor(int colorRes) {
        refreshLayout.getLayout().setBackgroundResource(R.color.common_nomal_color);
        refreshLayout.setPrimaryColors(0, 0xff666666);
        toolbar.setBackgroundResource(colorRes);
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(colorRes);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
