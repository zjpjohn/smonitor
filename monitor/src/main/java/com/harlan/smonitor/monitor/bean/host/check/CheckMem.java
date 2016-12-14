package com.harlan.smonitor.monitor.bean.host.check;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.core.job.host.CheckMemServiceImpl;
import org.quartz.Job;

import java.util.Map;


public class CheckMem extends CheckItem {

	public CheckMem(Map<String,Object> checkMap) {
		super(checkMap);
		if(checkMap.get("exceed")!=null){
			this.exceed=Integer.valueOf(checkMap.get("exceed").toString());
		}
	}
	@Override
	public Map<String,Object> setAttrs(Map<String,Object> checkMap) {
		checkMap.put("exceed",exceed);
		return checkMap;
	}
	private Integer exceed;

	@Override
	public Class<? extends Job> getJobServiceImpl() {
		return CheckMemServiceImpl.class;
	}



	public Integer getExceed() {
		return exceed;
	}
}
