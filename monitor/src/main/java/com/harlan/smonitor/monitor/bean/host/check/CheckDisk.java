package com.harlan.smonitor.monitor.bean.host.check;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.core.job.host.CheckDiskServiceImpl;
import org.quartz.Job;

import java.util.Map;


public class CheckDisk extends CheckItem {

	private Integer exceed;
	private String path;
	private Integer inodeExceed;

	public CheckDisk(Map<String,Object> checkMap) {
		super(checkMap);
		if(checkMap.get("path")!=null){
			this.path=checkMap.get("path").toString();
		}
		if(checkMap.get("exceed")!=null){
			this.exceed=Integer.valueOf(checkMap.get("exceed").toString());
		}
		if(checkMap.get("inodeExceed")!=null){
			this.inodeExceed=Integer.valueOf(checkMap.get("inodeExceed").toString());
		}
	}

	@Override
	public Class<? extends Job> getJobServiceImpl() {
		return CheckDiskServiceImpl.class;
	}

	@Override
	public Map<String,Object> setAttrs(Map<String,Object> checkMap) {
		checkMap.put("path",path);
		checkMap.put("exceed",exceed);
		checkMap.put("inodeExceed",inodeExceed);
		return checkMap;
	}

	public Integer getExceed() {
		return exceed;
	}
	public String getPath() {
		return path;
	}
	public Integer getInodeExceed() {
		return inodeExceed;
	}
}
