package com.harlan.smonitor.monitor.data.dao;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * Created by harlan on 2016/12/12.
 */
public class JobDao {

    /**
     * 重置alamcount
     * @param checkItemId
     */
    public static void jobAlarmReset(Integer checkItemId){
        CachedData.JOB_ALARM_COUNT.put(checkItemId,0);
    }

    /**
     * alamCount加一
     * @param checkItemId
     * @return
     */
    public static int jobAlamIncrease(Integer checkItemId){
        Integer count=CachedData.JOB_ALARM_COUNT.get(checkItemId);
        if(count==null){
            count=1;
        }else{
            count++;
        }
        CachedData.JOB_ALARM_COUNT.put(checkItemId,count);
        return count;
    }

    public static void start(Scheduler scheduler) throws SchedulerException {
        CachedData.SCHEDULER=scheduler;
        CachedData.SCHEDULER.start();
    }
}
