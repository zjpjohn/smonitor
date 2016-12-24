package com.harlan.smonitor.monitor.core.job;

import java.util.List;

import com.harlan.smonitor.api.notice.Admin;
import com.harlan.smonitor.api.notice.INoticeService;
import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.core.init.ModuleRegister;
import com.harlan.smonitor.monitor.data.dao.AdminDao;
import com.harlan.smonitor.monitor.data.dao.JobDao;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 所有的job需要实现这个抽象类，实现run方法
 * Created by harlan on 2016/9/13.
 */
public abstract class AbstractService implements Job {
    private final static Logger logger = LoggerFactory.getLogger(AbstractService.class);

	@Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        MonitorItem item = (MonitorItem) context.getMergedJobDataMap().get("item");
        logger.debug("监控项 name:{} ",item.getName());
        CheckItem checkItem=(CheckItem) context.getMergedJobDataMap().get("check");
        try {
            run(item,checkItem);
        } catch (Exception e) {
            logger.error("任务执行时出现异常",e);
			//出现异常时需要通知所有管理员
			String title=checkItem.getName()+"在执行时抛出异常";
			String content=title+",异常是："+e.toString();
			sendNotice(item.getAdminList(),title,content);
        }
    }

    /**
     * 所有监控项的处理逻辑的实现，需要在run方法中实现
     * @param item 监控项对象，各个实现类中需要把item强转成相应的item bean
     * @throws Exception 抛出未知异常，会通知相应的管理员
     */
    protected abstract void run(MonitorItem item, CheckItem checkItem) throws Exception;
    
	/**
	 * 重置计数器
	 * @return
	 */
	protected void restAlarmCount(Integer checkItemId){
		logger.info("重置ID为 {} 的检查项，报警计数器",checkItemId);
		JobDao.jobAlarmReset(checkItemId);

	}
    
	/**
	 * 报警计数器+1，如果超过报警阀值，发送通知
	 * @param checkItem 检查项
	 * @param adminList 联系人列表
	 * @param title 标题
	 * @param content 内容
	 */
	protected void checkAndSendMsg(CheckItem checkItem,List<String> adminList,String title,String content) {
		int count= JobDao.jobAlamIncrease(checkItem.getId());
		if(count>=checkItem.getAlarmTimes()){
			logger.info("ID为 {} 的检查项，满足条件，发送通知，通知题目为 ：{},通知内容：{}",checkItem.getId(),title,content);
			sendNotice(adminList,title,content);
		}
	}

	private void sendNotice(List<String> adminList,String title,String content){
		for (String admin:adminList) {
			Admin admin_bean= AdminDao.getAdmin(admin);
			INoticeService service= ModuleRegister.getNoticeServiceImpl(admin_bean.getType());
			service.sendMessage(admin_bean,title,content);
		}
	}
}
