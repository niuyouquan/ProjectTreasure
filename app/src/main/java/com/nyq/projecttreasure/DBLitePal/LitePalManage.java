package com.nyq.projecttreasure.DBLitePal;

import com.baidu.location.BDLocation;
import com.nyq.projecttreasure.models.AreaInfo;

import org.litepal.LitePal;

import java.util.List;

/**
 * Created by Administrator on 2018/12/17.
 */

public class LitePalManage {
    private static LitePalManage manage;

    public static LitePalManage getIntance() {
        if (manage == null) {
            synchronized (LitePalManage.class) {
                if (manage == null) {
                    manage = new LitePalManage();
                }
            }
        }
        return manage;
    }

    /**
     * 查询城市的信息
     * @param cityCode
     * @return
     */
    public AreaInfo getAreaInfo(String cityCode) {
        AreaInfo areaInfo = null;
        List<AreaInfo>  areaInfoList= LitePal.where("cityCode='" + cityCode + "'").find(AreaInfo.class);
        if (areaInfoList != null && areaInfoList.size() > 0) {
            areaInfo = areaInfoList.get(0);
        }
        return areaInfo;
    }

    /**
     * 更新城市信息
     * @param location
     */
    public void updateAreaInfo(BDLocation location) {
        AreaInfo areaInfoVo = new AreaInfo();
        areaInfoVo.setCountryCode(location.getCountryCode());
        areaInfoVo.setCountry(location.getCountry());
        areaInfoVo.setProvince(location.getProvince());
        areaInfoVo.setCitycode(location.getCityCode());
        areaInfoVo.setCity(location.getCity());
        areaInfoVo.setDistrict(location.getDistrict());
        areaInfoVo.setStreet(location.getStreet());
        areaInfoVo.setAddressInfo(location.getAddrStr());
        areaInfoVo.setLontitude(location.getLongitude());
        areaInfoVo.setLatitude(location.getLatitude());
        areaInfoVo.updateAll("cityCode='" + location.getCityCode() + "'");
    }
}
