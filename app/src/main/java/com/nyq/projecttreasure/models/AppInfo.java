package com.nyq.projecttreasure.models;


import java.io.Serializable;

/**
 * @desc:(App对象)
 * @company:中国电信甘肃万维
 * @projectName:jkgs
 * @author:liufx
 * @CreateTime:2017/4/14 18:39
 */
public class AppInfo implements Serializable {
    private String name;
    private int img;

    public AppInfo(String name, int img) {
        this.name = name;
        this.img = img;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
