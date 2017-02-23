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
        MonitorItem monitor = (MonitorItem) context.getMergedJobDataMap().get("monitor");
        CheckItem checkItem=(CheckItem) context.getMergedJobDataMap().get("check");
        try {
			logger.info("---------------------------check start---------------------------");
            run(monitor,checkItem);
			logger.info("---------------------------check end-----------------------------");
        } catch (Exception e) {
            logger.error("任务执行时出现异常",e);
			//出现异常时需要通知所有管理员
			String title=checkItem.getName()+",在执行时抛出异常";
			String content=title+",异常是："+e.toString();
			sendNotice(monitor.getAdminList(),title,content);
        }
    }

    /**
     * 所有监控项的处理逻辑的实现，需要在run方法中实现
     * @param item 监控项对象，各个实现类中需要把item强转成相应的item bean
     * @throws Exception 抛出未知异常，会通知相应的管理员
     */
    protected abstract void run(MonitorItem item, CheckItem checkItem) throws Exception;
    

	/**
	 * 发送通知信息
	 * @param adminList 管理员列表
	 * @param title 标题
	 * @param content 内容
	 */
	protected void sendNotice(List<String> adminList,String title,String content){
		//TODO 修改INoticeService api 发送方法需要是多个admin一起通知
		logger.debug("发送通知通知管理员，管理员数量：{},title:{},内容：{}",adminList.size(),title,content);
		for (String admin:adminList) {
			Admin admin_bean= AdminDao.getAdmin(admin);
			INoticeService service= ModuleRegister.getNoticeServiceImpl(admin_bean.getType());
			service.sendMessage(admin_bean,title,content);
		}
	}
}
