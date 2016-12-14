package com.harlan.smonitor.monitor.data.dao;

import com.harlan.smonitor.api.Result;
import com.harlan.smonitor.api.notice.Admin;
import com.harlan.smonitor.monitor.data.DataFileOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * admin 数据操作
 * Created by harlan on 2016/12/5.
 */
public class AdminDao {
    private final static Logger logger = LoggerFactory.getLogger(AdminDao.class);


    public static Admin getAdmin(Integer adminId){
        return  CachedData.getAdmin(adminId);
    }

    public static List<Admin> getAdminList(Object start_str,Object limit_str){
        int start,limit;
        if(start_str==null){
            start=0;
        }else {
            start=Integer.valueOf(start_str.toString());
        }
        if(limit_str==null){
            limit=CachedData.adminSize();
        }else{
            limit=Integer.valueOf(limit_str.toString());
        }
        List<Admin> qryAdminList=new LinkedList<Admin>();
        List<Admin> allAdminList=CachedData.getAllAdmin();
        for (int i = 0; i <allAdminList.size(); i++) {
            if(i>=start && i< (start+limit)){
                qryAdminList.add(allAdminList.get(i));
            }
        }
        return  qryAdminList;
    }
    public static Result addAdmin(Admin admin) throws Exception {
        CachedData.putAdmin(admin);
        return  new Result();
    }
    public static int count(){
        return CachedData.adminSize();
    }
    public static Result saveAdmin() throws Exception {
        DataFileOperator.saveAdmin(CachedData.getAllAdmin());
        return  new Result();
    }
}
