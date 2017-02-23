package com.harlan.smonitor.monitor.data.dao;

import com.harlan.smonitor.api.Result;
import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.common.Constants;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
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

    public static Result addMonior(MonitorItem monitor){
        Result res=new Result();
        for (CheckItem checkItem:monitor.getCheckList()) {
            if(checkItem.getState()== Constants.CHECK_RUN){
                addCheck(checkItem,monitor);
            }
        }
        //因为这个对象中又存了几个属性，而get方法取出的是复制的对象
        // 所以需要再put下
        MonitorDao.addMonitor(monitor);
        return res;
    }
    public static Result pauseCheck(JobKey key){
        Result res=new Result();
        try {
            CachedData.SCHEDULER.pauseJob(key);
        } catch (SchedulerException e) {
            res.setMsg("暂停任务时异常");
            res.setSuccess(false);
            logger.error("check暂停时异常",e);
        }
        return res;
    }
    public static Result addCheck(CheckItem checkItem,MonitorItem monitor){
        Result res=new Result();
        try {
            JobDetail job = newJob(checkItem.getJobServiceImpl())
                    .build();
            job.getJobDataMap().put("monitor",monitor);
            job.getJobDataMap().put("check",checkItem);
            Set<Trigger> triggerSet=new HashSet<Trigger>(checkItem.getCronList().size());
//            List<TriggerKey> triggerKeyList=new ArrayList<TriggerKey>(checkItem.getCronList().size());
            for (String cronStr:checkItem.getCronList()) {
                logger.debug("添加的触发器：checkId:{},cron:{}",checkItem.getId(),cronStr);
                Trigger trigger = newTrigger()
                        .startNow()
                        .withSchedule(cronSchedule(cronStr))
                        .build();
                triggerSet.add(trigger);
            }
            checkItem.setJobKey(job.getKey());
//          checkItem.setTriggerKeys(triggerKeyList);
            CachedData.SCHEDULER.scheduleJob(job,triggerSet,true);
            logger.info("加载任务成功 checkid:{}",checkItem.getId());
        } catch (Exception e) {
            logger.info("添加检查项时异常",e);
            res.setMsg("新建任务异常");
            res.setSuccess(false);
            return res;
        }
        return res;
    }
    public static void restartCheck(JobKey key){
        try {
            CachedData.SCHEDULER.resumeJob(key);
        } catch (SchedulerException e) {
            logger.error("check暂停时异常",e);
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


    public static Result removeCheck(JobKey jobKey) {
        Result res=new Result();
        try {
            CachedData.SCHEDULER.deleteJob(jobKey);
        } catch (SchedulerException e) {
            res.setMsg("删除任务时异常");
            res.setSuccess(false);
            logger.error("check删除时异常",e);
        }
        return res;
    }
}
