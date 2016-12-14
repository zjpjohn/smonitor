package com.harlan.smonitor.monitor.bean.host.check;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.core.job.host.CheckCPUServiceImpl;
import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class CheckCPU extends CheckItem {
	private final static Logger logger = LoggerFactory.getLogger(CheckCPU.class);
	
	private Double exceed;

	public CheckCPU(Map<String,Object> checkMap) {
		super(checkMap);
		if(checkMap.get("exceed")!=null){
			this.exceed = Double.valueOf(checkMap.get("exceed").toString());
		}
	}

	@Override
	public Class<? extends Job> getJobServiceImpl() {
		return CheckCPUServiceImpl.class;
	}

	@Override
	public Map<String,Object> setAttrs(Map<String,Object> checkMap) {
		checkMap.put("exceed",exceed.toString());
		return checkMap;
	}

	public Double getExceed() {
		return exceed;
	}
}
