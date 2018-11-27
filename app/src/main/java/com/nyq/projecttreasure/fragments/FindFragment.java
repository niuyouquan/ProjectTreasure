package com.nyq.projecttreasure.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.interfaces.IPickerViewData;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.models.CityBean;
import com.nyq.projecttreasure.models.PickerViewData;
import com.nyq.projecttreasure.models.ProvinceBean;
import com.nyq.projecttreasure.selectphoto.GlideImageLoader;
import com.nyq.projecttreasure.selectphoto.MorePhotoAdapter;
import com.nyq.projecttreasure.selectphoto.lookbigImage.ImagePagerActivity;
import com.nyq.projecttreasure.utils.StringHelper;
import com.rain.library.controller.PhotoPickConfig;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 发现
 */
public class FindFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.more_img)
    TextView moreImg;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.menu)
    TextView menu;

    private TimePickerView pvTime;
    private OptionsPickerView pvOptions;
    private MorePhotoAdapter photoAdapter;
    private ArrayList<String> photoLists = new ArrayList<>();

    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<CityBean>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<IPickerViewData>>> options3Items = new ArrayList<>();

    public static FindFragment newInstance() {
        FindFragment fragment = new FindFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        unbinder = ButterKnife.bind(this, view);
        setStatusBarColor(R.color.colorPrimary);
        initView(view);
        return view;
    }

    public void initView(View view) {
        title.setText("发现工具了");
        getDatas();
        initTime();
        initCity();

        photoAdapter = new MorePhotoAdapter(getContext());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);
        photoAdapter.setOnItemClickListener(new MorePhotoAdapter.onItemListener() {
            @Override
            public void onItemClick(String type, int position) {
                if (photoAdapter.getItemViewType(position) == MorePhotoAdapter.TYPE_ADD) {
                    new PhotoPickConfig
                            .Builder(getActivity())
                            .imageLoader(GlideImageLoader.init())                //图片加载方式，支持任意第三方图片加载库
                            .spanCount(PhotoPickConfig.GRID_SPAN_COUNT)         //相册列表每列个数，默认为3
                            .pickMode(PhotoPickConfig.MODE_PICK_MORE)           //设置照片选择模式为单选，默认为单选
                            .maxPickSize(9)   //多选时可以选择的图片数量，默认为1张
                            .showCamera(true)           //是否展示相机icon，默认展示
                            .clipPhoto(false)            //是否开启裁剪照片功能，默认关闭
                            .clipCircle(false)          //是否裁剪方式为圆形，默认为矩形
                            .build();
                } else {
                    if (type.equals("bigImage")) {
                        Intent intent = new Intent(getContext(), ImagePagerActivity.class);
                        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, photoLists);
                        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                        getContext().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
                    } else if (type.equals("delete")) {
                        photoLists.remove(position);
                        photoAdapter.refresh(photoLists);
                    }
                }
            }
        });
    }

    /**
     * 初始化时间选择器
     */
    public void initTime() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);
        //正确设置方式 原因：注意事项有说明
        startDate.set(2013, 0, 1);
        endDate.set(2020, 11, 31);

        pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                timeTv.setText(getTime(date));
            }
        })
                .setType(new boolean[]{true, true, true, true, true, true})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确认")//确认按钮文字
                .setContentTextSize(18)//滚轮文字大小
                .setTitleSize(18)//标题文字大小
                .setTitleText("请选择时间")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(getContext().getResources().getColor(R.color.white))//标题文字颜色
                .setSubmitColor(getContext().getResources().getColor(R.color.white))//确定按钮文字颜色
                .setCancelColor(getContext().getResources().getColor(R.color.white))//取消按钮文字颜色
                .setTitleBgColor(getContext().getResources().getColor(R.color.colorPrimary))//标题背景颜色 Night mode
                .setBgColor(getContext().getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();

        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    /**
     * 省市县（区）三级联动
     */
    public void initCity() {
        //选项选择器
        pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(options2).getPickerViewText()
                        + options3Items.get(options1).get(options2).get(options3).getPickerViewText();
                addressTv.setText(tx);
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("城市选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(getContext().getResources().getColor(R.color.white))//标题文字颜色
                .setSubmitColor(getContext().getResources().getColor(R.color.white))//确定按钮文字颜色
                .setCancelColor(getContext().getResources().getColor(R.color.white))//取消按钮文字颜色
                .setTitleBgColor(getContext().getResources().getColor(R.color.colorPrimary))//标题背景颜色 Night mode
                .setBgColor(getContext().getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLabels("省", "市", "区")//设置选择的三级单位
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);//添加数据源
        addressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvOptions.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK) return;
        switch (requestCode) {
            case PhotoPickConfig.PICK_MORE_REQUEST_CODE:        //多选
                String path = data.getStringExtra(PhotoPickConfig.EXTRA_SINGLE_PHOTO);
                if (StringHelper.isNotBlank(path)) {
                    photoLists.add(path);
                }
                ArrayList<String> photoList = data.getStringArrayListExtra(PhotoPickConfig.EXTRA_STRING_ARRAYLIST);
                if (photoList != null && !photoList.isEmpty()) {
                    photoLists.addAll(photoList);
                }
                photoAdapter.refresh(photoLists);
                break;

        }
    }

    //为状态栏着色
    public void setStatusBarColor(int colorRes) {
        toolbar.setBackgroundResource(colorRes);
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(colorRes);
    }

    public void getDatas() {
        //选项1
        options1Items.add(new ProvinceBean(0, "广东", "广东省，以岭南东道、广南东路得名", "其他数据"));
        options1Items.add(new ProvinceBean(1, "湖南", "湖南省地处中国中部、长江中游，因大部分区域处于洞庭湖以南而得名湖南", "芒果TV"));
        options1Items.add(new ProvinceBean(2, "广西", "嗯～～", ""));

        //选项2
        ArrayList<CityBean> options2Items_01 = new ArrayList<>();
        options2Items_01.add(new CityBean("广州"));
        options2Items_01.add(new CityBean("佛山"));
        options2Items_01.add(new CityBean("东莞"));
        options2Items_01.add(new CityBean("阳江"));
        options2Items_01.add(new CityBean("珠海"));

        ArrayList<CityBean> options2Items_02 = new ArrayList<>();
        options2Items_02.add(new CityBean("长沙"));
        options2Items_02.add(new CityBean("岳阳"));

        ArrayList<CityBean> options2Items_03 = new ArrayList<>();
        options2Items_03.add(new CityBean("桂林"));

        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);
        options2Items.add(options2Items_03);

        //选项3
        ArrayList<ArrayList<IPickerViewData>> options3Items_01 = new ArrayList<>();
        ArrayList<ArrayList<IPickerViewData>> options3Items_02 = new ArrayList<>();
        ArrayList<ArrayList<IPickerViewData>> options3Items_03 = new ArrayList<>();

        ArrayList<IPickerViewData> options3Items_01_01 = new ArrayList<>();
        options3Items_01_01.add(new PickerViewData("天河"));
        options3Items_01_01.add(new PickerViewData("黄埔"));
        options3Items_01_01.add(new PickerViewData("海珠"));
        options3Items_01_01.add(new PickerViewData("越秀"));
        options3Items_01.add(options3Items_01_01);

        ArrayList<IPickerViewData> options3Items_01_02 = new ArrayList<>();
        options3Items_01_02.add(new PickerViewData("南海"));
        options3Items_01_02.add(new PickerViewData("高明"));
        options3Items_01_02.add(new PickerViewData("禅城"));
        options3Items_01_02.add(new PickerViewData("桂城"));
        options3Items_01.add(options3Items_01_02);
        ArrayList<IPickerViewData> options3Items_01_03 = new ArrayList<>();
        options3Items_01_03.add(new PickerViewData("其他"));
        options3Items_01_03.add(new PickerViewData("常平"));
        options3Items_01_03.add(new PickerViewData("虎门"));
        options3Items_01.add(options3Items_01_03);

        ArrayList<IPickerViewData> options3Items_01_04 = new ArrayList<>();
        options3Items_01_04.add(new PickerViewData("其他"));
        options3Items_01_04.add(new PickerViewData("其他"));
        options3Items_01_04.add(new PickerViewData("其他"));
        options3Items_01.add(options3Items_01_04);

        ArrayList<IPickerViewData> options3Items_01_05 = new ArrayList<>();

        options3Items_01_05.add(new PickerViewData("其他1"));
        options3Items_01_05.add(new PickerViewData("其他2"));
        options3Items_01.add(options3Items_01_05);

        ArrayList<IPickerViewData> options3Items_02_01 = new ArrayList<>();
        options3Items_02_01.add(new PickerViewData("长沙1"));
        options3Items_02_01.add(new PickerViewData("长沙2"));
        options3Items_02_01.add(new PickerViewData("长沙3"));
        options3Items_02_01.add(new PickerViewData("长沙4"));
        options3Items_02_01.add(new PickerViewData("长沙5"));
        options3Items_02.add(options3Items_02_01);

        ArrayList<IPickerViewData> options3Items_02_02 = new ArrayList<>();
        options3Items_02_02.add(new PickerViewData("岳阳"));
        options3Items_02_02.add(new PickerViewData("岳阳1"));
        options3Items_02_02.add(new PickerViewData("岳阳2"));
        options3Items_02_02.add(new PickerViewData("岳阳3"));
        options3Items_02_02.add(new PickerViewData("岳阳4"));
        options3Items_02_02.add(new PickerViewData("岳阳5"));
        options3Items_02.add(options3Items_02_02);

        ArrayList<IPickerViewData> options3Items_03_01 = new ArrayList<>();
        options3Items_03_01.add(new PickerViewData("好山水"));
        options3Items_03.add(options3Items_03_01);

        options3Items.add(options3Items_01);
        options3Items.add(options3Items_02);
        options3Items.add(options3Items_03);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
