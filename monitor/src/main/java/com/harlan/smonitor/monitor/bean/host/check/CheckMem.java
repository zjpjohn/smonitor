package com.harlan.smonitor.monitor.bean.host.check;

import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.core.job.host.CheckMemServiceImpl;
import org.quartz.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CheckMem extends CheckItem {

	public void getAttrs(Map<String,Object> checkMap) {
		this.exceed=Integer.valueOf(checkMap.get("exceed").toString());
	}

	@Override
	public List<FieldDeclare> getFields() {
		List<FieldDeclare> fieldList=new ArrayList<FieldDeclare>();
		fieldList.add(new FieldDeclare("exceed","内存阀值","超过报警"));
		return fieldList;
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
