package com.harlan.smonitor.monitor.core.job.host;

import java.util.Date;
import java.util.List;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.host.HostMonitorItem;
import com.harlan.smonitor.monitor.bean.host.check.CheckFile;
import com.harlan.smonitor.monitor.common.SshConnecter;
import com.harlan.smonitor.monitor.common.Util;
import com.harlan.smonitor.monitor.core.connection.SshPool;
import com.harlan.smonitor.monitor.core.job.AbstractService;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 文件检查job
 * CheckFileServiceImpl
 * Created by harlan on 2016/9/21.
 */
public class CheckFileServiceImpl extends AbstractService {
    private final Logger logger = LoggerFactory.getLogger(CheckFileServiceImpl.class);
	private static final String TITLE="主机文件监控报警";
	private final static FastDateFormat DATE_FRMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    /**
     * map为每一个job实例储存上次查询文件行数
     */

    @Override
    protected void run(MonitorItem item, CheckItem checkItem) throws Exception{
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
    	HostMonitorItem hostItem = (HostMonitorItem) item;
    	CheckFile checkFile = (CheckFile) checkItem;
    	//ip地址
    	String ip = hostItem.getIp();
    	//受监控的文件路径
    	String filePath = checkFile.getPath();
    	
    	logger.info("开始检查,检查的主机是:{},类型为：{},文件是:{}",ip,checkFile.getType(),filePath);
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
//		DataRecorder.record(hostItem.getName()+checkFile.getName(), minute+"");
		//2：计算是否满足报警条件
		//如果修改的时间在配置的频繁时间之内则满足单次的报警条件
		if(minute <= checkFile.getModifyIn()){
			boolean needSendMsg=checkFile.increaseAlarmCount();
			if(needSendMsg){
				String msg = hostItem.getName()+checkFile.getName();
				sendNotice(hostItem.getAdminList(),TITLE,msg);
			}
		//不满足报警条件,重置次数
		}else{
			checkFile.resetAlarmCount();
		}
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
//		DataRecorder.record(hostItem.getName()+checkFile.getName(), minute+"");
		//满足单次报警条件
		if(minute >= checkFile.getNotModifyIn()){
			boolean needSendMsg=checkFile.increaseAlarmCount();
			if(needSendMsg){
				String msg = hostItem.getName()+checkFile.getName();
				sendNotice(hostItem.getAdminList(),TITLE,msg);
			}
		}else{
			checkFile.resetAlarmCount();
		}
    }
    
    /**
     * 增长过快	
     * @param hostItem
     * @param checkFile
     * @throws Exception
     */
    private void checkRowsIncrease(HostMonitorItem hostItem,CheckFile checkFile) throws Exception{
		//获取上一次得出的文件总行数
		Long lastRowCoun = checkFile.getRows();
		lastRowCoun = (lastRowCoun == null ? 0 : lastRowCoun);
		//获取当前文件的总行数
		Long rowCount = checkFileRowCount(hostItem, checkFile);
		checkFile.setRows(rowCount);
		Integer rowsIncrease = checkFile.getRowsIncrease();
		Long diff = rowCount - lastRowCoun;
		
		//记录检测结果信息到单独的日志文件中
//		DataRecorder.record(hostItem.getName()+checkFile.getName(), diff+"");
		
		logger.info("上一次得出的文件总行数：{},当前的总行数：{},较上一次增长了:{}行",lastRowCoun,rowCount,diff);
		logger.info("报警阀值为:{}",rowsIncrease);
		if(diff >= rowsIncrease){
			boolean needSendMsg=checkFile.increaseAlarmCount();
			if(needSendMsg){
				String msg = hostItem.getName()+checkFile.getName();
				sendNotice(hostItem.getAdminList(),TITLE,msg);
			}
		}else{
			checkFile.resetAlarmCount();
		}
    }
    
    /**
     * 计算受监控的文件离上一次修改过去了多少分钟
     * @param hostItem
     * @param checkFile
     * @return
     * @throws Exception
     */
    private Long calculateFileChangeTimeDiff(HostMonitorItem hostItem,CheckFile checkFile) throws Exception{
		SshConnecter ssh = SshPool.getSsh(hostItem.getIp(),hostItem.getPort(), hostItem.getUser(), hostItem.getPasswd());
		Long nowDateLong = null;
		List<String> result = null;
		try {
			//获取服务器的当前时间
			String nowDateStr = ssh.command("date +%Y%m%d%H%M%S").get(0);
			Date nowDate= Util.string2Date(nowDateStr);
			logger.debug("服务器当前时间：{}",nowDate);
			nowDateLong = nowDate.getTime();

			//获取受监测的文件上一次的修改时间
			result = ssh.command("stat "+checkFile.getPath()+" |sed -n '6p'| awk '{print $2\" \"$3}'");
			if(result==null || result.size()==0){
                throw new RuntimeException("未获取到文件修改时间，请检查文件是否存在...");
            }
		} catch (Exception e) {
			logger.error("ssh命令执行异常",e);
			throw e;
		} finally {
			ssh.disconnect();
		}
		//相差的分钟数
		logger.info("{}文件最近的修改时间：{}",checkFile.getPath(),result.get(0));
		Date modifyDate = DATE_FRMAT.parse(result.get(0));
		logger.debug("修改时间：{}",modifyDate);
		Long modifyDateLong =modifyDate.getTime();

		long diff = nowDateLong-modifyDateLong;
		Long minute = diff/1000/60;
		logger.info("离上一次修改时间相差{}分钟",minute);
    	return minute;
    } 
    /**
     * 统计文件行数
     * @return
     * @throws Exception 
     */
    private Long checkFileRowCount(HostMonitorItem hostItem,CheckFile checkFile) throws Exception {
		SshConnecter ssh = SshPool.getSsh(hostItem.getIp(),hostItem.getPort(), hostItem.getUser(), hostItem.getPasswd());
		Long rowCount = null;
		String fileName = null;
		try {
			List<String> list = ssh.command("cat "+checkFile.getPath()+" | wc -l");
			rowCount = Long.valueOf(list.get(0));
			String[] arr = checkFile.getPath().split("/");
			fileName = arr[arr.length -1];
		} catch (Exception e) {
			logger.error("ssh命令执行异常",e);
			throw e;
		} finally {
			ssh.disconnect();
		}
		logger.info("{}文件当前的总行数为：{}",fileName,rowCount);
		return rowCount;
    }
}
