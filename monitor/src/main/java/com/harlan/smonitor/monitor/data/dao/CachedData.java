package com.harlan.smonitor.monitor.data.dao;

import com.harlan.smonitor.api.notice.Admin;
import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.Group;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import org.quartz.Scheduler;

import java.util.*;

/**
 * 部分数据存在内存中，类似数据库
 * Created by harlan on 2016/11/24.
 */
class CachedData {
    static{
        ADMIN_MAP=new LinkedHashMap<Integer, Admin>();
        GROUP_MAP=new LinkedHashMap<Integer, Group>();
        MONITOR_LIST=new LinkedList<MonitorItem>();
        JOB_ALARM_COUNT=new HashMap<Integer, Integer>();
        MONITOR_INDEX=0;
        CHECK_INDEX=0;
        ADMIN_INDEX=0;
        GROUP_INDEX=0;
    }
    //监控项的序列
    private static int MONITOR_INDEX;
    //检查项的序列
    private static int CHECK_INDEX;
    //管理员的序列
    private static int ADMIN_INDEX;
    //分组的序列
    private static int GROUP_INDEX;
    //通讯录，储存所有管理员
    private  static Map<Integer, Admin> ADMIN_MAP;
    //所有监控任务列表
    private static List<MonitorItem> MONITOR_LIST;
    //分类
    private static Map<Integer, Group> GROUP_MAP;
    //调度管理对象
    static Scheduler SCHEDULER;
    //每个job对应一个报警计数器
    static Map<Integer,Integer> JOB_ALARM_COUNT;

    /**
     * 添加admin，自带索引
     * @param admin
     */
    public static void putAdmin(Admin admin) {
        if(admin.getId()==null){
            admin.setId(++ADMIN_INDEX);
        }
        ADMIN_MAP.put(admin.getId(),admin);
        //如果admin中存的id大于现在的index，需要调整index
        if(admin.getId()>ADMIN_INDEX){
            ADMIN_INDEX=admin.getId();
        }
    }
    public static Integer adminSize() {
        return ADMIN_MAP.size();
    }
    public static Admin getAdmin(Integer id) {
        return ADMIN_MAP.get(id);
    }
    public static List<Admin> getAllAdmin() {
        List<Admin> admin_list=new ArrayList<Admin>(ADMIN_MAP.size());
        Iterator it=ADMIN_MAP.keySet().iterator();
        while(it.hasNext()){
            Integer id= (Integer) it.next();
            Admin admin=ADMIN_MAP.get(id);
            admin_list.add(admin);
        }
        return admin_list;
    }

    /**
     * 添加admin，自带索引
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
        return GROUP_MAP.get(id);
    }
    public static List<Group> getAllGroup() {
        List<Group> group_list=new ArrayList<Group>(GROUP_MAP.size());
        Iterator it=GROUP_MAP.keySet().iterator();
        while(it.hasNext()){
            Integer id= (Integer) it.next();
            Group group=GROUP_MAP.get(id);
            group_list.add(group);
        }
        return group_list;
    }

    public static List<MonitorItem> getAllMonitorItem() {
        return MONITOR_LIST;
    }

    public static int monitorItemSize() {
        return MONITOR_LIST.size();
    }

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
        MONITOR_LIST.add(monitorItem);
    }
}
