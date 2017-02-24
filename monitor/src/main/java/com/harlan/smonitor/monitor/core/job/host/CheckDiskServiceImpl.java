package com.harlan.smonitor.monitor.core.job.host;

import java.util.List;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.host.HostMonitorItem;
import com.harlan.smonitor.monitor.bean.host.check.CheckDisk;
import com.harlan.smonitor.monitor.common.SshConnecter;
import com.harlan.smonitor.monitor.core.connection.SshPool;
import com.harlan.smonitor.monitor.data.DataRecorder;
import com.harlan.smonitor.monitor.core.job.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckDiskServiceImpl extends AbstractService {
	private final static Logger logger = LoggerFactory.getLogger(CheckDiskServiceImpl.class);
	private static final String TITLE="主机磁盘监控报警";
	@Override
	protected void run(MonitorItem item, CheckItem checkItem) throws Exception {

		HostMonitorItem hostItem = (HostMonitorItem) item;
		CheckDisk diskItem = (CheckDisk) checkItem;
		// ip地址
		String ip = hostItem.getIp();
		Double exceed = Double.valueOf(diskItem.getExceed());

		logger.info("开始检查,检查的主机是:{},类型为：{}", ip ,diskItem.getType());
		logger.debug("检查{}{}是否超过了{}", hostItem.getName(), diskItem.getName(),exceed);
		SshConnecter ssh = SshPool.getSsh(hostItem.getIp(),hostItem.getPort(), hostItem.getUser(), hostItem.getPasswd());
		List<String> list = ssh.command("df -lP|grep -e "+diskItem.getPath()+"$ |awk '{print $5}'");
		double diskVal;
		String percent = list.get(0);
		String val = percent.substring(0, percent.length() - 1);
		diskVal = Double.valueOf(val);
		// 记录检测结果信息到单独的日志文件中
		logger.debug("当前磁盘使用率{}", diskVal);
		DataRecorder.record(hostItem.getType(),diskItem.getType(),diskItem.getPath(),diskVal+ "");
		if (diskVal > exceed) {
			boolean needSendMsg=checkItem.increaseAlarmCount();
			if(needSendMsg){
				String msg = hostItem.getName() + checkItem.getName()+",当前磁盘使用率:"+diskVal+",阀值:"+exceed;
				sendNotice(item.getAdminList(),TITLE,msg);
			}
		} else {
			checkItem.resetAlarmCount();
		}

	}

}
