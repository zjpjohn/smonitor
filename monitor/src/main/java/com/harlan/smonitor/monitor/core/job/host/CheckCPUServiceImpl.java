package com.harlan.smonitor.monitor.core.job.host;

import java.util.List;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.host.HostMonitorItem;
import com.harlan.smonitor.monitor.bean.host.check.CheckCPU;
import com.harlan.smonitor.monitor.common.SshConnecter;
import com.harlan.smonitor.monitor.core.connection.SshPool;
import com.harlan.smonitor.monitor.core.job.AbstractService;
import com.harlan.smonitor.monitor.data.DataRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckCPUServiceImpl extends AbstractService {
	private final static Logger logger = LoggerFactory.getLogger(CheckCPUServiceImpl.class);
	private String TITLE="主机cpu监控报警";
	@Override
	protected void run(MonitorItem item, CheckItem checkItem) throws Exception {

		logger.info("---------------------------check start---------------------------");
		HostMonitorItem hostItem = (HostMonitorItem) item;
		CheckCPU cpuItem = (CheckCPU) checkItem;
		// ip地址
		String ip = hostItem.getIp();
		Double exceed = Double.valueOf(cpuItem.getExceed());

		logger.debug("开始检查,检查的主机是:{},类型为：{}", ip + hostItem.getName(),
				cpuItem.getType());
		logger.debug("检查{}{}是否超过了{}", hostItem.getName(), cpuItem.getName(),exceed);
		SshConnecter ssh = SshPool.getSsh(hostItem.getIp(),hostItem.getPort(), hostItem.getUser(), hostItem.getPasswd());
		List<String> list = ssh.command("top -b -n 1 | grep Cpu| awk '{print $2}'|cut -f 1 -d \"u\"");
		double cpuVal = 0;
		String cpuRet = list.get(0);
		cpuVal = Double.valueOf(cpuRet.substring(0, cpuRet.length() - 1));
		logger.info("当前CPU使用率", cpuVal + "%");
		// 记录检测结果信息到单独的日志文件中
		DataRecorder.record(hostItem.getName() + checkItem.getName(), cpuVal + "");
		if (cpuVal > exceed) {
			logger.debug("当前CPU使用率{}超过设置的阀值{}", cpuVal, exceed);
			String msg = hostItem.getName() + checkItem.getName()+"达到"+cpuVal+"超过设置的阀值"+exceed;
			checkAndSendMsg(checkItem,item.getAdminList(),TITLE,msg);
		} else {
			logger.debug("当前CPU使用率{}没有超过设置的阀值{},重置报警次数", cpuVal, exceed);
			restAlarmCount(checkItem.getId());
		}
		logger.info("---------------------------check end---------------------------");
	}

}
