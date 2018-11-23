package com.nyq.projecttreasure.models;


import com.contrarywind.interfaces.IPickerViewData;

public class CityBean implements IPickerViewData {
    private String city;

    public CityBean(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getPickerViewText() {
        return city;
    }
}
