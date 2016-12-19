package com.harlan.smonitor.monitor.bean.http.check;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.core.job.http.CheckBodyServiceImpl;
import org.quartz.Job;

public class CheckBody extends CheckItem {

	private List<String> contains;
	
	private List<String> exclude;
	@SuppressWarnings("unchecked")
	public void getAttrs(Map<String,Object> checkMap) {
		if(checkMap.get("contains")!=null){
			contains= (List<String>) checkMap.get("contains");
		}
		if(checkMap.get("exclude")!=null){
			exclude= (List<String>) checkMap.get("exclude");
		}
	}

	@Override
	public List<FieldDeclare> getFields() {
		List<FieldDeclare> fieldList=new ArrayList<FieldDeclare>();
		fieldList.add(new FieldDeclare("contains","包含","包含某个字符串"));
		fieldList.add(new FieldDeclare("exclude","不包含","不包含某个字符串"));
		return fieldList;
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
