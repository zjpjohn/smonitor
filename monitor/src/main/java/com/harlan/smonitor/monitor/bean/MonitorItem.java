package com.harlan.smonitor.monitor.bean;

import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.api.impl.TypeDeclare;
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
public abstract class MonitorItem implements Cloneable{
    private final static Logger logger = LoggerFactory.getLogger(MonitorItem.class);
    private static Map<String,TypeDeclare> TYPE_MAP;
    static {
        TYPE_MAP=new HashMap<String, TypeDeclare>();
        TypeDeclare host=new TypeDeclare();
        host.setTypeValue("host");
        host.setName("主机");
        host.setDesc("可监控主机cpu、磁盘、文件修改时间等");
        host.setBeanClass(HostMonitorItem.class);
        TYPE_MAP.put(host.getTypeValue(),host);
        TypeDeclare http=new TypeDeclare();
        http.setTypeValue("http");
        http.setName("http服务");
        http.setDesc("可监控http服务");
        http.setBeanClass(HttpMonitorItem.class);
        TYPE_MAP.put(http.getTypeValue(),http);
        TypeDeclare inspection=new TypeDeclare();
        inspection.setTypeValue("inspection");
        inspection.setName("自检");
        inspection.setDesc("可检查系统本身情况");
        inspection.setBeanClass(InspectionMonitorItem.class);
        TYPE_MAP.put(inspection.getTypeValue(),inspection);
    }
    /**
     * 监控项id
     */
    protected Integer id;
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
     * 联系方式
     */
    protected List<String> adminList;

    /**
     * map → java bean
     * @param itemMap
     */
    @SuppressWarnings("unchecked")
    public void init(Map<String, Object> itemMap) throws Exception{
        if(itemMap.get("id")!=null){
            id=Integer.valueOf(itemMap.get("id").toString());
        }
        type =itemMap.get("type").toString();
        name =itemMap.get("name").toString();
        groupId=Integer.valueOf(itemMap.get("groupId").toString());
        adminList = (List<String>) itemMap.get("adminList");
        if(itemMap.get("checkList")!=null){
            List<Map<String,Object>> checkListMap = (List<Map<String, Object>>) itemMap.get("checkList");
            checkList=new ArrayList<CheckItem>();
            for (Map<String,Object> map:checkListMap) {
                String cType=map.get("type").toString();
                CheckItem checkItem=checkInstance(cType);
                checkItem.init(map);
                checkList.add(checkItem);
            }
        }
        getProps(itemMap);
    }
    /**
     * java bean → map
     * 直接使用一个map方便之后字段拼装map
     * @return
     */
    public Map<String,Object> createMap()throws Exception{
        Map<String,Object> item_map=new HashMap<String,Object>();
        item_map.put("id",id);
        item_map.put("name",name);
        item_map.put("type",type);
        item_map.put("groupId",groupId.toString());
        item_map.put("adminList",adminList);
        List<Map<String,Object>> checkListMap=new ArrayList<Map<String,Object>>();
        for (CheckItem checkItem: checkList) {
            checkListMap.add(checkItem.createMap());
        }
        item_map.put("checkList",checkListMap);
        item_map=setProps(item_map);
        return item_map;
    }

    public static Collection<TypeDeclare> getTypes(){
        return TYPE_MAP.values();
    }
    public static TypeDeclare getType(String type){
        return TYPE_MAP.get(type);
    }

    public Collection<TypeDeclare> getCheckTypes(){
        return getCheckTypeMap().values();
    }

    /**
     * 返回所有该类型monitor自定义的字段
     * @return
     */
    public abstract List<FieldDeclare> getFields();

    public  static MonitorItem monitorInstance(String type)throws Exception{
        Class<?> implClass= TYPE_MAP.get(type).getBeanClass();
        if(implClass==null){
            throw new RuntimeException("MonitorItem.createMonitor()方法中，没有配置该类型监控项:"+type);
        }
        MonitorItem item = (MonitorItem)implClass.newInstance();
        return item;
    }
    public CheckItem checkInstance(String checkType)throws Exception{
        Class<?> implClass= getCheckTypeMap().get(checkType).getBeanClass();
        if(implClass==null){
            throw new RuntimeException("MonitorItem.getCheckClassMap()方法中，没有配置该类型监控项:"+type);
        }
        return (CheckItem)implClass.newInstance();
    }

    /**
     * 各个监控项实现类需要实现此方法
     * 每个类型的监控项都需要返回一个TypeDeclare的map
     * @return CheckItem对象
     */
    public abstract Map<String,TypeDeclare> getCheckTypeMap();

    /**
     * bean转化成json对象时调用此方法
     * 用于  1、存文件，2、传到页面展示
     * 所以每个监控项的实现类需要将个性字段放入itemMap中，以便可展示和保存字段值
     * @return itemMap
     */
    protected abstract Map<String,Object> setProps(Map<String,Object> itemMap)throws Exception;

    /**
     * json转化成bean时调用此方法
     * 用于：1、从文件中读取数据  2、页面参数传值过来保存
     * 所以每个监控项的实现类需要将个性字段从itemMap中取出，以便可使用
     * @param itemMap
     */
    protected abstract void getProps(Map<String,Object> itemMap) throws Exception;
    public String getName() {
        return name;
    }

    public List<CheckItem> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<CheckItem> checkList) {
        this.checkList = checkList;
    }

    public String getType() {
        return type;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getAdminList() {
        return adminList;
    }

    public void setAdminList(List<String> adminList) {
        this.adminList = adminList;
    }

    @Override
    public MonitorItem clone(){
        MonitorItem monitor=null;
            try {
                monitor=(MonitorItem)super.clone();
                //复制admingList
                List<String> copyAdminList=new ArrayList<String>(monitor.getAdminList().size());
                copyAdminList.addAll(monitor.getAdminList());
                monitor.setAdminList(copyAdminList);
                //复制checkList
                List<CheckItem> copyCheckList=new ArrayList<CheckItem>(monitor.getCheckList().size());
                for (CheckItem check:monitor.getCheckList()) {
                    copyCheckList.add(check.clone());
                }
                monitor.setCheckList(copyCheckList);
            } catch (CloneNotSupportedException e) {
                logger.error("复制异常",e);
            }
        return monitor;
    }
}