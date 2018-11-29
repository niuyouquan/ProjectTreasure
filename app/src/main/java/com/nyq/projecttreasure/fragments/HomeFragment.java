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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.adapter.AppAdapter;
import com.nyq.projecttreasure.adapter.ViewPagerFragmentAdapter;
import com.nyq.projecttreasure.cehuacaidan.CeHuaCaiDanActivity;
import com.nyq.projecttreasure.models.AppInfo;
import com.nyq.projecttreasure.models.InfoColumn;
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
        BANNER_ITEMS.clear();
        BANNER_ITEMS.add("http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg");
        BANNER_ITEMS.add("http://app.infunpw.com/commons/images/cinema/cinema_films/3566.jpg");
        BANNER_ITEMS.add("http://app.infunpw.com/commons/images/cinema/cinema_films/3750.jpg");
        BANNER_ITEMS.add("http://app.infunpw.com/commons/images/cinema/cinema_films/3757.jpg");
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
                if (getGridData().get(position).getName().equals("侧滑菜单")) {
                    Intent intent = new Intent(getContext(), CeHuaCaiDanActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
                } else {
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
        appList.add(new AppInfo("香菇蒸鸟蛋", R.mipmap.image_practice_repast_2));
        appList.add(new AppInfo("花溪牛肉粉", R.mipmap.image_practice_repast_3));
        appList.add(new AppInfo("破酥包", R.mipmap.image_practice_repast_4));
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
