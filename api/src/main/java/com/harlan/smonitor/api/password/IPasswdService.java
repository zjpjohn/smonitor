package com.harlan.smonitor.api.password;

import com.harlan.smonitor.api.Implementor;

/**
 * 密码获取方法
 * Created by harlan on 2016/11/22.
 */
public interface IPasswdService extends Implementor {
    String getPassword(String tag);
}
