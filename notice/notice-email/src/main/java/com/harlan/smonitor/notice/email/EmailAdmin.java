package com.harlan.smonitor.notice.email;

import com.harlan.smonitor.api.notice.Admin;

import java.util.Map;

/**
 * EmailAdmin
 * Created by harlan on 2016/12/9.
 */
public class EmailAdmin extends Admin {

    private String emailAddress;

    public String getEmailAddress() {
        return emailAddress;
    }



    @Override
    protected Map<String, Object> setAttrs(Map<String, Object> adminMap) {
        adminMap.put("emailAddress",emailAddress);
        return adminMap;
    }

    @Override
    protected void getAttrs(Map<String, Object> adminMap) {
        if(adminMap.get("emailAddress")!=null){
            this.emailAddress=adminMap.get("emailAddress").toString();
        }
    }
}
