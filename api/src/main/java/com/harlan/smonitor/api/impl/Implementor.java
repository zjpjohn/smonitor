package com.harlan.smonitor.api.impl;

/**
 * 所有定制模块需要实现此接口
 * Created by harlan on 2016/12/7.
 */
public interface Implementor {
    /**
     * 实现本方法，可以让该TYPE类型，在页面上有对应的解释，方便管理员清楚 模块作用等信息
     * @return FieldDeclare 对象用于表示web页面上需要说明的信息
     */
    TypeDeclare getTypeDeclare();
}
