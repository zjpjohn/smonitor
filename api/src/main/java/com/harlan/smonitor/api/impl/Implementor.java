package com.harlan.smonitor.api.impl;

/**
 * 所有定制模块需要实现此接口
 * Created by harlan on 2016/12/7.
 */
public interface Implementor {
    /**
     * 实现本方法，返回一个TypeDeclare类型，表示该模块的类型，解释和实现bean等
     * @return FieldDeclare 对象用于表示web页面上需要说明的信息
     */
    TypeDeclare getTypeDeclare();
}
