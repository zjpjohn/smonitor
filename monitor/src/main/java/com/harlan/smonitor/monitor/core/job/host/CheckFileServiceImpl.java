package com.harlan.smonitor.monitor.core.job.host;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.host.HostMonitorItem;
import com.harlan.smonitor.monitor.bean.host.check.CheckFile;
import com.harlan.smonitor.monitor.common.SshConnecter;
import com.harlan.smonitor.monitor.core.connection.SSHSource;
import com.harlan.smonitor.monitor.data.DataRecorder;
import com.harlan.smonitor.monitor.core.job.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by harlan on 2016/9/21.
 */
public class CheckFileServiceImpl extends AbstractService {
    private final static Logger logger = LoggerFactory.getLogger(CheckFileServiceImpl.class);
	private String TITLE="主机文件监控报警";
    /**
     * map为每一个job实例储存上次查询文件行数
     */
    private static Map<String, Long> rowsMap;
    
    @Override
    protected void run(MonitorItem item, CheckItem checkItem) throws Exception{
    	if(rowsMap == null){
    		rowsMap = new HashMap<String,Long>();
    	}
    	/**
         * 检查文件修改的是否过于频繁
         * 计算文件上一次修改的时间和现在的时间的时间差
         * 查看连续满足报警条件的次数
         * 如果达到报警次数就报警，如果当前检查的结果是不满足报警条件就重置报警次数
         * 
         * 
         *可抽取的公共方法有
         *	获取当前检查项的报警次数，重置检查项的报警次数
         * 
         */
    	logger.info("---------------------------check start---------------------------");
    	HostMonitorItem hostItem = (HostMonitorItem) item;
    	CheckFile checkFile = (CheckFile) checkItem;
    	//ip地址
    	String ip = hostItem.getIp();
    	//受监控的文件路径
    	String filePath = checkFile.getPath();
    	
    	logger.info("开始检查,检查的主机是:{},类型为：{},文件是:{}",ip+hostItem.getName(),checkFile.getType(),filePath);
    	logger.info("检查{}是否{}",hostItem.getName(),checkFile.getName());
    	//更新频繁
    	if(checkFile.getModifyIn() != null){
    		checkModifyIn(hostItem, checkFile);
    	//停止更新
    	}else if(checkFile.getNotModifyIn() != null){
    		checkNotModifyIn(hostItem, checkFile);
    	//增长过快
    	}else if(checkFile.getRowsIncrease() != null){
    		checkRowsIncrease(hostItem, checkFile);
    	}
    }
    
    /**
     * 检查是否更新频繁
     * @param hostItem
     * @param checkFile
     * @throws Exception
     */
    private void checkModifyIn(HostMonitorItem hostItem,CheckFile checkFile) throws Exception{
		//1：计算受监控的文件离上一次修改过去了多少分钟
		Long minute = calculateFileChangeTimeDiff(hostItem,checkFile);
		//记录检测结果信息到单独的日志文件中
		DataRecorder.record(hostItem.getName()+checkFile.getName(), minute+"");
		//2：计算是否满足报警条件
		//如果修改的时间在配置的频繁时间之内则满足单次的报警条件
		if(minute <= checkFile.getModifyIn()){
			logger.info("当前符合单次报警条件");
			String msg = hostItem.getName()+checkFile.getName();
			checkYesOrNotSendMsg(checkFile,TITLE, msg);
		//不满足报警条件,重置次数
		}else{
			logger.info("不符合单次报警条件");
			restAlarmCount(checkFile.getId());
		}
		logger.info("---------------------------check end---------------------------");
	//停止更新
    }
    
    /**
     * 检查是否停止更新
     * @param hostItem
     * @param checkFile
     * @throws Exception
     */
    private void checkNotModifyIn(HostMonitorItem hostItem,CheckFile checkFile) throws Exception{
		//1：计算受监控的文件离上一次修改过去了多少分钟
		Long minute = calculateFileChangeTimeDiff(hostItem,checkFile);
		//记录检测结果信息到单独的日志文件中
		DataRecorder.record(hostItem.getName()+checkFile.getName(), minute+"");
		//满足单次报警条件
		if(minute >= checkFile.getNotModifyIn()){
			logger.info("当前符合单次报警条件");
			String msg = hostItem.getName()+checkFile.getName();
			checkYesOrNotSendMsg(checkFile,TITLE, msg);
		}else{
			logger.info("当前不符合单次报警条件");
			restAlarmCount(checkFile.getId());
		}
		logger.info("---------------------------check end---------------------------");
    }
    
