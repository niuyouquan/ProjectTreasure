package com.nyq.projecttreasure.models;

import java.io.Serializable;

/**
 * @desc:(健康资讯栏目)
 * @company:中国电信甘肃万维
 * @projectName:jkgs
 * @author:liufx
 * @CreateTime:2017/4/17 14:57
 */

public class InfoColumn implements Serializable {
    private String columnCode;
    private String columnName;
    private String columnType;

    public InfoColumn() {
    }

    public InfoColumn(String columnCode, String columnName, String columnType) {
        this.columnCode = columnCode;
        this.columnName = columnName;
        this.columnType = columnType;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }
}
