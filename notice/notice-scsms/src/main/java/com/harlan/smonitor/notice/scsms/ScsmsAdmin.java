package com.harlan.smonitor.notice.scsms;

import com.harlan.smonitor.api.notice.Admin;
import java.util.Map;

/**
 * ScsmsAdmin
 * Created by harlan on 2016/12/9.
 */
public class ScsmsAdmin extends Admin {
    private String phoneNo;

    @Override
    protected Map<String, Object> setAttrs(Map<String, Object> adminMap) {
        adminMap.put("phoneNo",phoneNo);
        return adminMap;
    }

    @Override
    protected void getAttrs(Map<String, Object> adminMap) {
        if(adminMap.get("phoneNo")!=null){
            this.phoneNo=adminMap.get("phoneNo").toString();
        }
    }

    public String getPhoneNo() {
        return phoneNo;
    }
}
