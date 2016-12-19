package com.harlan.smonitor.notice.scsms;

import com.harlan.smonitor.api.notice.Admin;
import java.util.Map;

/**
 * ScsmsAdmin
 * Created by harlan on 2016/12/9.
 */
public class ScsmsAdmin extends Admin {
    private String phoneNo;

    /***
     * 根据从map中取出属性，转化成对象

     * @param adminMap
    adminMap
     */
    public ScsmsAdmin(Map<String, Object> adminMap) {
        super(adminMap);
        if(adminMap.get("phoneNo")!=null){
            this.phoneNo=adminMap.get("phoneNo").toString();
        }
    }

    @Override
    protected Map<String, Object> setAttrs(Map<String, Object> adminMap) {
        adminMap.put("phoneNo",phoneNo);
        return adminMap;
    }

    public String getPhoneNo() {
        return phoneNo;
    }
}
