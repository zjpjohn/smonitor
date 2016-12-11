package com.harlan.smonitor.monitor.data;

import com.harlan.smonitor.api.Result;
import com.harlan.smonitor.api.notice.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * admin 数据操作
 * Created by harlan on 2016/12/5.
 */
public class AdminDao {
    private final static Logger logger = LoggerFactory.getLogger(AdminDao.class);


    public static Admin getAdmin(String adminId){
        return  CachedData.ADMIN_MAP.get(adminId);
    }
    public static List<Admin> getAdminList(){
        List<Admin> allAdmin=new ArrayList<Admin>(CachedData.ADMIN_MAP.size());
        for (Integer adminId: CachedData.ADMIN_MAP.keySet()) {
            allAdmin.add(CachedData.ADMIN_MAP.get(adminId));
        }
        return  allAdmin;
    }
    public static Result addAdmin(Admin admin) throws Exception {
        if(admin.getId()==null){
            admin.setId(DataOperator.nextAdminIndex());
        }
        CachedData.ADMIN_MAP.put(admin.getId(),admin);
        if(logger.isDebugEnabled()){
            for (Integer adminId: CachedData.ADMIN_MAP.keySet()) {
                logger.debug("adminId:{}",CachedData.ADMIN_MAP.get(adminId));
            }
        }
        DataOperator.saveAdmin(getAdminList());
        return  new Result();
    }


}
