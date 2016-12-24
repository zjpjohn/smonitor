package com.harlan.smonitor.monitor.core.job.host;

import java.util.List;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.host.HostMonitorItem;
import com.harlan.smonitor.monitor.bean.host.check.CheckMem;
import com.harlan.smonitor.monitor.common.SshConnecter;
import com.harlan.smonitor.monitor.core.connection.SSHSource;
import com.harlan.smonitor.monitor.data.DataRecorder;
import com.harlan.smonitor.monitor.core.job.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckMemServiceImpl extends AbstractService {
	private final static Logger logger = LoggerFactory.getLogger(CheckMemServiceImpl.class);
	private String TITLE="主机文件监控报警";
	@Override
	protected void run(MonitorItem item, CheckItem checkItem) throws Exception {

		logger.info("---------------------------check start---------------------------");
		HostMonitorItem hostItem = (HostMonitorItem) item;
		CheckMem memItem = (CheckMem) checkItem;
		// ip地址
		String ip = hostItem.getIp();
		Double exceed = Double.valueOf(memItem.getExceed());

		logger.info("开始检查,检查的主机是:{},类型为：{}", ip + hostItem.getName(),
				memItem.getType());
		logger.info("检查{}{}是否超过了{}", hostItem.getName(), memItem.getName(),
				exceed);
		SshConnecter ssh = SSHSource.obtainSSHConn(hostItem.getIp(), hostItem.getUser());
		logger.info("ssh链接建立成功");
		List<String> list = ssh.command("free");
		SSHSource.recoverSSHConn(hostItem.getIp(), hostItem.getUser(), ssh);
		double memVal = 0;
		for (String string : list) {
			if (string.contains("Mem")) {
				String[] arr = string.split("\\s+");
				Double val = Double.valueOf(arr[2]) / Double.valueOf(arr[1]);
				Long l = Math.round(val * 100);
				memVal = l.intValue();
				break;
			}
		}
		//记录检测结果信息到单独的日志文件中
		DataRecorder.record(hostItem.getName()+checkItem.getName(), memVal+"");
		if (memVal > exceed) {
			logger.info("当前内存使用率{}超过设置的阀值{},满足单次报警条件", memVal, exceed);
			String msg = hostItem.getName() + checkItem.getName()+"达到"+memVal+"超过设置的阀值"+exceed;
			checkAndSendMsg(checkItem,item.getAdminList(),TITLE,msg);
		} else {
			logger.info("当前内存使用率{}没有超过设置的阀值{},重置报警次数", memVal, exceed);
			restAlarmCount(checkItem.getId());
		}
		logger.info("---------------------------check end---------------------------");
	}

}
