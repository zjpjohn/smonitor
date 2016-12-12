package com.harlan.smonitor.api.impl;

/**
 * Created by harlan on 2016/12/9.
 */
public class TypeValueDeclare {
    /**
     * type字段的值
     */
    private String typeValue;
    /**
     * 值的名称
     */
    private String name;
    /**
     * 该种type值的描述
     */
    private String desc;

    public String getTypeValue() {
        return typeValue;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

