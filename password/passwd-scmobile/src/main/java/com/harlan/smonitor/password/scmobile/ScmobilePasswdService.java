package com.harlan.smonitor.password.scmobile;

import com.harlan.smonitor.api.password.IPasswdService;

/**
 * Created by harlan on 2016/11/22.
 */
public class ScmobilePasswdService implements IPasswdService{

    public String getPassword(String tag) {
        return null;
    }

    public String getName() {
        return "四川移动加密模块";
    }

    public String getId() {
        return "scmonile";
    }
}
