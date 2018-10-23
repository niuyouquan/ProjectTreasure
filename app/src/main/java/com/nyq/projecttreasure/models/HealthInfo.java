package com.nyq.projecttreasure.models;

import java.io.Serializable;

/**
 * @desc:(健康资讯实体类)
 * @company:中国电信甘肃万维
 * @projectName:jkgs
 * @author:liufx
 * @CreateTime:2017/4/18 16:04
 */

public class HealthInfo implements Serializable {


    private String columnName;
    private String title;
    private String img;
    private String source;

    public HealthInfo(String title, String columnName, String source, String img) {
        this.columnName = columnName;
        this.title = title;
        this.img = img;
        this.source = source;

    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
