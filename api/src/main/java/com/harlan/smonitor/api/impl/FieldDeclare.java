package com.harlan.smonitor.api.impl;

/**
 * FieldDeclare
 * Created by harlan on 2016/12/9.
 */
public class FieldDeclare {
    public FieldDeclare(String fieldName, String name, String desc) {
        this.fieldName = fieldName;
        this.name = name;
        this.desc = desc;
    }

    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 字段展示名称
     */
    private String name;
    /**
     * 字段填写备注
     */
    private String desc;

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
