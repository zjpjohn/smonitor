package com.harlan.smonitor.api.impl;

/**
 * Created by harlan on 2016/12/9.
 */
public class FieldDeclare {
    /**
     * 字段名
     */
    private String fieldNmae;
    /**
     * 字段展示名称
     */
    private String name;
    /**
     * 字段填写备注
     */
    private String desc;

    public String getFieldNmae() {
        return fieldNmae;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public void setFieldNmae(String fieldNmae) {
        this.fieldNmae = fieldNmae;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
