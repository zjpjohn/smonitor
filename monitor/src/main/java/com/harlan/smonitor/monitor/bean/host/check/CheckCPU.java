package com.harlan.smonitor.monitor.bean.host.check;

import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.core.job.host.CheckCPUServiceImpl;
import org.quartz.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CheckCPU extends CheckItem {

	private Double exceed;

	public void getAttrs(Map<String,Object> checkMap) {
		this.exceed = Double.valueOf(checkMap.get("exceed").toString().trim());
	}

	@Override
	public List<FieldDeclare> getFields() {
		List<FieldDeclare> fieldList=new ArrayList<FieldDeclare>();
		fieldList.add(new FieldDeclare("exceed","cpu阀值","规则：cpu超过这个值"));
		return fieldList;
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
