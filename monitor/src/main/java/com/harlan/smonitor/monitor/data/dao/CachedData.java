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
        MONITOR_LIST=new LinkedList<MonitorItem>();
        JOB_ALARM_COUNT=new HashMap<Integer, Integer>();
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
    private static List<MonitorItem> MONITOR_LIST;
    //分类
    private static Map<Integer, Group> GROUP_MAP;
    //调度管理对象
    static Scheduler SCHEDULER;
    //每个job对应一个报警计数器
    static Map<Integer,Integer> JOB_ALARM_COUNT;

    /**
     * ==================================================
     * admin
     * ==================================================
     */
    /**
     * 添加admin，自带索引
     * @param admin
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
     * @param admin
     * @return
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
     * MonitorItem
     * ==================================================
     */
    public static List<MonitorItem> getAllMonitorItem() {
        List<MonitorItem> monitor_list=new ArrayList<MonitorItem>(MONITOR_LIST.size());
        for (MonitorItem monitor:MONITOR_LIST) {
            MonitorItem copyMonitor=copyMonitor(monitor);
            monitor_list.add(copyMonitor);
        }
        return monitor_list;
    }

    private static MonitorItem copyMonitor(MonitorItem monitor) {
        MonitorItem copyMonitor=MonitorItem.monitorInstance(monitor.getType());
        copyMonitor.init(monitor.createMap());
        return copyMonitor;
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
