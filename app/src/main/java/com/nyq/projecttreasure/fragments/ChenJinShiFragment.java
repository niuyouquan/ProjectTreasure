package com.nyq.projecttreasure.fragments;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.models.Item;
import com.nyq.projecttreasure.utils.GlideImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * 消息
 */
public class ChenJinShiFragment extends Fragment implements BaseQuickAdapter.OnItemClickListener {

    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.menu)
    TextView menu;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;

    private ClassicsHeader mClassicsHeader;
    private Drawable mDrawableProgress;
    private QuickAdapter mAdpater;
    private List<String> BANNER_ITEMS = new ArrayList<>();

    public static ChenJinShiFragment newInstance() {
        ChenJinShiFragment fragment = new ChenJinShiFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemMore.clear();
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg, container, false);
        unbinder = ButterKnife.bind(this, view);
        title.setText("改变主题颜色");
        title.setTextColor(getResources().getColor(R.color.white));
        initView();
        return view;
    }

    public void initView() {
        refreshLayout.setEnableRefresh(true); // 是否开启下拉刷新功能（默认true）
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true); //设置是否在全部加载结束之后Footer跟随内容

        int delta = new Random().nextInt(7 * 24 * 60 * 60 * 1000);
        ClassicsFooter classicsFooter = (ClassicsFooter) refreshLayout.getRefreshFooter();
        mClassicsHeader = (ClassicsHeader) refreshLayout.getRefreshHeader();
//        mClassicsHeader.setLastUpdateTime(new Date(System.currentTimeMillis() - delta));
//        mClassicsHeader.setTimeFormat(new SimpleDateFormat("更新于 MM-dd HH:mm", Locale.CHINA));
//        mClassicsHeader.setTimeFormat(new DynamicTimeFormat("更新于 %s"));
        mClassicsHeader.setSpinnerStyle(SpinnerStyle.Scale);
        classicsFooter.setSpinnerStyle(SpinnerStyle.Scale);
        mDrawableProgress = ((ImageView) mClassicsHeader.findViewById(ClassicsHeader.ID_IMAGE_PROGRESS)).getDrawable();
        if (mDrawableProgress instanceof LayerDrawable) {
            mDrawableProgress = ((LayerDrawable) mDrawableProgress).getDrawable(0);
        }

        setStatusBarColor(R.color.colorPrimary);
        mClassicsHeader.setAccentColor(getResources().getColor(R.color.colorPrimary));
        classicsFooter.setAccentColor(getResources().getColor(R.color.colorPrimary));

        if (recyclerView instanceof RecyclerView) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdpater = new QuickAdapter();
            recyclerView.setAdapter(mAdpater);
            mAdpater.replaceData(items);
            refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getData();
                            mAdpater.replaceData(items);
                            refreshLayout.finishRefresh();
                            refreshLayout.setNoMoreData(false);//恢复上拉状态
                        }
                    }, 2000);
                }

                @Override
                public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mAdpater.getData().size()>15) {
                                Toast.makeText(getContext(), "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                                refreshLayout.finishLoadMoreWithNoMoreData();//设置之后，将不会再触发加载事件
                            } else {
                                getData();
                                mAdpater.addData(items);
                                refreshLayout.finishLoadMore();
                            }
                        }
                    }, 2000);
                }
            });

            BANNER_ITEMS.clear();
            BANNER_ITEMS.add("http://app.infunpw.com/commons/images/cinema/cinema_films/3823.jpg");
            BANNER_ITEMS.add("http://app.infunpw.com/commons/images/cinema/cinema_films/3566.jpg");
            BANNER_ITEMS.add("http://app.infunpw.com/commons/images/cinema/cinema_films/3750.jpg");
            BANNER_ITEMS.add("http://app.infunpw.com/commons/images/cinema/cinema_films/3757.jpg");
            //添加Header
            View header = LayoutInflater.from(getContext()).inflate(R.layout.listitem_movie_header, recyclerView, false);
            Banner banner = (Banner) header;
            // 设置banner样式
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            // 设置banner动画效果
            banner.setBannerAnimation(Transformer.DepthPage);
            banner.setImageLoader(new GlideImageLoader());
            banner.setImages(BANNER_ITEMS);
            banner.setDelayTime(3000);
            banner.start();
            mAdpater.addHeaderView(banner);
            mAdpater.openLoadAnimation();
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    Toast.makeText(getContext(), "banner  "+position, Toast.LENGTH_SHORT).show();
                }
            });
            mAdpater.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        String name = itemMore.get(position).getName();
        if (name.equals("默认主题")) {
            setStatusBarColor(R.color.colorPrimary);
            mClassicsHeader.setAccentColor(getResources().getColor(R.color.colorPrimary));
        } else if (name.equals("橙色主题")) {
            setStatusBarColor(android.R.color.holo_orange_light);
            mClassicsHeader.setAccentColor(getResources().getColor(android.R.color.holo_orange_light));
        } else if (name.equals("红色主题")) {
            setStatusBarColor(android.R.color.holo_red_light);
            mClassicsHeader.setAccentColor(getResources().getColor(android.R.color.holo_red_light));
        } else if (name.equals("绿色主题")) {
            setStatusBarColor(android.R.color.holo_green_light);
            mClassicsHeader.setAccentColor(getResources().getColor(android.R.color.holo_green_light));
        } else if (name.equals("蓝色主题")) {
            setStatusBarColor(R.color.colorPrimary);
            mClassicsHeader.setAccentColor(getResources().getColor(R.color.colorPrimary));
        }
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

    List<Item> items;
    private List<Item> itemMore = new ArrayList<>();
    public void getData() {
        items = new ArrayList<>();
        items.add(new Item("默认主题", getResources().getString(R.string.item_style_theme_default_abstract)));
        items.add(new Item("橙色主题", getResources().getString(R.string.item_style_theme_orange_abstract)));
        items.add(new Item("红色主题", getResources().getString(R.string.item_style_theme_red_abstract)));
        items.add(new Item("绿色主题", getResources().getString(R.string.item_style_theme_green_abstract)));
        items.add(new Item("蓝色主题", getResources().getString(R.string.item_style_theme_blue_abstract)));
        itemMore.addAll(items);
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
