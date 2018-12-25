package com.nyq.projecttreasure.activitys.baidumap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.base.BaseActivity;
import com.nyq.projecttreasure.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaiDuMapActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.mmap)
    MapView mmap;
    @BindView(R.id.rb_pt)
    RadioButton rbPt;
    @BindView(R.id.rb_wx)
    RadioButton rbWx;
    @BindView(R.id.rg_group)
    RadioGroup rgGroup;
    @BindView(R.id.btn_refresh)
    ImageView btnRefresh;
    @BindView(R.id.btn_locale)
    ImageView btnLocale;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.tv_about_search)
    TextView tvAboutSearch;

    private BaiduMap baiduMap;
    // 构造定位数据
    private MyLocationData locData = null;
    private boolean isFirstLoc = true; // 是否首次定位
    private double currentLatitude, currentLongitude;
    private BaiDuLocationReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_du_map);
        ButterKnife.bind(this);
        setStatusBarColor(R.color.colorPrimary);
        initMap();
    }

    public void initMap() {
        baiduMap = mmap.getMap();
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        //开启交通图
        baiduMap.setTrafficEnabled(true);
        //开启热力图
        //baiduMap.setBaiduHeatMapEnabled(true);

        receiver = new BaiDuLocationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_LOCATION);
        registerReceiver(receiver, intentFilter);

//        LocationMode.FOLLOWING;//定位跟随态
//        LocationMode.NORMAL;   //默认为 LocationMode.NORMAL 普通态
//        LocationMode.COMPASS;  //定位罗盘态
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,
                true, null));
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_pt:
                        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                        break;
                    case R.id.rb_wx:
                        baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                        break;
                    default:
                        break;
                }
            }
        });

        tvSearch.setOnClickListener(this);
        btnLocale.setOnClickListener(this);
        tvAboutSearch.setOnClickListener(this);

    }

    public class BaiDuLocationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            BDLocation location = intent.getParcelableExtra("location");
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            baiduMap.setMyLocationData(locData);
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }

    public void getMyLocation() {
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.setMapStatus(msu);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_locale:
                getMyLocation();
                break;
            case R.id.tv_search:
                getMyLocation();
                break;
            case R.id.tv_about_search:
                intent.setClass(activity,BaiDuAboutSearchActivity.class);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mmap.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mmap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mmap.onPause();
    }

}
