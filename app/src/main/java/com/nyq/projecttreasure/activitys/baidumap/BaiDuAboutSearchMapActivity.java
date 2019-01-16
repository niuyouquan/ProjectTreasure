package com.nyq.projecttreasure.activitys.baidumap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.nyq.projecttreasure.R;
import com.nyq.projecttreasure.activitys.baidumap.overlayutil.PoiOverlay;
import com.nyq.projecttreasure.base.BaseActivity;
import com.nyq.projecttreasure.popupwindow.AddressPopup;
import com.nyq.projecttreasure.utils.AGCache;
import com.nyq.projecttreasure.utils.Constant;
import com.nyq.projecttreasure.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaiDuAboutSearchMapActivity extends BaseActivity implements View.OnClickListener, OnGetPoiSearchResultListener {

    @BindView(R.id.mmap)
    MapView mmap;
    @BindView(R.id.btn_refresh)
    ImageView btnRefresh;
    @BindView(R.id.btn_locale)
    ImageView btnLocale;
    @BindView(R.id.tv_about_search)
    TextView tvAboutSearch;
    @BindView(R.id.rl_layout)
    RelativeLayout rlLayout;

    private BaiDuLocationReceiver receiver;
    // 构造定位数据
    private MyLocationData locData = null;
    private boolean isFirstLoc = true; // 是否首次定位
    private double currentLatitude, currentLongitude;
    private BaiduMap baiduMap;
    private String keyWords;
    private PoiSearch mPoiSearch = null;
    private int searchType = 0;  // 搜索的类型，在显示时区分
    private LatLng southwest = new LatLng(39.92235, 116.380338);
    private LatLng northeast = new LatLng(39.947246, 116.414977);
    private LatLngBounds searchBound = new LatLngBounds.Builder().include(southwest).include(northeast).build();

    private AddressPopup addressPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_du_about_search_map);
        ButterKnife.bind(this);
        setStatusBarColor(R.color.colorPrimary);
        keyWords = getIntent().getStringExtra("keyWords");
        initMap();
    }

    public void initMap() {
        baiduMap = mmap.getMap();
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        //开启交通图
        baiduMap.setTrafficEnabled(true);
        receiver = new BaiDuLocationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_LOCATION);
        registerReceiver(receiver, intentFilter);

        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_geo);
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,
                true, mCurrentMarker));

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.setMapStatus(msu);

        mPoiSearch = PoiSearch.newInstance();// 创建对象
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        btnLocale.setOnClickListener(this);

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

                searchNearbyProcess();
            }
        }
    }

    /**
     * 响应周边搜索
     */
    public void searchNearbyProcess() {
        searchType = 2;
        LatLng center = new LatLng(currentLatitude, currentLongitude);
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption()
                .keyword(keyWords)
                .sortType(PoiSortType.distance_from_near_to_far)
                .location(center)
                .radius(500)
                .pageNum(0)
                .scope(1);

        mPoiSearch.searchNearby(nearbySearchOption);
    }

    /**
     * 响应城市内搜索
     */
    public void searchButtonProcess() {
        searchType = 1;
        String citystr = AGCache.CITY_NAME;
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(citystr)
                .keyword(keyWords)
                .pageNum(0)
                .scope(1));
    }

    /**
     * 响应区域搜索按钮点击事件
     */
    public void searchBoundProcess(View v) {
        searchType = 3;
        mPoiSearch.searchInBound(new PoiBoundSearchOption()
                .bound(searchBound)
                .keyword(keyWords)
                .scope(1));

    }

    public void getMyLocation() {
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.setMapStatus(msu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_locale:
                getMyLocation();
                break;
            case R.id.tv_about_search:
                getMyLocation();
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            ToastUtil.refreshToast(activity, "未找到结果", Toast.LENGTH_LONG);
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            baiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(baiduMap);
            baiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();
            LatLng center = new LatLng(currentLatitude, currentLongitude);
            switch (searchType) {
                case 2:
                    showNearbyArea(center, 500);
                    break;
                case 3:
                    showBound(searchBound);
                    break;
                default:
                    break;
            }
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";

            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }

            strInfo += "找到结果";
            ToastUtil.refreshToast(activity, strInfo, Toast.LENGTH_LONG);
        }

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            ToastUtil.refreshToast(activity, "抱歉，未找到结果", Toast.LENGTH_SHORT);
        } else {
            ToastUtil.refreshToast(activity, result.getName() + "：" + result.getAddress(), Toast.LENGTH_SHORT);
            addressPopup = new AddressPopup(activity, result.getName() + "：\n" + result.getAddress());
            addressPopup.showAtLocation(rlLayout,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
        if (poiDetailSearchResult.error != SearchResult.ERRORNO.NO_ERROR) {
            ToastUtil.refreshToast(activity, "抱歉，未找到结果", Toast.LENGTH_SHORT);
        } else {
            List<PoiDetailInfo> poiDetailInfoList = poiDetailSearchResult.getPoiDetailInfoList();
            if (null == poiDetailInfoList || poiDetailInfoList.isEmpty()) {
                ToastUtil.refreshToast(activity, "抱歉，检索结果为空", Toast.LENGTH_SHORT);
                return;
            }

            for (int i = 0; i < poiDetailInfoList.size(); i++) {
                PoiDetailInfo poiDetailInfo = poiDetailInfoList.get(i);
                if (null != poiDetailInfo) {
                    ToastUtil.refreshToast(activity,
                            poiDetailInfo.getName() + ": " + poiDetailInfo.getAddress(),
                            Toast.LENGTH_SHORT);
                }
            }
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    private class MyPoiOverlay extends PoiOverlay {
        MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poi.uid));
            // }
            return true;
        }
    }

    /**
     * 对周边检索的范围进行绘制
     *
     * @param center 周边检索中心点坐标
     * @param radius 周边检索半径，单位米
     */
    public void showNearbyArea(LatLng center, int radius) {
        BitmapDescriptor centerBitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_geo);
        MarkerOptions ooMarker = new MarkerOptions().position(center).icon(centerBitmap);
        baiduMap.addOverlay(ooMarker);

        OverlayOptions ooCircle = new CircleOptions().fillColor(0x00000000)
                .center(center)
                .stroke(new Stroke(5, 0xff3fb9ff))
                .radius(radius);

        baiduMap.addOverlay(ooCircle);
    }

    /**
     * 对区域检索的范围进行绘制
     *
     * @param bounds 区域检索指定区域
     */
    public void showBound(LatLngBounds bounds) {
        BitmapDescriptor bdGround = BitmapDescriptorFactory.fromResource(R.mipmap.ground_overlay);

        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds)
                .image(bdGround)
                .transparency(0.8f)
                .zIndex(1);

        baiduMap.addOverlay(ooGround);

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(bounds.getCenter());
        baiduMap.setMapStatus(u);

        bdGround.recycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mmap.onDestroy();
        mPoiSearch.destroy();
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
