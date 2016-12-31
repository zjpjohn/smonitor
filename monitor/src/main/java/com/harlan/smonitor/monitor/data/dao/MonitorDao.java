package com.harlan.smonitor.monitor.data.dao;

import com.harlan.smonitor.api.Result;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.data.DataFileOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * MonitorDao
 * Created by harlan on 2016/12/5.
 */
public class MonitorDao {
    private final static Logger logger = LoggerFactory.getLogger(MonitorDao.class);


    public static List<MonitorItem> getMonitorItemList(Object start_str, Object limit_str){
        int start,limit;
        if(start_str==null){
            start=0;
        }else {
            start=Integer.valueOf(start_str.toString());
        }
        if(limit_str==null){
            limit=CachedData.monitorItemSize();
        }else{
            limit=Integer.valueOf(limit_str.toString());
        }
        List<MonitorItem> qryMonitorItemList=new LinkedList<MonitorItem>();
        List<MonitorItem> allMonitorItemList=CachedData.getAllMonitorItem();
        for (int i = 0; i <allMonitorItemList.size(); i++) {
            if(i>=start && i< (start+limit)){
                qryMonitorItemList.add(allMonitorItemList.get(i));
            }
        }
        return  qryMonitorItemList;
    }

    public static MonitorItem getMonitor(Integer id){
        MonitorItem qry_monitor=null;
        for (MonitorItem monitor:CachedData.getAllMonitorItem()) {
            if(monitor.getId().equals(id)){
                qry_monitor=monitor;
                break;
            }
        }
        return qry_monitor;
    }

    /**
     * 添加到内存
     * @param monitorItem
     * @return
     * @throws Exception
     */
    public static Result addMonitor(MonitorItem monitorItem) throws Exception {
        CachedData.putMonitorItem(monitorItem);
        return  new Result();
    }
    public static int count(){
        return CachedData.monitorItemSize();
    }

    /**
     * 保存到文件
     * @return
     * @throws Exception
     */
    public static Result saveMonitorItem() throws Exception {
        DataFileOperator.saveMonitor(CachedData.getAllMonitorItem());
        return  new Result();
    }
}
