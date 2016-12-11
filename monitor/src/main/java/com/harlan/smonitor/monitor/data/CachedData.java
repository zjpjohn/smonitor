package com.harlan.smonitor.monitor.data;

import com.harlan.smonitor.api.notice.Admin;
import com.harlan.smonitor.monitor.bean.Group;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import org.quartz.Scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部分数据存在内存中，类似数据库
 * Created by harlan on 2016/11/24.
 */
class CachedData {
    static{
        ADMIN_MAP=new HashMap<Integer, Admin>();
        JOB_ALARM_COUNT=new HashMap<Integer, Integer>();
        MONITOR_INDEX=0;
        ADMIN_INDEX=0;
    }
    //监控项的序列
    static int MONITOR_INDEX;
    //管理员的序列
    static int ADMIN_INDEX;
    //通讯录，储存所有管理员
    static Map<Integer, Admin> ADMIN_MAP;
    //所有监控任务列表
    static List<MonitorItem> MONITOR_LIST;
    //分类
    static Map<Integer, Group> GROUP_MAP;
    //调度管理对象
    static Scheduler SCHEDULER;
    //每个job对应一个报警计数器
    static Map<Integer,Integer> JOB_ALARM_COUNT;
}
