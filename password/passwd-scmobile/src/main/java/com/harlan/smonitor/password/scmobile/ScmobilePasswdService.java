package com.harlan.smonitor.password.scmobile;

import com.harlan.smonitor.api.impl.TypeDeclare;
import com.harlan.smonitor.api.password.IPasswdService;

/**
 * Created by harlan on 2016/11/22.
 */
public class ScmobilePasswdService implements IPasswdService{

    public String getPassword(String tag) {
        return null;
    }

    @Override
    public TypeDeclare getTypeDeclare() {
        TypeDeclare type =new TypeDeclare();
        type.setTypeValue("scmonile");
        type.setName("四川移动加密模块");
        type.setDesc("四川移动加密模块");
        return type;
    }
}
