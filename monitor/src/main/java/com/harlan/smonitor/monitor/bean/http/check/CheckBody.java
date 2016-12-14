package com.harlan.smonitor.monitor.bean.http.check;

import java.util.List;
import java.util.Map;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.core.job.http.CheckBodyServiceImpl;
import org.quartz.Job;

public class CheckBody extends CheckItem {

	private List<String> contains;
	
	private List<String> exclude;
	@SuppressWarnings("unchecked")
	public CheckBody(Map<String,Object> checkMap) {
		super(checkMap);
		if(checkMap.get("contains")!=null){
			contains= (List<String>) checkMap.get("contains");
		}
		if(checkMap.get("exclude")!=null){
			exclude= (List<String>) checkMap.get("exclude");
		}
	}
	@Override
	public Map<String,Object> setAttrs(Map<String,Object> checkMap) {
		checkMap.put("contains",contains);
		checkMap.put("exclude",exclude);
		return checkMap;
	}
	@Override
	public Class<? extends Job> getJobServiceImpl() {
		return CheckBodyServiceImpl.class;
	}



	public List<String> getContains() {
		return contains;
	}
	public List<String> getExclude() {
		return exclude;
	}
}
