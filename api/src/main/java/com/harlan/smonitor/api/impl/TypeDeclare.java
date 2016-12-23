package com.harlan.smonitor.api.impl;

/**
 * 一个 TypeDeclare 对象表示一个 type值和其解释、属性、对应的实体bean
 * Created by harlan on 2016/12/9.
 */
public class TypeDeclare {
    /**
     * type字段的值
     */
    private String typeValue;
    /**
     * type字段的名称
     */
    private String name;
    /**
     * 该种type值的描述和解释
     */
    private String desc;

    /**
     * 每种type都应该对应一个该类型的bean
     */
    private Class<?> beanClass;

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

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
}

