package com.harlan.smonitor.monitor.bean;

import com.harlan.smonitor.monitor.bean.host.HostMonitorItem;
import com.harlan.smonitor.monitor.bean.http.HttpMonitorItem;
import com.harlan.smonitor.monitor.bean.inspection.InspectionMonitorItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 监控项
 * 监控项的概念是指能够抽象成一个连接的东西：
 * 如：一个主机的某个帐号可以检查的所有东西，如：文件、内存、cpu等等
 * 如：数据库oracle的一次连接，mongodb的一次连接
 * @author harlan
 */
public abstract class MonitorItem{
    private final static Logger logger = LoggerFactory.getLogger(MonitorItem.class);
    private static Map<String,Class<?>> IMPL_MAP;
    static {
        IMPL_MAP=new HashMap<String, Class<?>>();
        IMPL_MAP.put("host",HostMonitorItem.class);
        IMPL_MAP.put("http",HttpMonitorItem.class);
        IMPL_MAP.put("inspection",InspectionMonitorItem.class);
    }
    /**
     * 监控项描述
     */
    protected String name;
    /**
     * 类型
     */
    protected String type;

    /**
     * 类型
     */
    protected Integer groupId;

    /**
     * 通知对象
     */
    protected List<CheckItem> checkList;
    /**
     * java bean → map
     * @return
     */
    public Map<String,Object> createMap(){
        Map<String,Object> item_map=new HashMap<String,Object>();
        item_map.put("name",name);
        item_map.put("type",type);
        item_map.put("groupId",groupId.toString());
        List<Map<String,Object>> checkListMap=new ArrayList<Map<String,Object>>();
        for (CheckItem checkItem: checkList) {
            checkListMap.add(checkItem.createMap());
        }
        item_map.put("checkList",checkListMap);
        item_map=setProps(item_map);
        return item_map;
    }

    /**
     * 工厂类
     * @param itemMap json转化成的map，或者页面请求map
     * @return MonitorItem
     */
    public static MonitorItem createMonitor(Map<String,Object> itemMap) throws Exception {
        String type=itemMap.get("type").toString();
        logger.debug("需要实例化的监控项 type:{}",type);
        Class<?> implClass= IMPL_MAP.get(type);
        if(implClass==null){
            throw new RuntimeException("MonitorItem.createMonitor()方法中，没有配置该类型监控项:"+type);
        }
        MonitorItem item= (MonitorItem) implClass.newInstance();
        item.init(itemMap);
        return item;
    }

    /**
     * map → java bean
     * @param itemMap
     */
    @SuppressWarnings("unchecked")
    private void init(Map<String, Object> itemMap) {
        logger.debug("初始化公共参数...");
        name =itemMap.get("name").toString();
        type =itemMap.get("type").toString();
        groupId=Integer.valueOf(itemMap.get("groupId").toString());
        List<Map<String,Object>> checkListMap = (List<Map<String, Object>>) itemMap.get("checkList");
        checkList=new ArrayList<CheckItem>();
        for (Map<String,Object> map:checkListMap) {
            CheckItem checkItem=createCheck(map);
            checkList.add(checkItem);
        }
        logger.info("name={}的监控项，共配置了{}个检查项",name,checkList.size());
        getProps(itemMap);
    }


    /**
     * 各个监控项实现类需要实现checkItem的工厂方法
     * @param checkMap check map 节点
     * @return CheckItem对象
     */
    protected abstract CheckItem createCheck(Map<String,Object> checkMap);

    /**
     * bean转化成json对象时
     * 个监控项实现类从需要将个性字段放入itemMap
     * @return itemMap
     */
    protected abstract Map<String,Object> setProps(Map<String,Object> itemMap);
    protected abstract void getProps(Map<String,Object> itemMap);
    public String getName() {
        return name;
    }

    public List<CheckItem> getCheckList() {
        return checkList;
    }



    public static  boolean checkTime(String default_run_time){
        String[] checkTimeArray = default_run_time.split("\\s+");
        if (checkTimeArray.length != 6 && checkTimeArray.length != 7) {
            return false;
        }
        return true;
    }
}