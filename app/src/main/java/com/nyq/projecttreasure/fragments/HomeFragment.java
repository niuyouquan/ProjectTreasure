package com.nyq.projecttreasure.fragments;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.adapter.AppAdapter;
import com.nyq.projecttreasure.adapter.ViewPagerFragmentAdapter;
import com.nyq.projecttreasure.models.AppInfo;
import com.nyq.projecttreasure.models.InfoColumn;
import com.nyq.projecttreasure.models.Item;
import com.nyq.projecttreasure.utils.GlideImageLoader;
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
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.menu)
    TextView menu;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
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
    private QuickAdapter mAdpater;
    private AppAdapter appAdapter;
    private List<AppInfo> appList;
    private List<Fragment> listData;
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
        title.setText("嵌套滑动");
        BANNER_ITEMS.clear();
        BANNER_ITEMS.add("http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg");
        BANNER_ITEMS.add("http://app.infunpw.com/commons/images/cinema/cinema_films/3566.jpg");
        BANNER_ITEMS.add("http://app.infunpw.com/commons/images/cinema/cinema_films/3750.jpg");
        BANNER_ITEMS.add("http://app.infunpw.com/commons/images/cinema/cinema_films/3757.jpg");
        // 设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        banner.setImageLoader(new GlideImageLoader());
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
                Toast.makeText(getContext(), "fixedGridView  " + position, Toast.LENGTH_SHORT).show();
            }
        });

        getFragmentData();
        refreshLayout.setEnableFooterFollowWhenLoadFinished(false); //设置是否在全部加载结束之后Footer跟随内容
//        ClassicsFooter classicsFooter = (ClassicsFooter) refreshLayout.getRefreshFooter();
        mClassicsHeader = (ClassicsHeader) refreshLayout.getRefreshHeader();
        mClassicsHeader.setSpinnerStyle(SpinnerStyle.Scale);
//        classicsFooter.setSpinnerStyle(SpinnerStyle.Scale);
        mDrawableProgress = ((ImageView) mClassicsHeader.findViewById(ClassicsHeader.ID_IMAGE_PROGRESS)).getDrawable();
        if (mDrawableProgress instanceof LayerDrawable) {
            mDrawableProgress = ((LayerDrawable) mDrawableProgress).getDrawable(0);
        }
        setStatusBarColor(R.color.colorPrimary);
        mClassicsHeader.setAccentColor(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getFragmentData();
                refreshLayout.finishRefresh();
                refreshLayout.setNoMoreData(false);//恢复上拉状态
            }
        });
    }

    public void getFragmentData() {
        infoColumnList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            InfoColumn infoColumn = new InfoColumn();
            infoColumn.setColumnCode(StringHelper.convertToString(""+i));
            infoColumn.setColumnName(StringHelper.convertToString("菜单"+i));
            infoColumn.setColumnType(StringHelper.convertToString("0"+i));
            infoColumnList.add(infoColumn);
        }

        listData = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            listData.add(JkzxFragment.newInstance(infoColumnList.get(i).getColumnCode(), infoColumnList.get(i).getColumnName()));
        }
        viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getChildFragmentManager());
        viewpager.setAdapter(viewPagerFragmentAdapter);
        tlMainTabtop.setupWithViewPager(viewpager);
        // 更新适配器数据
        viewPagerFragmentAdapter.setList(infoColumnList);
//        app:tabMode="fixed"
        tlMainTabtop.setTabMode(TabLayout.MODE_FIXED);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlMainTabtop));
        tlMainTabtop.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 取消平滑切换
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

    public class QuickAdapter extends BaseQuickAdapter<Item, BaseViewHolder> {
        public QuickAdapter() {
            super(R.layout.item_layout1);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, Item item) {
            viewHolder.setText(R.id.tv1, item.getName())
                    .setText(R.id.tv2, item.getContent());
        }
    }




    public List<AppInfo> getGridData() {
        appList = new ArrayList<>();
        appList.add(new AppInfo("但家香酥鸭", R.mipmap.image_practice_repast_1));
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
        toolbar.setBackgroundResource(colorRes);
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
