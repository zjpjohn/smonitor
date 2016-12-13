package com.harlan.smonitor.notice.email;

import com.harlan.smonitor.api.notice.Admin;

import java.util.Map;

/**
 * Created by harlan on 2016/12/9.
 */
public class EmailAdmin extends Admin {

    private String emailAddress;

    public String getEmailAddress() {
        return emailAddress;
    }

    /***
     * 根据从map中取出属性，转化成对象

     * @param adminMap
    adminMap
     */
    public EmailAdmin(Map<String, Object> adminMap) {
        super(adminMap);
        if(adminMap.get("emailAddress")!=null){
            this.emailAddress=adminMap.get("emailAddress").toString();
        }
    }

    @Override
    protected Map<String, Object> setAttrs(Map<String, Object> adminMap) {
        adminMap.put("emailAddress",emailAddress);
        return adminMap;
    }
}
