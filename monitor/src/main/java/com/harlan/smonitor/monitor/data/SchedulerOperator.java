package com.harlan.smonitor.monitor.data;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by harlan on 2016/12/5.
 */
public class SchedulerOperator {

    private final static Logger logger = LoggerFactory.getLogger(SchedulerOperator.class);

    public  static void startTask(List<MonitorItem> monitorItemList) throws Exception{
        logger.info("TaskService 开始初始化 任务...");
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        for (MonitorItem monitorItem : CachedData.MONITOR_LIST) {
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
                checkItem.setId(DataOperator.nextMonitorIndex());
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
