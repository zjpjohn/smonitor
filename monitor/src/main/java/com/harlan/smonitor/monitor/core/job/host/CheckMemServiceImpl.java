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
	//meminfo文件中属性的名称
	private static final String MEMINFO_KEY_MEMTOTAL="MemTotal";
	private static final String MEMINFO_KEY_MEMFREE="MemFree";
	private static final String MEMINFO_KEY_BUFFERS="Buffers";
	private static final String MEMINFO_KEY_CACHED="Cached";
	//要执行的命令
	private static final String SHELL_COMMAND="cat /proc/meminfo  | grep -E '^"
			+MEMINFO_KEY_MEMTOTAL+"|^"
			+MEMINFO_KEY_MEMFREE+"|^"
			+MEMINFO_KEY_BUFFERS+"|^"
			+MEMINFO_KEY_CACHED+"'";
	//meminfo里每行的分隔符
	private static final String MEMINFO_SPLIT_CHAR="\\s+";
	//meminfo里所有值的索引，取第2个值
	private static final int VALUE_INDEX=1;
	@Override
	protected void run(MonitorItem item, CheckItem checkItem) throws Exception {

		HostMonitorItem hostItem = (HostMonitorItem) item;
		CheckMem memItem = (CheckMem) checkItem;
		String ip = hostItem.getIp();
		Double exceed = Double.valueOf(memItem.getExceed());

		logger.info("开始检查,检查的主机是:{},类型为：{}", ip + hostItem.getName(),	memItem.getType());
		logger.info("检查{}{}是否超过了{}", hostItem.getName(), memItem.getName(),exceed);
		SshConnecter ssh = SshPool.getSsh(hostItem.getIp(),hostItem.getPort(), hostItem.getUser(), hostItem.getPasswd());
		Double memUseRate =null;
		Double memTotal=null;
		Double memFree=null;
		Double buffers=null;
		Double cached=null;
		try {
			//内存使用率计算方式
			//内存使用率=100*(MemTotal-MemFree-Buffers-Cached)/MemTotal
			List<String> result = ssh.command(SHELL_COMMAND);
			if(result==null || result.size() == 0|| result.size() != 4){
                throw new RuntimeException("free命令返回与预期不同");
            }
			for (String resStr:result) {
				if(resStr.contains(MEMINFO_KEY_MEMTOTAL)){
					memTotal=Double.valueOf(resStr.split(MEMINFO_SPLIT_CHAR)[VALUE_INDEX].trim());
				}else if(resStr.contains(MEMINFO_KEY_MEMFREE)){
					memFree=Double.valueOf(resStr.split(MEMINFO_SPLIT_CHAR)[VALUE_INDEX].trim());
				}else if(resStr.contains(MEMINFO_KEY_BUFFERS)){
					buffers=Double.valueOf(resStr.split(MEMINFO_SPLIT_CHAR)[VALUE_INDEX].trim());
				}else if(resStr.contains(MEMINFO_KEY_CACHED)){
					cached=Double.valueOf(resStr.split(MEMINFO_SPLIT_CHAR)[VALUE_INDEX].trim());
				}

			}
			memUseRate = 100*(memTotal-memFree-buffers-cached)/memTotal;
		} catch (Exception e) {
			logger.error("ssh命令执行异常",e);
			throw e;
		} finally {
			ssh.disconnect();
		}
		logger.info("当前内存使用率：{}%",memUseRate);
		//记录检测结果信息到单独的日志文件中
		DataRecorder.record(hostItem.getType(),memItem.getType(),hostItem.getUser(), memUseRate+"");
		if (memUseRate > exceed) {
			boolean needSendMsg=memItem.increaseAlarmCount();
			if(needSendMsg){
				String msg = hostItem.getName() + checkItem.getName()+"达到"+memUseRate+"超过设置的阀值"+exceed;
				sendNotice(hostItem.getAdminList(),TITLE,msg);
			}
		} else {
			checkItem.resetAlarmCount();
		}
	}

}
