package com.nyq.projecttreasure.cehuacaidan;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.activitys.main.ChenjinshiActivity;
import com.nyq.projecttreasure.base.BaseActivity;
import com.nyq.projecttreasure.models.HealthInfo;
import com.nyq.projecttreasure.utils.StringHelper;
import com.nyq.projecttreasure.utils.TimeHelper;
import com.nyq.projecttreasure.utils.ToastUtil;
import com.nyq.projecttreasure.views.DividerItemDecoration;
import com.nyq.projecttreasure.views.MLImageView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CeHuaCaiDanActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recycler_view)
    SwipeMenuRecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private List<HealthInfo> healthInfos;
    private JkzxAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ce_hua_cai_dan);
        ButterKnife.bind(this);
        setStatusBarColor(R.color.colorPrimary);
        initView();
    }

    public void initView() {
        getInfoList();
        back.setVisibility(View.VISIBLE);
        title.setText("侧滑菜单");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                activity.overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
            }
        });
        mRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        mRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new JkzxAdapter(activity, R.layout.item_listview_jkzx, healthInfos);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.replaceData(healthInfos);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtil.showToast(activity,""+position,Toast.LENGTH_SHORT);
            }
        });

    }

    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int position) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_72);

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            {
                SwipeMenuItem addItem = new SwipeMenuItem(activity).setBackground(R.drawable.selector_green)
                        .setImage(R.mipmap.ic_action_add)
                        .setWidth(width)
                        .setHeight(height);
                swipeLeftMenu.addMenuItem(addItem); // 添加菜单到左侧。

                SwipeMenuItem closeItem = new SwipeMenuItem(activity).setBackground(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_close)
                        .setWidth(width)
                        .setHeight(height);
                swipeLeftMenu.addMenuItem(closeItem); // 添加菜单到左侧。
            }

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(activity).setBackground(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

                SwipeMenuItem addItem = new SwipeMenuItem(activity).setBackground(R.drawable.selector_green)
                        .setImage(R.mipmap.ic_action_add)
                        .setText("添加")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // 添加菜单到右侧。
            }
        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                Toast.makeText(activity, "list第" + position + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT)
                        .show();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                Toast.makeText(activity, "list第" + position + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

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

    //为状态栏着色
    public void setStatusBarColor(int colorRes) {
        toolbar.setBackgroundResource(colorRes);
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(colorRes);
    }
}
