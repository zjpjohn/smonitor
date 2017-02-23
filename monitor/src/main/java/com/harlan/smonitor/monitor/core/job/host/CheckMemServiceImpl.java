package com.harlan.smonitor.monitor.core.job.host;

import java.util.List;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.host.HostMonitorItem;
import com.harlan.smonitor.monitor.bean.host.check.CheckMem;
import com.harlan.smonitor.monitor.common.SshConnecter;
import com.harlan.smonitor.monitor.core.connection.SshPool;
import com.harlan.smonitor.monitor.data.DataRecorder;
import com.harlan.smonitor.monitor.core.job.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckMemServiceImpl extends AbstractService {
	private final static Logger logger = LoggerFactory.getLogger(CheckMemServiceImpl.class);
	private static final String TITLE="主机内存监控报警";
	@Override
	protected void run(MonitorItem item, CheckItem checkItem) throws Exception {

		HostMonitorItem hostItem = (HostMonitorItem) item;
		CheckMem memItem = (CheckMem) checkItem;
		// ip地址
		String ip = hostItem.getIp();
		Double exceed = Double.valueOf(memItem.getExceed());

		logger.info("开始检查,检查的主机是:{},类型为：{}", ip + hostItem.getName(),
				memItem.getType());
		logger.info("检查{}{}是否超过了{}", hostItem.getName(), memItem.getName(),
				exceed);
		SshConnecter ssh = SshPool.getSsh(hostItem.getIp(),hostItem.getPort(), hostItem.getUser(), hostItem.getPasswd());
		List<String> result = ssh.command("free -m | sed -n '2p' | awk '{print $3/$2*100}'");
		double memVal ;
		if(result==null || result.size()==0){
			throw new RuntimeException("free命令返回空");
		}
		memVal = Double.valueOf(result.get(0));
		//记录检测结果信息到单独的日志文件中
		logger.info("当前内存使用率：{}%",memVal);
		DataRecorder.record(hostItem.getName()+checkItem.getName(), memVal+"");
		if (memVal > exceed) {
			boolean needSendMsg=memItem.increaseAlarmCount();
			if(needSendMsg){
				String msg = hostItem.getName() + checkItem.getName()+"达到"+memVal+"超过设置的阀值"+exceed;
				sendNotice(hostItem.getAdminList(),TITLE,msg);
			}
		} else {
			checkItem.resetAlarmCount();
		}
	}

}
