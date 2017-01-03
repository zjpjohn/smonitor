package com.harlan.smonitor.monitor.data.dao;

import com.harlan.smonitor.api.Result;
import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.common.Constants;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        //TODO 完成后需要测试多个 trigger导致的并发问题
        Integer count=CachedData.JOB_ALARM_COUNT.get(checkItemId);
        if(count==null){
            count=1;
        }else{
            count++;
        }
        CachedData.JOB_ALARM_COUNT.put(checkItemId,count);
        return count;
    }
    public static Result addMonior(MonitorItem monitor){
        Result res=new Result();
        for (CheckItem checkItem:monitor.getCheckList()) {
            if(checkItem.getState()== Constants.CHECK_RUN){
                try {
                    JobDetail job = newJob(checkItem.getJobServiceImpl())
//						.withIdentity("job_"+name)
                            .build();
                    job.getJobDataMap().put("item",monitor);
                    job.getJobDataMap().put("check",checkItem);
                    Set<Trigger> triggerSet=new HashSet<Trigger>(checkItem.getCronList().size());
                    List<TriggerKey> triggerKeyList=new ArrayList<TriggerKey>(checkItem.getCronList().size());
                    for (String cronStr:checkItem.getCronList()) {
                        logger.debug("添加的触发器：checkId:{},cron:{}",checkItem.getId(),cronStr);
                        Trigger trigger = newTrigger()
//						.withIdentity("trigger_"+name)
                                .startNow()
                                .withSchedule(cronSchedule(cronStr))
                                .build();
                        triggerSet.add(trigger);
                    }

                    checkItem.setJobKey(job.getKey());
//                  checkItem.setTriggerKeys(triggerKeyList);
                    CachedData.SCHEDULER.scheduleJob(job,triggerSet,true);
                } catch (Exception e) {
                    res.setSuccess(false);
                    logger.info("添加检查项时异常",e);
                }
            }
        }
        //因为这个对象中又存了几个属性，而get方法取出的是复制的对象
        // 所以需要再put下
        MonitorDao.addMonitor(monitor);
        return res;
    }
    public static void pauseCheck(JobKey key){
        try {
            CachedData.SCHEDULER.pauseJob(key);
        } catch (SchedulerException e) {
            logger.error("check暂停时异常",e);
        }
    }
    public static void restartCheck(JobKey key){
        try {
            CachedData.SCHEDULER.resumeJob(key);
        } catch (SchedulerException e) {
            logger.error("check暂停时异常",e);
        }
    }
    public static boolean removeCheck(JobKey key){
        try {
            return CachedData.SCHEDULER.deleteJob(key);
        } catch (SchedulerException e) {
            logger.error("check暂停时异常",e);
            return false;
        }
    }
    public static void init(){
        logger.info("任务模块 开始初始化...");
        try{
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            CachedData.SCHEDULER=scheduler;
            CachedData.SCHEDULER.start();
        }catch (Exception e){
            logger.error("任务模块启动时发生异常，已停止启动",e);
        }


    }


}
