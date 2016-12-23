package com.harlan.smonitor.monitor.data.dao;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * JobDao
 * Created by harlan on 2016/12/12.
 */
public class JobDao {
    private final static Logger logger = LoggerFactory.getLogger(JobDao.class);
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

    public static void start() throws SchedulerException {
        logger.info("TaskService 开始初始化 任务...");
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        for (MonitorItem monitorItem : CachedData.getAllMonitorItem()) {
            for (CheckItem checkItem:monitorItem.getCheckList()) {

                JobDetail job = newJob(checkItem.getJobServiceImpl())
//						.withIdentity("job_"+name)
                        .build();

                job.getJobDataMap().put("item",monitorItem);
                job.getJobDataMap().put("check",checkItem);
                Trigger trigger = newTrigger()
//						.withIdentity("trigger_"+name)
                        .startNow()
                        .withSchedule(cronSchedule(checkItem.getCronExpression()))
                        .build();
                checkItem.setJobKey(job.getKey());
                checkItem.setTriggerKey(trigger.getKey());
                scheduler.scheduleJob(job,trigger);
                logger.debug("检查项id:{},key：{}",checkItem.getId(),checkItem.getTriggerKey());
            }
        }
        logger.info("共添加任务个数:{}",0);
        CachedData.SCHEDULER=scheduler;
        CachedData.SCHEDULER.start();
    }


}
