package com.harlan.smonitor.monitor.bean.inspection.check;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.core.job.inspection.CheckSelfServiceImpl;
import org.quartz.Job;

import java.util.Map;


public class CheckSelf extends CheckItem {
	public CheckSelf(Map<String,Object> itemMap) {
		super(itemMap);
		
	}

	@Override
	public Class<? extends Job> getJobServiceImpl() {
		return CheckSelfServiceImpl.class;
	}

	@Override
	public Map<String,Object> setAttrs(Map<String,Object> itemMap) {
		return itemMap;
	}

}
