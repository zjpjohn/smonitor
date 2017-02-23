package com.harlan.smonitor.monitor.data.dao;

import com.harlan.smonitor.api.notice.Admin;
import com.harlan.smonitor.api.notice.INoticeService;
import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.Group;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.core.init.ModuleRegister;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 部分数据存在内存中，类似数据库
 * Created by harlan on 2016/11/24.
 */
class CachedData {
    private final static Logger logger = LoggerFactory.getLogger(CachedData.class);
    static{
        ADMIN_MAP=new LinkedHashMap<String, Admin>();
        GROUP_MAP=new LinkedHashMap<Integer, Group>();
        MONITOR_MAP=new LinkedHashMap<Integer,MonitorItem>();
        MONITOR_INDEX=0;
        CHECK_INDEX=0;
        GROUP_INDEX=0;
    }
    //监控项的序列
    private static int MONITOR_INDEX;
    //检查项的序列
    private static int CHECK_INDEX;
    //分组的序列
    private static int GROUP_INDEX;
    //通讯录，储存所有管理员
    private  static Map<String, Admin> ADMIN_MAP;
    //所有监控任务列表
    private static Map<Integer, MonitorItem> MONITOR_MAP;
    //分类
    private static Map<Integer, Group> GROUP_MAP;
    //调度管理对象
    static Scheduler SCHEDULER;

    /**
     * ==================================================
     * admin
     * ==================================================
     */
    /**
     * 添加admin，自带索引
     * @param admin 需要添加的对象
     */
    public static void putAdmin(Admin admin) {
        ADMIN_MAP.put(admin.getId(),admin);
    }
    public static Integer adminSize() {
        return ADMIN_MAP.size();
    }
    public static Admin getAdmin(String id) {
        if(ADMIN_MAP.get(id)!=null){
            return copyAdmin(ADMIN_MAP.get(id));
        }else{
            return null;
        }
    }
    public static List<Admin> getAllAdmin() {
        List<Admin> admin_list=new ArrayList<Admin>(ADMIN_MAP.size());
        Iterator it=ADMIN_MAP.keySet().iterator();
        while(it.hasNext()){
            String id= (String) it.next();
            Admin admin=ADMIN_MAP.get(id);

            Admin  copyAdmin=copyAdmin(admin);
            admin_list.add(copyAdmin);
        }
        return admin_list;
    }

    /**
     * 复制对象，防止后续业务侧修改了属性，影响原始数据
     * @param admin 原对象
     * @return 复制后的对象
     */
    private static Admin copyAdmin(Admin admin) {
        try {
            INoticeService service=ModuleRegister.getNoticeServiceImpl(admin.getType());
            Class<?> adminClass=service.getTypeDeclare().getBeanClass();
            Admin  copyAdmin = (Admin) adminClass.newInstance();
            copyAdmin.init(admin.createMap());
            return copyAdmin;
        }  catch (Exception e) {
            logger.error("admin复制对象异常",e);
        }
        return null;
    }
    public static void removeAdmin(String admin) {
        ADMIN_MAP.remove(admin);
    }

    /**
     * ==================================================
     * group
     * ==================================================
     */
    /**
     * group，自带索引
     * @param group
     */
    public static void putGroup(Group group) {
        if(group.getId()==null){
            group.setId(++GROUP_INDEX);
        }
        GROUP_MAP.put(group.getId(),group);
        //如果group中存的id大于现在的index，需要调整index
        if(group.getId()>GROUP_INDEX){
            GROUP_INDEX=group.getId();
        }
    }
    public static Integer groupSize() {
        return GROUP_MAP.size();
    }
    public static Group getGroup(Integer id) {
        return copyGroup(GROUP_MAP.get(id));
    }
    public static List<Group> getAllGroup() {
        List<Group> group_list=new ArrayList<Group>(GROUP_MAP.size());
        Iterator it=GROUP_MAP.keySet().iterator();
        while(it.hasNext()){
            Integer id= (Integer) it.next();
            Group group=GROUP_MAP.get(id);
            group_list.add(copyGroup(group));
        }
        return group_list;
    }

    private static Group copyGroup(Group group) {
        Group copyGroup=new Group();
        copyGroup.setName(group.getName());
        copyGroup.setId(group.getId());
        return copyGroup;
    }
    /**
     * ==================================================
     * Monitor
     * ==================================================
     */
    public static List<MonitorItem> getAllMonitorItem() {
        //TODO 需要排序，可以在修改、初始化时排序一下
        List<MonitorItem> monitor_list=new ArrayList<MonitorItem>(MONITOR_MAP.size());
        for (MonitorItem monitor:MONITOR_MAP.values()) {
            MonitorItem copyMonitor=monitor.clone();
            monitor_list.add(copyMonitor);
        }
        return monitor_list;
    }

    public static int monitorItemSize() {
        return MONITOR_MAP.size();
    }

    /**
     * put方法是直接放到内存
     * 所以在放入内存后，尽量不要修改入参
     * @param monitorItem
     */
    public static void putMonitorItem(MonitorItem monitorItem) {
        if(monitorItem.getId()==null){
            monitorItem.setId(++MONITOR_INDEX);
        }
        if(CachedData.MONITOR_INDEX < monitorItem.getId()){
            CachedData.MONITOR_INDEX=monitorItem.getId();
        }
        for (CheckItem item:monitorItem.getCheckList()) {
            if(item.getId()==null){
                item.setId(++CHECK_INDEX);
            }
            if(CachedData.CHECK_INDEX < item.getId()){
                CachedData.CHECK_INDEX=item.getId();
            }
        }
        MONITOR_MAP.put(monitorItem.getId(),monitorItem);
    }
    public static void removeMonitor(Integer monitorId){
        MONITOR_MAP.remove(monitorId);
    }

    /**
     * 获得的monitor是复制的对象，修改不会影响内存中存储的monitor
     * @param monitorId
     * @return
     */
    public static MonitorItem getMonitor(Integer monitorId){
        return MONITOR_MAP.get(monitorId).clone();
    }


}
