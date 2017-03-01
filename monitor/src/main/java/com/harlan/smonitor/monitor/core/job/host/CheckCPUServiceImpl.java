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
	private static final String TITLE="主机cpu监控报警";
	@Override
	protected void run(MonitorItem item, CheckItem checkItem) throws Exception {
		HostMonitorItem hostItem = (HostMonitorItem) item;
		CheckCPU cpuItem = (CheckCPU) checkItem;
		// ip地址
		String ip = hostItem.getIp();
		Double exceed = cpuItem.getExceed();

		logger.debug("开始检查,检查的主机是:{},类型为：{}", ip + hostItem.getName(),cpuItem.getType());
		logger.debug("检查{}{}是否超过了{}", hostItem.getName(), cpuItem.getName(),exceed);
		double cpuVal=0;
		SshConnecter ssh = SshPool.getSsh(hostItem.getIp(),hostItem.getPort(), hostItem.getUser(), hostItem.getPasswd());
		try {
			List<String> list = ssh.command("top -b -n 1 | grep Cpu| awk '{print $2}'|cut -f 1 -d \"u\"");
			String cpuStr = list.get(0);
			if(cpuStr.contains("%")){
                cpuStr = cpuStr.substring(0, cpuStr.length() - 1);
            }
			cpuVal=Double.valueOf(cpuStr);
		} catch (Exception e) {
			logger.error("主机连接过程中异常",e);
			throw e;
		}finally {
			ssh.disconnect();
		}
		logger.info("当前CPU使用率:{}%", cpuVal);
		// 记录检测结果信息到单独的日志文件中
		DataRecorder.record(hostItem.getType(),checkItem.getType(),hostItem.getUser(), cpuVal+"");
		if (cpuVal > exceed) {
			boolean needSendMsg=checkItem.increaseAlarmCount();
			if(needSendMsg){
				String msg = hostItem.getName() + checkItem.getName()+",当前CPU使用率"+cpuVal+"%,阀值:"+exceed;
				sendNotice(item.getAdminList(),TITLE,msg);
			}
		} else {
			checkItem.resetAlarmCount();
		}
	}

}