    /**
     * 增长过快	
     * @param hostItem
     * @param checkFile
     * @throws Exception
     */
    private void checkRowsIncrease(HostMonitorItem hostItem,CheckFile checkFile) throws Exception{
		//获取上一次得出的文件总行数
		Long lastRowCoun = rowsMap.get(checkFile.getPath());
		lastRowCoun = (lastRowCoun == null ? 0 : lastRowCoun);
		//获取当前文件的总行数
		Long rowCount = checkFileRowCount(hostItem, checkFile);
		rowsMap.put(checkFile.getPath(), rowCount);
		Integer rowsIncrease = checkFile.getRowsIncrease();
		Long diff = rowCount - lastRowCoun;
		
		//记录检测结果信息到单独的日志文件中
		DataRecorder.record(hostItem.getName()+checkFile.getName(), diff+"");
		
		logger.info("上一次得出的文件总行数：{},当前的总行数：{},较上一次增长了:{}行",lastRowCoun,rowCount,diff);
		logger.info("报警阀值为:{}",rowsIncrease);
		if(diff >= rowsIncrease){
			logger.info("满足单次报警条件");
			String msg = hostItem.getName()+checkFile.getName();
			checkYesOrNotSendMsg(checkFile,TITLE, msg);
		}else{
			logger.info("重置报警次数");
			restAlarmCount(checkFile.getId());
		}
		logger.info("---------------------------check end---------------------------");
    }
    
    /**
     * 计算受监控的文件离上一次修改过去了多少分钟
     * @param hostItem
     * @param checkFile
     * @return
     * @throws Exception
     */
    private Long calculateFileChangeTimeDiff(HostMonitorItem hostItem,CheckFile checkFile) throws Exception{
    	SshConnecter ssh = SSHSource.obtainSSHConn(hostItem.getIp(), hostItem.getUser());
    	logger.info("建立SSH连接");
    	//获取服务器的当前时间
    	String nowDate = ssh.command("date +%Y%m%d%H%M%S").get(0);
    	String filePath = checkFile.getPath();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Long nowDateLong = sdf.parse(nowDate).getTime();
    	logger.info("服务器当前时间的毫秒值：{}",nowDateLong);
    	//获取受监测的文件上一次的修改时间
    	List<String> list = ssh.command("stat "+checkFile.getPath());
    	SSHSource.recoverSSHConn(hostItem.getIp(), hostItem.getUser(), ssh);
    	//相差的分钟数
    	Long minute = null;
    	for (String string : list) {
			if(string.contains("Modify")){
				String modifyDate = string.substring(8, 27);
				String[] arr = filePath.split("/");
				String fileName = arr[arr.length -1];
				logger.info("{}文件最近的修改时间：{}",fileName,modifyDate); 
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				long cd = sdf.parse(modifyDate).getTime();
				long diff = nowDateLong-cd;
				logger.info("毫秒数的时间差,now-change=diff,{}-{}={}",nowDateLong,cd,diff);
				minute = diff/1000/60;
				logger.info("离上一次修改时间相差{}分钟",minute);
			}
		}
    	return minute;
    } 
    /**
     * 统计文件行数
     * @return
     * @throws Exception 
     */
    private Long checkFileRowCount(HostMonitorItem hostItem,CheckFile checkFile){
    	SshConnecter ssh;
		try {
			ssh = SSHSource.obtainSSHConn(hostItem.getIp(), hostItem.getUser());
			List<String> list = ssh.command("cat "+checkFile.getPath()+" | wc -l");
			SSHSource.recoverSSHConn(hostItem.getIp(), hostItem.getUser(), ssh);
			Long rowCount = Long.valueOf(list.get(0));
			String[] arr = checkFile.getPath().split("/");
			String fileName = arr[arr.length -1];
			logger.info("{}文件当前的总行数为：{}",fileName,rowCount);
			return rowCount;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询文件总行数出现异常：{}",e.getMessage());
		}
		return null;
    }
}
