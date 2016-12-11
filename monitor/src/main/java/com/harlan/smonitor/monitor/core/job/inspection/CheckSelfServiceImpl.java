package com.harlan.smonitor.monitor.core.job.inspection;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.inspection.InspectionMonitorItem;
import com.harlan.smonitor.monitor.bean.inspection.check.CheckSelf;
import com.harlan.smonitor.monitor.core.job.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CheckSelfServiceImpl extends AbstractService {
	private final static Logger logger = LoggerFactory.getLogger(CheckSelfServiceImpl.class);
	private String TITLE="系统自检报警";
	@Override
	protected void run(MonitorItem item, CheckItem checkItem) throws Exception {
		logger.info("---------------------------check start---------------------------");
		InspectionMonitorItem inspectionItem = (InspectionMonitorItem) item;
    	CheckSelf checkSelf = (CheckSelf) checkItem;
    	
    	logger.info("开始检查,检查类型：系统自检");
    	logger.info("检查当前监控系统是否在运行状态");
    	logger.info("开始发送消息，通知管理员");
    	
    	String msg = inspectionItem.getName()+checkSelf.getName();

		checkYesOrNotSendMsg(checkItem,TITLE,msg);
		logger.info("消息发送完成");
    	
    	logger.info("---------------------------check end---------------------------");
	}

}
