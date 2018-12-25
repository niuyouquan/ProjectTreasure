package com.nyq.projecttreasure.models;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Created by niuyq on 2018/12/17.
 */

public class AreaInfo extends LitePalSupport implements Serializable{
    private String countryCode; //国家码
    private String country; //国家
    private String province; //省份
    private String citycode; //城市编码
    private String city; //城市
    private String district; //县（区）
    private String street; //街道
    private String addressInfo; //地址信息
    private Double lontitude;//经度
    private Double latitude; //纬度
    private float radius;  // 半径

    @Override
    public String toString() {
        return "AreaInfo{" +
                "countryCode='" + countryCode + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", citycode='" + citycode + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", street='" + street + '\'' +
                ", addressInfo='" + addressInfo + '\'' +
                ", lontitude='" + lontitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", radius='" + radius + '\'' +
                '}';
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
    }

    public Double getLontitude() {
        return lontitude;
    }

    public void setLontitude(Double lontitude) {
        this.lontitude = lontitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}