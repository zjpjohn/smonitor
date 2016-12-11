package com.harlan.smonitor.api;

/**
 * 所有定制模块需要实现此接口
 * Created by harlan on 2016/12/7.
 */
public interface Implementor {
    /**
     * 本模块的功能
     * 用于页面展示说明等
     * @return
     */
    String getName();

    /**
     * 本模块的唯一标识
     * @return
     */
    String getId();
}
